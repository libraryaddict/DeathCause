package me.libraryaddict.death.causes;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.libraryaddict.death.DeathCause;

public class DeathCauseExplosion extends DeathCause {



    @Override
    public Object getKiller(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent)
            return ((EntityDamageByEntityEvent) event).getDamager();
        return null;
    }

    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        return event.getCause() == DamageCause.ENTITY_EXPLOSION || event.getCause() == DamageCause.BLOCK_EXPLOSION;
    }
}
