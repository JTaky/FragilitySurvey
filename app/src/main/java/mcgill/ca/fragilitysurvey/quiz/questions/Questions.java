package mcgill.ca.fragilitysurvey.quiz.questions;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import mcgill.ca.fragilitysurvey.R;
import mcgill.ca.fragilitysurvey.repo.entity.answer.AnswerType;

public class Questions {

    public static final int PATIENT_QUESTIONS_COUNT = 3;

    private static AtomicInteger nextId = new AtomicInteger();

    private static ArrayList<Question> patientQuestions;
    private static ArrayList<Question> completeSurveyQuestions;

    private static Question newQuestion() {
        return new Question().id(nextId.incrementAndGet());
    }

    public static List<OptionValue> yesNo(final Resources res) {
        return Arrays.asList(
                new OptionValue().caption(res.getString(R.string.question_no)).id(0),
                new OptionValue().caption(res.getString(R.string.question_yes)).id(1)
        );
    }

    public static ArrayList<Question> newPatientQuestions(final Resources res) {
        if (patientQuestions != null)
            return patientQuestions;

        patientQuestions = new ArrayList<Question>() {{
            add(newQuestion()
                            .questionText(res.getString(R.string.question_1))
                            .inputers(Arrays.asList(
                                    new Inputter()
                                            .inputType(AnswerType.CHOOSE)
                                            .caption(res.getString(R.string.question_1_1))
                                            .options(yesNo(res)),
                                    new Inputter()
                                            .inputType(AnswerType.CHOOSE)
                                            .caption(res.getString(R.string.question_1_2))
                                            .options(yesNo(res))
                            ))
            );
            add(newQuestion()
                            .questionText(res.getString(R.string.question_2))
                            .inputers(Arrays.asList(
                                    new Inputter()
                                            .inputType(AnswerType.CHOOSE)
                                            .caption(res.getString(R.string.question_2_1))
                                            .options(yesNo(res)),
                                    new Inputter()
                                            .inputType(AnswerType.CHOOSE)
                                            .caption(res.getString(R.string.question_2_2))
                                            .orLogic(false)
                                            .options(Arrays.asList(
                                                    new OptionValue().caption(res.getString(R.string.question_2_2_1)).id(0),
                                                    new OptionValue().caption(res.getString(R.string.question_2_2_2)).id(1),
                                                    new OptionValue().caption(res.getString(R.string.question_2_2_3)).id(2)
                                            ))
                            ))
            );
            add(newQuestion()
                            .questionText(res.getString(R.string.question_3))
                            .inputers(Arrays.asList(
                                    new Inputter()
                                            .inputType(AnswerType.INT)
                                            .caption(res.getString(R.string.question_3_1))
                            ))
            );
        }};
        return patientQuestions;
    }

    public static ArrayList<Question> completeSurveyQuestions(final Resources res) {
        if (completeSurveyQuestions != null)
            return completeSurveyQuestions;

        //init patient questions ids
        newPatientQuestions(res);

        completeSurveyQuestions = new ArrayList<Question>() {{
            add(newQuestion()
                            .questionText(res.getString(R.string.question_complete_1))
                            .inputers(Arrays.asList(
                                    new Inputter()
                                            .inputType(AnswerType.DOUBLE)
                                            .caption(res.getString(R.string.question_complete_1_1))
                            ))
            );
        }};
        return completeSurveyQuestions;
    }

    public static Question getQuestionById(int id, final Resources res){
        List<Question> questions = completeSurveyQuestions(res);
        questions.addAll(newPatientQuestions(res));
        for(Question question : questions){
            if(question.id() == id){
                return question;
            }
        }
        return null;
    }

}