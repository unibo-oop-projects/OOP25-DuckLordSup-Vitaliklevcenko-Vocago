package it.unibo.vocago.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.model.progress.WordProgress;
import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.types.MasteryLevel;
import it.unibo.vocago.model.vocabulary.Dictionary;
import it.unibo.vocago.model.vocabulary.DictionaryEntry;
import it.unibo.vocago.model.vocabulary.WordEntry;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.model.vocabulary.api.Word;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;

public class VocabularyEditorPanel extends JPanel{
    
    private final int FIRST_WORDS_COLUMN = 0;
    private final int SECOND_WORDS_COLUMN = 1;
    private final int MEMORIZATION_COLUMN = 2;
    private final int RECOGNITION_COLUMN = 3;
    private final int FIRST_PROGRESS_COLUMN = 4;
    private final int SECOND_PROGRESS_COLUMN = 5;
    private final int MASTERY_COLUMN_WIDTH = 160;
    private final int HEADER_HEIGHT = 60;
    private final int FOOTER_HEIGHT = 60;
    private final int ZERO = 0;
    private final int WORD_COLUMN_WIDTH = 280;

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

    public void buildLayout() {
        setLayout(new BorderLayout());
        add(buildUpperPanel(), BorderLayout.NORTH);
        add(buildTableScrollPane(), BorderLayout.CENTER);
        add(buildLowerPanel(), BorderLayout.SOUTH);
        showVocabulary();
    }

    private JPanel buildUpperPanel() {
        final JPanel upperButtonsPanel = UIFactory.createPanel(new GridLayout());
        upperButtonsPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, HEADER_HEIGHT));

        final JPanel leftPanel = UIFactory.createPanel(new BorderLayout());
        leftPanel.add(this.goBackButton, BorderLayout.WEST);
        upperButtonsPanel.add(leftPanel);

        upperButtonsPanel.add(UIFactory.createLabel(
                "My " + this.controller.getCurrentProfile().getSecondLanguage() + " Vocabulary",
                UIConstants.TITLE_FONT));

        final JPanel rightPanel = UIFactory.createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
        rightPanel.add(this.searchTextField);
        rightPanel.add(this.findButton);
        upperButtonsPanel.add(rightPanel);

        return upperButtonsPanel;
    }

    private JScrollPane buildTableScrollPane() {

        model.addColumn(this.controller.getCurrentProfile().getFirstLanguage());
        model.addColumn(this.controller.getCurrentProfile().getSecondLanguage());
        model.addColumn("Memorization level");
        model.addColumn("Recognition level");
        model.addColumn("firstProgress");
        model.addColumn("secondProgress");

        final DefaultCellEditor wordEditor = new DefaultCellEditor(UIFactory.createTextField());
        this.table.getColumnModel().getColumn(FIRST_WORDS_COLUMN).setCellEditor(wordEditor);
        this.table.getColumnModel().getColumn(SECOND_WORDS_COLUMN).setCellEditor(wordEditor);

        final DefaultCellEditor masteryEditor = new DefaultCellEditor(UIFactory.createComboBox(MasteryLevel.values()));
        this.table.getColumnModel().getColumn(MEMORIZATION_COLUMN).setCellEditor(masteryEditor);
        this.table.getColumnModel().getColumn(RECOGNITION_COLUMN).setCellEditor(masteryEditor);

        this.table.getColumnModel().getColumn(FIRST_WORDS_COLUMN)
                .setPreferredWidth(WORD_COLUMN_WIDTH);
        this.table.getColumnModel().getColumn(SECOND_WORDS_COLUMN)
                .setPreferredWidth(WORD_COLUMN_WIDTH);
        this.table.getColumnModel().getColumn(MEMORIZATION_COLUMN)
                .setPreferredWidth(MASTERY_COLUMN_WIDTH);
        this.table.getColumnModel().getColumn(RECOGNITION_COLUMN)
                .setPreferredWidth(MASTERY_COLUMN_WIDTH);

        hideColumn(FIRST_PROGRESS_COLUMN);
        hideColumn(SECOND_PROGRESS_COLUMN);

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
        lowerButtonsPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, FOOTER_HEIGHT));
        return lowerButtonsPanel;
    }

    private void hideColumn(final int columnIndex) {
        this.table.getColumnModel().getColumn(columnIndex).setMinWidth(ZERO);
        this.table.getColumnModel().getColumn(columnIndex).setMaxWidth(ZERO);
        this.table.getColumnModel().getColumn(columnIndex).setPreferredWidth(ZERO);
        this.table.getColumnModel().getColumn(columnIndex).setWidth(ZERO);
    }

    private void actionRegister() {
        this.addRowButton.addActionListener(e -> addEmptyRow());
        this.deleteRowButton.addActionListener(e -> deleteSelectedRows());
        this.goBackButton.addActionListener(e -> askBeforeLeaving());
        this.saveChangesButton.addActionListener(e -> {
            if (saveChanges()) {
                this.controller.showProfileDashboardPanel();
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
        this.controller.saveVocabulary(toVocabulary(this.model));
        return true;
    }

    private void showVocabulary() {
        for (final VocabularyItem item : this.controller.getCurrentProfile().getVocabulary().getItems()) {
            this.model.addRow(rowFromVocabularyItem(item));
        }
        addEmptyRow();
    }

    private void addEmptyRow() {
        this.model.addRow(emptyRow());
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
            final String firstColumn = cellToText(
                    this.table.getValueAt(row, FIRST_WORDS_COLUMN)).toLowerCase();
            final String secondColumn = cellToText(
                    this.table.getValueAt(row, SECOND_WORDS_COLUMN)).toLowerCase();

            if (firstColumn.contains(searchText) || secondColumn.contains(searchText)) {
                this.table.setRowSelectionInterval(row, row);
                this.table.scrollRectToVisible(this.table.getCellRect(row, 0, true));
                return;
            }
        }
    }
    
    private void askBeforeLeaving() {
        final int answer = this.controller.saveBeforeLeaving();

        if (answer == JOptionPane.YES_OPTION && !saveChanges()) {
            return;
        }
        if (answer != JOptionPane.CANCEL_OPTION && answer != JOptionPane.CLOSED_OPTION) {
            this.controller.showProfileDashboardPanel();
        }
    }

    private Object[] rowFromVocabularyItem(final VocabularyItem item) {
        return new Object[] {
                wordsToText(item.getFirstLanguageWords()),
                wordsToText(item.getSecondLanguageWords()),
                item.getProgress(Direction.FIRST_TO_SECOND).getMasteryLevel(),
                item.getProgress(Direction.SECOND_TO_FIRST).getMasteryLevel(),
                item.getProgress(Direction.FIRST_TO_SECOND),
                item.getProgress(Direction.SECOND_TO_FIRST)
        };
    }

    private String wordsToText(final List<Word> words) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            builder.append(words.get(i).getWord());
            if (i < words.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private Object[] emptyRow() {
        return new Object[] {
                "",
                "",
                MasteryLevel.NEW,
                MasteryLevel.NEW,
                new WordProgress(),
                new WordProgress()
        };
    }

    private Vocabulary toVocabulary(final DefaultTableModel model) {
        final List<VocabularyItem> vocabularyItems = new ArrayList<>();

        for (int row = 0; row < model.getRowCount(); row++) {

            final String firstText = cellToText(model.getValueAt(row, FIRST_WORDS_COLUMN));
            final String secondText = cellToText(model.getValueAt(row, SECOND_WORDS_COLUMN));
            if (firstText.isBlank() && secondText.isBlank()) {
                continue;
            }

            final MasteryLevel firstMasteryLevel = masteryLevelAt(model, row, MEMORIZATION_COLUMN);
            final MasteryLevel secondMasteryLevel = masteryLevelAt(model, row, RECOGNITION_COLUMN);

            vocabularyItems.add(new DictionaryEntry(
                    parseWordEntries(firstText),
                    parseWordEntries(secondText),
                    progressWithMastery(model.getValueAt(row, FIRST_PROGRESS_COLUMN), firstMasteryLevel),
                    progressWithMastery(model.getValueAt(row, SECOND_PROGRESS_COLUMN), secondMasteryLevel)));
        }

        return new Dictionary(vocabularyItems);
    }

    private String cellToText(final Object value) {
        return value == null ? "" : value.toString().trim();
    }

    private List<Word> parseWordEntries(final String text) {
        final List<Word> entries = new ArrayList<>();
        for (final String part : text.split(",")) {
            final String trimmed = part.trim();
            if (!trimmed.isBlank()) {
                entries.add(new WordEntry(trimmed));
            }
        }
        return entries;
    }
    
    private MasteryLevel masteryLevelAt(final DefaultTableModel model, final int row, final int column) {
        return model.getValueAt(row, column) instanceof MasteryLevel masteryLevel ? masteryLevel : MasteryLevel.NEW;
    }

    private Progress progressWithMastery(final Object value, final MasteryLevel masteryLevel) {
        return value instanceof Progress progress ? new WordProgress(masteryLevel, progress.getCorrectAnswers(),
            progress.getWrongAnswers()) : new WordProgress(masteryLevel);
    }
}
