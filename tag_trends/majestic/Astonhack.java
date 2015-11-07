/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tagtrends.astonhack;

import java.net.*;
import java.io.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author axj
 */
public class Astonhack {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            String key = "74C220F03D158D8F2574A1AFF11FB340";
            URL test = new URL("http://developer.majestic.com/api/json?app_api_key=" + key + "&cmd=GetNewLostBackLinkCalendar&datasource=fresh&item=msn.com");
            URLConnection yc = test.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));

   
            String inputLine;
            String jsonString = "";
            while ((inputLine = in.readLine()) != null)
                jsonString += inputLine;
            System.out.println(jsonString);
            JSONObject obj = new JSONObject(jsonString);
            System.out.println(obj.getString("ServerName"));
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
