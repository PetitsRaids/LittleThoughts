package com.example.littlethoughts.fragement;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.littlethoughts.R;
import com.example.littlethoughts.adapter.ThoughtsAdapter;
import com.example.littlethoughts.db.ThoughtsItem;
import com.example.littlethoughts.db.ThoughtsList;

import org.litepal.LitePal;

import java.util.List;

public class ThoughtsFragment extends Fragment {

    private static final String TAG = "ThoughtsFragment";

    public List<ThoughtsItem> thoughtsItemList;

    private static ThoughtsList thoughtsList;

    public ThoughtsAdapter adapter;

    public int childId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.thoughts_layout, container, false);
        thoughtsItemList = getThoughtsItemList(childId);
        adapter = new ThoughtsAdapter(getActivity(), thoughtsItemList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        RecyclerView thoughtsRecyclerView = view.findViewById(R.id.thoughts_recycler_view);
        thoughtsRecyclerView.setLayoutManager(manager);
        thoughtsRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshList();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshList();
        }
    }

    private List<ThoughtsItem> getThoughtsItemList(int childId) {
        thoughtsList = LitePal.find(ThoughtsList.class, childId);
        return LitePal.where("thoughtslist_id = ?", String.valueOf(childId))
                .find(ThoughtsItem.class);
    }

    public void removeList() {
        for (ThoughtsItem thoughtsItem : thoughtsItemList) {
            thoughtsItem.delete();
        }
        thoughtsItemList.clear();
        thoughtsList.delete();
        adapter.notifyDataSetChanged();
    }

    public void refreshListName(String name){
        thoughtsList.setThoughts(name);
        thoughtsList.update(thoughtsList.getId());
    }

    private void refreshList() {
        thoughtsItemList.clear();
        thoughtsItemList.addAll(getThoughtsItemList(childId));
        adapter.notifyDataSetChanged();
    }

    public ThoughtsList getThoughtsList() {
        return thoughtsList;
    }
}