package org.team1619.behavior;

import org.uacr.models.behavior.Behavior;
import org.uacr.shared.abstractions.InputValues;
import org.uacr.shared.abstractions.OutputValues;
import org.uacr.shared.abstractions.RobotConfiguration;
import org.uacr.utilities.Config;
import org.uacr.utilities.Timer;
import org.uacr.utilities.logging.LogManager;
import org.uacr.utilities.logging.Logger;

import java.util.Set;


public class Drivetrain implements Behavior {

    private static final Logger sLogger = LogManager.getLogger(Drivetrain.class);
    private static final Set<String> sSubsystems = Set.of("ss_drivetrain");

    private final InputValues fSharedInputValues;
    private final OutputValues fSharedOutputValues;
//    private Timer mTimer;

    private String leftYAxis;
    private String rightYAxis;

    private int mConfigurationValue;

    public Drivetrain(InputValues inputValues, OutputValues outputValues, Config config, RobotConfiguration robotConfiguration) {
        fSharedInputValues = inputValues;
        fSharedOutputValues = outputValues;

        leftYAxis = robotConfiguration.getString("global_drivetrain","y_left");
        rightYAxis = robotConfiguration.getString("global_drivetrain","y_right");

        mConfigurationValue = 0;
//        mTimer = new Timer();
    }

    @Override
    public void initialize(String stateName, Config config) {
        sLogger.debug("Entering state {}", stateName);

        mConfigurationValue = config.getInt("config_key", 0);
//        mTimer.start(mConfigurationValue);
    }

    @Override
    public void update() {
        double leftMotorSpeed = fSharedInputValues.getNumeric(leftYAxis);
        double rightMotorSpeed = fSharedInputValues.getNumeric(rightYAxis);

        fSharedOutputValues.setNumeric("opn_drivetrain_left","percent",leftMotorSpeed);
        fSharedOutputValues.setNumeric("opn_drivetrain_right","percent",rightMotorSpeed);
    }

    @Override
    public void dispose() {
        sLogger.trace("Leaving State ()");
        fSharedOutputValues.setNumeric("opn_drivetrain_left", "percent", 0.0);
        fSharedOutputValues.setNumeric("opn_drivetrain_right", "percent", 0.0);

    }


    @Override
    public boolean isDone() {
     // return mTimer.isDone();
        return false;
    }

    @Override
    public Set<String> getSubsystems() {
        return sSubsystems;
    }
}

