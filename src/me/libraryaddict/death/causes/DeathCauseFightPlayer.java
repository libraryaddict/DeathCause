package me.libraryaddict.death.causes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DeathCauseFightPlayer extends DeathCauseFight {

    public Entity getKiller(EntityDamageEvent event) {
        return ((EntityDamageByEntityEvent) event).getDamager();
    }

    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        if (event.getCause() == DamageCause.ENTITY_ATTACK) {
            return getKiller(event) instanceof Player;
        }
        return false;
    }

}
