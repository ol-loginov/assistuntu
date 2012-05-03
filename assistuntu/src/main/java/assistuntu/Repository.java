package assistuntu;

import assistuntu.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class Repository {
    private static interface HtmlTableLoader {
        void accept(String[] textList);
    }

    private static class IntegerHolder {
        public int val = 0;
    }

    private final DataTable<Integer, AnswerRow> answerTable = new DataTable<Integer, AnswerRow>();
    private final DataTable<Integer, ComplectRow> complectTable = new DataTable<Integer, ComplectRow>();
    private final DataTable<Integer, QuestRow> questTable = new DataTable<Integer, QuestRow>();
    private final DataTable<Integer, ThemeRow> themeTable = new DataTable<Integer, ThemeRow>();
    private final DataTable<String, SettingRow> userSettings = new DataTable<String, SettingRow>();

    public Repository() {
    }

    public Collection<ComplectRow> getComplectList() {
        return complectTable.values();
    }

    public QuestRow getQuest(int quest) {
        return questTable.get(quest);
    }

    public void loadFromResources() throws IOException {
        answerTable.clear();
        final IntegerHolder nextAnswerId = new IntegerHolder();
        loadTable(openLocalResource("db/answers.csv"), new HtmlTableLoader() {
            @Override
            public void accept(String[] textList) {
                AnswerRow row = new AnswerRow(textList);
                answerTable.put(row);
                nextAnswerId.val = Math.max(nextAnswerId.val, row.getId());
            }
        });
        loadTable(openLocalResource("db/new_table.csv"), new HtmlTableLoader() {
            @Override
            public void accept(String[] textList) {
                AnswerRow row = new AnswerRow(++nextAnswerId.val);
                row.setQuest(Integer.parseInt(textList[1]));
                row.setAnswer(textList[2]);
                row.setCorrect("1".equals(textList[3]));
                answerTable.put(row);
            }
        });

        complectTable.clear();
        loadTable(openLocalResource("db/complect.csv"), new HtmlTableLoader() {
            @Override
            public void accept(String[] textList) {
                complectTable.put(new ComplectRow(textList));
            }
        });

        themeTable.clear();
        loadTable(openLocalResource("db/themes.csv"), new HtmlTableLoader() {
            @Override
            public void accept(String[] textList) {
                themeTable.put(new ThemeRow(textList));
            }
        });

        questTable.clear();
        loadTable(openLocalResource("db/quest.csv"), new HtmlTableLoader() {
            @Override
            public void accept(String[] textList) {
                questTable.put(new QuestRow(textList));
            }
        });

        userSettings.clear();
        loadTable(openUserFile("settings.csv"), new HtmlTableLoader() {
            @Override
            public void accept(String[] textList) {
                userSettings.put(new SettingRow(textList));
            }
        });
    }

    private InputStream openLocalResource(String name) {
        return Repository.class.getClassLoader().getResourceAsStream(name);
    }

    private File getUserFile(String name) {
        File userHome = new File(System.getProperty("user.home"));
        File repositoryHome = new File(userHome, ".assistuntu");
        repositoryHome.mkdirs();
        return new File(repositoryHome, name);
    }

    private InputStream openUserFile(String name) {
        try {
            return new FileInputStream(getUserFile(name));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private void loadTable(InputStream resource, HtmlTableLoader loader) throws IOException {
        if (resource == null) {
            return;
        }
        InputStreamReader reader = new InputStreamReader(resource, "utf-8");
        BufferedReader scanner = new BufferedReader(reader);
        String line = scanner.readLine();

        Pattern splitter = Pattern.compile(";");
        while (line != null) {
            loader.accept(splitter.split(line));
            line = scanner.readLine();
        }
    }

    public List<QuestRow> selectQuestList(List<Integer> complectIds, List<Integer> themeIdList) {
        List<QuestRow> questList = new ArrayList<QuestRow>();
        for (QuestRow quest : questTable.values()) {
            if (!complectIds.contains(quest.getComplect())) {
                continue;
            }
            if (!themeIdList.isEmpty() && !themeIdList.contains(quest.getTheme())) {
                continue;
            }
            questList.add(quest);
        }
        return questList;
    }

    public List<AnswerRow> selectAnswerList(QuestRow questRow) {
        List<AnswerRow> result = new ArrayList<AnswerRow>();
        for (AnswerRow row : answerTable.values()) {
            if (row.getQuest() == questRow.getId()) {
                result.add(row);
            }
        }
        return result;
    }

    public BufferedImage getPicture(QuestRow quest) {
        String picture = String.format("%d b%dv%d.jpg", quest.getComplect(), quest.getBilet(), quest.getQuestion());
        InputStream pictureStream = openLocalResource("db/pictures/" + picture);
        if (pictureStream == null) {
            return null;
        }
        try {
            return ImageIO.read(pictureStream);
        } catch (IOException e) {
            return null;
        }
    }

    public DataTable<String, SettingRow> getUserSettings() {
        return userSettings;
    }

    public void saveUserSettings() throws IOException {
        File file = getUserFile("settings.csv");
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("cannot create settings file");
            }
        }

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
        try {
            BufferedWriter out = new BufferedWriter(writer);
            for (SettingRow row : this.userSettings.values()) {
                out.write(row.toCSV());
                out.newLine();
            }
            out.flush();
        } finally {
            writer.close();
        }
    }
}
