package me.libraryaddict.death.causes;

import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.libraryaddict.death.DeathCause;

public class DeathCausePotion extends DeathCause {

 
    @Override
    public Object getKiller(EntityDamageEvent event) {
        EntityDamageByEntityEvent eventDamage = (EntityDamageByEntityEvent) event;
        ThrownPotion potion = (ThrownPotion) eventDamage.getDamager();
        return potion.getShooter();
    }

    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        return event.getCause() == DamageCause.MAGIC;
    }

}
