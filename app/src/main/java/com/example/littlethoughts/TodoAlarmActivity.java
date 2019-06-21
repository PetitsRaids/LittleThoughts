package com.example.littlethoughts;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.littlethoughts.db.TodoItem;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

public class TodoAlarmActivity extends AppCompatActivity {

    private EditText todoContent;

    private EditText datePicker, timePicker;

    SwitchCompat switchCompat;

    private Calendar mCalendar;

    private AlarmManager alarmManager;

    private Intent mIntent;

    private PendingIntent pendingIntent;

    private TodoItem todoItem;

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
                mCalendar = Calendar.getInstance();
                int defaultTime = mCalendar.get(Calendar.HOUR_OF_DAY);
                String val;
                if (defaultTime == 23) {
                    val = "00:00";
                } else {
                    val = ++defaultTime + ":00";
                }
                timePicker.setText(val);
                datePicker.setText("今天");
                year = mCalendar.get(Calendar.YEAR);
                month = mCalendar.get(Calendar.MONTH);
                dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
                mCalendar.set(mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH),
                        defaultTime, 0, 0);
                startAlarm(mCalendar.getTimeInMillis());

            } else {
                reminderLayout.setVisibility(View.GONE);
                cancelAlarm();
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
            todoItem = LitePal.find(TodoItem.class, todoitemId);
            todoContent.setText(todoItem.getName());
        }

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save_todo:
                todoItem.setName(todoContent.getText().toString());
                todoItem.update(todoItem.getId());
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
                    Log.d("ALARM", "showTimePicker: " + year);
                    Log.d("ALARM", "showTimePicker: " + month);
                    Log.d("ALARM", "showTimePicker: " + dayOfMonth);
                    Log.d("ALARM", "showTimePicker: " + hourOfDay);
                    Log.d("ALARM", "showTimePicker: " + minute);
                    mCalendar.set(year, month, dayOfMonth, hourOfDay, minute, 0);
                    String val = hourOfDay + ":" + String.format(Locale.CHINA, "%02d", minute);
                    timePicker.setText(val);
                    long milliseconds = mCalendar.getTimeInMillis();
                    if (milliseconds < System.currentTimeMillis()) {
                        milliseconds = milliseconds + (24 * 60 * 60 * 1000);
                    }
                    startAlarm(milliseconds);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    private void startAlarm(long time) {
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
        mIntent = new Intent();
        mIntent.setAction("com.example.littlethoughts.REMINDER");
        mIntent.setPackage("com.example.littlethoughts");
        mIntent.putExtra("test", mCalendar.get(Calendar.MINUTE));
        Log.d("ALARM", "startAlarm: " + mCalendar.get(Calendar.MINUTE));
        pendingIntent = PendingIntent.getBroadcast(this, 1, mIntent, 0);
        // AlarmManger 是系统级别的，只要系统在运行，就能发送
        alarmManager.set(AlarmManager.RTC, time, pendingIntent);
    }

    private void cancelAlarm() {
        alarmManager.cancel(pendingIntent);
    }
}
