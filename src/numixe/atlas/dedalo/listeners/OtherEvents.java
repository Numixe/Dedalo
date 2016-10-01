package numixe.atlas.dedalo.listeners;

import numixe.atlas.dedalo.entities.Zone;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static numixe.atlas.dedalo.Dedalo.*;

public class OtherEvents implements Listener {
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
         
    	
    }
       
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        	
    	
    }
	
	@EventHandler
	  public void onSignCreate(SignChangeEvent e) {
	    //Player p = e.getPlayer();
	    if (e.getLine(0).equalsIgnoreCase("[Dedalo]")) {
	    	if (e.getLine(0).equalsIgnoreCase("lobby")) {
	    		
	    	}
	      e.setLine(0, "§1§l[Dedalo]");
	      e.setLine(1, "");
	      e.setLine(2, "§2§lJoin");
	    //e.setLine(3, Player dentro + "/" + MAX_LOBBY);
	    
	    }
	  }
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {	
		
		// prova init.yml
		
		Player p = e.getPlayer();
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			Block b = e.getClickedBlock();
			
			Zone tmp = new Zone("Template");
			
			if (!plugin.getInit().contains("zones." + tmp.name + ".blocks")) {
				
				if (!plugin.getInit().contains("zones"))
					plugin.getInit().createSection("zones");
				
				if (!plugin.getInit().contains("zones." + tmp.name))
					plugin.getInit().createSection("zones." + tmp.name);
					
				plugin.getInit().createSection("zones." + tmp.name + ".blocks");
			}
			
			Zone.writeBlock(tmp, 
					plugin.getInit().getConfigurationSection("zones." + tmp.name + ".blocks"), 
					tmp.new BlockNode(b, b.getLocation()));
			
			p.sendMessage("Block written");
			
			plugin.saveInit();
		}
	}
}
