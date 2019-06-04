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

        if (args.length < 4) {
            System.out.println("Invalid number of arguments!");
            return "Wrong configuration of ELT Job. Check parameters list of the JAR entry!";
//            return;
        }

        String IP = args[0];
        String PORT = args[1];
        String queueString = args[2];
        String[] queues = queueString.split(",");

        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> listAppName = new ArrayList<>();
        ArrayList<SparkApp> list = new ArrayList<>();

        for (int i = 3; i < args.length; i++) {
            listAppName.add(args[i]);
            System.out.println(args[i]);
        }

        for (String queue : queues) {
            System.out.println("Retrieving queue: " + queue);
            String URL = new StringBuilder().append("http://").append(IP).append(":").append(PORT).append("/ws/v1/cluster/apps?states=running&applicationTypes=SPARK&queue=").append(queue).toString();
            ApiResponse sparkAppResponse;
            sparkAppResponse = JsonService.getApiResponse(URL);
            if (sparkAppResponse == null || sparkAppResponse.getResponseCode() != 200) {
                System.out.println("Could not connect to " + URL);
                break;
//                return "Could not connect to " + URL;
//                return;
            }
            JSONObject response = sparkAppResponse.getJsonResponse();
            if (!response.has("apps") || !(response.get("apps") instanceof JSONObject)) {
                break;
            }
            JSONArray apps = response.getJSONObject("apps").getJSONArray("app");
            System.out.println("apps.length = " + apps.length());
            for (int i = 0; i < apps.length(); i++) {
                JSONObject e = (JSONObject) apps.get(i);

                if (listAppName.contains(e.getString("name"))) {
                    System.out.println(new SparkApp(IP, e.getString("user"), e.getString("name"), e.getString("state"), e.getString("trackingUrl")).toString());
                    list.add(new SparkApp(IP, e.getString("user"), e.getString("name"), e.getString("state"), e.getString("trackingUrl")));
                }
            }
        }

        if (list.isEmpty()) {
            System.out.println("There are no running streaming app");
            return "There are no running streaming app";
//            return;
        }
        // use jsoup to get latest batch
        for (SparkApp sparkApp : list) {
            // use jsoup to get latest batch
            Document doc = Jsoup.connect(sparkApp.getStreamingUrl()).get();
            Element lastBatchElement = doc.selectFirst("#completed-batches-table > tbody > tr:eq(0)");

            String batchTime = lastBatchElement.select("td:eq(0) > a").text();
            String inputSize = lastBatchElement.select("td:eq(1)").text();
            String schedulingDelay = lastBatchElement.select("td:eq(2)").text();
            String processingTime = lastBatchElement.select("td:eq(3)").text();
            String totalDelay = lastBatchElement.select("td:eq(4)").text();
            result.add(new Batch(batchTime, inputSize, schedulingDelay, processingTime, totalDelay, sparkApp.getName()).toString());
        }

        System.out.println(String.join(" ", result));

        return String.join(" ", result);
    }
}
