package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;

public class CmdFactionsUnclaim extends FactionsCommand
{
	{

	// Aliases
    this.addAliases("desproteger", "abandonar");

	// Descri��o do comando
	this.setDesc("�6 unclaim �8-�7 Abandona territ�rios da sua fac��o.");
	
	// Requisi��es
	this.addRequirements(ReqHasFaction.get());
    
	}
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsSetOne cmdFactionsUnclaimOne = new CmdFactionsSetOne(false);
	public CmdFactionsSetAll cmdFactionsUnclaimAll = new CmdFactionsSetAll(false);
}
