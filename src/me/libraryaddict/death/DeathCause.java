package me.libraryaddict.death;

import java.util.ArrayList;
import java.util.Random;

import me.libraryaddict.death.causes.*;
import me.libraryaddict.death.causes.Void;

import org.apache.commons.lang.StringUtils;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

public abstract class DeathCause {
    public static DeathCause ANVIL = new Anvil();
    public static DeathCause CACTUS = new Cactus();
    public static DeathCause CREEPER_EXPLOSION = new CreeperExplosion();
    public static DeathCause DROWN = new Drown();
    public static DeathCause EXPLODED = new Explosion();
    public static DeathCause FALL = new Fall();
    public static DeathCause FIGHT = new Fight();
    public static DeathCause FIRE = new Fire();
    public static DeathCause LAVA = new Lava();
    public static DeathCause LIGHTNING = new Lightning();
    public static DeathCause POTION = new Potion();
    public static DeathCause SHOT = new Shot();
    public static DeathCause STARVE = new Starve();
    public static DeathCause SUFFOCATION = new Suffocation();
    public static DeathCause SUICIDE = new Suicide();
    public static DeathCause UNKNOWN = new Unknown();
    public static DeathCause VOID = new Void();
    public static DeathCause WITHER = new Wither();
    private static ArrayList<DeathCause> deathCauses = new ArrayList<DeathCause>();
    private ArrayList<String> deathMessages = new ArrayList<String>();
    static {
        ANVIL.registerDeathMessage("%Killed% was squashed by a anvil");
        CACTUS.registerDeathMessage("%Killed% was pricked to death");
        CREEPER_EXPLOSION.registerDeathMessage("%Killed% was blown up by %Killer%");
        DROWN.registerDeathMessage("%Killed% forgot to swim");
        EXPLODED.registerDeathMessage("%Killed% got a mouthful of explosions");
        FALL.registerDeathMessage("%Killed% fell to their death");
        FIGHT.registerDeathMessage("%Killed% was slain by %Killer%");
        FIRE.registerDeathMessage("%Killed% burned to death");
        LAVA.registerDeathMessage("%Killed% was cooked in lava");
        LIGHTNING.registerDeathMessage("%Killed% was shocked by lightning");
        POTION.registerDeathMessage("%Killed% took %Killer%'s potion to the face");
        SHOT.registerDeathMessage("%Killed% was shot by %Killer%");
        STARVE.registerDeathMessage("%Killed% starved to death (somehow)");
        SUFFOCATION.registerDeathMessage("%Killed% suffocated to death");
        SUICIDE.registerDeathMessage("%Killed% pressed the suicide button despite pleas from friends and family");
        VOID.registerDeathMessage("%Killed% fell into the void");
        WITHER.registerDeathMessage("%Killed% sucked on a vial of wither poison");
        UNKNOWN.registerDeathMessage("%Killed% died by unknown means");
        deathCauses.add(DeathCause.ANVIL);
        deathCauses.add(DeathCause.CACTUS);
        deathCauses.add(DeathCause.CREEPER_EXPLOSION);
        deathCauses.add(DeathCause.DROWN);
        deathCauses.add(DeathCause.EXPLODED);
        deathCauses.add(DeathCause.FALL);
        deathCauses.add(DeathCause.FIGHT);
        deathCauses.add(DeathCause.FIRE);
        deathCauses.add(DeathCause.LAVA);
        deathCauses.add(DeathCause.LIGHTNING);
        deathCauses.add(DeathCause.POTION);
        deathCauses.add(DeathCause.SHOT);
        deathCauses.add(DeathCause.STARVE);
        deathCauses.add(DeathCause.SUFFOCATION);
        deathCauses.add(DeathCause.SUICIDE);
        deathCauses.add(DeathCause.VOID);
        deathCauses.add(DeathCause.WITHER);
        deathCauses.add(DeathCause.UNKNOWN);
    }

    public static void registerDeathCause(DeathCause cause) {
        deathCauses.add(0, cause);
    }

    public static DeathCause getDeathCause(LivingEntity entity) {
        if (entity.getLastDamageCause() == null) {
            System.out.println("[DeathCause] Cannot find the death cause for " + entity + " as there is no damage event");
            return DeathCause.UNKNOWN;
        }
        for (DeathCause cause : deathCauses) {
            if (cause.isCauseOfDeath(entity.getLastDamageCause())) {
                return cause;
            }
        }
        System.out.println("[DeathCause] Cannot find the death cause for " + entity);
        return DeathCause.UNKNOWN;
    }

    public void registerDeathMessage(String message) {
        deathMessages.add(message);
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    protected String getMessage() {
        if (deathMessages.isEmpty()) {
            throw new RuntimeException("No death messages found in DeathCause " + getName());
        }
        return deathMessages.get(new Random().nextInt(deathMessages.size()));
    }

    public void clearDeathMessages() {
        deathMessages.clear();
    }

    protected String getName(Object obj) {
        if (obj instanceof Entity) {
            Entity entity = (Entity) obj;
            if (entity instanceof HumanEntity) {
                return ((HumanEntity) entity).getName();
            }
            if (entity instanceof LivingEntity && ((LivingEntity) entity).getCustomName() != null
                    && ((LivingEntity) entity).getCustomName().length() > 0) {
                return ((LivingEntity) entity).getCustomName();
            }
            String[] names = entity.getType().name().split("_");
            for (int i = 0; i < names.length; i++) {
                names[i] = names[i].substring(0, 1) + names[i].substring(1);
            }
            return StringUtils.join(names, " ");
        } else if (obj instanceof Dispenser) {
            return "Dispenser";
        }
        return obj.toString();
    }

    public abstract String getDeathMessage(LivingEntity entity, Object damager);

    public abstract Object getKiller(EntityDamageEvent event);

    public abstract boolean isCauseOfDeath(EntityDamageEvent event);

}
