package com.work.nzqpdaili.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class CheckUtil {
	
    /**
     *  Regular expression pattern to match most part of RFC 3987
     *  Internationalized URLs, aka IRIs.  Commonly used Unicode characters are
     *  added.
     */
    public static final Pattern WEB_URL = Pattern.compile(
        "((?:(http|https|Http|Https):\\/\\/(?:(?:[a-zA-Z0-9\\" +
        "$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2}))" +
        "{1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|" +
        "(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-\\_]{0,64}\\.)" +
        "+(?:(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])|(?:biz|b[abdefghijmnorstvwyz])|" +
        "(?:cat|com|coop|c[acdfghiklmnoruvxyz])|d[ejkmoz]|(?:edu|e[cegrstu])|f[ijkmor]|" +
        "(?:gov|g[abdefghilmnpqrstuwy])" +
        "|h[kmnrtu]|(?:info|int|i[delmnoqrst])" +
        "|(?:jobs|j[emop])|k[eghimnrwyz]|l[abcikrstuvy]|(?:mil|mobi|museum|m[acdeghklmnopqrstuvwxyz])" +
        "|(?:name|net|n[acefgilopruz])|(?:org|om)|(?:pro|p[aefghklmnrstwy])|qa|r[eouw]|s[abcdeghijklmnortuvyz]" +
        "|(?:tel|travel|t[cdfghjklmnoprtvwz])|u[agkmsyz]|v[aceginu]|w[fs]|y[etu]|z[amw]))|" +
        "(?:(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\." +
        "(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\." +
        "(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\." +
        "(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])))" +
        "(?:\\:\\d{1,5})?)(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~%\\-" +
        "\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?");
	private static DecimalFormat df;

	/**
	 * 4-16 chars
	 * @param password
	 * @return
	 */
	public static boolean checkPassword(String password){
		if(isEmpty(password)){
			return false;
		}
		return password.length() >= 6 && password.length() <=16;
	}
	
	/**
	 * 4-16 chars
	 * @param password
	 * @return
	 */
	public static boolean checkPassword(CharSequence password){
		if(isEmpty(password)){
			return false;
		}
		return password.length() >= 6 && password.length() <=16;
	}
	
	public static int intValue(String value){
		try{
			Integer integer = Integer.parseInt(value);
			return integer == null ? 0 : integer;
		}catch(NumberFormatException e){
		}
		return 0;
	}
	
	public static long longValue(String value){
		try{
			Long longv = Long.parseLong(value);
			return longv == null ? 0 : longv;
		}catch(NumberFormatException e){
		}
		return 0;
	}
	
	public static int intValue(CharSequence value){
		return intValue(value.toString());
	}
	
	public static boolean checkCellphone(String str) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Check string is empty
	 * @param text
	 * @return
	 */
	public static boolean isEmpty(String text){
		return TextUtils.isEmpty(text);
	}
	
	public static boolean checkLength(String text, int minLength){
		return isEmpty(text) ? false : text.trim().length() >= minLength;
	}
	
	/**
	 * Check string is empty
	 * @param text
	 * @return
	 */
	public static boolean isEmpty(CharSequence text){
		return TextUtils.isEmpty(text);
	}
	
	public static <T> boolean isEmpty(Collection<T> coll){
		return coll == null || coll.size() == 0;
	}
	
	public static boolean isCellphone(String str) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isPassword(String str) {
		Pattern pattern = Pattern.compile("[^(\\u4e00-\\u9fa5)]{6,15}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isVefyCode(String str) {
		if(str == null){
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]{4}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	// 过滤特殊字符
	public static boolean isNickName(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		String regEx = "[a-zA-Z0-9\\u4e00-\\u9fa5]{1,16}";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 计算距离的方法，传入一个int型参数，大于1000则换算成千米数返回，否则换算成米
	 * @return
     */
	public static String toDistance(int meters){
		if(meters>=1000){
			double kMeters = meters/1000.0;
			DecimalFormat format = new DecimalFormat("0.0");
			return format.format(kMeters)+"千米";
		}else if(meters > 0){
			return meters+"米";
		}else{
			return "0千米";
		}

	}

	public static String checkNumber(int num){
		if(num>=10000){
			double kMeters = num/10000.0;
			DecimalFormat format = new DecimalFormat("0.0");
			return format.format(kMeters)+"万";
		}else if(num > 0){
			return num+"";
		}else{
			return "0";
		}

	}

//	public static boolean getTabMode(){
//		if(DeviceUtil.OPPO_MOBILE.equals(android.os.Build.MODEL)){
//			return true;
//        }
//		return false;
//	}

	public static String getHtmlString(String body){
		if(isEmpty(body)){
			return null;
		}
		return "<head><style > img{ width:100%"+" !important;} </style><body  style = font-size:28px><font >"+body+"</font></body></head>";
//		return "<head><style >  </style><body < style = font-size:18px;><font color= '#000000'>"+body+"</font></body></head>";
//		img{ width:wrap_content"+" !important;}
//		return body;
	}

	//百度坐标转火星坐标
	public static  double[] bdToGaoDe(double bd_lat, double bd_lon) {
		double[] gd_lat_lon = new double[2];
		double PI = 3.14159265358979324 * 3000.0 / 180.0;
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
		gd_lat_lon[0] = z * Math.cos(theta);
		gd_lat_lon[1] = z * Math.sin(theta);
		return gd_lat_lon;
	}

	//火星坐标转百度坐标
	public static  double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
		double[] bd_lat_lon = new double[2];
		double PI = 3.14159265358979324 * 3000.0 / 180.0;
		double x = gd_lon, y = gd_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
		bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
		bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
		return bd_lat_lon;
	}

	public static String getDoubleFormat(double d){
		if(df == null)
			df = new DecimalFormat("######0.00");
		return df.format(d);
	}

}
