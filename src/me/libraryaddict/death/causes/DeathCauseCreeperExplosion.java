package me.libraryaddict.death.causes;

import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DeathCauseCreeperExplosion extends DeathCauseExplosion {


    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        if (event.getCause() == DamageCause.ENTITY_EXPLOSION) {
            if (event instanceof EntityDamageByEntityEvent) {
                return getKiller(event) instanceof Creeper;
            }
        }
        return false;
    }

}
