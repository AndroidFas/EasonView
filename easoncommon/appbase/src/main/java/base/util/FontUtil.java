package base.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class FontUtil {

    private static Map<String, Typeface> typefaceMap = new HashMap<>();

    public static Typeface getFontByName(Context context, String fontName) {
        Typeface typeface = typefaceMap.get(fontName);

        if (null == typeface) {
            typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            typefaceMap.put(fontName, typeface);
        }
        return typeface;
    }
}