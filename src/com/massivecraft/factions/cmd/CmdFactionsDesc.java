package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsDescriptionChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeNullable;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsDesc extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDesc()
	{
		// Aliases
		this.addAliases("descricao", "description");

		// Parametros (necessario)
		this.addParameter(TypeNullable.get(TypeString.get()), "novaDesc", "erro", true);

		// Requisi��es
		this.addRequirements(ReqHasFaction.get());

		// Descri��o do comando
		this.setDesc("�6 desc �e<desc> �8-�7 Altera a descri��o da fac��o.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{	
		// Argumentos
		for (String newDescription: this.getArgs()) {
		
			
		// Verificando se o player possui permiss�o
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("�cVoc� precisar ser capit�o ou superior para poder alterar a descri��o da fac��o.");
			return;
		}
			
		// Verificando se os argumentos n�o s�o nulos
		if (!this.argIsSet(0)) 
		{
			msender.msg("�cArgumentos insuficientes, use /f desc <descri��o>");
			return;
		}
		
		// Evento
		EventFactionsDescriptionChange event = new EventFactionsDescriptionChange(sender, msenderFaction, newDescription);
		event.run();
		if (event.isCancelled()) return;
		newDescription = event.getNewDescription().replace("&", "�");

		// Aplicando evento
		msenderFaction.setDescription(newDescription);
		
		// Informando a fac��o
		for (MPlayer follower : msenderFaction.getMPlayers())
		{
			follower.msg("�e%s �edefiniu a descri��o da fac��o para:\n�7'�f%s�7'", msender.getRole().getPrefix() + msender.getName(), msenderFaction.getDescriptionDesc());
		}
	}
	}
}
