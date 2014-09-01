package me.libraryaddict.death;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import me.libraryaddict.death.listeners.PushedDeathListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class DeathHandler {
    private static DeathHandlerListener listener = new DeathHandlerListener();
    private static DeathCheck deathCheck = new DeathCheck() {

        @Override
        public boolean isValid(Player player) {
            return true;
        }

    };

    public static void setDeathCheck(DeathCheck newDeathCheck) {
        deathCheck = newDeathCheck;
    }

    /**
     * Get all the killers of this player, with the percentage of damage they are responsible for out of 1. 1 meaning they dealt
     * all the damage
     */
    public static HashMap<Player, Double> getDamageResponsibility(Player player) {
        HashMap<Player, Double> damageDealt = new HashMap<Player, Double>();
        double totalDamage = 0;
        for (Damage damage : getDamagers(player)) {
            if (damage.isPlayerDealt()) {
                Player p = (Player) damage.getDamager();
                if (p.isOnline() && deathCheck.isValid(p)) {
                    double dmg = damage.getDamage();
                    if (dmg == 0)
                        continue;
                    if (dmg < 0.1)
                        dmg = 0.1;
                    if (!damageDealt.containsKey(p)) {
                        damageDealt.put(p, dmg);
                    } else {
                        damageDealt.put(p, damageDealt.get(p) + dmg);
                    }
                    totalDamage += dmg;
                }
            }
        }
        HashMap<Player, Double> damageDivided = new HashMap<Player, Double>();
        for (Player p : damageDealt.keySet()) {
            damageDivided.put(p, damageDealt.get(p) / totalDamage);
        }
        return damageDivided;
    }

    public static ArrayList<Damage> getDamagers(Player player) {
        if (listener.getDamages().containsKey(player)) {
            return listener.getDamages().get(player);
        }
        return new ArrayList<Damage>();
    }

    /**
     * All damages must have happened after this time
     */
    public static ArrayList<Damage> getDamagers(Player player, long cutoffTime) {
        ArrayList<Damage> damage = new ArrayList<Damage>();
        if (listener.getDamages().containsKey(player)) {
            damage = new ArrayList<Damage>(listener.getDamages().get(player));
        }
        Iterator<Damage> itel = damage.iterator();
        while (itel.hasNext()) {
            if (itel.next().getWhen() < cutoffTime) {
                itel.remove();
            }
        }
        return damage;
    }

    public static DeathCause getDeathCause(Player player) {
        if (listener.getDamages().containsKey(player)) {
            return listener.getDamages().get(player).get(0).getCause();
        }
        return DeathCause.UNKNOWN;
    }

    public static Player getKiller(Player player) {
        for (Damage damage : getDamagers(player)) {
            if (damage.isPlayerDealt()) {
                return (Player) damage.getDamager();
            }
        }
        return null;
    }

    public static Player getKiller(Player player, long maxSecondsAgo) {
        for (Damage damage : getDamagers(player)) {
            if ((damage.getWhen() + (maxSecondsAgo * 1000)) >= System.currentTimeMillis() && damage.isPlayerDealt()) {
                return (Player) damage.getDamager();
            }
        }
        return null;
    }

    public static void addDamage(Player player, Damage damage) {
        if (!listener.getDamages().containsKey(player)) {
            listener.getDamages().put(player, new ArrayList<Damage>());
        }
        listener.getDamages().get(player).add(0, damage);
    }

    public static Damage getLastDamage(Player player) {
        if (listener.getDamages().containsKey(player)) {
            return listener.getDamages().get(player).get(0);
        }
        return new Damage(DeathCause.UNKNOWN, 0, null);
    }

    public static void registerListener(DeathListener deathListener) {
        if (deathListener instanceof Listener) {
            Plugin plugin = null;
            for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                if (p.isEnabled()) {
                    plugin = p;
                    break;
                }
            }
            Bukkit.getPluginManager().registerEvents((Listener) deathListener, plugin);
        }
        listener.addListener(deathListener);
    }

    /**
     * Just a method that initializes static stuff
     */
    public static void initialize(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        PushedDeathListener listen = new PushedDeathListener();
        registerListener(listen);
        Bukkit.getPluginManager().registerEvents(listen, plugin);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                listener.checkDamages();
            }
        }, 40, 40);
        DeathListener.setListener(listener);
    }
}
