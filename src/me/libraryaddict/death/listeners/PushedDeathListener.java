package me.libraryaddict.death.listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.libraryaddict.death.Damage;
import me.libraryaddict.death.DeathCause;
import me.libraryaddict.death.DeathListener;
import me.libraryaddict.death.causes.DeathCauseFight;
import me.libraryaddict.death.causes.DeathCauseProjectile;

public class PushedDeathListener extends DeathListener implements Listener {

    private HashMap<Player, Damage> pushed = new HashMap<Player, Damage>();

    @Override
    public void checkDamages(Player p) {
        if (pushed.containsKey(p) && !getListener().getDamages().get(p).contains(pushed.get(p))) {
            pushed.remove(p);
        }
    }

    @Override
    public boolean canRemove(Player p, Damage damage) {
        return !pushed.containsValue(damage);
    }

    @Override
    public void onDamage(Player p, Damage newDamage) {
        // If he was knocked around
        if (pushed.containsKey(p)) {
            // If the new damage is fall or void
            if (newDamage.getCause() == DeathCause.FALL || newDamage.getCause() == DeathCause.VOID) {
                Damage oldCause = pushed.remove(p);
                if (oldCause.getCause() instanceof DeathCauseFight) {
                    if (newDamage.getCause() == DeathCause.FALL) {
                        newDamage.setCause(DeathCause.PUSHED_FALL);
                    } else {
                        newDamage.setCause(DeathCause.PUSHED_VOID);
                    }
                } else if (oldCause.getCause() instanceof DeathCauseProjectile) {
                    if (newDamage.getCause() == DeathCause.FALL) {
                        newDamage.setCause(DeathCause.SHOT_FALL);
                    } else {
                        newDamage.setCause(DeathCause.SHOT_VOID);
                    }
                }
                newDamage.setDamager(oldCause.getDamager());
            }
        }
        if (!p.getAllowFlight() && (!pushed.containsKey(p) || p.getFallDistance() == 0 || p.isOnGround())) {
            if (newDamage.getCause() instanceof DeathCauseFight || newDamage.getCause() instanceof DeathCauseProjectile) {
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
        Player p = event.getPlayer();
        if (pushed.containsKey(p)) {
            if ((p.getFallDistance() == 0 || p.isOnGround())
                    && pushed.get(p).getWhen() + (p.getVelocity().length() < 0.05 ? 900 : 3000) < System.currentTimeMillis()) {
                pushed.remove(p);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        getListener().getDamages().remove(event.getPlayer());
        pushed.remove(event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        getListener().getDamages().remove(event.getEntity());
        pushed.remove(event.getEntity());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        getListener().getDamages().remove(event.getPlayer());
        pushed.remove(event.getPlayer());
    }
}
