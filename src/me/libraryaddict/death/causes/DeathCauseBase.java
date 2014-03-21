package me.libraryaddict.death.causes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

import me.libraryaddict.death.DeathCause;

/**
 * Used when isCauseOfDeath does not matter. getKiller does not matter. Just needs a object to fed.
 */
public class DeathCauseBase extends DeathCause {

    @Override
    public String getDeathMessage(LivingEntity entity, Object damager) {
        return getDeathMessage().replace("%Killed%", getName(entity)).replace("%Killer%", getName(damager));
    }

    @Override
    public Object getKiller(EntityDamageEvent event) {
        return null;
    }

    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        return false;
    }

}
