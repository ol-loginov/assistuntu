package assistuntu.model;

public class AnswerRow implements Identible<Integer> {
    private int id;
    private int quest;
    private String answer;
    private boolean correct;

    public AnswerRow(int id) {
        this.id = id;
    }

    public AnswerRow(String[] csvRow) {
        id = Integer.parseInt(csvRow[0]);
        quest = Integer.parseInt(csvRow[1]);
        answer = csvRow[2];
        correct = "1".equals(csvRow[3]);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuest() {
        return quest;
    }

    public void setQuest(int quest) {
        this.quest = quest;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
