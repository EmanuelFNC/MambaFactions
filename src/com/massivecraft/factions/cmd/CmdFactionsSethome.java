package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
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

		// Requisi��es
		this.addRequirements(RequirementIsPlayer.get());

		// Descri��o do comando
		this.setDesc("�6 sethome �8-�7 Define a home da fac��o.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		Faction faction = msenderFaction;
		
		PS newHome = PS.valueOf(me.getLocation());
		
		// Verificando se o player possui permiss�o
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER)) {
			msender.message("�cVoc� precisar ser capit�o ou superior para poder definir a home da fac��o.");
			return;
		}
		
		// Por algum motivo esta verifica��o n�o funciona direito
		// No entando criamos nossa pr�pria verifica��o na classe EngimeEditSource
		if (!faction.isValidHome(newHome))
		{
			msender.msg("�cVoc� s� pode definir a home da fac��o dentro dos territ�rios da sua fac��o.");
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
