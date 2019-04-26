package base.util;

import java.math.BigDecimal;

/**
 * 描述 处理金钱单位转换
 *
 * @author cyj
 * @Company 易尊通信
 * @Copyright e-joined
 * @create 2017-12-13 下午4:15
 **/
public class MoneyUtil {
    /**
     * @param src  元数据
     * @param from 从哪个单位
     * @param to   转到哪个单位
     * @return 转换后的单位
     */
    public static String convert(String src, Unit from, Unit to) {
        BigDecimal price = new BigDecimal(src);
        BigDecimal n1 = new BigDecimal(Float.valueOf("1000"));
        BigDecimal n2 = new BigDecimal(Float.valueOf("100"));
        BigDecimal n3 = new BigDecimal(Float.valueOf("10"));
        switch (from) {
            case YUAN:
                switch (to) {
                    case YUAN:
                        return src;
                    case JIAO:
                        return String.valueOf(price.multiply(n3).intValue());
                    case FEN:
                        return String.valueOf(price.multiply(n2).intValue());
                    case LI:
                        return String.valueOf(price.multiply(n1).intValue());
                }
            case JIAO:
                switch (to) {
                    case YUAN:
                        return String.valueOf(price.divide(n3).floatValue());
                    case JIAO:
                        return src;
                    case FEN:
                        return String.valueOf(price.multiply(n3).intValue());
                    case LI:
                        return String.valueOf(price.multiply(n2).intValue());
                }
            case FEN:
                switch (to) {
                    case YUAN:
                        return String.valueOf(price.divide(n2).floatValue());
                    case JIAO:
                        return String.valueOf(price.divide(n3).floatValue());
                    case FEN:
                        return src;
                    case LI:
                        return String.valueOf(price.multiply(n3).intValue());
                }
            case LI:
                switch (to) {
                    case YUAN:
                        return String.valueOf(price.divide(n1).floatValue());
                    case JIAO:
                        return String.valueOf(price.divide(n2).floatValue());
                    case FEN:
                        return String.valueOf(price.divide(n3).floatValue());
                    case LI:
                        return src;
                }
        }


        return "";
    }

    public static String convert(int src, Unit from, Unit to) {
        return convert(src + "", from, to);
    }

    public enum Unit {
        YUAN,//元
        JIAO,//角
        FEN,//分
        LI;//厘
    }
}
