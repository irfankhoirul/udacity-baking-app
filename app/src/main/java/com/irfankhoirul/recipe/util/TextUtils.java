package com.irfankhoirul.recipe.util;

/**
 * Created by Irfan Khoirul on 8/5/2017.
 */

public class TextUtils {

    public static String capitalizeEachWords(String text) {
        String[] stringArray = text.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : stringArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            stringBuilder.append(cap).append(" ");
        }
        return stringBuilder.toString();
    }

    public static String removeTrailingZero(String stringNumber) {
        return !stringNumber.contains(".") ? stringNumber :
                stringNumber.replaceAll("0*$", "").replaceAll("\\.$", "");
    }

}
