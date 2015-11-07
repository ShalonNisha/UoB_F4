package tag_trends;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import tag_trends.connection.HttpURL;
import tag_trends.parser.Country;
import tag_trends.parser.Trends;

public class Test {
	static HttpURL con = null;
	static Map<String,Integer> s;
	public static void main(String[] args) {
		s = Country.loadCountries();
		con = new HttpURL();
		findTags("2015-10-1", "2015-10-31", "United Kingdom/Birmingham");

	}

	public static void findTags(String inStartDate, String inEndDate, String country) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date startDate = formatter.parse(inStartDate);
			Date endDate = formatter.parse(inEndDate);

			Calendar start = Calendar.getInstance();
			start.setTime(startDate);
			Calendar end = Calendar.getInstance();
			end.setTime(endDate);

			
			for (Date date = start.getTime(); start.before(end); start.add(
					Calendar.DATE, 1), date = start.getTime()) {
				String strDate = formatter.format(date);
				System.out.println(Trends.parse(con.getTagsForDate(s.get(country),
						strDate)));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
