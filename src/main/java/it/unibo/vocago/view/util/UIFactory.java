package it.unibo.vocago.view.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.MouseEvent;

public final class UIFactory {

    private static final double SCALE = 1.13;
    private static final int ZERO = 0;
    private static final int PADDING = 1;
    private static final int TABLE_ROW_HEIGHT = 32;
    private static final int TABLE_HEADER_HEIGHT = 36;
    private static final int TEXT_SIZE = 14;
    private static final int CELL_BORDER_THICKNESS = 2;

    public static JButton createButton(final String text, final String iconPath,
            final int iconSize, final Color backGround, final int height, final int width,
            final boolean addListener, final boolean addIconHighlight, final boolean addFontHighlight,
            final Font font) {

        final JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setRolloverEnabled(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(font);
        button.setBackground(backGround);
        button.setForeground(UIConstants.TEXT_COLOR);
        button.setBorder(BorderFactory.createLineBorder(UIConstants.BUTTON_BORDER));

        if (iconPath != null && !iconPath.isBlank()) {
            final ImageIcon icon = loadIcon(iconPath);
            final Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setIconTextGap(5);
        }

        if (height > 0 && width > 0) {
            final Dimension dimension = new Dimension(width, height);
            button.setMinimumSize(dimension);
            button.setMaximumSize(dimension);
            button.setPreferredSize(dimension);
        }

        if (addListener) {
            buttonListener(button);
        }

        if (addIconHighlight && iconPath != null && !iconPath.isEmpty()) {
            highlightIcon(button);
        }

        if (addFontHighlight) {
            highlightFont(button);
        }

        return button;
    }

    public static ImageIcon loadIcon(final String iconPath) {
        if (iconPath == null || iconPath.isBlank()) {
            return new ImageIcon();
        }

        final URL resource = UIFactory.class.getClassLoader().getResource(iconPath);
        if (resource != null) {
            return new ImageIcon(resource);
        }

        return new ImageIcon(iconPath);
    }

    public static void buttonListener(final JButton button) {
        final MouseAdapter hoverListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }
        };
        button.addMouseListener(hoverListener);
    }

    public static void highlightIcon(final JButton button) {

        if (!(button.getIcon() instanceof ImageIcon icon)) {
            return;
        }

        final int width = button.getIcon().getIconWidth();
        final int height = button.getIcon().getIconHeight();
        if (width <= 0 || height <= 0) {
            return;
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                final Image scaledImage = icon.getImage().getScaledInstance(
                        (int) (width * SCALE), (int) (height * SCALE), Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                final Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
            }
        });
    }

    public static void highlightFont(final JButton button) {
        final Font normalFont = button.getFont();
        final Font hoverFont = normalFont.deriveFont((float) (normalFont.getSize() * SCALE));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setFont(hoverFont);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setFont(normalFont);
            }
        });
    }

    public static JButton createButton(final String text) {
        return createButton(text, "", 1, UIConstants.BUTTON_BACKGROUND,
                ZERO, ZERO, true, false, false, UIConstants.FONT);
    }

    public static JLabel createLabel(final String text, final Font font) {
        final JLabel label = new JLabel(text);
        label.setBackground(UIConstants.BACKGROUND);
        label.setFont(font);
        label.setForeground(UIConstants.TEXT_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setAlignmentY(Component.CENTER_ALIGNMENT);
        return label;
    }

    public static JTextField createTextField(final Font font) {
        final JTextField field = new JTextField(TEXT_SIZE);
        field.setFont(font);
        field.setBackground(UIConstants.TEXT_FIELD_BACKGROUND);
        field.setForeground(UIConstants.TEXT_COLOR);
        field.setCaretColor(UIConstants.TEXT_COLOR);
        field.setBorder(BorderFactory.createLineBorder(UIConstants.TEXT_FIELD_BORDER));
        return field;
    }

    public static JTextField createTextField() {
        return createTextField(UIConstants.FONT);
    }

    public static JPanel createPanel(final LayoutManager layout) {

        final JPanel panel = new JPanel();
        panel.setLayout(layout);
        stylePanel(panel);
        return panel;
    }

    public static JPanel createPanel() {
        final JPanel panel = createPanel(new FlowLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        stylePanel(panel);
        return panel;
    }

    public static void stylePanel(final JPanel panel) {
        panel.setBackground(UIConstants.BACKGROUND);
        panel.setForeground(UIConstants.TEXT_COLOR);
    }

    public static void brighter(final Component component) {
        component.setBackground(component.getBackground().brighter());
    }

    public static void highlight(final JComponent component) {
        component.setBorder(BorderFactory.createLineBorder(UIConstants.PANEL_BORDER));
    }

    public static <T> JComboBox<T> createComboBox(final T[] items) {
        final JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setFont(UIConstants.FONT);
        comboBox.setBackground(UIConstants.COMBOBOX_BACKGROUND);
        comboBox.setForeground(UIConstants.TEXT_COLOR);
        comboBox.setBorder(BorderFactory.createLineBorder(UIConstants.COMBOBOX_BORDER));
        return comboBox;
    }

    public static JTable createTable(final DefaultTableModel model) {
        final JTable table = new JTable(model);
        final JTableHeader tableHeader = table.getTableHeader();

        UIManager.put("TableHeader.cellBorder", BorderFactory.createMatteBorder(PADDING,
                PADDING, PADDING, PADDING, UIConstants.TABLE_GRID));

        UIManager.put("Table.focusCellHighlightBorder",
                BorderFactory.createLineBorder(UIConstants.TABLE_CELL_SELECTION, CELL_BORDER_THICKNESS));

        table.setBackground(UIConstants.BACKGROUND.brighter());
        tableHeader.setBackground(UIConstants.TABLE_HEADER);

        table.setFont(UIConstants.FONT);
        tableHeader.setFont(UIConstants.TABLE_HEADER_FONT);

        table.setForeground(UIConstants.TEXT_COLOR);
        tableHeader.setForeground(UIConstants.TEXT_COLOR);

        table.setRowHeight(TABLE_ROW_HEIGHT);

        tableHeader.setPreferredSize(new Dimension(tableHeader.getPreferredSize().width, TABLE_HEADER_HEIGHT));

        table.setGridColor(UIConstants.TABLE_GRID);

        table.setSelectionForeground(UIConstants.TEXT_COLOR);
        tableHeader.setOpaque(true);

        // make different colors for odd and even rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    final JTable table, final Object value, final boolean isSelected, final boolean hasFocus,
                    final int row, final int column) {
                        final Component cell = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                        if (isSelected) {
                            cell.setBackground(UIConstants.TABLE_SELECTION);
                            cell.setForeground(UIConstants.DARK_TEXT_COLOR);
                        } else {
                            cell.setBackground(row % 2 == 0 ? UIConstants.TABLE_ROW_EVEN : UIConstants.TABLE_ROW_ODD);
                            cell.setForeground(UIConstants.TEXT_COLOR);
                        }

                        return cell;
            }
        });

        return table;
    }

    public static JScrollPane createScrollPane(final JTable table) {
        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(UIConstants.BACKGROUND);
        scrollPane.getViewport().setBackground(UIConstants.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBorder(BorderFactory.createMatteBorder(PADDING, ZERO,
                PADDING, ZERO, UIConstants.PANEL_BORDER));
        styleScrollBar(scrollPane.getVerticalScrollBar());
        // styleScrollBar(scrollPane.getHorizontalScrollBar());
        return scrollPane;
    }

    public static void styleScrollBar(final JScrollBar scrollBar) {
        scrollBar.setBackground(UIConstants.SCROLLBAR_TRACK);
        scrollBar.setUnitIncrement(16);
        scrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.trackColor = UIConstants.SCROLLBAR_TRACK;
                this.thumbColor = UIConstants.SCROLLBAR_THUMB;
            }

            @Override
            protected JButton createDecreaseButton(final int orientation) {
                return createScrollButton();
            }

            @Override
            protected JButton createIncreaseButton(final int orientation) {
                return createScrollButton();
            }

            private JButton createScrollButton() {
                final JButton button = new JButton();
                button.setBackground(UIConstants.SCROLLBAR_TRACK);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintTrack(final Graphics g, final JComponent c, final java.awt.Rectangle trackBounds) {
                g.setColor(UIConstants.SCROLLBAR_TRACK);
                g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }

            @Override
            protected void paintThumb(final Graphics g, final JComponent c, final java.awt.Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !this.scrollbar.isEnabled()) {
                    return;
                }

                final Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(isThumbRollover() ? UIConstants.SCROLLBAR_THUMB_HOVER : UIConstants.SCROLLBAR_THUMB);
                g2.fillRoundRect(
                        thumbBounds.x + 2,
                        thumbBounds.y + 2,
                        thumbBounds.width - 4,
                        thumbBounds.height - 4,
                        10,
                        10);
                g2.dispose();
            }
        });
    }

    public static String toHex(final Color color) {
        return String.format("#%02x%02x%02x",
                color.getRed(),
                color.getGreen(),
                color.getBlue());
    }

    private UIFactory() {
    }

}
