package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsVerTerras extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsVerTerras()
	{
		// Aliases
		this.addAliases("sc");
		
		// Descri��o
		this.setDesc("�6 sc,verterras �8-�7 Mostra as delimita��es das terras.");

		// Requisitos
		this.addRequirements(RequirementIsPlayer.get());
		
		// Parametros (n�o necessario)
		this.addParameter(TypeString.get(), "on/off", "erro", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Boolean old = msender.isSeeingChunk();
		Boolean target = readBoolean(old);
		
		// Verificando se o player digitou um argumento correto
		if (target == null) {
			msg("�cComando incorreto, use /f sc [on/off]");
			return;
		}
		
		// Descri��o da a��o
		String desc = target ? "�2ativada": "�cdesativada";

		// Verificando se o player j� esta com modo ver terras ativado/desativado
		if (target == old) {
			msg("�aA visualiza��o das delimita��es das terras j� est� %s�a.", desc);
			return;
		}
		
		// Setando o modo verterras como ativado/desativado
		msender.setSeeingChunk(target);
		
		// Informando o sender
		msg("�aVisualiza��o das delimita��es das terras %s�a.", desc);
	}

}