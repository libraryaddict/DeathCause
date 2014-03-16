package me.libraryaddict.death.causes;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.libraryaddict.death.DeathCause;

public class CreeperExplosion extends Explosion {

    @Override
    public boolean isCauseOfDeath(LivingEntity entity) {
        if (entity.getLastDamageCause().getCause() == DamageCause.ENTITY_EXPLOSION) {
            if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                return getKiller(entity) instanceof Creeper;
            }
        }
        return false;
    }

    @Override
    public String getDeathMessage(LivingEntity entity) {
        return getMessage().replace("%Killed%", getName(entity));
    }

}
