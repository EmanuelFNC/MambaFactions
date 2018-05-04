package com.massivecraft.factions.cmd;

import com.massivecraft.factions.comparator.ComparatorFactionList;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CmdFactionsListar extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsListar()
	{

		// Aliases
        this.addAliases("l", "list");
        
		// Parametros (n�o necessario)
		this.addParameter(Parameter.getPage());

		// Descri��o do comando
		this.setDesc("�6 l,listar �e[p�gina] �8-�7 Mostra a lista de fac��es.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		int page = this.readArg();
		final CommandSender sender = this.sender;
		final MPlayer msender = this.msender;
		
		// NOTE: The faction list is quite slow and mostly thread safe.
		// We run it asynchronously to spare the primary server thread.
		
		// Pager Create
		final Pager<Faction> pager = new Pager<>(this, "�eLista de Fac��es", page, new Stringifier<Faction>() {
			@Override
			public String toString(Faction faction, int index)
			{
				if (faction.isNone())
				{
					return Txt.parse("�2Sem fac��o�e %d online", FactionColl.get().getNone().getMPlayersWhereOnlineTo(sender).size());
				}
				else
				{
					return Txt.parse("%s�e: Online %d/%d, Terras %d, Poder %d/%d",
						faction.getName(msender),
						faction.getMPlayersWhereOnlineTo(sender).size(),
						faction.getMPlayers().size(),
						faction.getLandCount(),
						faction.getPowerRounded(),
						faction.getPowerMaxRounded()
					);
				}
			}
		});
		
		Bukkit.getScheduler().runTaskAsynchronously(Factions.get(), new Runnable()
		{
			@Override
			public void run()
			{
				// Pager Items
				final List<Faction> factions = FactionColl.get().getAll(ComparatorFactionList.get(sender));
				pager.setItems(factions);
				
				// Pager Message
				pager.message();
			}
		});
	}
	
}
