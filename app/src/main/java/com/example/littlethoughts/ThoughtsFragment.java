package com.example.littlethoughts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.littlethoughts.adapter.ThoughtsAdapter;
import com.example.littlethoughts.db.ThoughtsItem;
import com.example.littlethoughts.db.ThoughtsList;

import org.litepal.LitePal;

import java.util.List;

public class ThoughtsFragment extends Fragment {

    public List<ThoughtsItem> thoughtsItemList;

    private static ThoughtsList thoughtsList;

    public ThoughtsAdapter adapter;

    public int childId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thoughts_layout, container, false);
        thoughtsItemList = getThoughtsItemList(childId);
        adapter = new ThoughtsAdapter(getContext(), thoughtsItemList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView thoughtsRecyclerView = view.findViewById(R.id.thoughts_recycler_view);
        thoughtsRecyclerView.setLayoutManager(manager);
        thoughtsRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            thoughtsItemList.clear();
            thoughtsItemList.addAll(getThoughtsItemList(childId));
            adapter.notifyDataSetChanged();
        }
    }

    public List<ThoughtsItem> getThoughtsItemList(int childId){
        thoughtsList = LitePal.find(ThoughtsList.class, childId);
        return LitePal.where("thoughtslist_id = ?", String.valueOf(childId))
                .find(ThoughtsItem.class);
    }

    public ThoughtsList getThoughtsList() {
        return thoughtsList;
    }
}
