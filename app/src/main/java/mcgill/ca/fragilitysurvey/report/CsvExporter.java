package mcgill.ca.fragilitysurvey.report;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
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
    public static final DateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss");

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public File exportPatients(File outputDirectory, DBContext dbContext, Context context) throws FileNotFoundException {
        File outputFile = new File(outputDirectory, "exported_" + fileDateFormat.format(new Date()) + ".csv");
        SurveyService surveyService = new SurveyService(dbContext, context.getResources());
        List<Survey> surveys = surveyService.getSurveys();
        String csvContent = serializeToCsv(surveys);
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(outputFile))) {
            writer.print(csvContent);
        }
        return outputFile;
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
                answersStringBuilder.append(inputter.inputType().fromAnswer(answer));
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
