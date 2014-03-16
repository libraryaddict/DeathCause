package me.libraryaddict.death.causes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.libraryaddict.death.DeathCause;

public class Drown extends DeathCause {

    @Override
    public boolean isCauseOfDeath(LivingEntity entity) {
        return entity.getLastDamageCause().getCause() == DamageCause.DROWNING;
    }

    @Override
    public String getDeathMessage(LivingEntity entity) {
        return getMessage().replace("%Killed%", getName(entity));
    }

    @Override
    public Object getKiller(LivingEntity entity) {
        return null;
    }
}
