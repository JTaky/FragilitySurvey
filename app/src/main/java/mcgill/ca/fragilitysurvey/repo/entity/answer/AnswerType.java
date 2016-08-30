package mcgill.ca.fragilitysurvey.repo.entity.answer;

import org.apache.commons.lang3.math.NumberUtils;

import mcgill.ca.fragilitysurvey.quiz.questions.Inputter;
import mcgill.ca.fragilitysurvey.quiz.questions.OptionValue;

public enum AnswerType {
    TEXT(0) {
        @Override
        public IAnswer toAnswer(String stringValue) {
            return new StringAnswer().value(stringValue);
        }

        @Override
        public String fromAnswer(IAnswer answer) {
            return ((StringAnswer)answer).value();
        }

    },
    CHOOSE(1) {
        @Override
        public IAnswer toAnswer(String idStr) {
            return new ChooseAnswer().value(Integer.parseInt(idStr));
        }

        @Override
        public String fromAnswer(IAnswer answer) {
            return ((ChooseAnswer)answer).value().toString();
        }

        @Override
        public String formatAnswer(IAnswer answer, Inputter inputter) {
            return getChooseName(inputter, ((ChooseAnswer)answer).value());
        }

        private String getChooseName(Inputter inputter, int id) {
            for(OptionValue o: inputter.options()){
                if(o.id() == id){
                    return o.caption();
                }
            }
            return "";
        }
    },
    INT(2) {
        @Override
        public IAnswer toAnswer(String stringValue) {
            return new IntAnswer().value(NumberUtils.toInt(stringValue, 0));
        }

        @Override
        public String fromAnswer(IAnswer answer) {
            return ((IntAnswer)answer).value().toString();
        }

    },
    DOUBLE(3) {
        @Override
        public IAnswer toAnswer(String stringValue) {
            return new DoubleAnswer().value(NumberUtils.toDouble(stringValue, 0.0));
        }

        @Override
        public String fromAnswer(IAnswer answer) {
            return ((DoubleAnswer)answer).value().toString();
        }
    },
    DATE(4) {
        @Override
        public IAnswer toAnswer(String stringValue) {
            return new DoubleAnswer().value(Double.parseDouble(stringValue));
        }

        @Override
        public String fromAnswer(IAnswer answer) {
            return ((DoubleAnswer)answer).value().toString();
        }

    };

    public final int id;

    AnswerType(int id){
        this.id = id;
    }

    public static AnswerType fromId(int v) {
        for(AnswerType answerType : values()){
            if(answerType.id == v){
                return answerType;
            }
        }
        return null;
    }

    public abstract IAnswer toAnswer(String stringValue);

    public int componentId() {
        return (id+1)*100 + 1;
    }

    public abstract String fromAnswer(IAnswer answer);

    public String formatAnswer(IAnswer answer, Inputter inputter) {
        return fromAnswer(answer);
    }
}
