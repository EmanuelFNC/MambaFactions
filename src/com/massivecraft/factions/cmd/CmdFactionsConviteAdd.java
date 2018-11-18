package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Invitation;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsInvitedChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.util.IdUtil;

public class CmdFactionsConviteAdd extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsConviteAdd()
	{
		// Aliases
	    this.addAliases("a", "add", "enviar", "adicionar");
		
		// Descri��o
		this.setDesc("�6 convite add �e<player> �8-�7 Envia um convite para um player.");
		
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
			msg("�cVoc� precisar ser capit�o ou superior para poder gerenciar os convites da fac��o.");
			return;
		}
		
		// Verficiando se os argumentos s�o validos
		if (!this.argIsSet()) {
			msg("�cArgumentos insuficientes, use /f convite add <player>");
			return;
		}
		
		// Verificando se a fac��o n�o exedeu o limite de convites
		if (msenderFaction.getInvitations().size() >= 21) {
			msg("�cLimite m�ximo de convites pendentes atingido (21)! Apague alguns convites in�teis para poder enviar novos convites.");
			return;
		}
		
		// Verificando se o sender e o target s�o a mesma pessoa
		String name = this.arg();
		if (msender.getName().equalsIgnoreCase(name)) {
			msg("�cVoc� n�o pode enviar um convite para voc� mesmo.");
			return;
		}
		
		// Argumentos
		MPlayer mplayer = readMPlayer(name);
			
		// Verificando se o player j� � um membro
		if (mplayer.getFaction() == msenderFaction) {
			msg("�c'%s'�c j� � membro da sua fac��o.", mplayer.getName());
			return;
		}
			
		// Verificando se o player j� esta possui um convite
		boolean isInvited = msenderFaction.isInvited(mplayer);
			
		if (!isInvited) {
			
			// Verificando se o player j� n�o possui muitos conites
			if (mplayer.getInvitations().size() >= 21) {
				msg("�cO player '" +  mplayer.getName() + "' j� antingiu o limite m�ximo de convites de fac��es pendentes (21), pe�a para que ele apague alguns convites in�teis para que voc� possa enviar o seu.");
				return;
			}
			
			// Evento
			EventFactionsInvitedChange event = new EventFactionsInvitedChange(sender, mplayer, msenderFaction, isInvited);
			event.run();
			if (event.isCancelled()) return;
			isInvited = event.isNewInvited();
				
			// Informando o sender e o target
			mplayer.msg("�a%s�a convidou voc� para entrar na fac��o �f[%s�f]�a.", msender.getRole().getPrefix() + msender.getName(), msenderFaction.getName());
			msg("�aConvite enviado com sucesso para '%s'.", mplayer.getName());
				
			// Aplicando o evento
			String senderId = IdUtil.getId(sender);
			long creationMillis = System.currentTimeMillis();
			Invitation invitation = new Invitation(senderId, creationMillis);
			msenderFaction.invite(mplayer, invitation);
			msenderFaction.changed();
		}
		else {
			msg("�c'%s'�c j� possui um convite para entrar na sua fac��o.", mplayer.getName());
		}
	}
	
}