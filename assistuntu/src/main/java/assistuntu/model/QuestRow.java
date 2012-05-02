package assistuntu.model;

public class QuestRow {
    private int id;
    private int complect;
    private int bilet;
    private int question;
    private String questionText;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComplect() {
        return complect;
    }

    public void setComplect(int complect) {
        this.complect = complect;
    }

    public int getBilet() {
        return bilet;
    }

    public void setBilet(int bilet) {
        this.bilet = bilet;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
