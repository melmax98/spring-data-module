package org.example.storage.dao;

import org.example.model.Storable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface Dao {
    Storable save(Storable storable);

    Storable update(Storable storable);

    boolean delete(long entityId);

    Map<String, Storable> getStorage();

    default List<Storable> getPage(List<Storable> entities, int pageNum, int pageSize) {
        if (pageSize <= 0 || pageNum <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (pageNum - 1) * pageSize;
        if (entities == null || entities.size() <= fromIndex) {
            return Collections.emptyList();
        }

        // toIndex exclusive
        return entities.subList(fromIndex, Math.min(fromIndex + pageSize, entities.size()));
    }
}
