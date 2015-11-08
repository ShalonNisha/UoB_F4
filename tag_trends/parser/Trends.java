package tag_trends.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trends {
	private static final Pattern TAG_REGEX_01 = Pattern
			.compile("/trend/(.+?)</a>");

	public static List<String> parse(String response) {
		Matcher matcher = TAG_REGEX_01.matcher(response);
		List<String> tags = new LinkedList<String>();
		while (matcher.find())
			tags.add(matcher.group().split(" ")[1].replaceAll("</a>", "").replace("#", ""));
		return tags;
	}
}
