package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsConviteListar extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsConviteListar()
	{
		// Aliases
	    this.addAliases("ver", "list");
		
		// Descri��o
		this.setDesc("�6 convite listar �8-�7 Mostra a lista de convites pendentes.");
		
		// Parametros (n�o necessario)
		this.addParameter(TypeString.get(), "null", "null", true);
		
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
		// Verificando se a fac��o possui convites pendentes
		if (msenderFaction.getInvitations().isEmpty()) {
			msg("�cSua fac��o n�o possui convites pendentes!");
			return;
		}
		
		// Abrindo o menu dos convites
		EngineMenuGui.get().abrirMenuConvitesEnviados(msenderFaction, msender);
	}
}
