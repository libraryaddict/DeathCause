package me.libraryaddict.death.causes;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

import me.libraryaddict.death.DeathCause;

public class DeathCauseUnknown extends DeathCause {

    @Override
    public Entity getKiller(EntityDamageEvent event) {
        return null;
    }

    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        return true;
    }

}
