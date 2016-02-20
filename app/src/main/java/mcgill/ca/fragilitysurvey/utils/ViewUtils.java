package mcgill.ca.fragilitysurvey.utils;

import android.view.View;
import android.widget.TextView;

public class ViewUtils {

    private ViewUtils(){}


    public static boolean isVisible(View component) {
        return component.getVisibility() == View.VISIBLE;
    }
}
