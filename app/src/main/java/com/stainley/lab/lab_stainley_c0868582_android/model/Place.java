package com.stainley.lab.lab_stainley_c0868582_android.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "FAVORITE_PLACE")
public class Place implements Serializable {

    @PrimaryKey
    private Long id;
    private String city;
    @ColumnInfo(name = "postal_code")
    private String postalCode;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
