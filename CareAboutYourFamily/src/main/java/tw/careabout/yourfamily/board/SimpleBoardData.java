package tw.careabout.yourfamily.board;

public class SimpleBoardData {

        private String s_title,s_creater,s_createTime;

        public SimpleBoardData(String title, String creater, String createTime){
            s_title = title;
            s_creater = creater;
            s_createTime = createTime;
        }

        public String getTitle(){
            return s_title;
        }

        public String getCreater(){
            return s_creater;
        }

        public String getCreateTime(){
            return s_createTime;
        }


}
