package com.massivecraft.factions.cmd;

import com.massivecraft.factions.engine.EngineFly;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsVoar extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsVoar() 
	{
		// Aliases
	    this.addAliases("fly");
		
		// Descri��o
		this.setDesc("�6 voar �e[on/off] �8-�7 Habilita o fly nos territ�rios da fac��o.");
	    
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
		// Verificando se o player permiss�o de bypass
		boolean bypass = me.hasPermission("factions.voar.bypass") || msender.isOverriding();
		if (!bypass) {
		
			// Verificando se o player possui fac��o
			if (!msender.hasFaction()) {
				msg("�cVoc� precisa estar em alguma fac��o para poder utilizar este comando.");
				return;
			}
			
			// Verificando se o player pode ligar o fly
			PS ps = PS.valueOf(me.getLocation());
			if (!BoardColl.get().getFactionAt(ps).equals(msenderFaction) && !MConf.get().mundosComFlyAtivado.contains(me.getWorld().getName())) {
				msg("�cVoc� n�o pode habilitar o modo voar fora dos territ�rios da sua fac��o.");
				return;
			}
			 
			// Verificando se a fac��o n�o esta sob ataque
			if (msenderFaction.isInAttack()) {
				msg("�cVoc� n�o pode habilitar o modo voar enquanto sua fac��o estiver sob ataque.");
				return;
			}
		}
		
		// Argumentos
		boolean old = me.getAllowFlight();
		Boolean target = readBoolean(old);
		
		// Verificando se o player digitou um argumento correto
		if (target == null) {
			msg("�cComando incorreto, use /f voar [on/off]");
			return;
		}
		
		// Descri��o da a��o
		String desc = target ? "�2ativado": "�cdesativado";
		
		// Verificando se o player ja esta com o modo fly ativado/desativado
		if (target == old) {
			msg("�aO seu modo voar j� est� %s�a.", desc);
			return;
		}
		
		// Caso o player n�o possua bypass, ativando
		if (!bypass) {
			if (target) EngineFly.flys.add(me);
			else        EngineFly.flys.remove(me);
		}
		
		// Setando o modo fly como ativado/desativado
		me.setAllowFlight(target);
		
		// Informando o msender
		msg("�aModo voar %s�a.", desc);
	}
}
