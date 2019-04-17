package com.example.littlethoughts.fragement;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.littlethoughts.R;

public class HeadFragment extends Fragment {

    private ImageView cover, headPortrait;

    private TextView userName, email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_head, container, false);
        cover = view.findViewById(R.id.cover);
        headPortrait = view.findViewById(R.id.head_portrait);
        userName = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cover.setImageDrawable(getResources().getDrawable(R.drawable.sensei));
        Glide.with(HeadFragment.this).load(R.drawable.sensei3).circleCrop().into(headPortrait);
        userName.setText(R.string.user_name);
        email.setText(R.string.email);
    }
}
