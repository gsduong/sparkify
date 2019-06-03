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
public class Batch {

    private final String batchTime;
    private final String inputSize;
    private final String scheduleDelay;
    private final String processingTime;
    private final String totalDelay;
    private final String appName;

    public Batch(String batchTime, String inputSize, String scheduleDelay, String processingTime, String totalDelay, String appName) {
        this.batchTime = batchTime;
        this.inputSize = inputSize;
        this.scheduleDelay = scheduleDelay;
        this.processingTime = processingTime;
        this.totalDelay = totalDelay;
        this.appName = appName;
    }

    public String getBatchTime() {
        return batchTime;
    }

    public String getInputSize() {
        return inputSize;
    }

    public String getScheduleDelay() {
        return scheduleDelay;
    }

    public String getProcessingTime() {
        return processingTime;
    }

    public String getTotalDelay() {
        return totalDelay;
    }

    public String getAppName() {
        return appName;
    }

    @Override
    public String toString() {
        return "<p><b>" + appName + "</b></p><table style=\"border:1px solid black\"><thead style=\"color:green\"><tr><th style=\"border:1px solid black\">Batch Time</th><th style=\"border:1px solid black\">Input Size</th><th style=\"border:1px solid black\">Scheduling Delay</th><th style=\"border:1px solid black\">Processing Time</th><th style=\"border:1px solid black\">Total Delay</th></thead><tbody style=\"color:blue\"><tr><td style=\"border:1px solid black\">" + batchTime + "</td><td style=\"border:1px solid black\">" + inputSize + "</td><td style=\"border:1px solid black\">" + scheduleDelay + "</td><td style=\"border:1px solid black\">" + processingTime + "</td><td style=\"border:1px solid black\">" + totalDelay + "</td></tbody></table>";
    }

}
