package me.libraryaddict.death;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import me.libraryaddict.damageapi.DamageApi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DeathHandlerListener implements Listener {
    private HashMap<Player, ArrayList<Damage>> damageCauses = new HashMap<Player, ArrayList<Damage>>();
    private ArrayList<DeathListener> listeners = new ArrayList<DeathListener>();

    public void addListener(DeathListener listener) {
        listeners.add(listener);
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
        boolean remove = false;
        while (itel.hasNext()) {
            Damage dmg = itel.next();
            if (hp == 0) {
                if (dmg.getDamage() > 0 || remove) {
                    itel.remove();
                    remove = true;
                }
            } else {
                hp -= dmg.getDamage();
                if (hp < 0) {
                    hp = 0;
                }
            }
        }
        for (DeathListener listener : listeners) {
            listener.checkDamages(p);
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
                for (DeathListener listener : listeners) {
                    listener.onDamage(p, newDamage);
                }
            }
        }
    }

}
