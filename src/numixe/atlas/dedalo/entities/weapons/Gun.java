package numixe.atlas.dedalo.entities.weapons;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import numixe.atlas.dedalo.entities.Weapon;

public class Gun extends Weapon {

	public Gun() {
		
		super("Gun", genItem("§4Gun"), 30);
	}

	@Override
	public void shoot() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hit() {
		// TODO Auto-generated method stub
		
	}
	
	public static final ItemStack genItem(String displayname) {
		
		ItemStack out = new ItemStack(Material.LEVER);	// ho scelto leva cosi a caso
		
		// custom itemstack
		
		return out;
	}

}
