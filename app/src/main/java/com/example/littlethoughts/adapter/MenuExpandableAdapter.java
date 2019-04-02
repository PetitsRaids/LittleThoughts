package com.example.littlethoughts.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.littlethoughts.R;
import com.example.littlethoughts.db.ThoughtsList;
import com.example.littlethoughts.db.TodoList;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

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
        groupViewHolder.addList.setOnClickListener((view) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            View dialogView = LayoutInflater.from(mContext).inflate(R.layout.add_list_dialog, null, false);
            TextView textView = dialogView.findViewById(R.id.add_text);
            EditText editText = dialogView.findViewById(R.id.add_edit);
            Button sureAdd = dialogView.findViewById(R.id.add_sure);
            Button cancelAdd = dialogView.findViewById(R.id.add_cancel);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            String str = "添加" + listNames[groupPosition];
            textView.setText(str);
            cancelAdd.setOnClickListener((v ->
                    dialog.dismiss()
            ));
            sureAdd.setOnClickListener((v -> {
                String val = editText.getText().toString();
                if (val.equals("")) {
                    Toast.makeText(mContext, "请输入列表名称！", Toast.LENGTH_SHORT).show();
                } else {
                    if (listNames[groupPosition].equals("待办事项")) {
                        TodoList todoList = new TodoList();
                        todoList.setListName(val);
                        todoList.save();
                        childrenList.get(groupPosition).add(val);
                        notifyDataSetChanged();
                    } else if (listNames[groupPosition].equals("小想法")) {
                        ThoughtsList thoughtsList = new ThoughtsList();
                        thoughtsList.setThoughts(val);
                        thoughtsList.save();
                        childrenList.get(groupPosition).add(val);
                        notifyDataSetChanged();
                    }
                    Toast.makeText(mContext, "已添加", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }));
            dialog.show();
        });
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
