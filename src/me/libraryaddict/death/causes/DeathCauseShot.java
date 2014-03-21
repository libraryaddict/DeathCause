package me.libraryaddict.death.causes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import me.libraryaddict.death.DeathCause;

public class DeathCauseShot extends DeathCause {

    @Override
    public String getDeathMessage(LivingEntity entity, Object damager) {
        return getDeathMessage().replace("%Killed%", getName(entity));
    }

    @Override
    public Object getKiller(EntityDamageEvent event) {
        org.bukkit.entity.Projectile projectile = (org.bukkit.entity.Projectile) ((EntityDamageByEntityEvent) event).getDamager();
        if (projectile.getShooter() != null) {
            return projectile.getShooter();
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
