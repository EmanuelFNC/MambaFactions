package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;

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
		msender.leave();
	}
	
}
