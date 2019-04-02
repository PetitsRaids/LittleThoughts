package com.example.littlethoughts.db;

import org.litepal.crud.LitePalSupport;

public class ThoughtsList extends LitePalSupport {

    private int id;

    private String thoughts;

    private ThoughtsItem thoughtsItem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public ThoughtsItem getThoughtsItem() {
        return thoughtsItem;
    }

    public void setThoughtsItem(ThoughtsItem thoughtsItem) {
        this.thoughtsItem = thoughtsItem;
    }
}
