package mcgill.ca.fragilitysurvey.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import java.util.LinkedList;
import java.util.List;

import mcgill.ca.fragilitysurvey.R;
import mcgill.ca.fragilitysurvey.quiz.postsubmit.GoodByeAtivity;
import mcgill.ca.fragilitysurvey.quiz.postsubmit.RecommendationActivity;
import mcgill.ca.fragilitysurvey.repo.DBContext;
import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.repo.SurveyService;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;

public class QuizActivity extends AppCompatActivity {

    private static int GOODBYE_REQUEST_CODE = 0;

    private static int RECOMENDATION_REQUEST_CODE = 1;
    private static String EXTRA_KEY_ACTIVITY = "recomendation.survey.id";

    public enum PostSubmitActions {
        NO_ACTIONS(DEFAULT_POST_SUBMIT_ACTION),
        SAY_GOODBYE(DEFAULT_POST_SUBMIT_ACTION + 1),
        SHOW_RECOMENDATIONS(DEFAULT_POST_SUBMIT_ACTION + 2);

        public final int value;

        PostSubmitActions(int value){
            this.value = value;
        }

        public static PostSubmitActions valueOf(int value){
            for(PostSubmitActions postSubmitActions: values()){
                if(postSubmitActions.value == value){
                    return postSubmitActions;
                }
            }
            return NO_ACTIONS;
        }
    }

    public static final int SURVEY_REQUEST_CODE = 1;
    public static final int SURVEY_SCORE_CODE = 2;

    public static int DEFAULT_POST_SUBMIT_ACTION = 0;

    private static final String TAG = "questions";

    public static final String EXTRAS_KEY = "mcgill.ca.fragilitysurvey.extras";
    public static final String POST_SUBMIT_ACTION = "mcgill.ca.fragilitysurvey.post.submit.action";
    public static final String QUESTIONS_KEY = "mcgill.ca.fragilitysurvey.questions";
    public static final String SURVEY_KEY = "mcgill.ca.fragilitysurvey.survey";

    private final LinkedList<Question> previousQuestions = new LinkedList<>();
    private final LinkedList<Question> nextQuestions = new LinkedList<>();
    private Question cur;

    private Survey survey;
    private PostSubmitActions postSubmitAction;

    private View.OnClickListener nextOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(!QuestionViewFactory.INSTANCE.validate(QuizActivity.this, cur, QuizActivity.this)){ //isValid ?
                //is invalid
                Log.i(TAG, "Question wasn't validated, question - " + cur.id());
                return;
            }
            if(nextQuestions.isEmpty()){
                //submit
                previousQuestions.add(cur);
                for(Question cur : previousQuestions){
                    Log.i(TAG, cur.toString());
                }
                saveSurvey(survey, previousQuestions);
                setResult(RESULT_OK, new Intent());
                postSubmitAction();
            } else {
                //show next question
                moveNext();
                displayQuestion();
            }
        }
    };

    private void postSubmitAction() {
        if(postSubmitAction == PostSubmitActions.SAY_GOODBYE){
            //say goodbye
            Intent myIntent = new Intent(QuizActivity.this, GoodByeAtivity.class);
            QuizActivity.this.startActivityForResult(myIntent, GOODBYE_REQUEST_CODE);
        } else if(postSubmitAction == PostSubmitActions.SHOW_RECOMENDATIONS){
            //show recommendations
            Intent myIntent = new Intent(QuizActivity.this, RecommendationActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable(SURVEY_KEY, survey);
            myIntent.putExtra(EXTRAS_KEY, extras);
            QuizActivity.this.startActivityForResult(myIntent, RECOMENDATION_REQUEST_CODE);
        } else {
            finish();
        }
    }

    private View.OnClickListener prevOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
        movePrev();
        displayQuestion();
        }
    };

    private View.OnClickListener closeListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        initSurvey();
        initBtns();
        displayQuestions();
    }

    private void initSurvey() {
        this.survey = getIntent().getBundleExtra(EXTRAS_KEY).getParcelable(SURVEY_KEY);
        this.postSubmitAction = PostSubmitActions.valueOf(getIntent().getIntExtra(POST_SUBMIT_ACTION, DEFAULT_POST_SUBMIT_ACTION));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //save state here
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //restore state here
    }

    private void initBtns() {
        Button btnPrev = (Button) findViewById(R.id.btnQuizPrev);
        btnPrev.setOnClickListener(prevOnClickListener);
        Button btnNext = (Button) findViewById(R.id.btnQuizNext);
        btnNext.setOnClickListener(nextOnClickListener);
        Button btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(closeListener);
    }

    private void updateBtnActivity() {
        Button btnPrev = (Button) findViewById(R.id.btnQuizPrev);
        btnPrev.setVisibility(previousQuestions.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        Button btnNext = (Button) findViewById(R.id.btnQuizNext);
        btnNext.setText(nextQuestions.isEmpty() ?
                        getResources().getString(R.string.quiz_btn_submit) :
                        getResources().getString(R.string.quiz_btn_next)
        );
    }

    private void displayQuestions() {
        List<Question> questions = getIntent().getBundleExtra(EXTRAS_KEY).getParcelableArrayList(QUESTIONS_KEY);
        nextQuestions.clear();
        nextQuestions.addAll(questions);
        assert questions != null;
        if(!questions.isEmpty()) {
            moveNext();
            displayQuestion();
        }
    }

    private void moveNext(){
        if(cur != null){
            previousQuestions.push(cur);
        }
        cur = nextQuestions.pop();
    }

    private void movePrev(){
        if (cur != null){
            nextQuestions.push(cur);
        }
        cur = previousQuestions.pop();
    }

    private void displayQuestion() {
        ScrollView scrollView = (ScrollView)findViewById(R.id.scrollQuiz);
        scrollView.removeAllViews();
        View questionView = QuestionViewFactory.INSTANCE.createQuestionView(this, cur);
        scrollView.addView(questionView);
        updateBtnActivity();
    }

    //service level
    private void saveSurvey(Survey survey, LinkedList<Question> questions) {
        SurveyService surveyService = new SurveyService(new DBContext(this), this.getResources());
        if(survey == null) {
            surveyService.insertNewSurvey(questions);
        } else {
            surveyService.saveSurvey(survey, questions);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }

}
