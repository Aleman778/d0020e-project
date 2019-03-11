package eu.arrowhead.lidar.common.hmi;

import java.awt.*;

public class HMIThemeManager {

    private static HMITheme theme = createDefaultTheme();

    private HMIThemeManager() { }

    public static HMITheme getTheme() {
        return theme;
    }

    public static void setTheme(String theme) {
        switch (theme) {
            case "dark":
                HMIThemeManager.theme = createDarkTheme();
                break;
            default:
                HMIThemeManager.theme = createDefaultTheme();
                break;
        }
    }

    private static HMITheme createDefaultTheme() {
        HMITheme theme = new HMITheme();
        theme.bgColor = Color.WHITE;
        theme.fgColor = Color.BLACK;
        theme.bgPrimaryColor = new Color(218, 218, 218);
        theme.fgPrimaryColor = Color.BLACK;
        theme.gridColor = Color.LIGHT_GRAY;
        theme.dataColor = Color.RED;
        theme.sensorColor = new Color(0, 91, 202);
        return theme;
    }

    private static HMITheme createDarkTheme() {
        HMITheme theme = new HMITheme();
        theme.bgColor = new Color(43, 43, 43);
        theme.fgColor = Color.WHITE;
        theme.bgPrimaryColor = new Color(60, 63, 65);
        theme.fgPrimaryColor = Color.WHITE;
        theme.gridColor = new Color(55, 55, 55);
        theme.dataColor = new Color(149, 0, 0);
        theme.sensorColor = new Color(37, 83, 194);
        return theme;
    }
}
