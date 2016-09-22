package numixe.atlas.dedalo.game;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Reload implements Listener {
	
	public static final ItemStack RedReload =  genBlock("red");
	public static final ItemStack BlueReload = genBlock("blue");
	public static final String RelComplete = "§9Dedalo> §7Ricarica completata!";
	
	@EventHandler
	public void reloadGun(PlayerInteractEvent e) {
		Action a = e.getAction();
		Player p = e.getPlayer();
		
		if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = e.getClickedBlock();
			if (block.equals(RedReload)) {
				p.sendMessage("a");
				//reload red
			} else if (block.equals(BlueReload)) {
				p.sendMessage(RelComplete);
				//reload blue
			}
		}
	}
	
	public static final ItemStack genBlock(String name) {
		
		ItemStack genBlock = null;
		
		if (name.equalsIgnoreCase("red")) {
			
			genBlock = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
			
		} else if (name.equalsIgnoreCase("blue")) {
			
			genBlock = new ItemStack(Material.HARD_CLAY, 1, (short) 11);
		}
		
		return genBlock;
	}
}
