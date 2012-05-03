package assistuntu.view;

import java.util.List;

public interface MainFormController {
    UserComplect getUserComplect();

    void setUserComplect(UserComplect complect);

    int getQuestionListSize();

    List<Question> getQuestionList();

    void nextQuestion();

    Question currentQuestion();

    void setCurrentQuestionAnswer(boolean correct);
}
