package assistuntu;

import assistuntu.view.EngineListener;
import assistuntu.view.MainFormController;
import assistuntu.view.Question;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        MouseInputAdapter answerButtonListener = new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                passAnswer((JTextPane) e.getSource());
            }
        };
        for (JTextPane answerButton : answerButtons) {
            answerButton.addMouseListener(answerButtonListener);
            answerButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    }

    private void selectComplect() {
        ComplectSelector.show(this, controller.getComplectList());
        controller.complectListChanged();

        int questionLineSize = controller.getQuestionLineSize();
        questionLine.removeAll();
        for (int i = 0; i < questionLineSize; ++i) {
            JLabel label = new JLabel();
            label.setText(Integer.toString(i + 1));
            label.setOpaque(true);
            setStatusQuestionLabelColor(label, label.getBackground());
            questionLine.add(label);
        }
        questionLinePane.getViewport().setViewSize(questionLine.getPreferredSize());

        takeNextQuestion();
    }

    public void takeNextQuestion() {
        Question question = controller.nextQuestion();
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
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    private void passAnswer(JTextPane source) {
        String answer = source.getText();
        boolean correct = answer.equals(controller.currentQuestion().getAnswer());
        boolean firstTry = Color.WHITE.equals(source.getBackground());

        if (firstTry) {
            controller.setCurrentQuestionAnswer(correct);
            setStatusQuestionLabelColor(controller.currentQuestion().getIndex(), correct ? Color.GREEN : Color.RED);
        }

        if (correct) {
            takeNextQuestion();
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
        int questionLineSize = controller.getQuestionLineSize();
        questionStatus.setText(String.format("%d из %d", question.getIndex() + 1, questionLineSize));
        JLabel label = (JLabel) questionLine.getComponent(question.getIndex());
        setStatusQuestionLabelColor(label, Color.YELLOW);
        label.scrollRectToVisible(label.getVisibleRect());
    }

    private void setStatusQuestionLabelColor(int index, Color color) {
        setStatusQuestionLabelColor((JLabel) questionLine.getComponent(index), color);
    }

    private void setStatusQuestionLabelColor(JLabel label, Color color) {
        label.setBackground(color);
        label.setBorder(BorderFactory.createLineBorder(color, 3));
    }
}
