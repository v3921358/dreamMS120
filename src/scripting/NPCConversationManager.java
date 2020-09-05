/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package scripting;

import Apple.client.Log;
import Apple.client.commands;
import Apple.console.ProductID;
import Apple.console.groups.setting.Gachapon;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import client.inventory.Equip;
import client.ISkill;
import client.inventory.IItem;
import client.MapleCharacter;
import constants.GameConstants;
import client.inventory.ItemFlag;
import client.MapleClient;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.SkillFactory;
import client.SkillEntry;
import client.MapleStat;
import client.messages.commands.AdminCommand;
import constants.GamblingConstants;
import server.MapleCarnivalParty;
import server.Randomizer;
import server.MapleInventoryManipulator;
import server.MapleShopFactory;
import server.MapleSquad;
import server.maps.MapleMap;
import server.maps.Event_DojoAgent;
import server.maps.AramiaFireWorks;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.packet.PlayerShopPacket;
import server.MapleItemInformationProvider;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import database.DatabaseConnection;
import handling.channel.handler.HiredFishingHandler;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import handling.world.guild.MapleGuild;
import server.MapleCarnivalChallenge;
import java.util.HashMap;
import handling.world.guild.MapleGuildAlliance;
import handling.world.guild.MapleGuildWar;
import java.io.File;
import java.util.Arrays;
import javax.script.Invocable;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.MapleStatEffect;
import server.SpeedRunner;
import server.maps.SpeedRunType;
import server.StructPotentialItem;
import server.Timer;
import server.Timer.CloneTimer;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MonsterDropEntry;
import server.life.MonsterGlobalDropEntry;
import server.maps.Event_PyramidSubway;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.StringUtil;
import tools.FileoutputUtil;

public class NPCConversationManager extends AbstractPlayerInteraction {

    private MapleClient c;
    private int npc, questid;
    private String getText;
    private byte type; // -1 = NPC, 0 = start quest, 1 = end quest
    private byte lastMsg = -1;
    public boolean pendingDisposal = false;
    private Invocable iv;
    private final String npcMode;

    public String Display(int itemId) {
        final MapleData item = MapleItemInformationProvider.getInstance().getItemData1(itemId);
        if (item == null) {
            return null;
        }
        final MapleData smEntry = item.getChildByPath("info");
        return String.valueOf(MapleDataTool.getInt("reqLevel", smEntry));
    }

    public void sendGachapon() {
        final Gachapon mi = Gachapon.getInstance();
        final List<Gachapon> globalEntry = new ArrayList<>(mi.getGlobalDrop());
        String text = "目前數量: " + globalEntry.size() + "\r\n";
        for (final Gachapon de : globalEntry) {
            text += "#i" + de.itemId + "##t" + de.itemId + "#\r\n";
        }
        sendSimple(text);
    }

    /*怪物查詢*/
    public String searchMobs(String mob_name) {
        StringBuilder sb = new StringBuilder();
        MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz"));
        MapleData data = dataProvider.getData("Mob.img");
        if (data != null) {
            String name;
            for (MapleData searchData : data.getChildren()) {
                name = MapleDataTool.getString(searchData.getChildByPath("name"), "NO-NAME");
                if (name.toLowerCase().contains(mob_name.toLowerCase())) {
                    sb.append("#L" + searchData.getName() + "##b").append(Integer.parseInt(searchData.getName())).append("#k - #r").append(name).append("\r\n");
                }
            }
        }
        if (sb.length() == 0) {
            sb.append("查無此怪物名稱.");
        }
        return sb.toString();
    }

    public void sendSimple(String text, String... selections) {
        if (selections.length > 0) // Adding this even if selections length is 0 will do anything, but whatever.
        {
            text += "#b\r\n";
        }
        for (int i = 0; i < selections.length; i++) {
            text += "#L" + i + "#" + selections[i] + "#l\r\n";
        }
        sendSimple(text);
    }

    /*怪物查詢*/
    public final boolean isWeapon(final int itemId) {
        int catｔ = itemId / 10000;
        catｔ = catｔ % 100;
        switch (catｔ) {
            case 30://單劍
            case 31://單斧
            case 32://單棍
            case 33://短刀
            //case 34://雙刀
            case 37://短仗
            case 38://長仗
            case 40://雙劍
            case 41://雙斧
            case 42://雙棍
            case 43://槍
            case 44://矛
            case 45://弓
            case 46://弩
            case 47://拳套
            case 48://指虎
            case 49://火槍
                return true;
            default:
                return false;
        }
    }

    /*刪除指定裝備(檢視)*/
    public String EquipList1(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<String> stra = new LinkedList<String>();
        for (IItem item : equip.list()) {
            Equip eq = (Equip) this.c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(item.getPosition());
            if (isWeapon(item.getItemId()) && !isCash(item.getItemId()) && eq.getOwner() == "") {
                stra.add("#L" + item.getPosition() + ":##v" + item.getItemId() + ":##l");
            }
        }

        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String CharInfo() {
        final StringBuilder builder = new StringBuilder();
        final MapleCharacter other = c.getChannelServer().getPlayerStorage().getCharacterByName(NameInfo());
        if (other == null) {
            builder.append("角色不存在");
            c.getPlayer().dropMessage(6, builder.toString());
        }
        if (other.getClient().getLastPing() <= 0) {
            other.getClient().sendPing();
        }
        builder.append("#e" + MapleClient.getLogMessage(other, ""));
        builder.append(" \r\n 在 ").append(other.getPosition().x);
        builder.append(" / ").append(other.getPosition().y);
        builder.append(" \r\n 血量 : ").append(other.getStat().getHp()).append(" /").append(other.getStat().getCurrentMaxHp());
        builder.append(" || 魔量 : ").append(other.getStat().getMp()).append(" /").append(other.getStat().getCurrentMaxMp());
        builder.append(" \r\n 物理攻擊力 : ").append(other.getStat().getTotalWatk());
        builder.append(" || 魔法攻擊力 : ").append(other.getStat().getTotalMagic());
        builder.append(" \r\n 最高攻擊 : ").append(other.getStat().getCurrentMaxBaseDamage());
        builder.append(" || 攻擊%數 : ").append(other.getStat().dam_r);
        builder.append(" \r\n BOSS攻擊%數 : ").append(other.getStat().bossdam_r);
        builder.append(" \r\n 力量 : ").append(other.getStat().getStr());
        builder.append(" || 敏捷 : ").append(other.getStat().getDex());
        builder.append(" || 智力 : ").append(other.getStat().getInt());
        builder.append(" || 幸運 : ").append(other.getStat().getLuk());
        builder.append(" \r\n #r力量 : ").append(other.getStat().getTotalStr());
        builder.append(" || #r敏捷 : ").append(other.getStat().getTotalDex());
        builder.append(" || #r智力 : ").append(other.getStat().getTotalInt());
        builder.append(" || #r幸運 : ").append(other.getStat().getTotalLuk());
        builder.append(" \r\n #k經驗值 : ").append(other.getExp());
        builder.append(" || 組隊狀態 : ").append(other.getParty() != null);
        builder.append(" \r\n 交易狀態: ").append(other.getTrade() != null);
        builder.append(" \r\n Latency: ").append(other.getClient().getLatency());
        builder.append(" \r\n 最後PING: ").append(other.getClient().getLastPing());
        builder.append(" \r\n 最後PONG: ").append(other.getClient().getLastPong());
        builder.append(" \r\n IP: ").append(other.getClient().getSessionIPAddress());
        builder.append(" \r\n remoteAddress: ");
        other.getClient().DebugMessage(builder);
        return builder.toString();
    }

    public String NameInfo() {
        return AdminCommand.NameInfo;
    }

    /*刪除指定裝備(檢視)*/
    public String List(MapleClient c, int Type) {
        StringBuilder str = new StringBuilder();
        for (IItem item : c.getPlayer().getInventory(AdminCommand.MapleInventory[Type]).list()) {
            str.append("#L").append(item.getPosition()).append(":##v").append(item.getItemId()).append(":#").append("#l#r").append(item.getQuantity());
        }
        return str.toString();
    }

    /*刪除指定裝備(檢視)*/
    public String EquipList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<String> stra = new LinkedList<String>();
        for (IItem item : equip.list()) {
            stra.add("#L" + item.getPosition() + ":##v" + item.getItemId() + ":##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    /*刪除指定裝備(檢視)*/
    public String UseList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory use = c.getPlayer().getInventory(MapleInventoryType.USE);
        List<String> stra = new LinkedList<String>();
        for (IItem item : use.list()) {
            stra.add("#L" + item.getPosition() + ":##v" + item.getItemId() + ":##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    /*刪除指定裝備(檢視)*/
    public String CashList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory cash = c.getPlayer().getInventory(MapleInventoryType.CASH);
        List<String> stra = new LinkedList<String>();
        for (IItem item : cash.list()) {
            stra.add("#L" + item.getPosition() + ":##v" + item.getItemId() + ":##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    /*刪除指定裝備(檢視)*/
    public String ETCList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory etc = c.getPlayer().getInventory(MapleInventoryType.ETC);
        List<String> stra = new LinkedList<String>();
        for (IItem item : etc.list()) {
            stra.add("#L" + item.getPosition() + ":##v" + item.getItemId() + ":##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    /*刪除指定裝備(檢視)*/
    public String SetupList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory setup = c.getPlayer().getInventory(MapleInventoryType.SETUP);
        List<String> stra = new LinkedList<String>();
        for (IItem item : setup.list()) {
            stra.add("#L" + item.getPosition() + ":##v" + item.getItemId() + ":##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    /*刪除指定裝備(角色名字)*/
    public MapleCharacter getCharByName(String name) {
        try {
            return c.getChannelServer().getPlayerStorage().getCharacterByName(name);
        } catch (Exception e) {
            return null;
        }
    }

    public MapleCharacter getV1() {
        return getPlayer();
    }

    public void reloadChar() {
        getPlayer().getClient().sendPacket(MaplePacketCreator.getCharInfo(getPlayer()));
        getPlayer().getMap().removePlayer(getPlayer());
        getPlayer().getMap().addPlayer(getPlayer());
    }

    /*自製強化裝備*/
    public int upgradeItem(byte slot, int Type, int Random, int item, short quantity) {
        Equip eq = (Equip) this.c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
        short initial = 0;
        short later = 0;
        String Lettering = "";
        if (eq.getOwner() != "") {
            playerMessage("該裝備無法進行提升.");
        } else {
            switch (Type) {
                case 0://力量
                    initial = eq.getStr();
                    Lettering = "力量<";
                    eq.setStr((short) (eq.getStr() + Random));
                    later = eq.getStr();
                    break;
                case 1://敏捷
                    initial = eq.getDex();
                    Lettering = "敏捷<";
                    eq.setDex((short) (eq.getDex() + Random));
                    later = eq.getDex();
                    break;
                case 2://智慧
                    initial = eq.getInt();
                    Lettering = "智慧<";
                    eq.setInt((short) (eq.getInt() + Random));
                    later = eq.getInt();
                    break;
                case 3://幸運
                    initial = eq.getLuk();
                    Lettering = "幸運<";
                    eq.setLuk((short) (eq.getLuk() + Random));
                    later = eq.getLuk();
                    break;
                case 4://物攻
                    if (eq.getWatk() == 0) {
                        playerMessage("該裝備沒有物攻係數");
                        return 1;
                    }
                    initial = eq.getWatk();
                    Lettering = "物攻<";
                    double Watks = eq.getWatk() * Random / 100;
                    eq.setWatk((short) (eq.getWatk() + Watks));
                    later = eq.getWatk();
                    break;
                case 5://魔攻
                    if (eq.getMatk() == 0) {
                        playerMessage("該裝備沒有魔攻係數");
                        return 1;
                    }
                    initial = eq.getMatk();
                    Lettering = "魔攻<";
                    double Matks = eq.getMatk() * Random / 100;
                    eq.setMatk((short) (eq.getMatk() + Matks));
                    later = eq.getMatk();
                    break;
            }
            eq.setOwner(Lettering + (Type == 4 || Type == 5 ? Random + "%>" : Random + ">"));//刻上字幕
            playerMessage(Lettering.split("<")[0] + "成功提升「" + (Type == 4 || Type == 5 ? Random + "%" : Random) + "」 " + Lettering.split("<")[0] + "：初始 [" + initial + "] / 提升後「" + later + "」");
            gainItem(item, quantity);
            reloadChar();
        }
        return 1;
    }

    public int gainIItem(int id) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (!ii.itemExists(id)) {
            return 1;
        }
        return 0;
    }

    public int gainRewardItemq(int itemId) {//
        final MapleData item = MapleItemInformationProvider.getInstance().getItemData1(itemId);
        final MapleData smEntry = item.getChildByPath("info");
        int array = MapleDataTool.getInt("reqLevel", smEntry, 0);
        return array;
    }

    public String gainRewardItemqss(int con) {
        int[] array = new int[con];
        for (int a = 0; a < array.length; ++a) {
            int i = 0;
            pick:
            while (i == 0) {
                i = (int) (Math.random() * 1000 + 1);
                for (int b = 0; b < a; ++b) {
                    if (array[b] == i) {
                        i = 0;
                        continue pick;
                    }
                }
                array[a] = i;
            }
        }
        String sumT = "";
        for (int z = 0; z < array.length; ++z) {
            sumT += array[z] + ",";
        }
        int hi = (int) (Math.random() * 1000 + 1);
        int sum = 0;
        for (int h = 0; h < array.length; ++h) {
            if (array[h] == hi) {
                sum += 1;
            }
        }

        return sum + "";
    }

    public NPCConversationManager(MapleClient c, int npc, int questid, byte type, Invocable iv, String npcMode) {
        super(c);
        this.c = c;
        this.npc = npc;
        this.questid = questid;
        this.type = type;
        this.iv = iv;
        this.npcMode = npcMode;
        if (c.getPlayer() != null) {
            c.getPlayer().setNpcNow(npc);
        }
    }

    public Invocable getIv() {
        return iv;
    }

    public int getNpc() {
        return npc;
    }

    public int getQuest() {
        return questid;
    }

    public byte getType() {
        return type;
    }

    public String getNpcMode() {
        return npcMode;
    }

    public void safeDispose() {
        pendingDisposal = true;
    }

    public void dispose() {
        NPCScriptManager.getInstance().dispose(c);
    }

    public void askMapSelection(final String sel) {
        if (lastMsg > -1) {
            return;
        }
        c.sendPacket(MaplePacketCreator.getMapSelection(npc, sel));
        lastMsg = 0xE;//TMS120 0xE
    }

    public void sendNext(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { //sendNext will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", (byte) 0));
        lastMsg = 0;
    }

    public void sendNextS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", type));
        lastMsg = 0;
    }

    public void sendPrev(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendPrevS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", type));
        lastMsg = 0;
    }

    public void sendNextPrev(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", (byte) 0));
        lastMsg = 0;
    }

    public void PlayerToNpc(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", type));
        lastMsg = 0;
    }

    public void sendOk(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendOkS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", type));
        lastMsg = 0;
    }

    public void sendYesNo(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 2, text, "", (byte) 0));
        lastMsg = 2;
    }

    public void sendYesNoS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 2, text, "", type));
        lastMsg = 2;
    }

    public void sendAcceptDecline(String text) {
        askAcceptDecline(text);
    }

    public void sendAcceptDeclineNoESC(String text) {
        askAcceptDeclineNoESC(text);
    }

    public void askAcceptDecline(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, "", (byte) 0));
        lastMsg = 0xC;
    }

    public void askAcceptDeclineNoESC(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0D, text, "", (byte) 0));
        lastMsg = 0xD;
    }

    public void askAvatar(String text, int... args) {
        if (lastMsg > -1) {
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkStyle(npc, text, args));
        lastMsg = 8;
    }

    public void sendSimple(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNext(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 5, text, "", (byte) 0));
        lastMsg = 5;
    }

    public void sendSimpleS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNextS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 5, text, "", (byte) type));
        lastMsg = 5;
    }

    public void sendStyle(String text, int styles[]) {
        if (lastMsg > -1) {
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkStyle(npc, text, styles));
        lastMsg = 8;
    }

    public void sendGetNumber(String text, int def, int min, int max) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkNum(npc, text, def, min, max));
        lastMsg = 4;
    }

    public void sendGetText(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkText(npc, text));
        lastMsg = 3;
    }

    public void setGetText(String text) {
        this.getText = text;
    }

    public String getText() {
        return getText;
    }

    public MapleItemInformationProvider getItemInfo() {
        return MapleItemInformationProvider.getInstance();
    }

    public Equip getEquipBySlot(short slot) {
        return (Equip) getClient().getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
    }

    public boolean changePotential(byte slot, int potline, boolean show) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Equip equip = (Equip) getClient().getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
        if (equip == null || ii.isCash(equip.getItemId())) {
            return false;
        }
        if (potline >= 1 && potline <= 6) {
            final Equip eqq = (Equip) equip;
            if (eqq.getPotential3() != 0) {
                c.getPlayer().dropMessage(5, "裝備的潛能等級已經達到上限囉.");
                return false;
            }
            final int reqLevel = ii.getReqLevel(eqq.getItemId()) / 10;
            final List<List<StructPotentialItem>> pots = new LinkedList<>(ii.getAllPotentialInfo().values());
            int new_state = Math.abs(eqq.getPotential1());
            if (new_state > 7 || new_state < 5) { //luls
                new_state = 5;
            }
            int i = 2; //第三排淺能
            boolean rewarded = false;
            int count = 0;
            while (!rewarded) {
                StructPotentialItem pot = pots.get(Randomizer.nextInt(pots.size())).get(reqLevel);
                if (pot != null && pot.reqLevel / 10 <= reqLevel && GameConstants.optionTypeFits(pot.optionType, eqq.getItemId()) && GameConstants.potentialIDFits(pot.potentialID, new_state, i)) { //optionType
                    if (i == 2) {
                        eqq.setPotential3(pot.potentialID);
                        break;
                    }
                    rewarded = true;
                }
                count++;
                if (count > 100) {
                    return false;
                }
            }
            getClient().getPlayer().forceUpdateItem(equip);
            if (show) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega(getClient().getPlayer().getName(), " : 使用 印章系統 將裝備{" + ii.getName(equip.getItemId()) + "}多增加 1 項潛能", equip, (byte) 2, getClient().getChannel() - 1));
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean canChangeHair(int id) {
        return getItemInfo().hairExists(id);
    }

    public boolean canChangeFace(int id) {
        return getItemInfo().faceExists(id);
    }

    public void setHair(int hair) {
        if (!canChangeHair(hair)) {
            playerMessage(1, "該髮型不存在，無法更換。\r\nID：" + hair);
            return;
        }
        getPlayer().setHair(hair);
        getPlayer().updateSingleStat(MapleStat.HAIR, hair);
        getPlayer().equipChanged();
    }

    public void setFace(int face) {
        getPlayer().setFace(face);
        getPlayer().updateSingleStat(MapleStat.FACE, face);
        getPlayer().equipChanged();
    }

    public void setSkin(int color) {
        getPlayer().setSkinColor((byte) color);
        getPlayer().updateSingleStat(MapleStat.SKIN, color);
        getPlayer().equipChanged();
    }

    public int setRandomAvatar(int ticket, int... args_all) {
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        int args = args_all[Randomizer.nextInt(args_all.length)];
        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public int setAvatar(int ticket, int args) {
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public void sendStorage() {
        c.getPlayer().setConversation(4);
        c.getPlayer().getStorage().sendStorage(c, npc);
    }

    public void openShop(int id) {
        MapleShopFactory.getInstance().getShop(id).sendShop(c);
    }

    public int gainGachaponItem(int id, int quantity) {
        return gainGachaponItem(id, quantity, c.getPlayer().getMap().getStreetName() + " - " + c.getPlayer().getMap().getMapName(), false);
    }

    public int gainGachaponItem(int id, int quantity, final boolean broad) {
        return gainGachaponItem(id, quantity, c.getPlayer().getMap().getStreetName() + " - " + c.getPlayer().getMap().getMapName(), broad);
    }

    public int Scroll() {
        int[] Scroll = {2040530, 2040512, 2040515, 2040530, 2041009, 2044010, 2040000, 2040003, 2040018, 2040024, 2040027, 2040102, 2040107, 2040202, 2040207, 2040300, 2040312, 2040316, 2040319, 2040324, 2040400, 2040414, 2040417, 2040420, 2040423, 2040500, 2040503, 2040600, 2040614, 2040617, 2040620, 2040623, 2040700, 2040703, 2040706, 2040800, 2040803, 2040818, 2040823, 2040900, 2040913, 2040918, 2040923, 2040926, 2040929, 2041000, 2041003, 2041006, 2041012, 2041015, 2041018, 2041021, 2041100, 2041103, 2041106, 2041109, 2041130, 2041131, 2041203, 2041208, 2041300, 2041303, 2041306, 2041309, 2043000, 2043010, 2043015, 2043100, 2043110, 2043200, 2043210, 2043300, 2043400, 2043700, 2043800, 2044000, 2044100, 2044110, 2044200, 2044210, 2044300, 2044310, 2044400, 2044410, 2044500, 2044600, 2044700, 2044800, 2044805, 2044900, 2048000, 2048003};
        int PetRandom = (int) Math.floor(Math.random() * Scroll.length);//亂碼變數
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (!ii.itemExists(Scroll[PetRandom])) {
            playerMessage(5, "[物品回報]你的客戶端內無此物品 ID:" + Scroll[PetRandom]);
            return -2;
        }
        if (!MapleItemInformationProvider.getInstance().itemExists(Scroll[PetRandom])) {
            playerMessage(5, "[物品回報]裝備出錯");
            return -2;
        }
        return Scroll[PetRandom];
    }

    public void text(String text) {
        //System.out.println(text);
        //FileoutputUtil.log(FileoutputUtil.其他轉蛋, text);
        //playerMessage(5, ProductID.drop(200));
    }

    public void Jobtext(int text) {
        final MapleData item１;
        item１ = MapleItemInformationProvider.getInstance().getItemData1(text);
        final MapleData info１ = item１.getChildByPath("info");
        FileoutputUtil.log(FileoutputUtil.轉蛋機 + "else", text + "    " + MapleDataTool.getInt("reqJob", info１, 0));
    }

    public int gainGachaponItem(int id, int quantity, final boolean broad, int doge, String mgs) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final String tide = FileoutputUtil.轉蛋機 + "潮流轉蛋.ini";
        final String job = FileoutputUtil.轉蛋機 + "職業轉蛋.ini";
        final String other = FileoutputUtil.轉蛋機 + "其他轉蛋.ini";
        final String id_Name = id + " [" + ii.getName(id) + "]";
        String news = "一般";
        if (!"".equals(mgs)) {
            news = mgs;
        }

        if (doge == 1) {
            FileoutputUtil.log(tide, FileoutputUtil.output_text(c, c.getPlayer(), "[" + news + "]抽到了:" + id_Name));
        } else if (doge == 2) {
            FileoutputUtil.log(job, FileoutputUtil.output_text(c, c.getPlayer(), "[" + news + "]抽到了:" + id_Name));
        } else {
            FileoutputUtil.log(other, FileoutputUtil.output_text(c, c.getPlayer(), "[" + news + "]抽到了:" + id_Name));
        }

        if (broad) {
            return gainGachaponItem(id, quantity, mgs + "：" + c.getPlayer().getMap().getStreetName() + " - " + c.getPlayer().getMap().getMapName(), broad);
        } else {
            return gainGachaponItem(id, quantity, mgs, broad);
        }
    }

    public int gainGachaponItem(int id, int quantity, final String msg, final boolean broad) {
        try {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            if (!ii.itemExists(id)) {
                playerMessage(5, "[物品回報]你的客戶端內無此物品 ID:" + id);
                return -1;
            }
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                playerMessage(5, "[物品回報]裝備出錯");
                return -1;
            }
            final IItem item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                playerMessage(5, "[物品回報] 背包已滿或者專屬道具重複。");
                return -1;
            }
            final byte rareness = GameConstants.gachaponRareItem(item.getItemId());
            if (rareness > 0 || broad) {
                if (c.getPlayer().isGM()) {
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.getGachaponMega("恭喜 " + c.getPlayer().getName(), " : 被他從<" + msg.split("：")[0] + ">轉到了，大家快恭喜她吧！", item, rareness, c.getChannel() - 1));
                } else {
                    World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("恭喜 " + c.getPlayer().getName(), " : 被他從<" + msg.split("：")[0] + ">轉到了，大家快恭喜她吧！", item, rareness, c.getChannel() - 1));
                }
            }
            if (!broad && msg != "") {
                if (c.getPlayer().isGM()) {
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.itemMegaphone(c.getPlayer().getName() + " : 從 <" + msg.split("：")[0] + "> 中獲得", true, c.getChannel(), item));
                } else {
                    World.Broadcast.broadcastMessage(MaplePacketCreator.itemMegaphone(c.getPlayer().getName() + " : 從 <" + msg.split("：")[0] + "> 中獲得", true, c.getChannel(), item));
                }
            }
            final String tide = FileoutputUtil.轉蛋機 + "onlyid/" + c.getPlayer().getName() + ".ini";
            final String id_Name = id + " [" + ii.getName(id) + "]";
            FileoutputUtil.log(tide, FileoutputUtil.output_text(c, c.getPlayer(), "抽到了:" + id_Name, "唯一ID:" + item.getEquipOnlyId()));
            return item.getItemId();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[物品回報] error");
        }
        playerMessage(5, "[物品回報] end");
        System.out.println("[物品回報] end");
        return -1;
    }

    public void changeJob(int job) {
        c.getPlayer().changeJob(job);
    }

    public void startQuest(int id) {
        MapleQuest.getInstance(id).start(getPlayer(), npc);
    }

    public void completeQuest(int id) {
        MapleQuest.getInstance(id).complete(getPlayer(), npc);
    }

    public void forfeitQuest(int id) {
        MapleQuest.getInstance(id).forfeit(getPlayer());
    }

    public void forceStartQuest() {
        MapleQuest.getInstance(questid).forceStart(getPlayer(), getNpc(), null);
    }

    public void forceStartQuest(int id) {
        MapleQuest.getInstance(id).forceStart(getPlayer(), getNpc(), null);
    }

    public void forceStartQuest(String customData) {
        MapleQuest.getInstance(questid).forceStart(getPlayer(), getNpc(), customData);
    }

    public void forceCompleteQuest() {
        MapleQuest.getInstance(questid).forceComplete(getPlayer(), getNpc());
    }

    public void forceCompleteQuest(final int id) {
        MapleQuest.getInstance(id).forceComplete(getPlayer(), getNpc());
    }

    public String getQuestCustomData() {
        return c.getPlayer().getQuestNAdd(MapleQuest.getInstance(questid)).getCustomData();
    }

    public void setQuestCustomData(String customData) {
        getPlayer().getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(customData);
    }

    public int getMeso() {
        return getPlayer().getMeso();
    }

    public void gainAp(final int amount) {
        c.getPlayer().gainAp((short) amount);
    }

    public void expandInventory(byte type, int amt) {
        c.getPlayer().expandInventory(type, amt);
    }

    public void unequipEverything() {
        MapleInventory equipped = getPlayer().getInventory(MapleInventoryType.EQUIPPED);
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<Short> ids = new LinkedList<Short>();
        for (IItem item : equipped.list()) {
            ids.add(item.getPosition());
        }
        for (short id : ids) {
            MapleInventoryManipulator.unequip(getC(), id, equip.getNextFreeSlot());
        }
    }

    public final void clearSkills() {
        Map<ISkill, SkillEntry> skills = getPlayer().getSkills();
        for (Entry<ISkill, SkillEntry> skill : skills.entrySet()) {
            getPlayer().changeSkillLevel(skill.getKey(), (byte) 0, (byte) 0);
        }
    }

    public boolean hasSkill(int skillid) {
        ISkill theSkill = SkillFactory.getSkill(skillid);
        if (theSkill != null) {
            return c.getPlayer().getSkillLevel(theSkill) > 0;
        }
        return false;
    }

    public void showEffect(boolean broadcast, String effect) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.showEffect(effect));
        } else {
            c.sendPacket(MaplePacketCreator.showEffect(effect));
        }
    }

    public void playSound(boolean broadcast, String sound) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.playSound(sound));
        } else {
            c.sendPacket(MaplePacketCreator.playSound(sound));
        }
    }

    public void environmentChange(boolean broadcast, String env) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(env, 2));
        } else {
            c.sendPacket(MaplePacketCreator.environmentChange(env, 2));
        }
    }

    public void updateBuddyCapacity(int capacity) {
        c.getPlayer().setBuddyCapacity((byte) capacity);
    }

    public int getBuddyCapacity() {
        return c.getPlayer().getBuddyCapacity();
    }

    public int partyMembersInMap() {
        int inMap = 0;
        for (MapleCharacter char2 : getPlayer().getMap().getCharactersThreadsafe()) {
            if (char2.getParty() == getPlayer().getParty()) {
                inMap++;
            }
        }
        return inMap;
    }

    public List<MapleCharacter> getPartyMembers() {
        if (getPlayer().getParty() == null) {
            return null;
        }
        List<MapleCharacter> chars = new LinkedList<MapleCharacter>(); // creates an empty array full of shit..
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                if (ch != null) { // double check <3
                    chars.add(ch);
                }
            }
        }
        return chars;
    }

    public void warpPartyWithExp(int mapId, int exp) {
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
            }
        }
    }

    public void warpPartyWithExpMeso(int mapId, int exp, int meso) {
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
                curChar.gainMeso(meso, true);
            }
        }
    }

    public MapleSquad getSquad(String type) {
        return c.getChannelServer().getMapleSquad(type);
    }

    public int getSquadAvailability(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        }
        return squad.getStatus();
    }

    public boolean registerSquad(String type, int minutes, String startText) {
        if (c.getChannelServer().getMapleSquad(type) == null) {
            final MapleSquad squad = new MapleSquad(c.getChannel(), type, c.getPlayer(), minutes * 60 * 1000, startText);
            final boolean ret = c.getChannelServer().addMapleSquad(squad, type);
            if (ret) {
                final MapleMap map = c.getPlayer().getMap();

                map.broadcastMessage(MaplePacketCreator.getClock(minutes * 60));
                map.broadcastMessage(MaplePacketCreator.serverNotice(6, c.getPlayer().getName() + startText));
            } else {
                squad.clear();
            }
            return ret;
        }
        return false;
    }

    public boolean getSquadList(String type, byte type_) {
//        try {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return false;
        }
        if (type_ == 0 || type_ == 3) { // Normal viewing
            sendNext(squad.getSquadMemberString(type_));
        } else if (type_ == 1) { // Squad Leader banning, Check out banned participant
            sendSimple(squad.getSquadMemberString(type_));
        } else if (type_ == 2) {
            if (squad.getBannedMemberSize() > 0) {
                sendSimple(squad.getSquadMemberString(type_));
            } else {
                sendNext(squad.getSquadMemberString(type_));
            }
        }
        return true;
        /*        } catch (NullPointerException ex) {
         FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
         return false;
         }*/
    }

    public byte isSquadLeader(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else {
            if (squad.getLeader() != null && squad.getLeader().getId() == c.getPlayer().getId()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public boolean reAdd(String eim, String squad) {
        EventInstanceManager eimz = getDisconnected(eim);
        MapleSquad squadz = getSquad(squad);
        if (eimz != null && squadz != null) {
            squadz.reAddMember(getPlayer());
            eimz.registerPlayer(getPlayer());
            return true;
        }
        return false;
    }

    public void banMember(String type, int pos) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.banMember(pos);
        }
    }

    public void acceptMember(String type, int pos) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.acceptMember(pos);
        }
    }

    public String getReadableMillis(long startMillis, long endMillis) {
        return StringUtil.getReadableMillis(startMillis, endMillis);
    }

    public int addMember(String type, boolean join) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            return squad.addMember(c.getPlayer(), join);
        }
        return -1;
    }

    public byte isSquadMember(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else {
            if (squad.getMembers().contains(c.getPlayer())) {
                return 1;
            } else if (squad.isBanned(c.getPlayer())) {
                return 2;
            } else {
                return 0;
            }
        }
    }

    public void resetReactors() {
        getPlayer().getMap().resetReactors();
    }

    public void genericGuildMessage(int code) {
        c.sendPacket(MaplePacketCreator.genericGuildMessage((byte) code));
    }

    public void disbandGuild() {
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0 || c.getPlayer().getGuildRank() != 1) {
            return;
        }
        World.Guild.disbandGuild(gid);
    }

    public void increaseGuildCapacity(boolean UseGP) {
        if (!UseGP) {
            if (c.getPlayer().getMeso() < 250000) {
                c.sendPacket(MaplePacketCreator.serverNotice(1, "你沒有足夠的錢"));
                return;
            }
            if (c.getPlayer().getGuild().getCapacity() >= 100) {
                c.sendPacket(MaplePacketCreator.serverNotice(1, "公會人數已經擴充超過100"));
                return;
            }
            final int gid = c.getPlayer().getGuildId();
            if (gid <= 0) {
                return;
            }
            World.Guild.increaseGuildCapacity(gid);
            c.getPlayer().gainMeso(-250000, true, false, true);
        } else {
            if (c.getPlayer().getGuild().getGP() < 2500) {
                c.sendPacket(MaplePacketCreator.serverNotice(1, "公會的GP不足."));
                return;
            }
            final int gid = c.getPlayer().getGuildId();
            if (gid <= 0) {
                return;
            }
            World.Guild.increaseGuildCapacity(gid);
            c.getPlayer().getGuild().gainGP(-2500);
        }
    }

    public void displayGuildRanks() {
        c.sendPacket(MaplePacketCreator.showGuildRanks(npc, MapleGuildRanking.getInstance().getRank()));
    }

    public boolean removePlayerFromInstance() {
        if (c.getPlayer().getEventInstance() != null) {
            c.getPlayer().getEventInstance().removePlayer(c.getPlayer());
            return true;
        }
        return false;
    }

    public boolean isPlayerInstance() {
        if (c.getPlayer().getEventInstance() != null) {
            return true;
        }
        return false;
    }

    public void changeStat(byte slot, int type, short amount) {
        Equip sel = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot);
        switch (type) {
            case 0:
                sel.setStr(amount);
                break;
            case 1:
                sel.setDex(amount);
                break;
            case 2:
                sel.setInt(amount);
                break;
            case 3:
                sel.setLuk(amount);
                break;
            case 4:
                sel.setHp(amount);
                break;
            case 5:
                sel.setMp(amount);
                break;
            case 6:
                sel.setWatk(amount);
                break;
            case 7:
                sel.setMatk(amount);
                break;
            case 8:
                sel.setWdef(amount);
                break;
            case 9:
                sel.setMdef(amount);
                break;
            case 10:
                sel.setAcc(amount);
                break;
            case 11:
                sel.setAvoid(amount);
                break;
            case 12:
                sel.setHands(amount);
                break;
            case 13:
                sel.setSpeed(amount);
                break;
            case 14:
                sel.setJump(amount);
                break;
            case 15:
                sel.setUpgradeSlots((byte) amount);
                break;
            case 16:
                sel.setViciousHammer((byte) amount);
                break;
            case 17:
                sel.setLevel((byte) amount);
                break;
            case 18:
                sel.setEnhance((byte) amount);
                break;
            case 19:
                sel.setPotential1(amount);
                break;
            case 20:
                sel.setPotential2(amount);
                break;
            case 21:
                sel.setPotential3(amount);
                break;
            case 22:
                sel.setOwner(getText());
                break;
            default:
                break;
        }
        c.getPlayer().equipChanged();
    }

    public void killAllMonsters() {
        MapleMap map = c.getPlayer().getMap();
        double range = Double.POSITIVE_INFINITY;
        MapleMonster mob;
        for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
            mob = (MapleMonster) monstermo;
            if (mob.getStats().isBoss()) {
                map.killMonster(mob, c.getPlayer(), false, false, (byte) 1);
            }
        }
        /*int mapid = c.getPlayer().getMapId();
         MapleMap map = c.getChannelServer().getMapFactory().getMap(mapid);
         map.killAllMonsters(true); // No drop. */
    }

    public void giveMerchantMesos() {
        long mesos = 0;
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM hiredmerchants WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
            } else {
                mesos = rs.getLong("mesos");
            }
            rs.close();
            ps.close();

            ps = (PreparedStatement) con.prepareStatement("UPDATE hiredmerchants SET mesos = 0 WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            System.err.println("Error gaining mesos in hired merchant" + ex);
        }
        c.getPlayer().gainMeso((int) mesos, true);
    }

    public void dc() {
        MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(c.getPlayer().getName().toString());
        victim.getClient().getSession().close();
        victim.getClient().disconnect(true, false);
        System.err.println("\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
        FileoutputUtil.log("logs/data/DC.txt", "\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
    }

    public long getMerchantMesos() {
        long mesos = 0;
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM hiredmerchants WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
            } else {
                mesos = rs.getLong("mesos");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("Error gaining mesos in hired merchant" + ex);
        }
        return mesos;
    }

    public void openDuey() {
        c.getPlayer().setConversation(2);
        c.sendPacket(MaplePacketCreator.sendDuey((byte) 9, null));
    }

    public void openMerchantItemStore() {
        c.getPlayer().setConversation(3);
        c.sendPacket(PlayerShopPacket.merchItemStore((byte) 0x22));
    }

    public void openFishingItemStore() {
        c.getPlayer().setConversation(6);
        HiredFishingHandler.OpenFishingItemStore(c);
    }

    public void sendRepairWindow() {
        c.sendPacket(MaplePacketCreator.sendRepairWindow(npc));
    }

    public final int getDojoPoints() {
        return c.getPlayer().getDojo();
    }

    public final int getDojoRecord() {
        return c.getPlayer().getDojoRecord();
    }

    public void setDojoRecord(final boolean reset) {
        c.getPlayer().setDojoRecord(reset);
    }

    public boolean start_DojoAgent(final boolean dojo, final boolean party) {
        if (dojo) {
            return Event_DojoAgent.warpStartDojo(c.getPlayer(), party);
        }
        return Event_DojoAgent.warpStartAgent(c.getPlayer(), party);
    }

    public boolean start_PyramidSubway(final int pyramid) {
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpStartPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpStartSubway(c.getPlayer());
    }

    public boolean bonus_PyramidSubway(final int pyramid) {
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpBonusPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpBonusSubway(c.getPlayer());
    }

    public final short getKegs() {
        return AramiaFireWorks.getInstance().getKegsPercentage();
    }

    public void giveKegs(final int kegs) {
        AramiaFireWorks.getInstance().giveKegs(c.getPlayer(), kegs);
    }

    public final short getSunshines() {
        return AramiaFireWorks.getInstance().getSunsPercentage();
    }

    public void addSunshines(final int kegs) {
        AramiaFireWorks.getInstance().giveSuns(c.getPlayer(), kegs);
    }

    public final short getDecorations() {
        return AramiaFireWorks.getInstance().getDecsPercentage();
    }

    public void addDecorations(final int kegs) {
        try {
            AramiaFireWorks.getInstance().giveDecs(c.getPlayer(), kegs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final MapleInventory getInventory(int type) {
        return c.getPlayer().getInventory(MapleInventoryType.getByType((byte) type));
    }

    public final MapleCarnivalParty getCarnivalParty() {
        return c.getPlayer().getCarnivalParty();
    }

    public final MapleCarnivalChallenge getNextCarnivalRequest() {
        return c.getPlayer().getNextCarnivalRequest();
    }

    public final MapleCarnivalChallenge getCarnivalChallenge(MapleCharacter chr) {
        return new MapleCarnivalChallenge(chr);
    }

    public void maxStats() {
        List<Pair<MapleStat, Integer>> statup = new ArrayList<Pair<MapleStat, Integer>>(2);
        c.getPlayer().getStat().setStr((short) 32767);
        c.getPlayer().getStat().setDex((short) 32767);
        c.getPlayer().getStat().setInt((short) 32767);
        c.getPlayer().getStat().setLuk((short) 32767);

        c.getPlayer().getStat().setMaxHp((short) 30000);
        c.getPlayer().getStat().setMaxMp((short) 30000);
        c.getPlayer().getStat().setHp((short) 30000);
        c.getPlayer().getStat().setMp((short) 30000);

        statup.add(new Pair<MapleStat, Integer>(MapleStat.STR, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.DEX, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.LUK, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.INT, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.HP, Integer.valueOf(30000)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, Integer.valueOf(30000)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MP, Integer.valueOf(30000)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, Integer.valueOf(30000)));

        c.sendPacket(MaplePacketCreator.updatePlayerStats(statup, c.getPlayer().getJob()));
    }

    public Pair<String, Map<Integer, String>> getSpeedRun(String typ) {
        final SpeedRunType type = SpeedRunType.valueOf(typ);
        if (SpeedRunner.getInstance().getSpeedRunData(type) != null) {
            return SpeedRunner.getInstance().getSpeedRunData(type);
        }
        return new Pair<String, Map<Integer, String>>("", new HashMap<Integer, String>());
    }

    public boolean getSR(Pair<String, Map<Integer, String>> ma, int sel) {
        if (ma.getRight().get(sel) == null || ma.getRight().get(sel).length() <= 0) {
            dispose();
            return false;
        }
        sendOk(ma.getRight().get(sel));
        return true;
    }

    public Equip getEquip(int itemid) {
        return (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemid);
    }

    public void setExpiration(Object statsSel, long expire) {
        if (statsSel instanceof Equip) {
            ((Equip) statsSel).setExpiration(System.currentTimeMillis() + (expire * 24 * 60 * 60 * 1000));
        }
    }

    public void setLock(Object statsSel) {
        if (statsSel instanceof Equip) {
            Equip eq = (Equip) statsSel;
            if (eq.getExpiration() == -1) {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
            } else {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADEABLE.getValue()));
            }
        }
    }

    public boolean addFromDrop(Object statsSel) {
        if (statsSel instanceof IItem) {
            final IItem it = (IItem) statsSel;
            return MapleInventoryManipulator.checkSpace(getClient(), it.getItemId(), it.getQuantity(), it.getOwner()) && MapleInventoryManipulator.addFromDrop(getClient(), it, false);
        }
        return false;
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type) {
        return replaceItem(slot, invType, statsSel, offset, type, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type, boolean takeSlot) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        IItem item = getPlayer().getInventory(inv).getItem((byte) slot);
        if (item == null || statsSel instanceof IItem) {
            item = (IItem) statsSel;
        }
        if (offset > 0) {
            if (inv != MapleInventoryType.EQUIP) {
                return false;
            }
            Equip eq = (Equip) item;
            if (takeSlot) {
                if (eq.getUpgradeSlots() < 1) {
                    return false;
                } else {
                    eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() - 1));
                }
            }
            if (type.equalsIgnoreCase("Slots")) {
                eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() + offset));
            } else if (type.equalsIgnoreCase("Level")) {
                eq.setLevel((byte) (eq.getLevel() + offset));
            } else if (type.equalsIgnoreCase("Hammer")) {
                eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
            } else if (type.equalsIgnoreCase("STR")) {
                eq.setStr((short) (eq.getStr() + offset));
            } else if (type.equalsIgnoreCase("DEX")) {
                eq.setDex((short) (eq.getDex() + offset));
            } else if (type.equalsIgnoreCase("INT")) {
                eq.setInt((short) (eq.getInt() + offset));
            } else if (type.equalsIgnoreCase("LUK")) {
                eq.setLuk((short) (eq.getLuk() + offset));
            } else if (type.equalsIgnoreCase("HP")) {
                eq.setHp((short) (eq.getHp() + offset));
            } else if (type.equalsIgnoreCase("MP")) {
                eq.setMp((short) (eq.getMp() + offset));
            } else if (type.equalsIgnoreCase("WATK")) {
                eq.setWatk((short) (eq.getWatk() + offset));
            } else if (type.equalsIgnoreCase("MATK")) {
                eq.setMatk((short) (eq.getMatk() + offset));
            } else if (type.equalsIgnoreCase("WDEF")) {
                eq.setWdef((short) (eq.getWdef() + offset));
            } else if (type.equalsIgnoreCase("MDEF")) {
                eq.setMdef((short) (eq.getMdef() + offset));
            } else if (type.equalsIgnoreCase("ACC")) {
                eq.setAcc((short) (eq.getAcc() + offset));
            } else if (type.equalsIgnoreCase("Avoid")) {
                eq.setAvoid((short) (eq.getAvoid() + offset));
            } else if (type.equalsIgnoreCase("Hands")) {
                eq.setHands((short) (eq.getHands() + offset));
            } else if (type.equalsIgnoreCase("Speed")) {
                eq.setSpeed((short) (eq.getSpeed() + offset));
            } else if (type.equalsIgnoreCase("Jump")) {
                eq.setJump((short) (eq.getJump() + offset));
            } else if (type.equalsIgnoreCase("ItemEXP")) {
                eq.setItemEXP(eq.getItemEXP() + offset);
            } else if (type.equalsIgnoreCase("Expiration")) {
                eq.setExpiration((long) (eq.getExpiration() + offset));
            } else if (type.equalsIgnoreCase("Flag")) {
                eq.setFlag((byte) (eq.getFlag() + offset));
            }
            if (eq.getExpiration() == -1) {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
            } else {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADEABLE.getValue()));
            }
            item = eq.copy();
        }
        MapleInventoryManipulator.removeFromSlot(getClient(), inv, (short) slot, item.getQuantity(), false);
        return MapleInventoryManipulator.addFromDrop(getClient(), item, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int upgradeSlots) {
        return replaceItem(slot, invType, statsSel, upgradeSlots, "Slots");
    }

    public boolean isCash(final int itemId) {
        return MapleItemInformationProvider.getInstance().isCash(itemId);
    }

    public int getReqLevel(int itemId) {
        return MapleItemInformationProvider.getInstance().getReqLevel(itemId);
    }

    public MapleStatEffect getEffect(int buff) {
        return MapleItemInformationProvider.getInstance().getItemEffect(buff);
    }

    public void buffGuild(final int buff, final int duration, final String msg) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.getItemEffect(buff) != null && getPlayer().getGuildId() > 0) {
            final MapleStatEffect mse = ii.getItemEffect(buff);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr.getGuildId() == getPlayer().getGuildId()) {
                        mse.applyTo(chr, chr, true, null, duration);
                        chr.dropMessage(5, "Your guild has gotten a " + msg + " buff.");
                    }
                }
            }
        }
    }

    public boolean createAlliance(String alliancename) {
        MapleParty pt = c.getPlayer().getParty();
        MapleCharacter otherChar = c.getChannelServer().getPlayerStorage().getCharacterById(pt.getMemberByIndex(1).getId());
        if (otherChar == null || otherChar.getId() == c.getPlayer().getId()) {
            return false;
        }
        try {
            return World.Alliance.createAlliance(alliancename, c.getPlayer().getId(), otherChar.getId(), c.getPlayer().getGuildId(), otherChar.getGuildId());
        } catch (Exception re) {
            re.printStackTrace();
            return false;
        }
    }

    public boolean addCapacityToAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.changeAllianceCapacity(gs.getAllianceId())) {
                    gainMeso(-MapleGuildAlliance.CHANGE_CAPACITY_COST);
                    return true;
                }
            }
        } catch (Exception re) {
            re.printStackTrace();
        }
        return false;
    }

    public boolean disbandAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.disbandAlliance(gs.getAllianceId())) {
                    return true;
                }
            }
        } catch (Exception re) {
            re.printStackTrace();
        }
        return false;
    }

    public byte getLastMsg() {
        return lastMsg;
    }

    public final void setLastMsg(final byte last) {
        this.lastMsg = last;
    }

    public int getBossLog(String bossid) {
        return getPlayer().getBossLog(bossid);
    }

    public void setBossLog(String bossid) {
        getPlayer().setBossLog(bossid);
    }

    //日誌
    public int getLog(String bossid, boolean acc) {
        return Log.getLog(bossid, c, acc);
    }

    public void setLog(String bossid) {
        Log.setLog(bossid, c, null);
    }

    public void setLog(String bossid, String UPDATE) {
        Log.setLog(bossid, c, UPDATE);
    }

    public final void maxAllSkills() {
        for (ISkill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId())) { //no db/additionals/resistance skills
                teachSkill(skil.getId(), skil.getMaxLevel(), skil.getMaxLevel());
            }
        }
    }

    public final void resetStats(int str, int dex, int z, int luk) {
        c.getPlayer().resetStats(str, dex, z, luk);
    }

    public final boolean dropItem(int slot, int invType, int quantity) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        return MapleInventoryManipulator.drop(c, inv, (short) slot, (short) quantity, true);
    }

    public final List<Integer> getAllPotentialInfo() {
        return new ArrayList<Integer>(MapleItemInformationProvider.getInstance().getAllPotentialInfo().keySet());
    }

    public final String getPotentialInfo(final int id) {
        final List<StructPotentialItem> potInfo = MapleItemInformationProvider.getInstance().getPotentialInfo(id);
        final StringBuilder builder = new StringBuilder("#b#ePOTENTIAL INFO FOR ID: ");
        builder.append(id);
        builder.append("#n#k\r\n\r\n");
        int minLevel = 1, maxLevel = 10;
        for (StructPotentialItem item : potInfo) {
            builder.append("#eLevels ");
            builder.append(minLevel);
            builder.append("~");
            builder.append(maxLevel);
            builder.append(": #n");
            builder.append(item.toString());
            minLevel += 10;
            maxLevel += 10;
            builder.append("\r\n");
        }
        return builder.toString();
    }

    public final void sendRPS() {
        c.sendPacket(MaplePacketCreator.getRPSMode((byte) 8, -1, -1, -1));
    }

    public final void setQuestRecord(Object ch, final int questid, final String data) {
        ((MapleCharacter) ch).getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(data);
    }

    public final void doWeddingEffect(final Object ch) {
        final MapleCharacter chr = (MapleCharacter) ch;
        getMap().broadcastMessage(MaplePacketCreator.yellowChat(getPlayer().getName() + "，你願意娶 " + chr.getName() + " 作為你的妻子嗎，與她在神聖的婚約中共同生活？無論是疾病或健康、貧窮或富裕、美貌或失色、順利或失意，你都願意愛她、安慰她、尊敬她、保護她？並願意在你們一生之中對她永遠忠心不變？ "));
        CloneTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (chr == null || getPlayer() == null) {
                    warpMap(680000500, 0);
                } else {
                    getMap().broadcastMessage(MaplePacketCreator.yellowChat(chr.getName() + "，你願意嫁 " + getPlayer().getName() + " 作為你的丈夫嗎，與他在神聖的婚約中共同生活？無論是疾病或健康、貧窮或富裕、美貌或失色、順利或失意，你都願意愛他、安慰他、尊敬他、保護他？並願意在你們一生之中對他永遠忠心不變？ "));
                }
            }
        }, 10000);
        CloneTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (chr == null || getPlayer() == null) {
                    if (getPlayer() != null) {
                        setQuestRecord(getPlayer(), 160001, "3");
                        setQuestRecord(getPlayer(), 160002, "0");
                    } else if (chr != null) {
                        setQuestRecord(chr, 160001, "3");
                        setQuestRecord(chr, 160002, "0");
                    }
                    warpMap(680000500, 0);
                } else {
                    setQuestRecord(getPlayer(), 160001, "2");
                    setQuestRecord(chr, 160001, "2");
                    sendNPCText(getPlayer().getName() + " 和 " + chr.getName() + ", 祝福你們在未來的路上永恆不變、幸福美滿!", 9201002);
                    getMap().startExtendedMapEffect("親吻你的新娘， " + getPlayer().getName() + " !", 5120006);
                    if (chr.getGuildId() > 0) {
                        World.Guild.guildPacket(chr.getGuildId(), MaplePacketCreator.sendMarriage(false, chr.getName()));
                    }
                    if (chr.getFamilyId() > 0) {
                        World.Family.familyPacket(chr.getFamilyId(), MaplePacketCreator.sendMarriage(true, chr.getName()), chr.getId());
                    }
                    if (getPlayer().getGuildId() > 0) {
                        World.Guild.guildPacket(getPlayer().getGuildId(), MaplePacketCreator.sendMarriage(false, getPlayer().getName()));
                    }
                    if (getPlayer().getFamilyId() > 0) {
                        World.Family.familyPacket(getPlayer().getFamilyId(), MaplePacketCreator.sendMarriage(true, chr.getName()), getPlayer().getId());
                    }
                }
            }
        }, 20000); //10 sec 10 sec
    }

    public void 開啟小鋼珠(int type) {
        c.sendPacket(MaplePacketCreator.openBeans(getPlayer().getBeans(), type));
    }

    public void worldMessage(String text) {
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, text));
    }

    public void warpBack(int mid, final int retmap, final int time) { //時間秒數

        MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(mid);
        c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
        c.sendPacket(MaplePacketCreator.getClock(time));
        Timer.EventTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(retmap);
                if (c.getPlayer() != null) {
                    c.sendPacket(MaplePacketCreator.stopClock());
                    c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
                    c.getPlayer().dropMessage(6, "已經到達目的地了!");
                }
            }
        }, 1000 * time); //設定時間, (1 秒 = 1000)
    }

    public int getBeans() {
        return getClient().getPlayer().getBeans();
    }

    public void gainBeans(int s) {
        getPlayer().gainBeans(s);
        c.sendPacket(MaplePacketCreator.updateBeans(c.getPlayer().getId(), s));
    }

    public String checkDrop_err(int mobId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        List<MonsterDropEntry> ranks = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobId);
        if (!ranks.isEmpty() && ranks.size() > 0) {
            int num = 0, itemId, chance;
            MonsterDropEntry de;
            StringBuilder name = new StringBuilder();
            for (MonsterDropEntry rank : ranks) {
                de = rank;
                if (de.chance > 0 && (de.questid <= 0 || (de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0))) {
                    itemId = de.itemId;
                    if (itemId == 0) {
                        continue;
                    }
                    if (!ii.itemExists(itemId)) {
                        continue;
                    }
                    if (num == 0) {
                        name.append("當前怪物 #o").append(mobId).append("# 的爆率為:\r\n");
                        name.append("--------------------------------------\r\n");
                        //outputWithLogging(mobId, "-- 怪物ID " + mobId);
                        //outputWithLogging(mobId, "-- 掉寶數量 " + ranks.size());
                        //outputWithLogging(mobId, "INSERT INTO drop_data (`dropperid`, `itemid`, `minimum_quantity`, `maximum_quantity`, `questid`, `chance`) VALUES");
                    }
                    //String lineMarker = (i == ranks.size() ? ";" : ",");
                    //outputWithLogging(mobId, "(" + mobId + ", " + itemId + ", " + de.minimum + ", " + de.maximum + ", " + de.questid + ", " + de.chance + ")" + lineMarker + " -- " + ii.getName(itemId) + ii.getReqLevel(itemId));

                    String namez = "#z" + itemId + "#";
                    if (itemId == 0) { //金幣 物品ID為0就是金幣道具
                        itemId = 4031041; //休咪的錢包 display sack of cash
                        namez = (de.Minimum * getClient().getChannelServer().getMesoRate()) + " - " + (de.Maximum * getClient().getChannelServer().getMesoRate()) + " 的金幣";
                    }
                    chance = de.chance * getClient().getChannelServer().getDropRate();
                    if (getPlayer().isAdmin()) {
                        name.append(num + 1).append(") #v").append(itemId).append("#").append(namez).append(" - ").append(Integer.valueOf(chance >= 999999 ? 1000000 : chance).doubleValue() / 10000.0).append("%的爆率. ").append(de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0 ? ("需要接受任務: " + MapleQuest.getInstance(de.questid).getName()) : "").append("\r\n");
                    } else {
                        name.append(num + 1).append(") #v").append(itemId).append("#").append(namez).append(de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0 ? ("需要接受任務: " + MapleQuest.getInstance(de.questid).getName()) : "").append("\r\n");
                    }
                    num++;
                }
            }
            if (name.length() > 0) {
                return name.toString();
            }
        }
        return "沒有找到這個怪物的爆率數據。";
    }

    public String checkDrop(int mobId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        List<MonsterDropEntry> ranks = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobId);
        if (!ranks.isEmpty() && ranks.size() > 0) {
            int num = 0, itemId, chance;
            MonsterDropEntry de;
            StringBuilder name = new StringBuilder();
            for (MonsterDropEntry rank : ranks) {
                de = rank;
                if (de.chance > 0 && (de.questid <= 0 || (de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0))) {
                    itemId = de.itemId;
                    if (itemId == 0) {
                        continue;
                    } else if (!ii.itemExists(itemId)) {
                        continue;
                    }
                    if (num == 0) {
                        name.append("當前怪物 #o").append(mobId).append("# 的爆率為:\r\n");
                        name.append("--------------------------------------\r\n");
                    }
                    String namez = "";
                    if (itemId == 0) { //金幣 物品ID為0就是金幣道具
                        itemId = 4031041; //休咪的錢包 display sack of cash
                        namez = (de.Minimum * getClient().getChannelServer().getMesoRate()) + " - " + (de.Maximum * getClient().getChannelServer().getMesoRate()) + " 的金幣";
                    } else {
                        namez = "#z" + itemId + "#";
                    }
                    chance = de.chance * getClient().getChannelServer().getDropRate();
                    if (getPlayer().isAdmin()) {
                        name.append(num + 1).append(") #v").append(itemId).append("#").append(namez).append(" - ").append(Integer.valueOf(chance >= 999999 ? 1000000 : chance).doubleValue() / 10000.0).append("%的爆率. ").append(de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0 ? ("需要接受任務: " + MapleQuest.getInstance(de.questid).getName()) : "").append("\r\n");
                    } else {
                        name.append(num + 1).append(") #v").append(itemId).append("#").append(namez).append(de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0 ? ("需要接受任務: " + MapleQuest.getInstance(de.questid).getName()) : "").append("\r\n");
                    }
                    num++;
                }
            }
            if (name.length() > 0) {
                return name.toString();
            }
        }
        return "沒有找到這個怪物的爆率數據。";
    }

    public String checkMapDrop() {
        List<MonsterGlobalDropEntry> ranks = new ArrayList<>(MapleMonsterInformationProvider.getInstance().getGlobalDrop());
        int mapid = getPlayer().getMap().getId();
//        int cashServerRate = getClient().getChannelServer().getCashRate(); //點卷爆率
        //int globalServerRate = getClient().getChannelServer().getDropgRate(); //特殊數據庫道具爆率
        int globalServerRate = 1; //特殊數據庫道具爆率
        if (ranks != null && ranks.size() > 0) {
            int num = 0, itemId, chance;
            MonsterGlobalDropEntry de;
            StringBuilder name = new StringBuilder();
            for (MonsterGlobalDropEntry rank : ranks) {
                de = rank;
                if (de.continent < 0 || (de.continent < 10 && mapid / 100000000 == de.continent) || (de.continent < 100 && mapid / 10000000 == de.continent) || (de.continent < 1000 && mapid / 1000000 == de.continent)) {
                    itemId = de.itemId;
                    if (num == 0) {
                        name.append("當前地圖 #r").append(mapid).append("#k - #m").append(mapid).append("# 的全局爆率為:");
                        name.append("\r\n--------------------------------------\r\n");
                    }
                    String names = "#z" + itemId + "#";
//                    if (itemId == 0 && cashServerRate != 0) {
//                        itemId = 4031041;
//                        names = (de.minimum * cashServerRate) + " - " + (de.maximum * cashServerRate) + " 的抵用卷";
//                    }
                    chance = de.chance * globalServerRate;
                    if (getPlayer().isAdmin()) {
                        name.append(num + 1).append(") #v").append(itemId).append("#").append(names).append(" - ").append(Integer.valueOf(chance >= 999999 ? 1000000 : chance).doubleValue() / 10000.0).append("%的爆率. ").append(de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0 ? ("需要接受任務: " + MapleQuest.getInstance(de.questid).getName()) : "").append("\r\n");
                    } else {
                        name.append(num + 1).append(") #v").append(itemId).append("#").append(names).append(de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0 ? ("需要接受任務: " + MapleQuest.getInstance(de.questid).getName()) : "").append("\r\n");
                    }
                    num++;
                }
            }
            if (name.length() > 0) {
                return name.toString();
            }
        }
        return "當前地圖沒有設置全局爆率。";
    }
        //Guild War setting

    public MapleGuildWar getGuildWar(int id) {
        return getPlayer().getGuildWar(id);
    }

    public String getWGuildWarName(int id) {
        return World.Guild.getGuild(getGuildWar(id).getGuildId()).getName();
    }

    public boolean isLord() {
        return MapleGuildWar.isLord(getPlayer().getGuildId());
    }

    public boolean isLord(int id) {
        return MapleGuildWar.isLord(id, getPlayer().getGuildId());
    }

    public String getWVillage(int id) {
        return getPlayer().getGuildWar(id).getVillage();
    }

    public void setWVillage(int id, String village) {
        getPlayer().getGuildWar(id).setVillage(village);
    }

    public void setWGuildId(int id, int GuildId) {
        getPlayer().getGuildWar(id).setGuildId(GuildId);
    }

    public int getWGuildId(int id) {
        return getPlayer().getGuildWar(id).getGuildId();
    }

    public void WGuildSave(int id) {
        getPlayer().getGuildWar(id).saveToDB();
    }

    public void setWarStatus(int i,boolean type) {
        GameConstants.GuildWar[i] = type;
    }

    public boolean getWarStatus(int i) {
        return GameConstants.GuildWar[i];
    }

    public void addWarList(int id,int guildid, String village) {
        getPlayer().getGuildWar(id).addWar(guildid, village);
    }

    public void deleteWarList(int id,int guildid) {
        getPlayer().getGuildWar(id).deleteWar(guildid);
    }

    public void clearWarList(int id) {
        getPlayer().getGuildWar(id).clearWar();
    }

    public boolean checkWarParty(int id,int Guildid, String village) {
        return getPlayer().getGuildWar(id).checkWarParty(Guildid, village);
    }

    public List getWarList(int id,String village) {
        return getPlayer().getGuildWar(id).getWar(village);
    }

    public int getWarListSize(int id,String village) {
        return getPlayer().getGuildWar(id).getWarSize(village);
    }
    public MapleGuild getGuilds(int id) {
         return World.Guild.getGuild(id);
    }
    public boolean getStartWar(int id){
        return getPlayer().getGuildWar(id).getWarStart();
    }
    //---Guild War setting end
    private GamblingConstants GbConstants = GamblingConstants.getInstance();
    // 活動賭博 -function
    public void addBet(int chrid, int Bet, int money){
        GbConstants.addBet(chrid, Bet, money);
    }
    public void BetClear(){
        GbConstants.BetClear();
    }
    public void getBetPlayer(int number){
        GbConstants.getBetPlayer(number);
    }
    public void BetRandMath(int min, int max, boolean fixed) {
        GbConstants.RandMath(min, max, fixed);
    }
    public void getThisAccumMoney(){
        GbConstants.getThisAccumMoney();
    }
    public void setAction(int action){
        GbConstants.setAction(action);
    }
    public int checkBet(int chrid){
        return GbConstants.CheckBet(chrid);
    }
    public int getAccumMoney(){
        return GbConstants.getAccumMoney();
    }
    public int[][] getCheckBet(){
        return GbConstants.getCheckBet();
    }
    public int[][] getCheckUnBet(){
        return GbConstants.getCheckUnBet();
    }
    public int getAction(){
       return GbConstants.getAction();
    }
    public boolean getBetChr(){
       return GbConstants.getChr(getPlayer().getId());
    }
    public int getChrBetMath(){
        return GbConstants.getChrBetMath(getPlayer().getId());
    }
    public int  getChrBetMoney(){
        return GbConstants.getChrBetMoney(getPlayer().getId());
    }
    
    // 活動賭博 -end
     public int online() {
        int totalOnline = 0;
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            totalOnline += cserv.getConnectedClients();
        }
        return (totalOnline + Randomizer.rand(47, 57));
    }
      public boolean checkSlot(MapleCharacter chr, int[] items) {
        int eq = 0, use = 0, setup = 0, etc = 0, cash = 0;
        List<Integer> useitem = new ArrayList<Integer>();
        for (int item : items) {
            if (!canHold(item, 1)) {
                return false;
            }
            switch (GameConstants.getInventoryType(item)) {
                case EQUIP:
                    eq++;
                    break;
                case USE:
                    useitem.add(item);
                    break;
                case SETUP:
                    setup++;
                    break;
                case ETC:
                    etc++;
                    break;
                case CASH:
                    cash++;
                    break;
            }
        }
        int[] usei = new int[]{2022179, 2450000, 2250001};
        List<Integer> useii = new ArrayList<Integer>();
        for (int i = 0; i < usei.length; i++) {
            if (useitem.contains(usei[i])) {
                if (!useii.contains(usei[i])) {
                    useii.add(usei[i]);
                    use++;
                }
            }
        }
        if (chr.getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < eq || chr.getInventory(MapleInventoryType.USE).getNumFreeSlot() < use || chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() < setup || chr.getInventory(MapleInventoryType.ETC).getNumFreeSlot() < etc || chr.getInventory(MapleInventoryType.CASH).getNumFreeSlot() < cash) {
            return false;
        }
        return true;
    }
}

