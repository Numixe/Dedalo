package numixe.atlas.dedalo.entities;

import org.bukkit.inventory.ItemStack;

public abstract class Weapon {
	
	public final int damage;	// in percentuale
	public final ItemStack item;
	public final String name;
	
	public Weapon(String name, ItemStack item, int damage) {
		
		this.name = name;
		this.item = item;
		this.damage = damage;
	}

	public abstract void shoot();
	public abstract void hit();
}
