package com.massivecraft.factions.cmd;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.event.EventFactionsHomeTeleport;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
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
		
		// Descri��o
		this.setDesc("�6 home �e[fac��o] �8-�7 Teleporta para a home da fac��o.");

		// Requisitos
		this.addRequirements(RequirementIsPlayer.get());
		
		// Parametros (n�o necessario)
		this.addParameter(TypeString.get(), "outra fac��o", "sua fac��o", true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Faction faction = readFaction();
		
		// Verificando se a fac��o � a zona livre
		if (faction.isNone()) {
			msg("�cArgumentos insuficientes, use /f home <fac��o>");
			return;
		}
		
		// Verificando se o sender possui permiss�o
		if (!MPerm.getPermHome().has(msender, faction, true)) return;
		
		// Pegando a home da fac��o e a descri��o da a��o
		PS home = faction.getHome();
		String homeDesc = "�ehome da " + (msenderFaction == faction ? "�efac��o" : "�f[" + faction.getName() + "�f]");
		
		// Verificando se a fac��o possui permiss�o.
		if (home == null) {
			msg("�f" + (msenderFaction == faction ? "�cSua fac��o n�o possui a home definida." : "�f[" + faction.getName() + "�f]�c n�o possui a home da fac��o definida."));
			return;
		}
		
		// Verificando se o player esta em um territ�rio inimigo.
		if (!MConf.get().homesTeleportAllowedFromEnemyTerritory && msender.isInEnemyTerritory()) {
			msg("�eVoc� n�o pode se teleportar para %s �epois voc� esta em um territ�rio de uma fac��o inimiga.", homeDesc);
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
			MixinMessage.get().messageOne(me, e.getMessage());
		}
	}
	
}