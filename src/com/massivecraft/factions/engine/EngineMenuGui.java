package com.massivecraft.factions.engine;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.Heads;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;

public class EngineMenuGui  extends Engine{
	
	private static EngineMenuGui i = new EngineMenuGui();
	public static EngineMenuGui get() { return i; }
	
	@EventHandler
	public void aoExecutarComando(PlayerCommandPreprocessEvent e) {
		
		final String cmd = e.getMessage().toLowerCase();
		final Player p = e.getPlayer();
		final MPlayer mplayer = MPlayer.get(p);
        int terras = mplayer.getFaction().getLandCount();

		if (cmd.equalsIgnoreCase("/f")) {
			if (p instanceof Player ) {
				if (mplayer.hasFaction()) {
			        if (mplayer.getFaction().getLeader() == null ) {
			        	mplayer.message("�cA sua fac��o n�o possui um l�der portanto o Menu GUI n�o sera aberto.");
			        	e.setCancelled(false);
			        	return;
			        } else {
					e.setCancelled(true);
					abrirMenuPlayerComFaccao(p);
					return;
			        }
				} else {
					e.setCancelled(true);
					abrirMenuPlayerSemFaccao(p);
					return;
				}
			}
		}
		
		else if (cmd.startsWith("/f desfazer") || cmd.startsWith("/f disband") || cmd.startsWith("/f deletar")) {
			if (p instanceof Player ) {
				if (mplayer.hasFaction()) {
					if (mplayer.getRole() == Rel.LEADER || mplayer.isOverriding()) {
						e.setCancelled(true);
						abrirMenuDesfazerFaccao(p);
						return;
					}
					e.setCancelled(true);
					p.sendMessage("�cApenas o l�der pode desfazer a fac��o.");
					return;
				}
				return;
			}
		}
		
		else if (cmd.equalsIgnoreCase("/f convite") || cmd.equalsIgnoreCase("/f invite") || cmd.equalsIgnoreCase("/f convidar") || cmd.equalsIgnoreCase("/f adicionar")  || cmd.equalsIgnoreCase("/f i")) {
			if (p instanceof Player ) {
				if (mplayer.hasFaction()) {
					if (mplayer.getRole() == Rel.OFFICER ||mplayer.getRole() == Rel.LEADER || mplayer.isOverriding()) {
						e.setCancelled(true);
						abrirMenuConvite(p);
						return;
					}
					e.setCancelled(true);
					p.sendMessage("�cVoc� precisar ser capit�o ou superior para poder gerenciar os convites da fac��o.");
					return;
				}
				return;
			}
		}
		
		else if (cmd.startsWith("/f desproteger all") || cmd.startsWith("/f abandonar all") || cmd.startsWith("/f unclaim all")) {
			if (p instanceof Player ) {
				if (mplayer.hasFaction()) {
					if (mplayer.getRole() == Rel.LEADER || mplayer.getRole() == Rel.OFFICER || mplayer.isOverriding()) {
						if (terras >= 1) {
							abrirMenuAbandonarTerras(p);
							e.setCancelled(true);
							return;
						} else {
							p.sendMessage("�cA sua fac��o n�o possui terras para abandonar.");
							e.setCancelled(true);
							return; 
						}
					}
					p.sendMessage("�cSua fac��o n�o deixa voc� administrar os territ�rios.");
					e.setCancelled(true);
					return;
				}
				return;
			}
		}
		
		else if (cmd.equalsIgnoreCase("/f relacao") || cmd.equalsIgnoreCase("/f rela��o") || cmd.equalsIgnoreCase("/f relation") || cmd.equalsIgnoreCase("/f rel")) {
			if (p instanceof Player ) {
				if (mplayer.hasFaction()) {
					if (mplayer.getRole() == Rel.OFFICER ||mplayer.getRole() == Rel.LEADER || mplayer.isOverriding()) {
						e.setCancelled(true);
						abrirMenuRelacoes(p);
						return;
					}
					e.setCancelled(true);
					p.sendMessage("�cVoc� precisar ser capit�o ou superior para poder gerenciar as rela��es da fac��o.");
					return;
				}
				return;
			}
		}
	}
	
	public void abrirMenuPlayerComFaccao(Player p) {
		
		/*
		 * VARIAVEIS
		 */
		
		final MPlayer mplayer = MPlayer.get(p);
		final Rel cargo = mplayer.getRole();
		final Faction factionclaim = BoardColl.get().getFactionAt(PS.valueOf(p.getLocation()));
		final Faction faction = mplayer.getFaction();
		final String factionNome = faction.getName();
		final String lider = faction.getLeader().getName();
		final String factiondesc = faction.getDescriptionDesc();
		int fackills = EngineKdr.getFacKills(faction);
		int facmortes = EngineKdr.getFacDeaths(faction);
		int membrosonline = faction.getOnlinePlayers().size();
		int membrosnafac = faction.getMPlayers().size();
		int terrastotal = faction.getLandCount();
		int mortes = EngineKdr.getPlayerDeaths(mplayer);
		int kills = EngineKdr.getPlayerKills(mplayer);
		double fackdr = EngineKdr.getFacKdr(faction);
		double factionpodermaximo = faction.getPowerMax();
		double playerpodermaximo = mplayer.getPowerMax();
		double factionpoder = faction.getPower();
		double playerpoder = mplayer.getPower();
		final String kdr2f = EngineKdr.getPlayerKdr(mplayer);
		final String fackdr2f = String.format("%.2f", fackdr);
		final String playerpoder1f = String.format("%.1f", playerpoder);
		final String factionpoder1f = String.format("%.1f", factionpoder);
	
			Inventory inv = Bukkit.createInventory(null, 54, "�r�8Fac��o - " + factionNome);
					
			/*
			 * ITEMS NORMAIS
			 */
			
			inv.setItem(10, new ItemBuilder(Material.SKULL_ITEM,1,3).setSkullOwner(p.getName()).setName("�7"+p.getName()).setLore("�fPoder: �7" +  playerpoder1f + "/" + playerpodermaximo,"�fCargo: �7" + cargo.getPrefix() + cargo.getName(),"�fAbates: �7" + kills,"�fMortes: �7" + mortes, "�fKdr: �7" + kdr2f).toItemStack());
			inv.setItem(14, new ItemBuilder(Heads.ROXO.clone()).setName("�eRanking das Fac��es").setLore("�7Clique para ver os rankings com as", "�7fac��es mais poderosas do servidor.").toItemStack());
			inv.setItem(15, new ItemBuilder(Heads.LARANJA.clone()).setName("�eFac��es Online").setLore("�7Clique para ver a lista de fac��es","�7online no servidor.").toItemStack());
			inv.setItem(16, new ItemBuilder(Heads.AMARELO.clone()).setName("�eAjuda").setLore("�7Todas as a��es dispon�veis neste menu", "�7tamb�m podem ser realizadas por","�7comando. Utilize o comando '�f/f ajuda�7'", "�7para ver todos os comandos dispon�veis.").toItemStack());
			inv.setItem(30, new ItemBuilder(Material.PAPER).setName("�eGerenciar Convites").setLore("�fClique para gerenciar os","�fconvites da sua fac��o.").toItemStack());
			inv.setItem(31, new ItemBuilder(Material.BOOK_AND_QUILL).setName("�dGerenciar permiss�es").setLore("�fClique para gerenciar as","�fpermiss�es da sua fac��o.").toItemStack());
			inv.setItem(39, new ItemBuilder(Material.SKULL_ITEM,membrosnafac,3).setName("�aMembros").setLore("�fA sua fac��o possui �3" + membrosnafac + "�f membros.","�fClique para obter mais informa��es.").toItemStack());
			inv.setItem(40, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�aRela��es").setLore("�fClique para gerenciar todas","�fas rela��es da sua fac��o.").setLeatherArmorColor(Color.LIME).toItemStack());
			
			/*
			 * ITENS QUE USAM SUBSTRING
			 */
			if (!EngineSobAtaque.factionattack.containsKey(faction)) {
			inv.setItem(34, new ItemBuilder(Heads.BRANCO.clone()).setName("�e" + factionNome).setLore("�aA fac��o n�o esta sob ataque.","�fTerras: �7" + terrastotal,"�fPoder: �7" + factionpoder1f,"�fPoder m�ximo: �7" + factionpodermaximo, "�fAbates: �7" + fackills, "�fMortes: �7" + facmortes, "�fKdr: �7" + fackdr2f, "�fL�der: �7" + lider,"�fMembros: �7" + membrosnafac + "/" + MConf.get().factionMemberLimit, "�fMembros online: �7" + membrosonline, faction.getOnlinePlayers().size() == 1 ? ("�7  " + EngineEditSource.fplayers(faction).toString().replace("[", "").replace("]", "").replace(",", "")) : ("�7  " + EngineEditSource.fplayers(faction).toString().replace("[", "").replace("]", "")),"�7","�fDescri��o:","�7'"+factiondesc+"�7'","�f", "�fMotd: �7", EngineEditSource.fmotd(faction)).toItemStack());
			} else {
			inv.setItem(34, new ItemBuilder(Heads.VERMELHO.clone()).setName("�e" + factionNome).setLore("�cFac��o sob ataque! Clique para mais detalhes.","�fTerras: �7" + terrastotal,"�fPoder: �7" + factionpoder1f,"�fPoder m�ximo: �7" + factionpodermaximo, "�fAbates: �7" + fackills, "�fMortes: �7" + facmortes, "�fKdr: �7" + fackdr2f, "�fL�der: �7" + lider,"�fMembros: �7" + membrosnafac + "/" + MConf.get().factionMemberLimit, "�fMembros online: �7" + membrosonline, faction.getOnlinePlayers().size() == 1 ? ("�7  " + EngineEditSource.fplayers(faction).toString().replace("[", "").replace("]", "").replace(",", "")) : ("�7  " + EngineEditSource.fplayers(faction).toString().replace("[", "").replace("]", "")),"�7","�fDescri��o:","�7'"+factiondesc+"�7'","�f", "�fMotd: �7", EngineEditSource.fmotd(faction)).toItemStack()); }
			
			if (mplayer.getRole() == Rel.LEADER) {
			inv.setItem(43, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM).setName("�cDesfazer fac��o").setLore("�7Clique para desfazer a sua fac��o.").toItemStack());
			} else {
			inv.setItem(43, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM).setName("�cSair da fac��o").setLore("�7Clique para abandonar a sua fac��o.").toItemStack()); }	
			
			if (mplayer.isTerritoryInfoTitles() == true) {
			inv.setItem(37, new ItemBuilder(Material.PAINTING).setName("�eTitulos dos Territ�rios").setLore("�7Clique para alternar.","","�fStatus: �aAtivado").toItemStack());
			} else {
			inv.setItem(37, new ItemBuilder(Material.PAINTING).setName("�eTitulos dos Territ�rios").setLore("�7Clique para alternar.","","�fStatus: �cDesativado").toItemStack()); }
			
			if (mplayer.isMapAutoUpdating() == true) {
			inv.setItem(38, new ItemBuilder(Material.MAP).setName("�aMapa dos Territ�rios").setLore("�7Voc� esta pisando em um territ�rio","�7protegido pela fac��o �e" + factionclaim.getName(),"","�fBot�o direito: �7Mostra o mapa completo.","�fBot�o esquerdo: �7Desliga o mapa autom�tico.", "", "�fMapa autom�tico: �aLigado").addItemFlag(ItemFlag.HIDE_POTION_EFFECTS).toItemStack());
			} else {
			inv.setItem(38, new ItemBuilder(Material.EMPTY_MAP).setName("�aMapa dos Territ�rios").setLore("�7Voc� esta pisando em um territ�rio","�7protegido pela fac��o �e" + factionclaim.getName(),"","�fBot�o direito: �7Mostra o mapa completo.","�fBot�o esquerdo: �7Liga o mapa autom�tico.", "", "�fMapa autom�tico: �cDesligado").toItemStack()); }
			
			if (mplayer.isSeeingChunk() == true) {
			inv.setItem(28, new ItemBuilder(Material.GRASS).setName("�aDelimita��es das Terras").setLore("�7Clique para alternar.","","�fStatus: �aAtivado").toItemStack());
			} else {
			inv.setItem(28, new ItemBuilder(Material.GRASS).setName("�aDelimita��es das Terras").setLore("�7Clique para alternar.","","�fStatus: �cDesativado").toItemStack());	}
			
			if (faction.hasHome() == true && (cargo == Rel.LEADER || cargo == Rel.OFFICER)) {
			inv.setItem(29, new ItemBuilder(Material.BEDROCK).setName("�aBase da Fac��o").setLore("�7Sua fac��o possui uma base!","","�fBot�o esquerdo: �7Ir para base.","�fBot�o direito: �7Definir base.","�fShift + Bot�o direito: �7Remover base.").toItemStack());
			} else if (faction.hasHome() == true && (!(cargo == Rel.LEADER || cargo == Rel.OFFICER))) {
			inv.setItem(29, new ItemBuilder(Material.BEDROCK).setName("�aBase da Fac��o").setLore("�7Sua fac��o possui uma base!","","�fBot�o esquerdo: �7Ir para base.").toItemStack());
			} else if (faction.hasHome() == false && (cargo == Rel.LEADER || cargo == Rel.OFFICER)) {
			inv.setItem(29, new ItemBuilder(Material.BEDROCK).setName("�aBase da Fac��o").setLore("�7Sua fac��o ainda n�o definiu uma base.","","�fBot�o direito: �7Definir base.").toItemStack());
			} else if (faction.hasHome() == false && (!(cargo == Rel.LEADER || cargo == Rel.OFFICER))) { 	
			inv.setItem(29, new ItemBuilder(Material.BEDROCK).setName("�aBase da Fac��o").setLore("�7Sua fac��o ainda n�o definiu uma base.").toItemStack()); }

			p.openInventory(inv);
	}
	
	public void abrirMenuPlayerSemFaccao(Player p) {
		
		/*
		 * VARIAVEIS
		 */
		
		final MPlayer mplayer = MPlayer.get(p);
		final Faction factionclaim = BoardColl.get().getFactionAt(PS.valueOf(p.getLocation()));
		int mortes = EngineKdr.getPlayerDeaths(mplayer);
		int kills = EngineKdr.getPlayerKills(mplayer);
		double playerpodermaximo = mplayer.getPowerMax();
		double playerpoder = mplayer.getPower();
		final String kdr2f = EngineKdr.getPlayerKdr(mplayer);
		final String playerpoder1f = String.format("%.1f", playerpoder);
		
		Inventory inv = Bukkit.createInventory(null, 45, "�8Sem fac��o�1�2�3");
		
		/*
		 * ITEMS NORMAIS
		 */
		
		inv.setItem(10, new ItemBuilder(Material.SKULL_ITEM,1,3).setSkullOwner(p.getName()).setName("�7"+p.getName()).setLore("�fPoder: �7" +  playerpoder1f + "/" + playerpodermaximo,"�fCargo: �7�oNenhum","�fAbates: �7" + kills,"�fMortes: �7" + mortes, "�fKdr: �7" + kdr2f).toItemStack());
		inv.setItem(14, new ItemBuilder(Heads.ROXO.clone()).setName("�eRanking das Fac��es").setLore("�7Clique para ver os rankings com as", "�7fac��es mais poderosas do servidor.").toItemStack());
		inv.setItem(15, new ItemBuilder(Heads.LARANJA.clone()).setName("�eFac��es Online").setLore("�7Clique para ver a lista de fac��es","�7online no servidor.").toItemStack());
		inv.setItem(16, new ItemBuilder(Heads.AMARELO.clone()).setName("�eAjuda").setLore("�7Todas as a��es dispon�veis neste menu", "�7tamb�m podem ser realizadas por","�7comando. Utilize o comando '�f/f ajuda�7'", "�7para ver todos os comandos dispon�veis.").toItemStack());
		inv.setItem(29, new ItemBuilder(Material.BANNER,1,15).setName("�aCriar fac��o").setLore("�7Crie j� sua fac��o usando", "�7o comando '�f/f criar <nome>�7'").toItemStack());
		inv.setItem(30, new ItemBuilder(Material.PAPER).setName("�eEntrar em uma Fac��o").setLore("�7Entre j� em uma fac��o usando","�7o comando '�f/f entrar <nome>�7'","","�7�oLembre-se, voc� precisa de", "�7�oum convite para poder entrar.").toItemStack());

		/*
		 * ITENS QUE USAM VERIFICA��ES
		 */
		
		if (mplayer.isMapAutoUpdating() == true) {
		inv.setItem(31, new ItemBuilder(Material.MAP).setName("�aMapa dos Territ�rios").setLore("�7Voc� esta pisando em um territ�rio","�7protegido pela fac��o �e" + factionclaim.getName(),"","�fBot�o direito: �7Mostra o mapa completo.","�fBot�o esquerdo: �7Desliga o mapa autom�tico.", "", "�fMapa autom�tico: �aLigado").addItemFlag(ItemFlag.HIDE_POTION_EFFECTS).toItemStack());
		} else {
		inv.setItem(31, new ItemBuilder(Material.EMPTY_MAP).setName("�aMapa dos Territ�rios").setLore("�7Voc� esta pisando em um territ�rio","�7protegido pela fac��o �e" + factionclaim.getName(),"","�fBot�o direito: �7Mostra o mapa completo.","�fBot�o esquerdo: �7Liga o mapa autom�tico.", "", "�fMapa autom�tico: �cDesligado").toItemStack()); }
		
		if (mplayer.isTerritoryInfoTitles() == true) {
		inv.setItem(32, new ItemBuilder(Material.PAINTING).setName("�eTitulos dos Territ�rios").setLore("�7Clique para alternar.","","�fStatus: �aAtivado").toItemStack());
		} else {
		inv.setItem(32, new ItemBuilder(Material.PAINTING).setName("�eTitulos dos Territ�rios").setLore("�7Clique para alternar.","","�fStatus: �cDesativado").toItemStack()); }
			
		if (mplayer.isSeeingChunk() == true) {
		inv.setItem(33, new ItemBuilder(Material.GRASS).setName("�aDelimita��es das Terras").setLore("�7Clique para alternar.","","�fStatus: �aAtivado").toItemStack());
		} else {
		inv.setItem(33, new ItemBuilder(Material.GRASS).setName("�aDelimita��es das Terras").setLore("�7Clique para alternar.","","�fStatus: �cDesativado").toItemStack());	}
	
		p.openInventory(inv);
	}
	
	public static void abrirMenuFaccaoSobAtaque(Player p) {
		
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();		
		Inventory inv = Bukkit.createInventory(null, 54, "�8Terrenos sob ataque");
		
		Set<Chunk> chunks = EngineSobAtaque.underattack.keySet();

		int slot = 0;
		Iterator<Chunk> it = chunks.iterator();
		while (it.hasNext() && slot < 54) {
			Chunk c = it.next();
			Faction fac = BoardColl.get().getFactionAt(PS.valueOf(c));
			if (fac.equals(faction)) {
				Location l = EngineSobAtaque.infoattack.get(c);
				inv.setItem(slot, new ItemBuilder(Material.GRASS).setName("�e�l#"+slot+"�e Territ�rio sob ataque!").setLore("�7Chunk: �fX:"+ c.getX() + ", Z:" + c.getZ(),"�7Mundo: �f" + c.getWorld().getName(),"�7Coordenadas: �fX:" + l.getBlockX() + "�8, �fY:" + l.getBlockY() + "�8, �fZ:" + l.getBlockZ()).toItemStack());
			}
			slot++;
		}
		
		p.openInventory(inv);
	}
	
	@SuppressWarnings("deprecation")
	public void abrirMenuDesfazerFaccao(Player p) {
		
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
        String factionNome = faction.getName();
		
		Inventory inv = Bukkit.createInventory(null, 36, "�1�2�3�8Desfazer fac��o");
		
		inv.setItem(13, new ItemBuilder(Material.PAPER).setName("�fInforma��es").setLore("�7Voc� esta prestes a desfazer", "�7totalmente a fac��o �e" + factionNome + "�7.").toItemStack());
		inv.setItem(20, new ItemBuilder(Material.WOOL, 1, DyeColor.LIME.getWoolData()).setName("�aConfirmar a��o").setLore("�7Clique para confirmar.").toItemStack());
		inv.setItem(24, new ItemBuilder(Material.WOOL, 1, DyeColor.RED.getWoolData()).setName("�cCancelar a��o").setLore("�7Clique para cancelar.").toItemStack());

		p.openInventory(inv);
	}
	
	@SuppressWarnings("deprecation")
	public void abrirMenuAbandonarTerras(Player p) {
		
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
        int terras = faction.getLandCount();
		
		Inventory inv = Bukkit.createInventory(null, 36, "�8Abandonar todas as terras");
		
		inv.setItem(13, new ItemBuilder(Material.PAPER).setName("�fInforma��es").setLore("�7Voc� esta prestes a abandonar �2" + terras + " �7terras.").toItemStack());
		inv.setItem(20, new ItemBuilder(Material.WOOL, 1, DyeColor.LIME.getWoolData()).setName("�aConfirmar a��o").setLore("�7Clique para confirmar.").toItemStack());
		inv.setItem(24, new ItemBuilder(Material.WOOL, 1, DyeColor.RED.getWoolData()).setName("�cCancelar a��o").setLore("�7Clique para cancelar.").toItemStack());

		p.openInventory(inv);
	}
	
	public void abrirMenuConvite(Player p) {
		
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
		int nconvites = faction.getInvitations().size();
        
		Inventory inv = Bukkit.createInventory(null, 27, "�1�2�3�8Gerenciar convites");
		
		inv.setItem(11, new ItemBuilder(Heads.AZURE.clone()).setName("�aEnviar convite").setLore("�7Envie j� um convite de fac��o usando", "�7o comando '�f/f convite enviar <nome>�7'").toItemStack());
		if (nconvites == 0) {
		inv.setItem(15, new ItemBuilder(Material.PAPER).setName("�eGerenciar convites pendentes").setLore("�cSua fac��o n�o possui convites pendentes.").setAmount(nconvites).toItemStack());
		} else {
		inv.setItem(15, new ItemBuilder(Material.PAPER).setName("�eGerenciar convites pendentes").setLore("�fSua fac��o possui �3" + nconvites + (nconvites == 1 ? " �fconvite pendente." : " �fconvites pendentes."),"�fClique para gerenciar" + (nconvites == 1 ? " �fo convite pendente." : " �fos convites pendentes.") ).setAmount(nconvites).toItemStack()); }
		
		p.openInventory(inv);
	}
	
	public void abrirMenuRelacoes(Player p) {
		
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
        int nrelations = faction.getRelationWishes().size() - EngineEditSource.getAliadosPendentesEnviados(faction).size();
        
		Inventory inv = Bukkit.createInventory(null, 27, "�8Gerenciar rela��es");
		
		inv.setItem(11, new ItemBuilder(Heads.AZURE.clone()).setName("�aDefinir rela��o").setLore("�7Defina j� uma rela��o com alguma fac��o usando" , "�7o comando '�f/f rela��o definir <fac��o>�7'").toItemStack());
		inv.setItem(13, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�aAlian�as pendentes").setLore("�fClique para ver todos os convites","�fde alian�a pendentes recebidos ou", "�fenviados pela sua fac��o.").setLeatherArmorColor(Color.LIME).setAmount(EngineEditSource.getAliadosPendentesEnviados(faction).size() + EngineEditSource.getAliadosPendentesRecebidos(faction).size()).toItemStack());
		if (nrelations < 1) {
			inv.setItem(15, new ItemBuilder(Material.BOOK).setName("�eVer rela��es").setLore("�cSua fac��o n�o possui rela��es definidas.").setAmount(nrelations).toItemStack());
		} else if (nrelations > 64) {
			inv.setItem(15, new ItemBuilder(Material.BOOK).setName("�eVer rela��es").setLore("�cSua fac��o n�o possui rela��es definidas.").toItemStack());
		} else {
		inv.setItem(15, new ItemBuilder(Material.BOOK).setName("�eVer rela��es").setLore("�fClique para ver a lista", "�fde rela��es da sua fac��o.").setAmount(nrelations).toItemStack()); }
		
		p.openInventory(inv);
	}
	
	public void abrirMenuRelacoesPendentes(Player p) {
		
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
        int enviados = EngineEditSource.getAliadosPendentesEnviados(faction).size();
        int recebidos = EngineEditSource.getAliadosPendentesRecebidos(faction).size();
        int npendentes = enviados + recebidos;
        
		Inventory inv = Bukkit.createInventory(null, 36, "�8Alian�as pendentes (" + npendentes + ")");
		
		if (recebidos > 0) {
		inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�bConvites de alian�a recebidos pendentes").setLore("�fSua fac��o possui �3" + recebidos + (recebidos == 1 ? " �fconvite" : " �fconvites") + (recebidos == 1 ? " recebido pendente." : " recebidos pendentes.")).setLeatherArmorColor(Color.AQUA).setAmount(recebidos).toItemStack());
		} else {
		inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�bConvites de alian�a recebidos pendentes").setLore("�cSua fac��o n�o possui nenhum", "�cconvite recebido pendente.").setLeatherArmorColor(Color.AQUA).setAmount(0).toItemStack()); }
		
		if (enviados > 0) {
		inv.setItem(15, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�aConvites de alian�a enviados pendentes").setLore("�fSua fac��o possui �3" + enviados + (enviados == 1 ? " �fconvite" : " �fconvites") + (enviados == 1 ? " enviado pendente." :  "enviados pendentes.")).setLeatherArmorColor(Color.LIME).setAmount(enviados).toItemStack());		
		} else {
		inv.setItem(15, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("�aConvites de alian�a enviados pendentes").setLore("�cSua fac��o n�o possui nenhum", "�cconvite enviado pendente.").setLeatherArmorColor(Color.LIME).setAmount(0).toItemStack()); }
		p.openInventory(inv);
		
		inv.setItem(31, new ItemBuilder(Material.ARROW).setName("�1�2�cVoltar").toItemStack());
	}
	
	public void abrirMenuRelacoesPendentesEnviados(Player p) {
		
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
        int enviados = EngineEditSource.getAliadosPendentesEnviados(faction).size();
		int tamanho = 54;
		int flecha = 49;
		int slot = 11;
		
		//Tamano do menu GUI
		if (enviados <= 5) {
			tamanho = 36;
			flecha = 31;
		} else if (enviados > 5 && enviados <= 10) {
			tamanho = 45;
			flecha = 40;
		} else { 
			tamanho = 54;
			flecha = 49;
		}
		
		Inventory inv = Bukkit.createInventory(null, tamanho, "�8Convites enviados pendentes");
		inv.setItem(flecha, new ItemBuilder(Material.ARROW).setName("�1�2�cVoltar").toItemStack());
		
		List<Faction> facs = EngineEditSource.getAliadosPendentesEnviados(faction);
		for (int i = 0; i < facs.size(); i++) {
			Faction f = facs.get(i);
			String factionNome = f.getName();
			inv.setItem(slot, new ItemBuilder(Material.PAPER).setName("�eConvite para fac��o "+factionNome).setLore("�fBot�o Direito: �7Deletar convite","�fShift + Bot�o direito: �7Informa��es da fac��o").toItemStack());
			
			slot+= slot == 15 || slot == 24 ? + 5 : + 1;
		}
		p.openInventory(inv);
	}
	
	public void abrirMenuRelacoesPendentesRecebidos(Player p) {
		
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
        int recebidos = EngineEditSource.getAliadosPendentesRecebidos(faction).size();
		int tamanho = 54;
		int flecha = 49;
		int slot = 11;
		
		//Tamano do menu GUI
		if (recebidos <= 5) {
			tamanho = 36;
			flecha = 31;
		} else if (recebidos > 5 && recebidos <= 10) {
			tamanho = 45;
			flecha = 40;
		} else { 
			tamanho = 54;
			flecha = 49;
		}
		
		Inventory inv = Bukkit.createInventory(null, tamanho, "�8Convites recebidos pendentes");
		inv.setItem(flecha, new ItemBuilder(Material.ARROW).setName("�1�2�cVoltar").toItemStack());
		
		List<Faction> facs = EngineEditSource.getAliadosPendentesRecebidos(faction);
		for (int i = 0; i < facs.size(); i++) {
			Faction f = facs.get(i);
			String factionNome = f.getName();
			inv.setItem(slot, new ItemBuilder(Material.PAPER).setName("�eConvite da fac��o "+factionNome).setLore("�fBot�o Esquerdo: �7Aceitar convite","�fBot�o Direito: �7Deletar convite","�fShift + Bot�o direito: �7Informa��es da fac��o").toItemStack());
			
			slot+= slot == 15 || slot == 24 ? + 5 : + 1;
		}
		p.openInventory(inv);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void aoClickar(InventoryClickEvent e) {
		
		if (e.getSlotType().equals(SlotType.OUTSIDE) || e.getCurrentItem() == null || e.getCurrentItem().getTypeId() == 0)
			return;

		Player p = (Player)e.getWhoClicked();
    	String inventarioNome = e.getInventory().getName();
    	int slot = e.getSlot();
		
		if (inventarioNome.equalsIgnoreCase("�8Sem fac��o�1�2�3")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
			MPlayer mp = MPlayer.get(p);
			
			if (slot == 14) {
				p.chat("/f top");
			}
		
			else if (slot == 15) {
				p.performCommand("f listar");
				p.closeInventory();
			}
		
			else if (slot == 16) {
				p.performCommand("f ajuda");
				p.closeInventory();
			}
		
			else if (slot == 29) {
				p.sendMessage("�f");
				p.sendMessage("�aCrie j� a sua fac��o usando o comando '�f/f criar <nome>�a'");
				p.sendMessage("�f");
				p.closeInventory();
			}
			
			else if (slot == 30) {
				p.sendMessage("�f");
				p.sendMessage("�aEntra j� em uma fac��o usando o comando '�f/f entrar <nome>�a'");
				p.sendMessage("�f");
				p.closeInventory();
			}
			
			else if (slot == 31) {
				if (e.getClick().isRightClick()) {
					p.performCommand("f mapa");
				}
				else if (e.getClick().isLeftClick()) {
					if (mp.isMapAutoUpdating() == true) {
						mp.setMapAutoUpdating(false);
						abrirMenuPlayerSemFaccao(p);
					} else { 
						mp.setMapAutoUpdating(true);
						abrirMenuPlayerSemFaccao(p);
					}
				}
			}
			
			else if (slot == 32) {
				p.performCommand("f tt");
				abrirMenuPlayerSemFaccao(p);
			}
			
			else if (slot == 33) {
				p.performCommand("f sc");
				abrirMenuPlayerSemFaccao(p);
			}
		}
		
		else if (inventarioNome.startsWith("�r�8Fac��o - ")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
			MPlayer mp = MPlayer.get(p);
			Faction f = mp.getFaction();
			
			if (slot == 34) {
				if (EngineSobAtaque.factionattack.containsKey(f)) {
					abrirMenuFaccaoSobAtaque(p);
				}
			}
			
			else if (slot == 14) {
				p.chat("/f top");
			}
		
			else if (slot == 15) {
				p.performCommand("f listar");
				p.closeInventory();
			}
		
			else if (slot == 16) {
				p.performCommand("f ajuda");
				p.closeInventory();
			}
			
			else if (slot == 28) {
				p.performCommand("f sc");
				abrirMenuPlayerComFaccao(p);
			}
			
			else if (slot == 29) {
				if (f.hasHome() == false) {
					if (mp.getRole() == Rel.LEADER || mp.getRole() == Rel.OFFICER || mp.isOverriding()) {
						if (e.getClick().isRightClick()) {
							p.performCommand("f sethome");
							abrirMenuPlayerComFaccao(p);
						}
					}
				} else {
					if (e.getClick().isShiftClick()) {
						if (mp.getRole() == Rel.LEADER || mp.getRole() == Rel.OFFICER || mp.isOverriding()) {
							p.performCommand("f delhome");
							abrirMenuPlayerComFaccao(p);
							return;
						}
					}
					if (e.getClick().isRightClick()) {
						if (mp.getRole() == Rel.LEADER || mp.getRole() == Rel.OFFICER || mp.isOverriding()) {
							p.performCommand("f sethome");
							return;
						}
					}
					if (e.getClick().isLeftClick()) {
						p.performCommand("f home");
						return;
					}
				}
			}
			
			else if (slot == 30) {
				if (mp.getRole() == Rel.LEADER || mp.getRole() == Rel.OFFICER || mp.isOverriding()) {
					p.chat("/f convite");
				} else {
					p.sendMessage("�cVoc� precisar ser capit�o ou superior para poder gerenciar os convites da fac��o.");
				}
			}
			
			else if (slot == 31) {
				p.performCommand("f perm");
			}
			
			else if (slot == 37) {
				p.performCommand("f tt");
				abrirMenuPlayerComFaccao(p);
			}
			
			else if (slot == 38) {
				if (e.getClick().isRightClick()) {
					p.performCommand("f mapa");
				}
				else if (e.getClick().isLeftClick()) {
					if (mp.isMapAutoUpdating() == true) {
						mp.setMapAutoUpdating(false);
						abrirMenuPlayerComFaccao(p);
					} else { 
						mp.setMapAutoUpdating(true);
						abrirMenuPlayerComFaccao(p);
					}
				}
			}
			
			else if (slot == 39) {
				p.performCommand("f membros");
			}
			
			else if (slot == 40) {
				if (mp.getRole() == Rel.LEADER || mp.getRole() == Rel.OFFICER || mp.isOverriding()) {
					abrirMenuRelacoes(p);
				} else {
					p.sendMessage("�cVoc� precisar ser capit�o ou superior para poder gerenciar as rela��es da fac��o.");
				}
			}
			
			else if (slot == 43) {
				if (mp.getRole() == Rel.LEADER || mp.isOverriding()) {
					abrirMenuDesfazerFaccao(p);
				} else { 
					p.performCommand("f sair");
				}
			}
		}
		
		else if (inventarioNome.equalsIgnoreCase("�1�2�3�8Desfazer fac��o")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
		
			if (slot == 24) {
				p.sendMessage("�cA��o cancelada com sucesso.");
				p.closeInventory();
			}
			
			else if (slot == 20) {
				p.performCommand("f desfazer");
				p.closeInventory();
			}
		}
		
		else if (inventarioNome.equalsIgnoreCase("�8Abandonar todas as terras")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
			MPlayer mp = MPlayer.get(p);
			Faction f = mp.getFaction();
	    	String factionNome = f.getName();
		
			if (slot == 24) {
				p.sendMessage("�cA��o cancelada com sucesso.");
				p.closeInventory();
			}
			
			else if (slot == 20) {
				p.performCommand("f unclaim all all " + factionNome.replaceAll(" ", ""));
				p.closeInventory();
			}
		}
		
		else if (inventarioNome.equalsIgnoreCase("�1�2�3�8Gerenciar convites")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
			MPlayer mp = MPlayer.get(p);
			Faction f = mp.getFaction();
		
			if (slot == 11) {
				p.sendMessage("�f");
				p.sendMessage("�aEnvie j� um convite de fac��o para um player usando o comando '�f/f convite enviar <nome>�a'");
				p.sendMessage("�f");
				p.closeInventory();
			}
			
			else if (slot == 15) {
				if (f.getInvitations().size() > 0) {
					p.performCommand("f convite listar");
				} else {
					p.chat("/f convite");
				}
			}
		}
		
		else if (inventarioNome.startsWith("�1�2�3�8Convites pendentes (")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
		
			ItemStack item = e.getCurrentItem();
			
			if (item.getType() == Material.ARROW) {
				p.chat("/f convite");
			}
			
			else if (item.getType() == Material.PAPER) {
				if (e.getClick().isRightClick()) {
					int nomeTamanho = item.getItemMeta().getLore().get(0).length();
					String nome = item.getItemMeta().getLore().get(0).substring(30, nomeTamanho);
					p.performCommand("f convite del " + nome);
					p.performCommand("f convite listar");
				} else if (e.getClick().isLeftClick()) {
					int nomeTamanho = item.getItemMeta().getLore().get(0).length();
					String nome = item.getItemMeta().getLore().get(0).substring(30, nomeTamanho);
					p.performCommand("f perfil " + nome);
					p.closeInventory();
				} else {
					p.performCommand("f convite listar");
				}
			}
		}
		
		else if (inventarioNome.equalsIgnoreCase("�8Gerenciar rela��es")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
			MPlayer mp = MPlayer.get(p);
			Faction f = mp.getFaction();
	    	
			if (slot == 11) {
				p.sendMessage("�f");
				p.sendMessage("�eDefina j� uma rela��o com alguma fac��o usando o comando '�f/f rela��o definir <fac��o>�e'");
				p.sendMessage("�f");
				p.closeInventory();
			}
			
			else if (slot == 13) {
				abrirMenuRelacoesPendentes(p);
			}
			
			else if (slot == 15) {
				if ((f.getRelationWishes().size() - EngineEditSource.getAliadosPendentesEnviados(f).size())  > 0) {
					p.performCommand("f relacao listar");
					p.closeInventory();
				} else {
					p.chat("/f relation");
				}
			}
		}
		
		else if (inventarioNome.startsWith("�8Alian�as pendentes (")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
			MPlayer mp = MPlayer.get(p);
			Faction f = mp.getFaction();
	    	
			if (slot == 15) {
				if (EngineEditSource.getAliadosPendentesEnviados(f).size() > 0) {
					abrirMenuRelacoesPendentesEnviados(p);
				} else {
					abrirMenuRelacoesPendentes(p);
				} 
			}
			
			else if (slot == 11) {
				if (EngineEditSource.getAliadosPendentesRecebidos(f).size() > 0) {
					abrirMenuRelacoesPendentesRecebidos(p);
				} else {
					abrirMenuRelacoesPendentes(p);
				} 
			}
			
			else if (slot == 31) {
				abrirMenuRelacoes(p);
			}
		}
		
		else if (inventarioNome.startsWith("�8Convites enviados pendentes")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
			MPlayer mp = MPlayer.get(p);
			Faction f = mp.getFaction();
		
			ItemStack item = e.getCurrentItem();
			
			if (item.getType() == Material.ARROW) {
				abrirMenuRelacoesPendentes(p);
			}
			
			else if (item.getType() == Material.PAPER) {
				if (e.getClick().isShiftClick()) {
					int nomeTamanho = item.getItemMeta().getDisplayName().length();
					String nome = item.getItemMeta().getDisplayName().substring(22, nomeTamanho);
					p.performCommand("f info " + nome.replace(" ", ""));
					p.closeInventory();
				} else if (e.getClick().isRightClick()) {
					int nomeTamanho = item.getItemMeta().getDisplayName().length();
					String nome = item.getItemMeta().getDisplayName().substring(22, nomeTamanho);
					Faction target = EngineEditSource.getFactionByName(nome);
					f.setRelationWish(target, Rel.NEUTRAL);
					abrirMenuRelacoesPendentesEnviados(p);
				}
			}
		}
		
		else if (inventarioNome.startsWith("�8Convites recebidos pendentes")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
			MPlayer mp = MPlayer.get(p);
			Faction f = mp.getFaction();
	    	String factionNome = f.getName();
			ItemStack item = e.getCurrentItem();
			
			if (item.getType() == Material.ARROW) {
				abrirMenuRelacoesPendentes(p);
			}
			
			else if (item.getType() == Material.PAPER) {
				if (e.getClick().isShiftClick()) {
					int nomeTamanho = item.getItemMeta().getDisplayName().length();
					String nome = item.getItemMeta().getDisplayName().substring(20, nomeTamanho);
					p.performCommand("f info " + nome.replace(" ", ""));
					p.closeInventory();
				} else if (e.getClick().isLeftClick()) {
					int nomeTamanho = item.getItemMeta().getDisplayName().length();
					String nome = item.getItemMeta().getDisplayName().substring(20, nomeTamanho);
					p.performCommand("f relacao definir ally " + nome.replace(" ", ""));
					abrirMenuRelacoesPendentesRecebidos(p);
				} else if (e.getClick().isRightClick()) {
					int nomeTamanho = item.getItemMeta().getDisplayName().length();
					String nome = item.getItemMeta().getDisplayName().substring(20, nomeTamanho);
					Faction target = EngineEditSource.getFactionByName(nome);
					target.setRelationWish(f, Rel.NEUTRAL);
					if (target.getOnlinePlayers().size() > 0) {
						target.msg("�eA fac��o �f" + factionNome + "�e recusou seu convite de alian�a.");
					}
					abrirMenuRelacoesPendentesRecebidos(p);
				}
			}
		}
		
		else if (inventarioNome.startsWith("�8Membros da ")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
		}
		
		else if (inventarioNome.startsWith("�8Terrenos sob ataque")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
		}
		
		else if (inventarioNome.startsWith("�8Rela��o com ")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);
			
			ItemStack item = e.getCurrentItem();
			
			if (slot == 11) {
				if (item.getItemMeta().getLore().size() == 2) {
					int nomeTamanho = item.getItemMeta().getDisplayName().length();
					String nome = item.getItemMeta().getDisplayName().substring(22, nomeTamanho);
					p.performCommand("f relacao definir " + nome.replace(" ", "") + " ally");
					p.closeInventory();
				} else if (item.getItemMeta().getLore().size() > 2) {
					abrirMenuRelacoesPendentes(p);
				} else {
					e.setCancelled(true);
				}
			}
			
			else if (slot == 13) {
				if (item.getItemMeta().getLore().size() == 2) {
					int nomeTamanho = item.getItemMeta().getDisplayName().length();
					String nome = item.getItemMeta().getDisplayName().substring(27, nomeTamanho);
					p.performCommand("f relacao definir " + nome.replace(" ", "") + " neutral");
					p.closeInventory();
				} else {
					e.setCancelled(true);
				}
			}
			
			else if (slot == 15) {
				if (item.getItemMeta().getLore().size() == 2) {
					int nomeTamanho = item.getItemMeta().getDisplayName().length();
					String nome = item.getItemMeta().getDisplayName().substring(25, nomeTamanho);
					p.performCommand("f relacao definir " + nome.replace(" ", "") + " enemy");
					p.closeInventory();
				} else {
					e.setCancelled(true);
				}
			}	
		}
	}
}