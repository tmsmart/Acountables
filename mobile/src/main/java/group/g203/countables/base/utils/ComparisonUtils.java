package group.g203.countables.base.utils;

import java.util.Comparator;

import group.g203.countables.model.Countable;

public class ComparisonUtils {

    public static class CompleteCountComparator implements Comparator<Countable> {
        @Override
        public int compare(Countable countable, Countable countable2) {
            int counts = (CollectionUtils.isEmpty(countable.loggedDates, true)) ? 0 : countable.loggedDates.size();
            int counts2 = (CollectionUtils.isEmpty(countable2.loggedDates, true)) ? 0 : countable2.loggedDates.size();
            return counts < counts2 ? -1 : counts == counts2 ? 0 : 1;
        }
    }
}
