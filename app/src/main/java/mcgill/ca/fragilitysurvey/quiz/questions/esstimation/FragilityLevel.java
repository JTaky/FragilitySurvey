package mcgill.ca.fragilitysurvey.quiz.questions.esstimation;

public enum FragilityLevel {
    NO(0, 3),
    MILD(3, 8),
    MODERATE(8, 13),
    SEVERE(13, Integer.MAX_VALUE);

    private int from;
    private int to;

    FragilityLevel(int from, int to){
        this.from = from;
        this.to = to;
    }

    public boolean accept(int score){
        return score >= from && score < to;
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
