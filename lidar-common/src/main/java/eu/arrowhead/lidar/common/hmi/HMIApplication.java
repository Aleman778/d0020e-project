package eu.arrowhead.lidar.common.hmi;

import eu.arrowhead.common.api.ArrowheadApplication;
import eu.arrowhead.common.exception.ArrowheadException;

/**
 * HMIApplication is a common application used by both HMIConsumer and HMISubscriber
 */
public abstract class HMIApplication extends ArrowheadApplication {

    public static HMIWindow window;

    public HMIApplication(String[] args) throws ArrowheadException {
        super(args);

        HMIThemeManager.setTheme("dark");

        window = new HMIWindow();
        window.setVisible(true);
    }
}
