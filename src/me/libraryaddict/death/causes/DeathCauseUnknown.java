package me.libraryaddict.death.causes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import me.libraryaddict.death.DeathCause;

public class DeathCauseUnknown extends DeathCause {

    @Override
    public String getDeathMessage(LivingEntity entity, Object damager) {
        return getDeathMessage().replace("%Killed%", getName(entity));
    }

    @Override
    public Object getKiller(EntityDamageEvent event) {
        return null;
    }

    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        return true;
    }

}
