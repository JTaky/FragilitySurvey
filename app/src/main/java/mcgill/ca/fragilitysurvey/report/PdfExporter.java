package mcgill.ca.fragilitysurvey.report;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import mcgill.ca.fragilitysurvey.repo.DBContext;
import mcgill.ca.fragilitysurvey.repo.SurveyService;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;

public class PdfExporter {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void exportPatients(File outputDirectory, DBContext dbContext, Context context) throws FileNotFoundException {
        SurveyService surveyService = new SurveyService(dbContext);
        List<Survey> surveys = surveyService.getSurveys();
        File outputFile = new File(outputDirectory, "exported_" + CsvExporter.dateFormat.format(new Date()) + ".pdf");

    }

    private static void addMetaData(Document document) {
        document.addTitle("Fragility survey report");
        document.addSubject("Surveys data");
        document.addKeywords("Old people, Fragility, Survey");
        document.addAuthor("FragilitySurvey");
        document.addCreator("FragilitySurvey");
    }

    private static void addHeader(Document document, Context context){
        //add empty box
        //add date
        //add caption
    }

    private static void addSurveys(Document document, Context context, List<Survey> surveys){

    }

    private static void addQuestion(Document document, Context context, List<Survey> surveys){

    }

}
