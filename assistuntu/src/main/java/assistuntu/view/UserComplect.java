package assistuntu.view;

import java.util.ArrayList;
import java.util.List;

public class UserComplect {
    private final List<Complect> complects = new ArrayList<Complect>();
    private final List<Theme> themes = new ArrayList<Theme>();

    public List<Complect> getComplects() {
        return complects;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public UserComplect deepCopy() {
        UserComplect other = new UserComplect();
        for (Complect c : complects) {
            other.getComplects().add(c.deepCopy());
        }
        for (Theme theme : themes) {
            other.getThemes().add(theme.deepCopy());
        }
        return other;
    }
}
