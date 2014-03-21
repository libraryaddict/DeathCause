package me.libraryaddict.death.causes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Fight extends Explosion {

    @Override
    public String getDeathMessage(LivingEntity entity, Object damager) {
        return getMessage().replace("%Killed%", getName(entity)).replace("%Killer%", getName(damager));
    }

    public Entity getKiller(EntityDamageEvent event) {
        return ((EntityDamageByEntityEvent) event).getDamager();
    }

    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        if (event.getCause() == DamageCause.ENTITY_ATTACK) {
            return getKiller(event) instanceof LivingEntity;
        }
        return false;
    }

}
