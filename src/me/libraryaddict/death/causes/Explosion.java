package me.libraryaddict.death.causes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.libraryaddict.death.DeathCause;

public class Explosion extends DeathCause {

    @Override
    public boolean isCauseOfDeath(LivingEntity entity) {
        return entity.getLastDamageCause().getCause() == DamageCause.ENTITY_EXPLOSION
                || entity.getLastDamageCause().getCause() == DamageCause.BLOCK_EXPLOSION;
    }

    @Override
    public String getDeathMessage(LivingEntity entity) {
        return getMessage().replace("%Killed%", getName(entity));
    }

    @Override
    public Object getKiller(LivingEntity entity) {
        return ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager();
    }
}
