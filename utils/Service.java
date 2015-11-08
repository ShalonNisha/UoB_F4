package utils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import tag_trends.connection.HttpURL;
import tag_trends.parser.Country;
import tag_trends.parser.Trends;

public class Service {
	static HttpURL con = null;
	static Map<String, Integer> s;
	static Map<String, Integer> tags;

	public static List<String> findTags(String inStartDate,
			String inEndDate, String country) {

		tags = new HashMap<String, Integer>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		con = new HttpURL();

		try {
			Date startDate = formatter.parse(inStartDate);
			Date endDate = formatter.parse(inEndDate);

			Calendar start = Calendar.getInstance();
			start.setTime(startDate);
			Calendar end = Calendar.getInstance();
			end.setTime(endDate);

			for (Date date = start.getTime(); !start.after(end); start.add(
					Calendar.DATE, 1), date = start.getTime()) {
				String strDate = formatter.format(date);
				int index = Country.data.get(country);
				String tmp_tags = con.getTagsForDate(index, strDate);
				add(Trends.parse(tmp_tags));
			}
			sort();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		List<String> topTags = new ArrayList<String>(tags.keySet()); 
		return topTags.subList(0, 10);
	}

	public static void add(List<String> in_tags) {
		for (String s : in_tags)
			tags.compute(s, (K, V) -> V == null ? 1 : V + 1);
	}

	public static void sort() {
		ValueComparator vc = new ValueComparator(tags);
		TreeMap<String, Integer> sorted = new TreeMap<String, Integer>(vc);
		sorted.putAll(tags);
		tags = sorted;
	}

}

class ValueComparator implements Comparator {
	Map<String, Integer> base;

	public ValueComparator(Map<String, Integer> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	public int compare(Object a, Object b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}

}
