package me.libraryaddict.death.causes;

import me.libraryaddict.death.DeathCause;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DeathCauseThorns extends DeathCause {

    public Entity getKiller(EntityDamageEvent event) {
        return ((EntityDamageByEntityEvent) event).getDamager();
    }

    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        return event.getCause() == DamageCause.THORNS;
    }

}
