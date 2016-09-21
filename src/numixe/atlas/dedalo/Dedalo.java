package numixe.atlas.dedalo;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Dedalo extends JavaPlugin implements Listener {
	
	public static Game game = new Game(new Lobby());
	public static Dedalo plugin = null;

	public void onEnable() {
		
		plugin = this;
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(game, this);
        Bukkit.getServer().getPluginManager().registerEvents(game.lobby, this);
	}
	
	public void onDisable() {
		
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
	    	
		return true;
	}
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
         
    	
    }
       
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        	
    	
    }
}
