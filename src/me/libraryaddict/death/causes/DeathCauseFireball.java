package me.libraryaddict.death.causes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import me.libraryaddict.death.DeathCause;

public class DeathCauseFireball extends DeathCause {

    @Override
    public Entity getKiller(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager != null && damager instanceof Fireball && ((Fireball) damager).getShooter() instanceof Entity) {
                return (Entity) ((Fireball) damager).getShooter();
            }
        }
        return null;
    }

    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager != null && damager instanceof Fireball) {
                return true;
            }
        }
        return false;
    }
}
