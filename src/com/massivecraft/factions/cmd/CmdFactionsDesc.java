package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsDescriptionChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsDesc extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDesc()
	{
		// Aliases
		this.addAliases("descricao", "description");

		// Descri��o
		this.setDesc("�6 desc �e<desc> �8-�7 Altera a descri��o da fac��o.");

		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (necessario)
		this.addParameter(TypeString.get(), "descricao", "erro", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{					
		// Verificando se o player possui permiss�o
		if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msg("�cVoc� precisar ser capit�o ou superior para poder alterar a descri��o da fac��o.");
			return;
		}
				
		// Verificando se os argumentos n�o s�o nulos
		if (!this.argIsSet()) {
			msg("�cArgumentos insuficientes, use /f desc <descri��o>");
			return;
		}
		
		// Argumentos
		String newDescription = Txt.parse(this.arg()).replace('&', '�');
		
		// Verificando se a descri��o antiga n�o � igual anova
		if (msenderFaction.getDescriptionDesc().equals(newDescription)) {
			msg("�cA descri��o da fac��o j� � '" + newDescription + "�c'.");
			return;
		}
			
		// Evento
		EventFactionsDescriptionChange event = new EventFactionsDescriptionChange(sender, msenderFaction, newDescription);
		event.run();
		if (event.isCancelled()) return;
		newDescription = event.getNewDescription();
	
		// Aplicando evento
		msenderFaction.setDescription(newDescription);
			
		// Informando a fac��o
		for (MPlayer mp : msenderFaction.getMPlayersWhereOnline(true)) {
			mp.msg("�e%s�e definiu a descri��o da fac��o para:\n�7�l'�f%s�7�l'", msender.getRole().getPrefix() + msender.getName(), msenderFaction.getDescriptionDesc());
		}
	}
}
