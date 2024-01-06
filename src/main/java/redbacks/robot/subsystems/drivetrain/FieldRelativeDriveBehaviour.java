package redbacks.robot.subsystems.drivetrain;

import edu.wpi.first.math.kinematics.ChassisSpeeds;

import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;

import arachne4.lib.units.MetresPerSec;
import arachne4.lib.units.RadiansPerSec;

public class FieldRelativeDriveBehaviour extends DrivetrainBehaviour {
    public FieldRelativeDriveBehaviour(DrivetrainHardware hardware) {
        super(hardware);
    }

    @Override
    protected void acceptDriverInputs(double forward, double left, double rotate) {
        hardware.drivetrain.driveWithVelocity(
            ChassisSpeeds.fromFieldRelativeSpeeds(
                MAX_LINEAR_VELOCITY.asRaw(MetresPerSec::convert) * forward,
                MAX_LINEAR_VELOCITY.asRaw(MetresPerSec::convert) * left,
                MAX_ROTATIONAL_VELOCITY.asRaw(RadiansPerSec::convert) * rotate,
                hardware.drivetrain.getPosition().getHeading()
            )
        );
    }
}
