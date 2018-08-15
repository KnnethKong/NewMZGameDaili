package com.work.nzqpdaili.Bean;

import java.util.List;

/**
 * Created by range on 2017/12/13.
 */

public class ZhanjiBean {

    /**
     * consume : 2
     * roomlist : [{"id":"41","roomid":"488621","serverid":"4037","createuserid":"39705","playcount":"0","gamerule":"4350088","createdate":"2017-12-13 11:55:08","startdate":null,"enddate":null,"gamestatus":"3","playername1":"yangzhou","playername2":"默默","playername3":"Lisa","playername4":"布鲁斯","score1":"8","score2":"4","score3":"0","score4":"-12","playerid1":"39705","playerid2":"36813","playerid3":"36870","playerid4":"36694","recordid":"184569","clubid":"124157","insurescore":"2","row_number":"1","enddatel":"2017-12-13","enddater":"11:55:08"}]
     */

    private String consume;
    private List<RoomlistBean> roomlist;

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public List<RoomlistBean> getRoomlist() {
        return roomlist;
    }

    public void setRoomlist(List<RoomlistBean> roomlist) {
        this.roomlist = roomlist;
    }

    public static class RoomlistBean {
        /**
         * id : 41
         * roomid : 488621
         * serverid : 4037
         * createuserid : 39705
         * playcount : 0
         * gamerule : 4350088
         * createdate : 2017-12-13 11:55:08
         * startdate : null
         * enddate : null
         * gamestatus : 3
         * playername1 : yangzhou
         * playername2 : 默默
         * playername3 : Lisa
         * playername4 : 布鲁斯
         * score1 : 8
         * score2 : 4
         * score3 : 0
         * score4 : -12
         * playerid1 : 39705
         * playerid2 : 36813
         * playerid3 : 36870
         * playerid4 : 36694
         * recordid : 184569
         * clubid : 124157
         * insurescore : 2
         * row_number : 1
         * enddatel : 2017-12-13
         * enddater : 11:55:08
         */

        private String id;
        private String roomid;
        private String serverid;
        private String createuserid;
        private String playcount;
        private String gamerule;
        private String createdate;
        private Object startdate;
        private Object enddate;
        private String gamestatus;
        private String playername1;
        private String playername2;
        private String playername3;
        private String playername4;
        private String score1;
        private String score2;
        private String score3;
        private String score4;
        private String playerid1;
        private String playerid2;
        private String playerid3;
        private String playerid4;
        private String recordid;
        private String clubid;
        private String insurescore;
        private String row_number;
        private String enddatel;
        private String enddater;
        private String userwin;

        public String getUserwin() {
            return userwin;
        }

        public void setUserwin(String userwin) {
            this.userwin = userwin;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRoomid() {
            return roomid;
        }

        public void setRoomid(String roomid) {
            this.roomid = roomid;
        }

        public String getServerid() {
            return serverid;
        }

        public void setServerid(String serverid) {
            this.serverid = serverid;
        }

        public String getCreateuserid() {
            return createuserid;
        }

        public void setCreateuserid(String createuserid) {
            this.createuserid = createuserid;
        }

        public String getPlaycount() {
            return playcount;
        }

        public void setPlaycount(String playcount) {
            this.playcount = playcount;
        }

        public String getGamerule() {
            return gamerule;
        }

        public void setGamerule(String gamerule) {
            this.gamerule = gamerule;
        }

        public String getCreatedate() {
            return createdate;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public Object getStartdate() {
            return startdate;
        }

        public void setStartdate(Object startdate) {
            this.startdate = startdate;
        }

        public Object getEnddate() {
            return enddate;
        }

        public void setEnddate(Object enddate) {
            this.enddate = enddate;
        }

        public String getGamestatus() {
            return gamestatus;
        }

        public void setGamestatus(String gamestatus) {
            this.gamestatus = gamestatus;
        }

        public String getPlayername1() {
            return playername1;
        }

        public void setPlayername1(String playername1) {
            this.playername1 = playername1;
        }

        public String getPlayername2() {
            return playername2;
        }

        public void setPlayername2(String playername2) {
            this.playername2 = playername2;
        }

        public String getPlayername3() {
            return playername3;
        }

        public void setPlayername3(String playername3) {
            this.playername3 = playername3;
        }

        public String getPlayername4() {
            return playername4;
        }

        public void setPlayername4(String playername4) {
            this.playername4 = playername4;
        }

        public String getScore1() {
            return score1;
        }

        public void setScore1(String score1) {
            this.score1 = score1;
        }

        public String getScore2() {
            return score2;
        }

        public void setScore2(String score2) {
            this.score2 = score2;
        }

        public String getScore3() {
            return score3;
        }

        public void setScore3(String score3) {
            this.score3 = score3;
        }

        public String getScore4() {
            return score4;
        }

        public void setScore4(String score4) {
            this.score4 = score4;
        }

        public String getPlayerid1() {
            return playerid1;
        }

        public void setPlayerid1(String playerid1) {
            this.playerid1 = playerid1;
        }

        public String getPlayerid2() {
            return playerid2;
        }

        public void setPlayerid2(String playerid2) {
            this.playerid2 = playerid2;
        }

        public String getPlayerid3() {
            return playerid3;
        }

        public void setPlayerid3(String playerid3) {
            this.playerid3 = playerid3;
        }

        public String getPlayerid4() {
            return playerid4;
        }

        public void setPlayerid4(String playerid4) {
            this.playerid4 = playerid4;
        }

        public String getRecordid() {
            return recordid;
        }

        public void setRecordid(String recordid) {
            this.recordid = recordid;
        }

        public String getClubid() {
            return clubid;
        }

        public void setClubid(String clubid) {
            this.clubid = clubid;
        }

        public String getInsurescore() {
            return insurescore;
        }

        public void setInsurescore(String insurescore) {
            this.insurescore = insurescore;
        }

        public String getRow_number() {
            return row_number;
        }

        public void setRow_number(String row_number) {
            this.row_number = row_number;
        }

        public String getEnddatel() {
            return enddatel;
        }

        public void setEnddatel(String enddatel) {
            this.enddatel = enddatel;
        }

        public String getEnddater() {
            return enddater;
        }

        public void setEnddater(String enddater) {
            this.enddater = enddater;
        }
    }
}
