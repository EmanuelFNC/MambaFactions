package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.util.MUtil;

public class CmdFactionsRelacaoOld extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public final String relName;

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRelacaoOld(String rel)
	{
		// Fields
		this.relName = rel.toLowerCase();
		this.setSetupEnabled(false);

		// Aliases
		this.addAliases(relName);

		// Parametros (necessario)
		this.addParameter(TypeFaction.get(), "fac��o", true);

		// Visibilidade do comando
		this.setVisibility(Visibility.INVISIBLE);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Faction faction = this.readArg();
		
		// Verificando se o player possui permiss�o
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("�cVoc� precisar ser capit�o ou superior para poder gerenciar as rela��es da fac��o.");
			return;
		}
		
		// Aplicando o evento
		CmdFactions.get().cmdFactionsRelacao.cmdFactionsRelacaoDefinir.execute(sender, MUtil.list(
			faction.getId(),
			this.relName
		));
	}

}
