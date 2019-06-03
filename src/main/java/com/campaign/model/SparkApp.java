/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campaign.model;

/**
 *
 * @author vtt-cntt-l-51
 */
public class SparkApp {

    private final String sparkServerIP;
    private final String user;
    private final String name;
    private final String status;
    private final String url;
    private final String actualUrl;
    private final String streamingUrl;

    public SparkApp(String sparkServerIP, String user, String name, String status, String url) {
        this.sparkServerIP = sparkServerIP;
        this.user = user;
        this.name = name;
        this.status = status;
        this.url = url;
        this.actualUrl = url.replaceAll("http:\\/\\/.*:", "http://" + this.sparkServerIP + ":");
        this.streamingUrl = actualUrl + "streaming";
    }

    public String getSparkServerIP() {
        return sparkServerIP;
    }

    public String getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getUrl() {
        return url;
    }

    public String getActualUrl() {
        return actualUrl;
    }

    public String getStreamingUrl() {
        return streamingUrl;
    }

    @Override
    public String toString() {
        return name + " - Spark Streaming URL: " + streamingUrl + "\n";
    }

}
