package com.massivecraft.factions.engine;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.ps.PS;

public class EngineChunkChange extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static EngineChunkChange i = new EngineChunkChange();
	public static EngineChunkChange get() { return i; }

	// -------------------------------------------- //
	// CHUNK CHANGE: ALLOWED
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onChunksChange(EventFactionsChunksChange event)
	{
		// For security reasons we block the chunk change on any error since an error might block security checks from happening.
		try
		{
			onChunksChangeInner(event);
		}
		catch (Throwable throwable)
		{
			event.setCancelled(true);
			throwable.printStackTrace();
		}
	}

	public void onChunksChangeInner(EventFactionsChunksChange event)
	{
		// Pegando o MPlyaer
		final MPlayer mplayer = event.getMPlayer();

		// Verificando se o MPlayer � um admin
		if (mplayer.isOverriding()) return;
		
		// Pegando a fac��o que esta claimando o local, as chunks claimadas e as fac��es das chunks
		final Faction newFaction = event.getNewFaction();
		final Set<PS> chunks = event.getChunks();
		final Map<Faction, Set<PS>> currentFactionChunks = event.getOldFactionChunks();
		final Set<Faction> currentFactions = currentFactionChunks.keySet();
		
		// Verificando se a fac��o tem poder infinito
		if (newFaction.getFlag(MFlag.getFlagInfpower())) return;
		
		// Verificando se o player tem permiss�o para usar o comando
		if (!MPerm.getPermTerritory().has(mplayer, newFaction, true)) {
			event.setCancelled(true);
			return;
		}		

		// Verificando se o player esta realmente claimando
		if (newFaction.isNormal()) {
			
			// Verificando se a fac��o possui o minimo de membros requeridos
			if (newFaction.getMPlayers().size() < MConf.get().claimsRequireMinFactionMembers) {
				mplayer.msg("�cA sua fac��o precisa ter no minimo %s membros para poder conquistar territ�rios.", MConf.get().claimsRequireMinFactionMembers);
				event.setCancelled(true);
				return;
			}
			
			// Verificando se a compra de terrenos esta habilitada naquele mundo
			for (PS chunk : chunks) {
				String worldId = chunk.getWorld();
				if (!MConf.get().worldsClaimingEnabled.contains(worldId)) {
					mplayer.msg("�cA compra de territ�rios esta desabilitada neste mundo.");
					event.setCancelled(true);
					return;
				}
			}
			
			// Verificando se a fac��o n�o claimou al�m do limite permitido
			int totalLandCount = newFaction.getLandCount() + chunks.size();
			if (MConf.get().claimedLandsMax > 0 && totalLandCount > MConf.get().claimedLandsMax) {
				mplayer.msg("�cLimite m�ximo de terras atingido ("+MConf.get().claimedLandsMax+"�c)! Voc� n�o pode mais conquistar territ�rios.");
				event.setCancelled(true);
				return;
			}
			
			// Verificando se a fac��o tem poder necessario para claimar as terras
			if (totalLandCount > newFaction.getPowerRounded()) {
				mplayer.msg("�cA sua fac��o n�o tem poder suficiente para poder conquistar mais territ�rios.");
				event.setCancelled(true);
				return;
			}
			
			// Verificando se a fac��o j� claimou al�m do n�mero de mundos permitidos
			if (MConf.get().claimedWorldsMax > 0) {
				Set<String> oldWorlds = newFaction.getClaimedWorlds();
				Set<String> newWorlds = PS.getDistinctWorlds(chunks);
				Set<String> worlds = new MassiveSet<>();
				worlds.addAll(oldWorlds);
				worlds.addAll(newWorlds);
				if (!oldWorlds.containsAll(newWorlds) && worlds.size() > MConf.get().claimedWorldsMax) {
					String worldsMax = MConf.get().claimedWorldsMax == 1 ? "mundo diferente." : "mundos diferentes.";
					mplayer.msg("�cVoc� s� pode conquistar terras em %d %s", MConf.get().claimedWorldsMax, worldsMax);
					event.setCancelled(true);
					return;
				}
			}
			
			// Verificando se os claims precisam estar conectados, e verificando se a fac��o j� claimou algo naquele mundo
			if (MConf.get().claimsMustBeConnected && newFaction.getLandCountInWorld(chunks.iterator().next().getWorld()) > 0) {
				
				// Verificando se os claims n�o est�o conectados
				if (!BoardColl.get().isAnyConnectedPs(chunks, newFaction)) {
					
					// Verificando se o sistema de proteger terras de inimigos mesmo n�o estando conectadas esta ativado
					if (!MConf.get().claimsCanBeUnconnectedIfOwnedByOtherFaction) {
						mplayer.msg("�cVoc� s� poder conquistar terras que estejam conectadas �s suas.");
						event.setCancelled(true);
						return;
					}
					
					// Verificando se alguma das fac��es claimadas � normal
					boolean containsNormalFaction = BoardColl.containsNormalFaction(currentFactions);
					if (!containsNormalFaction) {
						mplayer.msg("�cVoc� s� poder conquistar terras que estejam conectadas �s suas ou que sejam pertencentes a outras fac��es.");
						event.setCancelled(true);
						return;
					}
				}
			}
			
			// Percorrendo todas as chunks e fac��es claimandas
			for (Entry<Faction, Set<PS>> entry : currentFactionChunks.entrySet()) {
				Faction oldFaction = entry.getKey();
				Set<PS> oldChunks = entry.getValue();

				// Verificando se a fac��o n�o � a zona livre
				// Caso a fac��o n�o seja a zona livre, ent�o o terreno esta sendo dominado de uma fac��o para outra
				if (!oldFaction.isNone()) {

					// Verificando se a fac��o n�o possui rela��es de tr�gua ou alian�a
					if (newFaction.getRelationTo(oldFaction) == Rel.ALLY) {
						mplayer.msg("�cVoc� n�o pode conquistar este territ�rio devido a sua rela��o com o atual dono do territ�rio. ");
						event.setCancelled(true);
						return;
					}
	
					// Verificando se a fac��o inimiga esta realmente com poder baixo
					if (oldFaction.getPowerRounded() > oldFaction.getLandCount() - oldChunks.size()) {
						mplayer.msg("�eA fac��o �f[%s�f]�e � dona deste territ�rio e � forte o bastante para mant�-lo.", oldFaction.getName());
						event.setCancelled(true);
						return;
					}
	
					// Verificando se a fac��o come�ou a dominar pela borda
					if (!BoardColl.get().isAnyBorderPs(chunks)) {
						mplayer.msg("�cVoc� deve come�ar a dominar as terras pelas bordas n�o pelo meio.");
						event.setCancelled(true);
						return;
					}
				}
			}
			
			// Pegando todas as chunks pr�ximas
			Set<PS> nearbyChunks = BoardColl.getNearbyChunks(chunks, MConf.get().claimMinimumChunksDistanceToOthers);
			nearbyChunks.removeAll(chunks);
			
			// Pegando todas as fac��es pr�ximas e pegando a permiss�o para claimar pr�ximo
			Set<Faction> nearbyFactions = BoardColl.getDistinctFactions(nearbyChunks);
			nearbyFactions.remove(FactionColl.get().getNone());
			nearbyFactions.remove(newFaction);
			MPerm claimnear = MPerm.getPermClaimnear();
			
			// Percorrendo todos as chunks claimadas
			for (Entry<Faction, Set<PS>> entry : currentFactionChunks.entrySet()) {
				
				// Verificando se a fac��o antiga n�o � a zona livre
				Faction oldFaction = entry.getKey();
				if (oldFaction.isNone()) {
					
					// Percorrendo todas as fac��es pr�ximas
					for (Faction nearbyFaction : nearbyFactions) {
	
						// Verificando se a fac��o tem permiss�o para claimar pr�ximo
						if (!claimnear.has(newFaction, nearbyFaction)) {
							mplayer.message(claimnear.createDeniedMessage(mplayer, nearbyFaction));
							event.setCancelled(true);
							return;
						}
					}
				}
			}
		}
	}
}