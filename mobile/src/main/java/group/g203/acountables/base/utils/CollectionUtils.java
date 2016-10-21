package group.g203.acountables.base.utils;

import java.util.Collection;
import java.util.Collections;

public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        } else {
            collection.removeAll(Collections.singleton(null));
            return (collection == null || collection.isEmpty());
        }
    }

}
