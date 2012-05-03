package assistuntu.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DataTable<K, V extends Identible<K>> {
    private Map<K, V> rows = new HashMap<K, V>();


    public void put(V row) {
        rows.put(row.getId(), row);
    }

    public void clear() {
        rows.clear();
    }

    public Collection<K> keys() {
        return rows.keySet();
    }

    public Collection<V> values() {
        return rows.values();
    }

    public V get(K id) {
        return rows.get(id);
    }

    public void remove(K k) {
        rows.remove(k);
    }
}
