package base.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
    private static String[] bigs = new String[]{"", "一", "二", "三", "四", "五", "六", "日"};

    public enum TYPE {
        yyyyMMddHHmm("yyyy-MM-dd HH:mm"),
        yyyyoMModd("yyyy.MM.dd"),
        day("dd"),
        month("MM"),
        year("yyyy"),
        MM_dd_HH_mm("MM-dd HH:mm"),
        MMdd_ZH("MM月dd日");
        private String time;

        TYPE(String s) {
            time = s;
        }
    }

    public static final String yyyyMMddHHmm = "yyyy-MM-dd HH:mm";

    private static SimpleDateFormat mDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();

    /**
     * 时间转化
     *
     * @param time 时间戳
     * @param type 转换格式
     * @return
     */
    public static String convent(long time, TYPE type) {
        mDateFormat.applyPattern(type.time);
        return mDateFormat.format(new Date(time));
    }

    /**
     * 获取天
     *
     * @param time
     * @return
     */
    public static String getDay(long time) {
        mDateFormat.applyLocalizedPattern(TYPE.day.time);
        return mDateFormat.format(new Date(time));
    }

    /**
     * 获取月
     *
     * @param time
     * @return
     */
    public static String getMonth(long time) {
        mDateFormat.applyLocalizedPattern(TYPE.month.time);
        return mDateFormat.format(new Date(time));
    }

    /**
     * 根据date获取此月份的第一天和最后一天
     *
     * @param date
     * @return
     */
    public static Date[] getFirstAndLastDayByDate(Date date) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//        String format = simpleDateFormat.format(date);
        Date[] result = new Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.add(Calendar.HOUR, -12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        result[0] = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        result[1] = calendar.getTime();
        return result;
    }

    //获取app时间 2月14日 星期四
    public static String getAppTime() {
        mDateFormat.applyLocalizedPattern(TYPE.MMdd_ZH.time);
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        String date = mDateFormat.format(now);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return date + " 星期" + buildBig(week);
    }

    private static String buildBig(int i) {
        return bigs[i];
    }

    public static void main(String a[]) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date[] dayByDate = getFirstAndLastDayByDate(new Date());
        System.out.println("当前月份第一天：" + simpleDateFormat.format(dayByDate[0].getTime()));
        System.out.println("当前月份最后一天：" + simpleDateFormat.format(dayByDate[1].getTime()));
    }
}
