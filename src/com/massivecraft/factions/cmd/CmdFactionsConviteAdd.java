package com.massivecraft.factions.cmd;

import java.util.Collection;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Invitation;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsInvitedChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.TypeSet;
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
		
		// Descri��o do comando
		this.setDesc("�6 convite add �e<player> �8-�7 Envia um convite para um player.");
		
		// Parametros (necessario)
		this.addParameter(TypeSet.get(TypeMPlayer.get()), "players", true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //	
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Collection<MPlayer> mplayers = this.readArg();
		
		// Variaveis
		String senderId = IdUtil.getId(sender);
		long creationMillis = System.currentTimeMillis();
		
		// Verificando se o player possui permiss�o
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("�cVoc� precisar ser capit�o ou superior para poder gerenciar os convites da fac��o.");
			return;
		}
		
		// Verificando se a fac��o n�o exedeu o limite de convites
		if(msenderFaction.getInvitations().size() >= 10) {
			msender.message("�cLimite m�ximo de convites pendentes atingido (10)! Apague alguns convites in�teis�para poder enviar novos convites.");
			return;
		}
		
		for (MPlayer mplayer : mplayers)
		{	
			
			// Verificando se o sender e o target s�o a mesma pessoa
			if (msender == mplayer) {
				msender.message("�cVoc� j� faz parte de uma fac��o e voc� n�o pode adicionar um convite para voc� mesmo.");
				continue;
			}
			
			// Verificando se o player j� � um membro
			if (mplayer.getFaction() == msenderFaction)
			{
				msg("�c\"%s\"�c j� � membro da sua fac��o.", mplayer.getName());
				continue;
			}
			
			// Verificando se o player j� esta possui um convite
			boolean isInvited = msenderFaction.isInvited(mplayer);
			
			if ( ! isInvited)
			{
				// Evento
				EventFactionsInvitedChange event = new EventFactionsInvitedChange(sender, mplayer, msenderFaction, isInvited);
				event.run();
				if (event.isCancelled()) continue;
				isInvited = event.isNewInvited();
				
				// Informando o sender e o target
				mplayer.msg("�a%s�a convidou voc� para entrar na fac��o �f%s�a.", msender.getName(), msenderFaction.getName());
				msenderFaction.msg("�e%s�e convidou �e\"%s\"�e para entrar na sua fac��o.", msender.getRole().getPrefix() + msender.getName(), mplayer.getName());
				
				// Aplicando o evento
				Invitation invitation = new Invitation(senderId, creationMillis);
				msenderFaction.invite(mplayer.getId(), invitation);
				msenderFaction.changed();
			}
			else
			{
				// Informando que o player ja possui 1 convite
				msg("�c\"%s\"�c j� possui um convite para entrar na sua fac��o.", mplayer.getName());
			}
		}
	}
	
}
