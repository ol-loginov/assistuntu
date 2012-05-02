package assistuntu.view;

import java.awt.image.BufferedImage;

public class Question {
    private int index;
    private boolean loaded;
    private int quest;
    private int complect;

    private String text;
    private String answer;
    private BufferedImage picture;

    private String[] variants = new String[3];
    private int variantsSize = 0;

    private int failCount;
    private int correctCount;

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public int getQuest() {
        return quest;
    }

    public void setQuest(int quest) {
        this.quest = quest;
    }

    public int getComplect() {
        return complect;
    }

    public void setComplect(int complect) {
        this.complect = complect;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void addAnswer(String answer, boolean correct) {
        if (correct) {
            setAnswer(answer);
        } else {
            variants[variantsSize++] = answer;
        }
    }

    public void setPicture(BufferedImage picture) {
        this.picture = picture;
    }

    public BufferedImage getPicture() {
        return picture;
    }

    public String[] getVariants() {
        return variants;
    }

    public int getVariantsSize() {
        return variantsSize;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getPassCount() {
        return failCount + correctCount;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
