package numixe.atlas.dedalo;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import numixe.atlas.dedalo.listeners.OtherEvents;
import numixe.atlas.dedalo.listeners.Timer;


public class Dedalo extends JavaPlugin {
	
	public static Game game = null;
	public static Dedalo plugin = null;
	private FileConfiguration init = null;
	private File initfile = null;

	public void onEnable() {
		
		plugin = this;
		game = new Game(new Lobby());
        Bukkit.getServer().getPluginManager().registerEvents(new OtherEvents(), this);
        reloadInit();
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
	
	// methods to get init.yml
	
	public FileConfiguration getInit() {	// analogly to getConfig()
		
		return init;
	}
	
	public void saveInit() {	// analogly to saveConfig()
		
		if (init == null || initfile == null)
	        return;
		
	    try {
	    	
	        init.save(initfile);
	        
	    } catch (IOException ex) {
	    	
	        getLogger().log(Level.SEVERE, "Could not save configuration to " + initfile, ex);
	    }
	}
	
	public void reloadInit() {	// analogly to reloadConfig()
		
		if (initfile == null) {
			
			initfile = new File(getDataFolder(), "init.yml");
			
			if (!initfile.exists()) {
				
				initfile.getParentFile().mkdirs();
				
				try {
					initfile.createNewFile();
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		init = YamlConfiguration.loadConfiguration(initfile);
	}
}
