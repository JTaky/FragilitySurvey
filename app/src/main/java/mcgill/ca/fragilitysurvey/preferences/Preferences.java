package mcgill.ca.fragilitysurvey.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String PREFERENCES_KEY = "PreferencesClassKey";

    public static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

}
