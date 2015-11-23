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

/**
 * A class to facilitate database interaction with sqlite
 */
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
    private static final String MEETING_ID_COLUMN_NAME = "meeting_id";
    private static final String NAME_COLUMN_NAME = "name";


    /**
     * Constructor for the class
     * @param context The context passed from the caller
     * @param name The name of the database
     * @param factory A cursor factory
     * @param version The version of the database
     */
    public SchedulerDbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        db = getWritableDatabase();
    }


    /**
     * Simplified overloaded constructor
     * @param context The context passed from the caller
     */
    public SchedulerDbHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    /**
     * Callback that is invoked with the creation which is used here to initialize the
     * tables of the database
     * @param db An instance of the sqlite database that has been created
     */
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
                " (" + MEETING_ID_COLUMN_NAME + " INTEGER NOT NULL, " + NAME_COLUMN_NAME + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + MEETING_ID_COLUMN_NAME + ") REFERENCES " +  MEETINGS_TABLE_NAME + "(rowid))";
        db.execSQL(createInvitees);

        fill(db);

    }


    /**
     * utility method created to fill up the tables with some sample data used for testing
     * @param db An instance of the sqlitedatabase
     */
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


    /**
     * Retrieve a list of meetings from the database where the meeting date matches
     * the date that is passed as a parameter
     * @param date The date to match
     * @return A list of meetings
     */
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


    /**
     * Retrieve a list of invitees matching on the meetingID that is passed as a paramter
     * @param meetingID Identified of the meeting used to query for a list of invited contacts
     * @return A list of invited contacts
     */
    private ArrayList<String> selectFromInvitees(int meetingID) {
        final Cursor cursor = db.rawQuery("select " + NAME_COLUMN_NAME + " from " + INVITEES_TABLE_NAME +
                " where " + MEETING_ID_COLUMN_NAME + " = " + meetingID + " ORDER BY " + NAME_COLUMN_NAME + " ASC", new String[]{});
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


    /**
     * Insert a new meeting into the database
     * @param title A title for the meeting
     * @param date The date of the meeting
     * @param time The time of the meeting
     * @param duration The duration of the meeting
     * @param invitees The list of contacts that are invited to the meeting
     */
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
            values.put(MEETING_ID_COLUMN_NAME, meetingID);
            values.put(NAME_COLUMN_NAME, invitees.get(i).name);
            db.insert(INVITEES_TABLE_NAME, null, values);
        }
    }


    /**
     * Delete a meeting from the database
     * @param id Identifier of the meeting
     */
    public void delete(int id) {
        db.delete(INVITEES_TABLE_NAME, MEETING_ID_COLUMN_NAME + "=" + id, null);
        db.delete(MEETINGS_TABLE_NAME, "rowid=" + id, null);
    }


    /**
     * Delete all meetings that where scheduled to have taken place prior to the date that
     * is passed as a parameter
     * @param date The date used to compare to
     */
    public void deleteBefore(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        db.execSQL("DELETE FROM invitees WHERE invitees.meeting_id IN (SELECT meetings.rowid FROM meetings LEFT JOIN invitees ON meetings.rowid = invitees.meeting_id WHERE meetings.meeting_date <'" + dateFormat.format(date) + "')");
        db.execSQL("DELETE FROM meetings WHERE meeting_date < '" + dateFormat.format(date) + "'");
    }


    /**
     * Delete all the meetings that were scheduled to take place on the date
     * that is passed as a parameter
     * @param date The date used to compare to
     */
    public void delete(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        db.execSQL("DELETE FROM invitees WHERE invitees.meeting_id IN (SELECT meetings.rowid FROM meetings LEFT JOIN invitees ON meetings.rowid = invitees.meeting_id WHERE meetings.meeting_date ='" + dateFormat.format(date) + "')");
        db.execSQL("DELETE FROM meetings WHERE meeting_date = '" + dateFormat.format(date) + "'");
    }


    /**
     * Delete all the invitees and meetings from the database
     */
    public void deleteAll() {
        db.execSQL("DELETE FROM invitees;");
        db.execSQL("DELETE FROM meetings;");
    }


    /**
     * Push all meetings scheduled to occur on the fromDate passed as a parameter
     * and reschedule them to take place on the toDate passed as a parameter
     * @param fromDate A date that meetings were scheduled
     * @param toDate A date that the meetings need to be rescheduled
     */
    public void push(Date fromDate, Date toDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        db.execSQL("UPDATE " + MEETINGS_TABLE_NAME + " SET " + DATE_COLUMN_NAME +
                " = '" + dateFormat.format(toDate) +
                "' WHERE date(" + DATE_COLUMN_NAME + ") = '" + dateFormat.format(fromDate) + "';");
    }


    /**
     * Callback method invoked when the database version has been modified
     * @param db The instance of the database
     * @param oldVersion The previous version #
     * @param newVersion The next version #
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
