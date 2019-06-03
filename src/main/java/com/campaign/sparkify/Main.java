/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campaign.sparkify;

import com.campaign.model.ApiResponse;
import com.campaign.model.Batch;
import com.campaign.model.SparkApp;
import com.campaign.service.JsonService;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author vtt-cntt-l-51
 */
public class Main {

    public static String main(String[] args) throws IOException {

        if (args.length != 3) {
            System.out.println("Invalid number of arguments!");
            return "Wrong configuration of ELT Job. Check parameters list of the JAR entry!";
        }

        String IP = args[0];
        String PORT = args[1];
        String appName = args[2];
        String URL = new StringBuilder().append("http://").append(IP).append(":").append(PORT).append("/ws/v1/cluster/apps?states=running&applicationTypes=SPARK").toString();
        ApiResponse sparkAppResponse;
        sparkAppResponse = JsonService.getApiResponse(URL);
        if (sparkAppResponse == null || sparkAppResponse.getResponseCode() != 200) {
            return "Could not connect to " + URL;
        }
        JSONObject response = sparkAppResponse.getJsonResponse();
        JSONArray apps = response.getJSONObject("apps").getJSONArray("app");

        ArrayList<SparkApp> list = new ArrayList<>();
        for (int i = 0; i < apps.length(); i++) {
            JSONObject e = (JSONObject) apps.get(i);

            if (e.getString("name").equals(appName)) {
                list.add(new SparkApp(IP, e.getString("user"), e.getString("name"), e.getString("state"), e.getString("trackingUrl")));
                break;
            }

        }
        
        if (list.size() == 0) {
            System.out.println(appName + " is currently offline");
            return appName + " is currently offline";
        }
        
        // use jsoup to get latest batch
        
        SparkApp offerBank = list.get(0);
        Document doc = Jsoup.connect(offerBank.getStreamingUrl()).get();
        Element lastBatchElement = doc.selectFirst("#completed-batches-table > tbody > tr:eq(0)");
        
        String batchTime = lastBatchElement.select("td:eq(0) > a").text();
        String inputSize = lastBatchElement.select("td:eq(1)").text();
        String schedulingDelay = lastBatchElement.select("td:eq(2)").text();
        String processingTime = lastBatchElement.select("td:eq(3)").text();
        String totalDelay = lastBatchElement.select("td:eq(4)").text();
        Batch latestBatch = new Batch(batchTime, inputSize, schedulingDelay, processingTime, totalDelay);
        return latestBatch.toString();
    }
}
