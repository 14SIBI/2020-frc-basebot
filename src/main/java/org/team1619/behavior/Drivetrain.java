package org.team1619.behavior;

import org.uacr.models.behavior.Behavior;
import org.uacr.shared.abstractions.InputValues;
import org.uacr.shared.abstractions.OutputValues;
import org.uacr.shared.abstractions.RobotConfiguration;
import org.uacr.utilities.Config;
import org.uacr.utilities.logging.LogManager;
import org.uacr.utilities.logging.Logger;
import org.uacr.utilities.Timer;

import java.util.Set;

/**
 * Let's you move backward and forward with left stick and turn with right stick
 */

public class Drivetrain implements Behavior {

	private static final Logger sLogger = LogManager.getLogger(Drivetrain.class);
	private static final Set<String> sSubsystems = Set.of("ss_drivetrain");

	private final InputValues fSharedInputValues;
	private final OutputValues fSharedOutputValues;
//	private final String fWhatThisButtonDoes;
	private Timer mTimer;
	private final String fXAxis;
	private final String fYAxis;

	private int mConfigurationValue;

	public Drivetrain(InputValues inputValues, OutputValues outputValues, Config config, RobotConfiguration robotConfiguration) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;
		fXAxis = robotConfiguration.getString("global_drivetrain", "x");
		fYAxis = robotConfiguration.getString("global_drivetrain", "y");

		mConfigurationValue = 0;
		mTimer = new Timer();
	}

	@Override
	public void initialize(String stateName, Config config) {
		sLogger.debug("Entering state {}", stateName);

		mConfigurationValue = config.getInt("config_key", 0);
		mTimer.start(mConfigurationValue);
	}

	@Override
	public void update() {
		double xAxis = fSharedInputValues.getNumeric(fXAxis);
		double yAxis = fSharedInputValues.getNumeric(fYAxis);

		double leftMotorSpeed = yAxis + xAxis;
		double rightMotorSpeed = yAxis - xAxis;

		if (leftMotorSpeed > 1) {
			rightMotorSpeed = rightMotorSpeed - (leftMotorSpeed - 1);
			leftMotorSpeed = 1;
		} else if (leftMotorSpeed < -1) {
			rightMotorSpeed = rightMotorSpeed - (leftMotorSpeed + 1);
			leftMotorSpeed = -1;
		} else if (rightMotorSpeed > 1) {
			leftMotorSpeed = leftMotorSpeed - (rightMotorSpeed - 1);
			rightMotorSpeed = 1;
		} else if (leftMotorSpeed < -1) {
			leftMotorSpeed = leftMotorSpeed - (rightMotorSpeed + 1);
			rightMotorSpeed = -1;
		}

		fSharedOutputValues.setNumeric("opn_drivetrain_left","percent",leftMotorSpeed);
		fSharedOutputValues.setNumeric("opn_drivetrain_right","percent",rightMotorSpeed);




//		boolean whatThisButtonDoes = fSharedInputValues.getBoolean(fWhatThisButtonDoes);
//		fSharedInputValues.setBoolean("opb_example", whatThisButtonDoes);
	}

	@Override
	public void dispose() {
		sLogger.trace("Leaving State ()");
		fSharedOutputValues.setNumeric("opn_drivetrain_left","percent",0.0);
		fSharedOutputValues.setNumeric("opn_drivetrain_right","percent",0.0);
	}

	@Override
	public boolean isDone() {
		return mTimer.isDone();
	}

	@Override
	public Set<String> getSubsystems() {
		return sSubsystems;
	}
}