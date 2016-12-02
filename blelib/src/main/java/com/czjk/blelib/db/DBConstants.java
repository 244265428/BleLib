package com.czjk.blelib.db;

public class DBConstants {
    // 步数
    public static final String TABLE_NAME_STEP = "step";
    public static final String STEP_FIELD_DATE = "date";
    public static final String STEP_FIELD_STEP = "step";
    public static final String STEP_FIELD_TYPE = "type";


    // 闹钟
    public static final String TABLE_NAME_ALARM = "alarm";
    public static final String ALARM_FIELD_ID = "id";
    public static final String ALARM_FIELD_NAME = "name";
    public static final String ALARM_FIELD_TIME = "time";
    public static final String ALARM_FIELD_STATE = "state";
    public static final String ALARM_FIELD_REPEAT = "repeat";

    //吃药
    public static final String TABLE_NAME_MEDICATION = "medication";
    public static final String MEDICATION_FIELD_ID = "id";
    public static final String MEDICATION_FIELD_TIME1 = "time1";
    public static final String MEDICATION_FIELD_TIME2 = "time2";
    public static final String MEDICATION_FIELD_TIME3 = "time3";
    public static final String MEDICATION_FIELD_STATE1 = "state1";
    public static final String MEDICATION_FIELD_STATE2 = "state2";
    public static final String MEDICATION_FIELD_STATE3 = "state3";
    public static final String MEDICATION_FIELD_STATE = "state";
    public static final String MEDICATION_FIELD_REPEAT = "repeat";

    //喝水
    public static final String TABLE_NAME_DRINK = "drink";
    public static final String DRINK_FIELD_ID = "id";
    public static final String DRINK_FIELD_TIME = "time";
    public static final String DRINK_FIELD_INTERVALMINUTES = "intervalMinutes";
    public static final String DRINK_FIELD_FREQUENCY = "frequency";
    public static final String DRINK_FIELD_STATE = "state";

    //久坐
    public static final String TABLE_NAME_SEDENTARY = "sedentary";
    public static final String SEDENTARY_FIELD_ID = "id";
    public static final String SEDENTARY_FIELD_STARTTIME = "startTime";
    public static final String SEDENTARY_FIELD_ENDTIME = "endTime";
    public static final String SEDENTARY_FIELD_DETECTIONTIME = "detectionTime";
    public static final String SEDENTARY_FIELD_STATE = "state";
    public static final String SEDENTARY_FIELD_REPEAT = "repeat";

    //心率
    public static final String TABLE_NAME_HEARTRATE = "heartrate";
    public static final String HEARTRATE_FIELD_DATE = "date";
    public static final String HEARTRATE_FIELD_TIME = "time";
    public static final String HEARTRATE_FIELD_VALUE = "value";
}
