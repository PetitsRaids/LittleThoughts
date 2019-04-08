package com.example.littlethoughts;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.littlethoughts.db.ThoughtsItem;

import org.litepal.LitePal;

public class ThoughtsEditActivity extends AppCompatActivity {

    EditText editText;

    ThoughtsItem thoughtsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoughts_edit);
        int id = getIntent().getIntExtra("thoughts_id", 1);
        thoughtsItem = LitePal.find(ThoughtsItem.class, id);
        editText = findViewById(R.id.edit_thoughts);
        editText.setText(thoughtsItem.getContent());
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        toolbar.setTitle(R.string.edit_thoughts);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.thoughtds_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save_thoughts:
                thoughtsItem.setContent(editText.getText().toString());
                thoughtsItem.update(thoughtsItem.getId());
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
