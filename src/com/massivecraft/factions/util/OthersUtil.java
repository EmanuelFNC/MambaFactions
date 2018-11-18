package com.massivecraft.factions.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;

public class OthersUtil {
	
	public static Character testInvalidCharacter(String string) 
	{
		for (int i = 0; i < string.length(); i++) 
		{
			char c = Character.toUpperCase(string.charAt(i));
			if (c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F' || c == 'G' ||
				c == 'H' ||	c == 'I' || c == 'J' || c == 'K' || c == 'L' || c == 'M' || c == 'N' ||	
				c == 'O' || c == 'P' ||	c == 'Q' || c == 'R' || c == 'S' || c == 'T' || c == 'U' || 
				c == 'V' ||	c == 'W' || c == 'X' ||	c == 'Y' || c == 'Z' ||	c == '0' || c == '1' ||	
				c == '2' || c == '3' ||	c == '4' || c == '5' ||	c == '6' || c == '7' ||	c == '8' || c == '9')
				continue;
			else 
				return c;
		}
		return null;
	}

	public static String cleanMessage(String message)
	{
		String target = message;
		if (target == null) return null;
		
		target = target.trim();
		if (target.isEmpty()) target = null;
		
		return target;
	}
	
	public static boolean isValidFactionsName(String newName, Player p) 
	{
		// Verificando se o nome n�o possui caracteres invalidos
		Character character = testInvalidCharacter(newName);
		if (character != null) {
			p.sendMessage("�cO caractere '�e" + character + "�c' n�o � permitido!");
			return false;
		}
		
		// Verificando se o nome possui o n�mero minimo de caracteres
		int minLength = MConf.get().factionNameLengthMin;
		if (newName.length() < minLength) {
			p.sendMessage("�cO nome da fac��o deve conter no min�mo " + minLength + "�c caracteres.");
			return false;
		}
		
		// Verificando se o nome ultrapassa o n�mero maximo de caracteres
		int maxLength = MConf.get().factionNameLengthMax;
		if (newName.length() > maxLength) {
			p.sendMessage("�cO nome da fac��o poder conter no m�ximo " + maxLength + "�c caracteres.");
			return false;
		}
		
		// Verificando se o nome n�o esta em uso
		Faction f = FactionColl.get().getByName(newName);
		if (f != null) {
			p.sendMessage("�cO nome '" + newName + "' j� esta em uso.");
			return false;
		}
		
		return true;
	}
	
	
	public static Set<Player> getPlayersNearby(Player player, int distance) 
	{
		Set<Player> players = new HashSet<Player>();
		int d2 = distance * distance;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2) {
				players.add(p);
			}
		}
		return players;
	}
	
	public static List<String> fplayers(Faction f)
	{
		List<String> list = new ArrayList<>();
		List<MPlayer> mps = f.getMPlayers();
		int pNumber = 1;
		String line = "�7";
		for (MPlayer mp : mps) {
			line += mp.getRole().getPrefix() + (mp.isOnline() ? "�a" : "�7") + mp.getName();
			if (pNumber == 3) {
				list.add(line);
				line = "�7";
				pNumber = 1;
			} else {
				line += "�7, ";
				pNumber++;
			}
		}
		if (pNumber != 1) {
			list.add(line.substring(0, line.length() -2));
		}
		return list;
	}
	
	public static List<String> fmotd(Faction f) 
	{
		String factionmotd = f.getMotdDesc();
		int factionmotdtamanho = factionmotd.length();
		List<String> motd = new ArrayList<>();
		
		if (!f.hasMotd()) {
			motd.add("�7�o'Mensagem do dia indefinida.'");
			return motd; }
		
		else if (factionmotdtamanho < 40) {
			motd.add("�7'�b"+ factionmotd.substring(0, factionmotdtamanho) + "�7'"); 
			return motd; }
		
		else if (factionmotdtamanho < 110) {
			motd.add("�7'�b"+factionmotd.substring(0,40));
			motd.add("�b"+factionmotd.substring(50, factionmotdtamanho) + "�7'");
			return motd; }
		
		else {
			motd.add("�7A frase da motd � muito grande!");
			motd.add("�7Para visualiza-la use '�f/f motd�7'");
			return motd; 
		}
	}
	
	public static Set<Faction> getAliadosPendentesEnviados(Faction f) 
	{
		Set<Faction> aliadosPendentesEnviados = new HashSet<>();
        Map<String, Rel> relations = f.getRelationWishes();
		for (Entry<String, Rel> relation : relations.entrySet()){
			Faction ally = Faction.get(relation.getKey());
			if (ally != null) {
				if (relation.getValue() == Rel.ALLY) {
					if (ally.getRelationWish(f) != Rel.ALLY) {
						aliadosPendentesEnviados.add(ally);
					}
				}
			}
		}
		return aliadosPendentesEnviados;
	}
	
}