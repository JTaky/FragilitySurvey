package mcgill.ca.fragilitysurvey.quiz;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import mcgill.ca.fragilitysurvey.quiz.questions.IQuestion;

public class QuestionViewFactory {

    public static final QuestionViewFactory INSTANCE = new QuestionViewFactory();

    public View createQuestionView(Context context, IQuestion cur) {
        LinearLayout questionView = new LinearLayout(context);
        questionView.setOrientation(LinearLayout.VERTICAL);
        questionView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        questionView.addView(createQuestionTitle(context, cur.questionText()));

        for(IQuestion.Inputter curChose: cur.inputters()){
            LinearLayout subQuestionLayout = new LinearLayout(context);
            subQuestionLayout.setOrientation(LinearLayout.HORIZONTAL);
            subQuestionLayout.setPadding(5, 5, 5, 5);
            TextView questionTitle = createQuestionTitle(context, curChose.caption());
            subQuestionLayout.addView(questionTitle);

            switch (curChose.inputType()){
                case CHOOSE:
                    createChoseInputView(subQuestionLayout, context, curChose);
                    break;
                case INT_INPUT:
                    createIntInputView(subQuestionLayout, context, curChose);
                    break;
                case DOUBLE_INPUT:
                    createDoubleInputView(subQuestionLayout, context, curChose);
                    break;
                default:
                    throw new IllegalArgumentException("Do not support InputType - " + curChose.inputType());
            }
            questionView.addView(subQuestionLayout);
        }
        return questionView;
    }

    private void createChoseInputView(LinearLayout subQuestionLayout, Context context, IQuestion.Inputter curChose) {
        if(curChose.isOrLogic()) {
            RadioGroup radioGroup = new RadioGroup(context);
            radioGroup.setOrientation(RadioGroup.HORIZONTAL);
            for (String option : curChose.options()) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(option);
                radioGroup.addView(radioButton);
            }
            subQuestionLayout.addView(radioGroup);
        } else {
            LinearLayout checkBoxGroup = new LinearLayout(context);
            subQuestionLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (String option : curChose.options()) {
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(option);
                checkBoxGroup.addView(checkBox);
            }
            subQuestionLayout.addView(checkBoxGroup);
        }
    }

    private void createIntInputView(LinearLayout subQuestionLayout, Context context, IQuestion.Inputter curChose) {
        EditText txt = new EditText(context);
        txt.setInputType(InputType.TYPE_CLASS_NUMBER);
        subQuestionLayout.addView(txt);
    }

    private void createDoubleInputView(LinearLayout subQuestionLayout, Context context, IQuestion.Inputter curChose) {
        EditText txt = new EditText(context);
        txt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        subQuestionLayout.addView(txt);
    }

    private TextView createQuestionTitle(Context context, String lbl){
        TextView questionTitle = new TextView(context);
        questionTitle.setTextSize(14);
        questionTitle.setText(lbl);
        questionTitle.setPadding(5, 5, 5, 5);
        return questionTitle;
    }

    private QuestionViewFactory(){}

}
