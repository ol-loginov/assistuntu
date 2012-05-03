package assistuntu.view;

public class Theme {
    private int id;
    private String name;
    private boolean selected;

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
