package assistuntu.model;

public class ComplectRow implements Identible<Integer> {
    private int id;
    private String name;
    private String description;
    private int questionCount;
    private int questionTime;
    private int passMark;

    public ComplectRow(String[] csvRow) {
        setDescription(csvRow[0]);
        setId(Integer.parseInt(csvRow[1]));
        setQuestionCount(Integer.parseInt(csvRow[2]));
        setPassMark(Integer.parseInt(csvRow[3]));
        setName(csvRow[4]);
        setQuestionTime(Integer.parseInt(csvRow[5]));
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(int questionTime) {
        this.questionTime = questionTime;
    }

    public int getPassMark() {
        return passMark;
    }

    public void setPassMark(int passMark) {
        this.passMark = passMark;
    }
}
