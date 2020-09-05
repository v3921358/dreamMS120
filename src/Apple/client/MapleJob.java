package Apple.client;



public enum MapleJob {

    初心者(0),
    劍士(100),
    狂戰士(110),
    十字軍(111),
    英雄(112),
    見習騎士(120),
    騎士(121),
    聖騎士(122),
    槍騎兵(130),
    嗜血狂騎(131),
    黑騎士(132),
    法師(200),
    巫師_火毒(210),
    魔導士_火毒(211),
    大魔導士_火毒(212),
    巫師_冰雷(220),
    魔導士_冰雷(221),
    大魔導士_冰雷(222),
    僧侶(230),
    祭司(231),
    主教(232),
    弓箭手(300),
    獵人(310),
    遊俠(311),
    箭神(312),
    弩弓手(320),
    狙擊手(321),
    神射手(322),
    盜賊(400),
    刺客(410),
    暗殺者(411),
    夜使者(412),
    俠盜(420),
    神偷(421),
    暗影神偷(422),
    海盜(500),
    打手(510),
    格鬥家(511),
    拳霸(512),
    槍手(520),
    神槍手(521),
    槍神(522),
    MANAGER(800),
    管理員(900),
    貴族(1000),
    聖魂劍士1轉(1100),
    聖魂劍士2轉(1110),
    聖魂劍士3轉(1111),
    聖魂劍士4轉(1112),
    烈焰巫師1轉(1200),
    烈焰巫師2轉(1210),
    烈焰巫師3轉(1211),
    烈焰巫師4轉(1212),
    破風使者1轉(1300),
    破風使者2轉(1310),
    破風使者3轉(1311),
    破風使者4轉(1312),
    暗夜行者1轉(1400),
    暗夜行者2轉(1410),
    暗夜行者3轉(1411),
    暗夜行者4轉(1412),
    閃雷悍將1轉(1500),
    閃雷悍將2轉(1510),
    閃雷悍將3轉(1511),
    閃雷悍將4轉(1512),
    傳說(2000),
    狂狼勇士1轉(2100),
    狂狼勇士2轉(2110),
    狂狼勇士3轉(2111),
    狂狼勇士4轉(2112),
    未知(999999),;
    private final int jobid;

    private MapleJob(int id) {
        this.jobid = id;
    }

    public int getId() {
        return this.jobid;
    }

    public static String getName(MapleJob mjob) {
        return mjob.name();
    }

    public static MapleJob getById(int id) {
        for (MapleJob l : values()) {
            if (l.getId() == id) {
                return l;
            }
        }
        return 未知;
    }

    public static boolean isExist(int id) {
        for (MapleJob job : values()) {
            if (job.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public static boolean is冒險家(final int job) {
        return job / 1000 == 0;
    }

    public static boolean is英雄(final int job) {
        return job / 10 == 11;
    }

    public static boolean is聖騎士(final int job) {
        return job / 10 == 12;
    }

    public static boolean is黑騎士(final int job) {
        return job / 10 == 13;
    }

    public static boolean is大魔導士_火毒(final int job) {
        return job / 10 == 21;
    }

    public static boolean is大魔導士_冰雷(final int job) {
        return job / 10 == 22;
    }

    public static boolean is主教(final int job) {
        return job / 10 == 23;
    }

    public static boolean is箭神(final int job) {
        return job / 10 == 31;
    }

    public static boolean is神射手(final int job) {
        return job / 10 == 32;
    }

    public static boolean is夜使者(final int job) {
        return job / 10 == 41;
    }

    public static boolean is暗影神偷(final int job) {
        return job / 10 == 42;
    }

    public static boolean is影武者(final int job) {
        return job / 10 == 43; // sub == 1 && job == 400
    }

    public static boolean is拳霸(final int job) {
        return job / 10 == 51;
    }

    public static boolean is槍神(final int job) {
        return job / 10 == 52;
    }

    public static boolean is管理員(final int job) {
        return job == 800 || job == 900 || job == 910;
    }

    public static boolean is皇家騎士團(final int job) {
        return job / 1000 == 1;
    }

    public static boolean is聖魂劍士(final int job) {
        return job / 100 == 11;
    }

    public static boolean is烈焰巫師(final int job) {
        return job / 100 == 12;
    }

    public static boolean is破風使者(final int job) {
        return job / 100 == 13;
    }

    public static boolean is暗夜行者(final int job) {
        return job / 100 == 14;
    }

    public static boolean is閃雷悍將(final int job) {
        return job / 100 == 15;
    }

    public static boolean is英雄團(final int job) {
        return job / 1000 == 2;
    }

    public static boolean is狂狼勇士(final int job) {
        return job / 100 == 21 || job == 2000;
    }

    public static boolean is劍士(final int job) {
        return getJobBranch(job) == 1;
    }

    public static boolean is法師(final int job) {
        return getJobBranch(job) == 2;
    }

    public static boolean is弓箭手(final int job) {
        return getJobBranch(job) == 3;
    }

    public static boolean is盜賊(final int job) {
        return getJobBranch(job) == 4 || getJobBranch(job) == 6;
    }

    public static boolean is海盜(final int job) {
        return getJobBranch(job) == 5 || getJobBranch(job) == 6;
    }

    public static short getBeginner(final short job) {
        if (job % 1000 < 10) {
            return job;
        }
        switch (job / 100) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 8:
            case 9:
                return (short) 初心者.getId();
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return (short) 貴族.getId();
            case 20:
                return (short) 傳說.getId();
            case 21:
                return (short) 傳說.getId();
        }
        return (short) 初心者.getId();
    }

    public static boolean is初心者(int jobid) {
        if (jobid <= 5000) {
            if (jobid != 5000 && (jobid < 2001 || jobid > 2005 && (jobid <= 3000 || jobid > 3002 && (jobid <= 4000 || jobid > 4002)))) {
            } else {
                return true;
            }
        } else if (jobid >= 6000 && (jobid <= 6001 || jobid == 13000)) {
            return true;
        }
        boolean result = isJob12000(jobid);
        if (jobid % 1000 == 0 || jobid / 100 == 8000 || jobid == 8001 || result) {
            result = true;
        }
        return result;
    }

    public static boolean isJob12000(int job) {
        boolean result = isJob12000HighLv(job);
        if (isJob12000LowLv(job) || result) {
            result = true;
        }
        return result;
    }

    public static boolean isJob12000HighLv(int job) {
        return job == 12003 || job == 12004;
    }

    public static boolean isJob12000LowLv(int job) {
        return job == 12000 || job == 12001 || job == 12002;
    }
/*
    public static boolean isJob8000(int job) {
        int v1 = SkillConstants.getJobBySkill(job);
        return v1 >= 800000 && v1 <= 800099 || v1 == 8001;
    }

    public static boolean isJob9500(int job) {
        boolean result;
        if (job >= 0) {
            result = SkillConstants.getJobBySkill(job) == 9500;
        } else {
            result = false;
        }
        return result;
    }*/

    public static int get轉數(int jobid) {
        int result;
        if (is初心者(jobid) || jobid % 100 == 0 || jobid == 501 || jobid == 3101 || jobid == 508) {
            result = 1;
        } else {
            int v1 = jobid % 10;
            int v2;
            if (jobid / 10 == 43) {
                v2 = v1 / 2 + 2;
            } else {
                v2 = v1 + 2;
            }
            //  if (v2 >= 2 && (v2 <= 4 || v2 <= 10 && is龍魔導士(jobid))) {
            //       result = v2;
            //  } else {
            result = 0;
            //  }
        }
        return result;
    }

    public static boolean isBeginner(final int job) {
        return getJobGrade(job) == 0;
    }

    public static boolean isSameJob(int job, int job2) {
        int jobNum = getJobGrade(job);
        int job2Num = getJobGrade(job2);
        // 對初心者判斷
        if (jobNum == 0 || job2Num == 0) {
            return getBeginner((short) job) == getBeginner((short) job2);
        }

        // 初心者過濾掉后, 對職業群進行判斷
        if (getJobGroup(job) != getJobGroup(job2)) {
            return false;
        }

        // 代碼特殊的單獨判斷
        if (MapleJob.is管理員(job) || MapleJob.is管理員(job)) {
            return MapleJob.is管理員(job2) && MapleJob.is管理員(job2);
        }
//        } else if (MapleJob.is重砲指揮官(job) || MapleJob.is重砲指揮官(job)) {
//            return MapleJob.is重砲指揮官(job2) && MapleJob.is重砲指揮官(job2);
//        } else if (MapleJob.is蒼龍俠客(job) || MapleJob.is蒼龍俠客(job)) {
//            return MapleJob.is蒼龍俠客(job2) && MapleJob.is蒼龍俠客(job2);
//        } else if (MapleJob.is惡魔復仇者(job) || MapleJob.is惡魔復仇者(job)) {
//            return MapleJob.is惡魔復仇者(job2) && MapleJob.is惡魔復仇者(job2);
//        }

        // 對一轉分支判斷(如 劍士 跟 黑騎)
        if (jobNum == 1 || job2Num == 1) {
            return job / 100 == job2 / 100;
        }

        return job / 10 == job2 / 10;
    }

    public static int getJobGroup(int job) {
        return job / 1000;
    }

    public static int getJobBranch(int job) {
        if (job / 100 == 27) {
            return 2;
        } else {
            return job % 1000 / 100;
        }
    }

    public static int getJobBranch2nd(int job) {
        if (job / 100 == 27) {
            return 2;
        } else {
            return job % 1000 / 100;
        }
    }

    public static int getJobGrade(int jobz) {
        int job = (jobz % 1000);
        if (job / 10 == 0) {
            return 0; //beginner
        } else if (job / 10 % 10 == 0) {
            return 1;
        } else {
            return job % 10 + 2;
        }
    }
}
