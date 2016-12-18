package group.g203.countables.base.utils;

import java.util.Collection;

public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        return (collection == null || collection.isEmpty()) ? true : false;
    }

}
