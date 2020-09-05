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
package handling.channel.handler;

import Apple.client.MapleJob;
import client.MapleCharacter;
import client.MapleClient;
import handling.channel.ChannelServer;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.World;
import handling.world.expedition.ExpeditionType;
import tools.MaplePacketCreator;
import tools.StringUtil;
import tools.data.LittleEndianAccessor;

public class PartyHandler {

    public static final void DenyPartyRequest(final LittleEndianAccessor slea, final MapleClient c) {
        final int action = slea.readByte();
        final int partyid = slea.readInt();
        if (c.getPlayer().getParty() == null) {
            MapleParty party = World.Party.getParty(partyid);
            if (party != null) {
                if (action == 0x1B) { //accept
                    if (party.getMembers().size() < 6) {
                        World.Party.updateParty(partyid, PartyOperation.JOIN, new MaplePartyCharacter(c.getPlayer()));
                        c.getPlayer().receivePartyMemberHP();
                        c.getPlayer().updatePartyMemberHP();
                    } else {
                        c.sendPacket(MaplePacketCreator.partyStatusMessage(17));
                    }
                } else if (action != 0x16) {
                    final MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterById(party.getLeader().getId());
                    if (cfrom != null) {
                        cfrom.getClient().sendPacket(MaplePacketCreator.partyStatusMessage(23, c.getPlayer().getName()));
                    }
                }
            } else {
                c.getPlayer().dropMessage(5, "該隊伍不存在");
            }
        } else {
            c.getPlayer().dropMessage(5, "你無法重複加入隊伍");
        }

    }

    public static final void PartyOperatopn(final LittleEndianAccessor slea, final MapleClient c) {
        final int operation = slea.readByte();
        MapleParty party = c.getPlayer().getParty();
        MaplePartyCharacter partyplayer = new MaplePartyCharacter(c.getPlayer());

        switch (operation) {
            case 1: // 創建
                if (c.getPlayer().getParty() == null) {
                    party = World.Party.createParty(partyplayer);
                    c.getPlayer().setParty(party);
                    c.sendPacket(MaplePacketCreator.partyCreated(party.getId()));

                } else {
                    if (partyplayer.equals(party.getLeader()) && party.getMembers().size() == 1) { //only one, reupdate
                        c.sendPacket(MaplePacketCreator.partyCreated(party.getId()));
                    } else {
                        if (c.getPlayer().getParty().getId() == 0) {
                            c.getPlayer().setParty(null);
                            c.getPlayer().dropMessage(5, "已經清除卡組隊");
                            break;
                        }
                        c.getPlayer().dropMessage(5, "你無法重複創建隊伍");
                    }
                }
                break;
            case 2: // 退出
                if (party != null) { //are we in a party? o.O"
                    if (partyplayer.equals(party.getLeader())) { // disband
                        World.Party.updateParty(party.getId(), PartyOperation.DISBAND, partyplayer);
                        if (c.getPlayer().getEventInstance() != null) {
                            c.getPlayer().getEventInstance().disbandParty();
                        }
                        if (c.getPlayer().getPyramidSubway() != null) {
                            c.getPlayer().getPyramidSubway().fail(c.getPlayer());
                        }
                    } else {
                        World.Party.updateParty(party.getId(), PartyOperation.LEAVE, partyplayer);
                        if (c.getPlayer().getEventInstance() != null) {
                            c.getPlayer().getEventInstance().leftParty(c.getPlayer());
                        }
                        if (c.getPlayer().getPyramidSubway() != null) {
                            c.getPlayer().getPyramidSubway().fail(c.getPlayer());
                        }
                    }
                    c.getPlayer().setParty(null);
                }
                break;
            case 3: // accept invitation
                final int partyid = slea.readInt();
                if (c.getPlayer().getParty() == null) {
                    party = World.Party.getParty(partyid);
                    if (party != null) {
                        if (party.getMembers().size() < 6) {
                            World.Party.updateParty(party.getId(), PartyOperation.JOIN, partyplayer);
                            c.getPlayer().receivePartyMemberHP();
                            c.getPlayer().updatePartyMemberHP();
                        } else {
                            c.sendPacket(MaplePacketCreator.partyStatusMessage(17));
                        }
                    } else {
                        c.getPlayer().dropMessage(5, "該隊伍不存在");
                    }
                } else {
                    c.getPlayer().dropMessage(5, "你無法重複加入隊伍");
                }
                break;
            case 4: // invite　組隊邀請
                // TODO store pending invitations and check against them
                String theName = slea.readMapleAsciiString();
                final MapleCharacter invited = c.getChannelServer().getPlayerStorage().getCharacterByName(theName);
                if (invited != null) {
                    if (invited.getParty() == null && party != null) {
                        if (party.getMembers().size() < 6) {
                            c.sendPacket(MaplePacketCreator.serverNotice(5, "發出隊伍邀請給'" + invited.getName() + "'"));
                            invited.getClient().sendPacket(MaplePacketCreator.partyInvite(c.getPlayer()));
                        } else {
                            c.sendPacket(MaplePacketCreator.partyStatusMessage(16));//加入組隊.
                        }
                    } else {
                        c.sendPacket(MaplePacketCreator.partyStatusMessage(17));//已經加入其他組.
                    }
                } else {
                    c.sendPacket(MaplePacketCreator.partyStatusMessage(19));//沒顯示文字.
                }
                break;
            case 5: // expel
                if (partyplayer.equals(party.getLeader())) {
                    final MaplePartyCharacter expelled = party.getMemberById(slea.readInt());
                    if (expelled != null) {
                        World.Party.updateParty(party.getId(), PartyOperation.EXPEL, expelled);
                        if (c.getPlayer().getEventInstance() != null) {
                            /*if leader wants to boot someone, then the whole party gets expelled
                             TODO: Find an easier way to get the character behind a MaplePartyCharacter
                             possibly remove just the expellee.*/
                            if (expelled.isOnline()) {
                                c.getPlayer().getEventInstance().disbandParty();
                            }
                        }
                        if (c.getPlayer().getPyramidSubway() != null && expelled.isOnline()) {
                            c.getPlayer().getPyramidSubway().fail(c.getPlayer());
                        }
                    }
                }
                break;
            case 6: // change leader
                if (party != null) {
                    final MaplePartyCharacter newleader = party.getMemberById(slea.readInt());
                    if (newleader != null && partyplayer.equals(party.getLeader())) {
                        World.Party.updateParty(party.getId(), PartyOperation.CHANGE_LEADER, newleader);
                    }
                }
                break;
            default:
                System.out.println("Unhandled Party function." + operation);
                break;
        }
    }

    public static enum PartySearchJob {

        全職業(0x1),
        初心者(0x2),
        狂狼勇士(0x4),
        劍士(0x8),
        十字軍(0x10),
        騎士(0x20),
        龍騎士(0x40),
        聖魂劍士(0x80),
        法師(0x100),
        魔導士_火毒(0x200),
        魔導士_冰雷(0x400),
        祭司(0x800),
        烈焰巫師(0x1000),
        海盜(0x2000),
        格鬥家(0x4000),
        神槍手(0x8000),
        閃雷悍將(0x10000),
        盜賊(0x20000),
        暗殺者(0x40000),
        神偷(0x80000),
        暗夜行者(0x100000),
        弓箭手(0x200000),
        游俠(0x400000),
        狙擊手(0x800000),
        破風使者(0x1000000);

        private int code;

        private PartySearchJob(int code) {
            this.code = code;
        }

        public final boolean check(int mask) {
            return (mask & code) == code;
        }

        public static boolean checkJob(int mask, int job) {
            return 全職業.check(mask)
                    || (初心者.check(mask) && MapleJob.is初心者(job) && !MapleJob.is狂狼勇士(job))
                    || (狂狼勇士.check(mask) && MapleJob.is狂狼勇士(job))
                    || (劍士.check(mask) && MapleJob.is劍士(job) && !MapleJob.is狂狼勇士(job))
                    || (十字軍.check(mask) && MapleJob.is英雄(job))
                    || (騎士.check(mask) && MapleJob.is聖騎士(job))
                    || (騎士.check(mask) && MapleJob.is黑騎士(job))
                    || (聖魂劍士.check(mask) && MapleJob.is聖魂劍士(job))
                    || (法師.check(mask) && MapleJob.is法師(job))
                    || (魔導士_火毒.check(mask) && MapleJob.is大魔導士_火毒(job))
                    || (魔導士_冰雷.check(mask) && MapleJob.is大魔導士_冰雷(job))
                    || (祭司.check(mask) && MapleJob.is主教(job))
                    || (烈焰巫師.check(mask) && MapleJob.is烈焰巫師(job))
                    || (海盜.check(mask) && MapleJob.is海盜(job))
                    || (格鬥家.check(mask) && MapleJob.is拳霸(job))
                    || (神槍手.check(mask) && MapleJob.is槍神(job))
                    || (閃雷悍將.check(mask) && MapleJob.is閃雷悍將(job))
                    || (盜賊.check(mask) && MapleJob.is盜賊(job))
                    || (暗殺者.check(mask) && MapleJob.is夜使者(job))
                    || (神偷.check(mask) && MapleJob.is暗影神偷(job))
                    || (暗夜行者.check(mask) && MapleJob.is暗夜行者(job))
                    || (弓箭手.check(mask) && MapleJob.is弓箭手(job))
                    || (游俠.check(mask) && MapleJob.is箭神(job))
                    || (狙擊手.check(mask) && MapleJob.is神射手(job))
                    || (破風使者.check(mask) && MapleJob.is破風使者(job));
        }
    }

    public static final void PartySearchStart(final LittleEndianAccessor slea, final MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        MapleParty party = chr.getParty();
        if (party == null || party.getLeader().getId() != chr.getId()) {
            chr.dropMessage(1, "您並非隊伍的隊長！");
            return;
        }

        int minLevel = slea.readInt();
        int maxLevel = slea.readInt();
        int memberNum = slea.readInt();
        int jobMask = slea.readInt();

        if (minLevel > maxLevel) {
            chr.dropMessage(1, "搜尋等級範圍的下限高出上限！請重新確認！");
            return;
        }
        if (minLevel < 0) {
            chr.dropMessage(1, "等級異常！");
            return;
        }
        if (maxLevel > 200) {
            chr.dropMessage(1, "目前楓之谷的等級上限為200級！");
            return;
        }
        if (maxLevel - minLevel > 30) {
            chr.dropMessage(1, "等級範圍最多可設定到30級！");
            return;
        }
        if (minLevel > chr.getLevel()) {
            chr.dropMessage(1, "所要搜尋的等級範圍中，必須包含自己的等級。");
            return;
        }
        if (memberNum < 2 || memberNum > 6) {
            chr.dropMessage(1, "隊員最多輸入到2~6人！");
            return;
        }
        if (party.getMembers().size() >= memberNum) {
            chr.dropMessage(1, "隊員已達到" + memberNum + "人以上");
            return;
        }
        if (jobMask == 0) {
            chr.dropMessage(1, "請選擇想要組成隊伍的角色職業！");
            return;
        }

        World.PartySearch.startSearch(chr, minLevel, maxLevel, memberNum, jobMask);
    }

    public static final void PartySearchStop(final MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        World.PartySearch.stopSearch(chr);
    }
    public static final byte // Party actions
            BEGINNER_NO_PARTY = 0x0A, // A beginner can't create a party.
            NOT_IN_PARTY = 0x0D, // You have yet to join a party.
            JOINED_PARTY = 0x10, // You have joined the party.
            ALREADY_JOINED = 0x11, // Already have joined a party.
            PARTY_FULL = 0x12, // The party you're trying to join is already in full capacity.
            INVITE_MSG = 0x16, // You have invited <name> to your party. (Popup)
            NO_EXPEL = 0x1D, // Cannot kick another user in this map | Expel function is not available in this map.
            NOT_SAME_MAP = 0x20, // This can only be given to a party member within the vicinity. | The Party Leader can only be handed over to the party member in the same map.
            FAILED_TO_HAND_OVER = 0x21, // Unable to hand over the leadership post; No party member is currently within the vicinity of the party leader | There is no party member in the same field with party leader for the hand over.
            NOT_SAME_MAP1 = 0x22, // You may only change with the party member that's on the same channel. | You can only hand over to the party member within the same map.
            NO_GM_CREATES = 0x24, // As a GM, you're forbidden from creating a party.
            NON_EXISTANT = 0x25; // Unable to find the character.

    public static void ExpeditionOperation(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null || chr.getMap() == null) {
            return;
        }
        final byte mode = slea.readByte();
        switch (mode) {
            case 0x2B:
                final ExpeditionType et = ExpeditionType.getById(slea.readInt());
                if (chr.getParty() != null || et == null) {
                    chr.dropMessage("已有組隊");
                    //c.getSession().write(MaplePacketCreator.partyStatusMessage(PartyHandler.ALREADY_JOINED));
                    return;
                }
                if (chr.getLevel() > et.maxLevel || chr.getLevel() < et.minLevel) {
                    chr.dropMessage("等級不足");
                    //c.getSession().write(MaplePacketCreator.expeditionStatusMessage(3, chr.getName()));
                    return;
                }

                final MapleParty party = World.Party.createParty(new MaplePartyCharacter(chr), et.exped);
                chr.setParty(party);
                c.sendPacket(MaplePacketCreator.partyCreated(party.getId()));
                c.sendPacket(MaplePacketCreator.showExpedition(World.Party.getExped(party.getExpeditionId()), true, false));

                break;
            default:
                System.out.println("未知的远征队操作 : 0x" + StringUtil.getLeftPaddedStr(Integer.toHexString(mode).toUpperCase(), '0', 2) + " " + slea);
                break;

        }
        if (chr != null) {
            return;
        }/*
        switch (mode) {
            case 0x2D: // Creating
                final ExpeditionType et = ExpeditionType.getById(slea.readInt());
                if (chr.getParty() != null || et == null) {
                    //c.getSession().write(MaplePacketCreator.partyStatusMessage(PartyHandler.ALREADY_JOINED));
                    return;
                }
                if (chr.getLevel() > et.maxLevel || chr.getLevel() < et.minLevel) {
                    // c.getSession().write(MaplePacketCreator.expeditionStatusMessage(3, chr.getName()));
                    return;
                }
                final MapleParty party = World.Party.createParty(new MaplePartyCharacter(chr), et.exped);
                chr.setParty(party);
                c.getSession().write(MaplePacketCreator.partyCreated(party.getId()));
                c.getSession().write(MaplePacketCreator.showExpedition(World.Party.getExped(party.getExpeditionId()), true, false));
                break;
            case 0x2E: // Invite
                final String name = slea.readMapleAsciiString();
                int theCh = World.Find.findChannel(name);
                if (theCh <= 0) {
                    c.getSession().write(MaplePacketCreator.expeditionStatusMessage(0, name));
                    return;
                }
                final MapleCharacter invited = ChannelServer.getInstance(theCh).getPlayerStorage().getCharacterByName(name);
                if (invited == null) {
                    c.getSession().write(MaplePacketCreator.expeditionStatusMessage(0, name));
                    return;
                }
                final MapleParty partyI = chr.getParty();
                if (invited.getParty() != null || partyI == null || partyI.getExpeditionId() <= 0) {
                    c.getSession().write(MaplePacketCreator.expeditionStatusMessage(2, name));
                    return;
                }
                final MapleExpedition me = World.Party.getExped(partyI.getExpeditionId());
                if (me != null && me.getAllMembers() < me.getType().maxMembers && invited.getLevel() <= me.getType().maxLevel && invited.getLevel() >= me.getType().minLevel) {
                    invited.getClient().getSession().write(MaplePacketCreator.expeditionInvite(chr, me.getType().exped));
                } else {
                    c.getSession().write(MaplePacketCreator.expeditionStatusMessage(3, invited.getName()));
                }
                break;
            case 0x2F: // Response
                final String recvName = slea.readMapleAsciiString();
                final int action = slea.readInt(); // 7 = send invite, 8 = accept, 9 = deny

                int theChh = World.Find.findChannel(recvName);
                if (theChh <= 0) {
                    break;
                }
                final MapleCharacter cfrom = ChannelServer.getInstance(theChh).getPlayerStorage().getCharacterByName(recvName);
                if (cfrom == null) {
                    break;
                }
                if (action == 6 || action == 7) {
                    if (cfrom.getParty() == null || cfrom.getParty().getExpeditionId() <= 0) {
                        c.getSession().write(MaplePacketCreator.partyStatusMessage(PartyHandler.NOT_IN_PARTY));
                        return;
                    }
                    if (action == 6) {
                        cfrom.dropMessage(5, "You have already invited '" + chr.getName() + "' to the expedition.");
                    } else {
                        cfrom.getClient().getSession().write(MaplePacketCreator.expeditionStatusMessage(7, chr.getName()));
                    }
                } else if (action == 8 || action == 9) {
                    if (cfrom.getParty() == null || cfrom.getParty().getExpeditionId() <= 0) {
                        if (action == 8) {
                            chr.dropMessage(1, "The expedition you are trying to join does not exist.");
                        }
                        return;
                    }
                    MapleParty partyN = cfrom.getParty();
                    final MapleExpedition exped = World.Party.getExped(partyN.getExpeditionId());
                    if (action == 8) {
                        if (exped == null || chr.getParty() != null) {
                            if (chr.getParty() != null) {
                                cfrom.getClient().getSession().write(MaplePacketCreator.expeditionStatusMessage(2, chr.getName()));
                            }
                            chr.dropMessage(1, "The expedition you are trying to join does not exist.");
                            return;
                        }
                        if (chr.getLevel() <= exped.getType().maxLevel && chr.getLevel() >= exped.getType().minLevel && exped.getAllMembers() < exped.getType().maxMembers) {
                            int partyId = exped.getFreeParty();
                            if (partyId < 0) {
                                c.getSession().write(MaplePacketCreator.partyStatusMessage(PartyHandler.PARTY_FULL));
                            } else if (partyId == 0) {
                                partyN = World.Party.createPartyAndAdd(new MaplePartyCharacter(chr), exped.getId());
                                chr.setParty(partyN);
                                c.getSession().write(MaplePacketCreator.partyCreated(partyN.getId()));
                                c.getSession().write(MaplePacketCreator.showExpedition(exped, false, false));
                                World.Party.expedPacket(exped.getId(), MaplePacketCreator.expeditionNotice(56, chr.getName()), new MaplePartyCharacter(chr));
                                World.Party.expedPacket(exped.getId(), MaplePacketCreator.expeditionUpdate(exped.getIndex(partyN.getId()), partyN), null);
                            } else {
                                chr.setParty(World.Party.getParty(partyId));
                                World.Party.updateParty(partyId, PartyOperation.JOIN, new MaplePartyCharacter(chr));
                                chr.receivePartyMemberHP();
                                chr.updatePartyMemberHP();
                                c.getSession().write(MaplePacketCreator.showExpedition(exped, false, false));
                                World.Party.expedPacket(exped.getId(), MaplePacketCreator.expeditionNotice(56, chr.getName()), new MaplePartyCharacter(chr));
                            }
                        } else {
                            c.getSession().write(MaplePacketCreator.expeditionStatusMessage(3, cfrom.getName()));
                        }
                    } else if (action == 9) {
                        cfrom.dropMessage(5, "'" + chr.getName() + " has declined the expedition invitation.");
                    }
                } else {
                    System.out.println("Unhandled Expedition Operation found: " + slea.toString());
                }
                break;
            case 0x30: // Leave
                final MapleParty part = chr.getParty();
                if (part == null || part.getExpeditionId() <= 0) {
                    c.getSession().write(MaplePacketCreator.partyStatusMessage(PartyHandler.NOT_IN_PARTY));
                    break;
                }
                MapleExpedition exped1 = World.Party.getExped(part.getExpeditionId());
                if (exped1 != null) {
                    if (exped1.getLeader() == chr.getId()) {
                        World.Party.expedPacket(exped1.getId(), MaplePacketCreator.removeExpedition(64), null);
                        World.Party.disbandExped(exped1.getId());
                        if (chr.getEventInstance() != null) {
                            chr.getEventInstance().disbandParty();
                        }
                    } else {
                        if (part.getLeader().getId() == chr.getId()) {
                            World.Party.updateParty(part.getId(), PartyOperation.DISBAND_IN_EXPEDITION, new MaplePartyCharacter(chr));
                            if (chr.getEventInstance() != null) {
                                chr.getEventInstance().disbandParty();
                            }
                            World.Party.expedPacket(exped1.getId(), MaplePacketCreator.showExpedition(exped1, false, true), null);
                        } else {
                            World.Party.updateParty(part.getId(), PartyOperation.LEAVE, new MaplePartyCharacter(chr));
                            if (chr.getEventInstance() != null) {
                                chr.getEventInstance().leftParty(chr);
                            }
                        }
                    }
                    if (chr.getPyramidSubway() != null) {
                        chr.getPyramidSubway().fail(chr);
                    }
                    chr.setParty(null);
                }
                break;
            case 0x31: // Kick
                final MapleParty currentParty = chr.getParty();
                if (currentParty == null || currentParty.getExpeditionId() <= 0) {
                    c.getSession().write(MaplePacketCreator.partyStatusMessage(PartyHandler.NOT_IN_PARTY));
                    break;
                }
                final MapleExpedition currexped = World.Party.getExped(currentParty.getExpeditionId());
                if (currexped != null && currexped.getLeader() == chr.getId()) {
                    final int toKick = slea.readInt();
                    for (Integer i : currexped.getParties()) {
                        final MapleParty partyy = World.Party.getParty(i);
                        if (partyy != null) {
                            MaplePartyCharacter expelled = partyy.getMemberById(toKick);
                            if (expelled != null) {
                                World.Party.updateParty(i, PartyOperation.EXPEL, expelled);
                                if (chr.getEventInstance() != null && expelled.isOnline()) {
                                    chr.getEventInstance().disbandParty();
                                }
                                if (chr.getPyramidSubway() != null && expelled.isOnline()) {
                                    chr.getPyramidSubway().fail(chr);
                                }
                                break;
                            }
                        }
                    }
                }
                break;
            case 0x32: // Change Expedition Captain
                final MapleParty mparty = chr.getParty();
                if (mparty == null || mparty.getExpeditionId() <= 0) {
                    break;
                }
                final MapleExpedition expedd = World.Party.getExped(mparty.getExpeditionId());
                if (expedd != null && expedd.getLeader() == chr.getId()) {
                    final int cid = slea.readInt();
                    final MaplePartyCharacter newleader = mparty.getMemberById(cid);
                    if (newleader != null) {
                        World.Party.updateParty(mparty.getId(), PartyOperation.CHANGE_LEADER, newleader);
                        expedd.setLeader(newleader.getId());
                        World.Party.expedPacket(expedd.getId(), MaplePacketCreator.changeExpeditionLeader(0), null);
                    } else {
                        chr.dropMessage(5, "You only can perform this action when the character is in the same party.");
                    }
                }
                break;
            case 0x33: // Change Party Leader
                final MapleParty mparty1 = chr.getParty();
                if (mparty1 == null || mparty1.getExpeditionId() <= 0) {
                    break;
                }
                MapleExpedition expedit = World.Party.getExped(mparty1.getExpeditionId());
                if (expedit != null && expedit.getLeader() == chr.getId()) {
                    final int toCid = slea.readInt();
                    for (Integer i : expedit.getParties()) {
                        final MapleParty par = World.Party.getParty(i);
                        if (par != null) {
                            MaplePartyCharacter newleader = par.getMemberById(toCid);
                            if (newleader != null && par.getId() != mparty1.getId()) {
                                if (par.getLeader() != newleader) {
                                    World.Party.updateParty(par.getId(), PartyOperation.CHANGE_LEADER, newleader);
                                } else {
                                    chr.dropMessage(5, "You cannot perform this action as the character is already the party leader.");
                                }
                            }
                        }
                    }
                }
                break;
            case 0x34: // Move to new party (got to check this from msea), ask someone mvoe me to new party
                final MapleParty oriPart = chr.getParty();
                if (oriPart == null || oriPart.getExpeditionId() <= 0) {
                    break;
                }
                final MapleExpedition nowExped = World.Party.getExped(oriPart.getExpeditionId());
                if (nowExped == null || nowExped.getLeader() != chr.getId()) {
                    break;
                }
                final int partyIndexTo = slea.readInt();
                if (partyIndexTo < nowExped.getType().maxParty && partyIndexTo <= nowExped.getParties().size()) {
                    final int Tcid = slea.readInt();
                    for (Integer i : nowExped.getParties()) {
                        final MapleParty par = World.Party.getParty(i);
                        if (par == null) {
                            continue;
                        }
                        MaplePartyCharacter expelled = par.getMemberById(Tcid);
                        if (expelled != null && expelled.isOnline()) {
                            final MapleCharacter player = World.getStorage(expelled.getChannel()).getCharacterById(expelled.getId());
                            if (player == null) {
                                break;
                            }
                            if (partyIndexTo < nowExped.getParties().size()) {
                                final MapleParty partyIndex = World.Party.getParty(nowExped.getParties().get(partyIndexTo));
                                if (partyIndex == null || partyIndex.getMembers().size() >= 6) {
                                    chr.dropMessage(5, "You can't move a character to a non-existent party.");
                                    break;
                                }
                            }
                            World.Party.updateParty(i, PartyOperation.MOVE_MEMBER, expelled);
                            if (partyIndexTo < nowExped.getParties().size()) {
                                final MapleParty oldParty = World.Party.getParty(nowExped.getParties().get(partyIndexTo));
                                if (oldParty != null && oldParty.getMembers().size() < 6) {
                                    World.Party.updateParty(oldParty.getId(), PartyOperation.JOIN, expelled);
                                    player.receivePartyMemberHP();
                                    player.updatePartyMemberHP();
                                    player.getClient().getSession().write(MapleUserPackets.showExpedition(nowExped, false, true));
                                }
                            } else { // Moving to a new party
                                final MapleParty newParty = World.Party.createPartyAndAdd(expelled, nowExped.getId());
                                player.setParty(newParty);
                                player.getClient().getSession().write(MaplePacketCreator.partyCreated(newParty.getId()));
                                player.getClient().getSession().write(MaplePacketCreator.showExpedition(nowExped, false, true));
                                World.Party.expedPacket(nowExped.getId(), MaplePacketCreator.expeditionUpdate(nowExped.getIndex(newParty.getId()), newParty), null);
                            }
                            if (chr.getEventInstance() != null && expelled.isOnline()) {
                                chr.getEventInstance().disbandParty();
                            }
                            if (chr.getPyramidSubway() != null) {
                                chr.getPyramidSubway().fail(chr);
                            }
                            break;
                        }
                    }
                }
                break;
                   
        }*/
    }

    /*
     * 远征队伍操作
         *//*
    public static void Expedition(LittleEndianAccessor slea, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null || player.getMap() == null) {
            return;
        }
        int mode = slea.readByte();
        WorldPartyService partyService = WorldPartyService.getInstance();
        MapleParty part, party;
        String name;
        int partySearchId;
        switch (mode) {
            case 0x4E: //创建远征队 create [PartySearchID]
                partySearchId = slea.readInt();
                ExpeditionType et = ExpeditionType.getById(partySearchId);
                if (et != null && player.getParty() == null && player.getLevel() <= et.maxLevel && player.getLevel() >= et.minLevel) {
                    party = partyService.createParty(new MaplePartyCharacter(player), et.exped);
                    player.setParty(party);
                    c.announce(PartyPacket.partyCreated(party));
                    c.announce(PartyPacket.expeditionStatus(partyService.getExped(party.getExpeditionId()), true));
                } else {
                    c.announce(PartyPacket.expeditionInviteMessage(0, "远征模式ID[" + partySearchId + "]"));
                }
                break;
            case 0x4F: //远征邀请 invite [name]
                name = slea.readMapleAsciiString();
                int theCh = WorldFindService.getInstance().findChannel(name);
                if (theCh > 0) {
                    MapleCharacter invited = ChannelServer.getInstance(theCh).getPlayerStorage().getCharacterByName(name);
                    party = c.getPlayer().getParty();
                    if (invited != null && invited.getParty() == null && party != null && party.getExpeditionId() > 0) {
                        MapleExpedition me = partyService.getExped(party.getExpeditionId());
                        if (me != null && me.getAllMembers() < me.getType().maxMembers && invited.getLevel() <= me.getType().maxLevel && invited.getLevel() >= me.getType().minLevel) {
                            c.announce(PartyPacket.expeditionInviteMessage(0x07, invited.getName()));
                            invited.getClient().announce(PartyPacket.expeditionInvite(player, me.getType().exped));
                        } else {
                            c.announce(PartyPacket.expeditionInviteMessage(3, invited.getName())); //‘xxxx’的等级不符，无法邀请加入远征队。
                        }
                    } else {
                        c.announce(PartyPacket.expeditionInviteMessage(2, name)); //‘xxxx’已经加入了其他队伍。
                    }
                } else {
                    c.announce(PartyPacket.expeditionInviteMessage(0, name)); //在当前服务器找不到‘xxxx’。
                }
                break;
            case 0x50: //接受远征邀请 accept invite [name] [int - 7, then int 8? lol.]
                name = slea.readMapleAsciiString();
                slea.readInt(); //partySearchId
                int action = slea.readInt();
                int theChh = WorldFindService.getInstance().findChannel(name);
                if (theChh > 0) {
                    MapleCharacter cfrom = ChannelServer.getInstance(theChh).getPlayerStorage().getCharacterByName(name);
                    if (cfrom != null && cfrom.getParty() != null && cfrom.getParty().getExpeditionId() > 0) {
                        party = cfrom.getParty();
                        MapleExpedition exped = partyService.getExped(party.getExpeditionId());
                        if (exped != null && action == 8) {
                            if (player.getLevel() <= exped.getType().maxLevel && player.getLevel() >= exped.getType().minLevel && exped.getAllMembers() < exped.getType().maxMembers) {
                                int partyId = exped.getFreeParty();
                                if (partyId < 0) {
                                    c.announce(PartyPacket.partyStatusMessage(0x11));
                                } else if (partyId == 0) { //signal to make a new party
                                    party = partyService.createPartyAndAdd(new MaplePartyCharacter(player), exped.getId());
                                    player.setParty(party);
                                    c.announce(PartyPacket.partyCreated(party));
                                    c.announce(PartyPacket.expeditionStatus(exped, true));
                                    partyService.sendExpedPacket(exped.getId(), PartyPacket.expeditionJoined(player.getName()), null);
                                    partyService.sendExpedPacket(exped.getId(), PartyPacket.expeditionUpdate(exped.getIndex(party.getPartyId()), party), null);
                                } else {
                                    player.setParty(partyService.getParty(partyId));
                                    partyService.updateParty(partyId, PartyOperation.加入队伍, new MaplePartyCharacter(player));
                                    player.receivePartyMemberHP();
                                    player.updatePartyMemberHP();
                                    partyService.sendExpedPacket(exped.getId(), PartyPacket.expeditionJoined(player.getName()), null);
                                    c.announce(PartyPacket.expeditionStatus(exped, false));
                                }
                            } else {
                                c.announce(PartyPacket.expeditionInviteMessage(3, cfrom.getName())); //‘xxxx’的等级不符，无法邀请加入远征队
                            }
                        } else if (action == 9) { //拒绝远征队邀请
                            cfrom.dropMessage(5, "'" + player.getName() + "'拒绝了远征队邀请。");
                        }
                    }
                }
                break;
            case 0x51: //离开远征队伍 leaving
                part = player.getParty();
                if (part != null && part.getExpeditionId() > 0) {
                    MapleExpedition exped = partyService.getExped(part.getExpeditionId());
                    if (exped != null) {
                        if (exped.getLeader() == player.getId()) { //解散远征队伍
                            partyService.disbandExped(exped.getId()); //should take care of the rest
                            if (player.getEventInstance() != null) {
                                player.getEventInstance().disbandParty();
                            }
                        } else if (part.getLeader().getId() == player.getId()) {
                            partyService.updateParty(part.getPartyId(), PartyOperation.解散队伍, new MaplePartyCharacter(player));
                            if (player.getEventInstance() != null) {
                                player.getEventInstance().disbandParty();
                            }
                            //发送给还在远征队的队员消息
                            //partyService.sendExpedPacket(exped.getId(), PartyPacket.expeditionLeft(0x4E, player.getName()), null);
                        } else {
                            partyService.updateParty(part.getPartyId(), PartyOperation.离开队伍, new MaplePartyCharacter(player));
                            if (player.getEventInstance() != null) {
                                player.getEventInstance().leftParty(player);
                            }
                            //发送给还在远征队的队员消息
                            partyService.sendExpedPacket(exped.getId(), PartyPacket.expeditionLeft(true, player.getName()), null);
                        }
                        if (player.getPyramidSubway() != null) {
                            player.getPyramidSubway().fail(c.getPlayer());
                        }
                        player.setParty(null);
                    }
                }
                break;
            case 0x52: //远征队伍驱逐 kick [cid]
                part = player.getParty();
                if (part != null && part.getExpeditionId() > 0) {
                    MapleExpedition exped = partyService.getExped(part.getExpeditionId());
                    if (exped != null && exped.getLeader() == player.getId()) {
                        int cid = slea.readInt();
                        for (int i : exped.getParties()) {
                            MapleParty par = partyService.getParty(i);
                            if (par != null) {
                                MaplePartyCharacter expelled = par.getMemberById(cid);
                                if (expelled != null) {
                                    partyService.updateParty(i, PartyOperation.驱逐成员, expelled);
                                    if (player.getEventInstance() != null) {
                                        if (expelled.isOnline()) {
                                            player.getEventInstance().disbandParty();
                                        }
                                    }
                                    if (player.getPyramidSubway() != null && expelled.isOnline()) {
                                        player.getPyramidSubway().fail(player);
                                    }
                                    partyService.sendExpedPacket(exped.getId(), PartyPacket.expeditionLeft(false, expelled.getName()), null);
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case 0x53: //改变远征队长 give exped leader [cid]
                part = player.getParty();
                if (part != null && part.getExpeditionId() > 0) {
                    MapleExpedition exped = partyService.getExped(part.getExpeditionId());
                    if (exped != null && exped.getLeader() == player.getId()) {
                        MaplePartyCharacter newleader = part.getMemberById(slea.readInt());
                        if (newleader != null) {
                            partyService.updateParty(part.getPartyId(), PartyOperation.改变队长, newleader);
                            exped.setLeader(newleader.getId());
                            partyService.sendExpedPacket(exped.getId(), PartyPacket.expeditionLeaderChanged(0), null);
                        }
                    }
                }
                break;
            case 0x54: //改变小组队长 give party leader [cid]
                part = player.getParty();
                if (part != null && part.getExpeditionId() > 0) {
                    MapleExpedition exped = partyService.getExped(part.getExpeditionId());
                    if (exped != null && exped.getLeader() == player.getId()) {
                        int cid = slea.readInt();
                        for (int i : exped.getParties()) {
                            MapleParty par = partyService.getParty(i);
                            if (par != null) {
                                MaplePartyCharacter newleader = par.getMemberById(cid);
                                if (newleader != null && par.getPartyId() != part.getPartyId()) {
                                    partyService.updateParty(par.getPartyId(), PartyOperation.改变队长, newleader);
                                }
                            }
                        }
                    }
                }
                break;
            case 0x55: //change party of diff player [partyIndexTo] [cid]
                part = player.getParty();
                if (part != null && part.getExpeditionId() > 0) {
                    MapleExpedition exped = partyService.getExped(part.getExpeditionId());
                    if (exped != null && exped.getLeader() == player.getId()) {
                        int partyIndexTo = slea.readInt();
                        if (partyIndexTo < exped.getType().maxParty && partyIndexTo <= exped.getParties().size()) {
                            int cid = slea.readInt();
                            for (int i : exped.getParties()) {
                                MapleParty par = partyService.getParty(i);
                                if (par != null) {
                                    MaplePartyCharacter expelled = par.getMemberById(cid);
                                    if (expelled != null && expelled.isOnline()) {
                                        MapleCharacter chr = World.getStorage(expelled.getChannel()).getCharacterById(expelled.getId());
                                        if (chr == null) {
                                            break;
                                        }
                                        if (partyIndexTo < exped.getParties().size()) { //already exists
                                            party = partyService.getParty(exped.getParties().get(partyIndexTo));
                                            if (party == null || party.getMembers().size() >= 6) {
                                                player.dropMessage(5, "Invalid party.");
                                                break;
                                            }
                                        }
                                        partyService.updateParty(i, PartyOperation.驱逐成员, expelled);
                                        if (partyIndexTo < exped.getParties().size()) { //already exists
                                            party = partyService.getParty(exped.getParties().get(partyIndexTo));
                                            if (party != null && party.getMembers().size() < 6) {
                                                partyService.updateParty(party.getPartyId(), PartyOperation.加入队伍, expelled);
                                                chr.receivePartyMemberHP();
                                                chr.updatePartyMemberHP();
                                                chr.send(PartyPacket.expeditionStatus(exped, true));
                                            }
                                        } else {
                                            party = partyService.createPartyAndAdd(expelled, exped.getId());
                                            chr.setParty(party);
                                            chr.send(PartyPacket.partyCreated(party));
                                            chr.send(PartyPacket.expeditionStatus(exped, true));
                                            partyService.sendExpedPacket(exped.getId(), PartyPacket.expeditionUpdate(exped.getIndex(party.getPartyId()), party), null);
                                        }
                                        if (player.getEventInstance() != null) {
                                            if (expelled.isOnline()) {
                                                player.getEventInstance().disbandParty();
                                            }
                                        }
                                        if (player.getPyramidSubway() != null) {
                                            player.getPyramidSubway().fail(c.getPlayer());
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default:
                if (ServerConstants.isShowPacket()) {
                    System.out.println("未知的远征队操作 : 0x" + StringUtil.getLeftPaddedStr(Integer.toHexString(mode).toUpperCase(), '0', 2) + " " + slea);
                }
                break;
        }
    }*/
    }
