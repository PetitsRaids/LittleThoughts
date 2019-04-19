package com.example.littlethoughts;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.littlethoughts.broadcast.ReminderReceiver;
import com.example.littlethoughts.db.TodoItem;

import org.litepal.LitePal;

import java.util.Calendar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

public class TodoAlarmActivity extends AppCompatActivity {

    EditText todoContent;

    private EditText datePicker, timePicker;

    SwitchCompat switchCompat;

    private Calendar mCalendar;

    private int year, month, dayOfMonth;

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
        LinearLayout reminderLayout = findViewById(R.id.reminder_layout);
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                reminderLayout.setVisibility(View.VISIBLE);
            } else {
                reminderLayout.setVisibility(View.GONE);
            }
        });
        reminderLayout.setVisibility(View.GONE);

        datePicker = findViewById(R.id.date_picker_text);
        datePicker.setOnClickListener(l -> {
            showDatePicker();
        });
        datePicker.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                showDatePicker();
            }
        });
        datePicker.setInputType(InputType.TYPE_NULL);
        timePicker = findViewById(R.id.time_picker_text);
        timePicker.setOnClickListener(l -> {
            showTimePicker();
        });
        timePicker.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                showTimePicker();
            }
        });
        timePicker.setInputType(InputType.TYPE_NULL);

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

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    this.year = year;
                    this.month = month;
                    this.dayOfMonth = dayOfMonth;
                    String val = month + "月" + dayOfMonth + "日";
                    datePicker.setText(val);
                    if (timePicker.getText().toString().equals("")) {
                        showTimePicker();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    mCalendar = Calendar.getInstance();
                    mCalendar.set(year, month, dayOfMonth, hourOfDay, minute);
                    String val = hourOfDay + ":" + minute;
                    timePicker.setText(val);
                    Intent intent = new Intent("com.example.littlethoughts.REMINDER");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
                    long milliseconds = mCalendar.getTimeInMillis();
                    if (milliseconds < System.currentTimeMillis()) {
                        milliseconds = milliseconds + (24 * 60 * 60 * 1000);
                    }
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC, milliseconds, pendingIntent);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }
}
