package com.example.littlethoughts;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.littlethoughts.db.TodoItem;

import org.litepal.LitePal;

public class TodoAlarmActivity extends AppCompatActivity {

    EditText todoContent;

    SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_alarm);
        Toolbar toolbar = findViewById(R.id.todo_alarm_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        todoContent = findViewById(R.id.todo_edit_name);
        switchCompat = findViewById(R.id.remind_switch);
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                Toast.makeText(this, "开", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "关", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = getIntent();
        int todoitemId = intent.getIntExtra("todoitem_id", -1);
        if (todoitemId != -1) {
            TodoItem todoItem = LitePal.find(TodoItem.class, todoitemId);
            todoContent.setText(todoItem.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
