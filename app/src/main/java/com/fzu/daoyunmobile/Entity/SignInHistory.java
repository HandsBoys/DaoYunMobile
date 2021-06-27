package com.fzu.daoyunmobile.Entity;

public class SignInHistory {
    private String signID;
    private String startTime;
    private String endTime;
    private String lng;
    private String lat;
    private boolean isFinish;
    private String dateType;
    private String conDate;

    public SignInHistory(String signID, String startTime, String endTime, String lng, String lat, boolean isFinish) {
        this.signID = signID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lat = lat;
        this.lng = lng;
        this.isFinish = isFinish;
    }


    public String getSignID() {
        return signID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getLng() {
        return lng;
    }

    public String getLat() {
        return lat;
    }

    public boolean getIsFinish() {
        return isFinish;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getConDate() {
        return conDate;
    }

    public void setConDate(String conDate) {
        this.conDate = conDate;
    }
}
