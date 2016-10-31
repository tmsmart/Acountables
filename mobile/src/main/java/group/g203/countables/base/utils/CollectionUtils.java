package group.g203.countables.base.utils;

import java.util.Collection;
import java.util.Collections;

public class CollectionUtils {

    public static boolean isEmpty(Collection collection, boolean isRealm) {
        if (collection == null || collection.isEmpty()) {
            return true;
        } else {
            if (!isRealm) {
                collection.removeAll(Collections.singleton(null));
            }
            return (collection == null || collection.isEmpty());
        }
    }

}
