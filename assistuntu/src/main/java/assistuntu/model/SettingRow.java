package assistuntu.model;

public class SettingRow implements Identible<String> {
    private String id;
    private String value;

    public SettingRow(String id) {
        this.id = id;
    }

    public SettingRow(String[] csvRow) {
        id = csvRow[0];
        value = csvRow[1];
    }

    @Override
    public String getId() {
        return id;
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
}
