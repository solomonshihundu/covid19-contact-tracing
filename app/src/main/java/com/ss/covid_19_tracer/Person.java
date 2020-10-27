package com.ss.covid_19_tracer;

public class Person
{

    private String phone;
    private String latitude;
    private String longitude;

    public Person( String phone, String latitude, String longitude)
    {
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
