package mcgill.ca.fragilitysurvey.report;

import android.annotation.TargetApi;
import android.os.Build;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mcgill.ca.fragilitysurvey.quiz.questions.Inputter;
import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.repo.DBContext;
import mcgill.ca.fragilitysurvey.repo.SurveyService;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;
import mcgill.ca.fragilitysurvey.repo.entity.answer.IAnswer;

public class CsvExporter {

    private static final String SEP = " ";
    public static final DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd_hh:mm:ss");

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void exportPatients(File outputDirectory, DBContext dbContext) throws FileNotFoundException {
        File outputFile = new File(outputDirectory, "exported_" + dateFormat.format(new Date()) + ".csv");
        SurveyService surveyService = new SurveyService(dbContext);
        List<Survey> surveys = surveyService.getSurveys();
        String csvContent = serializeToCsv(surveys);
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(outputFile))) {
            writer.print(csvContent);
        }
    }

    public static String serializeToCsv(List<Survey> surveys) {
        if(surveys.isEmpty())
            return "";
        StringWriter csvStringWriter = new StringWriter();
        CSVWriter writer = new CSVWriter(csvStringWriter);
        writer.writeNext(getHeaders(surveys.get(0)));
        for (Survey survey : surveys) {
            String[] values = getValues(survey);
            writer.writeNext(values, false);
        }
        return csvStringWriter.toString().trim();
    }

    private static String[] getValues(Survey survey) {
        List<String> values = new ArrayList<>();
        for(Question question : survey.questions()) {
            values.add(serializeQuestionValue(question.inputters()));
        }
        return values.toArray(new String[]{});
    }

    private static String serializeQuestionValue(List<Inputter> inputters) {
        StringBuilder valuesStringBuilder = new StringBuilder();
        for (Inputter inputter: inputters){
            StringBuilder answersStringBuilder = new StringBuilder();
            for(IAnswer answer : inputter.answers()) {
                if(valuesStringBuilder.length() != 0) answersStringBuilder.append(SEP);
                answersStringBuilder.append("'");
                answersStringBuilder.append(inputter.inputType().fromAnswer(answer));
                answersStringBuilder.append("'");
            }
            valuesStringBuilder.append(answersStringBuilder);
        }
        return valuesStringBuilder.toString();
    }

    private static String[] getHeaders(Survey survey) {
        List<String> headers = new ArrayList<>();
        for(Question question : survey.questions()){
            headers.add(question.id()+"");
        }
        return headers.toArray(new String[]{});
    }
}
