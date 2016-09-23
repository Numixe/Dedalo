package numixe.atlas.dedalo.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
}
