package com.massivecraft.factions;

import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.Colorized;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.collections.MassiveSet;
import org.bukkit.ChatColor;


import java.util.Collections;
import java.util.Set;

public enum Rel implements Colorized, Named
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	ENEMY(
		"um inimigo", "inimigos", "�euma fac��o �cinimiga", "fac��o inimiga",
		"Inimigo"
	) { @Override public ChatColor getColor() { return MConf.get().colorEnemy; } },
	
	NEUTRAL(
		"um neutro", "neutros", "�euma fac��o �fneutra", "fac��o neutra",
		"Neutro"
	) { @Override public ChatColor getColor() { return MConf.get().colorNeutral; } },
	
	ALLY(
		"um aliado", "aliados", "�euma fac��o �baliada", "fac��o aliada",
		"Aliado"
	) { @Override public ChatColor getColor() { return MConf.get().colorAlly; } },
	
	TRUCE(
			"um tr�gua", "tr�guas", "�euma fac��o em �8tr�gua", "fac��o em tr�gua",
			""
		) { @Override public ChatColor getColor() { return MConf.get().colorTruce; } },
	
	RECRUIT(
		"um recruta da sua fac��o", "recrutas da sua fac��o", "", "",
		"Recruta"
	) { @Override public String getPrefix() { return MConf.get().prefixRecruit; } },
	
	MEMBER(
		"um membro da sua fac��o", "membros da sua fac��o", "sua fac��o", "suas fac��es",
		"Membro"
	) { @Override public String getPrefix() { return MConf.get().prefixMember; } },
	
	OFFICER(
		"um capit�o da sua fac��o", "capit�es da sua fac��o", "", "",
		"Capit�o", "Capitao"
	) { @Override public String getPrefix() { return MConf.get().prefixOfficer; } },
	
	LEADER(
		"l�der da sua fac��o", "lider da sua fac��o", "", "",
		"L�der", "Lider", "Dono"
	) { @Override public String getPrefix() { return MConf.get().prefixLeader; } },
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public int getValue() { return this.ordinal(); }
	
	private final String descPlayerOne;
	public String getDescPlayerOne() { return this.descPlayerOne; }
	
	private final String descPlayerMany;
	public String getDescPlayerMany() { return this.descPlayerMany; }
	
	private final String descFactionOne;
	public String getDescFactionOne() { return this.descFactionOne; }
	
	private final String descFactionMany;
	public String getDescFactionMany() { return this.descFactionMany; }
	
	private final Set<String> names;
	public Set<String> getNames() { return this.names; }
	@Override public String getName() { return this.getNames().iterator().next(); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	Rel(String descPlayerOne, String descPlayerMany, String descFactionOne, String descFactionMany, String... names)
	{
		this.descPlayerOne = descPlayerOne;
		this.descPlayerMany = descPlayerMany;
		this.descFactionOne = descFactionOne;
		this.descFactionMany = descFactionMany;
		this.names = Collections.unmodifiableSet(new MassiveSet<>(names));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ChatColor getColor()
	{
		return MConf.get().colorMember;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public boolean isAtLeast(Rel rel)
	{
		return this.getValue() >= rel.getValue();
	}
	
	public boolean isAtMost(Rel rel)
	{
		return this.getValue() <= rel.getValue();
	}
	
	public boolean isLessThan(Rel rel)
	{
		return this.getValue() < rel.getValue();
	}
	
	public boolean isMoreThan(Rel rel)
	{
		return this.getValue() > rel.getValue();
	}
	
	public boolean isRank()
	{
		return this.isAtLeast(Rel.RECRUIT);
	}
	
	// Used for friendly fire.
	public boolean isFriend()
	{
		return this.isAtLeast(TRUCE);
	}
	
	public String getPrefix()
	{
		return "";
	}
	
}
