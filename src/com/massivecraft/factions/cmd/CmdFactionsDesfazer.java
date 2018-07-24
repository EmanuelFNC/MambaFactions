package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsDesfazer extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDesfazer()
	{
		// Aliases
		this.addAliases("disband", "deletar", "excluir");

		// Requisi��es
		this.addRequirements(ReqHasFaction.get());
		
		// Descri��o do comando
		this.setDesc("�6 desfazer �8-�7 Desfaz a sua fac��o.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{		
		
		// Verificando se o player possui permiss�o
		if(!(msender.getRole() == Rel.LEADER || msender.isOverriding())) {
			msender.message("�cApenas o l�der da fac��o pode desfazer a fac��o.");
			return;
		}
				
		// Verificando se a fac��o � uma fac��o permanente (zonalivre, zonadeguerra ou zonaprotegida)
		if (msenderFaction.getFlag(MFlag.getFlagPermanent()))
		{
			msg("�cEsta fac��o � uma fac��o permanente portanto n�o pode ser desfeita.");
			return;
		}

		// Evento
		EventFactionsDisband event = new EventFactionsDisband(me, msenderFaction);
		event.run();
		if (event.isCancelled()) return;

		
		// Eviando todos os jogadores para a zona livre e informandos os mesmo que a fac��o foi desfeita
		for (MPlayer mplayer : msenderFaction.getMPlayers())
		{
			EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(sender, mplayer, FactionColl.get().getNone(), MembershipChangeReason.DISBAND);
			membershipChangeEvent.run();
		}

		// Informando os players da fac��o
		for (MPlayer mplayer : msenderFaction.getMPlayersWhereOnline(true))
		{
			mplayer.msg("�e%s�e desfez a sua fac��o!", msender.describeTo(mplayer).replace("voc�", "�eVoc�"));
		}
		
		// Aplicando o evento.
		msenderFaction.detach();
	}
	
}
