package com.massivecraft.factions.cmd;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.cmd.type.TypeRelation;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.event.EventFactionsRelationChange;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.mson.Mson;

public class CmdFactionsRelacaoDefinir extends FactionsCommand
{
	{
		
	// Aliases
    this.addAliases("set", "setar");
    
	// Descri��o do comando
	this.setDesc("�6 relacao definir �e<fac��o> �8-�7 Define uma rela��o.");
	
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsRelacaoDefinir()
	{
		// Parametros (necessario)
		this.addParameter(TypeFaction.get(), "fac��o");
		this.addParameter(TypeRelation.get(), "rela��o", "abrirMenu");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Faction otherFaction = this.readArg();
		Rel newRelation = this.readArg();
		
		
		// Verificando se a fac��o target � a mesma fac��o do msender
		if (otherFaction == msenderFaction)
		{
			throw new MassiveException().setMsg("�cVoc� n�o pode definir uma rela��o com sua pr�pria fac��o.");
		}
		
		// Menu gui /f rela��o
		if (newRelation == null && msender.isPlayer())
		{
			Player p = msender.getPlayer();
			String factionNome = otherFaction.getName();
			Inventory inv = Bukkit.createInventory(null, 27, "�8Rela��o com " + factionNome);
			
			if (msenderFaction.getRelationTo(otherFaction) == Rel.ALLY) {
			inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�bDefinir alian�a com " + factionNome).setLore("�cSua a fac��o j� � �caliada da " + factionNome + "�c.").setLeatherArmorColor(Color.AQUA).toItemStack());
			} else if (msenderFaction.getRelationWish(otherFaction) == Rel.ALLY || otherFaction.getRelationWish(msenderFaction) == Rel.ALLY) {
			inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�bDefinir alian�a com " + factionNome).setLore("�eSua fac��o j� possui um", "�econvite de alian�a pendente", "�ecom a �f" + factionNome + "�e!", "", "�fClique para ver a lista de", "�fconvites de alian�a pendentes.").setLeatherArmorColor(Color.AQUA).toItemStack());
			} else {
			inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�bDefinir alian�a com " + factionNome).setLore("�fClique para definir a fac��o","�7" + factionNome + "�f como fac��o �baliada�f.").setLeatherArmorColor(Color.AQUA).toItemStack());
			}
			
			if (msenderFaction.getRelationWish(otherFaction) == Rel.NEUTRAL) {
			inv.setItem(13, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�fDefinir neutralidade com " + factionNome).setLore("�cSua a fac��o j� � neutra com a " + factionNome + ".").setLeatherArmorColor(Color.WHITE).toItemStack());
			} else {
			inv.setItem(13, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�fDefinir neutralidade com " + factionNome).setLore("�fClique para definir a fac��o","�7" + factionNome + "�f como fac��o �fneutra�f.").setLeatherArmorColor(Color.WHITE).toItemStack()); }
			
			if (msenderFaction.getRelationWish(otherFaction) == Rel.ENEMY) {
			inv.setItem(15, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�cDefinir rivalidade com " + factionNome).setLore("�cSua a fac��o j� � �crival da " + factionNome + ".").setLeatherArmorColor(Color.RED).toItemStack());
			} else {
			inv.setItem(15, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�cDefinir rivalidade com " + factionNome).setLore("�fClique para definir a fac��o","�7" + factionNome + "�f como fac��o �cinimiga�f.").setLeatherArmorColor(Color.RED).toItemStack()); }

			p.openInventory(inv);
			return;
		}
		
		if (newRelation == null && msender.isConsole()) 
		{
			throw new MassiveException().setMsg("�cUtilize /f relacao definir <faccao> <relacao>");
		}
		
		if (newRelation == Rel.TRUCE) 
		{
			throw new MassiveException().setMessage("�cDesculpe mas o sistema de fac��es em tr�gua foi desabilitado neste servidor.");
		}
		
		// Verificando se o player possui permiss�o
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("�cVoc� precisar ser capit�o ou superior para poder gerenciar as rela��es da fac��o.");
			return;
		}
		
		// Verificando se a rela��o da fac��o target � a mesma da fac��o do msender
		if (msenderFaction.getRelationWish(otherFaction) == newRelation)
		{
			throw new MassiveException().setMsg("�eA sua fac��o j� � %s�e da �f%s�e.", newRelation.getDescFactionOne(), otherFaction.getName());
		}
		
		// Evento
		EventFactionsRelationChange event = new EventFactionsRelationChange(sender, msenderFaction, otherFaction, newRelation);
		event.run();
		if (event.isCancelled()) return;
		newRelation = event.getNewRelation();

		// Enviando pedido (aliados)
		msenderFaction.setRelationWish(otherFaction, newRelation);
		Rel currentRelation = msenderFaction.getRelationTo(otherFaction, true);

		// Definindo a rela��o sem precisar de confirma��o
		if (newRelation == currentRelation)
		{
			otherFaction.msg("�f%s�e agora � %s�e.", msenderFaction.getName(), newRelation.getDescFactionOne());
			msenderFaction.msg("�f%s�e agora � %s�e.", otherFaction.getName(), newRelation.getDescFactionOne());
		}
		
		// Informando que a fac��o deseja ser aliado
		else
		{
			MassiveCommand command = CmdFactions.get().cmdFactionsRelacao.cmdFactionsRelacaoDefinir;
			String colorOne = newRelation.getColor() + newRelation.getDescFactionOne();

			// Mson && Json
			Mson factionsRelationshipChange = mson(
				Mson.parse("�f%s�e deseja se tornar %s�e.", msenderFaction.getName(), colorOne),
				Mson.SPACE,
				mson("�e[ACEITAR]").command(command, msenderFaction.getName(), newRelation.name())
			);
			
			otherFaction.sendMessage(factionsRelationshipChange);
			msenderFaction.msg("�f%s�e foi informada de que a sua fac��o deseja se tornar %s�e.", otherFaction.getName(), colorOne);
		}
		
		// Aplicando o evento
		msenderFaction.changed();
	}
	
}
