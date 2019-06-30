package com.example.littlethoughts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.littlethoughts.db.ThoughtsItem;
import com.example.littlethoughts.widget.EnsureDialog;

import org.litepal.LitePal;

public class ThoughtsEditActivity extends AppCompatActivity {

    private EditText editText;

    private ThoughtsItem thoughtsItem;

    private boolean isChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoughts_edit);
        int id = getIntent().getIntExtra("thoughts_id", 1);
        thoughtsItem = LitePal.find(ThoughtsItem.class, id);
        editText = findViewById(R.id.edit_thoughts);
        editText.setText(thoughtsItem.getContent());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        toolbar.setTitle(R.string.edit_thoughts);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
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
                if (isChanged)
                    new EnsureDialog(ThoughtsEditActivity.this,
                            R.string.back, R.string.save_or_not, R.string.save, () -> {
                        saveThought();
                        finish();
                    });
                else
                    finish();
                break;
            case R.id.save_thoughts:
                if (isChanged) {
                    saveThought();
                }
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void saveThought() {
        thoughtsItem.setContent(editText.getText().toString());
        thoughtsItem.update(thoughtsItem.getId());
    }
}
