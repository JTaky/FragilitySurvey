package mcgill.ca.fragilitysurvey.quiz.questions;

import android.support.annotation.StringRes;

import mcgill.ca.fragilitysurvey.R;

public enum AdditionalTest {
    SYSTEMATIC(R.string.tests_todo_systematic),
    MNA(R.string.tests_todo_mna),
    S_MMSE(R.string.tests_todo_smmse),
    GDS_4_ITEM(R.string.tests_todo_gds),
    SITE_TO_STAND(R.string.tests_todo_ftss);

    public final int STRING_ID;

    AdditionalTest(@StringRes int id){
        this.STRING_ID = id;
    }
}
