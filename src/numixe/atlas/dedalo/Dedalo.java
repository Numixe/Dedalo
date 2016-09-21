package numixe.atlas.dedalo;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
	}
	
	public void onDisable() {
		
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("startgame")) {
			
			game.start();
			return true;
			
		} else if (cmd.getName().equalsIgnoreCase("finishgame")) {
			
			game.finish();
			return true;
		}
		
		if (!(sender instanceof Player)) {
			
			sender.sendMessage("You are not a Player");
			return false;
		}
		
		if (cmd.getName().equalsIgnoreCase("timerstartgame")) {
			
			Timer timer = new Timer("startgame", "§7Il gioco iniziera' tra &sec secondi...", 5);
			timer.start();
		}
	    	
		return true;
	}
}
