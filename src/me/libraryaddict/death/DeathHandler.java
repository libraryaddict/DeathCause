package me.libraryaddict.death;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DeathHandler {
    private static DeathHandlerListener listener = new DeathHandlerListener();
    static {
        Plugin plugin = null;
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if (p.isEnabled()) {
                plugin = p;
                break;
            }
        }
        final Plugin p = plugin;
        Bukkit.getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
            public void run() {
                Bukkit.getPluginManager().registerEvents(listener, p);
            }
        });
    }

    /**
     * Just a method that instilizes static stuff
     */
    public static void initialize() {
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
            damage.addAll(listener.getDamages().get(player));
        }
        Iterator<Damage> itel = damage.iterator();
        while (itel.hasNext()) {
            if (itel.next().getWhen() < cutoffTime) {
                itel.remove();
            }
        }
        return damage;
    }

    public static DeathCause getDeathCause(Player entity) {
        if (listener.getDamages().containsKey(entity)) {
            return listener.getDamages().get(entity).get(0).getCause();
        }
        return DeathCause.UNKNOWN;
    }

    public static Damage getLastDamage(Player player) {
        if (listener.getDamages().containsKey(player)) {
            return listener.getDamages().get(player).get(0);
        }
        return new Damage(DeathCause.UNKNOWN, 0, null);
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
                double dmg = damage.getDamage();
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
        HashMap<Player, Double> damageDivided = new HashMap<Player, Double>();
        for (Player p : damageDealt.keySet()) {
            damageDivided.put(p, damageDealt.get(p) / totalDamage);
        }
        return damageDivided;
    }
}
