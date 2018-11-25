package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mson.Mson;

public class CmdFactionsKick extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsKick()
	{
		// Aliases
		this.addAliases("expulsar", "kickar");
		
		// Descri��o
		this.setDesc("�6 kick �e<player> �8-�7 Expulsa um player da fac��o.");
		
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
			msg("�cVoc� precisar ser capit�o ou superior para poder expulsar membros da fac��o.");
			return;
		}
		
		 // Verificando se a fac��o n�o esta sob ataque
		if (msenderFaction.isInAttack()) {
			msg("�cVoc� n�o pode expulsar membros da fac��o enquanto ela estiver sobre ataque.");
			return;
		}
		
		// Verficiando se os argumentos s�o validos
		if (!this.argIsSet()) {
			msg("�cArgumentos insuficientes, use /f kick <player>");
			return;
		}
		
		// Verificando se o sender e o target s�o os mesmos
		String name = this.arg();
		if (msender.getName().equalsIgnoreCase(name)) {
			message(Mson.parse("�cVoc� n�o pode se expulsar da fac��o, caso queira sair use /f sair").command("/f sair"));
			return;
		}
		
		// Argumentos
		MPlayer mplayer = readMPlayer(name);
		
		// Verificando se o target � da mesma fac�o que o sender
		if (!msenderFaction.getMPlayers().contains(mplayer)) {
			msg("�cEste jogador n�o esta na sua fac��o.");
			return;
		}
		
		// Verificando se o target � o l�der e verificando se o sender n�o � 1 admin
		if (mplayer.getRole() == Rel.LEADER && !msender.isOverriding()) {
			msg("�cO l�der da fac��o n�o pode ser expulso!");
			return;
		}
		
		// Verificando se o rank do sender � maior que o do target e verificando se o sender n�o � 1 admin
		if (mplayer.getRole() == Rel.OFFICER && msender.getRole() == Rel.OFFICER && !msender.isOverriding()) {
			msg("�cApenas o l�der da fac��o pode expulsar ou rebaixar outros capit�es.");
			return;
		}

		// Evento
		EventFactionsMembershipChange event = new EventFactionsMembershipChange(sender, mplayer, FactionColl.get().getNone(), MembershipChangeReason.KICK);
		event.run();
		if (event.isCancelled()) return;

		Faction faction = mplayer.getFaction();
		// Aplicando o evento e informando o player e fac��o
		if (mplayer.getRole() == Rel.LEADER) {
			faction.promoteNewLeader();
		}
		faction.uninvite(mplayer);
		mplayer.resetFactionData();
		faction.msg("�e%s�e expulsou �f%s�e da fac��o! :O", msender.getRole().getPrefix() + msender.getName(), mplayer.getName());
		mplayer.msg("�eVoc� foi expulso da fac��o �f[%s�f]�e por �e%s!", faction.getName(), msender.getRole().getPrefix() + msender.getName());
	}
	
}