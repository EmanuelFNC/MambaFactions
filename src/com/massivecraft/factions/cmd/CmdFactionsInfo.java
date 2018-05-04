package com.massivecraft.factions.cmd;

import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.event.EventFactionsFactionShowAsync;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.PriorityLines;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsInfo extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsInfo()
	{
		// Aliases
		this.addAliases("f", "show", "ver", "faction");

		// Parametros (n�o necessario)
		this.addParameter(TypeFaction.get(), "fac��o", "voc�");

		// Descri��o do comando
		this.setDesc("�6 f,info �e<fac��o> �8-�7 Mostra as informa��es da fac��o.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		final Faction faction = this.readArg(msenderFaction);
		final CommandSender sender = this.sender;
	
		Bukkit.getScheduler().runTaskAsynchronously(Factions.get(), new Runnable()
		{
			@Override
			public void run()
			{
				// Evento
				EventFactionsFactionShowAsync event = new EventFactionsFactionShowAsync(sender, faction);
				event.run();
				if (event.isCancelled()) return;
				
				// Titulo da mensagem
				MixinMessage.get().messageOne(sender, Txt.titleize("�eFac��o " + faction.getName(msender)));
				
				// Linhas da mensagem (para alterar as mensagens consulte a classe EngineShowEvent)
				TreeSet<PriorityLines> priorityLiness = new TreeSet<>(event.getIdPriorityLiness().values());
				for (PriorityLines priorityLines : priorityLiness)
				{
					MixinMessage.get().messageOne(sender, priorityLines.getLines());
				}
			}
		});
		
	}
	
}
