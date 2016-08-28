package mcgill.ca.fragilitysurvey.repo;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mcgill.ca.fragilitysurvey.quiz.questions.Inputter;
import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.quiz.questions.Questions;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;
import mcgill.ca.fragilitysurvey.repo.entity.answer.AnswerType;
import mcgill.ca.fragilitysurvey.repo.entity.answer.IAnswer;

/**
 * Store hierarchy of the Answers
 */
public class AnswerRepository extends BaseRepository {

    private static final String TAG = "repository.survey";

    public static String ANSWER_TABLE_NAME = "answer";

    //join to the question
    public static String SURVEY_ID = "survey_id";
    public static String QUESTION_ID = "question_id";
    public static String QUESTION_TEXT = "question_text";
    public static String INPUTTER_ID = "inputter_id";
    //value fields
    public static String ANSWER_TYPE = "type";
    public static String ANSWER_VALUE = "value";

    public static String SQL_CREATE_ANSWER_TABLE = "CREATE TABLE " + ANSWER_TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY, " +
            QUESTION_ID + " INTEGER NOT NULL, " +
            QUESTION_TEXT + " TEXT NOT NULL, " +
            SURVEY_ID + " STRING NOT NULL, " +
            INPUTTER_ID + " INTEGER NOT NULL, " +
            ANSWER_TYPE + " INTEGER NOT NULL, " +
            ANSWER_VALUE + " TEXT NOT NULL" +
            " )";

    public static final String SQL_DELETE_ANSWER_TABLE =
            "DROP TABLE IF EXISTS " + ANSWER_TABLE_NAME;

    private final DBContext dbContext;

    public AnswerRepository(DBContext dbContext) {
        this.dbContext = dbContext;
    }


    public AnswerRepository insertQuestions(Survey survey, List<Question> questions) {
        for (Question q : questions) {
            saveQuestion(survey, q);
        }
        return this;
    }

    private AnswerRepository saveQuestion(Survey survey, Question q) {
        deleteQuestionFromSurvey(survey, q);
        for (Inputter inputter : q.inputters()) {
            saveInputter(survey, q, inputter);
        }
        return this;
    }

    public AnswerRepository deleteQuestionFromSurvey(Survey survey, Question q) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        db.delete(
                ANSWER_TABLE_NAME,
                SURVEY_ID + "=? and " + QUESTION_ID+ "=?",
                new String[]{survey.surveyId(), String.valueOf(q.id())});
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
        values.put(QUESTION_ID, q.id());
        values.put(QUESTION_TEXT, q.questionText());
        values.put(SURVEY_ID, survey.surveyId());
        values.put(INPUTTER_ID, inputter.id());
        values.put(ANSWER_TYPE, inputter.inputType().id);
        values.put(ANSWER_VALUE, inputter.inputType().fromAnswer(answer));

        long newRowId = db.insert(ANSWER_TABLE_NAME, null, values);
        Log.d(TAG, "Inserted answer with id - " + newRowId);
        return this;
    }

    //very dirty method, because of the bad DB model. It can be faster to store evrything in the single table
    public List<Question> getBySurveyId(String surveyId, Resources res) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Map<Integer, Question> questionsMap = new HashMap<>();
        Map<Integer, Map<Integer, Inputter>> inputtersMap = new HashMap<>();
        try(Cursor surveyCursor = db.rawQuery("SELECT " +
                QUESTION_ID + ", " +
                INPUTTER_ID + ", " +
                ANSWER_TYPE + ", " +
                ANSWER_VALUE + "" +
                " FROM " + ANSWER_TABLE_NAME +
                " WHERE " +
                SURVEY_ID + " = '" + surveyId + "'", new String[]{})){
            while(surveyCursor.moveToNext()) {
                //create questions
                int questionId = surveyCursor.getInt(surveyCursor.getColumnIndex(QUESTION_ID));
                Question templateQuestion = Questions.getQuestionById(questionId, res);
                Question question = questionsMap.get(questionId);
                if(question == null){
                    question = new Question().id(questionId);
                    questionsMap.put(questionId, question);
                    question.questionText(templateQuestion.questionText());
//                    question.inputers(new ArrayList<>(templateQuestion.inputters()));
                }
                //create inputters
                int inputterId = surveyCursor.getInt(surveyCursor.getColumnIndex(INPUTTER_ID));
                Map<Integer, Inputter> inputterSubMap = inputtersMap.get(questionId);
                if(inputterSubMap == null){
                    inputterSubMap = new HashMap<>();
                    inputtersMap.put(questionId, inputterSubMap);
                }
                Inputter inputter = inputterSubMap.get(inputterId);
                Inputter templateInputter = templateQuestion.getInputterById(inputterId);
                if(inputter == null){
                    inputter = new Inputter().id(inputterId);
                    inputter.caption(templateInputter.caption());
                    inputter.options(templateInputter.options());
                    inputter.orLogic(templateInputter.isOrLogic());
                    inputter.inputType(templateInputter.inputType());
                    inputterSubMap.put(inputterId, inputter);
                    question.addInputter(inputter);
                }
                //create answer
                int answerTypeId = surveyCursor.getInt(surveyCursor.getColumnIndex(ANSWER_TYPE));
                AnswerType answerType = AnswerType.fromId(answerTypeId);
                if(answerType != null) {
                    String answerValue = surveyCursor.getString(surveyCursor.getColumnIndex(ANSWER_VALUE));
                    IAnswer answer = answerType.toAnswer(answerValue);
                    inputter.inputType(answerType);
                    inputter.addAnswer(answer);
                } else {
                    Log.e(TAG, "Cannot find answerType for id " + answerTypeId);
                }
            }
        }
        return new ArrayList<>(questionsMap.values());
    }
}
