package me.libraryaddict.death.causes;

import org.bukkit.event.entity.EntityDamageEvent;

import me.libraryaddict.death.DeathCause;

/**
 * Used when isCauseOfDeath does not matter. getKiller does not matter. Just needs a object to fed.
 */
public class DeathCauseGeneric extends DeathCause {


    @Override
    public Object getKiller(EntityDamageEvent event) {
        return null;
    }

    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        return false;
    }

}
