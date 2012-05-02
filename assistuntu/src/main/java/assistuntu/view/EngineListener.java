package assistuntu.view;

public interface EngineListener {
    void completeReport(int failed, int passed);

    void questionTaken(Question question);

    void afterQuestionSetSelected();
}
