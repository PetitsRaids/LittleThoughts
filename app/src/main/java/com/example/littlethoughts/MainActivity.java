package com.example.littlethoughts;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.littlethoughts.db.ThoughtsItem;
import com.example.littlethoughts.db.ThoughtsList;
import com.example.littlethoughts.db.TodoList;
import com.example.littlethoughts.fragement.MenuFragment;
import com.example.littlethoughts.fragement.ThoughtsFragment;
import com.example.littlethoughts.fragement.TodoFragment;

import org.litepal.LitePal;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static java.text.DateFormat.getDateTimeInstance;

public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;

    private static TodoFragment todoFragment;

    private static ThoughtsFragment thoughtsFragment;

    public DrawerLayout drawerLayout;

    public FloatingActionButton floatingActionButton;

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
        drawerLayout = findViewById(R.id.drawer_layout);
        todoFragment = new TodoFragment();
        thoughtsFragment = new ThoughtsFragment();
        floatingActionButton = findViewById(R.id.floating_button);
        floatingActionButton.setOnClickListener(v -> {
            if (groupId != -1 && childId != -1) {
                if (currentFragment == todoFragment) {
                    isInputing = 1;
                    todoFragment.addTodoLayout.setVisibility(View.VISIBLE);
                    inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    todoFragment.addTodoEdit.requestFocus();
                    floatingActionButton.hide();
                } else if (currentFragment == thoughtsFragment) {
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_thoughts_dialog, null, false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    EditText thoughtsEdit = view.findViewById(R.id.add_thoughts_content);
                    Button cancel = view.findViewById(R.id.thoughts_add_cancel);
                    Button sure = view.findViewById(R.id.thoughts_add_sure);
                    AlertDialog dialog = builder.create();
                    dialog.setView(view);
                    dialog.setCancelable(true);
                    cancel.setOnClickListener(l -> {
                        inputMethodManager.hideSoftInputFromWindow(thoughtsEdit.getWindowToken(), 0);
                        dialog.dismiss();
                        floatingActionButton.show();
                    });
                    sure.setOnClickListener(l -> {
                        ThoughtsItem thoughtsItem = new ThoughtsItem();
                        thoughtsItem.setContent(thoughtsEdit.getText().toString());
                        thoughtsItem.setThoughtsList(thoughtsFragment.getThoughtsList());
                        thoughtsItem.setCreateTime(getDateTimeInstance().format(new Date(System.currentTimeMillis())));
                        thoughtsItem.save();
                        thoughtsFragment.thoughtsItemList.add(thoughtsItem);
                        thoughtsFragment.adapter.notifyItemInserted(thoughtsFragment.adapter.getItemCount() - 1);
                        inputMethodManager.hideSoftInputFromWindow(thoughtsEdit.getWindowToken(), 0);
                        dialog.dismiss();
                        floatingActionButton.show();
                    });
                    dialog.show();
                    floatingActionButton.hide();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> {
                                thoughtsEdit.requestFocus();
                                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputMethodManager.showSoftInput(thoughtsEdit, 0);
                            });
                        }
                    }, 300);
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                    Toast.makeText(this, R.string.introduction, Toast.LENGTH_SHORT).show();
                }
            } else {
                drawerLayout.openDrawer(Gravity.START);
                Toast.makeText(this, R.string.introduction, Toast.LENGTH_SHORT).show();
            }
        });

        toolbarLayout = findViewById(R.id.collapsing);
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
            showHideFragment(groupId, childId);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop");
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
                drawerLayout.openDrawer(Gravity.START);
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
                        if(groupId == 0){
                            todoFragment.refreshListName(str);
                        }else{
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
                        if (currentFragment == todoFragment) {
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
        Log.d("MainActivity", "onDestroy");
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt("group_id", groupId);
        editor.putInt("child_id", childId);
        editor.apply();
    }

    public void showHideFragment(int groupId, int childPosition) {
        this.groupId = groupId;
        this.childId = childPosition;
        Fragment fragment;
        if (groupId == 0) {
            fragment = todoFragment;
            todoList = LitePal.findAll(TodoList.class).get(childPosition);
            toolbarLayout.setTitle(todoList.getListName());
            todoFragment.childId = todoList.getId();
        } else {
            fragment = thoughtsFragment;
            thoughtsList = LitePal.findAll(ThoughtsList.class).get(childPosition);
            toolbarLayout.setTitle(thoughtsList.getThoughts());
            thoughtsFragment.childId = thoughtsList.getId();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
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
    public void onBackPressed() {
        if (isInputing == 1) {
            todoFragment.addTodoLayout.setVisibility(View.GONE);
            floatingActionButton.show();
            isInputing = 0;
        } else {
            super.onBackPressed();
        }
    }
}
