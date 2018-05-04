package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.massivecore.command.Visibility;

public class CmdFactionsPoderFaction extends CmdFactionsPoderAbstract
{
	
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsPoderFaction()
	{
		super(TypeFaction.get(), "faction");
		
        // Visibilidade do comando
        this.setVisibility(Visibility.SECRET);
        
    	// Descri��o do comando
    	this.setDesc("�6 poder f �e<fac��o> <quantia> �8-�7 Adiciona poder a um fac��o.");
	}
	
}
