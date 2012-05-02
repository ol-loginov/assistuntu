package assistuntu.view;

import java.util.List;

public interface MainFormController {
    List<Complect> getComplectList();

    int getQuestionListSize();

    List<Question> getQuestionList();

    void nextQuestion();

    Question currentQuestion();

    void setCurrentQuestionAnswer(boolean correct);

    void updateComplectSet();
}
