package com.weilian.covid19.config;

public class Header {

    private String origin;

    private String referer;

    private String userAgent;

    public Header(String origin, String referer, String userAgent){

        this.origin = origin;
        this.referer = referer;
        this.userAgent = userAgent;
    }


    public String getOrigin() {
        return origin;
    }

    public String getReferer() {
        return referer;
    }

    public String getUserAgent() {
        return userAgent;
    }



    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return "origin=" + origin + "&" +
                "referer=" + referer + "&" +
                "user-agent=" + userAgent;
    }
}
