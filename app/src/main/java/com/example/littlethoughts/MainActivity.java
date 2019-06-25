package com.example.littlethoughts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.littlethoughts.adapter.FragmentAdapter;
import com.example.littlethoughts.db.ThoughtsItem;
import com.example.littlethoughts.db.ThoughtsList;
import com.example.littlethoughts.db.TodoList;
import com.example.littlethoughts.fragement.MenuFragment;
import com.example.littlethoughts.fragement.ThoughtsFragment;
import com.example.littlethoughts.fragement.TodoFragment;
import com.example.littlethoughts.service.ReminderService;
import com.example.littlethoughts.utils.Logger;
import com.example.littlethoughts.widget.CustomViewPager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.text.DateFormat.getDateTimeInstance;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private TodoFragment todoFragment;

    private ThoughtsFragment thoughtsFragment;

    public DrawerLayout drawerLayout;

    private FloatingActionButton floatingActionButton;

    public InputMethodManager inputMethodManager;

    CollapsingToolbarLayout toolbarLayout;

    TodoList todoList;

    ThoughtsList thoughtsList;

    public static int isInputing = 0;

    private int groupId = 0;

    private int childId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        floatingActionButton.setOnClickListener(v -> {
            if (groupId != -1 && childId != -1) {
                if (groupId == 0) {
                    isInputing = 1;
                    todoFragment.addTodoItem();
                    floatingActionButton.hide();
                } else if (groupId == 1) {
                    thoughtsFragment.addLittleThought();
                    floatingActionButton.hide();
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                    Toast.makeText(this, R.string.introduction, Toast.LENGTH_SHORT).show();
                }
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
                Toast.makeText(this, R.string.introduction, Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        groupId = sharedPreferences.getInt("group_id", -1);
        childId = sharedPreferences.getInt("child_id", -1);
        if (groupId != -1 || childId != -1) {
            changeList(groupId, childId);
        }
    }

    private void init() {
        drawerLayout = findViewById(R.id.drawer_layout);
        viewPager = findViewById(R.id.view_pager);
        floatingActionButton = findViewById(R.id.floating_button);
        toolbarLayout = findViewById(R.id.collapsing);
        todoFragment = new TodoFragment();
        thoughtsFragment = new ThoughtsFragment();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(todoFragment);
        fragmentList.add(thoughtsFragment);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentList));
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, ReminderService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.edit_list:
                if (groupId != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.edit_list_dialog, null, false);
                    EditText editText = view.findViewById(R.id.edit_list_name);
                    Button cancel = view.findViewById(R.id.edit_cancel);
                    Button editName = view.findViewById(R.id.edit_sure);
                    builder.setView(view);
                    AlertDialog dialog = builder.create();
                    cancel.setOnClickListener(l -> dialog.dismiss());
                    editName.setOnClickListener(l -> {
                        String str = editText.getText().toString();
                        MenuFragment menuFragment = new MenuFragment();
                        menuFragment.editChild(groupId, childId, str);
                        toolbarLayout.setTitle(str);
                        if (groupId == 0) {
                            todoFragment.refreshListName(str);
                        } else {
                            thoughtsFragment.refreshListName(str);
                        }
                        dialog.dismiss();
                    });
                    dialog.show();
                } else {
                    Toast.makeText(this, R.string.delete_without_select, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.remove_list:
                if (groupId != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View dialogView = LayoutInflater.from(this).inflate(R.layout.delete_dialog, null, false);
                    Button cancel = dialogView.findViewById(R.id.list_delete_cancel);
                    Button deleteBtn = dialogView.findViewById(R.id.list_delete_sure);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();
                    cancel.setOnClickListener(l ->
                            alertDialog.dismiss()
                    );
                    deleteBtn.setOnClickListener(l -> {
                        alertDialog.dismiss();
                        if (groupId == 0) {
                            todoFragment.removeList();
                        } else {
                            thoughtsFragment.removeList();
                        }
                        MenuFragment menuFragment = new MenuFragment();
                        menuFragment.removeChild(groupId, childId);
                        toolbarLayout.setTitle("Little Thoughts");
                        groupId = -1;
                        childId = -1;
                    });
                    alertDialog.show();
                } else {
                    Toast.makeText(this, R.string.delete_without_select, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.exit:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt("group_id", groupId);
        editor.putInt("child_id", childId);
        editor.apply();
    }

    public void changeList(int groupId, int childPosition) {
        viewPager.setCurrentItem(groupId);
        this.groupId = groupId;
        this.childId = childPosition;
        if (groupId == 0) {
            todoList = LitePal.findAll(TodoList.class).get(childPosition);
            toolbarLayout.setTitle(todoList.getListName());
            todoFragment.childId = todoList.getId();
        } else {
            thoughtsList = LitePal.findAll(ThoughtsList.class).get(childPosition);
            toolbarLayout.setTitle(thoughtsList.getThoughts());
            thoughtsFragment.childId = thoughtsList.getId();
        }
    }

    @Override
    public void onBackPressed() {
        if (isInputing == 1) {
            todoFragment.hiddenAddLayout();
            floatingActionButton.show();
            isInputing = 0;
        } else {
            super.onBackPressed();
        }
    }

    public InputMethodManager getInputMethodManager(){
        return inputMethodManager;
    }

    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }
}
