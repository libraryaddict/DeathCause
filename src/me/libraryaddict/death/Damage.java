package me.libraryaddict.death;

import org.bukkit.entity.Player;

public class Damage {
    private DeathCause damageCause;
    private long damageOccured;
    private Object damager;
    private double damageAmount;

    public Damage(DeathCause cause, double damageAmount, Object damager) {
        damageOccured = System.currentTimeMillis();
        this.damager = damager;
        this.damageAmount = damageAmount;
        this.damageCause = cause;
    }

    public void setCause(DeathCause cause) {
        damageCause = cause;
    }

    public void setDamager(Object damager) {
        this.damager = damager;
    }

    public double getDamage() {
        return damageAmount;
    }

    public boolean isPlayerDealt() {
        return damager != null && damager instanceof Player;
    }

    public DeathCause getCause() {
        return damageCause;
    }

    public Object getDamager() {
        return damager;
    }

    public long getWhen() {
        return damageOccured;
    }
}
