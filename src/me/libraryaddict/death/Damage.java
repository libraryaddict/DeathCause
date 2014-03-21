package me.libraryaddict.death;

import org.bukkit.entity.Player;

public class Damage {
    private double damageAmount;
    private DeathCause damageCause;
    private long damageOccured;
    private Object damager;

    public Damage(DeathCause cause, double damageAmount, Object damager) {
        damageOccured = System.currentTimeMillis();
        this.damager = damager;
        this.damageAmount = damageAmount;
        this.damageCause = cause;
    }

    public DeathCause getCause() {
        return damageCause;
    }

    public double getDamage() {
        return damageAmount;
    }

    public Object getDamager() {
        return damager;
    }

    public long getWhen() {
        return damageOccured;
    }

    public boolean isPlayerDealt() {
        return damager != null && damager instanceof Player;
    }

    public void setCause(DeathCause cause) {
        damageCause = cause;
    }

    public void setDamager(Object damager) {
        this.damager = damager;
    }
}
