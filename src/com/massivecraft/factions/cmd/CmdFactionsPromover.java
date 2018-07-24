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
		
		// Verificando se o player possui permiss�o
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("�cVoc� precisar ser capit�o ou superior para poder administrar os cargos da fac��o.");
			return;
		}
		
		// Argumentos
		MPlayer target = this.readArg(msender);
		Faction facSender = msender.getFaction();
		Faction facTarget = target.getFaction();
		
		// Verificando se o sender e o target s�o a mesma pessoa
		if (msender == target) {
			msender.message("�cVoc� n�o pode promover voc� mesmo.");
			return;
		}

		// Verificando se o target � da mesma fac�o que o sender
		if (facSender != facTarget) {
			msender.message("�cEste jogador n�o esta na sua fac��o.");
			return;
		}

		Rel cargoms = msender.getRole();
		Rel cargomp = target.getRole();

		// Verificando se o target ja � l�der
		if (cargomp == Rel.LEADER) {
			msender.message("�c"+ target.getName() + "�c j� � o l�der da fac��o.");
			return;
		}

		// Se o targe for recruit = sucesso
		if (cargomp == Rel.RECRUIT) {
			facSender.msg("�e" + msender.getRole().getPrefix() + msender.getName() + "�e promoveu \"�e" + target.getName() + "�e\" para o cargo de membro da fac��o.");
			target.setRole(Rel.MEMBER);
			return;
		}

		// Verificando se o target � um membro e verificando ainda se o sender � capit�o
		if (cargomp == Rel.MEMBER) {
			if (cargoms == Rel.OFFICER) {
				msender.message("�cApenas o l�der da fac��o pode promover um membro para capit�o.");
				return;
			}
			facSender.msg("�e" + msender.getRole().getPrefix() + msender.getName() + "�e promoveu \"�e" + target.getName() + "�e\"�e para o cargo de capit�o da fac��o.");
			target.setRole(Rel.OFFICER);
			return;
		}

		// Verificando se o target � um capit�o e verificando ainda se o sender � l�der
		if (cargomp == Rel.OFFICER) {
			if (cargoms == Rel.LEADER) {
				msender.message(
						"�cEste jogador j� � capit�o da fac��o, caso queira transferir a lideran�a da fac��o use /f transferir �c" +  target.getName() + "�c");
				return;
			}
			msender.message("�cApenas o l�der da fac��o pode promover um capit�o para l�der.");
			return;
		}
	}

}
