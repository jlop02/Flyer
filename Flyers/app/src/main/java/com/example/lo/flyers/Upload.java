package com.example.lo.flyers;

public class Upload {
    private String title;
    private String imageUrl;
    private String time;
    private String location;
    private String details;
    private String lat;
    private String lng;
    private String timeS;
    private String timeB;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLat(){return lat;}

    public void setLat(String lat){this.lat=lat;}

    public String getLng(){return lng;}

    public void setLng(String lng){this.lng = lng;}

    public String getTimeS() { return timeS; }

    public void setTimeS(String timeS) { this.timeS = timeS; }

    public String getTimeB() { return timeB; }

    public void setTimeB(String timeB) { this.timeB = timeB; }



    public Upload() {

    }
    public Upload(String title, String details, String time, String location, String imageUrl, String lat, String lng, String timeS, String timeB ) {
        this.title = title;
        this.details = details;
        this.location = location;
        this.time = time;
        this.imageUrl = imageUrl;
        this.lat = lat;
        this.lng = lng;
        this.timeS = timeS;
        this.timeB = timeB;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
