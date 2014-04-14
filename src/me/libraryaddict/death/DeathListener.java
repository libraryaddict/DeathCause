package me.libraryaddict.death;

import org.bukkit.entity.Player;

public abstract class DeathListener {
    private static DeathHandlerListener deathListener;

    public static void setListener(DeathHandlerListener listener) {
        deathListener = listener;
    }

    public abstract void checkDamages(Player p);

    public abstract void onDamage(Player p, Damage damage);

    public DeathHandlerListener getListener() {
        return deathListener;
    }
}
