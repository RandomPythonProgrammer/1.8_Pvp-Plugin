package com.jchen.oldpvp;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {

	}

	@EventHandler()
	public void onAttack(EntityDamageByEntityEvent event) {

		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			ItemStack item = player.getInventory().getItemInMainHand();
			Map<Enchantment, Integer> enchantments = item.getEnchantments();
			LivingEntity entity = (LivingEntity) event.getEntity();
			double rawDamage = getRawDamage(item);

			if (enchantments.get(Enchantment.DAMAGE_ARTHROPODS) != null)
				if (enchantments.get(Enchantment.DAMAGE_ARTHROPODS) > 0
						&& entity.getCategory().equals(EntityCategory.ARTHROPOD)) {
					rawDamage += (2.5 * enchantments.get(Enchantment.DAMAGE_ARTHROPODS));
				}

			if (enchantments.get(Enchantment.DAMAGE_UNDEAD) != null)
				if (enchantments.get(Enchantment.DAMAGE_UNDEAD) > 0
						&& entity.getCategory().equals(EntityCategory.UNDEAD)) {
					rawDamage += (2.5 * enchantments.get(Enchantment.DAMAGE_UNDEAD));
				}

			if (enchantments.get(Enchantment.DAMAGE_ALL) != null)
				if (enchantments.get(Enchantment.DAMAGE_ALL) > 0) {
					rawDamage += (1.25 * enchantments.get(Enchantment.DAMAGE_ALL));
				}

			if (checkCrit(player)) {
				rawDamage *= 1.5;
			}
			
			if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
				rawDamage += (3 * player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier());
			}

			event.setDamage(rawDamage);
		}

	}

	public double getRawDamage(ItemStack item) {
		Material material = item.getType();
		double rawDamage;
		switch (material) {

		case NETHERITE_SWORD:
			rawDamage = 8;
			break;
		case DIAMOND_SWORD:
			rawDamage = 7;
			break;
		case IRON_SWORD:
			rawDamage = 6;
			break;
		case STONE_SWORD:
			rawDamage = 5;
			break;
		case WOODEN_SWORD:
			rawDamage = 4;
			break;

		case NETHERITE_AXE:
			rawDamage = 7;
			break;
		case DIAMOND_AXE:
			rawDamage = 6;
			break;
		case IRON_AXE:
			rawDamage = 5;
			break;
		case STONE_AXE:
			rawDamage = 4;
			break;
		case WOODEN_AXE:
			rawDamage = 3;
			break;

		case NETHERITE_PICKAXE:
		case NETHERITE_SHOVEL:
			rawDamage = 6;
			break;
		case DIAMOND_PICKAXE:
		case DIAMOND_SHOVEL:
			rawDamage = 5;
			break;
		case IRON_PICKAXE:
		case IRON_SHOVEL:
			rawDamage = 4;
			break;
		case STONE_PICKAXE:
		case STONE_SHOVEL:
			rawDamage = 3;
			break;
		case WOODEN_PICKAXE:
		case WOODEN_SHOVEL:
			rawDamage = 2;
			break;

		case TRIDENT:
			rawDamage = 7;
			break;

		default:
			return 1;
		}

		return rawDamage;
	}

	public boolean checkCrit(Player player) {
		Material blockMaterial = player.getLocation().getWorld().getBlockAt(player.getLocation()).getType();
		ArrayList<Material> blackList = new ArrayList<Material>() {
			{
				add(Material.WATER);
				add(Material.LADDER);
				add(Material.VINE);
				add(Material.CAVE_VINES);
				add(Material.GLOW_LICHEN);
				add(Material.TWISTING_VINES);
				add(Material.WEEPING_VINES);
			}
		};

		return (!player.isOnGround() && !blackList.contains(blockMaterial) && player.getFallDistance() != 0
				&& !player.isSprinting() && !player.hasPotionEffect(PotionEffectType.BLINDNESS));
	}

}
