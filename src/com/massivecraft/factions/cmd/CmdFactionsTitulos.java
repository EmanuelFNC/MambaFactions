package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.requirement.RequirementTitlesAvailable;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanOn;
import com.massivecraft.massivecore.mixin.MixinTitle;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsTitulos extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsTitulos()
	{
		// Aliases
		this.addAliases("tt", "territorytitles");

		// Parametros (n�o necessario)
		this.addParameter(TypeBooleanOn.get(), "on|off", "toggle");

		// Requisi��es
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(RequirementTitlesAvailable.get());
		
		// Descri��o do comando
		this.setDesc("�6 tt,titulos �8-�7 Mostra os titulos dos territ�rio.");
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
		boolean before = msender.isTerritoryInfoTitles();
		boolean after = this.readArg(!before);
		String desc = Txt.parse(after ? "�2ativada": "�cdesativada");
		
		// Verificando se o player ja esta com o modo title ativado
		if (after == before)
		{
			msg("�aA visualiza��o dos titulos dos territ�rios j� est� %s�a.", desc);
			return;
		}
		
		// Setando o modo title como ativado/desativado
		msender.setTerritoryInfoTitles(after);
		
		// Informando o msender
		msg("�aVisualiza��o dos titulos dos territ�rios %s�a.", desc);
	}
	
}
