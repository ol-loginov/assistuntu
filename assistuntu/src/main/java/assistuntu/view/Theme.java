package assistuntu.view;

import assistuntu.model.ThemeRow;

public class Theme {
    private int id;
    private String name;
    private boolean selected;

    public Theme() {
    }

    public Theme(ThemeRow row) {
        id = row.getId();
        name = row.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Theme deepCopy() {
        Theme other = new Theme();
        other.id = id;
        other.name = name;
        other.selected = selected;
        return other;
    }
}
