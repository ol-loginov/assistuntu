package assistuntu.model;

public class ThemeRow implements Identible<Integer> {
    private int id;
    private String name;

    public ThemeRow(String[] csvRow) {
        id = Integer.parseInt(csvRow[0]);
        name = csvRow[1];
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
