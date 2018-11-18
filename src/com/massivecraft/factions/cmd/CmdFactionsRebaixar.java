package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsRebaixar extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRebaixar() 
	{
		// Aliases
		this.addAliases("demotar", "demote", "demover");
		
		// Descri��o
		this.setDesc("�6 rebaixar �e<player> �8-�7 Rebaixa um player de cargo.");
		
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
		// Verificando se o player possui permiss�o
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msg("�cVoc� precisar ser capit�o ou superior para poder administrar os cargos da fac��o.");
			return;
		}
		
		// Verficiando se os argumentos s�o validos
		if (!this.argIsSet()) {
			msg("�cArgumentos insuficientes, use /f rebaixar <player>");
			return;
		}
		
		// Verificando se o sender e o target s�o a mesma pessoa
		String name = this.arg();
		if (msender.getName().equalsIgnoreCase(name)) {
			msg("�cVoc� n�o pode rebaixar voc� mesmo.");
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
		
		Rel cargoms = msender.getRole();
		Rel cargomp = target.getRole();

		// Verificando se o target � o l�der da fac��o
		if (cargomp == Rel.LEADER) {
			msg("�c" + cargomp.getPrefix() + target.getName() + "�c � o l�der da fac��o portanto n�o pode ser rebaixado.");
			return;
		}

		// Verificando se o target j� � o cargo mais baixo (recruit)
		if (cargomp == Rel.RECRUIT) {
			msg("�cEste jogador j� esta no cargo mais baixo da fac��o, caso queira expulsa-lo use /f expulsar �c" + target.getName());
			return;
		}

		// Se o targe for member = sucesso
		if (cargomp == Rel.MEMBER) {
			msender.msg("�aPlayer rebaixado com sucesso para o cargo de Recruta.");
			target.msg("�eVoc� foi rebaixado para o cargo de Recruta por " + cargoms.getPrefix() + msender.getName() + ".");
			target.setRole(Rel.RECRUIT);
			return;
		}

		// Verificando se o target � um capit�o e verificando ainda se o sender � um capit�o
		if (cargomp == Rel.OFFICER) {
			if (cargoms == Rel.OFFICER) {
				msg("�cApenas o l�der da fac��o pode rebaixar um capit�o.");
				return;
			}
			msender.msg("�aPlayer rebaixado com sucesso para o cargo de Membro.");
			target.msg("�eVoc� foi rebaixado para o cargo de Membro por " + cargoms.getPrefix() + msender.getName() + ".");
			target.setRole(Rel.MEMBER);
			return;
		}
	}

}