package pl.edu.ug.todolist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    Button btn_add;
    EditText et_taskName;
    TextView et_deadlineDate, et_deadlineTime;
    ListView lv_taskList;

    ArrayAdapter taskArrayAdapter;
    DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.btn_add);
        et_taskName = findViewById(R.id.et_taskName);
        et_deadlineDate = findViewById(R.id.et_deadlineDate);
        et_deadlineTime = findViewById(R.id.et_deadlineTime);
        lv_taskList = findViewById(R.id.lv_taskList);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        showTasksOnListView(dataBaseHelper);


        // button listeners
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskModel taskModel = new TaskModel(
                        -1,
                        et_taskName.getText().toString(),
                        et_deadlineDate.getText().toString(),
                        et_deadlineTime.getText().toString()
                );
                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

                boolean isAdded = dataBaseHelper.addOne(taskModel);
                if (isAdded) {
                    Toast.makeText(MainActivity.this, taskModel.getName() + " added!", Toast.LENGTH_LONG).show();
                    et_taskName.setText("");
                    et_deadlineDate.setText("");
                    et_deadlineTime.setText("");
                    showTasksOnListView(dataBaseHelper);
                } else
                    Toast.makeText(MainActivity.this, "Task already exists!", Toast.LENGTH_LONG).show();
            }
        });

        lv_taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TaskModel clickedTask = (TaskModel) parent.getItemAtPosition(position);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("text")
                        .setMessage("Do you really want to delete task " + clickedTask.getName())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int whichButton) {
                                String taskName = clickedTask.getName();
                                dataBaseHelper.delete(taskName);
                                showTasksOnListView(dataBaseHelper);
                                Toast.makeText(MainActivity.this, "Deleted " + clickedTask.getName(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });


        //date and time listeners
        et_deadlineDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_deadlineDate.setText(String.format("%d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        et_deadlineTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                et_deadlineTime.setText(String.format("%02d:%02d", hour, minute));
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });
    }

    private void showTasksOnListView(DataBaseHelper dataBaseHelper2) {
        taskArrayAdapter = new ArrayAdapter<TaskModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper2.getAll());
        lv_taskList.setAdapter(taskArrayAdapter);
    }
}
