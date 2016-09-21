package numixe.atlas.dedalo;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import numixe.atlas.dedalo.listeners.OtherEvents;
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
	    	
		return true;
	}
}
