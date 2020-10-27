package com.ss.covid_19_tracer;

public class Person
{

    private String phone;
    private String latitude;
    private String longitude;
    private String covid_status;


    public Person(String phone, String latitude, String longitude, String covid_status)
    {
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.covid_status = covid_status;
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

    public String getCovid_status() {
        return covid_status;
    }

    public void setCovid_status(String covid_status) {
        this.covid_status = covid_status;
    }
}
