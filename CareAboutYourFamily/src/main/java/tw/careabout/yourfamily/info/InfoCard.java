package tw.careabout.yourfamily.info;

public class InfoCard {

    private boolean expandable;
    private String title, t001, t002, t003, t000, t010, t100;

//    title   店名
//    t001    電話
//    t002    地址
//    t003    網址
//    t000    內容
//
//    t010    備註
//    t100    地區


    public InfoCard(String title, String t001, String t002, String t003, String t000, String t100) {
        this.expandable = false;
        this.title = title;
        this.t001 = t001;
        this.t002 = t002;
        this.t003 = t003;
        this.t000 = t000;
        this.t010 = t010;
        this.t100 = t100;
    }


    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getT001() {
        return t001;
    }

    public void setT001(String t001) {
        this.t001 = t001;
    }

    public String getT002() {
        return t002;
    }

    public void setT002(String t002) {
        this.t002 = t002;
    }

    public String getT003() {
        return t003;
    }

    public void setT003(String t003) {
        this.t003 = t003;
    }

    public String getT000() {
        return t000;
    }

    public void setT000(String t000) {
        this.t000 = t000;
    }

    public String getT010() {
        return t010;
    }

    public void setT010(String t010) {
        this.t010 = t010;
    }

    public String getT100() {
        return t100;
    }

    public void setT100(String t100) {
        this.t100 = t100;
    }
}
