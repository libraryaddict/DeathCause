package me.libraryaddict.death.listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.libraryaddict.death.Damage;
import me.libraryaddict.death.DeathCause;
import me.libraryaddict.death.DeathHandler;
import me.libraryaddict.death.DeathListener;

public class PushedDeathListener extends DeathListener {

    private HashMap<Player, Damage> pushed = new HashMap<Player, Damage>();

    @Override
    public void checkDamages(Player p) {
        if (pushed.containsKey(p) && !getListener().getDamages().get(p).contains(pushed.get(p))) {
            pushed.remove(p);
        }
    }

    @Override
    public void onDamage(Player p, Damage newDamage) {
        if (pushed.containsKey(p)) {
            if (newDamage.getCause() == DeathCause.FALL || newDamage.getCause() == DeathCause.VOID) {
                Damage oldCause = pushed.remove(p);
                if (oldCause.getCause() == DeathCause.FIGHT) {
                    if (newDamage.getCause() == DeathCause.FALL) {
                        newDamage.setCause(DeathCause.PUSHED_FALL);
                    } else {
                        newDamage.setCause(DeathCause.PUSHED_VOID);
                    }
                } else {
                    if (newDamage.getCause() == DeathCause.FALL) {
                        newDamage.setCause(DeathCause.SHOT_FALL);
                    } else {
                        newDamage.setCause(DeathCause.SHOT_VOID);
                    }
                }
                newDamage.setDamager(oldCause.getDamager());
            } else if (p.getFallDistance() == 0) {
                pushed.remove(p);
            }
        }
        if (!p.getAllowFlight() && (!pushed.containsKey(p) || p.getFallDistance() == 0)) {
            if (newDamage.getCause() == DeathCause.FIGHT || newDamage.getCause() == DeathCause.SHOT) {
                pushed.put(p, newDamage);
            }
        }
        if (newDamage.getCause() == DeathCause.VOID) {
            if (getListener().getDamages().get(p).size() > 1) {
                Damage dmg = getListener().getDamages().get(p).get(1);
                if (dmg.getCause() == DeathCause.PUSHED_VOID || dmg.getCause() == DeathCause.SHOT_VOID) {
                    newDamage.setCause(dmg.getCause());
                    newDamage.setDamager(dmg.getDamager());
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (pushed.containsKey(event.getPlayer())) {
            if (event.getPlayer().getFallDistance() == 0 && event.getPlayer().isOnGround()
                    && DeathHandler.getLastDamage(event.getPlayer()).getWhen() + 500 < System.currentTimeMillis()) {
                pushed.remove(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        getListener().getDamages().remove(event.getPlayer());
        pushed.remove(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerDeathEvent event) {
        getListener().getDamages().remove(event.getEntity());
        pushed.remove(event.getEntity());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        getListener().getDamages().remove(event.getPlayer());
        pushed.remove(event.getPlayer());
    }
}
