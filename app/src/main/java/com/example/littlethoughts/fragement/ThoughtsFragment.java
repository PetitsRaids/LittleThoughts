package com.example.littlethoughts.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.littlethoughts.MainActivity;
import com.example.littlethoughts.R;
import com.example.littlethoughts.adapter.ThoughtsAdapter;
import com.example.littlethoughts.db.ThoughtsItem;
import com.example.littlethoughts.db.ThoughtsList;
import com.example.littlethoughts.widget.AddEditDialog;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.text.DateFormat.getDateTimeInstance;

public class ThoughtsFragment extends Fragment {

    private List<ThoughtsItem> thoughtsItemList;

    private ThoughtsList thoughtsList;

    private ThoughtsAdapter adapter;

    private MainActivity mainActivity;

    private int childPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thoughts_layout, container, false);
        thoughtsItemList = new ArrayList<>();
        adapter = new ThoughtsAdapter(getContext(), thoughtsItemList);
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
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList(childPosition);
    }

    public void refreshList(int childPosition) {
        thoughtsList = LitePal.findAll(ThoughtsList.class).get(childPosition);
        LitePal.where("thoughtslist_id = ?", String.valueOf(thoughtsList.getId()))
                .findAsync(ThoughtsItem.class).listen(allItem -> {
            thoughtsItemList.clear();
            thoughtsItemList.addAll(allItem);
            adapter.notifyDataSetChanged();
        });
    }

    public void removeList() {
        LitePal.deleteAllAsync(ThoughtsItem.class, "thoughtslist_id = ?", String.valueOf(thoughtsList.getId()))
                .listen(rowsAffected -> {
                    thoughtsItemList.clear();
                    thoughtsList.delete();
                    adapter.notifyDataSetChanged();
                });
    }

    public void refreshListName(String name) {
        thoughtsList.setThoughts(name);
        thoughtsList.update(thoughtsList.getId());
    }

    private void addItem(ThoughtsItem thoughtsItem) {
        thoughtsItemList.add(thoughtsItem);
        adapter.notifyItemInserted(adapter.getItemCount());
    }

    public String getListName(int childPosition) {
        thoughtsList = LitePal.findAll(ThoughtsList.class).get(childPosition);
        this.childPosition = childPosition;
        return thoughtsList.getThoughts();
    }

    public void addLittleThought() {
        new AddEditDialog(getContext(), R.string.add_thoughts, "", R.string.sure_add,
                () -> mainActivity.getFloatingActionButton().show(),
                editText -> {
                    ThoughtsItem thoughtsItem = new ThoughtsItem();
                    thoughtsItem.setContent(editText);
                    thoughtsItem.setThoughtsList(thoughtsList);
                    thoughtsItem.setCreateTime(getDateTimeInstance().format(new Date(System.currentTimeMillis())));
                    thoughtsItem.save();
                    addItem(thoughtsItem);
                    mainActivity.getFloatingActionButton().show();
                });
    }

}
