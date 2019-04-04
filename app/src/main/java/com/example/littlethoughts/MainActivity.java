package com.example.littlethoughts;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.littlethoughts.db.TodoItem;

public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;

    private static TodoFragment todoFragment;

    private ThoughtsFragment thoughtsFragment;

    public DrawerLayout drawerLayout;

    public FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        todoFragment = new TodoFragment();
        thoughtsFragment = new ThoughtsFragment();
        floatingActionButton = findViewById(R.id.floating_button);
        floatingActionButton.setOnClickListener(v -> {
            todoFragment.addTodoLayout.setVisibility(View.VISIBLE);
            floatingActionButton.hide();
        });
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
    }

    public void showHideFragment(int groupId, int childPosition){
        Fragment fragment;
        if(groupId == 0){
            fragment = todoFragment;
            todoFragment.childId = childPosition + 1;
        }else{
            fragment = thoughtsFragment;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(!fragment.isAdded()){
            Bundle bundle = new Bundle();
            bundle.putInt("list_id", childPosition + 1);
            fragment.setArguments(bundle);
            if(currentFragment != null){
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.main_frame_layout, fragment);
        }else{
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
}
