package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsPromover extends FactionsCommand {

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsPromover() {
		
		// Aliases
		this.addAliases("promote", "up");
		
		// Parametros (necessario)
		this.addParameter(TypeMPlayer.get(), "jogador");
		
		// Requisi��es
		this.addRequirements(ReqHasFaction.get());
		
		// Descri��o do comando
		this.setDesc("�6 promover �e<player> �8-�7 Promove um player de cargo.");
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
		
		// Verificando se o sender e o target s�o a mesma pessoa
		if (msender == mp) {
			msender.message("�cVoc� n�o pode promover voc� mesmo.");
			return;
		}

		// Verificando se o target � da mesma fac�o que o sender
		if (msf != mpf) {
			msender.message("�cEste jogador n�o esta na sua fac��o.");
			return;
		}

		Rel cargoms = msender.getRole();
		Rel cargomp = mp.getRole();

		// Verificando se o target ja � l�der
		if (cargomp == Rel.LEADER) {
			msender.message("�c"+ mp.getName() + "�c j� � o l�der da fac��o.");
			return;
		}

		// Se o targe for recruit = sucesso
		if (cargomp == Rel.RECRUIT) {
			msf.msg("�e" + msender.getRole().getPrefix() + msender.getName() + "�e promoveu \"�e" + mp.getName() + "�e\" para o cargo de membro da fac��o.");
			mp.setRole(Rel.MEMBER);
			return;
		}

		// Verificando se o target � um membro e verificando ainda se o sender � capit�o
		if (cargomp == Rel.MEMBER) {
			if (cargoms == Rel.OFFICER) {
				msender.message("�cApenas o l�der da fac��o pode promover um membro para capit�o.");
				return;
			}
			msf.msg("�e" + msender.getRole().getPrefix() + msender.getName() + "�e promoveu \"�e" + mp.getName() + "�e\"�e para o cargo de capit�o da fac��o.");
			mp.setRole(Rel.OFFICER);
			return;
		}

		// Verificando se o target � um capit�o e verificando ainda se o sender � l�der
		if (cargomp == Rel.OFFICER) {
			if (cargoms == Rel.LEADER) {
				msender.message(
						"�cEste jogador j� � capit�o da fac��o, caso queira transferir a lideran�a da fac��o use /f transferir �c" +  mp.getName() + "�c");
				return;
			}
			msender.message("�cApenas o l�der da fac��o pode promover um capit�o para l�der.");
			return;
		}
	}

}
