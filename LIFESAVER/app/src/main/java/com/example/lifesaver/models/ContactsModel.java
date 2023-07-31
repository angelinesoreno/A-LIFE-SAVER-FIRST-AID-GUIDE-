package com.example.lifesaver.models;

public class ContactsModel {
    String locationName;
    String hotlineNumber;
    String address;

    String locationLatitude;

    String locationLongitude;

    public ContactsModel() {

    }

    public ContactsModel(String locationName, String hotlineNumber, String address, String locationLatitude, String locationlongitude) {
        this.locationName = locationName;
        this.hotlineNumber = hotlineNumber;
        this.address = address;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }


    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getHotlineNumber() {
        return hotlineNumber;
    }

    public void setHotlineNumber(String hotlineNumber) {
        this.hotlineNumber = hotlineNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

}
