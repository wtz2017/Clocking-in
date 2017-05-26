package com.test.clocking_in.utils;

import com.test.clocking_in.data.DateTimeRecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = DatabaseHelper.class.getName();

    private static final String DATABASE_NAME = "test_1.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate...database " + DATABASE_NAME + " is created!");
        // For new user
        // Just create the newest tables
        createCurrentNewestTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade...oldVersion = " + oldVersion + ", newVersion = " + newVersion);
        // For old user
        // Loop upgrade old version to new version
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            upgradeTo(db, version);
        }
    }

    private void createCurrentNewestTables(SQLiteDatabase db) {
        // TODO Update this when database version change
        createTable(db);
    }

    private void createTable(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + Table.NAME + "("
                    + Table._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Table.DATE_TIME + " TEXT, "
                    + Table.WEEKS + " INTEGER, "
                    + Table.DAY_OF_WEEK + " INTEGER, "
                    + Table.MILLISECONDS + " INTEGER); ");
        } catch (SQLException ex) {
            Log.e(TAG, "couldn't create table " + Table.NAME);
            throw ex;
        }
    }

    private void upgradeTo(SQLiteDatabase db, int version) {
        Log.d(TAG, "upgradeTo version " + version);
        switch (version) {
            case 2:
                // Do something
                break;

            case 3:
                // Do something
                break;

            default:
                throw new IllegalStateException("Don't know how to upgrade to " + version);
        }
    }

    public long insert(DateTimeRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Table.DATE_TIME, record.getDateTime());
        values.put(Table.WEEKS, record.getWeeks());
        values.put(Table.DAY_OF_WEEK, record.getWeekday());
        values.put(Table.MILLISECONDS, record.getMilliseconds());
        // 如果第二个参数传递的是null，那么系统则不会对那些没有提供数据的列进行填充
        long rowid = db.insert(Table.NAME, null, values);
        return rowid;
    }

    public int deleteByMilliseconds(long milliseconds) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Define 'where' part of query.
        String selection = Table.MILLISECONDS + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selelectionArgs = {String.valueOf(milliseconds)};
        // Issue SQL statement.
        int count = db.delete(Table.NAME, selection, selelectionArgs);
        return count;
    }

    public Cursor queryCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = null;// Passing null will return all columns
        String selection = null;// Passing null will return all columns
        String[] selectionArgs = null;
        String sortOrder = Table.MILLISECONDS + " desc";
        Cursor c = db.query(
                Table.NAME,       // The table to query
                projection,       // The columns to return
                selection,        // The columns for the WHERE clause
                selectionArgs,    // The values for the WHERE clause
                null,             // don't group the rows
                null,             // don't filter by row groups
                sortOrder         // The sort order
        );
        return c;
    }

    public List<DateTimeRecord> queryList() {
        List<DateTimeRecord> list = null;
        Cursor c = queryCursor();
        if (c == null || c.moveToFirst() == false) {
            return list;
        }

        list = new ArrayList<DateTimeRecord>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            DateTimeRecord record = new DateTimeRecord();
            record.setDateTime(c.getString(c.getColumnIndex(Table.DATE_TIME)));
            record.setWeeks(c.getInt(c.getColumnIndex(Table.WEEKS)));
            record.setWeekday(c.getInt(c.getColumnIndex(Table.DAY_OF_WEEK)));
            record.setMilliseconds(c.getLong(c.getColumnIndex(Table.MILLISECONDS)));
            list.add(record);
        }

        return list;
    }

    class Table implements BaseColumns {
        public static final String NAME = "work_time_record";

        public static final String DATE_TIME = "date_time";
        public static final String WEEKS = "weeks";
        public static final String DAY_OF_WEEK = "day_of_week";
        public static final String MILLISECONDS = "milliseconds";
    }
}
