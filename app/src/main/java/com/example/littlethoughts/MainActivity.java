package com.example.littlethoughts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.littlethoughts.adapter.FragmentAdapter;
import com.example.littlethoughts.fragement.MenuFragment;
import com.example.littlethoughts.fragement.ThoughtsFragment;
import com.example.littlethoughts.fragement.TodoFragment;
import com.example.littlethoughts.service.ReminderService;
import com.example.littlethoughts.utils.Logger;
import com.example.littlethoughts.widget.AddEditDialog;
import com.example.littlethoughts.widget.EnsureDialog;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private TodoFragment todoFragment;

    private ThoughtsFragment thoughtsFragment;

    public DrawerLayout drawerLayout;

    private FloatingActionButton floatingActionButton;

    public InputMethodManager inputMethodManager;

    CollapsingToolbarLayout toolbarLayout;

    public static boolean isInputing = false;

    private int groupId = 0;

    private int childId = 0;

    private boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        floatingActionButton.setOnClickListener(v -> {
            if (groupId != -1 && childId != -1) {
                if (groupId == 0) {
                    isInputing = true;
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
        } else {
            changeList(0, 0);
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
                    new AddEditDialog(MainActivity.this,
                            R.string.edit_list_name,
                            "", R.string.sure_edit, null,
                            str -> {
                                MenuFragment.editChild(groupId, childId, str);
                                toolbarLayout.setTitle(str);
                                if (groupId == 0) {
                                    todoFragment.refreshListName(str);
                                } else {
                                    thoughtsFragment.refreshListName(str);
                                }
                            });
                } else {
                    Toast.makeText(this, R.string.delete_without_select, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.remove_list:
                if (groupId != -1) {
                    new EnsureDialog(MainActivity.this,
                            R.string.delete_make_sure, R.string.delete_message, R.string.delete,
                            () -> {
                                if (groupId == 0) {
                                    todoFragment.removeList();
                                } else {
                                    thoughtsFragment.removeList();
                                }
                                MenuFragment.removeChild(groupId, childId);
                                toolbarLayout.setTitle("Little Thoughts");
                                Toast.makeText(MainActivity.this, R.string.delete, Toast.LENGTH_SHORT).show();
                                groupId = -1;
                                childId = -1;
                            });
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
        Logger.d("group_id" + groupId);
        editor.putInt("child_id", childId);
        Logger.d("child_id" + childId);
        editor.apply();
    }

    public void changeList(int groupId, int childPosition) {
        if (groupId == 0) {
            toolbarLayout.setTitle(todoFragment.getListName(childPosition));
            if (isFirstTime) {
                isFirstTime = false;
            } else {
                if (this.groupId == groupId)
                    todoFragment.refreshList(childPosition);
            }
        } else {
            toolbarLayout.setTitle(thoughtsFragment.getListName(childPosition));
            if (isFirstTime) {
                isFirstTime = false;
            } else {
                if (this.groupId == groupId)
                    thoughtsFragment.refreshList(childPosition);
            }
        }
        viewPager.setCurrentItem(groupId, false);
        this.groupId = groupId;
        this.childId = childPosition;
    }

    @Override
    public void onBackPressed() {
        if (isInputing) {
            todoFragment.hiddenAddLayout();
            floatingActionButton.show();
            isInputing = false;
        } else {
            super.onBackPressed();
        }
    }

    public InputMethodManager getInputMethodManager() {
        return inputMethodManager;
    }

    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }
}
