package mcgill.ca.fragilitysurvey.repo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import mcgill.ca.fragilitysurvey.quiz.questions.Inputter;
import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;
import mcgill.ca.fragilitysurvey.repo.entity.answer.IAnswer;

/**
 * Store hierarchy of the Answers
 */
public class AnswerRepository extends BaseRepository {

    private static final String TAG = "repository.survey";

    public static String TABLE_NAME = "answer";

    //join to the question
    public static String SURVEY_ID = "survey_id";
    public static String QUESTION_ID = "question_id";
    public static String INPUTTER_ID = "inputter_id";
    //value fields
    public static String ANSWER_TYPE = "type";
    public static String ANSWER_VALUE = "value";

    public static String SQL_CREATE_ANSWER_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY, " +
            SURVEY_ID + " STRING, " +
            QUESTION_ID + " INTEGER, " +
            INPUTTER_ID + " INTEGER, " +
            ANSWER_TYPE + " INTEGER, " +
            ANSWER_VALUE + " TEXT " +
            " )";

    public static final String SQL_DELETE_ANSWER_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private final DBContext dbContext;

    public AnswerRepository(DBContext dbContext) {
        this.dbContext = dbContext;
    }


    public AnswerRepository saveQuestions(Survey survey, List<Question> questions) {
        for (Question q : questions) {
            saveQuestion(survey, q);
        }
        return this;
    }

    private AnswerRepository saveQuestion(Survey survey, Question q) {
        for (Inputter inputter : q.inputters()) {
            saveInputter(survey, q, inputter);
        }
        return this;
    }

    private AnswerRepository saveInputter(Survey survey, Question q, Inputter inputter) {
        for (IAnswer answer : inputter.answers()) {
            saveAnswer(survey, q, inputter, answer);
        }
        return this;
    }

    private AnswerRepository saveAnswer(Survey survey, Question q, Inputter inputter, IAnswer answer) {
        SQLiteDatabase db = dbContext.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SURVEY_ID, survey.surveyId());
        values.put(QUESTION_ID, q.id());
        values.put(INPUTTER_ID, inputter.id());
        values.put(ANSWER_TYPE, inputter.inputType().id);
        values.put(ANSWER_VALUE, inputter.inputType().fromAnswer(answer));

        long newRowId = db.insert(TABLE_NAME, null, values);
        Log.d(TAG, "Inserted answer with id - " + newRowId);
        return this;
    }

}
