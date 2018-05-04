package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.command.Visibility;

public class CmdFactionsPoder extends FactionsCommand
{
	{
    
	// Aliases
    this.addAliases("powerboost", "power", "pb");
    
	// Descri��o do comando
	this.setDesc("�6 poder �8-�7 Adiciona ou remove poder de um player ou fac��o.");
	
    // Visibilidade do comando
    this.setVisibility(Visibility.SECRET);
		
	}
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsPoderPlayer cmdFactionsPoderPlayer = new CmdFactionsPoderPlayer();
	public CmdFactionsPoderFaction cmdFactionsPoderFaction = new CmdFactionsPoderFaction();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsPoder()
	{
		// Child (filhos?)
		this.addChild(this.cmdFactionsPoderPlayer);
		this.addChild(this.cmdFactionsPoderFaction);
		
	    // Visibilidade do comando
	    this.setVisibility(Visibility.SECRET);
	}
	
}
