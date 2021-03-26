package tw.careabout.yourfamily.med;

public class MedShow_Card {


    private String med001, med002,  med003;

    public MedShow_Card(String med001, String med002, String med003) {
        this.med001 = med001;
        this.med002 = med002;
        this.med003 = med003;
    }

    public String getMed001() {
        return med001;
    }

    public void setMed001(String med001) {
        this.med001 = med001;
    }

    public String getMed002() {
        return med002;
    }

    public void setMed002(String med002) {
        this.med002 = med002;
    }

    public String getMed003() {
        return med003;
    }

    public void setMed003(String med003) {
        this.med003 = med003;
    }
}
