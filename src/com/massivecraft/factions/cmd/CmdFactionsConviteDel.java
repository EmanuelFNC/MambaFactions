package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsInvitedChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsConviteDel extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsConviteDel()
	{
		// Aliases
	    this.addAliases("delete", "remove", "remover", "deletar");
		
		// Descri��o
		this.setDesc("�6 convite del �e<player> �8-�7 Deleta um convite enviado.");
		
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
			msg("�cVoc� precisar ser capit�o ou superior para poder deletar convites enviados.");
			return;
		}
		
		// Verficiando se os argumentos s�o validos
		if (!this.argIsSet()) {
			msg("�cArgumentos insuficientes, use /f convite del <player>");
			return;
		}
			
		// Verificando se o sender e o target s�o a mesma pessoa
		String name = this.arg();
		if (msender.getName().equalsIgnoreCase(name)) {
			msg("�cVoc� j� faz parte de uma fac��o e voc� n�o pode remover um convite enviado a voc�.");
			return;
		}
		
		// Argumentos
		MPlayer mplayer = readMPlayer(name);

		// Verificando se o player ja � membro da fac��o
		if (mplayer.getFaction() == msenderFaction) {				
			msg("�c'%s'�c j� � membro da sua fac��o.", mplayer.getName());
			return;
		}
			
		// Verificando se o player esta convidado
		boolean isInvited = msenderFaction.isInvited(mplayer);
			
		if (isInvited) {
			// Evento
			EventFactionsInvitedChange event = new EventFactionsInvitedChange(sender, mplayer, msenderFaction, isInvited);
			event.run();
			if (event.isCancelled()) return;
			isInvited = event.isNewInvited();
				
			// Informando o sender e o target
			mplayer.msg("�e%s�e removeu seu convite para entrar na fac��o �f[%s�f]�e.", msender.getRole().getPrefix() + msender.getName(), msenderFaction.getName());
			msg("�aConvite para �a'%s'�a deletado com sucesso.", mplayer.getName());
			
			// Aplicando o evento
			msenderFaction.uninvite(mplayer);
			msenderFaction.changed();
		} 
		else {			
			msg("�c'%s'�c n�o possui um convite para entrar na sua fac��o.", mplayer.getName());
		}
	}
	
}
