package pl.edu.ug.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TASK_TABLE = "TASK_TABLE";
    public static final String COLUMN_TASK_NAME = "TASK_NAME";
    public static final String COLUMN_TASK_DEADLINE_DATE = "TASK_DEADLINE_DATE";
    public static final String COLUMN_TASK_DEADLINE_TIME = "TASK_DEADLINE_TIME";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "task.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TASK_TABLE
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK_NAME + " TEXT UNIQUE, "
                + COLUMN_TASK_DEADLINE_DATE + " TEXT, "
                + COLUMN_TASK_DEADLINE_TIME + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(TaskModel taskModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

            cv.put(COLUMN_TASK_NAME, taskModel.getName());
            cv.put(COLUMN_TASK_DEADLINE_DATE, taskModel.getDeadlineDate());
            cv.put(COLUMN_TASK_DEADLINE_TIME, taskModel.getDeadlineTime());

            long insert = db.insert(TASK_TABLE, null, cv);
            if (insert == -1)
                return false;
            else
                return true;

    }

    public boolean delete(String taskName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + TASK_TABLE + " WHERE " + COLUMN_TASK_NAME + "=\'" + taskName+'\'';

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst())
            return true;
        else
            return false;
    }

    public List<TaskModel> getAll() {
        List<TaskModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TASK_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String taskName = cursor.getString(1);
                String taskDeadlineDate = cursor.getString(2);
                String taskDeadLineTime = cursor.getString(3);

                TaskModel taskModel = new TaskModel(id, taskName, taskDeadlineDate, taskDeadLineTime);
                returnList.add(taskModel);

            } while (cursor.moveToNext());
        }

        sortTasks(returnList);

        cursor.close();
        db.close();
        return returnList;
    }

    public void sortTasks (List<TaskModel> taskModels){
        Collections.sort(taskModels, new Comparator<TaskModel>() {
            @Override
            public int compare(TaskModel t1, TaskModel t2) {
                String date1 = t1.getDeadlineDate();
                String date2 = t2.getDeadlineDate();
                int dateCompare = date1.compareTo(date2);

                if(dateCompare != 0){
                    return dateCompare;
                }

                String time1 = t1.getDeadlineTime();
                String time2 = t2.getDeadlineTime();
                return time1.compareTo(time2);
            }
        });
    }
}
