package com.massivecraft.factions.cmd;

import java.util.List;

import org.bukkit.Location;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsMapa extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMapa()
	{
		// Aliases
        this.addAliases("map");

		// Descri��o
		this.setDesc("�6 mapa �e[on/off] �8-�7 Mostra o mapa dos territ�rios.");
        
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
		// Verificando se algum foi digitado e verificando se o argumento � valido
		if (this.argIsSet()) {
			Boolean old = msender.isMapAutoUpdating();
			Boolean target = readBoolean(old);
			
			// Verificando se o player digitou um argumento correto
			if (target == null) {
				msg("�cComando incorreto, use /f mapa [on/off]");
				return;
			}
			
			// Descri��o da a��o
			String desc = target ? "�2ativado": "�cdesativado";

			// Verificando se o player ja esta com o modo mapa ativado
			if (target == old) {
				msg("�aO seu mapa automatico j� esta %s�a.", desc);
				return;
			}
			
			// Setando o modo mapa como ativado/desativado
			msender.setMapAutoUpdating(target);
			
			// Atualizando o /f mapa auto
			if (target) showMap(19, 19);
			
			// Informando o msender
			msg("�aMapa autom�tico %s�e.", desc);
			return;
		}
		
		// Chamando o m�todo para exibir o mapa
		showMap(19, 19);
	}
	
	// M�todo para mostrar o mapa
	private void showMap(int width, int height)
	{
		Location location = me.getLocation();
		List<Mson> message = BoardColl.get().getMap(me, msenderFaction, PS.valueOf(location), location.getYaw(), width, height);
		message(message);
	}
	
}
