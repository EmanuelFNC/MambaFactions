package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsRebaixar extends FactionsCommand {

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRebaixar() {
		
		// Aliases
		this.addAliases("demotar", "demote", "demover");
		
		// Parametros (necessario)
		this.addParameter(TypeMPlayer.get(), "jogador");
		
		// Requisi��es
		this.addRequirements(ReqHasFaction.get());
		
		// Descri��o do comando
		this.setDesc("�6 rebaixar �e<player> �8-�7 Rebaixa um player de cargo.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException {
		MPlayer mp = this.readArg(msender);
		Faction msf = msender.getFaction();
		Faction mpf = mp.getFaction();
		
		// Verificando se o player possui permiss�o
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("�cVoc� precisar ser capit�o ou superior para poder administrar os cargos da fac��o.");
			return;
		}
		
		// Verificando se o target � da mesma fac�o que o sender
		if (msf != mpf) {
			msender.message("�cEste jogador n�o esta na sua fac��o.");
			return;
		}

		// Verificando se o sender e o target s�o a mesma pessoa
		if (msender == mp) {
			msender.message("�cVoc� n�o pode rebaixar voc� mesmo.");
			return;
		}
		
		Rel cargoms = msender.getRole();
		Rel cargomp = mp.getRole();

		// Verificando se o target � o l�der da fac��o
		if (cargomp == Rel.LEADER) {
			msender.message("�c" + cargomp.getName() + "�c � o l�der da fac��o portanto n�o pode ser rebaixado.");
			return;
		}

		// Verificando se o target j� � o cargo mais baixo (recruit)
		if (cargomp == Rel.RECRUIT) {
			msender.message("�cEste jogador j� esta no cargo mais baixo da fac��o caso queira expulsa-lo use /f expulsar �c" + mp.getName() + "�c" );
			return;
		}

		// Se o targe for member = sucesso
		if (cargomp == Rel.MEMBER) {
			msf.msg("�e" + msender.getRole().getPrefix() + msender.getName() + "�e rebaixou \"�e" + mp.getName() + "�e\" para o cargo de recruta da fac��o.");
			mp.setRole(Rel.RECRUIT);
			return;
		}

		// Verificando se o target � um capit�o e verificando ainda se o sender � um capit�o
		if (cargomp == Rel.OFFICER) {
			if (cargoms == Rel.OFFICER) {
				msender.message("�cApenas o l�der da fac��o pode rebaixar um capit�o.");
				return;
			}
			msf.msg("�e" + msender.getRole().getPrefix() + msender.getName() + "�e rebaixou \"�e" + mp.getName() + "�e\" para o cargo de membro da fac��o.");
			mp.setRole(Rel.MEMBER);
			return;
		}
	}

}
