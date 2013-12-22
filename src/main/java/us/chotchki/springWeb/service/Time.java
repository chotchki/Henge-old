package us.chotchki.springWeb.service;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.stereotype.Service;

@Service
public class Time {
	private DateTimeFormatter formatter = new DateTimeFormatterBuilder()
    .appendMonthOfYearText()
    .appendLiteral(' ')
    .appendDayOfMonth(1)
    .appendLiteral(", ")
    .appendYear(4, 4)
    .appendLiteral(" at ")
    .appendClockhourOfHalfday(1)
    .appendLiteral(':')
    .appendMinuteOfHour(2)
    .appendHalfdayOfDayText()
    .toFormatter();
	
	/**
	 * Even though we deal exclusively in GMT for storage, we always display in EST (until I implement fancier things)
	 * @param instant
	 * @return
	 */
	public String format(Instant instant) {
		DateTime dt = instant.toDateTime(DateTimeZone.forID("America/New_York"));
		return formatter.print(dt);
	}
}
