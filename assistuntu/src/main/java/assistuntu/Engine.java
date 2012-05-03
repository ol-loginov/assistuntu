package assistuntu;

import assistuntu.model.AnswerRow;
import assistuntu.model.ComplectRow;
import assistuntu.model.QuestRow;
import assistuntu.model.SettingRow;
import assistuntu.view.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Engine implements MainFormController {
    private final Random random = new Random(System.currentTimeMillis());

    private Repository repository = new Repository();
    private UserComplect userComplect = new UserComplect();

    private EngineListener listener;
    private Question question = null;
    private ArrayList<Question> questionList = new ArrayList<Question>();
    private ArrayList<Question> questionLine = new ArrayList<Question>();

    public void load() {
        try {
            repository.loadFromResources();
            repository.loadUserTables();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Integer> activeComplect = new ArrayList<Integer>();
        for (SettingRow row : repository.getUserSettings().values()) {
            if (row.getId().startsWith("active.complect.")) {
                activeComplect.add(Integer.parseInt(row.getValue()));
            }
        }

        userComplect.getComplects().clear();
        for (ComplectRow row : repository.getComplectList()) {
            Complect complect = new Complect();
            complect.setId(row.getId());
            complect.setName(row.getName());
            complect.setDescription(row.getDescription());
            complect.setSelected(activeComplect.contains(row.getId()));
            userComplect.getComplects().add(complect);
        }

        setUserComplect(userComplect);
    }

    public void setListener(EngineListener listener) {
        this.listener = listener;
    }

    @Override
    public UserComplect getUserComplect() {
        return userComplect;
    }

    @Override
    public List<Question> getQuestionList() {
        return Collections.unmodifiableList(questionList);
    }

    @Override
    public void setUserComplect(UserComplect userComplect) {
        this.userComplect = userComplect;

        for (String setting : repository.getUserSettings().keys().toArray(new String[0])) {
            if (setting.startsWith("active.complect.")) {
                repository.getUserSettings().remove(setting);
            }
        }

        List<Integer> complectIdList = new ArrayList<Integer>();
        for (Complect complect : userComplect.getComplects()) {
            if (complect.isSelected()) {
                complectIdList.add(complect.getId());

                SettingRow row = new SettingRow("active.complect." + complect.getId());
                row.setValue(Integer.toString(complect.getId()));
                repository.getUserSettings().put(row);
            }
        }

        List<Integer> themeIdList = new ArrayList<Integer>();
        for (Theme theme : userComplect.getThemes()) {
            if (theme.isSelected()) {
                themeIdList.add(theme.getId());

                SettingRow row = new SettingRow("active.theme." + theme.getId());
                row.setValue(Integer.toString(theme.getId()));
                repository.getUserSettings().put(row);
            }
        }

        try {
            repository.saveUserSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<QuestRow> questInput = repository.selectQuestList(complectIdList, themeIdList);
        Collections.shuffle(questInput);

        questionList.clear();
        for (QuestRow quest : questInput) {
            Question question = new Question(quest);
            question.setIndex(questionList.size());
            questionList.add(question);
        }
        refillQueue();

        listener.afterQuestionSetSelected();
    }

    private void refillQueue() {
        questionLine.clear();
        questionLine.addAll(questionList);
    }

    @Override
    public void nextQuestion() {
        if (questionLine.isEmpty()) {
            listener.completeReport(0, 0);
            refillQueue();
        }

        question = questionLine.remove(0);
        loadQuestion(question);
        listener.questionTaken(question);
    }

    private void loadQuestion(Question question) {
        if (question == null || question.isLoaded()) {
            return;
        }
        QuestRow questRow = repository.getQuest(question.getId());
        question.setText(questRow.getQuestionText());
        for (AnswerRow answer : repository.selectAnswerList(questRow)) {
            question.addAnswer(answer.getAnswer(), answer.isCorrect());
        }
        question.setPicture(repository.getPicture(questRow));
        question.setLoaded(true);
    }

    @Override
    public void setCurrentQuestionAnswer(boolean correct) {
        if (correct) {
            question.setCorrectCount(question.getCorrectCount() + 1);
        } else {
            question.setFailCount(question.getFailCount() + 1);

            if (questionLine.size() < 20) {
                questionLine.add(question);
            } else {
                questionLine.add(10 + random.nextInt(10), question);
            }
        }
    }

    @Override
    public Question currentQuestion() {
        return question;
    }

    @Override
    public int getQuestionListSize() {
        return questionList.size();
    }
}
