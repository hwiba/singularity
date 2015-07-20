package singularity.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
	public static Date addCurrentTime(String noteTargetDate) {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date to;
		try {
			to = transFormat.parse(noteTargetDate);
			return to;
		} catch (ParseException e) {
			return new Date();
		}
	}
}
