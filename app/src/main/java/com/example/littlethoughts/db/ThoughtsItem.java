package com.example.littlethoughts.db;

import org.litepal.crud.LitePalSupport;

import java.sql.Date;

public class ThoughtsItem extends LitePalSupport {

    private int id;

    private String title;

    private String content;

    private ThoughtsList thoughtsList;

    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public ThoughtsList getThoughtsList() {
        return thoughtsList;
    }

    public void setThoughtsList(ThoughtsList thoughtsList) {
        this.thoughtsList = thoughtsList;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
