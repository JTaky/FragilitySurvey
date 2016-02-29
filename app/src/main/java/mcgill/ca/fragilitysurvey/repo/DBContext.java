package mcgill.ca.fragilitysurvey.repo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBContext extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "FeedReader.db";

    public DBContext(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AnswerRepository.SQL_CREATE_ANSWER_TABLE);
        db.execSQL(SurveyRepository.SQL_CREATE_SURVEY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(AnswerRepository.SQL_DELETE_ANSWER_TABLE);
        db.execSQL(SurveyRepository.SQL_DELETE_SURVEY_TABLE);
        onCreate(db);
    }
}
