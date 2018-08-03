package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsGeradores extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsGeradores() 
	{	    
		// Requisi��es
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		
		// Descri��o do comando
		this.setDesc("�6 geradores �8-�7 Administra os geradores da fac��o.");
		
	    // Visibilidade do comando
		if (!MConf.get().colocarIconeDoFGeradoresNoMenuGUI)
		{
		    this.setVisibility(Visibility.INVISIBLE);
		}
		
		/*
		 * A unica raz�o deste comando estar aqui
		 * � porque n�s queriamos mostrar o comando /f geradores
		 * na lista de comandos do /f ajuda
		 * 
		 * Voc� pode ignorar isso ou apagar se quiser :D
		 */
	}
}
