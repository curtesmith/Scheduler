package ca.brocku.cosc3p97.cs97aa.assignment2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SchedulerDbHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    Context context;
    private static final String DATABASE_NAME = "Scheduler";
    private static final int DATABASE_VERSION = 1;
    private static final String MEETINGS_TABLE_NAME = "meetings";
    private static final String INVITEES_TABLE_NAME  = "invitees";
    private static final String TITLE_COLUMN_NAME = "title";
    private static final String DATE_COLUMN_NAME = "meeting_date";
    private static final String TIME_COLUMN_NAME = "_meeting_time";
    private static final String DURATION_COLUMN_NAME = "duration";
    private static final String MEETINGID_COLUMN_NAME = "meeting_id";
    private static final String NAME_COLUMN_NAME = "name";


    public SchedulerDbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        db = getWritableDatabase();
    }

    public SchedulerDbHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String dropMeetings = "DROP TABLE IF EXISTS " + MEETINGS_TABLE_NAME + ";";
        db.execSQL(dropMeetings);

        final String createMeetings = "CREATE TABLE IF NOT EXISTS " + MEETINGS_TABLE_NAME +
                " (" + TITLE_COLUMN_NAME + " TEXT NOT NULL, " + DATE_COLUMN_NAME + " TEXT NOT NULL," +
                TIME_COLUMN_NAME + " TEXT NOT NULL, " + DURATION_COLUMN_NAME + " INTEGER);";
        db.execSQL(createMeetings);

        final String dropInvitees = "DROP TABLE IF EXISTS " + INVITEES_TABLE_NAME +";";
        db.execSQL(dropInvitees);

        final String createInvitees = "CREATE TABLE IF NOT EXISTS " + INVITEES_TABLE_NAME +
                " (" + MEETINGID_COLUMN_NAME + " INTEGER NOT NULL, " + NAME_COLUMN_NAME + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + MEETINGID_COLUMN_NAME + ") REFERENCES " +  MEETINGS_TABLE_NAME + "(rowid))";
        db.execSQL(createInvitees);

        fill(db);

    }


    public void fill(SQLiteDatabase db) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(c.getTime());

        c.add(Calendar.DATE, 1);
        String tomorrow = dateFormat.format(c.getTime());


        String date = today;

        String fill = "INSERT INTO " + MEETINGS_TABLE_NAME + " (" + DATE_COLUMN_NAME +
                ", " + TIME_COLUMN_NAME +  ", " + TITLE_COLUMN_NAME +
                ", " + DURATION_COLUMN_NAME + ")" +
                "VALUES ('" + date + "', '09:00', 'Go for a run', 20), " +
                "('" + date + "', '10:00', 'Brunch with the wife', 60), " +
                "('" + date + "', '11:00', 'Dentist appointment', 15), " +
                "('" + date + "', '12:00', 'Lunch with parents', 30), " +
                "('" + date + "', '13:00', 'Meeting Lori and Brian after lunch', 45), " +
                "('" + date + "', '13:30', 'Drop off the stuff', 5), " +
                "('" + date + "', '14:00', 'Go to Service Ontario to get my health card', 90), " +
                "('" + date + "', '15:00', 'Daily issues conference call', 10), " +
                "('" + date + "', '17:00', 'Quitting time', 0), " +
                "('" + date + "', '18:00', 'Dinner with th in-laws', 120), " +
                "('" + date + "', '20:00', 'Go to bed', 0);";

        db.execSQL(fill);

        date = tomorrow;

        fill =  "INSERT INTO " + MEETINGS_TABLE_NAME + " (" + DATE_COLUMN_NAME +
                ", " + TIME_COLUMN_NAME +  ", " + TITLE_COLUMN_NAME +
                ", " + DURATION_COLUMN_NAME + ")" +
                "VALUES ('" + date + "', '09:00', 'Go for a run again', 20), " +
                "('" + date + "', '10:00', 'Brunch with the wife again', 60), " +
                "('" + date + "', '11:00', 'Dentist appointment again', 15), " +
                "('" + date + "', '12:00', 'Lunch with parents again', 30), " +
                "('" + date + "', '13:00', 'Meeting Lori and Brian after lunch again', 45), " +
                "('" + date + "', '13:30', 'Drop off the stuff again', 5), " +
                "('" + date + "', '14:00', 'Go to Service Ontario to get my health card again', 90), " +
                "('" + date + "', '15:00', 'Daily issues conference call again', 10), " +
                "('" + date + "', '17:00', 'Quitting time again', 0), " +
                "('" + date + "', '18:00', 'Dinner with th in-laws again', 120), " +
                "('" + date + "', '20:00', 'Go to bed again', 0);";
        db.execSQL(fill);

    }


    public List<MeetingsListItem> selectFromMeetings(String date) {
        final Cursor cursor = db.rawQuery("select rowid, date(" + DATE_COLUMN_NAME +
                ") as meeting_date, strftime('%H:%M', " + TIME_COLUMN_NAME +
                ") as meeting_time, " + TITLE_COLUMN_NAME + ", " + DURATION_COLUMN_NAME +
                " from " + MEETINGS_TABLE_NAME + " where date(" + DATE_COLUMN_NAME +
                ") = '" + date + "' ORDER BY meeting_time ASC", new String[]{});

        ArrayList<MeetingsListItem> list = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MeetingsListItem item = new MeetingsListItem();
            item.id = cursor.getInt(cursor.getColumnIndex("rowid"));
            item.time = cursor.getString(cursor.getColumnIndex("meeting_time"));
            item.date = cursor.getString(cursor.getColumnIndex("meeting_date"));
            item.title = cursor.getString(cursor.getColumnIndex(TITLE_COLUMN_NAME));
            item.duration = cursor.getString(cursor.getColumnIndex(DURATION_COLUMN_NAME));
            item.invitees = selectFromInvitees(item.id);
            list.add(item);
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }


    private ArrayList<String> selectFromInvitees(int meetingID) {
        final Cursor cursor = db.rawQuery("select " + NAME_COLUMN_NAME + " from " + INVITEES_TABLE_NAME +
                " where " + MEETINGID_COLUMN_NAME + " = " + meetingID + " ORDER BY " + NAME_COLUMN_NAME + " ASC", new String[]{});
        ArrayList<String> invitees = new ArrayList<>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                invitees.add(cursor.getString(cursor.getColumnIndex(NAME_COLUMN_NAME)));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return invitees;
    }

    public void insert(String title, String date, String time, Integer duration, List<ContactsListItem> invitees) {
        ContentValues values = new ContentValues();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        calendar.setTime(dateFormat.parse(date, new ParsePosition(0)));


        values.put(DATE_COLUMN_NAME, dateFormat.format(calendar.getTime()));

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        calendar.setTime(timeFormat.parse(time, new ParsePosition(0)));
        values.put(TIME_COLUMN_NAME, timeFormat.format(calendar.getTime()));


        values.put(TITLE_COLUMN_NAME, title);
        values.put(DURATION_COLUMN_NAME, duration);
        long meetingID = db.insert(MEETINGS_TABLE_NAME, null, values);

        for(int i=0; i<invitees.size(); i++) {
            values.clear();
            values.put(MEETINGID_COLUMN_NAME, meetingID);
            values.put(NAME_COLUMN_NAME, invitees.get(i).name);
            db.insert(INVITEES_TABLE_NAME, null, values);
        }
    }


    public void delete(int id) {
        db.delete(INVITEES_TABLE_NAME, MEETINGID_COLUMN_NAME + "=" + id, null);
        db.delete(MEETINGS_TABLE_NAME, "rowid=" + id, null);
    }


    public void deleteBefore(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        db.execSQL("DELETE FROM invitees WHERE invitees.meeting_id IN (SELECT meetings.rowid FROM meetings LEFT JOIN invitees ON meetings.rowid = invitees.meeting_id WHERE meetings.meeting_date <'" + dateFormat.format(date) + "')");
        db.execSQL("DELETE FROM meetings WHERE meeting_date < '" + dateFormat.format(date) + "'");
    }

    public void delete(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        db.execSQL("DELETE FROM invitees WHERE invitees.meeting_id IN (SELECT meetings.rowid FROM meetings LEFT JOIN invitees ON meetings.rowid = invitees.meeting_id WHERE meetings.meeting_date ='" + dateFormat.format(date) + "')");
        db.execSQL("DELETE FROM meetings WHERE meeting_date = '" + dateFormat.format(date) + "'");
    }


    public void deleteAll() {
        db.execSQL("DELETE FROM invitees;");
        db.execSQL("DELETE FROM meetings;");
    }


    private String dumpCursorAsString(Cursor cursor) {
        ArrayList<String> list = dumpCursor(cursor);
        StringBuilder result = new StringBuilder();
        for(int i=0; i<list.size(); i++) {
            result.append("[");
            result.append(list.get(i));
            result.append("]");
        }

        return result.toString();
    }

    private ArrayList<String> dumpCursor(Cursor cursor) {
        String[] columns = new String[cursor.getColumnCount()];
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            columns[i] = cursor.getColumnName(i);
        }

        boolean more = cursor.moveToFirst();
        ArrayList<String> result = new ArrayList<>();
        while (more) {
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


    public void push(Date fromDate, Date toDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        db.execSQL("UPDATE " + MEETINGS_TABLE_NAME + " SET " + DATE_COLUMN_NAME +
                " = '" + dateFormat.format(toDate) +
                "' WHERE date(" + DATE_COLUMN_NAME + ") = '" + dateFormat.format(fromDate) + "';");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        onCreate(db);
    }
}
