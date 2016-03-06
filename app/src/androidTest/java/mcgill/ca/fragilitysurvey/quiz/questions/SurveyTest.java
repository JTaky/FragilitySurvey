package mcgill.ca.fragilitysurvey.quiz.questions;

import android.os.Parcel;
import android.os.Parcelable;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import mcgill.ca.fragilitysurvey.repo.entity.Survey;
import mcgill.ca.fragilitysurvey.repo.entity.answer.AnswerType;
import mcgill.ca.fragilitysurvey.repo.entity.answer.IntAnswer;
import mcgill.ca.fragilitysurvey.repo.entity.answer.StringAnswer;

import static java.util.Arrays.asList;

public class SurveyTest extends TestCase {

    private Question chooseQuestion = new Question()
            .id(42)
            .questionText("title")
            .inputers(new ArrayList<>(asList(
                    new Inputter()
                            .caption("option 1")
                            .inputType(AnswerType.CHOOSE)
                            .orLogic(false)
                            .options(asList(new OptionValue().caption("+"),
                                    new OptionValue().caption("-"),
                                    new OptionValue().caption("=")
                            )).addAnswer(new IntAnswer().value(3)).addAnswer(new StringAnswer().value("-")),
                    new Inputter()
                            .caption("option 2")
                            .inputType(AnswerType.INT)
            )));

    private Survey survey = new Survey()
            .surveyId("some id")
            .timestamp(new Date())
            .questions(Arrays.asList(chooseQuestion));

    public void testReadWrite(){
        Parcel parcel = Parcel.obtain();
        survey.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);

        //done writing, now reset parcel for reading
        parcel.setDataPosition(0);

        Survey createFromParcel = Survey.CREATOR.createFromParcel(parcel);

        assertEquals("expected the same objects", survey, createFromParcel);
    }

}
