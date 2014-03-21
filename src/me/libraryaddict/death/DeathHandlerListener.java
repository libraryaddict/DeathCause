package me.libraryaddict.death;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import me.libraryaddict.damageapi.DamageApi;
import me.libraryaddict.death.causes.DeathCauseFight;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathHandlerListener implements Listener {
    private HashMap<Player, ArrayList<Damage>> damageCauses = new HashMap<Player, ArrayList<Damage>>();
    private HashMap<Player, Damage> pushed = new HashMap<Player, Damage>();

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        damageCauses.remove(event.getPlayer());
    }

    public void checkDamages() {
        Iterator<Player> pItel = damageCauses.keySet().iterator();
        while (pItel.hasNext()) {
            Player p = pItel.next();
            checkDamages(p);
            if (damageCauses.get(p).isEmpty()) {
                pItel.remove();
            }
        }
    }

    public void checkDamages(Player p) {
        double hp = p.getMaxHealth() - p.getHealth();
        Iterator<Damage> itel = damageCauses.get(p).iterator();
        while (itel.hasNext()) {
            Damage dmg = itel.next();
            if (hp <= 0) {
                itel.remove();
            } else {
                hp -= dmg.getDamage();
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerDeathEvent event) {
        damageCauses.remove(event.getEntity());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (pushed.containsKey(event.getPlayer())) {
            if (event.getPlayer().getFallDistance() == 0) {
                pushed.remove(event.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && !event.isCancelled()) {
            DeathCause cause = DeathCause.getDeathCause(event);
            if (cause != DeathCause.UNKNOWN || event.getDamage() != 0) {
                Player p = (Player) event.getEntity();
                if (damageCauses.containsKey(p)) {
                    checkDamages(p);
                } else {
                    damageCauses.put(p, new ArrayList<Damage>());
                }
                double damage = DamageApi.calculateDamageAddArmor(p, event.getCause(), event.getDamage());
                Damage newDamage = new Damage(cause, damage, cause.getKiller(event));
                damageCauses.get(p).add(0, newDamage);
                if (pushed.containsKey(p)) {
                    if (newDamage.getCause() == DeathCause.FALL) {
                        newDamage.setCause(DeathCause.PUSHED_FALL);
                        newDamage.setDamager(pushed.remove(p).getDamager());
                    } else if (p.getFallDistance() == 0) {
                        pushed.remove(p);
                    }
                }
                if (!p.getAllowFlight() && p.getFallDistance() == 0) {
                    if (newDamage.getCause() instanceof DeathCauseFight || newDamage.getCause() == DeathCause.SHOT) {
                        pushed.put(p, newDamage);
                    }
                }
            }
        }
    }

    public HashMap<Player, ArrayList<Damage>> getDamages() {
        return damageCauses;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        damageCauses.remove(event.getPlayer());
    }

}
