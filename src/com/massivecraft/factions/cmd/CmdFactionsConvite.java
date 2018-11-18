package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsConvite extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsConvite() 
	{
		// Aliases
	    this.addAliases("convidar", "i", "adicionar", "invite", "convites");
		    
		// Descri��o
		this.setDesc("�6 convite �8-�7 Gerencia os convites da fac��o.");
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsConviteAdd cmdFactionsConviteAdd = new CmdFactionsConviteAdd();
	public CmdFactionsConviteDel cmdFactionsConviteDel = new CmdFactionsConviteDel();
	public CmdFactionsConviteListar cmdFactionsConviteListar = new CmdFactionsConviteListar();
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //	
	
	@Override
	public void perform() throws MassiveException
	{
		// Verificando se o player possui fac��o
		if (!msender.hasFaction() && msender.isPlayer()) {
			if (msender.getInvitations().isEmpty()) {
				msg("�cVoc� n�o possui convites de fac��es pendentes.");
				return;
			} else {
				EngineMenuGui.get().abrirMenuConvitesRecebidos(msender);
				return;
			}
		}
		
		// Verificando se possui permiss�o
		if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msg("�cVoc� precisar ser capit�o ou superior para poder gerenciar os convites da fac��o.");
			return;
		}
		
		// Verificando se � um player para abrir o menu gui
		if (msender.isPlayer()) {
			EngineMenuGui.get().abrirMenuConvites(me);
			return;
		}
	}
}
