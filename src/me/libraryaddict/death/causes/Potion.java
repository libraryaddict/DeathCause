package me.libraryaddict.death.causes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.libraryaddict.death.DeathCause;

public class Potion extends DeathCause {

    @Override
    public boolean isCauseOfDeath(LivingEntity entity) {
        return entity.getLastDamageCause().getCause() == DamageCause.MAGIC;
    }

    @Override
    public String getDeathMessage(LivingEntity entity) {
        return getMessage().replace("%Killed%", getName(entity)).replace("%Killer%", getName(getKiller(entity)));
    }

    @Override
    public Object getKiller(LivingEntity entity) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) entity.getLastDamageCause();
        ThrownPotion potion = (ThrownPotion) event.getDamager();
        return potion.getShooter();
    }

}
