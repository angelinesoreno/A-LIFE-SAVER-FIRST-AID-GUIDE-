package com.example.lifesaver.models;

import java.io.Serializable;

public class EmergencyModel implements Serializable {

    String sakitImage;
    String sakitName;

    String sakitDescription;

    String link;

    String remedies;

    public EmergencyModel() {
        // Empty constructor required
    }


    public EmergencyModel(String sakitImage, String sakitName, String sakitDescription, String link) {
        this.sakitImage = sakitImage;
        this.sakitName = sakitName;
        this.sakitDescription = sakitDescription;
        this.link = link;
        this.remedies = remedies;
    }

    public String getSakitImage() {
        return sakitImage;
    }

    public void setSakitImage(String sakitImage) {
        this.sakitImage = sakitImage;
    }

    public String getSakitName() {
        return sakitName;
    }

    public void setSakitName(String sakitName) {
        this.sakitName = sakitName;
    }

    public String getSakitDescription() {
        return sakitDescription;
    }

    public void setSakitDescription(String sakitDescription) {
        this.sakitDescription = sakitDescription;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRemedies() {
        return remedies;
    }

    public void setRemedies(String remedies) {
        this.remedies = remedies;
    }
}
