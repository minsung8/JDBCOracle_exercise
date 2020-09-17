package my.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyUtil {
	
	public static String getDay(int n) {
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, n);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(currentDate.getTime());		
		
	}

}
