package com.karrostech.tool.model;

import com.google.gson.Gson;

public class AvlEvent {
    private String id;
    private String busid;
    private String tenantId;
    private String vehicleId;
    private long databaseinsertiontime;
    private int eventid;
    private long eventtime;
    private String geom;
    private double heading;
    private double latitude;
    private double longitude;
    private double hdop;
    private int mileage;
    private int speed;
    private String gear;
    private String unitid;
    private String unitvendorid;
    private String additionaldata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public long getDatabaseinsertiontime() {
        return databaseinsertiontime;
    }

    public void setDatabaseinsertiontime(long databaseinsertiontime) {
        this.databaseinsertiontime = databaseinsertiontime;
    }

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public long getEventtime() {
        return eventtime;
    }

    public void setEventtime(long eventtime) {
        this.eventtime = eventtime;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getHdop() {
        return hdop;
    }

    public void setHdop(double hdop) {
        this.hdop = hdop;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getUnitvendorid() {
        return unitvendorid;
    }

    public void setUnitvendorid(String unitvendorid) {
        this.unitvendorid = unitvendorid;
    }

    public String getAdditionaldata() {
        return additionaldata;
    }

    public void setAdditionaldata(String additionaldata) {
        this.additionaldata = additionaldata;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
