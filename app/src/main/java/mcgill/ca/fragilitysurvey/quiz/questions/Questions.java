package mcgill.ca.fragilitysurvey.quiz.questions;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import mcgill.ca.fragilitysurvey.R;
import mcgill.ca.fragilitysurvey.repo.entity.answer.AnswerType;

public class Questions {

    public static final int ID_QUESTION_INDEX = 2;
    public static final int FIRST_PATIENT_ID = ID_QUESTION_INDEX + 1;

    public static int MNA_FIRST_ID = 25;
    public static int sMMSE_FIRST_ID = 32;
    public static int GDS_FIRST_ID = 36;
    public static int FTSST_FIRST_ID = 40;

    public static final int PATIENT_QUESTIONS_COUNT = 21 + 2;
    public static final int YES_ID = 0;
    public static final int NO_ID = 1;
    public static final int MALE_ID = 0;
    public static final int FEMALE_ID = 1;

    private static AtomicInteger nextId = new AtomicInteger();

    private static boolean isInitialized = false;
    private static ArrayList<Question> patientQuestions;
    private static ArrayList<Question> systematicSurveyQuestions;
    private static ArrayList<Question> mnaQuestions;
    private static ArrayList<Question> sMmseQuestions;
    private static ArrayList<Question> gdsQuestions;
    private static ArrayList<Question> sitToStandQuestions;

    private static Question newQuestion() {
        return new Question().id(nextId.incrementAndGet());
    }

    public static List<OptionValue> yesNo(final Resources res) {
        return Arrays.asList(
                new OptionValue().caption(res.getString(R.string.question_yes)).id(YES_ID),
                new OptionValue().caption(res.getString(R.string.question_no)).id(NO_ID)
        );
    }

    public static Question binaryQuestion(final Resources res, String questionLabel, String inputterMsg){
        return newQuestion()
                .questionText(questionLabel)
                .inputers(Arrays.asList(
                        new Inputter()
                                .inputType(AnswerType.CHOOSE)
                                .caption(inputterMsg)
                                .options(yesNo(res))
                ));
    }

    public static void reset(){
        patientQuestions = null;
        systematicSurveyQuestions = null;
        mnaQuestions = null;
        sMmseQuestions = null;
        gdsQuestions = null;
        sitToStandQuestions = null;
    }

    public static ArrayList<Question> newPatientQuestions(final Resources res) {
        initQuestions(res);
        return new ArrayList<>(patientQuestions);
    }

    public static ArrayList<Question> systematicSurveyQuestions(final Resources res) {
        initQuestions(res);
        return new ArrayList<>(systematicSurveyQuestions);
    }

    public static ArrayList<Question> mnaQuestions(final Resources res) {
        initQuestions(res);
        return new ArrayList<>(mnaQuestions);
    }

    public static ArrayList<Question> sMmseQuestions(final Resources res) {
        initQuestions(res);
        return new ArrayList<>(sMmseQuestions);
    }

    public static ArrayList<Question> gdsQuestions(final Resources res) {
        initQuestions(res);
        return new ArrayList<>(gdsQuestions);
    }

    public static ArrayList<Question> sitToStandQuestions(final Resources res) {
        initQuestions(res);
        return new ArrayList<>(sitToStandQuestions);
    }

    public static void initQuestions(final Resources res){
        if(isInitialized)
            return;
        initNewPatientQuestions(res);
        initSystematicSurveyQuestions(res);
        initMnaQuestions(res);
        initSMmseQuestions(res);
        initGdsQuestions(res);
        initSitToStandQuestions(res);
        isInitialized = true;
    }

    private static void initNewPatientQuestions(final Resources res) {
        patientQuestions = new ArrayList<Question>() {{
            add(newQuestion()
                    .questionText(res.getString(R.string.question_preface))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_0))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.TEXT)
                                    .caption(res.getString(R.string.question_0_1)),
                            new Inputter()
                                    .inputType(AnswerType.TEXT)
                                    .caption(res.getString(R.string.question_0_2)),
                            new Inputter()
                                    .inputType(AnswerType.INT)
                                    .caption(res.getString(R.string.question_0_3))
                    ))
            );
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
                                    .isValidateable(false)
                                    .options(yesNo(res))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_2))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.INT)
                                    .caption(res.getString(R.string.question_2_1))
                    ))
            );
            add(binaryQuestion(res, res.getString(R.string.question_3), res.getString(R.string.question_3_1)));
            add(binaryQuestion(res, res.getString(R.string.question_4), res.getString(R.string.question_4_1)));
            add(binaryQuestion(res, res.getString(R.string.question_5), res.getString(R.string.question_5_1)));
            add(newQuestion()
                    .questionText(res.getString(R.string.question_6))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_6_1))
                                    .options(yesNo(res)),
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_6_2))
                                    .orLogic(false)
                                    .isValidateable(false)
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_6_2_1)).id(0),
                                            new OptionValue().caption(res.getString(R.string.question_6_2_2)).id(1),
                                            new OptionValue().caption(res.getString(R.string.question_6_2_3)).id(2)
                                    ))
                    ))
            );
            add(binaryQuestion(res, res.getString(R.string.question_7), res.getString(R.string.question_7_1)));
            add(binaryQuestion(res, res.getString(R.string.question_8), res.getString(R.string.question_8_1)));
            add(binaryQuestion(res, res.getString(R.string.question_9), res.getString(R.string.question_9_1)));
            add(binaryQuestion(res, res.getString(R.string.question_10), res.getString(R.string.question_10_1)));
            add(binaryQuestion(res, res.getString(R.string.question_11), res.getString(R.string.question_11_1)));
            add(binaryQuestion(res, res.getString(R.string.question_12), res.getString(R.string.question_12_1)));
            add(binaryQuestion(res, res.getString(R.string.question_13), res.getString(R.string.question_13_1)));
            add(binaryQuestion(res, res.getString(R.string.question_14), res.getString(R.string.question_14_1)));
            add(binaryQuestion(res, res.getString(R.string.question_15), res.getString(R.string.question_15_1)));
            add(binaryQuestion(res, res.getString(R.string.question_16), res.getString(R.string.question_16_1)));
            add(newQuestion()
                    .questionText(res.getString(R.string.question_17))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_17_1))
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_17_1_1)).id(0),
                                            new OptionValue().caption(res.getString(R.string.question_17_1_2)).id(1),
                                            new OptionValue().caption(res.getString(R.string.question_17_1_3)).id(2)
                                    ))
                    ))
            );
            add(binaryQuestion(res, res.getString(R.string.question_18), res.getString(R.string.question_18_1)));
            add(binaryQuestion(res, res.getString(R.string.question_19), res.getString(R.string.question_19_1)));
            add(binaryQuestion(res, res.getString(R.string.question_20), res.getString(R.string.question_20_1)));
            add(newQuestion()
                    .questionText(res.getString(R.string.question_21))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_21_1))
                                    .options(yesNo(res)),
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_21_2))
                                    .orLogic(false)
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_21_2_1)).id(0),
                                            new OptionValue().caption(res.getString(R.string.question_21_2_2)).id(1),
                                            new OptionValue().caption(res.getString(R.string.question_21_2_3)).id(2)
                                    )),
                            new Inputter()
                                    .inputType(AnswerType.TEXT)
                                    .caption(res.getString(R.string.question_21_2_4))
                                    .isValidateable(false)
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_thanks))
            );
        }};
    }

    private static void initSystematicSurveyQuestions(final Resources res) {
        systematicSurveyQuestions = new ArrayList<Question>() {{
            add(newQuestion()
                    .questionText(res.getString(R.string.question_systematic_1))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_systematic_1_1))
                                    .options(yesNo(res))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_systematic_2))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.INT)
                                    .caption(res.getString(R.string.question_systematic_2_1))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_systematic_3))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_systematic_3_1))
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_male)).id(YES_ID),
                                            new OptionValue().caption(res.getString(R.string.question_female)).id(NO_ID)
                                    ))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_systematic_4))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.INT)
                                    .caption(res.getString(R.string.question_systematic_4_1))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_systematic_5))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.INT)
                                    .caption(res.getString(R.string.question_systematic_5_1)),
                            new Inputter()
                                    .inputType(AnswerType.DOUBLE)
                                    .caption(res.getString(R.string.question_systematic_5_2))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_systematic_6))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_systematic_6_2))
                                    .options(yesNo(res)),
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_systematic_6_3))
                                    .options(yesNo(res))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_systematic_7))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_systematic_7_1))
                                    .options(yesNo(res))
                    ))
            );
        }};
    }

    private static void initMnaQuestions(final Resources res) {
        mnaQuestions = new ArrayList<Question>() {{
            add(newQuestion()
                    .questionText(res.getString(R.string.question_mna_1))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_mna_1_1))
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_mna_1_1_1)).id(0),
                                            new OptionValue().caption(res.getString(R.string.question_mna_1_1_2)).id(1),
                                            new OptionValue().caption(res.getString(R.string.question_mna_1_1_3)).id(2)
                                    ))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_mna_2))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_mna_2_1))
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_mna_2_1_1)).id(0),
                                            new OptionValue().caption(res.getString(R.string.question_mna_2_1_2)).id(1),
                                            new OptionValue().caption(res.getString(R.string.question_mna_2_1_3)).id(2),
                                            new OptionValue().caption(res.getString(R.string.question_mna_2_1_4)).id(3)
                                    ))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_mna_3))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_mna_3_1))
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_mna_3_1_1)).id(0),
                                            new OptionValue().caption(res.getString(R.string.question_mna_3_1_2)).id(1),
                                            new OptionValue().caption(res.getString(R.string.question_mna_3_1_3)).id(2)
                                    ))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_mna_4))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_mna_4_1))
                                    .options(yesNo(res))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_mna_5))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_mna_5_1))
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_mna_5_1_1)).id(0),
                                            new OptionValue().caption(res.getString(R.string.question_mna_5_1_2)).id(1),
                                            new OptionValue().caption(res.getString(R.string.question_mna_5_1_3)).id(2)
                                    ))
                    ))
            );
            add(newQuestion()
                    .questionText( res.getString(R.string.question_mna_6, res.getString(R.string.question_mna_title_3)) )
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_mna_6_1))
                                    .isValidateable(false)
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_mna_6_1_1)).id(0),
                                            new OptionValue().caption(res.getString(R.string.question_mna_6_1_2)).id(1),
                                            new OptionValue().caption(res.getString(R.string.question_mna_6_1_3)).id(2),
                                            new OptionValue().caption(res.getString(R.string.question_mna_6_1_4)).id(3)
                                    ))
                    ))
            );
            add(newQuestion()
                    .questionText( res.getString(R.string.question_mna_7, res.getString(R.string.question_mna_title_3)) )
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_mna_7_1))
                                    .isValidateable(false)
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_mna_7_1_1)).id(0),
                                            new OptionValue().caption(res.getString(R.string.question_mna_7_1_2)).id(1)
                                    ))
                    ))
            );
        }};
        MNA_FIRST_ID = mnaQuestions.get(0).id();
    }

    private static void initSMmseQuestions(final Resources res) {
        sMmseQuestions = new ArrayList<Question>() {{
            add(newQuestion()
                    .questionText(res.getString(R.string.question_smmse_1))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_smmse_1_1))
                                    .orLogic(false)
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_smmse_1_1_1)).id(0),
                                            new OptionValue().caption(res.getString(R.string.question_smmse_1_1_2)).id(1),
                                            new OptionValue().caption(res.getString(R.string.question_smmse_1_1_3)).id(2)
                                    ))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_smmse_2))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_smmse_2_1))
                                    .options(yesNo(res))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_smmse_3))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_smmse_3_2))
                                    .options(yesNo(res)),
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_smmse_3_1))
                                    .orLogic(false)
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_smmse_3_1_1)).id(0),
                                            new OptionValue().caption(res.getString(R.string.question_smmse_3_1_2)).id(1),
                                            new OptionValue().caption(res.getString(R.string.question_smmse_3_1_3)).id(2),
                                            new OptionValue().caption(res.getString(R.string.question_smmse_3_1_4)).id(3),
                                            new OptionValue().caption(res.getString(R.string.question_smmse_3_1_5)).id(4)
                                    ))
                    ))
            );
            add(newQuestion()
                    .questionText("")
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_smmse_4))
                                    .orLogic(false)
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_smmse_1_1_1)).id(0),
                                            new OptionValue().caption(res.getString(R.string.question_smmse_1_1_2)).id(1),
                                            new OptionValue().caption(res.getString(R.string.question_smmse_1_1_3)).id(2)
                                    ))
                    ))
            );
        }};
        sMMSE_FIRST_ID = sMmseQuestions.get(0).id();
    }

    private static void initGdsQuestions(final Resources res) {
        gdsQuestions = new ArrayList<Question>() {{
            add(newQuestion()
                    .questionText(res.getString(R.string.question_gds_1))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_gds_1_1))
                                    .options(yesNo(res))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_gds_1))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_gds_2_1))
                                    .options(yesNo(res))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_gds_1))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_gds_3_1))
                                    .options(yesNo(res))
                    ))
            );
            add(newQuestion()
                    .questionText(res.getString(R.string.question_gds_1))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_gds_4_1))
                                    .options(yesNo(res))
                    ))
            );
        }};
        GDS_FIRST_ID = gdsQuestions.get(0).id();
    }

    private static void initSitToStandQuestions(final Resources res) {
        sitToStandQuestions = new ArrayList<Question>() {{
            add(newQuestion()
                    .questionText(res.getString(R.string.question_sit_stand_1))
                    .inputers(Arrays.asList(
                            new Inputter()
                                    .inputType(AnswerType.CHOOSE)
                                    .caption(res.getString(R.string.question_sit_stand_1_1))
                                    .orLogic(false)
                                    .options(Arrays.asList(
                                            new OptionValue().caption(res.getString(R.string.question_yes)).id(YES_ID),
                                            new OptionValue().caption(res.getString(R.string.question_no)).id(NO_ID),
                                            new OptionValue().caption(res.getString(R.string.question_sit_stand_1_1_1_option_3)).id(NO_ID)
                                    )),
                            new Inputter()
                                    .inputType(AnswerType.INT)
                                    .caption(res.getString(R.string.question_sit_stand_1_2))
                    ))
            );
        }};
        FTSST_FIRST_ID = sitToStandQuestions.get(0).id();
    }

    public static ArrayList<Question> getQuestionsForTests(final Resources res, List<AdditionalTest> additionalTests) {
        initQuestions(res);
        ArrayList<Question> additionalTestQuestions = new ArrayList<>();
        additionalTestQuestions.addAll(systematicSurveyQuestions(res));
        if(additionalTests.contains(AdditionalTest.MNA)){
            additionalTestQuestions.addAll(mnaQuestions(res));
        }
        if(additionalTests.contains(AdditionalTest.S_MMSE)){
            additionalTestQuestions.addAll(sMmseQuestions(res));
        }
        if(additionalTests.contains(AdditionalTest.GDS_4_ITEM)){
            additionalTestQuestions.addAll(gdsQuestions(res));
        }
        if(additionalTests.contains(AdditionalTest.SITE_TO_STAND)){
            additionalTestQuestions.addAll(sitToStandQuestions(res));
        }
        return additionalTestQuestions;
    }

    public static Question getQuestionById(int id, final Resources res){
        List<Question> questions = new ArrayList<>();
        questions.addAll(newPatientQuestions(res));
        questions.addAll(systematicSurveyQuestions(res));
        questions.addAll(mnaQuestions(res));
        questions.addAll(sMmseQuestions(res));
        questions.addAll(gdsQuestions(res));
        questions.addAll(sitToStandQuestions(res));
        for(Question question : questions){
            if(question.id() == id){
                return question;
            }
        }
        return null;
    }

}