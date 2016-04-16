package mcgill.ca.fragilitysurvey.report;

import junit.framework.TestCase;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import mcgill.ca.fragilitysurvey.quiz.questions.Inputter;
import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;
import mcgill.ca.fragilitysurvey.repo.entity.answer.AnswerType;
import mcgill.ca.fragilitysurvey.repo.entity.answer.ChooseAnswer;
import mcgill.ca.fragilitysurvey.repo.entity.answer.DoubleAnswer;
import mcgill.ca.fragilitysurvey.repo.entity.answer.IntAnswer;
import mcgill.ca.fragilitysurvey.repo.entity.answer.StringAnswer;

public class ExporterTest extends TestCase {

    public void testSerializeSingleSurveyToCsv() throws Exception {
        List<Question> questions1 = new ArrayList<>();
        IntAnswer intAnswer1 = new IntAnswer();
        intAnswer1.value(1);
        DoubleAnswer doubleAnswer1 = new DoubleAnswer();
        doubleAnswer1.value(1.5);
        ChooseAnswer chooseAnswer0 = new ChooseAnswer();
        chooseAnswer0.value(1);
        ChooseAnswer chooseAnswer1 = new ChooseAnswer();
        chooseAnswer1.value(2);
        StringAnswer stringAnswer1 = new StringAnswer();
        stringAnswer1.value("String input");

        Inputter inputter1 = new Inputter();
        inputter1.id(0);
        inputter1.caption("Inputter 1 caption");
        inputter1.inputType(AnswerType.INT);
        inputter1.addAnswer(intAnswer1);
        Inputter inputter2 = new Inputter();
        inputter2.id(1);
        inputter2.caption("Inputter 2 caption");
        inputter2.inputType(AnswerType.CHOOSE);
        inputter2.addAnswer(chooseAnswer0);
        inputter2.addAnswer(chooseAnswer1);
        Question q1 = new Question();
        q1.id(0);
        q1.questionText("Question 1 text");
        q1.addInputter(inputter1);
        q1.addInputter(inputter2);

        Inputter inputter3 = new Inputter();
        inputter3.id(3);
        inputter3.caption("Inputter 3 caption");
        inputter3.inputType(AnswerType.TEXT);
        inputter3.addAnswer(stringAnswer1);
        Question q2 = new Question();
        q2.id(1);
        q2.questionText("Question 2");
        q2.addInputter(inputter3);

        questions1.add(q1);
        questions1.add(q2);
        List<Survey> surveys = new ArrayList<>();
        Survey survey1 = new Survey();
        survey1.questions(questions1);
        surveys.add(survey1);

        String csv = CsvExporter.serializeToCsv(surveys);
        Assert.assertEquals("expected correct csv", csv, "\"Question 1 text\",\"Question 2\"\n'1' '1' '2','String input'");
    }

}