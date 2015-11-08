package tag_trends;

import java.util.Map;

import tag_trends.connection.HttpURL;
import tag_trends.parser.Country;
import utils.Service;

public class Test {
	static HttpURL con = null;
	static Map<String, Integer> s;

	public static void main(String[] args) {
		s = Country.loadCountries();
		con = new HttpURL();
		System.out.println((Service.findTags("2015-10-1", "2015-10-4",
				"United Kingdom/Birmingham")));
	}

}
