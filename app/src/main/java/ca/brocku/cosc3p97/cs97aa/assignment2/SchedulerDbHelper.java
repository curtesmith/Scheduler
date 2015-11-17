package ca.brocku.cosc3p97.cs97aa.assignment2;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SchedulerDbHelper extends SQLiteOpenHelper{
    SQLiteDatabase db;
    private static final String DATABASE_NAME = "Scheduler";
    private static final int DATABASE_VERSION = 1;

    public SchedulerDbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        db = getWritableDatabase();
    }

    public SchedulerDbHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String drop = "DROP TABLE IF EXISTS meetings;";
        db.execSQL(drop);

        final String create = "CREATE TABLE IF NOT EXISTS meetings " +
                "( title TEXT NOT NULL, datetime TEXT NOT NULL, duration INTEGER);";
        db.execSQL(create);

        fill(db);

    }


    public void fill(SQLiteDatabase db) {

        String date = "2015-11-15";

        String fill = "INSERT INTO meetings (datetime, title, duration)" +
                "VALUES ('" + date + " 09:00', 'Go for a run', 20), " +
                "('" + date + " 10:00', 'Brunch with the wife', 60), " +
                "('" + date + " 11:00', 'Dentist appointment', 15), " +
                "('" + date + " 12:00', 'Lunch with parents', 30), " +
                "('" + date + " 13:00', 'Meeting Lori and Brian after lunch', 45), " +
                "('" + date + " 13:30', 'Drop off the stuff', 5), " +
                "('" + date + " 14:00', 'Go to Service Ontario to get my health card', 90), " +
                "('" + date + " 15:00', 'Daily issues conference call', 10), " +
                "('" + date + " 17:00', 'Quitting time', 0), " +
                "('" + date + " 18:00', 'Dinner with th in-laws', 120), " +
                "('" + date + " 20:00', 'Go to bed', 0);";

        db.execSQL(fill);

        date = "2015-11-17";

        fill = "INSERT INTO meetings (datetime, title, duration)" +
                "VALUES ('" + date + " 09:00', 'Go for a run again', 20), " +
                "('" + date + " 10:00', 'Brunch with the wife again', 60), " +
                "('" + date + " 11:00', 'Dentist appointment again', 15), " +
                "('" + date + " 12:00', 'Lunch with parents again', 30), " +
                "('" + date + " 13:00', 'Meeting Lori and Brian after lunch again', 45), " +
                "('" + date + " 13:30', 'Drop off the stuff again', 5), " +
                "('" + date + " 14:00', 'Go to Service Ontario to get my health card again', 90), " +
                "('" + date + " 15:00', 'Daily issues conference call again', 10), " +
                "('" + date + " 17:00', 'Quitting time again', 0), " +
                "('" + date + " 18:00', 'Dinner with th in-laws again', 120), " +
                "('" + date + " 20:00', 'Go to bed again', 0);";
        db.execSQL(fill);

    }



    public String[] selectFromMeetings(String date) {
        Cursor cursor = db.rawQuery("select rowid, time(datetime) as meeting_time, title from meetings where date(datetime) = '"
                + date + "' ORDER BY meeting_time ASC", new String[]{});

        ArrayList<String> result = new ArrayList<>();
        boolean more = cursor.moveToFirst();
        while(more) {
            result.add(cursor.getString(cursor.getColumnIndex("meeting_time")) + " - " + cursor.getString(cursor.getColumnIndex("title")));
            more = cursor.moveToNext();
        }

        return result.toArray(new String[result.size()]);
    }


    private ArrayList<String> dumpCursor(Cursor cursor) {
        String[] columns = new String[cursor.getColumnCount()];
        for(int i=0; i<cursor.getColumnCount(); i++) {
            columns[i] = cursor.getColumnName(i);
        }

        boolean more = cursor.moveToFirst();
        ArrayList<String> result = new ArrayList<>();
        while(more) {
            StringBuilder line = new StringBuilder();
            for (String column : columns) {
                line.append("{");
                line.append(column);
                line.append("=");
                line.append(cursor.getString(cursor.getColumnIndex(column)));
                line.append("}");
            }
            result.add(line.toString());
            more = cursor.moveToNext();
        }

        return result;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        onCreate(db);
    }
}
