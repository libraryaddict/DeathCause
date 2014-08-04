package me.libraryaddict.death.causes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.libraryaddict.death.DeathCause;

public class DeathCauseProjectile extends DeathCause {

    @Override
    public Entity getKiller(EntityDamageEvent event) {
        org.bukkit.entity.Projectile projectile = (org.bukkit.entity.Projectile) ((EntityDamageByEntityEvent) event).getDamager();
        if (projectile.getShooter() != null && projectile.getShooter() instanceof Entity) {
            return (Entity) projectile.getShooter();
        }
        return null;
    }

    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent eventDamage = (EntityDamageByEntityEvent) event;
            if (eventDamage.getDamager() instanceof Projectile && event.getDamage() > 0) {
                return true;
            }
        }
        return false;
    }
}
