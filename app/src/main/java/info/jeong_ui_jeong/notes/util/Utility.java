package info.jeong_ui_jeong.notes.util;

import java.text.SimpleDateFormat;
import java.util.Date;

// 유틸리티 클래스
public class Utility {

    public static String getCurrentTimestamp(){

        try{
            // 현재 시간을 가져온다.
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy"); // must be lowercase for api 23-
            String currentDateTime = dateFormat.format(new Date());
            return currentDateTime;

        } catch (Exception e){
            return null;
        }

    }

    public static String getMonthFromNumber(String monthNumber){

        switch (monthNumber){
            case "01":{
                return "1월";
            }
            case "02":{
                return "2월";
            }
            case "03":{
                return "3월";
            }
            case "04":{
                return "4월";
            }
            case "05":{
                return "5월";
            }
            case "06":{
                return "6월";
            }
            case "07":{
                return "7월";
            }
            case "08":{
                return "8월";
            }
            case "09":{
                return "9월";
            }
            case "10":{
                return "10월";
            }
            case "11":{
                return "11월";
            }
            case "12":{
                return "12월";
            }
            default: {
                return "Error";
            }
        }

    }

}
