package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.event.EventFactionsHomeTeleport;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.mixin.MixinTeleport;
import com.massivecraft.massivecore.mixin.TeleporterException;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.teleport.Destination;
import com.massivecraft.massivecore.teleport.DestinationSimple;

public class CmdFactionsHome extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsHome()
	{
		// Aliases
		this.addAliases("base", "h");
		
		// Parametros (n�o necessario)
		this.addParameter(TypeFaction.get(), "fac��o", "voc�");

		// Requisi��es
		this.addRequirements(RequirementIsPlayer.get());
		
		// Descri��o do comando
		this.setDesc("�6 home �e[fac��o] �8-�7 Teleporta para a home da fac��o.");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		
		// Argumentos
		Faction faction = this.readArg(msenderFaction);
		PS home = faction.getHome();
		String homeDesc = "�ehome da " + (msenderFaction == faction ? "�esua fac��o" : "�efac��o �f" + faction.getName());
		
		// Verificando se o sender possui permiss�o
		if ( ! MPerm.getPermHome().has(msender, faction, true)) return;
		
		// Verificando se a fac��o possui permiss�o.
		if (home == null)
		{
			msender.msg("�f" + (msenderFaction == faction ? "�cSua fac��o" : faction.getName()) + "�c ainda n�o definiu a home da fac��o.");
			return;
		}
		
		// Verificando se o player esta em um territ�rio inimigo.
		if ( ! MConf.get().homesTeleportAllowedFromEnemyTerritory && msender.isInEnemyTerritory())
		{
			msender.msg("�eVoc� n�o pode se teleportar para %s �epois voc� esta em um territ�rio de uma fac��o inimiga.", homeDesc);
			return;
		}

		// Evento
		EventFactionsHomeTeleport event = new EventFactionsHomeTeleport(sender);
		event.run();
		if (event.isCancelled()) return;
		
		// Aplicando o evento
		try
		{
			Destination destination = new DestinationSimple(home, homeDesc);
			MixinTeleport.get().teleport(me, destination, MConf.get().homeSeconds);
		}
		catch (TeleporterException e)
		{
			String message = e.getMessage();
			MixinMessage.get().messageOne(me, message);
		}
	}
	
}
