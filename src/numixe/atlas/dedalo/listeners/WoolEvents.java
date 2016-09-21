package numixe.atlas.dedalo.listeners;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import static numixe.atlas.dedalo.Dedalo.*;
import numixe.atlas.dedalo.entities.DPlayer;

public class WoolEvents implements Listener {
	
	public static final ItemStack redWool = WoolEvents.genWool("red");
	public static final ItemStack blueWool = WoolEvents.genWool("blue");
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		
		Player p = event.getPlayer();
		Block block = event.getClickedBlock();
		
		if (block.getState() instanceof Sign) {
			
		    Sign sign = (Sign) block.getState();
		    
		    if (sign.getLine(0).equalsIgnoreCase("§1§l[Dedalo]")) { // i nomi possono essere anche presi dal config
		    	
		    	game.lobby.chooseTeam(p);
		    }
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		if (!event.getInventory().getName().equalsIgnoreCase("§9§lTeam Chooser"))
			return;
		
		event.setCancelled(true);
		Player p = (Player) event.getWhoClicked();
		
		if (event.getCurrentItem() == null
				|| event.getCurrentItem().getType() == Material.AIR
				|| !event.getCurrentItem().hasItemMeta()) {
			p.closeInventory();
			return;
		}
		
		if (event.getCurrentItem().equals(redWool)) {
			
			if (game.lobby.ownedBy(p) == null)
				game.lobby.addToTeam(0, new DPlayer(p));
			else
				p.sendMessage("§9Sei già in un team");
		}
		else if (event.getCurrentItem().equals(blueWool)) {
			
			if (game.lobby.ownedBy(p) == null)
				game.lobby.addToTeam(1, new DPlayer(p));
			else
				p.sendMessage("§9Sei già in un team");
		}
		
		if (game.lobby.isFull()) {
			
			new Timer("startgame", "§7Il gioco iniziera' tra &sec secondi...", 5);
		}
		
		p.closeInventory();
	}
	
	public static final ItemStack genWool(String name) {
		
		ItemStack out = null;
		String display_name = null;
		DyeColor color = null; 
		
		if (name.equalsIgnoreCase("red")) {
			
			color = DyeColor.RED;
			display_name = "§c§lRed";
			
		} else if (name.equalsIgnoreCase("blue")) {
			
			color = DyeColor.BLUE;
			display_name = "§9§lBlue";
		}
		
		Wool wool = new Wool(color);
		out = wool.toItemStack(1);
		ItemMeta meta = out.getItemMeta();
		meta.setDisplayName(display_name);
		out.setItemMeta(meta);
		
		return out;
	}
}
