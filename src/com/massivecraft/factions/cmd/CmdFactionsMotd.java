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
        
		// Parametros (n�o necessario)
		this.addParameter(TypeNullable.get(TypeString.get()), "novaMotd", "erro", true);
		
		// Requisi��es
		this.addRequirements(ReqHasFaction.get());
		
		// Descri��o do comando
		this.setDesc("�6 motd �e[mensagem] �8-�7 Altera ou mostra a mensagem da fac��o.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		
		// Verificando se o player possui permiss�o
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("�cVoc� precisar ser capit�o ou superior para poder alterar a motd da fac��o.");
			return;
		}
		
		// Lendo os argumanetos e verificando se o argumento � nulo
		if (!this.argIsSet(0)) 
		{
			msender.msg("�cArgumentos insuficientes, use /f motd <mensagem>");
			return;
		}
		
		// Argumentos
		String target = this.readArg();

		target = target.trim();
		target = Txt.parse(target);

		// Evento
		EventFactionsMotdChange event = new EventFactionsMotdChange(sender, msenderFaction, target);
		event.run();
		if (event.isCancelled()) return;
		target = event.getNewMotd().replace("�", "&");
		
		// Aplicando o evento
		msenderFaction.setMotd(target);
		
		// Informando os players
		for (MPlayer follower : msenderFaction.getMPlayers())
		{
			follower.msg("�e%s �edefiniu a motd da fac��o para:\n�7'�f%s�7'", msender.getRole().getPrefix() + msender.getName(), msenderFaction.getMotdDesc());
		}
	}
	
}
