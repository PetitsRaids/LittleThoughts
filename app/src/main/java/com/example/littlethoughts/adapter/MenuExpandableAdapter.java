package com.example.littlethoughts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.littlethoughts.R;
import com.example.littlethoughts.db.ThoughtsList;
import com.example.littlethoughts.db.TodoList;
import com.example.littlethoughts.widget.AddEditDialog;

import java.util.List;

public class MenuExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    private String[] listNames;

    private List<List<String>> childrenList;

    class GroupViewHolder {
        TextView listName;
        Button addList;
    }

    class ChildViewHolder {
        TextView itemName;
    }

    public MenuExpandableAdapter(Context context, String[] listNames, List<List<String>> childernList) {
        mContext = context;
        this.listNames = listNames;
        this.childrenList = childernList;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_group_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.listName = convertView.findViewById(R.id.list_name);
            groupViewHolder.addList = convertView.findViewById(R.id.add_todo);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.listName.setText(listNames[groupPosition]);
        groupViewHolder.addList.setOnClickListener(view ->
                new AddEditDialog(mContext,
                        "添加" + listNames[groupPosition], "", mContext.getText(R.string.sure_add).toString(), null,
                        editText -> {
                            if (editText.equals("")) {
                                Toast.makeText(mContext, "请输入列表名称！", Toast.LENGTH_SHORT).show();
                            } else {
                                if (groupPosition == 0) {
                                    TodoList todoList = new TodoList();
                                    todoList.setListName(editText);
                                    todoList.save();
                                    childrenList.get(groupPosition).add(editText);
                                    notifyDataSetChanged();
                                } else {
                                    ThoughtsList thoughtsList = new ThoughtsList();
                                    thoughtsList.setThoughts(editText);
                                    thoughtsList.save();
                                    childrenList.get(groupPosition).add(editText);
                                    notifyDataSetChanged();
                                }
                                Toast.makeText(mContext, "已添加", Toast.LENGTH_SHORT).show();
                            }
                        }));
        groupViewHolder.addList.setFocusable(false);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childHolder = new ChildViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_child_item, parent, false);
            childHolder.itemName = convertView.findViewById(R.id.todo_name);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildViewHolder) convertView.getTag();
        }
        String name = childrenList.get(groupPosition).get(childPosition);
        childHolder.itemName.setText(name);
        return convertView;
    }

    @Override
    public int getGroupCount() {
        return listNames.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childrenList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listNames[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childrenList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
