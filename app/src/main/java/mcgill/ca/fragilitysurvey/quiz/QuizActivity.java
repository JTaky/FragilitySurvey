package mcgill.ca.fragilitysurvey.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import java.util.LinkedList;
import java.util.List;

import mcgill.ca.fragilitysurvey.R;
import mcgill.ca.fragilitysurvey.repo.DBContext;
import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.repo.SurveyService;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "questions";

    public static final String QUESTIONS_KEY = "mcgill.ca.fragilitysurvey";

    private final LinkedList<Question> previousQuestions = new LinkedList<>();
    private final LinkedList<Question> nextQuestions = new LinkedList<>();
    private Question cur;

    private View.OnClickListener nextOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(nextQuestions.isEmpty()){
                //submit
                previousQuestions.add(cur);
                for(Question cur : previousQuestions){
                    Log.i(TAG, cur.toString());
                }
                saveSurvey(previousQuestions);
                finish();
            } else {
                //show next question
                moveNext();
                displayQuestion();
            }
        }
    };

    private View.OnClickListener prevOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            movePrev();
            displayQuestion();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        initBtns();
        displayQuestions();
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
        List<Question> questions = getIntent().getParcelableArrayListExtra(QUESTIONS_KEY);
        nextQuestions.clear();
        nextQuestions.addAll(questions);
        if(!questions.isEmpty()) {
            moveNext();
            displayQuestion();
        }
    }

    private void moveNext(){
        if(cur != null){
            previousQuestions.add(cur);
        }
        cur = nextQuestions.pop();
    }

    private void movePrev(){
        if(cur != null){
            nextQuestions.add(cur);
        }
        cur = previousQuestions.pop();
    }

    private void displayQuestion() {
        ScrollView scrollView = (ScrollView)findViewById(R.id.scrollQuiz);
        scrollView.removeAllViews();
        scrollView.addView(QuestionViewFactory.INSTANCE.createQuestionView(this, cur));
        updateBtnActivity();
    }

    //service level
    private void saveSurvey(LinkedList<Question> questions) {
        SurveyService surveyService = new SurveyService(new DBContext(this));
        surveyService.newSurvey(questions);
    }

}
