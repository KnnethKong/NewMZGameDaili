package com.work.nzqpdaili.Bean;

import java.util.List;

/**
 * Created by range on 2017/12/25.
 */

public class MessageBean {

    /**
     * status : 10001
     * result : {"data":[{"id":"3","status":"1","create_time":"2017-06-21 18:41:39.000","member_id":"1","title":"欢娱互动使用协议","content":"\r\n\r\n\r\n \r\n \r\n \r\n<\/head>\r","is_open":"0","member_name":"haiboyc","msg_type":"1","row_number":"1"}]}
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
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 3
             * status : 1
             * create_time : 2017-06-21 18:41:39.000
             * member_id : 1
             * title : 欢娱互动使用协议
             * content :





             </head>
             * is_open : 0
             * member_name : haiboyc
             * msg_type : 1
             * row_number : 1
             */

            private String id;
            private String status;
            private String create_time;
            private String member_id;
            private String title;
            private String content;
            private String is_open;
            private String member_name;
            private String msg_type;
            private String row_number;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getIs_open() {
                return is_open;
            }

            public void setIs_open(String is_open) {
                this.is_open = is_open;
            }

            public String getMember_name() {
                return member_name;
            }

            public void setMember_name(String member_name) {
                this.member_name = member_name;
            }

            public String getMsg_type() {
                return msg_type;
            }

            public void setMsg_type(String msg_type) {
                this.msg_type = msg_type;
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
