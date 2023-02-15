package mod.gottsch.fabric.eechelons.core.echelon;

import com.google.common.collect.Maps;
import mod.gottsch.fabric.eechelons.EEchelons;
import mod.gottsch.fabric.eechelons.core.bst.Interval;
import mod.gottsch.fabric.eechelons.core.bst.IntervalTree;
import mod.gottsch.fabric.eechelons.core.config.EchelonsHolder;
import mod.gottsch.fabric.eechelons.core.data.ILevelSupport;
import mod.gottsch.fabric.eechelons.core.mixin.XpAccessor;
import mod.gottsch.fabric.eechelons.core.setup.Registration;
import mod.gottsch.fabric.gottschcore.random.WeightedCollection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Mark Gottschling on Jul 26, 2022
 *
 */
public class EchelonManager {
    // f_21364_ => xpReward
    private static final String XP_REWARD_FIELDNAME = "f_21364_";
    private static final Identifier ALL_DIMENSION = new Identifier(".", ".");

    /*
     * map of echelons by dimension
     */
    private static final Map<Identifier, EchelonsHolder.Echelon> ECHELONS = Maps.newHashMap();
    /*
     * map of level-histogram interval tree (bst) by dimension
     */
    private static final Map<Identifier, IntervalTree<WeightedCollection<Double, Integer>>> HISTOGRAM_TREES = Maps.newHashMap();

    /*
     * map of echelons by dimension-mob pair
     */
    private static final Map<Pair<Identifier, Identifier>, EchelonsHolder.Echelon> ECHELONS_BY_MOB = Maps.newHashMap();
    /*
     * map of level-histogram interval tree (bst) by dimension-mob pair
     */
    private static final Map<Pair<Identifier, Identifier>, IntervalTree<WeightedCollection<Double, Integer>>> HISTOGRAM_TREES_BY_MOB = Maps.newHashMap();


    /**
     *
     */
    public static void build() {
        ECHELONS.clear();
        HISTOGRAM_TREES.clear();

        ECHELONS_BY_MOB.clear();
        HISTOGRAM_TREES_BY_MOB.clear();

        List<EchelonsHolder.Echelon> echelons = Registration.holder.echelons;
        if (ObjectUtils.isEmpty(echelons)) {
            return;
        }
        echelons.forEach(echelon -> {
            if (ObjectUtils.isEmpty(echelon.getStratum())) {
                return;
            }
            /*
             *  build BST
             */
            // create a new tree
            IntervalTree<WeightedCollection<Double, Integer>> tree = new IntervalTree<>();
            // process each strata in the stratum
            echelon.getStratum().forEach(strata -> {
                // build weighted collection from histogram
                WeightedCollection<Double, Integer> collection = new WeightedCollection<>();
                strata.getHistogram().forEach(entry -> {
                    collection.add(entry.getWeight(), entry.getLevel());
                });
                // create new interval
                Interval<WeightedCollection<Double, Integer>> interval = new Interval<>(strata.getMin(), strata.getMax(), collection);
                // add interval to tree
                tree.insert(interval);
            });

            // TODO refactor to not duplicate code
            if (ObjectUtils.isEmpty(echelon.getDimensions())) {
                if (!echelon.getMobWhitelist().isEmpty()) {
                    echelon.getMobWhitelist().forEach(mob -> {
                        // create a key pair
                        Pair<Identifier, Identifier> keyPair = new ImmutablePair<>(ALL_DIMENSION, new Identifier(mob));
                        ECHELONS_BY_MOB.put(keyPair, echelon);
                        HISTOGRAM_TREES_BY_MOB.put(keyPair, tree);
                    });

                }
                else {
                    ECHELONS.put(ALL_DIMENSION, echelon);
                    HISTOGRAM_TREES.put(ALL_DIMENSION, tree);
                }
            }
            else {
                // build
                echelon.getDimensions().forEach(dimension -> {
                    Identifier dimensionKey;
                    if (dimension.equals(".") || dimension.equals("*") || dimension.equals("*:*")) {
                        dimensionKey = ALL_DIMENSION;
                    }else {
                        dimensionKey = new Identifier(dimension);
                    }

                    if (!echelon.getMobWhitelist().isEmpty()) {
                        echelon.getMobWhitelist().forEach(mob -> {
                            // create a key pair
                            Pair<Identifier, Identifier> keyPair = new ImmutablePair<>(dimensionKey, new Identifier(mob));
                            ECHELONS_BY_MOB.put(keyPair, echelon);
                            HISTOGRAM_TREES_BY_MOB.put(keyPair, tree);
                        });
                    }
                    else {
                        ECHELONS.put(dimensionKey, echelon);
                        HISTOGRAM_TREES.put(dimensionKey, tree);
                    }
                });
            }
        });
    }

    /**
     *
     * @param entity
     * @return
     */
    public static boolean isValidEntity(Entity entity) {

        return (entity instanceof HostileEntity);
    }

    /**
     *
     * @param dimension
     * @param entity
     * @return
     */
    public static boolean isValidEntity(Identifier dimension, Entity entity) {
        boolean result = false;
        if (ECHELONS.containsKey(dimension)) {
            if (!ECHELONS.get(dimension).getMobBlacklist().contains(EntityType.getId(entity.getType()).toString())) {
                result = true;
            }
        }
        return result;
    }

    /**
     *
     * @param mob
     * @return
     */
    public static EchelonsHolder.Echelon getEchelon(Entity mob) {
        Pair<Identifier, Identifier> keyPair = new ImmutablePair<>(mob.getWorld().getDimension().getEffects(), EntityType.getId(mob.getType()));
        if (ECHELONS_BY_MOB.containsKey(keyPair)) {
            return ECHELONS_BY_MOB.get(keyPair);
        }
        else {
            keyPair = new ImmutablePair<>(ALL_DIMENSION, EntityType.getId(mob.getType()));
            if (ECHELONS_BY_MOB.containsKey(keyPair)) {
                return ECHELONS_BY_MOB.get(keyPair);
            }
            else {
                if (isValidEntity(mob.getWorld().getDimension().getEffects(), mob)) {
                    return ECHELONS.get(mob.getWorld().getDimension().getEffects());
                }
                else if (isValidEntity(ALL_DIMENSION, mob)) {
                    return ECHELONS.get(ALL_DIMENSION);
                }
            }
        }
        return null;
    }

    public static Integer getLevel(Entity mob, Integer searchValue) {
        Integer result = 0;

        IntervalTree<WeightedCollection<Double, Integer>> tree = null;

        // first check the histograms by mob map
        Pair<Identifier, Identifier> keyPair = new ImmutablePair<>(mob.getWorld().getDimension().getEffects(), EntityType.getId(mob.getType()));
        if (HISTOGRAM_TREES_BY_MOB.containsKey(keyPair)) {
            tree = HISTOGRAM_TREES_BY_MOB.get(keyPair);
            result = getLevel(tree, searchValue);
        }
        else {
            keyPair = new ImmutablePair<>(ALL_DIMENSION, EntityType.getId(mob.getType()));
            if (HISTOGRAM_TREES_BY_MOB.containsKey(keyPair)) {
                tree = HISTOGRAM_TREES_BY_MOB.get(keyPair);
                result = getLevel(tree, searchValue);
            }
            else {
                result = getLevel(mob.getWorld().getDimension().getEffects(), searchValue);
            }
        }

        return result;
    }

    /**
     *
     * @param key
     * @param searchValue
     * @return
     */
    public static Integer getLevel(Identifier key, Integer searchValue) {
        Integer result = 0;

        // use default key is not found in the histogram tree
        if (!HISTOGRAM_TREES.containsKey(key)) {
            key = ALL_DIMENSION;
        }

        if (HISTOGRAM_TREES.containsKey(key)) {
            IntervalTree<WeightedCollection<Double, Integer>> tree = HISTOGRAM_TREES.get(key);
            result = getLevel(tree, searchValue);
        }
        return result;
    }

    /**
     *
     * @param tree
     * @param searchValue
     * @return
     */
    private static Integer getLevel(IntervalTree<WeightedCollection<Double, Integer>> tree, Integer searchValue) {
        Integer result = 0;

        List<Interval<WeightedCollection<Double, Integer>>> stratum = tree
                .getOverlapping(tree.getRoot(), new Interval<>(searchValue, searchValue), false);

        if (ObjectUtils.isEmpty(stratum)) {
            return 0;
        }

        // get the first element/strata - there should only be one.
        WeightedCollection<Double, Integer> col = stratum.get(0).getData();
        if (ObjectUtils.isEmpty(col)) {
            return 0;
        }
        // get the next weighted random integer
        result = col.next();

        return result;
    }

    /**
     *
     * @param entity the entity that is having modificatins being applied to
     */
    public static void applyModifications(Entity entity) {
        ILevelSupport levelEntity = (ILevelSupport) entity;

        if (levelEntity.getLevel() < 0) {
            // determine dimension
            Identifier dimension = entity.getEntityWorld().getDimension().getEffects();

            // determine the altitute (y-value)
            int y = entity.getBlockY();

            //				Integer echelonLevel = EchelonManager.getLevel(dimension, y);
            Integer echelonLevel = EchelonManager.getLevel(entity, y);

            //				EEchelons.LOGGER.debug("selected level -> {} for dimension -> {} @ y -> {}", echelonLevel, dimension, y);

            /*
             *  apply the attribute modifications
             */
            //				Echelon echelon = getEchelon(dimension);
            EchelonsHolder.Echelon echelon = getEchelon(entity);

            if (echelon == null) {
                levelEntity.setLevel(0);
                return;
            }

            MobEntity mob = (MobEntity) entity;
            // health
            modifyHealth(mob, echelonLevel, echelon);

            // damage
            modifyDamage(mob, echelonLevel, echelon);

            // armor
            modifyArmor(mob, echelonLevel, echelon);

            // armor
            modifyArmorToughness(mob, echelonLevel, echelon);

            // knockback
            modifyKnockback(mob, echelonLevel, echelon);

            // knockback resist
            modifyKnockbackResist(mob, echelonLevel, echelon);

            // speed
            modifySpeed(mob, echelonLevel, echelon);

            // experience
            modifyXp(mob, echelonLevel, echelon);

            // update the capability
            levelEntity.setLevel(echelonLevel);
        }
    }

    private static void modifyHealth(MobEntity mob, int level, EchelonsHolder.Echelon echelon) {

        if (echelon.hasHpFactor()) {
            EntityAttributeInstance attribute = mob.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            if (attribute != null) {
                double health = 1.0 + (echelon.getHpFactor() * level);
                double newHealth = mob.getMaxHealth() * health;
                if (echelon.getMaxHp() != null && echelon.getMaxHp() > 0.0) {
                    newHealth = Math.min(newHealth, echelon.getMaxHp());
                }
                attribute.setBaseValue(newHealth);
                mob.setHealth(mob.getMaxHealth());
				EEchelons.LOGGER.debug("mob new health -> {}", mob.getMaxHealth());
            }
        }
    }

    private static void modifyDamage(MobEntity mob, int level, EchelonsHolder.Echelon echelon) {
        if (echelon.hasDamageFactor()) {
            EntityAttributeInstance attribute = mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            if (attribute != null) {
                double damage = 1.0 + (echelon.getDamageFactor() * level);
                double newDamage = attribute.getBaseValue() * damage;
                if (echelon.getMaxDamage() != null) {
                    newDamage = Math.min(newDamage, echelon.getMaxDamage());
                }
                attribute.setBaseValue(newDamage);
//				EEchelons.LOGGER.debug("mob new damage -> {}", mob.getAttributeValue(Attributes.ATTACK_DAMAGE));
            }
        }
    }

    private static void modifyArmor(MobEntity mob, Integer level, EchelonsHolder.Echelon echelon) {
        if (echelon.hasArmorFactor()) {
            EntityAttributeInstance attribute = mob.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
            if (attribute != null) {
                double armor = 1.0 + (echelon.getArmorFactor() * level);
                double newArmor = attribute.getBaseValue() * armor;
                if (echelon.getMaxArmor() != null) {
                    newArmor = Math.min(newArmor, echelon.getMaxArmor());
                }
                attribute.setBaseValue(newArmor);
                //		EEchelons.LOGGER.debug("mob new armor -> {}", mob.getAttributeInstanceValue(EntityAttributes.ARMOR));
            }
        }
    }

    private static void modifyArmorToughness(MobEntity mob, Integer level, EchelonsHolder.Echelon echelon) {
        if (echelon.hasArmorToughnessFactor()) {
            EntityAttributeInstance attribute = mob.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
            if (attribute != null) {
                double armor = 1.0 + (echelon.getArmorToughnessFactor() * level);
                double newArmor = attribute.getBaseValue() * armor;
                if (echelon.getMaxArmorToughness() != null) {
                    newArmor = Math.min(newArmor, echelon.getMaxArmorToughness());
                }
                attribute.setBaseValue(newArmor);
                //		EEchelons.LOGGER.debug("mob new armor toughness -> {}", mob.getAttributeInstanceValue(EntityAttributes.ARMOR_TOUGHNESS));
            }
        }
    }

    private static void modifyKnockback(MobEntity mob, int level, EchelonsHolder.Echelon echelon) {
        if (echelon.hasKnockbackIncrement()) {
            EntityAttributeInstance attribute = mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
            if (attribute != null) {
                double knockback = echelon.getKnockbackIncrement() * level;
                double newKnockback = attribute.getBaseValue() + knockback;
                if (echelon.getMaxKnockback() != null) {
                    newKnockback = Math.min(newKnockback, echelon.getMaxKnockback());
                }
                attribute.setBaseValue(newKnockback);
                //			EEchelons.LOGGER.debug("mob new knockback -> {}", mob.getAttributeInstanceValue(EntityAttributes.ATTACK_KNOCKBACK));
            }
        }
    }

    private static void modifyKnockbackResist(MobEntity mob, int level, EchelonsHolder.Echelon echelon) {
        if (echelon.hasKnockbackResistIncrement()) {
            EntityAttributeInstance attribute = mob.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
            if (attribute != null) {
                double knockback = echelon.getKnockbackResistIncrement() * level;
                double newKnockback = attribute.getBaseValue() + knockback;
                if (echelon.getMaxKnockbackResist() != null) {
                    newKnockback = Math.min(newKnockback, echelon.getMaxKnockbackResist());
                }
                attribute.setBaseValue(newKnockback);
                //			EEchelons.LOGGER.debug("mob new knockback resist -> {}", mob.getAttributeInstanceValue(EntityAttributes.KNOCKBACK_RESISTANCE));
            }
        }
    }

    private static void modifySpeed(MobEntity mob, Integer level, EchelonsHolder.Echelon echelon) {
        if (echelon.hasSpeedFactor()) {
            EntityAttributeInstance attribute = mob.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            if (attribute != null) {
                double speed = 1.0 + (echelon.getSpeedFactor() * level);
                double newSpeed = attribute.getBaseValue() * speed;
                if (echelon.getMaxDamage() != null) {
                    // TODO what if max speed <= 0
                    newSpeed = Math.min(newSpeed, echelon.getMaxSpeed());
                }
                attribute.setBaseValue(newSpeed);
                //			EEchelons.LOGGER.debug("mob new speed -> {}", mob.getAttributeInstanceValue(EntityAttributes.MOVEMENT_SPEED));
            }
        }
    }

    private static void modifyXp(MobEntity mob, Integer level, EchelonsHolder.Echelon echelon) {
        if (echelon.hasXpFactor()) {
            double xp = 1.0 + (echelon.getXpFactor() * level);
            try {
//                int xpReward = (int)ObfuscationReflectionHelper.getPrivateValue(Mob.class, mob, XP_REWARD_FIELDNAME);
                int xpReward = ((XpAccessor)mob).getExperiencePoints();
                double newXpReward = xpReward * xp;
                if (echelon.getMaxXp() != null) {
                    newXpReward = Math.min(newXpReward, echelon.getMaxXp());
                }
//                ObfuscationReflectionHelper.setPrivateValue(Mob.class, mob, (int)newXpReward, XP_REWARD_FIELDNAME);
                ((XpAccessor)mob).setExperiencePoints((int)newXpReward);
            } catch(Exception e	) {
                return;
            }
        }
    }
}
