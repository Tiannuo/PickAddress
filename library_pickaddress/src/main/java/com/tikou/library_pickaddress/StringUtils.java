package com.tikou.library_pickaddress;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils {

//    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    private final static Pattern chinese = Pattern.compile("[\\u4E00-\\u9FA5]$");

    private final static Pattern password = Pattern.compile("^[0-9a-zA-Z]{6,16}$");

    private final static Pattern url = Pattern.compile("^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&amp;%\\$#\\=~_\\-@]*)*$");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate, int type) {
        try {
            if (type == 1) {
                return dateFormater.get().parse(sdate);
            } else if (type == 2) {
                return dateFormater2.get().parse(sdate);
            } else if (type == 3) {
                return dateFormater3.get().parse(sdate);
            } else {
                return dateFormater.get().parse(sdate);
            }
        } catch (ParseException e) {
            return null;
        }
    }


    public static Date toDate(long ldate, int type) {

        try {
            if (type == 1) {
                return dateFormater.get().parse(dateFormater.get().format(new Date(ldate)));
            } else if (type == 2) {
                return dateFormater2.get().parse(dateFormater2.get().format(new Date(ldate)));
            } else if (type == 3) {
                return dateFormater3.get().parse(dateFormater3.get().format(new Date(ldate)));
            } else {
                return dateFormater.get().parse(dateFormater.get().format(new Date(ldate)));
            }
        } catch (ParseException e) {
            return null;
        }

    }

    public static String toSt(long ldate, int type) {

        if (type == 1) {
            return dateFormater.get().format(new Date(ldate));
        } else if (type == 2) {
            return dateFormater2.get().format(new Date(ldate));
        } else if (type == 3) {
            return dateFormater3.get().format(new Date(ldate));
        } else {
            return dateFormater.get().format(new Date(ldate));
        }

    }


    public static String toSt(String ldate, int type) {
        long time;
        if (StringUtils.isEmpty(ldate)) {
            time = 0;
        } else {
            time = Integer.parseInt(ldate) * 1000;
        }
        return toSt(time, type);
    }

    /**
     * 比较当前时间
     *
     * @param
     * @return
     */
    public static String diffTime(long time) {
        StringBuffer buffer = new StringBuffer();
        Date date1 = toDate(time, 1);
        Date date2 = toDate(System.currentTimeMillis(), 1);

        long between = (date2.getTime() - date1.getTime()) / 1000;//除以1000是为了转换成秒

        long day = between / (24 * 3600);
        long hour = between % (24 * 3600) / 3600;
        long minute = between % 3600 / 60;
        long second = between % 60 / 60;
        if (day > 0) {
            if (day <= 3 && day >= 1) {
                return buffer.append(day).append("天前").toString();
            } else {
                return toSt(time, 2);
            }
        } else if (day == 0) {
            if (hour >= 1 && hour <= 24) {
                return buffer.append(hour).append("小时前").toString();
            } else if (hour == 0 && minute > 0) {
                return buffer.append(minute).append("分钟前").toString();
            } else if (hour == 0 && minute == 0 && second > 0) {
                return buffer.append("1分钟前").toString();
            }
        }
        return "未知";
    }

//    public static boolean isEqualTime(String sdate1, String sdate2) {
//        return isEqualTime(toDate2(sdate1), toDate2(sdate2));
//    }
//
//    public static boolean isEqualTime(long ldate1, long ldate2) {
//        return isEqualTime(toDate3(ldate1), toDate3(ldate2));
//    }
//
//    public static boolean isEqualTime(Date date1, Date date2) {
//        if (date1 == null || date2 == null) {
//            return false;
//        }
//        if (date1.getTime() == date2.getTime()) {
//            return true;
//        }
//        return false;
//    }


    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate, 1);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }


    /**
     * 判断给定字符串是否空白串。
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串
     * 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        return false;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null) return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }


    /**
     * 判断list是否为空
     *
     * @param list
     * @return
     */
    public static boolean isEmptyList(List<?> list) {
        if (list == null || list.isEmpty())
            return true;
        return false;
    }

    /**
     * 判断字符串是否相等
     *
     * @param st1
     * @param st2
     * @return
     */
    public static boolean isEqualSt(String st1, String st2) {
        if (null == st1 || null == st2) {
            return false;
        }

        if (st1.equals(st2)) {
            return true;
        }
        return false;
    }

    public static void changeColor(View view, int color) {
        GradientDrawable bgShape = (GradientDrawable) view.getBackground();
        bgShape.setColor(color);
    }


    public static void changeColors(int color, View... views) {
        for (View view : views) {
            changeColor(view, color);
        }
    }

    /**
     * 判断是否为汉字
     *
     * @param ch
     * @return
     */
    public static boolean isChinese(String ch) {
        if (isEmpty(ch))
            return false;
        return chinese.matcher(ch).matches();
    }

    /**
     * 判断是否为密码
     *
     * @param ch
     * @return
     */
    public static boolean isPassword(String ch) {
        if (isEmpty(ch))
            return false;
        return password.matcher(ch).matches();
    }

    /**
     * 验证网址Url
     *
     * @param
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUrl(String str) {

        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return url.matcher(str).matches();
    }


    /**
     * @param ch
     * @return
     */
    public static String decoder(String ch) {
        if (isEmpty(ch))
            return "";
        try {
            return URLDecoder.decode(ch, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ch;
        }
    }


    /**
     * @param ch
     * @return
     */
    public static String enDecoder(String ch) {
        if (isEmpty(ch))
            return "";
        try {
            return URLEncoder.encode(ch, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ch;
        }
    }


//    public static LatLng bd_decrypt(double bd_lat, double bd_lon)
//    {
//        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
//        double z =Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
//        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
////        gg_lon = z * cos(theta);
////        gg_lat = z * sin(theta);
//        return new LatLng(z * Math.sin(theta),z * Math.cos(theta));
//    }
//    public static LatLonPoint point_decrypt(double bd_lat, double bd_lon)
//    {
//        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
//        double z =Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
//        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
////        gg_lon = z * cos(theta);
////        gg_lat = z * sin(theta);
//        return new LatLonPoint(z * Math.sin(theta),z * Math.cos(theta));
//    }


    public static String[] getAddressItem(String address) {
        if (StringUtils.isEmpty(address)) {
            return null;
        }
        try {
            if (address.contains("-")) {
                return address.split("-");
            } else {
                return address.split(" ");
            }
        } catch (Exception e) {
            return null;
        }

    }


    /**
     * 字符串转换成十六进制字符串
     */

    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }


    public  static String getBlank(String code) {
        if (isEmpty(code) || code.length() <= 4) {
            return code;
        }
        int length = code.length();
        double count = length % 4.0;
        StringBuffer buffer = new StringBuffer();

        if (count == 0) {
            for (int i = 0; i < count-1; i++) {
                buffer.append("****");
                buffer.append(" ");
            }
        } else {
            for (int i = 0; i < length - Math.floor(count) * 4; i++) {
                buffer.append("*");
                buffer.append(" ");
            }
            for (int i = 0; i <= Math.floor(count)-1; i++) {
                buffer.append("****");
                buffer.append(" ");
            }

        }

        buffer.append(code.substring(code.length() - 4));
        return buffer.toString();
    }

}
