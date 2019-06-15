package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsProteger extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsProteger()
	{	
		// Descri��o
		this.setDesc("�6 proteger �8-�7 Protege territ�rios temporariamente.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(RequirementIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Verificando se tem permiss�o
		if (!MPerm.getPermTerritory().has(msender, msenderFaction, true)) return;
		
		// Verificando se a fac��o n�o esta sob ataque
		if (msenderFaction.isInAttack()) {
			msg("�cVoc� n�o pode proteger terrenos enquanto sua fac��o estiver sobre ataque!");
			return;
		}
		
		// Verificando se � poss�vel proteger claims nesse mundo
		if (!MConf.get().worldsClaimingEnabled.contains(PS.valueOf(me.getLocation()).getWorld())) {
			msg("�cA compra de territ�rios esta desabilitada neste mundo.");
			return;
		}
		
		// Verificando se a fac��o atingiu o limite de claims tempor�rios
		int limit = MConf.get().limiteDeProtecoesTemporaria;
		if (limit > 0 && msenderFaction.getTempClaims().size() >= limit) {
			msg("�cLimite m�ximo de terrenos tempor�rios atingido (" + limit + ")! Abandone terrenos tempor�rio antigos para poder proteger novos terrenos.");
			return;
		}
		
		// Verificando se a fac��o j� n�o � dona do territ�rio
		Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(me.getLocation()));
		if (factionAt.equals(msenderFaction)) {
			msg("�eSua fac��o j� � dona deste territ�rio.");
			return;
		}
		
		// Verificando se o territ�rio esta livre
		if (!factionAt.isNone()) {
			msg("�cVoc� s� pode proteger terrenos que estejam livres.");
			return;
		} 
		
		EngineMenuGui.get().abrirMenuProtegerTerreno(msender);
	}
}
