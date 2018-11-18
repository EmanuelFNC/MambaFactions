package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuPermissoes;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsPermissoes extends FactionsCommand{


	public CmdFactionsPermissoes()
	{
		// Aliases
		this.addAliases("perm");

		// Descri��o
		this.setDesc("�6 perm �8-�7 Gerencia as permiss�es da fac��o.");
		
		// Requisitos
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
	}


	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Abrindo o menu geral das permiss�es
		EngineMenuPermissoes.get().abrirMenuPermissoes(msender, msenderFaction);
	}
}
