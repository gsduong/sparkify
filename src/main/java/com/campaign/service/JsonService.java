/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campaign.service;

import com.campaign.model.ApiResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;

/**
 *
 * @author vtt-cntt-l-51
 */
public class JsonService {

    public static ApiResponse getApiResponse(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new ApiResponse(responseCode, new JSONObject(response.toString()));
        } catch (MalformedURLException ex) {
//            Logger.getLogger(StormService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
//            Logger.getLogger(StormService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
