package tag_trends.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Country {

	public static Map<String, Integer> data = null;
	
	public static void parse(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					fileName)));
			String line = null;
			Map<String, Integer> countriesData = new HashMap<String, Integer>();

			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				countriesData.put(data[1], Integer.parseInt(data[0]));
			}

			br.close();
			File f = new File(".");
			System.out.println(f.getAbsolutePath());
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream("tag_trends/parser/countries.dat"));
			oos.writeObject(countriesData);
			oos.flush();
			oos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Integer> loadCountries() {
		ObjectInputStream oos;
		try {
			oos = new ObjectInputStream(new FileInputStream("trunk/tag_trends/parser/countries.dat"));
			data = (Map<String, Integer>) oos.readObject();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return data;
	}
}
