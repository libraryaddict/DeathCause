package me.libraryaddict.death.causes;

import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import me.libraryaddict.death.DeathCause;

public class DeathCauseAnvil extends DeathCause {

    @Override
    public String getDeathMessage(LivingEntity entity, Object damager) {
        return getDeathMessage().replace("%Killed%", getName(entity));
    }

    @Override
    public Object getKiller(EntityDamageEvent event) {
        return null;
    }

    @Override
    public boolean isCauseOfDeath(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
            if (entityEvent.getDamager() instanceof FallingBlock && event.getDamage() > 0) {
                FallingBlock block = (FallingBlock) entityEvent.getDamager();
                return block.getBlockId() == Material.ANVIL.getId();
            }
        }
        return false;
    }

}
