package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsSair extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSair()
	{
		// Aliases
        this.addAliases("leave", "deixar");

		// Descri��o
		this.setDesc("�6 sair �8-�7 Abandona a sua fac��o atual.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (n�o necessario)
		this.addParameter(TypeString.get(), "confirma��o", "null", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Verificando se a fac��o n�o esta sob ataque
		if (msenderFaction.isInAttack()) {
			msg("�cVoc� n�o pode abandonar a fac��o enquanto ela estiver sobre ataque!");
			return;
		}

		// Verificando se o player � o lider da fac��o
		if (msender.getRole() == Rel.LEADER) {
			msg("�cVoc� � o lider da fac��o, portanto n�o pode abandona-la. Caso queira desfaze-la use /f desfazer.");
			return;
		}
		
		// Caso n�o haja o argumento "confirmar" ent�o � aberto um menu de confirma��o
		if ((!this.argIsSet() || !this.arg().equalsIgnoreCase("confirmar")) && msender.isPlayer()) {
			EngineMenuGui.get().abrirMenuAbandonarFaccao(msender);
			return;
		}

		// Saindo da fac��o
		msender.leave();
	}
	
}