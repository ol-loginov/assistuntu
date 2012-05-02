package assistuntu.view;

import java.util.List;

public interface MainFormController {
    List<Complect> getComplectList();

    int getQuestionLineSize();

    Question nextQuestion();

    Question currentQuestion();

    void setCurrentQuestionAnswer(boolean correct);

    void complectListChanged();
}
