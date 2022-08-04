/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:ResponseBean.java
 * Date:2022/08/04 19:34:04
 */

package com.qj315.cloudmusicoutlink.bean;

public class ResponseBean {
    private Integer code;
    private String url;
    private String token;

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code=" + code +
                ", url='" + url + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
