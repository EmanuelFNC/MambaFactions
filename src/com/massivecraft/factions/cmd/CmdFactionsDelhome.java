package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.event.EventFactionsHomeChange;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsDelhome extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDelhome()
	{
		// Aliases
		this.addAliases("unsethome", "removerbase", "delbase");
		
		// Descri��o do comando
		this.setDesc("�6 delhome �8-�7 Deleta a home da fac��o.");
		
		// Requisi��es
		this.addRequirements(ReqHasFaction.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{		
		// Verificando se o player possui permiss�o
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("�cVoc� precisar ser capit�o ou superior para poder deletar a home da fac��o.");
			return;
		}
				
		// Verificando se a fac��o possui home
		if ( ! msenderFaction.hasHome())
		{
			msender.msg("�cA sua fac��o ainda n�o definiu a home da fac��o.");
			return;
		}
		
		// Evento
		EventFactionsHomeChange event = new EventFactionsHomeChange(sender, msenderFaction, null);
		event.run();
		if (event.isCancelled()) return;

		// Aplicando o evento
		msenderFaction.setHome(null);
		
		// Informando os players da fac��o
		msenderFaction.msg("�e%s�e deletou a home da fac��o!", msender.getRole().getPrefix() + msender.getName());
	}
	
}
