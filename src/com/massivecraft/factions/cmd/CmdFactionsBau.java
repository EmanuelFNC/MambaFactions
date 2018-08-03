package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsBau extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsBau() 
	{
		// Aliases
	    this.addAliases("chest");
	    
		// Requisi��es
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		
		// Descri��o do comando
		this.setDesc("�6 bau �8-�7 Abre o ba� virtual da fac��o.");
		
	    // Visibilidade do comando
		if (!MConf.get().colocarIconeDoFBauNoMenuGUI)
		{
		    this.setVisibility(Visibility.INVISIBLE);
		}
		
		/*
		 * A unica raz�o deste comando estar aqui
		 * � porque n�s queriamos mostrar o comando /f bau
		 * na lista de comandos do /f ajuda
		 * 
		 * Voc� pode ignorar isso ou apagar se quiser :D
		 */
	}
}
