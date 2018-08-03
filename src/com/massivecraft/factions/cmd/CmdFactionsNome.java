package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeFactionNameLenient;
import com.massivecraft.factions.engine.EngineSobAtaque;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.event.EventFactionsNameChange;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsNome extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsNome()
	{
		// Parameters
		this.addParameter(TypeFactionNameLenient.get(), "novoNome");

		// Aliases
        this.addAliases("name", "renomear", "rename");

		// Requisi��es
		this.addRequirements(ReqHasFaction.get());
        
		// Descri��o do comando
		this.setDesc("�6 nome �e<nome> �8-�7 Altera o nome da fac��o.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Verificando se o player possui permiss�o
		if (!(msender.getRole() == Rel.LEADER || msender.isOverriding())) {
			msender.message("�cApenas o l�der da fac��o pode alterar o nome da fac��o.");
			return;
		}
		
		// Verificando se a fac��o n�o esta sob ataque
		if (EngineSobAtaque.factionattack.containsKey(msenderFaction.getName())) {
			msender.message("�cVoc� n�o pode alterar o nome da sua fac��o enquanto ela estiver sobre ataque!");
			return;
		}
		
		// Argumentos
		String newName = this.readArg();
		Faction faction = msenderFaction;
		
		// Evento
		EventFactionsNameChange event = new EventFactionsNameChange(sender, faction, newName);
		event.run();
		if (event.isCancelled()) return;
		newName = event.getNewName();

		// Applicando evento
		faction.setName(newName);

		// Informando a fac��o
		faction.msg("�e%s�e definiu o nome da fac��o para �f%s�e.", msender.getRole().getPrefix() + msender.getName(), newName);
	}
	
}
