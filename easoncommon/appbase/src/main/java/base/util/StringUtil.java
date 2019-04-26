package base.util;

import android.text.Html;
import android.text.Spanned;

public class StringUtil {
    public static boolean isEmpty(String src) {
        return null == src || "".equals(src)||src.length()==0;
    }

    /**
     * 将结果构建成突出关键词的字符串
     *
     * @param result         结果总串
     * @param highLight      高亮的字
     * @param highLightColor 高亮色
     * @return 显示的富文本  直接设置给TextView
     */
    public static Spanned buildHighLightResult(String result, String highLight, String highLightColor) {
        String replace = "<font color=\"" + highLightColor + "\"> " + highLight + "</font>";
        result = result.replace(highLight, replace);
        Spanned tip = Html.fromHtml(result);
        return tip;
    }

}
