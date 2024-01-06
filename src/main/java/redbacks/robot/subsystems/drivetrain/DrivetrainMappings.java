package redbacks.robot.subsystems.drivetrain;

import redbacks.robot.Controllers;

import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;

import arachne4.lib.math.ArachneMath;
import arachne4.lib.scheduler.SchedulerProvider;
import arachne4.lib.scheduler.mappings.ButtonMapping;
import arachne4.lib.scheduler.mappings.ComplexMapping;
import arachne4.lib.scheduler.mappings.MappingManager;
import arachne4.lib.units.concats.Motion2dPercentage;

public class DrivetrainMappings extends MappingManager<Controllers> {
    public DrivetrainMappings(SchedulerProvider schedulerProvider, Controllers controllers) {
        super(schedulerProvider, controllers);
    }
	
	public final ButtonMapping resetHeading = mapping(controllers.driver::getStartButton);
	public final ButtonMapping activateSnapTo0 = mapping(controllers.driver::getLeftStickButton);
	public final ButtonMapping activateSnapTo180 = mapping(controllers.driver::getRightStickButton); 

	public final ComplexMapping<Motion2dPercentage> driverInputs = mapping(() -> {
		double forward = -controllers.driver.getRightY();
		double left = -controllers.driver.getRightX();

		double angle = Math.atan2(left, forward);

		// Apply linear deadzone
		double linearMagnitude = Math.sqrt(forward * forward + left * left);
		if(linearMagnitude != 0) {
			double correctedLinearMagnitude = ArachneMath.signedPow(applyJoystickDeadzone(Math.min(1, linearMagnitude)), JOYSTICK_EXPONENT_FOR_LINEAR);
			forward = correctedLinearMagnitude * Math.cos(angle);
			left = correctedLinearMagnitude * Math.sin(angle);
		}

		// Apply rotational deadzone
		double rotate = -applyJoystickDeadzone(controllers.driver.getLeftX());

		return new Motion2dPercentage(
			forward,
			left,
			ArachneMath.signedPow(rotate, JOYSTICK_EXPONENT_FOR_ROTATION)
		);
	});

    private static double applyJoystickDeadzone(double value) {
        if(Math.abs(value) < JOYSTICK_DEADZONE_RADIUS) return 0;

        if(value > 0) return (value - JOYSTICK_DEADZONE_RADIUS) / (1 - JOYSTICK_DEADZONE_RADIUS);
        else return (value + JOYSTICK_DEADZONE_RADIUS) / (1 - JOYSTICK_DEADZONE_RADIUS);
    }
}
