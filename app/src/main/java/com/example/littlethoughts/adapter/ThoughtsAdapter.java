package com.example.littlethoughts.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.littlethoughts.R;
import com.example.littlethoughts.ThoughtsEditActivity;
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
        TextView thoughtsContent;
        TextView createTime;
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.thoughts_card_view);
            thoughtsContent = view.findViewById(R.id.thoughts_content);
            createTime = view.findViewById(R.id.create_time);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.thoughts_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cardView.setOnClickListener(l -> {
            Intent intent = new Intent(mContext, ThoughtsEditActivity.class);
            intent.putExtra("thoughts_id", thoughtsItemList.get(viewHolder.getAdapterPosition()).getId());
            mContext.startActivity(intent);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ThoughtsItem thoughtsItem = thoughtsItemList.get(i);
        viewHolder.thoughtsContent.setText(thoughtsItem.getContent());
        viewHolder.createTime.setText(thoughtsItem.getCreateTime());
    }

    @Override
    public int getItemCount() {
        return thoughtsItemList.size();
    }
}
