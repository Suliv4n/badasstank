package fr.sulivan.badasstank.mob.player;

import java.util.HashMap;
import java.util.Set;

public class PlayersSet {
	
	private int slots;
	private HashMap<Integer, Player> players;
	
	public PlayersSet(int slots){
		players = new HashMap<Integer, Player>();
		this.slots = slots;
	}

	public Player get(int position){
		return players.get(position);
	}

	public boolean put(int position, Player player) {
		if(position<slots){
			players.put(position, player);
			return true;
		}
		return false;
	}
	
	public void add(){
		
	}

	public Set<Integer> keySet() {
		return players.keySet();
	}

	public Player remove(int position) {
		return players.remove(position); 
	}
	
	public int add(Player player){
		int position = 0;
		while(players.get(position) != null && position < slots){
			position++;
		}
		
		if(position == slots){
			return -1;
		}
		
		players.put(position, player);
		
		return position;
	}
	
	public void removeFromRemoteKey(String key) {
		for(Integer position : players.keySet()){
			
			if(players.get(position).getRemoteKey() != null && players.get(position).getRemoteKey().equals(key)){
				players.remove(position);
				break;
			}
		}
	}
}
