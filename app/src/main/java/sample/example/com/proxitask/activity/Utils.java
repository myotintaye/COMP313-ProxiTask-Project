package sample.example.com.proxitask.activity;

import java.util.function.Predicate;

/**
 * Created by Myo Tint Aye on 2/11/2018.
 */

public final class Utils {


    public static boolean isEmpty(String... args) {
        for (String s : args) {
            if (s.equals("")) return true;
        }
        return false;
    }

    public static <T> boolean isNull(T... args) {
        for (T val : args) {
            if (val == null) return true;
        }
        return false;
    }

    @SafeVarargs
    public static <T> boolean all(Predicate<T> predicate, T... args) {
        for (T val : args) {
            if (!predicate.test(val))
                return false;
        }
        return true;
    }
}


