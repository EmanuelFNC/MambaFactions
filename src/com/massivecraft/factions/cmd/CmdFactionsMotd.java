package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsMotdChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeNullable;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsMotd extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMotd()
	{
		// Aliases
        this.addAliases("mensagem");
		
		// Descri��o
		this.setDesc("�6 motd �e<mensagem> �8-�7 Altera a mensagem da fac��o.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (n�o necessario)
		this.addParameter(TypeNullable.get(TypeString.get()), "motd", "erro", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Verificando se o player possui permiss�o
		if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msg("�cVoc� precisar ser capit�o ou superior para poder alterar a mensagem diaria da fac��o.");
			return;
		}
		
		// Lendo os argumanetos e verificando se o argumento � nulo
		if (!this.argIsSet()) {
			msg("�cArgumentos insuficientes, use /f motd <mensagem>");
			return;
		}
		
		// Argumentos
		String newMotd = Txt.parse(this.arg()).trim().replace('&', '�');
		
		// Verificando se a descri��o antiga n�o � igual anova
		if (msenderFaction.getMotdDesc().equals(newMotd)) {
			msg("�cA motd da fac��o j� � '" + newMotd + "�c'.");
			return;
		}
			
		// Evento
		EventFactionsMotdChange event = new EventFactionsMotdChange(sender, msenderFaction, newMotd);
		event.run();
		if (event.isCancelled()) return;
		newMotd = event.getNewMotd();
		
		// Aplicando o evento
		msenderFaction.setMotd(newMotd);
		
		// Informando os players
		for (MPlayer follower : msenderFaction.getMPlayersWhereOnline(true)) {
			follower.msg("�e%s �edefiniu a motd da fac��o para:\n�7'�f%s�7'", msender.getRole().getPrefix() + msender.getName(), newMotd);
		}
	}
	
}