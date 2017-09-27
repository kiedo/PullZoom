package com.example.apple.orm.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.w3c.dom.ProcessingInstruction;

/**
 * Created by apple on 17/6/26.
 */
@Entity
public class User {

    @Id
    private Long id;

    @Property(nameInDb = "USERNAME")
    private String username;

    @Property(nameInDb = "NICKNAME")
    private String nickname;

    @Property(nameInDb = "uu")
    private String uname;

    @Property
    private String A;

    @Generated(hash = 1405214221)
    public User(Long id, String username, String nickname, String uname, String A) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.uname = uname;
        this.A = A;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUname() {
        return this.uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getA() {
        return this.A;
    }

    public void setA(String A) {
        this.A = A;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", uname='" + uname + '\'' +
                ", A='" + A + '\'' +
                '}';
    }
}
