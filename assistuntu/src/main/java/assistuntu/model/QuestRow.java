package assistuntu.model;

public class QuestRow implements Identible<Integer> {
    private int id;
    private int theme;
    private int complect;
    private int bilet;
    private int question;
    private String questionText;

    public QuestRow(String[] csvRow) {
        complect = Integer.parseInt(csvRow[0]);
        theme = Integer.parseInt(csvRow[1]);
        id = Integer.parseInt(csvRow[2]);
        bilet = Integer.parseInt(csvRow[3]);
        question = Integer.parseInt(csvRow[4]);
        questionText = csvRow[5];
    }

    public String toCsv() {
        return String.format("%d;%d;%d;%d;%d;%s", complect, theme, id, bilet, question, questionText);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getComplect() {
        return complect;
    }

    public int getBilet() {
        return bilet;
    }

    public int getQuestion() {
        return question;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }
}
