package com.example.littlethoughts.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.littlethoughts.R;
import com.example.littlethoughts.db.ThoughtsItem;

import java.util.List;

public class ThoughtsAdapter extends RecyclerView.Adapter<ThoughtsAdapter.ViewHolder> {

    private Context mContext;

    private List<ThoughtsItem> thoughtsItemList;

    public ThoughtsAdapter(Context context, List<ThoughtsItem> thoughtsItemList) {
        mContext = context;
        this.thoughtsItemList = thoughtsItemList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView thoughtsTitle;
        TextView thoughtsContent;
        TextView createTime;

        ViewHolder(View view) {
            super(view);
            thoughtsTitle = view.findViewById(R.id.thoughts_title);
            thoughtsContent = view.findViewById(R.id.thoughts_content);
            createTime = view.findViewById(R.id.create_time);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.thoughts_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ThoughtsItem thoughtsItem = thoughtsItemList.get(i);
        viewHolder.thoughtsTitle.setText(thoughtsItem.getTitle());
        viewHolder.thoughtsContent.setText(thoughtsItem.getContent());
        viewHolder.createTime.setText(thoughtsItem.getCreateTime().toString());
    }

    @Override
    public int getItemCount() {
        return thoughtsItemList.size();
    }
}
