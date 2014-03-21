package me.libraryaddict.death.causes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.libraryaddict.death.DeathCause;

public class DeathCausePushedFall extends DeathCause {

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
        return event.getCause() == DamageCause.FALL;
    }

}