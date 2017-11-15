package com.personal.neycasilla.dangerousroutes.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Ney Casilla on 8/11/2017.
 * modelo de zonas peligrosas
 */

public class DangerZone implements Serializable {
    private String key;
    private Double latitude;
    private Double longitud;
    private Date dateMarked;
    private String dangerName;
    private String sector;
    private String provincia;
    private String image;

    public String getDangerName() {
        return dangerName;
    }

    public void setDangerName(String dangerName) {
        this.dangerName = dangerName;
    }

    public DangerZone() {
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getImage() {
        return image;
    }

    public void setImageUrls(String image) {
        this.image = image;
    }

    public Date getDateMarked() {
        return dateMarked;
    }

    public void setDateMarked(Date dateMarked) {
        this.dateMarked = dateMarked;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
