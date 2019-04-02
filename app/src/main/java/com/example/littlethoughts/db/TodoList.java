package com.example.littlethoughts.db;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class TodoList extends LitePalSupport {

    private int id;

    private String listName;

    private List<TodoItem> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String itemName) {
        this.listName = itemName;
    }

    public List<TodoItem> getItems() {
        return items;
    }

    public void setItems(List<TodoItem> items) {
        this.items = items;
    }
}
