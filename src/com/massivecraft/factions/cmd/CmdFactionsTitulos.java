package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.requirement.RequirementTitlesAvailable;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mixin.MixinTitle;

public class CmdFactionsTitulos extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsTitulos()
	{
		// Aliases
		this.addAliases("tt", "territorytitles");
		
		// Descri��o
		this.setDesc("�6 tt,titulos �8-�7 Mostra os titulos dos territ�rio.");

		// Requisitos
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(RequirementTitlesAvailable.get());
		
		// Parametros (n�o necessario)
		this.addParameter(TypeString.get(), "on/off", "erro", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Visibility getVisibility()
	{
		// Isto esconde o comando caso o player estiver usando uma vers�o que n�o suporta titles
		if ( ! MixinTitle.get().isAvailable()) return Visibility.INVISIBLE;
		return super.getVisibility();
	}
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Boolean old = msender.isTerritoryInfoTitles();
		Boolean target = readBoolean(old);
		
		// Verificando se o player digitou um argumento correto
		if (target == null) {
			msg("�cComando incorreto, use /f tt [on/off]");
			return;
		}
		
		// Descri��o da a��o
		String desc = target ? "�2ativada": "�cdesativada";

		// Verificando se o player j� esta com a visualiza��o ativada/desativada
		if (target == old) {
			msg("�aA visualiza��o dos titulos dos territ�rios j� est� %s�a.", desc);
			return;
		}
		
		// Setando a visualiza��o como ativado/desativado
		msender.setTerritoryInfoTitles(target);
		
		// Informando os players players
		msg("�aVisualiza��o dos titulos dos territ�rios %s�a.", desc);
	}
	
}