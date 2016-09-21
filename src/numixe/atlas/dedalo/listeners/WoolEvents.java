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
		
		if (p.getType().equals(redWool))
			game.lobby.addToTeam(0, new DPlayer(p));
		else if (p.getType().equals(blueWool))
			game.lobby.addToTeam(1, new DPlayer(p));
		
		if (game.lobby.isFull()) {
			
			game.start();
		}
		
		p.closeInventory();
		p.sendMessage("FUNZIONA :D");
	}
	
	public static final ItemStack genWool(String name) {
		
		ItemStack out;
		
		if (name.equalsIgnoreCase("red")) {
			
			Wool wool = new Wool(DyeColor.RED);
			out = wool.toItemStack(1);
			ItemMeta meta = out.getItemMeta();
			meta.setDisplayName("§c§lRed");
			out.setItemMeta(meta);
			
			return out;
			
		} else if (name.equalsIgnoreCase("blue")) {
			
			Wool wool = new Wool(DyeColor.BLUE);
			out = wool.toItemStack(1);
			ItemMeta meta = out.getItemMeta();
			meta.setDisplayName("§9§lBlue");
			out.setItemMeta(meta);
			
			return out;
		}
		
		return null;
	}
}
