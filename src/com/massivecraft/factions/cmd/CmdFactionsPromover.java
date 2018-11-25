package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mson.Mson;

public class CmdFactionsPromover extends FactionsCommand 
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsPromover() 
	{	
		// Aliases
		this.addAliases("promote", "up");
		
		// Descri��o
		this.setDesc("�6 promover �e<player> �8-�7 Promove um player de cargo.");
		
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
		if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msg("�cVoc� precisar ser capit�o ou superior para poder administrar os cargos da fac��o.");
			return;
		}
		
		// Verficiando se os argumentos s�o validos
		if (!this.argIsSet()) {
			msg("�cArgumentos insuficientes, use /f promover <player>");
			return;
		}
		
		// Verificando se o sender e o target s�o a mesma pessoa
		String name = this.arg();
		if (msender.getName().equalsIgnoreCase(name)) {
			msg("�cVoc� n�o pode promover voc� mesmo.");
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

		// Verificando se o target ja � l�der
		if (cargomp == Rel.LEADER) {
			msg("�c"+ cargomp.getPrefix() + target.getName() + "�c j� � o l�der da fac��o.");
			return;
		}

		// Se o target for recruit = sucesso
		if (cargomp == Rel.RECRUIT) {
			msender.msg("�aPlayer promovido com sucesso para o cargo de Membro.");
			target.msg("�aVoc� foi promovido para o cargo de Membro por " + cargoms.getPrefix() + msender.getName() + ".");
			target.setRole(Rel.MEMBER);
			return;
		}

		// Verificando se o target � um membro e verificando ainda se o sender � capit�o
		if (cargomp == Rel.MEMBER) {
			if (cargoms == Rel.OFFICER) {
				msg("�cApenas o l�der da fac��o pode promover um membro para capit�o.");
				return;
			}
			msender.msg("�aPlayer promovido com sucesso para o cargo de Capit�o.");
			target.msg("�aVoc� foi promovido para o cargo de Capit�o por " + cargoms.getPrefix() + msender.getName() + ".");
			target.setRole(Rel.OFFICER);
			return;
		}

		// Verificando se o target � um capit�o e verificando ainda se o sender � l�der
		if (cargomp == Rel.OFFICER) {
			if (cargoms == Rel.LEADER) {
				message(Mson.parse("�cEste jogador j� � capit�o da fac��o, caso queira transferir a lideran�a da fac��o use /f transferir �c" +  target.getName()).suggest("/f transferir " + target.getName()));
				return;
			}
			msg("�cApenas o l�der da fac��o pode promover um capit�o para l�der.");
			return;
		}
	}

}
