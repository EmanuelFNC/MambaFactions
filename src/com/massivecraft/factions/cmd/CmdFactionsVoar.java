package com.massivecraft.factions.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineSobAtaque;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.Lang;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsVoar extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsVoar() 
	{
		// Aliases
	    this.addAliases("fly");
		
		// Parametros (n�o necessario)
		this.addParameter(TypeBooleanYes.get(), "on/off", "uma vez");
	    
		// Requisi��es
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		
		// Descri��o do comando
		this.setDesc("�6 voar �e[on/off] �8-�7 Habilita o fly nos territ�rios da fac��o.");
		
	    // Visibilidade do comando
		if (!MConf.get().sistemaDeVoarNoClaim)
		{
		    this.setVisibility(Visibility.INVISIBLE);
		}
	
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		if (!MConf.get().sistemaDeVoarNoClaim) {
			message(Lang.COMMAND_CHILD_NONE);
			message(Lang.COMMAND_CHILD_HELP);
			return;
		}
		
		// Argumentos
		Player p = msender.getPlayer();
		boolean old = p.getAllowFlight();
		boolean target = this.readArg(!old);
		String targetDesc = Txt.parse(target ? "�2ativado": "�cdesativado");
		
		// Verificando se o player pode ligar o fly
		PS ps = PS.valueOf(p.getLocation());
		if (!msender.isOverriding() && !BoardColl.get().getFactionAt(ps).equals(msenderFaction)) {
			msg("�cVoc� n�o pode habilitar o modo voar fora dos territ�rios da sua fac��o.");
			return;
		}
		 
		// Verificando se a fac��o n�o esta sob ataque
		if (EngineSobAtaque.factionattack.containsKey(msenderFaction.getName())) {
			msg("�cVoc� n�o pode habilitar o modo voar enquanto sua fac��o estiver sob ataque.");
			return;
		}
		
		// Verificando se o player ja esta com o modo fly ativado
		if (target == old)
		{
			msg("�aO modo voar j� est� %s�a.", targetDesc);
			return;
		}
		
		// Setando o modo fly como ativado/desativado
		p.setAllowFlight(target);
		
		// Informando o msender
		msg("�aModo voar %s�a.", targetDesc);
	}
}
