package assistuntu.model;

public class SettingRow {
    private String id;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toCSV() {
        return id + ";" + value;
    }

    public void parseCsv(String[] textList) {
        setId(textList[0]);
        setValue(textList[1]);
    }
}
