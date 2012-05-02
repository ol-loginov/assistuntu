package assistuntu;

import assistuntu.view.Complect;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.*;
import java.util.List;

public class ComplectSelector extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTable table;

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

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setValues(final List<Complect> values) {
        table.setModel(new AbstractTableModel() {
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

        TableColumn column = table.getColumn("Включить");
        column.setResizable(false);
        column.setPreferredWidth(30);
        column.setMaxWidth(30);
        column.setWidth(30);

        column = table.getColumn("Название");
        column.setResizable(true);
        column.setPreferredWidth(110);
        column.setWidth(110);
        column.setMaxWidth(110);

        column = table.getColumn("Описание");
        column.setResizable(true);
        column.setPreferredWidth(2000);
        column.setWidth(2000);
        column.setMaxWidth(2000);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    public static void show(JFrame frame, List<Complect> values) {
        ComplectSelector dialog = new ComplectSelector(frame);
        dialog.setValues(values);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setSize(500, 300);
        dialog.setVisible(true);
    }
}
