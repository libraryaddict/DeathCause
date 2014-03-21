package me.libraryaddict.death;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DeathHandlerListener implements Listener {
    private HashMap<Player, ArrayList<Damage>> damageCauses = new HashMap<Player, ArrayList<Damage>>();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && !event.isCancelled()) {
            DeathCause cause = DeathCause.getDeathCause(event);
            if (cause != DeathCause.UNKNOWN || event.getDamage() != 0) {
                if (damageCauses.containsKey(event.getEntity())) {
                    double hp = ((Player) event.getEntity()).getMaxHealth() - ((Player) event.getEntity()).getHealth();
                    Iterator<Damage> itel = damageCauses.get(event.getEntity()).iterator();
                    while (itel.hasNext()) {
                        Damage dmg = itel.next();
                        if (hp <= 0) {
                            itel.remove();
                        } else {
                            hp -= dmg.getDamage();
                        }
                    }
                } else {
                    damageCauses.put((Player) event.getEntity(), new ArrayList<Damage>());
                }
                damageCauses.get(event.getEntity()).add(0, new Damage(cause, event.getDamage(), cause.getKiller(event)));
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
