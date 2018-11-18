package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasntFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsEntrar extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsEntrar()
	{
		// Aliases
        this.addAliases("join", "aceitar");

		// Descri��o
		this.setDesc("�6 entrar �e<fac��o> �8-�7 Entra em uma fac��o.");
		
		// Requisitos
		this.addRequirements(ReqHasntFaction.get());
		
		// Parametros (necessario)
		this.addParameter(TypeString.get(), "fac��o", "erro", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Verficiando se os argumentos s�o validos
		if (!this.argIsSet()) {
			msg("�cArgumentos insuficientes, use /f entrar <fac��o>");
			return;
		}
				
		// Argumentos
		String name = this.arg();
		Faction faction = readFaction(name);

		// Verificando se a fac��o atingiu o limite de membros definido na config
		if (MConf.get().factionMemberLimit > 0 && faction.getMPlayers().size() >= MConf.get().factionMemberLimit) {
			msg("�cA fac��o �f[%s�f] j� atingiu o limite maximo de membros por fac��o (%d), portanto voc� n�o podera entrar.", faction.getName(), MConf.get().factionMemberLimit);
			return;
		}

		// Verificando se o player possui um convite para entrar na fac��o
		// N�s tambem verificando se ele � admin ><
		if (!(faction.isInvited(msender) || msender.isOverriding())) {
			msg("�cVoc� precisa de um convite para poder entrar na fac��o.");
			return;
		}

		// Evento
		EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(sender, msender, faction, MembershipChangeReason.JOIN);
		membershipChangeEvent.run();
		if (membershipChangeEvent.isCancelled()) return;
		
		// Informando o player e a fac��o
		faction.msg("�f%s�e entrou na fac��o�e.", msender.getName());
		msender.msg("�aVoc� entrou na fac��o �f[%s�f]�a.", faction.getName());
		
		// Aplicando o evento
		msender.resetFactionData();
		msender.setFaction(faction);
		
		faction.uninvite(msender);
	}
	
}