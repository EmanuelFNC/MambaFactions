package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineSobAtaque;

public class CmdFactionsSair extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSair()
	{
		// Aliases
        this.addAliases("leave", "deixar");
        
		// Requisi��es
		this.addRequirements(ReqHasFaction.get());

		// Descri��o do comando
		this.setDesc("�6 sair �8-�7 Abandona a sua fac��o atual.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		
		// Verificando se a fac��o n�o esta sob ataque
		if (EngineSobAtaque.factionattack.containsKey(msenderFaction.getName())) {
			msender.message("�cVoc� n�o pode abandonar a sua fac��o enquanto ela estiver sobre ataque!");
			return;
		}
		
		// Saindo da fac��o
		msender.leave();
	}
	
}
