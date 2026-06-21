package it.unibo.vocago.view.util;

import java.awt.Color;
import java.awt.Font;

/**
 * Holds the visual constants used across the user interface, such as colors,
 * fonts and spacing values, defining the application's dark-grey theme.
 */
public final class UIConstants {

    public static final int BACK_BUTTON_ICON_SIZE = 60;
    public static final int BACK_BUTTON_WIDTH = 70;
    public static final int BACK_BUTTON_HEIGHT = 60;
    public static final int SPACING_SMALL = 10;
    public static final int SPACING_MEDIUM = 15;
    public static final int SPACING_LARGE = 20;

    public static final Color BACKGROUND = new Color(50, 50, 50);
    public static final Color BUTTON_BACKGROUND = new Color(80, 80, 80);
    public static final Color SCROLLBAR_TRACK = new Color(60, 60, 60);
    public static final Color TABLE_HEADER = new Color(50, 50, 50);

    public static final Color GREEN = new Color(50, 130, 50);
    public static final Color RED = new Color(160, 50, 50);
    public static final Color BLUE = new Color(90, 130, 170);
    public static final Color TEAL = new Color(65, 130, 125);
    public static final Color AMBER = new Color(170, 120, 45);

    public static final Color TEXT_FIELD_BACKGROUND = new Color(110, 110, 110);
    public static final Color COMBOBOX_BACKGROUND = new Color(110, 110, 110);
    public static final Color COMBOBOX_BRIGHT_BACKGROUND = new Color(110, 110, 110);
    public static final Color SCROLLBAR_THUMB = new Color(110, 110, 110);

    public static final Color TEXT_FIELD_BORDER = new Color(130, 130, 130);
    public static final Color COMBOBOX_BORDER = new Color(130, 130, 130);
    public static final Color BUTTON_BORDER = new Color(130, 130, 130);
    public static final Color SCROLLBAR_THUMB_HOVER = new Color(130, 130, 130);
    public static final Color BACKGROUND_HOVER = new Color(110, 110, 110);
    public static final Color PANEL_BORDER = new Color(110, 110, 110);
    public static final Color TABLE_GRID = new Color(110, 110, 110);
    public static final Color BRIGHT_PANEL_BORDER = new Color(200, 200, 200);

    public static final Color DARK_TEXT_COLOR = new Color(50, 50, 50);
    public static final Color TEXT_COLOR = new Color(230, 230, 230);
    public static final Color TABLE_CELL_SELECTION = new Color(240, 240, 250);

    public static final Color TABLE_SELECTION = new Color(163, 184, 204);
    public static final Color TABLE_ROW_EVEN = new Color(65, 65, 65);
    public static final Color TABLE_ROW_ODD = new Color(75, 75, 75);

    public static final Font TITLE_FONT = new Font(UIConstants.FONT_NAME, Font.BOLD, 32);
    public static final Font PROMPT_FONT = new Font(UIConstants.FONT_NAME, Font.BOLD, 24);
    public static final Font FONT = new Font(UIConstants.FONT_NAME, Font.PLAIN, 18);
    public static final Font TABLE_HEADER_FONT = new Font(UIConstants.FONT_NAME, Font.BOLD, 22);
    public static final Font BIG_PROMT_FONT = new Font(UIConstants.FONT_NAME, Font.PLAIN, 46);

    private static final String FONT_NAME = "Roboto";

    private UIConstants() {
    }
}
