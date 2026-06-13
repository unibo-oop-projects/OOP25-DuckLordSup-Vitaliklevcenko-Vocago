package it.unibo.vocago.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.model.types.MasteryLevel;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.view.panels.api.PanelLayout;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;

public class VocabularyEditorPanel extends JPanel implements PanelLayout {

    private final DefaultTableModel model;
    private final JTable table;
    private final JButton addRowButton;
    private final JButton goBackButton;
    private final JButton deleteRowButton;
    private final JButton saveChangesButton;
    private final JTextField searchTextField;
    private final JButton findButton;
    private final Controller controller;

    public VocabularyEditorPanel(final Controller controller) {
        this.controller = controller;
        this.model = new DefaultTableModel();
        this.table = UIFactory.createTable(this.model);
        this.addRowButton = UIFactory.createButton("ADD A ROW", "", 0, UIConstants.BUTTON_BACKGROUND, 0, 0, true,
                true, true, UIConstants.FONT);
        this.deleteRowButton = UIFactory.createButton("DELETE SELECTED ROWS", "", 0, UIConstants.BUTTON_BACKGROUND,
                0, 0, true, true, true, UIConstants.FONT);
        this.saveChangesButton = UIFactory.createButton("SAVE CHANGES", "", 0, UIConstants.BUTTON_BACKGROUND, 0, 0,
                true, true, true, UIConstants.FONT);
        this.goBackButton = UIFactory.createButton("", "data/resources/pictures/back.png", 60, UIConstants.BACKGROUND,
                60, 70, false, true, false, UIConstants.FONT);
        this.searchTextField = UIFactory.createTextField();
        this.findButton = UIFactory.createButton("", "data/resources/pictures/search.png", 25, UIConstants.BACKGROUND,
                40, 40, false, true, false, UIConstants.FONT);

        buildLayout();
        UIFactory.stylePanel(this);
        actionRegister();
    }

    @Override
    public void buildLayout() {
        setLayout(new BorderLayout());
        add(buildUpperPanel(), BorderLayout.NORTH);
        add(buildTableScrollPane(), BorderLayout.CENTER);
        add(buildLowerPanel(), BorderLayout.SOUTH);
        showVocabulary();
    }

    private JPanel buildUpperPanel() {
        final JPanel upperButtonsPanel = UIFactory.createPanel(new GridLayout());
        upperButtonsPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, UIConstants.HEADER_HEIGHT));

        final JPanel leftPanel = UIFactory.createPanel(new BorderLayout());
        leftPanel.add(this.goBackButton, BorderLayout.WEST);
        upperButtonsPanel.add(leftPanel);

        upperButtonsPanel.add(UIFactory.createLabel(
                "My " + this.controller.getCurrentUser().getSecondLanguage() + " Vocabulary",
                UIConstants.TITLE_FONT));

        final JPanel rightPanel = UIFactory.createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
        rightPanel.add(this.searchTextField);
        rightPanel.add(this.findButton);
        upperButtonsPanel.add(rightPanel);

        return upperButtonsPanel;
    }

    private JScrollPane buildTableScrollPane() {

        model.addColumn(this.controller.getCurrentUser().getFirstLanguage());
        model.addColumn(this.controller.getCurrentUser().getSecondLanguage());
        model.addColumn("Memorization level");
        model.addColumn("Recognition level");
        model.addColumn("firstProgress");
        model.addColumn("secondProgress");

        final DefaultCellEditor wordEditor = new DefaultCellEditor(UIFactory.createTextField());
        this.table.getColumnModel().getColumn(VocabularyEditorHelper.FIRST_WORDS_COLUMN).setCellEditor(wordEditor);
        this.table.getColumnModel().getColumn(VocabularyEditorHelper.SECOND_WORDS_COLUMN).setCellEditor(wordEditor);

        final DefaultCellEditor masteryEditor = new DefaultCellEditor(UIFactory.createComboBox(MasteryLevel.values()));
        this.table.getColumnModel().getColumn(VocabularyEditorHelper.MEMORIZATION_COLUMN).setCellEditor(masteryEditor);
        this.table.getColumnModel().getColumn(VocabularyEditorHelper.RECOGNITION_COLUMN).setCellEditor(masteryEditor);

        this.table.getColumnModel().getColumn(VocabularyEditorHelper.FIRST_WORDS_COLUMN)
                .setPreferredWidth(UIConstants.WORD_COLUMN_WIDTH);
        this.table.getColumnModel().getColumn(VocabularyEditorHelper.SECOND_WORDS_COLUMN)
                .setPreferredWidth(UIConstants.WORD_COLUMN_WIDTH);
        this.table.getColumnModel().getColumn(VocabularyEditorHelper.MEMORIZATION_COLUMN)
                .setPreferredWidth(UIConstants.MASTERY_COLUMN_WIDTH);
        this.table.getColumnModel().getColumn(VocabularyEditorHelper.RECOGNITION_COLUMN)
                .setPreferredWidth(UIConstants.MASTERY_COLUMN_WIDTH);

        hideColumn(VocabularyEditorHelper.FIRST_PROGRESS_COLUMN);
        hideColumn(VocabularyEditorHelper.SECOND_PROGRESS_COLUMN);

        return UIFactory.createScrollPane(this.table);
    }

    private JPanel buildLowerPanel() {
        final JPanel lowerButtonsPanel = UIFactory.createPanel(new GridLayout());
        this.addRowButton.setBorderPainted(true);
        this.deleteRowButton.setBorderPainted(true);
        this.saveChangesButton.setBorderPainted(true);
        lowerButtonsPanel.add(this.addRowButton);
        lowerButtonsPanel.add(this.deleteRowButton);
        lowerButtonsPanel.add(this.saveChangesButton);
        lowerButtonsPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, UIConstants.FOOTER_HEIGHT));
        return lowerButtonsPanel;
    }

    private void hideColumn(final int columnIndex) {
        this.table.getColumnModel().getColumn(columnIndex).setMinWidth(UIConstants.ZERO);
        this.table.getColumnModel().getColumn(columnIndex).setMaxWidth(UIConstants.ZERO);
        this.table.getColumnModel().getColumn(columnIndex).setPreferredWidth(UIConstants.ZERO);
        this.table.getColumnModel().getColumn(columnIndex).setWidth(UIConstants.ZERO);
    }

    private void actionRegister() {
        this.addRowButton.addActionListener(e -> addEmptyRow());
        this.deleteRowButton.addActionListener(e -> deleteSelectedRows());
        this.goBackButton.addActionListener(e -> askBeforeLeaving());
        this.saveChangesButton.addActionListener(e -> {
            if (saveChanges()) {
                this.controller.showUserDashboardPanel();
            }
        });

        this.searchTextField.addActionListener(e -> findWordInTable());
        this.findButton.addActionListener(e -> findWordInTable());
        addRowOnKeyPress();
    }

    private boolean saveChanges() {
        if (!stopTableEditing()) {
            return false;
        }
        this.controller.saveVocabulary(VocabularyEditorHelper.toVocabulary(this.model));
        return true;
    }

    private void showVocabulary() {
        for (final VocabularyItem item : this.controller.getCurrentUser().getVocabulary().getItems()) {
            this.model.addRow(VocabularyEditorHelper.rowFromVocabularyItem(item));
        }
        addEmptyRow();
    }

    private void addEmptyRow() {
        this.model.addRow(VocabularyEditorHelper.emptyRow());
    }

    private void deleteSelectedRows() {
        if (!stopTableEditing()) {
            return;
        }

        final int[] selectedRows = this.table.getSelectedRows();
        if (selectedRows.length == 0) {
            return;
        }

        for (int i = selectedRows.length - 1; i >= 0; i--) {
            final int modelRow = this.table.convertRowIndexToModel(selectedRows[i]);
            this.model.removeRow(modelRow);
        }

        if (this.model.getRowCount() == 0) {
            addEmptyRow();
        }
    }

    private boolean stopTableEditing() {
        return !this.table.isEditing() || this.table.getCellEditor().stopCellEditing();
    }

    private void addRowOnKeyPress() {
        this.table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER && stopTableEditing()) {
                    addEmptyRow();
                }
            }
        });
    }

    private void findWordInTable() {
        final String searchText = this.searchTextField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            return;
        }

        for (int row = 0; row < this.table.getRowCount(); row++) {
            final String firstColumn = VocabularyEditorHelper.cellToText(
                    this.table.getValueAt(row, VocabularyEditorHelper.FIRST_WORDS_COLUMN)).toLowerCase();
            final String secondColumn = VocabularyEditorHelper.cellToText(
                    this.table.getValueAt(row, VocabularyEditorHelper.SECOND_WORDS_COLUMN)).toLowerCase();

            if (firstColumn.contains(searchText) || secondColumn.contains(searchText)) {
                this.table.setRowSelectionInterval(row, row);
                this.table.scrollRectToVisible(this.table.getCellRect(row, 0, true));
                return;
            }
        }
    }
    
    private void askBeforeLeaving() {
        final int answer = JOptionPane.showConfirmDialog(this, "Save changes?", "Before Exit",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (answer == JOptionPane.YES_OPTION && !saveChanges()) {
            return;
        }
        if (answer != JOptionPane.CANCEL_OPTION && answer != JOptionPane.CLOSED_OPTION) {
            this.controller.showUserDashboardPanel();
        }
    }
}
