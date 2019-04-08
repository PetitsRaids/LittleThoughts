package com.example.littlethoughts.db;

import org.litepal.crud.LitePalSupport;

public class ThoughtsItem extends LitePalSupport {

    private int id;

    private String content;

    private ThoughtsList thoughtsList;

    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
