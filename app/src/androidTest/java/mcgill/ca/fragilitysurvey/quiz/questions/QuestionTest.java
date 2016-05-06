package mcgill.ca.fragilitysurvey.quiz.questions;

import android.os.Parcel;
import android.os.Parcelable;

import junit.framework.TestCase;

import mcgill.ca.fragilitysurvey.repo.entity.answer.AnswerType;
import mcgill.ca.fragilitysurvey.repo.entity.answer.ChooseAnswer;
import mcgill.ca.fragilitysurvey.repo.entity.answer.IntAnswer;
import mcgill.ca.fragilitysurvey.repo.entity.answer.StringAnswer;

import static java.util.Arrays.asList;

public class QuestionTest extends TestCase {

    private Inputter chose = new Inputter()
            .inputType(AnswerType.CHOOSE)
            .caption("option 1")
            .options(asList(new OptionValue().caption("+"), new OptionValue().caption("-")))
                    .addAnswer(new ChooseAnswer().value(5));

    private Question chooseQuestion = new Question()
            .id(42)
            .questionText("title")
            .inputers(asList(
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
                                            .isValidateable(false)
                                            .inputType(AnswerType.INT)
                            ));

    public void testChoseReadWrite(){
        Parcel parcel = Parcel.obtain();
        chose.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);

        //done writing, now reset parcel for reading
        parcel.setDataPosition(0);

        Inputter createFromParcel = (Inputter) Inputter.CREATOR.createFromParcel(parcel);

        assertEquals("expected the same objects", chose, createFromParcel);
    }

    public void testChoseQuestionReadWrite(){
        Parcel parcel = Parcel.obtain();
        chooseQuestion.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);

        //done writing, now reset parcel for reading
        parcel.setDataPosition(0);

        Question createFromParcel = (Question) Question.CREATOR.createFromParcel(parcel);

        assertEquals("expected id is selialized", 42, createFromParcel.id());
        assertEquals("expected the same objects", chooseQuestion, createFromParcel);
    }



}