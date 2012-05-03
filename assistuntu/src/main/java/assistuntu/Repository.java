package assistuntu;

import assistuntu.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class Repository {
    private static interface HtmlTableLoader {
        void accept(String[] textList);
    }

    private static interface HtmlTableSaver<T> {
        String getCsvRow(T item);
    }

    private static interface TableInputStreamProvider {
        InputStream getTableStream(String name) throws IOException;
    }

    private static interface TableOutputStreamProvider {
        OutputStream getTableStream(String name) throws IOException;
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

    public DataTable<Integer, QuestRow> getQuestTable() {
        return questTable;
    }

    public DataTable<Integer, ThemeRow> getThemeTable() {
        return themeTable;
    }

    public void loadFromPath(final File repositoryDir) throws IOException {
        loadFromStreams(new TableInputStreamProvider() {
            @Override
            public InputStream getTableStream(String name) throws IOException {
                return new FileInputStream(new File(repositoryDir, name));
            }
        });
    }

    public void saveToFolder(final File repositoryDir) throws IOException {
        saveToStreams(new TableOutputStreamProvider() {
            @Override
            public OutputStream getTableStream(String name) throws IOException {
                return new FileOutputStream(new File(repositoryDir, name), false);
            }
        });
    }

    public void loadFromResources() throws IOException {
        loadFromStreams(new TableInputStreamProvider() {
            @Override
            public InputStream getTableStream(String name) {
                return openLocalResource(name);
            }
        });
    }

    public void loadUserTables() throws IOException {
        userSettings.clear();
        loadTable(openUserFile("settings.csv"), new HtmlTableLoader() {
            @Override
            public void accept(String[] textList) {
                userSettings.put(new SettingRow(textList));
            }
        });
    }

    private void saveToStreams(TableOutputStreamProvider streamProvider) throws IOException {
        saveTable(streamProvider.getTableStream("db/quest.csv"), questTable.values(), new HtmlTableSaver<QuestRow>() {
            @Override
            public String getCsvRow(QuestRow item) {
                return item.toCsv();
            }
        });
    }

    public void loadFromStreams(TableInputStreamProvider tableProvider) throws IOException {
        answerTable.clear();
        final Map<Integer, List<AnswerRow>> questToAnswerMap = new HashMap<Integer, List<AnswerRow>>();
        loadTable(tableProvider.getTableStream("db/new_table.csv"), new HtmlTableLoader() {
            @Override
            public void accept(String[] textList) {
                AnswerRow row = new AnswerRow(0);
                row.setQuest(Integer.parseInt(textList[1]));
                row.setAnswer(textList[2]);
                row.setCorrect("1".equals(textList[3]));
                //store answer to get it later in parsing "db/answers.csv"
                List<AnswerRow> rows = questToAnswerMap.get(row.getQuest());
                if (rows == null) {
                    rows = new ArrayList<AnswerRow>();
                    questToAnswerMap.put(row.getQuest(), rows);
                }
                rows.add(row);
            }
        });
        loadTable(tableProvider.getTableStream("db/answers.csv"), new HtmlTableLoader() {
            @Override
            public void accept(String[] textList) {
                AnswerRow row = new AnswerRow(textList);

                // if we had loaded the answer from "new_table.csv" - then use it
                List<AnswerRow> rows = questToAnswerMap.get(row.getQuest());
                if (rows != null && !rows.isEmpty()) {
                    AnswerRow newTableAnswer = rows.remove(0);
                    newTableAnswer.setId(row.getId());
                    row = newTableAnswer;
                    if (rows.isEmpty()) {
                        questToAnswerMap.remove(row.getQuest());
                    }
                }

                answerTable.put(row);
            }
        });

        complectTable.clear();
        loadTable(tableProvider.getTableStream("db/complect.csv"), new HtmlTableLoader() {
            @Override
            public void accept(String[] textList) {
                complectTable.put(new ComplectRow(textList));
            }
        });

        themeTable.clear();
        loadTable(tableProvider.getTableStream("db/themes.csv"), new HtmlTableLoader() {
            @Override
            public void accept(String[] textList) {
                themeTable.put(new ThemeRow(textList));
            }
        });

        questTable.clear();
        loadTable(tableProvider.getTableStream("db/quest.csv"), new HtmlTableLoader() {
            @Override
            public void accept(String[] textList) {
                questTable.put(new QuestRow(textList));
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
        try {
            InputStreamReader reader = new InputStreamReader(resource, "utf-8");
            BufferedReader scanner = new BufferedReader(reader);
            String line = scanner.readLine();

            Pattern splitter = Pattern.compile(";");
            while (line != null) {
                loader.accept(splitter.split(line));
                line = scanner.readLine();
            }
        } finally {
            resource.close();
        }
    }

    private <T> void saveTable(OutputStream out, Iterable<T> table, HtmlTableSaver<T> saver) throws IOException {
        if (out == null) {
            return;
        }
        try {
            OutputStreamWriter output = new OutputStreamWriter(out, "utf-8");
            BufferedWriter writer = new BufferedWriter(output);
            for (T item : table) {
                writer.append(saver.getCsvRow(item));
                writer.newLine();
            }
            writer.flush();
        } finally {
            out.close();
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
