package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasntFaction;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsEntrar extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsEntrar()
	{
		// Aliases
        this.addAliases("join");
        
		// Parametros (necessario)
		this.addParameter(TypeFaction.get(), "fac��o");
		
		// Requisi��es
		this.addRequirements(ReqHasntFaction.get());

		// Descri��o do comando
		this.setDesc("�6 entrar �e<fac��o> �8-�7 Entra em uma fac��o.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Faction faction = this.readArg();		

		// Verificando se a fac��o atingiu o limite de membros definido na config
		if (MConf.get().factionMemberLimit > 0 && faction.getMPlayers().size() >= MConf.get().factionMemberLimit)
		{
			msg("�cA fac��o %s atingiu o limite maximo de membros (%d), portanto voc� n�o podera entrar na fac��o.", faction.getName(), MConf.get().factionMemberLimit);
			return;
		}

		// Verificando se o player possui um convite para entrar na fac��o
		// N�s tambem verificando se ele � admin ><
		if( ! (faction.isInvited(msender) || msender.isOverriding()))
		{
			msg("�cVoc� precisa de um convite para poder entrar na fac��o.");
			return;
		}

		// Evento
		EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(sender, msender, faction, MembershipChangeReason.JOIN);
		membershipChangeEvent.run();
		if (membershipChangeEvent.isCancelled()) return;
		
		// Informando o player e a fac��o
		faction.msg("�e\"%s\" �eentrou na sua fac��o�e.", msender.getName());
		msender.msg("�aVoc� entrou na fac��o �f%s�a.", faction.getName());
		
		// Aplicando o evento
		msender.resetFactionData();
		msender.setFaction(faction);
		
		faction.uninvite(msender);
	}
	
}
