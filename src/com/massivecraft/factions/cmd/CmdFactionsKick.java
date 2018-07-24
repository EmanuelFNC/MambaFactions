package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsKick extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsKick()
	{
		// Aliases
		this.addAliases("expulsar", "kickar");
		
		// Parametros (necessario)
		this.addParameter(TypeMPlayer.get(), "player");
		
		// Requisi��es
		this.addRequirements(ReqHasFaction.get());
		
		// Descri��o do comando
		this.setDesc("�6 kick �e<player> �8-�7 Expulsa um player da fac��o.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Verificando se o player possui permiss�o
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("�cVoc� precisar ser capit�o ou superior para poder expulsar membros da fac��o.");
			return;
		}
		
		// Argumentos
		MPlayer mplayer = this.readArg();
		
		// Verificando se o target � da mesma fac�o que o sender
		if (mplayer.getFaction() != msenderFaction) {
			msender.message("�cEste jogador n�o esta na sua fac��o.");
			return;
		}
		
		// Verificando se o sender e o target s�o os mesmos
		if (msender == mplayer)
		{
			msg("�cVoc� n�o pode se expulsar da fac��o, caso queira sair use /f sair.");
			return;
		}
		
		// Verificando se o target � o l�der e verificando se o sender n�o � 1 admin
		if (mplayer.getRole() == Rel.LEADER && !msender.isOverriding())
		{
			throw new MassiveException().addMsg("�cO l�der da fac��o n�o pode ser expulso!");
		}
		
		// Verificando se o rank do sender � maior que o do target e verificando se o sender n�o � 1 admin
		if (mplayer.getRole() == Rel.OFFICER && msender.getRole() == Rel.OFFICER && ! msender.isOverriding())
		{
			throw new MassiveException().addMsg("�cApenas o l�der da fac��o pode expulsar ou rebaixar outros capit�es.");
		}

		// Evento
		EventFactionsMembershipChange event = new EventFactionsMembershipChange(sender, mplayer, FactionColl.get().getNone(), MembershipChangeReason.KICK);
		event.run();
		if (event.isCancelled()) return;

		Faction mplayerFaction = mplayer.getFaction();
		// Aplicando o evento e informando o player e fac��o
		if (mplayer.getRole() == Rel.LEADER)
		{
			mplayerFaction.promoteNewLeader();
		}
		mplayerFaction.uninvite(mplayer);
		mplayer.resetFactionData();
		mplayerFaction.msg("�e%s�e expulsou �e\"%s\"�e da fac��o! :O", msender.getRole().getPrefix() + msender.getName(), mplayer.getName());
		mplayer.msg("�eVoc� foi expulso da fac��o �f%s �epor �e%s!�e :O", mplayerFaction.getName(), msender.getRole().getPrefix() + msender.getName());
	}
	
}
