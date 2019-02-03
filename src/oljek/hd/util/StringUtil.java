package oljek.hd.util;

public class StringUtil {

    public static String stripColor(String str, char customChar) {
        char[] chars = str.toCharArray();

        StringBuilder finalString = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (c == customChar) {
                if (i + 1 <= chars.length - 1) {
                    char cAnd = chars[i + 1];

                    if ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".contains(String.valueOf(cAnd)))
                        i += 1;
                    else
                        finalString.append(c);
                }
            } else
                finalString.append(c);
        }

        return finalString.toString();
    }

}
