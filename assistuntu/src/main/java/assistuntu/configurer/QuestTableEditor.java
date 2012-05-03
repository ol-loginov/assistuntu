package assistuntu.configurer;

import assistuntu.Repository;
import assistuntu.model.QuestRow;
import assistuntu.model.ThemeRow;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestTableEditor extends JFrame {
    private JPanel rootPane;
    private JTable table;
    private JButton btnLoad;
    private JButton btnSave;

    private File repositoryDir;
    private Repository repository;
    private KeyAdapter tableKeyAdapter;

    public QuestTableEditor() {
        setContentPane(rootPane);

        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRepository();
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRepositoryFiles();
            }
        });
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                tableKeyAdapter.keyPressed(e);
            }
        });
    }


    private void loadRepository() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(this)) {
            repositoryDir = chooser.getSelectedFile();
            Repository repo = new Repository();
            try {
                repo.loadFromPath(repositoryDir);
                setRepository(repo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setRepository(Repository repository) {
        this.repository = repository;

        final List<ThemeRow> themeTable = new ArrayList<ThemeRow>(repository.getThemeTable().values());
        final List<QuestRow> data = new ArrayList<QuestRow>(repository.getQuestTable().values());
        final TableModel tableModel = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return data.size();
            }

            @Override
            public int getColumnCount() {
                return themeTable.size() + 1;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                QuestRow quest = data.get(rowIndex);
                if (columnIndex == themeTable.size()) {
                    return quest.getQuestionText();
                }
                return quest.getTheme() == themeTable.get(columnIndex).getId();
            }

            @Override
            public String getColumnName(int column) {
                if (column == themeTable.size()) {
                    return "Вопрос";
                }
                return Integer.toString(themeTable.get(column).getId());
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == themeTable.size()) {
                    return String.class;
                }
                return Boolean.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex < themeTable.size();
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if (Boolean.TRUE.equals(aValue)) {
                    QuestRow quest = data.get(rowIndex);
                    quest.setTheme(themeTable.get(columnIndex).getId());
                    fireTableRowsUpdated(rowIndex, rowIndex);
                }
            }
        };
        table.setModel(tableModel);

        for (ThemeRow row : themeTable) {
            TableColumn column = table.getColumn(Integer.toString(row.getId()));
            column.setResizable(false);
            column.setPreferredWidth(30);
            column.setMaxWidth(30);
            column.setWidth(30);
        }

        tableKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int value;
                try {
                    value = Integer.parseInt(e.getKeyChar() + "");
                } catch (NumberFormatException nfe) {
                    return;
                }
                int row = table.getSelectedRow();
                if (row < 0) {
                    return;
                }
                tableModel.setValueAt(true, row, value - 1);
            }
        };
    }

    private void saveRepositoryFiles() {
        try {
            repository.saveToFolder(repositoryDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        QuestTableEditor editor = new QuestTableEditor();
        editor.setDefaultCloseOperation(EXIT_ON_CLOSE);
        editor.setTitle("Quest Table Editor");
        editor.pack();
        editor.setLocation(100, 100);
        editor.setVisible(true);
    }
}
