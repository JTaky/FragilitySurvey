package mcgill.ca.fragilitysurvey.quiz.questions;

import java.util.ArrayList;

public class Questions {

    public static ArrayList<IQuestion> newPatientQuestions = new ArrayList<IQuestion>(){{
        add(new IQuestion.Question()
                .questionText("test 1")
                .questionType(IQuestion.QuestionType.TEXT_INPUT));
        add(new IQuestion.Question()
                .questionText("test 2")
                .questionType(IQuestion.QuestionType.TEXT_INPUT));
    }};

}
