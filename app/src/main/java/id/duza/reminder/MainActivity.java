package id.duza.reminder;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String PICKER_TAG = "time_picker";
    private EditText etReminder;
    private Button btnSetTime;
    private Button btnSave;

    private int mHour;
    private int mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
        setupViewListener();
    }

    private void setupViewListener() {
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        mHour = hour;
                        mMinute = minute;
                    }
                });

                timePicker.show(getFragmentManager(), PICKER_TAG);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void saveData() {
        String reminderText = etReminder.getText().toString();

        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("data", reminderText);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 123,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.clear();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), mHour, mMinute);

        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Reminder for : \n" +
                                reminderText +
                                "\n time : " +
                                c.get(Calendar.YEAR) + " " +
                                c.get(Calendar.MONTH) + " " +
                                c.get(Calendar.DAY_OF_MONTH) + "at : " +
                                mHour + ":" + mMinute,
                                Toast.LENGTH_SHORT).show();
    }

    private void setupView() {
        etReminder = (EditText) findViewById(R.id.et_reminder);
        btnSetTime = (Button) findViewById(R.id.btnSetTime);
        btnSave = (Button) findViewById(R.id.btnSave);
    }
}
