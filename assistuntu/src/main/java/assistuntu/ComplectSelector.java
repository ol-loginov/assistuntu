package assistuntu;

import assistuntu.view.Complect;
import assistuntu.view.Theme;
import assistuntu.view.UserComplect;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.*;
import java.util.List;

public class ComplectSelector extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTable complectTable;
    private JTable themeTable;
    private JButton buttonCancel;

    private boolean result;

    public ComplectSelector(JFrame owner) {
        super(owner);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setValues(final UserComplect values) {
        setComplectTable(values.getComplects());
        setThemeTable(values.getThemes());
    }

    private void setThemeTable(final List<Theme> values) {
        themeTable.setModel(new AbstractTableModel() {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 0;
            }

            @Override
            public int getRowCount() {
                return values.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Theme complect = values.get(rowIndex);
                switch (columnIndex) {
                    case 0:
                        return complect.isSelected();
                    case 1:
                        return complect.getName();
                }
                return null;
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                Theme complect = values.get(rowIndex);
                if (columnIndex == 0) {
                    complect.setSelected(Boolean.TRUE.equals(aValue));
                }
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "Включить";
                    case 1:
                        return "Название";
                }
                return "";
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        });

        TableColumn column = complectTable.getColumn("Включить");
        column.setResizable(false);
        column.setPreferredWidth(30);
        column.setMaxWidth(30);
        column.setWidth(30);

        complectTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        complectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setComplectTable(final List<Complect> values) {
        complectTable.setModel(new AbstractTableModel() {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 0;
            }

            @Override
            public int getRowCount() {
                return values.size();
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Complect complect = values.get(rowIndex);
                switch (columnIndex) {
                    case 0:
                        return complect.isSelected();
                    case 1:
                        return complect.getName();
                    case 2:
                        return complect.getDescription();
                }
                return null;
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                Complect complect = values.get(rowIndex);
                if (columnIndex == 0) {
                    complect.setSelected(Boolean.TRUE.equals(aValue));
                }
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "Включить";
                    case 1:
                        return "Название";
                    case 2:
                        return "Описание";
                }
                return "";
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        });

        TableColumn column = complectTable.getColumn("Включить");
        column.setResizable(false);
        column.setPreferredWidth(30);
        column.setMaxWidth(30);
        column.setWidth(30);

        column = complectTable.getColumn("Название");
        column.setResizable(true);
        column.setPreferredWidth(110);
        column.setWidth(110);
        column.setMaxWidth(110);

        column = complectTable.getColumn("Описание");
        column.setResizable(true);
        column.setPreferredWidth(2000);
        column.setWidth(2000);
        column.setMaxWidth(2000);

        complectTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        complectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void onOK() {
        result = true;
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here
        dispose();
    }

    public boolean getResult() {
        return result;
    }

    public static boolean show(JFrame frame, UserComplect values) {
        ComplectSelector dialog = new ComplectSelector(frame);
        dialog.setValues(values);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setSize(500, 300);
        dialog.setVisible(true);
        return dialog.getResult();
    }
}
