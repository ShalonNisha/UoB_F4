/*
 * Version 0.9.3
 *
 * Copyright (c) 2011, Majestic-12 Ltd
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the Majestic-12 Ltd nor the
 *      names of its contributors may be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Majestic-12 Ltd BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

/* NOTE: The code below is specifically for the GetTopBackLinks API command.
 *       For other API commands, the arguments required may differ.
 *       Please refer to the Majestic SEO Developer Wiki for more information
 *       regarding other API commands and their arguments.
 */
package majestic;

import com.majesticseo.external.rpc.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class GetTopBackLinks {

    public static ArrayList< ArrayList<String>> getUrls(ArrayList<String> hastags, APIService apiService) {
        ArrayList< ArrayList<String>> urls = new ArrayList< ArrayList<String>>();
        for (int i = 0; i < hastags.size(); i++) {
            ArrayList<String> tempurls = new ArrayList<String>();
            //SearchByKeyword
            Map<String, String> parameters = new LinkedHashMap<String, String>();
            parameters.put("query", hastags.get(i));
            parameters.put("scope", "0");
            parameters.put("count", "10");
            //parameters.put("datasource", "fresh");
            Response SearchByKeywordresponse = apiService.executeCommand("SearchByKeyword", parameters);
            // check the response code
            if (SearchByKeywordresponse.isOK()) {

                // print the URL table
                DataTable results = SearchByKeywordresponse.getTableForName("Results");
                System.out.println(results.getRowCount());

                for (Map<String, String> row : results.getTableRows()) {

                    tempurls.add(row.get("Item"));
                    // if(row.get("Name").equals("TotalLinks"))
//                    for (Entry<String, String> e : row.entrySet()) {
//                        System.out.println(e.getKey() + " : " + e.getValue());
//
//                    }
                }

            } else {
                System.out.println("\nERROR MESSAGE:");
                System.out.println(SearchByKeywordresponse.getErrorMessage());

                System.out.println("\n\n***********************************************************"
                        + "*****************");

            }

            urls.add(tempurls);
        }

        return urls;
    }

    public static int getNewBackLinks(String url, APIService apiService, String from, String to)
    {
        int numNewBackLink=0;

        Map<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put("item", url);
        parameters.put("Mode", "0");
          parameters.put("Dateform",from);
            parameters.put("Dateto", to);
          //  parameters.put("datasource", "fresh");
        Response response = apiService.executeCommand("GetNewLostBackLinks", parameters);

        // check the response code
        if (response.isOK()) {

            // print the URL table
            DataTable results = response.getTableForName("BackLinks");
            numNewBackLink = results.getRowCount();
//                for (Map<String, String> row : results.getTableRows()) {
//                   
//                        
//                }

        } else {
            System.out.println("\nERROR MESSAGE:");
            System.out.println(response.getErrorMessage());

            System.out.println("\n\n***********************************************************"
                    + "*****************");

        }

        return numNewBackLink;
    }

    public static void main(String[] args) {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String endpoint = "http://enterprise.majesticseo.com/api_command";

        System.out.println("\nEndpoint: " + endpoint);
        String app_api_key = "35F61E1409AEAFBA452B99CFA8D454B2";
        APIService apiService = new APIService(app_api_key, endpoint);

        // set up parameters
//            Map<String, String> parameters = new LinkedHashMap<String, String>();
//            parameters.put("MaxSourceURLs", "100");
//            parameters.put("URL", itemToQuery);
//            parameters.put("GetUrlData", "1");
//            parameters.put("MaxSourceURLsPerRefDomain", "1");
        // parameters.put("datasource", "fresh");
//            
//            Response response = apiService.executeCommand("GetTopBackLinks", parameters);
//            parameters.put("MaxSourceURLs", "10");
//            parameters.put("item0", itemToQuery);
////            parameters.put("GetUrlData", "1");
////            parameters.put("MaxSourceURLsPerRefDomain", "1");
//           parameters.put("datasource", "fresh");
//            Response response = apiService.executeCommand("GetBackLinksHistory", parameters);
//        parameters.put("item", itemToQuery);
//        parameters.put("Mode", 1);
//        Response response = apiService.executeCommand("GetNewLostBackLinks", parameters);
        ArrayList<String> hastags = new ArrayList<String>();
        hastags.add("Quran");
        hastags.add("yolo");
//String itemToQuery = "www.birmingham.ac.uk/";
        String from="2015-09-08";
        String to="2015-10-08";
        ArrayList< ArrayList<String>> urls = getUrls(hastags, apiService);
        for (int i = 0; i < urls.size(); i++) {
            for (int j = 0; j < urls.get(i).size(); j++) {
                ;
                System.out.println(getNewBackLinks(urls.get(i).get(j),apiService,from,to));
                //System.out.println( urls.get(i).get(j));
            }
        }

//        for (int i = 0; i < hastags.size(); i++) {
//
//            //SearchByKeyword
//            Map<String, String> parameters = new LinkedHashMap<String, String>();
//            parameters.put("query", hastags.get(i));
//            parameters.put("scope", "0");
//            parameters.put("count", "10");
//            Response SearchByKeywordresponse = apiService.executeCommand("SearchByKeyword", parameters);
//            // check the response code
//            if (SearchByKeywordresponse.isOK()) {
//
//                // print the URL table
//                DataTable results = SearchByKeywordresponse.getTableForName("Results");//("BackLinks");//("item0");
//                System.out.println(results.getRowCount());
//
//                for (Map<String, String> row : results.getTableRows()) {
//                    // if(row.get("Name").equals("TotalLinks"))
//                    for (Entry<String, String> e : row.entrySet()) {
//                        System.out.println(e.getKey() + " : " + e.getValue());
//
//                    }
//
//                }
//
//            } else {
//                System.out.println("\nERROR MESSAGE:");
//                System.out.println(SearchByKeywordresponse.getErrorMessage());
//
//                System.out.println("\n\n***********************************************************"
//                        + "*****************");
//
//                System.out.println("\nDebugging Info:");
//                System.out.println("\n  Endpoint: \t" + endpoint);
//                System.out.println("  API Key: \t" + app_api_key);
//                System.out.println("\n***********************************************************"
//                        + "*****************");
//            }
//
//        }
    }

}
