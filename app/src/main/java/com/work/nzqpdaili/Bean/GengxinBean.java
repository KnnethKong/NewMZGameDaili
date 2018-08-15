package com.work.nzqpdaili.Bean;

/**
 * Created by range on 2017/12/18.
 */

public class GengxinBean {


    /**
     * status : 10001
     * result : {"data":{"gamename":"代理IOS端","gameversion":"2.0","is_upgrade":"1","downurl":"http://fir.im/5p1h","row_number":"1"}}
     */

    private String status;
    private ResultBean result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * data : {"gamename":"代理IOS端","gameversion":"2.0","is_upgrade":"1","downurl":"http://fir.im/5p1h","row_number":"1"}
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * gamename : 代理IOS端
             * gameversion : 2.0
             * is_upgrade : 1
             * downurl : http://fir.im/5p1h
             * row_number : 1
             */

            private String gamename;
            private String gameversion;
            private String is_upgrade;
            private String downurl;
            private String row_number;

            public String getGamename() {
                return gamename;
            }

            public void setGamename(String gamename) {
                this.gamename = gamename;
            }

            public String getGameversion() {
                return gameversion;
            }

            public void setGameversion(String gameversion) {
                this.gameversion = gameversion;
            }

            public String getIs_upgrade() {
                return is_upgrade;
            }

            public void setIs_upgrade(String is_upgrade) {
                this.is_upgrade = is_upgrade;
            }

            public String getDownurl() {
                return downurl;
            }

            public void setDownurl(String downurl) {
                this.downurl = downurl;
            }

            public String getRow_number() {
                return row_number;
            }

            public void setRow_number(String row_number) {
                this.row_number = row_number;
            }
        }
    }
}
