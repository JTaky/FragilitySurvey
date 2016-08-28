package mcgill.ca.fragilitysurvey.filter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import mcgill.ca.fragilitysurvey.MainActivity;
import mcgill.ca.fragilitysurvey.R;

public class SurveyFilterActivity extends AppCompatActivity {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static final int RESULT_OK = MainActivity.FILTER_ACTIVATED;
    public static final int RESULT_CANCEL = MainActivity.IGNORE_ANSWER;

    private static final int DATE_FROM_DIALOG = 0;
    private static final int DATE_TO_DIALOG = 1;

    private View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isFinishing())
                return;
            Intent resultData = new Intent();
            try {
                resultData.putExtra(MainActivity.FILTER_OBJECT, gatherFilterObject());
                setResult(RESULT_OK, resultData);
                finish();
            } catch (ParseException e) {
                Toast.makeText(
                        SurveyFilterActivity.this, "Cannot parse the date", Toast.LENGTH_LONG
                ).show();
                setResult(RESULT_CANCEL);
                finish();
            }
        }
    };

    private SurveySearchFilter gatherFilterObject() throws ParseException {
        SurveySearchFilter surveySearchFilter = new SurveySearchFilter();
        surveySearchFilter.id(
                ((TextView) findViewById(R.id.txtPatientId)).getText().toString()
        );

        surveySearchFilter.from(
                sdf.parse( ((TextView) findViewById(R.id.txtDateFrom)).getText().toString())
        );
        surveySearchFilter.to(
                sdf.parse(  ((TextView) findViewById(R.id.txtDateTo)).getText().toString())
        );
        return surveySearchFilter;
    }

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCEL);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_filter_activity);

        initDefaultValues();
        initBtns();
    }

    private void initDefaultValues() {
        Calendar today = Calendar.getInstance();
        ((TextView) findViewById(R.id.txtDateFrom)).setText(
                sdf.format(today.getTime())
        );
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        ((TextView) findViewById(R.id.txtDateTo)).setText(
                sdf.format(tomorrow.getTime())
        );
    }

    private void initBtns() {
        Button btnOk = (Button) findViewById(R.id.btnClose);
        btnOk.setOnClickListener(okListener);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(cancelListener);
        //date picker
        Button btnDateFrom = (Button) findViewById(R.id.btnDateFrom);
        btnDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_FROM_DIALOG);
            }
        });

        Button bteDateTo = (Button) findViewById(R.id.btnDateTo);
        bteDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_TO_DIALOG);
            }
        });
    }

    /**
     * Opens date pickers dialogs
     * TODO remove deprecation
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DATE_FROM_DIALOG){
            Calendar today = Calendar.getInstance();
            return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar date = Calendar.getInstance();
                    date.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                    ((TextView) findViewById(R.id.txtDateFrom)).setText(
                            sdf.format(date.getTime())
                    );
                }
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        } else /*if(id == DATE_TO_DIALOG)*/{
            Calendar tommorow = Calendar.getInstance();
            tommorow.add(Calendar.DATE, 1);
            return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar date = Calendar.getInstance();
                    date.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                    ((TextView) findViewById(R.id.txtDateTo)).setText(
                            sdf.format(date.getTime())
                    );
                }
            }, tommorow.get(Calendar.YEAR), tommorow.get(Calendar.MONTH), tommorow.get(Calendar.DAY_OF_MONTH));
        }
    }
}
