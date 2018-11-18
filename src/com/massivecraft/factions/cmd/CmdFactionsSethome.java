package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.event.EventFactionsHomeChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsSethome extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSethome()
	{
		// Aliases
		this.addAliases("definirhome", "definirbase", "setbase");

		// Descri��o
		this.setDesc("�6 sethome �8-�7 Define a home da fac��o.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(RequirementIsPlayer.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Verificando se o player possui permiss�o
		if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER)) {
			msg("�cVoc� precisar ser capit�o ou superior para poder definir a home da fac��o.");
			return;
		}
		
		// Argumentos
		Faction faction = msenderFaction;
		PS newHome = PS.valueOf(me.getLocation());
		
		// Por algum motivo esta verifica��o n�o funciona direito
		if (!faction.isValidHome(newHome)) {
			msg("�cVoc� s� pode definir a home da fac��o dentro dos territ�rios da fac��o.");
			return;
		}
		
		// Evento
		EventFactionsHomeChange event = new EventFactionsHomeChange(sender, faction, newHome);
		event.run();
		if (event.isCancelled()) return;
		newHome = event.getNewHome();

		// Aplicando o evento
		faction.setHome(newHome);
		
		// Informando a fac��o
		faction.msg("�a%s�a definiu a nova home da fac��o!", msender.getRole().getPrefix() + msender.getName());
	}
	
}