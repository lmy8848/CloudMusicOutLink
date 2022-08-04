/*
 * Copyright (c) 2022. @QJ315
 * github账户:https://github.com/lmy8848
 * User:NJQ-PC
 * File:MusicList.java
 * Date:2022/07/23 21:15:23
 */

package com.qj315.cloudmusicoutlink;

public class MusicList {
    private Integer id;
    private String music_url;
    private String musicName;

    public MusicList() {
    }

    public MusicList(Integer id, String music_url, String musicName) {
        this.id = id;
        this.music_url = music_url;
        this.musicName = musicName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMusic_url() {
        return music_url;
    }

    public void setMusic_url(String music_url) {
        this.music_url = music_url;
    }

    public String getMusic_name() {
        return musicName;
    }

    public void setMusic_name(String musicName) {
        this.musicName = musicName;
    }

    @Override
    public String toString() {
        return "MusicList{" +
                "id=" + id +
                ", music_url='" + music_url + '\'' +
                ", musicName='" + musicName + '\'' +
                '}';
    }
}
