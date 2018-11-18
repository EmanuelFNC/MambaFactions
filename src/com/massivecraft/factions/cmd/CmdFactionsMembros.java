package com.massivecraft.factions.cmd;

import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsMembros extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMembros()
	{
		// Aliases
		this.addAliases("status", "s");
		
		// Descri��o
		this.setDesc("�6 membros �e<fac��o> �8-�7 Mostra a lista de membros da fac��o.");
		
		// Requisitos
		this.addRequirements(RequirementIsPlayer.get());
		
		// Parametros (n�o ecessario)
		this.addParameter(TypeString.get(), "outra fac��o", "sua fac��o", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Pegando a fac��o
		Faction faction = readFaction();
		
		// Verificando se o player � da fac��o � a zona livre
		if (faction.isNone()) {
			msg("�cArgumentos insuficientes, use /f membros <fac��o>");
			return;
		}
		
		// Verificando se a fac��o tem muitos membros
		if (faction.getMPlayers().size() > 29) {
			msg("�cA fac��o �f"+ faction.getName() +"�c possui muitos membros portanto o Menu n�o podera ser aberto.");
			return;
		}
		
		// Verificando se esta sem membros
		if (faction.getMPlayers().size() == 0) {
			msg("�cA fac��o �f" + faction.getName() + "�c n�o possui membros!");
			return;
		}
		
		EngineMenuGui.get().abrirMenuMembrosDaFaccao(msender, faction);
	}
}