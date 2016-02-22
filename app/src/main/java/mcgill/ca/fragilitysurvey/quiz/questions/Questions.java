package mcgill.ca.fragilitysurvey.quiz.questions;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mcgill.ca.fragilitysurvey.R;

public class Questions {

    public static List<OptionValue> yesNo(final Resources res) {
        return Arrays.asList(
                new OptionValue().caption(res.getString(R.string.question_no)).id(0),
                new OptionValue().caption(res.getString(R.string.question_yes)).id(1)
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
                                                    new OptionValue().caption(res.getString(R.string.question_2_2_1)).id(0),
                                                    new OptionValue().caption(res.getString(R.string.question_2_2_2)).id(1),
                                                    new OptionValue().caption(res.getString(R.string.question_2_2_3)).id(2)
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
