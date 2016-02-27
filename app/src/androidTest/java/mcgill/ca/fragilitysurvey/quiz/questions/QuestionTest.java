package mcgill.ca.fragilitysurvey.quiz.questions;

import android.os.Parcel;
import android.os.Parcelable;

import junit.framework.TestCase;

import static java.util.Arrays.asList;

public class QuestionTest extends TestCase {

    private Inputter chose = new Inputter()
            .inputType(IQuestion.InputType.CHOOSE)
            .caption("option 1")
            .options(asList(new OptionValue().caption("+"), new OptionValue().caption("-")))
                    .addAnswer(new IAnswer.ChooseAnswer().value(5));

    private IQuestion.Question chooseQuestion = new IQuestion.Question()
            .questionText("title")
            .inputers(asList(
                    new Inputter()
                            .caption("option 1")
                            .inputType(IQuestion.InputType.CHOOSE)
                            .orLogic(false)
                            .options(asList(new OptionValue().caption("+"),
                                            new OptionValue().caption("-"),
                                            new OptionValue().caption("=")
                                    )).addAnswer(new IAnswer.IntAnswer().value(3)).addAnswer(new IAnswer.StringAnswer().value("-")),
                                    new Inputter()
                                            .caption("option 2")
                                            .inputType(IQuestion.InputType.INT_INPUT)
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

        IQuestion.Question createFromParcel = (IQuestion.Question)IQuestion.Question.CREATOR.createFromParcel(parcel);

        assertEquals("expected the same objects", chooseQuestion, createFromParcel);
    }



}