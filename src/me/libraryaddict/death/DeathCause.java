package me.libraryaddict.death;

import java.util.ArrayList;
import java.util.Random;

import me.libraryaddict.death.causes.*;

import org.apache.commons.lang.StringUtils;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public abstract class DeathCause {
    public static DeathCause ANVIL = new DeathCauseAnvil();
    public static DeathCause CACTUS = new DeathCauseCactus();
    public static DeathCause CREEPER_EXPLOSION = new DeathCauseCreeperExplosion();
    public static DeathCause DROWN = new DeathCauseDrown();
    public static DeathCause ENDERPEARL = new DeathCauseEnderpearl();
    public static DeathCause SHOT_FALL = new DeathCauseGeneric();
    public static DeathCause SHOT_VOID = new DeathCauseGeneric();
    public static DeathCause EXPLODED = new DeathCauseExplosion();
    public static DeathCause FALL = new DeathCauseFall();
    public static DeathCause FIGHT = new DeathCauseFight();
    public static DeathCause FIRE = new DeathCauseFire();
    public static DeathCause LAVA = new DeathCauseLava();
    public static DeathCause LIGHTNING = new DeathCauseLightning();
    public static DeathCause POTION = new DeathCausePotion();
    public static DeathCause SHOT = new DeathCauseShot();
    public static DeathCause STARVE = new DeathCauseStarve();
    public static DeathCause PUSHED_FALL = new DeathCauseGeneric();
    public static DeathCause PUSHED_VOID = new DeathCauseGeneric();
    public static DeathCause SUFFOCATION = new DeathCauseSuffocation();
    public static DeathCause SUICIDE = new DeathCauseSuicide();
    public static DeathCause UNKNOWN = new DeathCauseUnknown();
    public static DeathCause VOID = new DeathCauseVoid();
    public static DeathCause WITHER = new DeathCauseWither();
    private static ArrayList<DeathCause> deathCauses = new ArrayList<DeathCause>();
    private ArrayList<String> deathMessages = new ArrayList<String>();
    static {
        ANVIL.registerDeathMessage("%Killed% was squashed by a anvil");
        CACTUS.registerDeathMessage("%Killed% was pricked to death");
        CREEPER_EXPLOSION.registerDeathMessage("%Killed% was blown up by %Killer%");
        DROWN.registerDeathMessage("%Killed% forgot to swim");
        ENDERPEARL.registerDeathMessage("%Killed% took too much damage using enderpearls");
        EXPLODED.registerDeathMessage("%Killed% got a mouthful of explosions");
        FALL.registerDeathMessage("%Killed% fell to their death");
        FIGHT.registerDeathMessage("%Killed% was slain by %Killer%");
        FIRE.registerDeathMessage("%Killed% burned to death");
        LAVA.registerDeathMessage("%Killed% was cooked in lava");
        LIGHTNING.registerDeathMessage("%Killed% was shocked by lightning");
        POTION.registerDeathMessage("%Killed% took %Killer%'s potion to the face");
        PUSHED_FALL.registerDeathMessage("%Killed% was doomed to fall by %Killer%");
        PUSHED_VOID.registerDeathMessage("%Killed% was knocked into the void by %Killer%");
        SHOT.registerDeathMessage("%Killed% was shot by %Killer%");
        SHOT_FALL.registerDeathMessage("%Killed% was shot off their ledge by %Killer%");
        SHOT_VOID.registerDeathMessage("%Killed% was shot into the void by %Killer%");
        STARVE.registerDeathMessage("%Killed% starved to death (somehow)");
        SUFFOCATION.registerDeathMessage("%Killed% suffocated to death");
        SUICIDE.registerDeathMessage("%Killed% pressed the suicide button");
        VOID.registerDeathMessage("%Killed% fell into the void");
        WITHER.registerDeathMessage("%Killed% sucked on a vial of wither poison");
        UNKNOWN.registerDeathMessage("%Killed% died by unknown means");
        deathCauses.add(DeathCause.ANVIL);
        deathCauses.add(DeathCause.CACTUS);
        deathCauses.add(DeathCause.CREEPER_EXPLOSION);
        deathCauses.add(DeathCause.DROWN);
        deathCauses.add(DeathCause.ENDERPEARL);
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
            entity.setLastDamageCause(new EntityDamageEvent(entity, DamageCause.CUSTOM, 0));
            // System.out.println("[DeathCause] Cannot find the death cause for " + entity + " as there is no damage event");
            // return DeathCause.UNKNOWN;
        }
        return getDeathCause(entity.getLastDamageCause());
    }

    public static DeathCause getDeathCause(EntityDamageEvent event) {
        for (DeathCause cause : deathCauses) {
            if (cause.isCauseOfDeath(event)) {
                return cause;
            }
        }
        System.out.println("[DeathCause] Cannot find the death cause for " + event.getEntity());
        return DeathCause.UNKNOWN;
    }

    public void registerDeathMessage(String... messages) {
        for (String message : messages) {
            deathMessages.add(message);
        }
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public String getDeathMessage() {
        if (deathMessages.isEmpty()) {
            throw new RuntimeException("No death messages found in DeathCause " + getName());
        }
        return deathMessages.get(new Random().nextInt(deathMessages.size()));
    }

    public void clearDeathMessages() {
        deathMessages.clear();
    }

    public static String getEntityName(Object obj) {
        if (obj == null) {
            return "";
        }
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
                names[i] = names[i].substring(0, 1) + names[i].substring(1).toLowerCase();
            }
            return StringUtils.join(names, " ");
        } else if (obj instanceof Dispenser) {
            return "Dispenser";
        }
        return obj.toString();
    }

    final public String getDeathMessage(LivingEntity entity, Object damager) {
        return getDeathMessage(getEntityName(entity), getEntityName(damager));
    }

    public String getDeathMessage(String killed, Object killer) {
        return getDeathMessage(killed, getEntityName(killer));
    }

    public String getDeathMessage(Object killed, String killer) {
        return getDeathMessage(getEntityName(killed), killer);
    }

    public String getDeathMessage(String killedName, String killerName) {
        String deathMessage = getDeathMessage();
        {
            char[] chars = killedName.toCharArray();
            int starting = chars.length;
            while (starting - 2 > 0 && chars[starting - 2] == '§') {
                starting--;
            }
            deathMessage = deathMessage.replace("%Killed%'s",
                    killedName.substring(0, starting - 1) + "'s" + killedName.substring(starting - 1)).replace("%Killed%",
                    killedName);
        }
        if (killerName != null) {
            {
                char[] chars = killerName.toCharArray();
                int starting = chars.length;
                while (starting - 2 > 0 && chars[starting - 2] == '§') {
                    starting--;
                }
                deathMessage = deathMessage.replace("%Killer%'s",
                        killerName.substring(0, starting - 1) + "'s" + killerName.substring(starting - 1)).replace("%Killer%",
                        killerName);
            }
        }
        return deathMessage;
    }

    public abstract Object getKiller(EntityDamageEvent event);

    public abstract boolean isCauseOfDeath(EntityDamageEvent event);

}
