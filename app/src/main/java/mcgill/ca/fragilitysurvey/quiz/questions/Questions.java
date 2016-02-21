package mcgill.ca.fragilitysurvey.quiz.questions;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mcgill.ca.fragilitysurvey.R;

public class Questions {

    public static List<String> yesNo(final Resources res) {
        return Arrays.asList(
                res.getString(R.string.question_yes),
                res.getString(R.string.question_no)
        );
    }

    public static ArrayList<IQuestion> newPatientQuestions(final Resources res) {
        return new ArrayList<IQuestion>() {{
            add(new IQuestion.Question()
                            .questionText(res.getString(R.string.question_1))
                            .inputers(Arrays.asList(
                                    new Inputter()
                                            .inputType(IQuestion.InputType.CHOOSE)
                                            .caption(res.getString(R.string.question_1_1))
                                            .options(yesNo(res)),
                                    new Inputter()
                                            .inputType(IQuestion.InputType.CHOOSE)
                                            .caption(res.getString(R.string.question_1_2))
                                            .options(yesNo(res))
                            ))
            );
            add(new IQuestion.Question()
                            .questionText(res.getString(R.string.question_1))
                            .inputers(Arrays.asList(
                                    new Inputter()
                                            .inputType(IQuestion.InputType.CHOOSE)
                                            .caption(res.getString(R.string.question_2_1))
                                            .options(yesNo(res)),
                                    new Inputter()
                                            .inputType(IQuestion.InputType.CHOOSE)
                                            .caption(res.getString(R.string.question_2_2))
                                            .orLogic(false)
                                            .options(Arrays.asList(
                                                    res.getString(R.string.question_2_2_1),
                                                    res.getString(R.string.question_2_2_2),
                                                    res.getString(R.string.question_2_2_3)
                                            ))
                            ))
            );
            add(new IQuestion.Question()
                            .questionText(res.getString(R.string.question_3))
                            .inputers(Arrays.asList(
                                    new Inputter()
                                            .inputType(IQuestion.InputType.INT_INPUT)
                                            .caption(res.getString(R.string.question_3_1))
                            ))
            );
        }};
    }

}
