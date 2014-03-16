package me.libraryaddict.death.causes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.libraryaddict.death.DeathCause;

public class Shot extends DeathCause {

    @Override
    public boolean isCauseOfDeath(LivingEntity entity) {
        if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) entity.getLastDamageCause();
            if (event.getDamager() instanceof Shot && event.getDamage() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDeathMessage(LivingEntity entity) {
        return getMessage().replace("%Killed%", getName(entity));
    }

    @Override
    public Object getKiller(LivingEntity entity) {
        org.bukkit.entity.Projectile projectile = (org.bukkit.entity.Projectile) ((EntityDamageByEntityEvent) entity
                .getLastDamageCause()).getDamager();
        if (projectile.getShooter() != null) {
            return projectile.getShooter();
        }
        return null;
    }
}
