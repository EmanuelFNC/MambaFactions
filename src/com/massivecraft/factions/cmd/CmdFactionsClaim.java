package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsClaim extends FactionsCommand
{	
	{

	// Aliases
    this.addAliases("proteger", "conquistar", "dominar");
    
	// Descri��o do comando
	this.setDesc("�6 claim �8-�7 Conquista territ�rios para a sua fac��o.");
	
	// Requisi��es
	this.addRequirements(ReqHasFaction.get());
	this.addRequirements(RequirementIsPlayer.get());
	
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsSetOne cmdFactionsClaimOne = new CmdFactionsSetOne(true);
	public CmdFactionsSetAuto cmdFactionsClaimAuto = new CmdFactionsSetAuto(true);
}
