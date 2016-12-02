package com.czjk.blelib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.czjk.blelib.model.Alarm;
import com.czjk.blelib.model.DrinkRemind;
import com.czjk.blelib.model.HeartRate;
import com.czjk.blelib.model.MedicationRemind;
import com.czjk.blelib.model.SedentaryRemind;
import com.czjk.blelib.model.Step;
import com.czjk.blelib.utils.BleLog;

import java.util.ArrayList;


public class DBTools {
    private DBOpenHelper myDBOpenHelper;
    private SQLiteDatabase db;
    public static DBTools dbTools;

    public static DBTools getInstance(Context context) {
        if (dbTools == null) {
            dbTools = new DBTools(context);
            return dbTools;
        }
        return dbTools;
    }

    public DBTools(Context context) {
        myDBOpenHelper = new DBOpenHelper(context);
        db = myDBOpenHelper.getWritableDatabase();

    }

    /**
     * 查询
     */
    //指定小时 2016110907
    //指定天 20161109
    //指定月  201611
    public ArrayList<Step> selectStep(String time) {
        String sql = "select * from " + DBConstants.TABLE_NAME_STEP + " where " + DBConstants.STEP_FIELD_DATE + " LIKE ?";
        Cursor cursor = db.rawQuery(sql, new String[]{time + "%"});
        ArrayList<Step> steps = new ArrayList<Step>();
        while (cursor.moveToNext()) {
            Step step = new Step(cursor.getString(cursor.getColumnIndex(DBConstants.STEP_FIELD_DATE)),
                    cursor.getInt(cursor.getColumnIndex(DBConstants.STEP_FIELD_STEP)),
                    cursor.getInt(cursor.getColumnIndex(DBConstants.STEP_FIELD_TYPE)));
            steps.add(step);
        }
        cursor.close();
        return steps;
    }

    public Step selectLastStep() {
        Cursor cursor = db.query(DBConstants.TABLE_NAME_STEP, null, null, null, null, null, null);
        Step step = null;
        if (cursor.moveToLast()) {
            step = new Step(cursor.getString(cursor.getColumnIndex(DBConstants.STEP_FIELD_DATE)),
                    cursor.getInt(cursor.getColumnIndex(DBConstants.STEP_FIELD_STEP)),
                    cursor.getInt(cursor.getColumnIndex(DBConstants.STEP_FIELD_TYPE)));
        }
        cursor.close();
        return step;
    }


    public ArrayList<Alarm> selectAllAlarm() {
        Cursor cursor = db.query(DBConstants.TABLE_NAME_ALARM, null, null, null, null, null, null);
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        while (cursor.moveToNext()) {
            Alarm alarm = new Alarm();
            alarm.id = cursor.getString(cursor.getColumnIndex(DBConstants.ALARM_FIELD_ID));
            alarm.name = cursor.getString(cursor.getColumnIndex(DBConstants.ALARM_FIELD_NAME));
            alarm.time = cursor.getString(cursor.getColumnIndex(DBConstants.ALARM_FIELD_TIME));
            alarm.state = cursor.getString(cursor.getColumnIndex(DBConstants.ALARM_FIELD_STATE));
            alarm.repeat = cursor.getString(cursor.getColumnIndex(DBConstants.ALARM_FIELD_REPEAT));
            alarms.add(alarm);
        }
        cursor.close();
        return alarms;
    }

    public ArrayList<DrinkRemind> selectAllDrink() {
        Cursor cursor = db.query(DBConstants.TABLE_NAME_DRINK, null, null, null, null, null, null);
        ArrayList<DrinkRemind> drinkReminds = new ArrayList<DrinkRemind>();
        while (cursor.moveToNext()) {
            DrinkRemind drinkRemind = new DrinkRemind();
            drinkRemind.id = cursor.getString(cursor.getColumnIndex(DBConstants.DRINK_FIELD_ID));
            drinkRemind.intervalMinutes = cursor.getString(cursor.getColumnIndex(DBConstants.DRINK_FIELD_INTERVALMINUTES));
            drinkRemind.time = cursor.getString(cursor.getColumnIndex(DBConstants.DRINK_FIELD_TIME));
            drinkRemind.state = cursor.getString(cursor.getColumnIndex(DBConstants.DRINK_FIELD_STATE));
            drinkRemind.frequency = cursor.getString(cursor.getColumnIndex(DBConstants.DRINK_FIELD_FREQUENCY));
            drinkReminds.add(drinkRemind);
        }
        cursor.close();
        return drinkReminds;
    }

    public ArrayList<SedentaryRemind> selectAllSedentary() {
        Cursor cursor = db.query(DBConstants.TABLE_NAME_SEDENTARY, null, null, null, null, null, null);
        ArrayList<SedentaryRemind> sedentaryReminds = new ArrayList<SedentaryRemind>();
        while (cursor.moveToNext()) {
            SedentaryRemind sedentaryRemind = new SedentaryRemind();
            sedentaryRemind.id = cursor.getString(cursor.getColumnIndex(DBConstants.SEDENTARY_FIELD_ID));
            sedentaryRemind.startTime = cursor.getString(cursor.getColumnIndex(DBConstants.SEDENTARY_FIELD_STARTTIME));
            sedentaryRemind.endTime = cursor.getString(cursor.getColumnIndex(DBConstants.SEDENTARY_FIELD_ENDTIME));
            sedentaryRemind.detectionTime = cursor.getString(cursor.getColumnIndex(DBConstants.SEDENTARY_FIELD_DETECTIONTIME));
            sedentaryRemind.state = cursor.getString(cursor.getColumnIndex(DBConstants.SEDENTARY_FIELD_STATE));
            sedentaryRemind.repeat = cursor.getString(cursor.getColumnIndex(DBConstants.SEDENTARY_FIELD_REPEAT));
            sedentaryReminds.add(sedentaryRemind);
        }
        cursor.close();
        return sedentaryReminds;
    }

    public ArrayList<MedicationRemind> selectAllMedication() {
        Cursor cursor = db.query(DBConstants.TABLE_NAME_MEDICATION, null, null, null, null, null, null);
        ArrayList<MedicationRemind> medicationReminds = new ArrayList<MedicationRemind>();
        while (cursor.moveToNext()) {
            MedicationRemind medicationRemind = new MedicationRemind();
            medicationRemind.id = cursor.getString(cursor.getColumnIndex(DBConstants.MEDICATION_FIELD_ID));
            medicationRemind.time1 = cursor.getString(cursor.getColumnIndex(DBConstants.MEDICATION_FIELD_TIME1));
            medicationRemind.time2 = cursor.getString(cursor.getColumnIndex(DBConstants.MEDICATION_FIELD_TIME2));
            medicationRemind.time3 = cursor.getString(cursor.getColumnIndex(DBConstants.MEDICATION_FIELD_TIME3));
            medicationRemind.repeat = cursor.getString(cursor.getColumnIndex(DBConstants.MEDICATION_FIELD_REPEAT));
            medicationRemind.t1State = cursor.getString(cursor.getColumnIndex(DBConstants.MEDICATION_FIELD_STATE1));
            medicationRemind.t2State = cursor.getString(cursor.getColumnIndex(DBConstants.MEDICATION_FIELD_STATE2));
            medicationRemind.t3State = cursor.getString(cursor.getColumnIndex(DBConstants.MEDICATION_FIELD_STATE3));
            medicationRemind.state = cursor.getString(cursor.getColumnIndex(DBConstants.MEDICATION_FIELD_STATE));
            medicationReminds.add(medicationRemind);
        }
        cursor.close();
        return medicationReminds;
    }

    public ArrayList<HeartRate> selectHeartRate(int date) {
        String where = DBConstants.HEARTRATE_FIELD_DATE + " = ?";
        String[] whereValue = {date + ""};
        Cursor cursor = db.query(DBConstants.TABLE_NAME_HEARTRATE, new String[]{DBConstants.HEARTRATE_FIELD_DATE}, where, whereValue, null, null, null);
        ArrayList<HeartRate> heartRateList = new ArrayList<HeartRate>();
        while (cursor.moveToNext()) {
            HeartRate heartRate = new HeartRate(
                    cursor.getInt(cursor.getColumnIndex(DBConstants.HEARTRATE_FIELD_DATE)),
                    cursor.getString(cursor.getColumnIndex(DBConstants.HEARTRATE_FIELD_TIME)),
                    cursor.getString(cursor.getColumnIndex(DBConstants.HEARTRATE_FIELD_VALUE)));
            heartRateList.add(heartRate);
        }
        return heartRateList;
    }

    public ArrayList<HeartRate> selectAllHeartRate() {
        Cursor cursor = db.query(DBConstants.TABLE_NAME_HEARTRATE, null, null, null, null, null, null);
        ArrayList<HeartRate> heartRateList = new ArrayList<HeartRate>();
        while (cursor.moveToNext()) {
            HeartRate heartRate = new HeartRate(
                    cursor.getInt(cursor.getColumnIndex(DBConstants.HEARTRATE_FIELD_DATE)),
                    cursor.getString(cursor.getColumnIndex(DBConstants.HEARTRATE_FIELD_TIME)),
                    cursor.getString(cursor.getColumnIndex(DBConstants.HEARTRATE_FIELD_VALUE)));
            heartRateList.add(heartRate);
        }
        cursor.close();
        return heartRateList;
    }

    /**
     * 插入与更新
     */
    public boolean saveStep(Step step) {
        String where = DBConstants.STEP_FIELD_DATE + " = ?";
        String[] whereValue = {step.getDate()};
        Cursor cursor = db.query(DBConstants.TABLE_NAME_STEP, new String[]{DBConstants.STEP_FIELD_DATE}, where, whereValue, null, null, null);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.STEP_FIELD_DATE, step.getDate());
        cv.put(DBConstants.STEP_FIELD_STEP, step.getStep());
        cv.put(DBConstants.STEP_FIELD_TYPE, step.getType());
        long result = 0;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                result = db.update(DBConstants.TABLE_NAME_STEP, cv, where, whereValue);
            } else {
                result = db.insert(DBConstants.TABLE_NAME_STEP, null, cv);
            }
        } catch (Exception e) {

        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return result > 0;
    }

    public boolean saveAlarm(Alarm alarm) {
        String where = DBConstants.ALARM_FIELD_ID + " = ?";
        String[] whereValue = {alarm.id};
        Cursor cursor = db.query(DBConstants.TABLE_NAME_ALARM, new String[]{DBConstants.ALARM_FIELD_ID}, where, whereValue, null, null, null);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.ALARM_FIELD_NAME, alarm.name);
        cv.put(DBConstants.ALARM_FIELD_TIME, alarm.time);
        cv.put(DBConstants.ALARM_FIELD_STATE, alarm.state);
        cv.put(DBConstants.ALARM_FIELD_REPEAT, alarm.repeat);
        long result = 0;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                //更新操作
                result = db.update(DBConstants.TABLE_NAME_ALARM, cv, where, whereValue);
            } else {
                //插入操作
                result = db.insert(DBConstants.TABLE_NAME_ALARM, null, cv);
            }
        } catch (Exception e) {

        } finally {
            if (null != cursor) {
                cursor.close();
            }

        }
        BleLog.e("saveAlarm");
        return result > 0;
    }

    public boolean saveDrink(DrinkRemind drinkRemind) {
        String where = DBConstants.DRINK_FIELD_ID + " = ?";
        String[] whereValue = {drinkRemind.id};
        Cursor cursor = db.query(DBConstants.TABLE_NAME_DRINK, new String[]{DBConstants.DRINK_FIELD_ID}, where, whereValue, null, null, null);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.DRINK_FIELD_TIME, drinkRemind.time);
        cv.put(DBConstants.DRINK_FIELD_FREQUENCY, drinkRemind.frequency);
        cv.put(DBConstants.DRINK_FIELD_INTERVALMINUTES, drinkRemind.intervalMinutes);
        cv.put(DBConstants.DRINK_FIELD_STATE, drinkRemind.state);
        long result = 0;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                //更新操作
                result = db.update(DBConstants.TABLE_NAME_DRINK, cv, where, whereValue);
            } else {
                //插入操作
                result = db.insert(DBConstants.TABLE_NAME_DRINK, null, cv);
            }
        } catch (Exception e) {

        } finally {
            if (null != cursor) {
                cursor.close();
            }

        }
        BleLog.e("saveDrink");
        return result > 0;
    }

    public boolean saveMedication(MedicationRemind medicationRemind) {
        String where = DBConstants.MEDICATION_FIELD_ID + " = ?";
        String[] whereValue = {medicationRemind.id};
        Cursor cursor = db.query(DBConstants.TABLE_NAME_MEDICATION, new String[]{DBConstants.MEDICATION_FIELD_ID}, where, whereValue, null, null, null);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.MEDICATION_FIELD_TIME1, medicationRemind.time1);
        cv.put(DBConstants.MEDICATION_FIELD_TIME2, medicationRemind.time2);
        cv.put(DBConstants.MEDICATION_FIELD_TIME3, medicationRemind.time3);
        cv.put(DBConstants.MEDICATION_FIELD_REPEAT, medicationRemind.repeat);
        cv.put(DBConstants.MEDICATION_FIELD_STATE1, medicationRemind.t1State);
        cv.put(DBConstants.MEDICATION_FIELD_STATE2, medicationRemind.t2State);
        cv.put(DBConstants.MEDICATION_FIELD_STATE3, medicationRemind.t3State);
        cv.put(DBConstants.MEDICATION_FIELD_STATE, medicationRemind.state);
        long result = 0;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                //更新操作
                result = db.update(DBConstants.TABLE_NAME_MEDICATION, cv, where, whereValue);
            } else {
                //插入操作
                result = db.insert(DBConstants.TABLE_NAME_MEDICATION, null, cv);
            }
        } catch (Exception e) {

        } finally {
            if (null != cursor) {
                cursor.close();
            }

        }
        BleLog.e("saveMedication");
        return result > 0;
    }

    public boolean saveSedentary(SedentaryRemind sedentaryRemind) {
        String where = DBConstants.SEDENTARY_FIELD_ID + " = ?";
        String[] whereValue = {sedentaryRemind.id};
        Cursor cursor = db.query(DBConstants.TABLE_NAME_SEDENTARY, new String[]{DBConstants.SEDENTARY_FIELD_ID}, where, whereValue, null, null, null);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.SEDENTARY_FIELD_STARTTIME, sedentaryRemind.startTime);
        cv.put(DBConstants.SEDENTARY_FIELD_ENDTIME, sedentaryRemind.endTime);
        cv.put(DBConstants.SEDENTARY_FIELD_DETECTIONTIME, sedentaryRemind.detectionTime);
        cv.put(DBConstants.SEDENTARY_FIELD_REPEAT, sedentaryRemind.repeat);
        cv.put(DBConstants.SEDENTARY_FIELD_STATE, sedentaryRemind.state);
        long result = 0;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                result = db.update(DBConstants.TABLE_NAME_SEDENTARY, cv, where, whereValue);
            } else {
                result = db.insert(DBConstants.TABLE_NAME_SEDENTARY, null, cv);
            }
        } catch (Exception e) {

        } finally {
            if (null != cursor) {
                cursor.close();
            }

        }
        BleLog.e("saveSedentary");
        return result > 0;
    }

    public boolean saveHeartRate(HeartRate heartRate) {
        String where = DBConstants.HEARTRATE_FIELD_DATE + " = ?";
        String[] whereValue = {heartRate.getDate() + ""};
        Cursor cursor = db.query(DBConstants.TABLE_NAME_HEARTRATE, new String[]{DBConstants.HEARTRATE_FIELD_DATE}, where, whereValue, null, null, null);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.HEARTRATE_FIELD_DATE, heartRate.getDate());
        cv.put(DBConstants.HEARTRATE_FIELD_TIME, heartRate.getTime());
        cv.put(DBConstants.HEARTRATE_FIELD_VALUE, heartRate.getValue());
        long result = 0;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                result = db.update(DBConstants.TABLE_NAME_HEARTRATE, cv, where, whereValue);
            } else {
                result = db.insert(DBConstants.TABLE_NAME_HEARTRATE, null, cv);
            }
        } catch (Exception e) {

        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        BleLog.e("saveHeartRate");
        return result > 0;
    }

    /**
     * 删除
     */
    public void deleteStep(int date) {
        String where = DBConstants.STEP_FIELD_DATE + " = ?";
        String[] whereValue = {Integer.toString(date)};
        db.delete(DBConstants.TABLE_NAME_STEP, where, whereValue);
    }


    public void deleteAlarm(int id) {
        String where = DBConstants.ALARM_FIELD_ID + " = ?";
        String[] whereValue = {Integer.toString(id)};
        db.delete(DBConstants.TABLE_NAME_ALARM, where, whereValue);
    }

    public void deleteAllData() {
        db.delete(DBConstants.TABLE_NAME_STEP, null, null);
        db.delete(DBConstants.TABLE_NAME_ALARM, null, null);
        db.delete(DBConstants.TABLE_NAME_DRINK, null, null);
        db.delete(DBConstants.TABLE_NAME_MEDICATION, null, null);
        db.delete(DBConstants.TABLE_NAME_SEDENTARY, null, null);
        db.delete(DBConstants.TABLE_NAME_HEARTRATE, null, null);
    }

    public void droptable(String tablename) {
        db.execSQL("DROP TABLE IF EXISTS " + tablename);
    }

    public void close() {
        db.close();
    }

}
