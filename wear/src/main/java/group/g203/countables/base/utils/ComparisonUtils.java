package group.g203.countables.base.utils;

public class ComparisonUtils {
    public static boolean isNumber(String string) {
        return string.matches("-?\\d+(\\.\\d+)?");
    }
}
