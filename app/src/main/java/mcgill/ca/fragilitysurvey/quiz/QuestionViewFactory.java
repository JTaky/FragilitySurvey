package mcgill.ca.fragilitysurvey.quiz;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import mcgill.ca.fragilitysurvey.quiz.questions.IQuestion;

public class QuestionViewFactory {

    public static final QuestionViewFactory INSTANCE = new QuestionViewFactory();

    public View createQuestionView(Context context, IQuestion cur){
        switch (cur.questionType()){
            case TEXT_INPUT:
                return createSimpleQuestionView(context, cur);
            default:
                throw new IllegalArgumentException("Do not support view type - " + cur.questionType());
        }
    }

    private View createSimpleQuestionView(Context context, IQuestion cur) {
        LinearLayout questionView = new LinearLayout(context);
        TextView questionTitle = new TextView(context);
        questionTitle.setText(cur.questionText());
        questionView.addView(questionTitle);
        return questionView;
    }

    private QuestionViewFactory(){}

}
