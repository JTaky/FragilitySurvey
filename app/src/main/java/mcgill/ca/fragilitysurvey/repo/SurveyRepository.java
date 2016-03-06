package mcgill.ca.fragilitysurvey.repo;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mcgill.ca.fragilitysurvey.repo.entity.Survey;

public class SurveyRepository extends BaseRepository {

    private static final String TAG = "repository.survey";

    public static String TABLE_NAME = "survey";

    //join to the question
    public static String SURVEY_ID = "survey_id";
    public static String TIMESTAMP = "timestamp";

    public static String SQL_CREATE_SURVEY_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    SURVEY_ID + " TEXT NOT NULL, " +
                    TIMESTAMP + " INTEGER NOT NULL" +
                    " )";

    public static final String SQL_DELETE_SURVEY_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private final DBContext dbContext;

    public SurveyRepository(DBContext dbContext){
        this.dbContext = dbContext;
    }

    public SurveyRepository saveSurvey(Survey survey) {
        SQLiteDatabase db = dbContext.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SURVEY_ID, survey.surveyId());
        values.put(TIMESTAMP, survey.timestamp().getTime());

        long newRowId = db.insert(TABLE_NAME, SURVEY_ID, values);
        Log.d(TAG, "Inserted survey with id - " + newRowId);
        return this;
    }

    public List<Survey> getSurveys() {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        List<Survey> surveyList = new ArrayList<>();
        try(Cursor surveyCursor = db.rawQuery("SELECT " +
                SURVEY_ID + ", " +
                TIMESTAMP + "" +
                " FROM " + TABLE_NAME, new String[]{})){
            while(surveyCursor.moveToNext()) {
                String surveyId = surveyCursor.getString(surveyCursor.getColumnIndex(SURVEY_ID));
                long timestamp = surveyCursor.getLong(surveyCursor.getColumnIndex(TIMESTAMP));
                surveyList.add(
                        new Survey().surveyId(surveyId).timestamp(new Date(timestamp))
                );
            }
        }
        return surveyList;
    }
}
