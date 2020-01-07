package com.demo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {
	
	/**
	 * DateUtill의 생성자
	 */
	public DateUtil() {
	}	

	/**
	 * 입력된 날짜가 유효한 날짜인지를 검사한다.
	 * @param date 기준일자
	 * @return 유효여부(true/false)
	 */
	public static boolean isDate( String date ) {
    	return isDate( date , null );
    }
    
    public static boolean isDate( String date , String format ) {
		if( date == null )
			return false;
		if( format == null )
			format = "yyyyMMdd";
		DateFormat df = new SimpleDateFormat( format , Locale.KOREA );
		df.setLenient( false );
		date = date.replaceAll( "\\D" , "" );
		try {
		    df.parse( date );
		    return true;
		}catch( ParseException pe ) {
		    return false;
		}catch( Exception e ) {
		    return false;
		}    	
    }
    

    /**
     * 날짜 비교 차 (월)
     * @param int yyyy1
     * @param int mm1
     * @param int dd1
     * @param int yyyy2
     * @param int mm2
     * @param int dd2
     * @return  int
     */
    public static int remainderMonth(int yyyy1, int mm1, int dd1, int yyyy2, int mm2, int dd2) {
        Calendar now = Calendar.getInstance();
        Calendar be = Calendar.getInstance();
        be.set(yyyy1, mm1, dd1);
        now.set(yyyy2, mm2, dd2);

        int y = (now.get(Calendar.YEAR) - be.get(Calendar.YEAR)) * 12;
        int m = (now.get(Calendar.MONTH) - be.get(Calendar.MONTH));
        int d = new Integer(now.get(Calendar.DATE)).compareTo(new Integer(be.get(Calendar.DATE)));


        return y + m + d;
    }
    
    /**
     * 날짜 비교 차 (월)
     * @param String yyyy1
     * @param String mm1
     * @param String dd1
     * @param String yyyy2
     * @param String mm2
     * @param String dd2
     * @return int
     */
    public static int remainderMonth(String yyyy1, String mm1, String dd1, String yyyy2, String mm2, String dd2) {
        return remainderMonth(Integer.parseInt(yyyy1), Integer.parseInt(mm1), Integer.parseInt(dd1), Integer.parseInt(yyyy2), Integer.parseInt(mm2), Integer.parseInt(dd2));
    }
    
    /**
     * 날짜 비교 차 (월)
     * @param int yyyy
     * @param int mm
     * @param int dd
     * @return int
     */
    public static int remainderMonth(int yyyy, int mm, int dd){
        Calendar now = Calendar.getInstance();
        return remainderMonth(yyyy, mm, dd, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
    }
    
    /**
     * 날짜 비교 차 (월)
     * @param String yyyy
     * @param String mm
     * @param String dd
     * @return int
     */
    public static int remainderMonth(String yyyy, String mm, String dd){
        return remainderMonth(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));
    }
    
    /**
     * 날짜 비교 차 (월)
     * @param String ymd
     * @return int
     */
    public static int remainderMonth(String ymd){
        return remainderMonth(ymd.substring(0, 4), ymd.substring(4, 6), ymd.substring(6, 8));
    }
  
    /**
     * 현재 날짜를 return한다.
     * @return String
     */
    
    public static String getDate() {
        Calendar cal = Calendar.getInstance();
        int yy = cal.get(Calendar.YEAR);
        int mo = cal.get(Calendar.MONTH) + 1;
        int dd = cal.get(Calendar.DAY_OF_MONTH);

        String yyy = null;
        String mmo = null;
        String ddd = null;

        //if(yy < 2000)   yyy = "19" + yy;
        //else yyy = "" + yy;
        yyy = "" + yy;
        if (mo < 10)
            mmo = "0" + mo;
        else
            mmo = "" + mo;
        if (dd < 10)
            ddd = "0" + dd;
        else
            ddd = "" + dd;

        String addDate = "" + yyy + mmo + ddd;
        return addDate;
    }

    public static String getDate(String dateFormat){

		Calendar cal = Calendar.getInstance();
		int result = 0;

		if(dateFormat.equals("yy")){
			result = cal.get(Calendar.YEAR);
		}else if(dateFormat.equals("mm")){
			result = cal.get(Calendar.MONTH) + 1;
		}else if(dateFormat.equals("dd")){
			result = cal.get(Calendar.DAY_OF_MONTH);
		}

    	return (result < 10 ? "0"+result : result+"");
	}

    /**
     * 현재 시간을 return한다.
     * @return String
     */
    public static String getTime() {
        Calendar cal = Calendar.getInstance();
//		int hh = cal.get(Calendar.HOUR_OF_DAY) + 1;
        int hh = cal.get(Calendar.HOUR_OF_DAY);
        int mm = cal.get(Calendar.MINUTE);
        int ss = cal.get(Calendar.SECOND);

        String hhh = null;
        String mmm = null;
        String sss = null;

        if (hh < 10)
            hhh = "0" + hh;
        else
            hhh = "" + hh;
        if (mm < 10)
            mmm = "0" + mm;
        else
            mmm = "" + mm;
        if (ss < 10)
            sss = "0" + ss;
        else
            sss = "" + ss;

        String addTime = "" + hhh + mmm + sss;
        return addTime;
    }
    
    /**
     * millisecond TimeStamp 
     * @param yyyymmdd
     * @param seperator
     * @return String
     */
    public static String getDateTimeStamp() {
        Calendar cal = Calendar.getInstance();
        int yy = cal.get(Calendar.YEAR);
        int mo = cal.get(Calendar.MONTH) + 1;
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        int hh = cal.get(Calendar.HOUR_OF_DAY);
        int mm = cal.get(Calendar.MINUTE);
        int ss = cal.get(Calendar.SECOND);
        int ms = cal.get(Calendar.MILLISECOND);

        String yyy = null;
        String mmo = null;
        String ddd = null;
        String hhh = null;
        String mmm = null;
        String sss = null;
        String mss = null;
    
        yyy = "" + yy;
        if (mo < 10)
            mmo = "0" + mo;
        else
            mmo = "" + mo;
        if (dd < 10)
            ddd = "0" + dd;
        else
            ddd = "" + dd;
       
        if (hh < 10)
            hhh = "0" + hh;
        else
            hhh = "" + hh;
        if (mm < 10)
            mmm = "0" + mm;
        else
            mmm = "" + mm;
        if (ss < 10)
            sss = "0" + ss;
        else
            sss = "" + ss;
        if (ms < 10)
        	mss = "00" + ms;
        else if (ms < 100)
        	mss = "0" + ms;
        else
        	mss = "" + ms;

        String addDateTimeStamp = ""  + yyy + mmo + ddd + hhh + mmm + sss + mss;
        
        return addDateTimeStamp;
    }
    
    /**
     * 구분자로 표현된 날짜값 반환
     * @param yyyymmdd
     * @param seperator
     * @return String
     */
    public static String getDateFormat(String yyyymmdd, String seperator) {
        if (yyyymmdd == null || yyyymmdd.trim().length() == 0) return "";

        if (yyyymmdd != null && yyyymmdd.trim().length() == 6) {
            return yyyymmdd.substring(0, 4) + seperator + yyyymmdd.substring(4);

        } else if (yyyymmdd != null && yyyymmdd.trim().length() == 8) {
            String yyyy = yyyymmdd.substring(0, 4);
            String mm = yyyymmdd.substring(4, 6);
            String dd = yyyymmdd.substring(6);
            return getDateFormat(yyyy, mm, dd, seperator);

        } else
            return yyyymmdd;
    }
    
    /**
     * 구분자로 표현된 날짜값 반환
     * @param yyyy
     * @param mm
     * @param dd
     * @param seperator
     * @return String
     */
    public static String getDateFormat(String yyyy, String mm, String dd, String seperator) {
        if (yyyy == null || yyyy.trim().length() != 4 || yyyy.trim().equals("")) {
            return "";
        } else if (mm == null || mm.trim().length() != 2 || mm.trim().equals("")) {
            return "";
        } else if (dd == null || dd.trim().length() != 2 || dd.trim().equals("")) {
            return "";
        }

        return yyyy + seperator + mm + seperator + dd;
    }

    
    
    /**
     * 
     * 20070529 날짜구분을 한글로..
     * @param yyyymmdd
     * @return String
     */
    public static String getDateFormatHangul(String yyyymmdd) {
        if (yyyymmdd == null || yyyymmdd.trim().length() == 0) return "";

        if (yyyymmdd != null && yyyymmdd.trim().length() == 6) {
            return yyyymmdd.substring(0, 4) + "년 " + yyyymmdd.substring(4)+"월";

        } else if (yyyymmdd != null && yyyymmdd.trim().length() == 8) {
            String yyyy = yyyymmdd.substring(0, 4);
            String mm = yyyymmdd.substring(4, 6);
            String dd = yyyymmdd.substring(6);
            return yyyy + "년 " + mm + "월 "+ dd + "일";
        } else
            return yyyymmdd;
    }
    
    /**
     * 문자열을 시간포맷으로 변경 (HHMMSS -> HH:MM:SS)
     * 
     * @param time 문자열.
     * @return 시간포맷 문자열.
     */
    public static String getFmtTime(String time) {
        String returnVal = "";
        if (time == null) {
            time = "";
        }
        time = time.trim();
        if (time.length() >= 6) {
            returnVal = time.substring(0, 2);
            returnVal += ":";
            returnVal += time.substring(2, 4);
            returnVal += ":";
            returnVal += time.substring(4, 6);
        } else if(time.length() >= 4){          //20061201추가
            returnVal = time.substring(0, 2);
            returnVal += ":";
            returnVal += time.substring(2, 4);
        } else {
            returnVal = time;
        }
        return returnVal;
    }    
    
    /**
	 * 패턴으로 날짜를 조회한다.
	 * @param pattern (yyyyMMddHHmmss)
	 * @return String
	 */
	public static String getCurrentTime(String pattern){	
		Calendar current = Calendar.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		String date = fmt.format(current.getTime());
		
		return date;
	}
	
	/**
	* @Method Name  : getCurDate
	* @Date      	: 2012. 6. 18.
	* @Author      	: co11992
	* @Description 	: 현재 날짜를 반환한다.
	* 
	* @param formatString
	* @return
	*/
	public static String getCurDate(String formatString){
		SimpleDateFormat formatter  = new SimpleDateFormat(formatString);
		Date currentTime= new Date();
		return formatter.format(currentTime);
	}
	
	/**
	* @Method Name  : getDateAdd
	* @Date      	: 2012. 6. 18.
	* @Author      	: LSJ
	* @Description 	: 달에 달을 더한 날짜를 구한다.
	* 
	* @param year
	* @param month
	* @param addMonth
	* @return
	*/
	public static String getDateAdd(String year, String month, int addMonth) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(year),Integer.parseInt(month)-1,1);
		cal.add(Calendar.MONTH, addMonth);
		return formatter.format(cal.getTime());
	}
	
	/**
	* @Method Name  : getDateAddDay
	* @Date      	: 2012. 6. 18.
	* @Author      	: LSJ
	* @Description 	: 달에 달을 더한 날짜를 구한다.
	* 
	* @param year
	* @param month
	* @param addMonth
	* @return
	*/
	public static String getDateAddDay(String year, String month, String day, int addDay) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
		cal.add(Calendar.DATE, addDay);
		return formatter.format(cal.getTime());
	}
	
	/**
	* @Method Name  : getDayOfWeek
	* @Date      	: 2012. 6. 20.
	* @Author      	: co12140
	* @Description 	: 해당 날짜의 요일을 반환한다..
	* 
	* @param year
	* @param month
	* @param day
	* @return
	*/
	public static String getDayOfWeek(int year , int month , int day){
		String[] dayOfWeek = new String[]{"일","월","화","수","목","금","토"};
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, day);
		return dayOfWeek[cal.get(Calendar.DAY_OF_WEEK) - 1];
	}

	
	/**
	* @Method Name  : getDateStrng
	* @Date      	: 2012. 7. 6.
	* @Author      	: ds.choi
	* @Description 	: Date를 8자리 날짜로 반환한다.
	* 
	* @param date
	* @return
	*/
	public static String getDateStrng(Date date){
		if(date==null) return "";
		else return new SimpleDateFormat("yyyyMMdd").format(date);		
	}

	/**
	* @Method Name  : getDateTimeStrng
	* @Date      	: 2012. 7. 6.
	* @Author      	: ds.choi
	* @Description 	: Date를 14자리 날짜시간으로 반환한다.
	* 
	* @param date
	* @return
	*/
	public static String getDateTimeStrng(Date date){
		if(date==null) return "";
		else return getDateTimeStrng(date,"yyyyMMddHHmmss");		
	}
	
	public static String getDateTimeStrng(Date date, String format){
		if(date==null) {
			return "";
		}else{ 
			return new SimpleDateFormat(format).format(date);
		}
	}	
	
	
	/**
	* @Method Name  : getDateAddByType
	* @Date      	: 2013. 1. 29.
	* @Author      	: lsj
	* @Description 	: 입력한 날짜에 년 월일을 더한값을 반환한다.
	* 
	* @param date
	* @param type
	* @param add
	* @return
	*/
	public static String getCalcDate(String type ,int add) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		
		Date date = new Date();
		
		String today = formatter.format(date);
		
		GregorianCalendar cal = null;
		String year = today.substring(0,4);
		String month = today.substring(4,6);
		String day = today.substring(6,8);
		
		if("y".equals(type)){
			cal = new GregorianCalendar(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
			cal.add(Calendar.YEAR, add);
			return formatter.format(cal.getTime());
		}else if("m".equals(type)){
			cal = new GregorianCalendar(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
			cal.add(Calendar.MONTH, add);
			return formatter.format(cal.getTime());
		}else if("d".equals(type)){
			cal = new GregorianCalendar(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
			cal.add(Calendar.DATE, add);
			return formatter.format(cal.getTime());
		}else{
			return "";
		}
		
	}
	
	public static Date getDate(int year, int month, int day) {
    	Calendar cal = Calendar.getInstance();
    	
    	cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, day);
		
		return cal.getTime();
    }
	
	public static int getDateInfo(Date date, int field) {
    	Calendar cal = Calendar.getInstance();		
    	cal.setTime(date);
    	
		return cal.get(field);
    }

    /*
	public static String getDayToYear(int val, String format){
		int year = (val/365);
		int mon = (val%365)/30;
		
		float day = (val%365)%30;
		if(year==0 && mon<11 && day > 0) mon += 1;
				
		String rtn = "";
		if("년월".equals(format))
			rtn = year + "년" + mon + "월";
		else if("년 월".equals(format))
			rtn = year + "년 " + mon + "월";
		else if("개월".equals(format))
			rtn = year + "년" + mon + "개월";
		else
			rtn = year + "." + mon;
		
		if(rtn.substring(0,1).equals("0")) rtn = rtn.replace("0년", "");
		rtn = rtn.replace("년0월", "년");
		rtn = rtn.replace("년0개월", "년");
		if("0개월".equals(rtn.trim())) rtn = "";
		
		return StringUtil.NVL(rtn,"-");
	}
	*/
}
