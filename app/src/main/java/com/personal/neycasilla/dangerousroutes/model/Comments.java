package com.personal.neycasilla.dangerousroutes.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ney Casilla on 8/10/2017.
 * modelo de usuario
 */

public class Comments implements Serializable {

    private String key;
    private String zoneKey;
    private String commentText;
    private Date dateMade;

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Date getDateMade() {
        return dateMade;
    }

    public void setDateMade(Date dateMade) {
        this.dateMade = dateMade;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getZoneKey() {
        return zoneKey;
    }

    public void setZoneKey(String zoneKey) {
        this.zoneKey = zoneKey;
    }
}
