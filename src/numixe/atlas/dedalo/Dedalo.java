package numixe.atlas.dedalo;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import numixe.atlas.dedalo.game.Reload;
import numixe.atlas.dedalo.listeners.OtherEvents;
import numixe.atlas.dedalo.listeners.Timer;
import numixe.atlas.dedalo.listeners.WoolEvents;


public class Dedalo extends JavaPlugin {
	
	public static Game game = new Game(new Lobby());
	public static Dedalo plugin = null;

	public void onEnable() {
		
		plugin = this;
        Bukkit.getServer().getPluginManager().registerEvents(new WoolEvents(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OtherEvents(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Reload(), this);
	}
	
	public void onDisable() {
		
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		// players and console access commands
		
		if (cmd.getName().equalsIgnoreCase("startgame")) {
			
			game.start();
			
		} else if (cmd.getName().equalsIgnoreCase("finishgame")) {
			
			game.finish();
			
		} else {
			
			if (!(sender instanceof Player)) {
				
				sender.sendMessage("You are not a Player");
				return false;
			}
		}
		
		// players only access commands
		
		if (cmd.getName().equalsIgnoreCase("timerstartgame")) {
			
			new Timer("startgame", "§7Il gioco iniziera' tra &sec secondi...", 5);
		}
	    	
		return true;
	}
}
