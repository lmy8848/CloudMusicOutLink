/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:Location.java
 * Date:2022/09/06 19:34:06
 */

package com.qj315.cloudmusicoutlink.bean;

import java.sql.Timestamp;

public class Location {
    private Integer id;
    private String ip;
    private String address;
    private Timestamp logins;

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", address='" + address + '\'' +
                ", logins=" + logins +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getLogins() {
        return logins;
    }

    public void setLogins(Timestamp logins) {
        this.logins = logins;
    }
}
