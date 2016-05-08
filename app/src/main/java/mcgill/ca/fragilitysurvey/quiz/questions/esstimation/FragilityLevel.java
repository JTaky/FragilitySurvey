package mcgill.ca.fragilitysurvey.quiz.questions.esstimation;

import mcgill.ca.fragilitysurvey.R;

public enum FragilityLevel {
    NO(0, 3, R.string.fragility_level_no, R.color.color_fragility_no),
    MILD(3, 8, R.string.fragility_level_mild, R.color.color_fragility_mild),
    MODERATE(8, 13, R.string.fragility_level_moderate, R.color.color_fragility_modetate),
    SEVERE(13, Integer.MAX_VALUE, R.string.fragility_level_severe, R.color.color_fragility_severe);

    private int fromInclusive;
    private int toExclusive;
    public final int RESOURCE_ID;
    public final int COLOR_ID;

    FragilityLevel(int fromInclusive, int toExclusive, int resourceId, int colorId){
        this.fromInclusive = fromInclusive;
        this.toExclusive = toExclusive;
        this.RESOURCE_ID = resourceId;
        this.COLOR_ID = colorId;
    }

    public boolean accept(int score){
        return score >= fromInclusive && score < toExclusive;
    }

    public static FragilityLevel estimate(int score){
        for(FragilityLevel fragilityLevel : values()){
            if(fragilityLevel.accept(score)){
                return fragilityLevel;
            }
        }
        return null;
    }
}
