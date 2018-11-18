package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsDesfazer extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDesfazer()
	{
		// Aliases
		this.addAliases("disband", "deletar", "excluir");
		
		// Descri��o
		this.setDesc("�6 desfazer �8-�7 Desfaz a sua fac��o.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (n�o necessario)
		this.addParameter(TypeString.get(), "confirma��o", "null", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{			
		// Verificando se o player possui permiss�o
		if (!(msender.getRole() == Rel.LEADER || msender.isOverriding())) {
			msg("�cApenas o l�der da fac��o pode desfazer a fac��o.");
			return;
		}
		
		// Verificando se a fac��o esta em ataque
		if (msenderFaction.isInAttack()) {
			msg("�cVoc� n�o pode desfazer sua fac��o enquanto ela estiver sobre ataque!");
			return;
		}			
		
		// Verificando se a fac��o � uma fac��o permanente (zonalivre, zonadeguerra ou zonaprotegida)
		if (msenderFaction.getFlag(MFlag.getFlagPermanent())) {
			msg("�cEsta fac��o � uma fac��o permanente portanto n�o pode ser desfeita.");
			return;
		}
		
		// Caso n�o haja o argumento "confirmar" ent�o � aberto um menu de confirma��o
		if ((!this.argIsSet() || !this.arg().equalsIgnoreCase("confirmar")) && msender.isPlayer()) {
			EngineMenuGui.get().abrirMenuDesfazerFaccao(msender);
			return;
		}

		// Evento
		EventFactionsDisband event = new EventFactionsDisband(me, msenderFaction);
		event.run();
		if (event.isCancelled()) return;
		
		// Eviando todos os jogadores para a zona livre e informando os mesmo que a fac��o foi desfeita
		for (MPlayer mplayer : msenderFaction.getMPlayers()) {
			EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(sender, mplayer, FactionColl.get().getNone(), MembershipChangeReason.DISBAND);
			membershipChangeEvent.run();
		}

		// Informando os players da fac��o
		for (MPlayer mplayer : msenderFaction.getMPlayersWhereOnline(true)) 	{
			mplayer.msg("�f%s�e desfez a fac��o!", msender.describeTo(mplayer).replace("Voc�", "�eVoc�"));
		}
		
		// Removendo os convites da fac��o
		for (String playerId : msenderFaction.getInvitations().keySet()) {
			MPlayer mplayer = MPlayer.get(playerId);
			if (mplayer != null) mplayer.removeInvitation(msenderFaction.getId());
		}
		
		// Aplicando o evento.
		msenderFaction.detach();
	}
	
}