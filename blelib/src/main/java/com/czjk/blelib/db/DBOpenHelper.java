package com.czjk.blelib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.czjk.blelib.utils.BleLog;


public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "db_name";
    private static final int DB_VERSION = 1;
    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STEP);
        db.execSQL(CREATE_TABLE_ALARM);
        db.execSQL(CREATE_TABLE_MEDICATION);
        db.execSQL(CREATE_TABLE_DRINK);
        db.execSQL(CREATE_TABLE_SEDENTARY);
        db.execSQL(CREATE_TABLE_HEARTRATE);
        BleLog.i("onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DB_NAME);
    }

    private static final String CREATE_TABLE_STEP = "CREATE TABLE "
            + DBConstants.TABLE_NAME_STEP
            + " (" + DBConstants.STEP_FIELD_DATE + " TEXT primary key, "
            + DBConstants.STEP_FIELD_STEP + " INTEGER,"
            + DBConstants.STEP_FIELD_TYPE + " INTEGER);";


    private static final String CREATE_TABLE_ALARM = "CREATE TABLE "
            + DBConstants.TABLE_NAME_ALARM
            + " (" + DBConstants.ALARM_FIELD_ID
            + " INTEGER primary key autoincrement, "
            + DBConstants.ALARM_FIELD_NAME + " TEXT,"
            + DBConstants.ALARM_FIELD_TIME + " TEXT,"
            + DBConstants.ALARM_FIELD_REPEAT + " TEXT,"
            + DBConstants.ALARM_FIELD_STATE + " TEXT);";


    private static final String CREATE_TABLE_MEDICATION = "CREATE TABLE "
            + DBConstants.TABLE_NAME_MEDICATION
            + " (" + DBConstants.MEDICATION_FIELD_ID
            + " INTEGER primary key autoincrement, "
            + DBConstants.MEDICATION_FIELD_TIME1 + " TEXT,"
            + DBConstants.MEDICATION_FIELD_TIME2 + " TEXT,"
            + DBConstants.MEDICATION_FIELD_TIME3 + " TEXT,"
            + DBConstants.MEDICATION_FIELD_REPEAT + " TEXT,"
            + DBConstants.MEDICATION_FIELD_STATE + " TEXT,"
            + DBConstants.MEDICATION_FIELD_STATE1 + " TEXT,"
            + DBConstants.MEDICATION_FIELD_STATE2 + " TEXT,"
            + DBConstants.MEDICATION_FIELD_STATE3 + " TEXT);";

    private static final String CREATE_TABLE_DRINK = "CREATE TABLE "
            + DBConstants.TABLE_NAME_DRINK
            + " (" + DBConstants.DRINK_FIELD_ID
            + " INTEGER primary key autoincrement, "
            + DBConstants.DRINK_FIELD_TIME + " TEXT,"
            + DBConstants.DRINK_FIELD_INTERVALMINUTES + " TEXT,"
            + DBConstants.DRINK_FIELD_FREQUENCY + " TEXT,"
            + DBConstants.DRINK_FIELD_STATE + " TEXT);";

    private static final String CREATE_TABLE_SEDENTARY = "CREATE TABLE "
            + DBConstants.TABLE_NAME_SEDENTARY
            + " (" + DBConstants.SEDENTARY_FIELD_ID
            + " INTEGER primary key autoincrement, "
            + DBConstants.SEDENTARY_FIELD_STARTTIME + " TEXT,"
            + DBConstants.SEDENTARY_FIELD_ENDTIME + " TEXT,"
            + DBConstants.SEDENTARY_FIELD_DETECTIONTIME + " TEXT,"
            + DBConstants.SEDENTARY_FIELD_REPEAT + " TEXT,"
            + DBConstants.SEDENTARY_FIELD_STATE + " TEXT);";

    private static final String CREATE_TABLE_HEARTRATE = "CREATE TABLE "
            + DBConstants.TABLE_NAME_HEARTRATE
            + " (" + DBConstants.HEARTRATE_FIELD_DATE
            + " INTEGER primary key, "
            + DBConstants.HEARTRATE_FIELD_TIME + " TEXT,"
            + DBConstants.HEARTRATE_FIELD_VALUE + " TEXT);";



}
