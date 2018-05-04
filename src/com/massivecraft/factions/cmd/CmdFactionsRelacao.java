package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;

public class CmdFactionsRelacao extends FactionsCommand
{
	{

	// Aliases
    this.addAliases("rela��o", "relation", "rel");

	// Requisi��es
	this.addRequirements(ReqHasFaction.get());
    
	// Descri��o do comando
	this.setDesc("�6 relacao �8-�7 Gerencia as rela��es da fac��o.");
	
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public CmdFactionsRelacaoDefinir cmdFactionsRelacaoDefinir = new CmdFactionsRelacaoDefinir();
	public CmdFactionsRelacaoListar cmdFactionsRelacaoListar = new CmdFactionsRelacaoListar();

}
