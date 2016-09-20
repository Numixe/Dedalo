package numixe.atlas.dedalo;

import numixe.atlas.dedalo.entities.DPlayer;
import numixe.atlas.dedalo.entities.Team;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Lobby implements Listener {

	public Team[] teams;
	
	public Lobby() {
		
		teams = new Team[2];
		teams[0] = new Team("Red");	// i nomi possono essere anche presi dal config
		teams[1] = new Team("Blue");
	}
	
	public void addToTeam(int team_index, DPlayer p) {
		
		teams[team_index].addPlayer(p);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		
		Player p = event.getPlayer();
		Block block = event.getClickedBlock();
		
		if (block.getState() instanceof Sign) {
			
		    Sign sign = (Sign) block.getState();
		    
		    if (sign.getLine(0).equalsIgnoreCase("Red")) { // i nomi possono essere anche presi dal config
		    	
		    	addToTeam(0, new DPlayer(p));
		        
		    } else if (sign.getLine(0).equalsIgnoreCase("Blue")) {
		    	
		    	addToTeam(1, new DPlayer(p));
		    }
		}
	}
	
	@EventHandler
	  public void onSignCreate(SignChangeEvent e)	// a che serve?
	  {
	    Player p = e.getPlayer();
	    if (e.getLine(0).equalsIgnoreCase("[Dedalo]")) {
	    	if (e.getLine(0).equalsIgnoreCase("lobby")) {
	    		
	    	}
	      e.setLine(0, "§1§l[Dedalo]");
	      e.setLine(2, "§2§lJoin");
	    //e.setLine(3, Player dentro + "/" + MAX_LOBBY);
	      
	    }
	  }
}
