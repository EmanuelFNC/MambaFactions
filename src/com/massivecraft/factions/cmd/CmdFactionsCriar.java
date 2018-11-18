package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasntFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.factions.util.OthersUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.store.MStore;

public class CmdFactionsCriar extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsCriar()
	{
		// Aliases
		this.addAliases("new", "create");

		// Descri��o
		this.setDesc("�6 criar �e<nome> �8-�7 Cria uma nova fac��o.");
		
		// Requisitos
		this.addRequirements(ReqHasntFaction.get());
		
		// Parametros (necessario)
		this.addParameter(TypeString.get(), "nome", "erro", true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Verficiando se os argumentos s�o validos
		if (!this.argIsSet()) {
			msg("�cArgumentos insuficientes, use /f criar <nome>");
			return;
		}
		
		// Argumentos
		String newName = this.arg();
		
		// M�todo para verificar se o nome da fac��o � valido
		if (!OthersUtil.isValidFactionsName(newName, me)) return;
		
		// Pre-Generate Id (pr�-cria��o do id da fac��o)
		String factionId = MStore.createId();
		
		// Evento
		EventFactionsCreate createEvent = new EventFactionsCreate(sender, factionId, newName);
		createEvent.run();
		if (createEvent.isCancelled()) return;
		
		// Aplicando o evento
		Faction faction = FactionColl.get().create(factionId);
		faction.setName(newName);
		
		msender.setRole(Rel.LEADER);
		msender.setFaction(faction);
		
		// NOTA: O factions cria uma fac��o vazia por isso existe o JoinEvent, para colocar o sender dentro da fac��o.
		EventFactionsMembershipChange joinEvent = new EventFactionsMembershipChange(sender, msender, faction, MembershipChangeReason.CREATE);
		joinEvent.run();
		
		// Informando o sender
		msg("�aFac��o �f[%s�f]�a criada com sucesso!", newName);
		MPerm.resetDefaultPermissions(faction);
	}
}
