package com.example.wassim.sprinkle.beans;

import android.support.annotation.Nullable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

/**
 * Created by wassim on 11/19/16.
 */

@Entity
public class Plant {

    @Id
    private Long id;

    private long createdAt;
    private long finishesAt;
    private long lastWateringAt;

    @NotNull
    private String name;
    private int wateringFrequency;

    public Plant(Long id, String name, int wateringFrequency) {
        this.id = id;
        this.name = name;
        this.wateringFrequency = wateringFrequency;
    }

    @Generated(hash = 943005619)
    public Plant(Long id, long createdAt, long finishesAt, long lastWateringAt,
            @NotNull String name, int wateringFrequency) {
        this.id = id;
        this.createdAt = createdAt;
        this.finishesAt = finishesAt;
        this.lastWateringAt = lastWateringAt;
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

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getFinishesAt() {
        return this.finishesAt;
    }

    public void setFinishesAt(long finishesAt) {
        this.finishesAt = finishesAt;
    }

    public long getLastWateringAt() {
        return this.lastWateringAt;
    }

    public void setLastWateringAt(long lastWateringAt) {
        this.lastWateringAt = lastWateringAt;
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
