package com.example.wassim.sprinkle.beans;

import com.example.wassim.sprinkle.extras.DateHelper;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * Created by wassim on 11/19/16.
 */

@Entity
public class Plant {

    @Id
    private Long id;

    private long lastWateringAt;
    private long createdAt;
    private String picturePath;

    @NotNull
    private String name;
    private int wateringFrequency;

    public Plant(Long id, String name, int wateringFrequency) {
        this.id = id;
        this.name = name;
        this.wateringFrequency = wateringFrequency;
        this.lastWateringAt = new Date().getTime();
        this.createdAt = this.lastWateringAt;
    }

    public Plant(Long id, String name, int wateringFrequency, int picturePath) {
        this.id = id;
        this.name = name;
        this.wateringFrequency = wateringFrequency;
        this.lastWateringAt = new Date().getTime();
        this.createdAt = this.lastWateringAt;
        this.picturePath = "plant_"+picturePath;
    }

    @Generated(hash = 1773069330)
    public Plant(Long id, long lastWateringAt, long createdAt, String picturePath,
            @NotNull String name, int wateringFrequency) {
        this.id = id;
        this.lastWateringAt = lastWateringAt;
        this.createdAt = createdAt;
        this.picturePath = picturePath;
        this.name = name;
        this.wateringFrequency = wateringFrequency;
    }

    @Generated(hash = 878011190)
    public Plant() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getLastWateringAt() {
        return this.lastWateringAt;
    }

    public void setLastWateringAt(long lastWateringAt) {
        this.lastWateringAt = lastWateringAt;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getPicturePath() {
        return this.picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWateringFrequency() {
        return this.wateringFrequency;
    }

    public void setWateringFrequency(int wateringFrequency) {
        this.wateringFrequency = wateringFrequency;
    }
}
