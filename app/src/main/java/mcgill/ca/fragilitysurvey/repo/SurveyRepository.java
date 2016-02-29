package mcgill.ca.fragilitysurvey.repo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
                    SURVEY_ID + " INTEGER, " +
                    TIMESTAMP + " INTEGER " +
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
}
