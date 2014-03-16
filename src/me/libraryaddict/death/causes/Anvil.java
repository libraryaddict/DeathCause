package me.libraryaddict.death.causes;

import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.libraryaddict.death.DeathCause;

public class Anvil extends DeathCause {

    @Override
    public boolean isCauseOfDeath(LivingEntity entity) {
        if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) entity.getLastDamageCause();
            if (event.getDamager() instanceof FallingBlock && event.getDamage() > 0) {
                FallingBlock block = (FallingBlock) event.getDamager();
                return block.getBlockId() == Material.ANVIL.getId();
            }
        }
        return false;
    }

    @Override
    public String getDeathMessage(LivingEntity entity) {
        return getMessage().replace("%Killed%", getName(entity));
    }

    @Override
    public Object getKiller(LivingEntity entity) {
        return null;
    }

}
