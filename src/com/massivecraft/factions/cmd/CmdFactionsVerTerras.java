package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanOn;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsVerTerras extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsVerTerras()
	{
		// Aliases
		this.addAliases("sc");
		
		// Parametros (n�o necessario)
		this.addParameter(TypeBooleanOn.get(), "mostrar", "esconder");

		// Requisi��es
		this.addRequirements(RequirementIsPlayer.get());
		
		// Descri��o do comando
		this.setDesc("�6 sc,verterras �8-�7 Mostra as delimita��es das terras.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		boolean old = msender.isSeeingChunk();
		boolean target = this.readArg(!old);
		String targetDesc = Txt.parse(target ? "�2ativada": "�cdesativada");
		
		// Verificando se o player ja esta com o modo verterras ativado
		if (target == old)
		{
			msg("�aA visualiza��o das delimita��es das terras j� est� %s�a.", targetDesc);
			return;
		}
		
		// Setando o modo verterras como ativado/desativado
		msender.setSeeingChunk(target);
		
		// Informando o msender
		msg("�aVisualiza��o das delimita��es das terras %s�a.", targetDesc);
	}

}
