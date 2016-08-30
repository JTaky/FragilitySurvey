package mcgill.ca.fragilitysurvey.filter;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import mcgill.ca.fragilitysurvey.R;

public final class SurveySearchFilter implements Parcelable {

    private String id;

    private Date from;
    private Date to;

    protected SurveySearchFilter() {}

    protected SurveySearchFilter(Parcel in) {
        id = in.readString();
        from = new Date(in.readLong());
        to = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(from.getTime());
        dest.writeLong(to.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SurveySearchFilter> CREATOR = new Creator<SurveySearchFilter>() {
        @Override
        public SurveySearchFilter createFromParcel(Parcel in) {
            return new SurveySearchFilter(in);
        }

        @Override
        public SurveySearchFilter[] newArray(int size) {
            return new SurveySearchFilter[size];
        }
    };

    public String id(){
        return id;
    }
    public SurveySearchFilter id(String id){
        this.id = id;
        return this;
    }

    public Date from(){
        return from;
    }
    public SurveySearchFilter from(Date from){
        this.from = from;
        return this;
    }

    public Date to(){
        return to;
    }
    public SurveySearchFilter to(Date to){
        this.to = to;
        return this;
    }

    public String asString(Context context){
        if(StringUtils.isBlank(id())){
            return context.getResources().getString(R.string.pdf_caption_filter_dates, from().toString() + ";" + to.toString());
        } else {
            return context.getResources().getString(R.string.pdf_caption_filter_id, id());
        }
    }

}
