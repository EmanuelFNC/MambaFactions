package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsTransferir extends FactionsCommand 
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsTransferir() 
	{
		// Aliases
		this.addAliases("lider", "lideran�a", "lideranca");
		
		// Descri��o
		this.setDesc("�6 transferir �e<player> �8-�7 Transfere a lideran�a da fac��o.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (necessario)
		this.addParameter(TypeString.get(), "player", "erro", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException 
	{
		// Verificando se o sender � lider da fac��o
		if (msender.getRole() != Rel.LEADER) {
			msg("�cApenas o l�der da fac��o pode promover uma novo l�der.");
			return;
		}
		
		// Verficiando se os argumentos s�o validos
		if (!this.argIsSet()) {
			msg("�cArgumentos insuficientes, use /f transferir <player>");
			return;
		}
		
		// Verificando se o sender e o target s�o a mesma pessoa
		String name = this.arg();
		if (msender.getName().equalsIgnoreCase(name)) {
			msg("�cVoc� n�o pode transferir a lideran�a para voc� mesmo");
			return;
		}

		// Argumentos
		MPlayer target = readMPlayer(name);
		Faction facSender = msender.getFaction();
		Faction facTarget = target.getFaction();

		// Verificando se o target � da mesma fac�o que o sender
		if (facSender != facTarget) {
			msg("�cEste jogador n�o esta na sua fac��o.");
			return;
		}
		
		// Aplicando o evento
		msender.setRole(Rel.OFFICER);
		target.setRole(Rel.LEADER);
		
		// Informando o sender e o target
		facSender.msg("�e" + msender.getName() + "�e transferiu a lidera��o da fac��o para �f" + target.getName() + "�e.");
	}
}
