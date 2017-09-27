package com.example.apple.orm.bean.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by apple on 17/6/27.
 */
@Entity
public class Car {
    @Id
    private  Long _id;
    @Property
    private String name;
    @Property
    private String color;
    @Generated(hash = 1671945016)
    public Car(Long _id, String name, String color) {
        this._id = _id;
        this.name = name;
        this.color = color;
    }
    @Generated(hash = 625572433)
    public Car() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getColor() {
        return this.color;
    }
    public void setColor(String color) {
        this.color = color;
    }
}
