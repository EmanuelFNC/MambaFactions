package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
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
		this.addParameter(TypeString.get(), "fac��o", "erro", true);

		// Visibilidade do comando
		this.setVisibility(Visibility.INVISIBLE);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		// Verficiando se os argumentos s�o validos
		if (!this.argIsSet()) {
			msg("�cArgumentos insuficientes, use /f " + this.relName + " <fac��o>");
			return;
		}
		
		// Verificando se o player possui permiss�o
		if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("�cVoc� precisar ser capit�o ou superior para poder gerenciar as rela��es da fac��o.");
			return;
		}
		
		// Aplicando o evento
		CmdFactions.get().cmdFactionsRelacao.execute(sender, MUtil.list(
			this.arg(),
			this.relName
		));
	}

}
