package numixe.atlas.dedalo.entities;

import static numixe.atlas.dedalo.Dedalo.game;

import numixe.atlas.dedalo.listeners.ChargeEvent;

import org.bukkit.entity.Player;

public class DPlayer {
	
	public static final int MAX_CHARGE = 99;
	public static final int CHARGE_PER_UNIT = 10;
	public static final String MAX_CHARGE_MSG = "§9Dedalo> §7Ricarica completata!";
	
	public Player player;
	public int kills, deaths;
	public int charge;			// sistema di ricarica ancora da decidere
	private boolean charging;
	private ChargeEvent chargeEvent;
	
	/*
	 * 	Suppongo che la carica dipenda dal giocatore
	 * 	e che usando l'arma si scarichi ad ogni colpo
	 * 	chiamando il metodo chargeDown()
	 */
	
	/*
	 * Per ricaricare e' sufficiente piazzarsi sul blocco di spawn
	 * L'evento e' gestito dalla classe PlayerEvents
	 * Il loop di ricarica e' invece gestito da ChargeEvent
	 */

	public DPlayer(Player p) {
		
		player = p;
		kills = 0;
		deaths = 0;
		charge = MAX_CHARGE;
		charging = false;
		chargeEvent = null;
		
		/*
		 * 	!!! Prima di modificare l'esperienza è meglio salvarla prima in uno .yml !!!
		 */
		
		player.setLevel(0);
		player.setExp(0.99f);
	}
	
	public String getName() {
		
		return player.getName();
	}
	
	public void chargeUp() {
		
		if (charge == MAX_CHARGE) {
			
			player.sendMessage(MAX_CHARGE_MSG);
		}
		
		charge += CHARGE_PER_UNIT;
		
		if (charge > MAX_CHARGE)
			charge = MAX_CHARGE;	// leggermente inferiore per evitare che salga di livello
		
		player.setExp((float)charge / 100);
	}
	
	public void chargeDown(int amount) {
		
		charge -= amount;
		
		if (charge < 0)
			charge = 0;
		
		player.setExp((float)charge / 100);
	}
	
	public void setCharging(boolean value) {
		
		if (charging == value)
			return;
			
		if (value) {
			
			chargeEvent = new ChargeEvent(this, 0.5f);	// inizia a caricare ogni mezzo secondo
			
		} else {
			
			chargeEvent.destroy();
			chargeEvent = null;
		}
		
		charging = value;
	}
	
	public boolean isCharging() {
		
		return charging;
	}
	
	public void spawn() {
		
		player.teleport(game.field.spawnLocation(game.lobby.ownedBy(player)));
	}
}
