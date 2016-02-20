package mcgill.ca.fragilitysurvey.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

public abstract class TextValidator implements TextWatcher {

    public static void addIsNotEmptyTextValidator(final TextView textView, final String errMsg){
        textView.addTextChangedListener(new TextValidator(textView) {
            @Override
            public void validate(TextView textView, String text) {
                checkIfEmpty(textView, text, errMsg);
            }
        });
    }

    public static void checkIfEmpty(final TextView textView, final String errMsg){
        checkIfEmpty(textView, String.valueOf(textView.getText()), errMsg);
    }

    public static void checkIfEmpty(final TextView textView, final String text, final String errMsg){
        if (ViewUtils.isVisible(textView) &&  StringUtils.isBlank(text)) {
            textView.setError(errMsg);
        }
    }

    public static boolean isValid(TextView textView){
        return StringUtils.isBlank(textView.getError());
    }

    public static boolean isNotValid(TextView textView) {
        return !isValid(textView);
    }

    private final TextView textView;

    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    public abstract void validate(TextView textView, String text);

    @Override
    final public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        validate(textView, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }

}