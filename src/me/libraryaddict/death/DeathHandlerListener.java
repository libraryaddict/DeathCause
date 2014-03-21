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
        if (pushed.containsKey(p) && !damageCauses.get(p).contains(pushed.get(p))) {
            pushed.remove(p);
        }
    }

    public HashMap<Player, ArrayList<Damage>> getDamages() {
        return damageCauses;
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
                    if (newDamage.getCause() == DeathCause.FALL || newDamage.getCause() == DeathCause.VOID) {
                        Damage oldCause = pushed.remove(p);
                        if (oldCause.getCause() instanceof DeathCauseFight) {
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
                    if (newDamage.getCause() instanceof DeathCauseFight || newDamage.getCause() == DeathCause.SHOT) {
                        pushed.put(p, newDamage);
                    }
                }
                if (newDamage.getCause() == DeathCause.VOID) {
                    if (damageCauses.get(p).size() > 1) {
                        Damage dmg = damageCauses.get(p).get(1);
                        if (dmg.getCause() == DeathCause.PUSHED_VOID || dmg.getCause() == DeathCause.SHOT_VOID) {
                            newDamage.setCause(dmg.getCause());
                            newDamage.setDamager(dmg.getDamager());
                        }
                    }
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
        damageCauses.remove(event.getPlayer());
        pushed.remove(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerDeathEvent event) {
        damageCauses.remove(event.getEntity());
        pushed.remove(event.getEntity());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        damageCauses.remove(event.getPlayer());
        pushed.remove(event.getPlayer());
    }

}
