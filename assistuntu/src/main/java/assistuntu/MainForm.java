package assistuntu;

import assistuntu.view.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class MainForm extends JFrame implements EngineListener {
    private final Random random = new Random(System.currentTimeMillis());

    private JPanel rootPanel;
    private JButton btnSelectComplect;
    private JTextPane answer1;
    private JTextPane answer2;
    private JTextPane answer3;
    private JTextPane answer4;
    private JLabel questionPicture;
    private JPanel questionPanel;
    private JTextPane questionText;
    private JLabel questionStatus;
    private JPanel questionLine;
    private JScrollPane questionLinePane;
    private JButton btnShowAnswer;
    private JLabel questionTheme;

    private final MainFormController controller;
    private final List<JTextPane> answerButtons = new ArrayList<JTextPane>();

    public MainForm(MainFormController controller) {
        this.controller = controller;
        setContentPane(rootPanel);

        questionPanel.setVisible(false);
        answerButtons.addAll(Arrays.asList(answer1, answer2, answer3, answer4));

        btnSelectComplect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectComplect();
            }
        });
        btnShowAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightAction();
            }
        });

        MouseInputAdapter answerButtonListener = new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                passAnswer((JTextPane) e.getSource());
            }
        };
        for (JTextPane answerButton : answerButtons) {
            answerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            answerButton.addMouseListener(answerButtonListener);
        }
    }

    private void selectComplect() {
        UserComplect complect = controller.getUserComplect().deepCopy();
        if (ComplectSelector.show(this, complect)) {
            controller.setUserComplect(complect);
        }
    }

    @Override
    public void afterQuestionSetSelected() {
        questionLine.removeAll();
        for (Question question : controller.getQuestionList()) {
            questionLine.add(new QuestionLabel(question));
        }
        questionLinePane.getViewport().setViewSize(questionLine.getPreferredSize());
        controller.nextQuestion();
    }

    private void showQuestion(Question question) {
        if (question == null) {
            questionPanel.setVisible(false);
            return;
        }

        questionPanel.setVisible(true);
        if (question.getPicture() != null) {
            questionPicture.setIcon(new ImageIcon(question.getPicture()));
        }

        questionText.setText(question.getText());

        Stack<JTextPane> buttons = new Stack<JTextPane>();
        buttons.addAll(answerButtons);
        for (JTextPane button : buttons) {
            button.setVisible(false);
            button.setText("");
            button.setBackground(Color.WHITE);
        }

        List<String> answers = new ArrayList<String>();
        answers.add(question.getAnswer());
        answers.addAll(Arrays.asList(question.getVariants()));
        while (!answers.isEmpty()) {
            String answer = answers.remove(random.nextInt(answers.size()));
            JTextPane button = buttons.pop();
            button.setText(answer);
            button.setVisible(true);
            button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    }

    private void highlightAction() {
        Question question = controller.currentQuestion();
        for (JTextPane pane : answerButtons) {
            if (pane.getText().equals(question.getAnswer())) {
                pane.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            }
        }
    }

    private void passAnswer(JTextPane source) {
        Question question = controller.currentQuestion();
        QuestionLabel label = getQuestionStatusLabel(question);
        String answer = source.getText();
        boolean correct = answer.equals(question.getAnswer());
        boolean firstTry = Color.WHITE.equals(source.getBackground());

        if (firstTry) {
            controller.setCurrentQuestionAnswer(correct);
            label.setState(correct ? QuestionLabelState.PASSED : QuestionLabelState.FAILED);
        }

        if (correct) {
            controller.nextQuestion();
        } else {
            source.setBackground(Color.RED);

            for (JTextPane button : answerButtons) {
                if (controller.currentQuestion().getAnswer().equals(button.getText())) {
                    button.setBackground(Color.GREEN);
                }
            }
        }
    }

    @Override
    public void completeReport(int failed, int passed) {
        JOptionPane.showMessageDialog(this, String.format("Закончен пакет из %d вопросов. Правильных ответов - %d, ошибок - %d", failed + passed, passed, failed));
    }

    @Override
    public void questionTaken(Question question) {
        showQuestion(question);

        if (question == null) {
            return;
        }

        int questionListSize = controller.getQuestionListSize();
        questionStatus.setText(String.format("%d из %d", question.getIndex() + 1, questionListSize));

        QuestionLabel questionLabel = null;
        for (Component component : questionLine.getComponents()) {
            QuestionLabel label = (QuestionLabel) component;
            if (label.getId() == question.getId()) {
                label.setState(QuestionLabelState.SELECTED);
                questionLabel = label;
            } else {
                label.setState(QuestionLabelState.UNSELECTED);
            }
        }

        if (questionLabel != null) {
            questionLabel.scrollRectToVisible(questionLabel.getVisibleRect());
        }

        Complect complect = controller.getComplect(question.getComplect());
        Theme theme = controller.getTheme(question.getTheme());
        questionTheme.setText(String.format("%s / %s", complect.getName(), theme.getName()));
    }

    private QuestionLabel getQuestionStatusLabel(Question question) {
        for (Component component : questionLine.getComponents()) {
            QuestionLabel label = (QuestionLabel) component;
            if (label.getId() == question.getId()) {
                return label;
            }
        }
        throw new IllegalStateException("label not found");
    }

    public static enum QuestionLabelState {
        UNSELECTED,
        SELECTED,
        FAILED,
        PASSED
    }

    public class QuestionLabel extends JLabel {
        private final int id;
        private QuestionLabelState state = null;
        private Color unselectedBackground;

        public QuestionLabel(Question q) {
            this.id = q.getId();
            setText(String.format("%d:%d:%d", q.getComplect(), q.getBilet(), q.getVopros()));
            setOpaque(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            changeBackground(unselectedBackground = getBackground());
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    MainForm.this.controller.selectQuestion(getId());
                }
            });
        }

        public int getId() {
            return id;
        }

        public void setState(QuestionLabelState newState) {
            switch (newState) {
                case SELECTED:
                    changeBackground(Color.YELLOW);
                    break;
                case UNSELECTED:
                    changeBackground(state == null ? unselectedBackground : state == QuestionLabelState.FAILED ? Color.RED : Color.GREEN);
                    break;
                case FAILED:
                    this.state = newState;
                    changeBackground(Color.RED);
                    break;
                case PASSED:
                    this.state = newState;
                    changeBackground(Color.GREEN);
                    break;
            }
        }

        public void changeBackground(Color color) {
            setBackground(color);
            setBorder(BorderFactory.createLineBorder(color, 3));
        }
    }
}
