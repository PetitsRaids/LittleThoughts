package com.example.littlethoughts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.littlethoughts.db.ThoughtsItem;

import java.sql.Date;

public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;

    private static TodoFragment todoFragment;

    private ThoughtsFragment thoughtsFragment;

    public DrawerLayout drawerLayout;

    public FloatingActionButton floatingActionButton;

    public InputMethodManager inputMethodManager;

    public static int isInputing = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        todoFragment = new TodoFragment();
        thoughtsFragment = new ThoughtsFragment();
        floatingActionButton = findViewById(R.id.floating_button);
        floatingActionButton.setOnClickListener(v -> {
            if (currentFragment == todoFragment) {
                isInputing = 1;
                todoFragment.addTodoLayout.setVisibility(View.VISIBLE);
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                todoFragment.addTodoEdit.requestFocus();
                floatingActionButton.hide();
            } else {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_thoughts_dialog, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                EditText thoughtsEdit = view.findViewById(R.id.add_thoughts_content);
                Button cancel = view.findViewById(R.id.thoughts_add_cancel);
                Button sure = view.findViewById(R.id.thoughts_add_sure);
                AlertDialog dialog = builder.create();
                dialog.setView(view);
                dialog.setCancelable(true);
                cancel.setOnClickListener(l -> {
                    dialog.dismiss();
                    floatingActionButton.show();
                });
                sure.setOnClickListener(l -> {
                    ThoughtsItem thoughtsItem = new ThoughtsItem();
                    thoughtsItem.setContent(thoughtsEdit.getText().toString());
                    thoughtsItem.setThoughtsList(thoughtsFragment.getThoughtsList());
                    thoughtsItem.setCreateTime(new Date(System.currentTimeMillis()));
                    thoughtsItem.save();
                    floatingActionButton.show();
                    dialog.dismiss();
                });
                floatingActionButton.hide();
                dialog.show();
            }
        });
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        currentFragment = todoFragment;
    }

    public void showHideFragment(int groupId, int childPosition) {
        Fragment fragment;
        if (groupId == 0) {
            fragment = todoFragment;
            todoFragment.childId = childPosition + 1;
        } else {
            fragment = thoughtsFragment;
            thoughtsFragment.childId = childPosition + 1;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putInt("list_id", childPosition + 1);
            fragment.setArguments(bundle);
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.main_frame_layout, fragment);
        } else {
            transaction.hide(currentFragment).show(fragment);
        }
        currentFragment = fragment;
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.START);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isInputing == 1) {
            todoFragment.addTodoLayout.setVisibility(View.GONE);
            floatingActionButton.show();
        } else {
            super.onBackPressed();
        }
    }
}
