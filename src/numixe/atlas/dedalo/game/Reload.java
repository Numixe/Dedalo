package numixe.atlas.dedalo.game;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Reload implements Listener {
	
	public static final ItemStack RedReload =  genBlock("red");
	public static final ItemStack BlueReload = genBlock("blue");
	public static final String RelComplete = "�9Dedalo> �7Ricarica completata!";	
	
	/**
	 * Ho gia' gestito questo evento tramite la classe DPlayer, PlayerEvents e ChargeEvent
	 * Quando il giocatore si piazza sul blocco di spawn, la barra dell'exp, che rappresenta la carica,
	 * inizia automaticamente a caricarsi finchè il giocatore non esce dal blocco o la barra non e' totalmente
	 * carica
	 */
	
	@EventHandler
	public void reloadGun(PlayerInteractEvent e) {	
		Action a = e.getAction();
		Player p = e.getPlayer();
		
		if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = e.getClickedBlock();
			if (block.equals(RedReload)) {	// non funzionerebbe perche stai comparando un Block con un ItemStack
				p.sendMessage("a");			// il che e' sempre false, dai un occhiata a DPlayer
				//reload red
			} else if (block.equals(BlueReload)) {
				p.sendMessage(RelComplete);
				//reload blue
			}
		}
	}
	
	public static final ItemStack genBlock(String name) {
		
		ItemStack genBlock = null;
		
		if (name.equalsIgnoreCase("red")) {
			
			genBlock = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
			
		} else if (name.equalsIgnoreCase("blue")) {
			
			genBlock = new ItemStack(Material.HARD_CLAY, 1, (short) 11);
		}
		
		return genBlock;
	}
}
