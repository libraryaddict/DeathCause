package me.libraryaddict.death.causes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Fight extends Explosion {

    @Override
    public boolean isCauseOfDeath(LivingEntity entity) {
        if (entity.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK) {
            return getKiller(entity) instanceof LivingEntity;
        }
        return false;
    }

    @Override
    public String getDeathMessage(LivingEntity entity) {
        return getMessage().replace("%Killed%", getName(entity)).replace("%Killer%", getName(getKiller(entity)));
    }

    public Entity getKiller(LivingEntity entity) {
        return ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager();
    }

}
