package mcgill.ca.fragilitysurvey.credentials;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import mcgill.ca.fragilitysurvey.MainActivity;
import mcgill.ca.fragilitysurvey.R;
import mcgill.ca.fragilitysurvey.preferences.Preferences;
import mcgill.ca.fragilitysurvey.utils.TextValidator;
import mcgill.ca.fragilitysurvey.utils.ViewUtils;

public class CredentialsActivity extends AppCompatActivity {

    private SharedPreferences getPreferences(){
        return Preferences.getPreferences(this);
    }

    private void saveOrganisationPassword(CharSequence organisationName, CharSequence password){
        SharedPreferences sharedPref = getPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.preferences_key_organisation), String.valueOf(organisationName));
        editor.putString(getString(R.string.preferences_key_password), String.valueOf(password));
        editor.apply();
        setResult(MainActivity.SIGN_UP_RESULT_SAVED);
        finish();
    }

    private String getPassword(){
        SharedPreferences sharedPref = getPreferences();
        return sharedPref.getString(getString(R.string.preferences_key_password), "");
    }

    private String getOrganisationName(){
        SharedPreferences sharedPref = getPreferences();
        return sharedPref.getString(getString(R.string.preferences_key_organisation), "");
    }

    private View.OnClickListener btnSaveCredentialsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView txtOrganisationName = (TextView) findViewById(R.id.txtOrganisationName);
            TextView txtCurrentPassword = (TextView) findViewById(R.id.txtCurrentPassword);
            TextView txtNewPassword = (TextView) findViewById(R.id.txtNewPassword);
            TextView txtNewPasswordRepeat = (TextView) findViewById(R.id.txtNewPasswordRepeat);
            //make validation
            if(!String.valueOf(txtNewPassword.getText()).equals(String.valueOf(txtNewPasswordRepeat.getText()))){
                txtNewPassword.setError(getString(R.string.credentials_validation_pwd_does_not_match));
                return;
            }
            if(
                    ViewUtils.isVisible(txtCurrentPassword) &&
                    !getPassword().equals(String.valueOf(txtCurrentPassword.getText()))
              ){
                txtCurrentPassword.setError(getString(R.string.credentials_validation_pwd_current_wrong));
                return;
            }
            TextValidator.checkIfEmpty(txtOrganisationName, getString(R.string.credentials_validation_empty_field));
            TextValidator.checkIfEmpty(txtCurrentPassword, getString(R.string.credentials_validation_empty_field));
            TextValidator.checkIfEmpty(txtNewPassword, getString(R.string.credentials_validation_empty_field));
            TextValidator.checkIfEmpty(txtNewPasswordRepeat, getString(R.string.credentials_validation_empty_field));
            if(TextValidator.isNotValid(txtOrganisationName) ||
                TextValidator.isNotValid(txtCurrentPassword) ||
                TextValidator.isNotValid(txtNewPassword) ||
                TextValidator.isNotValid(txtNewPasswordRepeat)){
                //error should be shown previously
                return;
            }
            //save preferences
            saveOrganisationPassword(txtOrganisationName.getText(), txtNewPassword.getText());
            //close activity
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);

        initBtns();
        fillOrganisation();
        hideIfNeed();
        setUpValidators();
    }

    private void initBtns() {
        Button btnSaveCredentials = (Button) findViewById(R.id.btwSaveCredentials);
        btnSaveCredentials.setOnClickListener(btnSaveCredentialsListener);
    }

    private void fillOrganisation() {
        TextView txtOrganisationName = (TextView) findViewById(R.id.txtOrganisationName);
        txtOrganisationName.setText(getOrganisationName());
    }

    private void hideIfNeed() {
        if(StringUtils.isBlank(getPassword())) {
            TextView lblCurrentPassword = (TextView) findViewById(R.id.lblCurrentPassword);
            lblCurrentPassword.setVisibility(View.INVISIBLE);
            TextView txtCurrentPassword = (TextView) findViewById(R.id.txtCurrentPassword);
            txtCurrentPassword.setVisibility(View.INVISIBLE);
        }
    }

    private void setUpValidators() {
        TextValidator.addIsNotEmptyTextValidator(
                (TextView) findViewById(R.id.txtOrganisationName),
                getString(R.string.credentials_validation_empty_field)
        );
        TextValidator.addIsNotEmptyTextValidator(
                (TextView) findViewById(R.id.txtCurrentPassword),
                getString(R.string.credentials_validation_empty_field)
        );
        TextValidator.addIsNotEmptyTextValidator(
                (TextView) findViewById(R.id.txtNewPassword),
                getString(R.string.credentials_validation_empty_field)
        );
        TextValidator.addIsNotEmptyTextValidator(
                (TextView) findViewById(R.id.txtNewPasswordRepeat),
                getString(R.string.credentials_validation_empty_field)
        );
    }

}
