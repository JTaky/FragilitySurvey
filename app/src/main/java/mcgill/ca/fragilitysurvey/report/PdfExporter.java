package mcgill.ca.fragilitysurvey.report;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mcgill.ca.fragilitysurvey.R;
import mcgill.ca.fragilitysurvey.quiz.questions.Inputter;
import mcgill.ca.fragilitysurvey.quiz.questions.Question;
import mcgill.ca.fragilitysurvey.repo.DBContext;
import mcgill.ca.fragilitysurvey.repo.SurveyService;
import mcgill.ca.fragilitysurvey.repo.entity.Survey;
import mcgill.ca.fragilitysurvey.repo.entity.answer.IAnswer;

public class PdfExporter {

    public static final DateFormat surveyDateFormat = new SimpleDateFormat("yyyyy-MM-dd hh");

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font greyFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.GRAY);
    private static Font okFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.BLACK);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String exportPatients(File outputDirectory, DBContext dbContext, Context context) throws FileNotFoundException, DocumentException {
        SurveyService surveyService = new SurveyService(dbContext, context.getResources());
        File outputFile = new File(outputDirectory, "exported_" + CsvExporter.fileDateFormat.format(new Date()) + ".pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        document.open();
        addMetaData(document);
        addHeader(document, context);
        addSurveys(document, context, surveyService.getSurveys());
        document.close();
        return outputFile.getName();
    }

    private static void addMetaData(Document document) {
        document.addTitle("Fragility survey report");
        document.addSubject("Surveys data");
        document.addKeywords("Old people, Fragility, Survey");
        document.addAuthor("FragilitySurvey");
        document.addCreator("FragilitySurvey");
    }

    private static void addHeader(Document document, Context context) throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        //title
        preface.add(new Paragraph(context.getString(R.string.pdf_title), catFont));
        addEmptyLine(preface, 2);
        //add empty box
        preface.add(new Paragraph("___________________", okFont));
        //add date
        Paragraph date = new Paragraph();
        date.setAlignment(Element.ALIGN_RIGHT);
        date.setIndentationRight(10);
        date.add(new Phrase(context.getString(R.string.pdf_date), okFont));
        date.add(new Phrase(" ...... ", greyFont));
        date.add(new Phrase("/", okFont));
        date.add(new Phrase(" ...... ", greyFont));
        date.add(new Phrase("/", okFont));
        date.add(new Phrase(" ............ ", greyFont));
        preface.add(date);
        addEmptyLine(preface, 1);
        //add caption
        preface.add(new Paragraph(context.getString(R.string.pdf_caption_1), okFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(context.getString(R.string.pdf_caption_2), okFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(context.getString(R.string.pdf_caption_3), okFont));
        addEmptyLine(preface, 2);

        document.add(preface);
    }

    private static void addSurveys(Document document, Context context, List<Survey> surveys) throws DocumentException {
        for(Survey survey : surveys){
            Paragraph surveyParagraph = new Paragraph("Survey with id '" + survey.surveyId() + "', for date: " + surveyDateFormat.format(survey.timestamp()));
            addEmptyLine(surveyParagraph, 1);
            document.add(surveyParagraph);
            for(Question question : survey.questions()){
                addQuestion(document, context, question);
            }
        }
    }

    private static void addQuestion(Document document, Context context, Question question) throws DocumentException {
        Paragraph questionParagraph = new Paragraph();
        questionParagraph.add(new Phrase(question.questionText(), okFont));
        addEmptyLine(questionParagraph, 1);
        for(Inputter inputter : question.inputters()){
            Paragraph inputterParagraph = new Paragraph();
            inputterParagraph.add(new Phrase(inputter.caption() + " ", okFont));
            for(IAnswer answer: inputter.answers()){
                inputterParagraph.add(new Phrase(inputter.inputType().fromAnswer(answer), okFont));
                inputterParagraph.add(new Phrase(" ", okFont));
            }
            questionParagraph.add(inputterParagraph);
        }
        document.add(questionParagraph);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
