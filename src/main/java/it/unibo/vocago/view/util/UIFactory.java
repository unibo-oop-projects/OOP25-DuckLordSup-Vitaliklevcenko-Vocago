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

/**
 * Factory of styled Swing components, centralising the creation and theming of
 * the widgets used throughout the user interface so the panels do not repeat
 * styling logic.
 */
public final class UIFactory {

    private static final double SCALE = 1.13;
    private static final int ZERO = 0;
    private static final int PADDING = 1;
    private static final int TABLE_ROW_HEIGHT = 32;
    private static final int TABLE_HEADER_HEIGHT = 36;
    private static final int TEXT_SIZE = 14;
    private static final int CELL_BORDER_THICKNESS = 2;
    private static final int ICON_TEXT_GAP = 5;
    private static final int SCROLL_UNIT_INCREMENT = 16;
    private static final int SCROLL_THUMB_INSET = 2;
    private static final int SCROLL_THUMB_ARC_SIZE = 10;

    private UIFactory() {
    }

    /**
     * Creates a fully configurable styled button.
     *
     * @param text             the button text
     * @param iconPath         the path of the icon to display, or blank for none
     * @param iconSize         the icon size in pixels
     * @param backGround       the background color
     * @param height           the preferred height, or non-positive to leave unset
     * @param width            the preferred width, or non-positive to leave unset
     * @param addListener      whether to add the default hover listener
     * @param addIconHighlight whether the icon should enlarge on hover
     * @param addFontHighlight whether the font should enlarge on hover
     * @param font             the button font
     * @return the configured button
     */
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
            button.setIconTextGap(ICON_TEXT_GAP);
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

    /**
     * Creates a styled button with default styling and the given text.
     *
     * @param text the button text
     * @return the configured button
     */
    public static JButton createButton(final String text) {
        return createButton(text, "", 1, UIConstants.BUTTON_BACKGROUND,
                ZERO, ZERO, true, false, false, UIConstants.FONT);
    }

    /**
     * Loads an image icon from the given path, returning an empty icon if the
     * path is blank or the resource cannot be found.
     *
     * @param iconPath the path of the icon
     * @return the loaded icon, or an empty icon
     */
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

    /**
     * Adds a hover listener that brightens the button while the pointer is over it.
     *
     * @param button the button to decorate
     */
    public static void buttonListener(final JButton button) {
        final MouseAdapter hoverListener = new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }
        };
        button.addMouseListener(hoverListener);
    }

    /**
     * Adds a hover effect that enlarges the button's icon while the pointer is
     * over it.
     *
     * @param button the button to decorate
     */
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
            public void mouseEntered(final MouseEvent e) {
                final Image scaledImage = icon.getImage().getScaledInstance(
                        (int) (width * SCALE), (int) (height * SCALE), Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                final Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
            }
        });
    }

    /**
     * Adds a hover effect that enlarges the button's font while the pointer is
     * over it.
     *
     * @param button the button to decorate
     */
    public static void highlightFont(final JButton button) {
        final Font normalFont = button.getFont();
        final Font hoverFont = normalFont.deriveFont((float) (normalFont.getSize() * SCALE));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                button.setFont(hoverFont);
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                button.setFont(normalFont);
            }
        });
    }

    /**
     * Creates a styled, centered label.
     *
     * @param text the label text
     * @param font the label font
     * @return the configured label
     */
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

    /**
     * Creates a styled text field with the given font.
     *
     * @param font the field font
     * @return the configured text field
     */
    public static JTextField createTextField(final Font font) {
        final JTextField field = new JTextField(TEXT_SIZE);
        field.setFont(font);
        field.setBackground(UIConstants.TEXT_FIELD_BACKGROUND);
        field.setForeground(UIConstants.TEXT_COLOR);
        field.setCaretColor(UIConstants.TEXT_COLOR);
        field.setBorder(BorderFactory.createLineBorder(UIConstants.TEXT_FIELD_BORDER));
        return field;
    }

    /**
     * Creates a styled text field with the default font.
     *
     * @return the configured text field
     */
    public static JTextField createTextField() {
        return createTextField(UIConstants.FONT);
    }

    /**
     * Creates a styled panel using the given layout.
     *
     * @param layout the layout manager
     * @return the configured panel
     */
    public static JPanel createPanel(final LayoutManager layout) {

        final JPanel panel = new JPanel();
        panel.setLayout(layout);
        stylePanel(panel);
        return panel;
    }

    /**
     * Creates a styled panel with a vertical box layout.
     *
     * @return the configured panel
     */
    public static JPanel createPanel() {
        final JPanel panel = createPanel(new FlowLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        stylePanel(panel);
        return panel;
    }

    /**
     * Applies the standard background and foreground colors to a panel.
     *
     * @param panel the panel to style
     */
    public static void stylePanel(final JPanel panel) {
        panel.setBackground(UIConstants.BACKGROUND);
        panel.setForeground(UIConstants.TEXT_COLOR);
    }

    /**
     * Brightens the background color of the given component.
     *
     * @param component the component to brighten
     */
    public static void brighter(final Component component) {
        component.setBackground(component.getBackground().brighter());
    }

    /**
     * Adds a border to the given component to highlight it.
     *
     * @param component the component to highlight
     */
    public static void highlight(final JComponent component) {
        component.setBorder(BorderFactory.createLineBorder(UIConstants.PANEL_BORDER));
    }

    /**
     * Creates a styled combo box containing the given items.
     *
     * @param <T>   the type of the items
     * @param items the items to display
     * @return the configured combo box
     */
    public static <T> JComboBox<T> createComboBox(final T[] items) {
        final JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setFont(UIConstants.FONT);
        comboBox.setBackground(UIConstants.COMBOBOX_BACKGROUND);
        comboBox.setForeground(UIConstants.TEXT_COLOR);
        comboBox.setBorder(BorderFactory.createLineBorder(UIConstants.COMBOBOX_BORDER));
        return comboBox;
    }

    /**
     * Creates a styled table with a given model, including alternating
     * row colors and a themed header.
     *
     * @param model the table model
     * @return the configured table
     */
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

    /**
     * Creates a styled scroll pane wrapping the given table.
     *
     * @param table the table to wrap
     * @return the configured scroll pane
     */
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

    /**
     * Applies the custom dark theme to the given scroll bar.
     *
     * @param scrollBar the scroll bar to style
     */
    public static void styleScrollBar(final JScrollBar scrollBar) {
        scrollBar.setBackground(UIConstants.SCROLLBAR_TRACK);
        scrollBar.setUnitIncrement(SCROLL_UNIT_INCREMENT);
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
                        thumbBounds.x + SCROLL_THUMB_INSET,
                        thumbBounds.y + SCROLL_THUMB_INSET,
                        thumbBounds.width - SCROLL_THUMB_INSET * 2,
                        thumbBounds.height - SCROLL_THUMB_INSET * 2,
                        SCROLL_THUMB_ARC_SIZE,
                        SCROLL_THUMB_ARC_SIZE);
                g2.dispose();
            }
        });
    }

    /**
     * Converts a color to its hexadecimal string representation.
     *
     * @param color the color to convert
     * @return the color as a {@code #rrggbb} string
     */
    public static String toHex(final Color color) {
        return String.format("#%02x%02x%02x",
                color.getRed(),
                color.getGreen(),
                color.getBlue());
    }

}
