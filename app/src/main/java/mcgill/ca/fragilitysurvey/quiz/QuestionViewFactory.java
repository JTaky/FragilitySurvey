package mcgill.ca.fragilitysurvey.quiz;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import mcgill.ca.fragilitysurvey.R;
import mcgill.ca.fragilitysurvey.quiz.questions.Inputter;
import mcgill.ca.fragilitysurvey.quiz.questions.OptionValue;
import mcgill.ca.fragilitysurvey.quiz.questions.Question;

public class QuestionViewFactory {

    public static final QuestionViewFactory INSTANCE = new QuestionViewFactory();

    public boolean validate(Context context, Question cur, Activity view){
        boolean isValid = true;
        for(Inputter curChose: cur.inputters()){
            if(curChose.isValidateable()) {
                switch (curChose.inputType()) {
                    case CHOOSE:
                        isValid = isValid && validateChooseView(context, view, curChose);
                        break;
                    case INT:
                    case TEXT:
                    case DOUBLE:
                        isValid = isValid && validateEditTextView(context, view, curChose);
                        break;
                    default:
                        throw new IllegalArgumentException("Do not support AnswerType - " + curChose.inputType());
                }
            }
        }
        return isValid;
    }

    private boolean validateEditTextView(Context context, Activity view, Inputter inputter) {
        EditText txt = (EditText)view.findViewById(inputter.inputType().componentId());
        if(StringUtils.isBlank(txt.getText())){
            txt.setError(context.getString(R.string.question_validation_empty_value));
            return false;
        }
        return true;
    }

    private boolean validateChooseView(Context context, Activity view, Inputter inputter) {
        boolean isValid = true;
        if(inputter.isOrLogic()) {
            RadioGroup chooseRadioGroup = (RadioGroup)view.findViewById(inputter.inputType().componentId());
            RadioButton lastRadioButton = null;
            for (final OptionValue option : inputter.options()) {
                lastRadioButton = (RadioButton)chooseRadioGroup.findViewById(option.id());
            }
            if(chooseRadioGroup.getCheckedRadioButtonId() == -1){
                lastRadioButton.setError(context.getString(R.string.question_validation_empty_value));
                isValid = false;
            }
        }
        //suppose, it is ok to have empty checkboxes
        return isValid;
    }

    public View createQuestionView(Context context, Question cur) {
        LinearLayout questionView = new LinearLayout(context);
        questionView.setOrientation(LinearLayout.VERTICAL);
        questionView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        questionView.addView(createQuestionTitle(context, cur.questionText()));

        for(Inputter curChose: cur.inputters()){
            LinearLayout subQuestionLayout = new LinearLayout(context);
            subQuestionLayout.setOrientation(LinearLayout.HORIZONTAL);
            final int padding = 5;
//            subQuestionLayout.setPadding(padding, padding, padding, padding);
            TextView questionTitle = createQuestionTitle(context, curChose.caption());
            subQuestionLayout.addView(questionTitle);

            switch (curChose.inputType()){
                case CHOOSE:
                    createChoseInputView(subQuestionLayout, context, curChose);
                    break;
                case INT:
                    createIntInputView(subQuestionLayout, context, curChose);
                    break;
                case TEXT:
                    createTextInputView(subQuestionLayout, context, curChose);
                    break;
                case DOUBLE:
                    createDoubleInputView(subQuestionLayout, context, curChose);
                    break;
                default:
                    throw new IllegalArgumentException("Do not support AnswerType - " + curChose.inputType());
            }
            questionView.addView(subQuestionLayout);
        }
        return questionView;
    }

    private void createChoseInputView(LinearLayout subQuestionLayout, Context context, final Inputter curChose) {
        subQuestionLayout.setOrientation(LinearLayout.VERTICAL);
        if(curChose.isOrLogic()) {
            RadioGroup radioGroup = new RadioGroup(context);
            radioGroup.setId(curChose.inputType().componentId());
            radioGroup.setOrientation(RadioGroup.HORIZONTAL);
            RadioButton lastR = null;
            for (final OptionValue option : curChose.options()) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(option.caption());
                radioButton.setId(option.id());
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        curChose.answer(curChose.inputType().toAnswer(String.valueOf(v.getId())));
                    }
                });
                radioGroup.addView(radioButton);
                lastR = radioButton;
            }
            //set default value, tmp
            radioGroup.check(0);
            curChose.answer(curChose.inputType().toAnswer(String.valueOf(0)));

            subQuestionLayout.addView(radioGroup);
        } else {
            LinearLayout checkBoxGroup = new LinearLayout(context);
            checkBoxGroup.setId(curChose.inputType().componentId());
            checkBoxGroup.setOrientation(LinearLayout.HORIZONTAL);
            for (final OptionValue option : curChose.options()) {
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(option.caption());
                checkBox.setId(option.id());
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        curChose.addAnswer(curChose.inputType().toAnswer(String.valueOf(v.getId())));
                    }
                });
                checkBoxGroup.addView(checkBox);
            }
            subQuestionLayout.addView(checkBoxGroup);
        }
    }

    private void createIntInputView(LinearLayout subQuestionLayout, Context context, final Inputter curChose) {
        EditText txt = new EditText(context);
        txt.setInputType(InputType.TYPE_CLASS_NUMBER);
        txt.setId(curChose.inputType().componentId());
        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable value) {
                curChose.answer(curChose.inputType().toAnswer(value.toString()));
            }
        });
        subQuestionLayout.addView(txt);
    }

    private void createTextInputView(LinearLayout subQuestionLayout, Context context, final Inputter curChose) {
        EditText txt = new EditText(context);
        txt.setId(curChose.inputType().componentId());
        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable value) {
                curChose.answer(curChose.inputType().toAnswer(value.toString()));
            }
        });
        subQuestionLayout.addView(txt);
    }

    private void createDoubleInputView(LinearLayout subQuestionLayout, Context context, final Inputter curChose) {
        EditText txt = new EditText(context);
        txt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        txt.setId(curChose.inputType().componentId());
        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable value) {
                curChose.answer(curChose.inputType().toAnswer(value.toString()));
            }
        });
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
