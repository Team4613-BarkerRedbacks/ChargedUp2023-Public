package redbacks.robot.subsystems.drivetrain;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import redbacks.field.FieldLocations;

import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;

import arachne4.lib.units.Metres;
import arachne4.lib.units.MetresPerSec;
import arachne4.lib.units.RadiansPerSec;

public class AutomaticSlowdownAtGridBehaviour extends DrivetrainBehaviour {
    ProfiledPIDController controller = new ProfiledPIDController(LINEAR_KP, 0, LINEAR_KD, LINEAR_MOTION_CONSTRAINTS);

    public AutomaticSlowdownAtGridBehaviour(DrivetrainHardware hardware) {
        super(hardware);
    }

    @Override
    protected void acceptDriverInputs(double forward, double left, double rotate) {
        double target = FieldLocations.gridDepth + ROBOT_OFFSET_X_FROM_EDGE;
        double maxVelocity = calculateMaxVelocity(hardware.drivetrain.getPosition().asRaw(Metres::convert).getX(), target);
        double minVelocity = calculateMinVelocity(hardware.drivetrain.getPosition().asRaw(Metres::convert).getX(), target);
        double xVelocity = MAX_LINEAR_VELOCITY.asRaw(MetresPerSec::convert) * forward;

        if(Math.abs(hardware.drivetrain.getPosition().getLocation().getY().asRaw(Metres::convert)) < Math.abs(FieldLocations.blue_communityEdgeY)) {
            if(xVelocity > maxVelocity) xVelocity = maxVelocity;
            else if(xVelocity < minVelocity) xVelocity = minVelocity;
        }

        hardware.drivetrain.driveWithVelocity(
            ChassisSpeeds.fromFieldRelativeSpeeds(
                xVelocity,
                MAX_LINEAR_VELOCITY.asRaw(MetresPerSec::convert) * left,
                MAX_ROTATIONAL_VELOCITY.asRaw(RadiansPerSec::convert) * rotate,
                hardware.drivetrain.getPosition().getHeading()
            )
        );
    }

    protected double calculateMaxVelocity(double xPosition, double targetX) {
        return MAX_LINEAR_VELOCITY.asRaw(MetresPerSec::convert);
    }

    protected double calculateMinVelocity(double xPosition, double targetX) {
        return controller.calculate(xPosition, targetX);
    }
}
