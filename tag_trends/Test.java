package tag_trends;

import tag_trends.connection.HttpURL;
import tag_trends.parser.Country;
import tag_trends.parser.Trends;

public class Test {
	public static void main(String[] args) {
		Country.loadCountries();	
		HttpURL con = new HttpURL();
		System.out.println(Trends.parse(con.getTagsForDate(1, "2015-05-06")));
	}
}
