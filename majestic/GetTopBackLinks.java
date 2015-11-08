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

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.datalab.api.AttributeColumnsController;
import org.gephi.datalab.api.datatables.DataTablesController;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.openide.util.Lookup;

import com.majesticseo.external.rpc.APIService;
import com.majesticseo.external.rpc.DataTable;
import com.majesticseo.external.rpc.Response;

import tag_trends.parser.Country;
import utils.Service;

public class GetTopBackLinks {

	public static ArrayList<ArrayList<String>> getUrls(List<String> hastags, APIService apiService) {
		ArrayList<ArrayList<String>> urls = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < hastags.size(); i++) {
			ArrayList<String> tempurls = new ArrayList<String>();
			// SearchByKeyword
			Map<String, String> parameters = new LinkedHashMap<String, String>();
			parameters.put("query", hastags.get(i));
			parameters.put("scope", "0");
			parameters.put("count", "10");
			// parameters.put("datasource", "fresh");
			Response SearchByKeywordresponse = apiService.executeCommand("SearchByKeyword", parameters);
			// check the response code
			if (SearchByKeywordresponse.isOK()) {

				// print the URL table
				DataTable results = SearchByKeywordresponse.getTableForName("Results");
				System.out.println(results.getRowCount());

				for (Map<String, String> row : results.getTableRows()) {

					tempurls.add(row.get("Item"));
					// if(row.get("Name").equals("TotalLinks"))
					// for (Entry<String, String> e : row.entrySet()) {
					// System.out.println(e.getKey() + " : " + e.getValue());
					//
					// }
				}

			} else {
				System.out.println("\nERROR MESSAGE:");
				System.out.println(SearchByKeywordresponse.getErrorMessage());

				System.out.println(
						"\n\n***********************************************************" + "*****************");

			}

			urls.add(tempurls);
		}

		return urls;
	}

	public static int getNewBackLinks(String url, APIService apiService, String from, String to) {
		int numNewBackLink = 0;

		Map<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put("item", url);
		parameters.put("Mode", "0");
		parameters.put("Dateform", from);
		parameters.put("Dateto", to);
		parameters.put("datasource", "fresh");
		Response response = apiService.executeCommand("GetNewLostBackLinks", parameters);

		// check the response code
		if (response.isOK()) {

			// print the URL table
			DataTable results = response.getTableForName("BackLinks");
			numNewBackLink = results.getRowCount();
			// for (Map<String, String> row : results.getTableRows()) {
			//
			//
			// }

		} else {
			System.out.println("\nERROR MESSAGE:");
			System.out.println(response.getErrorMessage());

			System.out.println("\n\n***********************************************************" + "*****************");

		}

		return numNewBackLink;
	}

	public static void creatGraph() {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// Get controllers and models
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);

		// Import file
		Container container = null;
		File file = new File("Edges.csv");
		try {
			container = importController.importFile(file);
			container.getLoader().setEdgeDefault(EdgeDefault.UNDIRECTED);
			container.setAllowAutoNode(false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		// att
		File attfile = new File("att.csv");
		AttributeColumnsController ac = Lookup.getDefault().lookup(AttributeColumnsController.class);
		String[] columnsNames = { "Id", "backLinks", "label", "type" };
		AttributeType[] columnTypes = { AttributeType.STRING, AttributeType.INT, AttributeType.STRING,
				AttributeType.INT };
		ac.importCSVToNodesTable(attfile, ';', StandardCharsets.UTF_8, columnsNames, columnTypes, false);
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();

		Lookup.getDefault().lookup(DataTablesController.class).refreshCurrentTable();

		RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);

		// labels

		//int maxsize = Integer.MIN_VALUE , minsize = Integer.MAX_VALUE;
		
		{
			GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
			Graph graph = graphModel.getGraph();

			for (Node n : graph.getNodes()) {
//				int nodeSize = Integer.parseInt((n.getNodeData().getAttributes().getValue("backLinks").toString()));
//				if (nodeSize > maxsize) {
//					maxsize = nodeSize;
//				}
//				if (nodeSize < minsize) {
//					minsize = nodeSize;
//				}
				

				n.getNodeData().setLabel(n.getNodeData().getAttributes().getValue("label").toString());
				// getAttributes().setValue(dateColumn.getIndex(),
				// randomDataValue);
			}
		}

		// Rank size by backlinks
		AttributeColumn backLinksColumn = attributeModel.getNodeTable().getColumn("backLinks");
		Ranking fitnessRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, backLinksColumn.getId());
		AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel()
				.getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
		//System.out.println(minsize + " " + maxsize);
		sizeTransformer.setMinSize(5);
		sizeTransformer.setMaxSize(90);
		rankingController.transform(fitnessRanking, sizeTransformer);

		// ================Rank color by types========================

		AttributeColumn typesColumn = attributeModel.getNodeTable().getColumn("type");

		System.out.println("coulns id " + attributeModel.getNodeTable().getColumn("type"));
		Ranking typesRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, typesColumn.getId());
		AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController.getModel()
				.getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR);
		// Color[]
		// cls={Color.RED,Color.PINK,Color.ORANGE,Color.BLUE,Color.DARK_GRAY,Color.LIGHT_GRAY};

		colorTransformer.setColors(new Color[] { new Color(0xFEF0D9), new Color(0xB30000) });
		rankingController.transform(typesRanking, colorTransformer);

		// Run YifanHuLayout for 100 passes - The layout always takes the
		// current visible view

		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();

		YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
		layout.setGraphModel(graphModel);
		layout.initAlgo();
		layout.resetPropertiesValues();
		layout.setOptimalDistance(200f);

		for (int i = 0; i < 100 && layout.canAlgo(); i++) {
			layout.goAlgo();
		}
		layout.endAlgo();

		// layout

		FruchtermanReingold firstLayout = new FruchtermanReingold(null);
		firstLayout.setGraphModel(graphModel);

		firstLayout.setArea((float) graphModel.getGraph().getNodeCount() * 10000);
		firstLayout.setSpeed(50.0);
		firstLayout.setGravity(5.0);
		int cnt = 0;

		do {
			cnt++;
			firstLayout.goAlgo();
		} while (!firstLayout.isConverged() && firstLayout.canAlgo() && cnt < 1000000);
		System.out.println("cnt: " + cnt);

		// ================================Preview
		// configuration======================================/
		PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
		PreviewModel previewModel = previewController.getModel();

		previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
		previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT,
				previewModel.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont((float) 108.5));
		previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.FALSE);
		previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR,
				new DependantOriginalColor(Color.BLACK));
		previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.TRUE);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 100);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 10f);
		previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE);
		previewController.refreshPreview();

		// ===============================Export===========================//
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);

		try {
			ec.exportFile(new File("graph.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// ======================== end layout=========================/
		try {
			ec.exportFile(new File("graph.gexf"));
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
	}

	public static void main(String[] args) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String endpoint = "http://enterprise.majesticseo.com/api_command";

		System.out.println("\nEndpoint: " + endpoint);
		String app_api_key = "FD99CA4B465C791995D204E86C7B9F9F";
		APIService apiService = new APIService(app_api_key, endpoint);

		// set up parameters
		// Map<String, String> parameters = new LinkedHashMap<String, String>();
		// parameters.put("MaxSourceURLs", "100");
		// parameters.put("URL", itemToQuery);
		// parameters.put("GetUrlData", "1");
		// parameters.put("MaxSourceURLsPerRefDomain", "1");
		// parameters.put("datasource", "fresh");
		//
		// Response response = apiService.executeCommand("GetTopBackLinks",
		// parameters);
		// parameters.put("MaxSourceURLs", "10");
		// parameters.put("item0", itemToQuery);
		//// parameters.put("GetUrlData", "1");
		//// parameters.put("MaxSourceURLsPerRefDomain", "1");
		// parameters.put("datasource", "fresh");
		// Response response = apiService.executeCommand("GetBackLinksHistory",
		// parameters);
		// parameters.put("item", itemToQuery);
		// parameters.put("Mode", 1);
		// Response response = apiService.executeCommand("GetNewLostBackLinks",
		// parameters);

		String from = "2015-08-15";
		String to = "2015-11-08";
		Country.loadCountries();
		List<String> hastags = Service.findTags(from, to, "Worldwide");// new
																		// ArrayList<String>();
																		// "Worldwide"

		int totalnumUrls = 0;
		// String itemToQuery = "www.birmingham.ac.uk/";

		ArrayList<ArrayList<String>> urls = getUrls(hastags, apiService);
		// ArrayList<ArrayList<Integer>> urlsBackLinks = new
		// ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> urlsBackLinks = new ArrayList<Integer>();
		ArrayList<String> url2 = new ArrayList<String>();
		for (int i = 0; i < urls.size(); i++) {
			totalnumUrls += urls.get(i).size();

			// ArrayList<Integer> temp = new ArrayList<Integer>();
			for (int j = 0; j < urls.get(i).size(); j++) {
				url2.add(urls.get(i).get(j));
				urlsBackLinks.add(getNewBackLinks(urls.get(i).get(j), apiService, from, to));
				// System.out.println(getNewBackLinks(urls.get(i).get(j),
				// apiService, from, to));
				// System.out.println( urls.get(i).get(j));
			}
			// urlsBackLinks.add(temp);
		}
		int edgesSize = hastags.size() + totalnumUrls;
		int[][] edges = new int[edgesSize][edgesSize];
		// int ids=0;
		int columnStart = hastags.size();
		for (int i = 0; i < hastags.size(); i++) {
			for (int j = columnStart; j < columnStart + urls.get(i).size(); j++)
				edges[i][j] = edges[j][i] = 1;
			columnStart += urls.get(i).size();
		}

		try {
			PrintWriter pw = new PrintWriter("Edges.csv");
			pw.print(";");

			for (int i = 0; i < edgesSize; i++)
				pw.print(i + ";");
			pw.println();

			for (int i = 0; i < edgesSize; i++) {
				pw.print(i + ";");
				for (int j = 0; j < edgesSize; j++)
					pw.print(edges[i][j] + ";");

				pw.println();
			}

			pw.flush();
			pw.close();

			PrintWriter pw1 = new PrintWriter("att.csv");
			pw1.println("Id;backLinks;label;type");
			for (int i = 0; i < hastags.size(); i++)
				pw1.println(i + ";" + 2 + ";" + hastags.get(i) + ";" + 1);

			for (int i = hastags.size(); i < edgesSize; i++)
				pw1.println(i + ";" + Math.log10((urlsBackLinks.get(i - hastags.size())) ) + ";"
						+ url2.get(i - hastags.size()) + ";" + 2);

			pw1.flush();
			pw1.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		creatGraph();
		// for (int i = 0; i < hastags.size(); i++) {
		//
		// //SearchByKeyword
		// Map<String, String> parameters = new LinkedHashMap<String, String>();
		// parameters.put("query", hastags.get(i));
		// parameters.put("scope", "0");
		// parameters.put("count", "10");
		// Response SearchByKeywordresponse =
		// apiService.executeCommand("SearchByKeyword", parameters);
		// // check the response code
		// if (SearchByKeywordresponse.isOK()) {
		//
		// // print the URL table
		// DataTable results =
		// SearchByKeywordresponse.getTableForName("Results");//("BackLinks");//("item0");
		// System.out.println(results.getRowCount());
		//
		// for (Map<String, String> row : results.getTableRows()) {
		// // if(row.get("Name").equals("TotalLinks"))
		// for (Entry<String, String> e : row.entrySet()) {
		// System.out.println(e.getKey() + " : " + e.getValue());
		//
		// }
		//
		// }
		//
		// } else {
		// System.out.println("\nERROR MESSAGE:");
		// System.out.println(SearchByKeywordresponse.getErrorMessage());
		//
		// System.out.println("\n\n***********************************************************"
		// + "*****************");
		//
		// System.out.println("\nDebugging Info:");
		// System.out.println("\n Endpoint: \t" + endpoint);
		// System.out.println(" API Key: \t" + app_api_key);
		// System.out.println("\n***********************************************************"
		// + "*****************");
		// }
		//
		// }
	}

}
