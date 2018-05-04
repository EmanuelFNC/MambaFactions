package com.massivecraft.factions.cmd;

import java.util.List;

import org.bukkit.Location;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsMapa extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMapa()
	{
		// Aliases
        this.addAliases("map");
        
		// Parametros (n�o necessario)
		this.addParameter(TypeBooleanYes.get(), "on/off", "uma vez");
        
		// Requisi��es
		this.addRequirements(RequirementIsPlayer.get());

		// Descri��o do comando
		this.setDesc("�6 mapa �e[on/off] �8-�7 Mostra o mapa dos territ�rios.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		boolean argSet = this.argIsSet();
		boolean showMap = true;
		
		// Mapa automatico
		if (argSet) showMap = this.adjustAutoUpdating();
		if (!showMap) return;
		
		// Mostrar Mapa
		mostrarMap(19, 19);
	}
	
	private boolean adjustAutoUpdating() throws MassiveException
	{
		// Verificando se o player ja esta com o modo mapa ativado
		boolean autoUpdating = this.readArg(!msender.isMapAutoUpdating());
		
		// Setando o modo mapa como ativado/desativado
		msender.setMapAutoUpdating(autoUpdating);
		
		// Informando o msender
		msg("�aMapa autom�tico %s�e.", Txt.parse(autoUpdating ? "�2habilitado" : "�cdesabilitado"));
		return autoUpdating;
	}
	
	// Mostrando o mapa (personalizado)
	public void mostrarMap(int width, int height)
	{
		Location location = me.getLocation();
		List<Mson> message = BoardColl.get().getMap(msender.getPlayer(), msenderFaction, PS.valueOf(location), location.getYaw(), width, height);
		message(message);
	}
	
}
