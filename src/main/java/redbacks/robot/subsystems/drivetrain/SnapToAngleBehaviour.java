package redbacks.robot.subsystems.drivetrain;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import redbacks.robot.Constants;

import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;

import arachne4.lib.units.MetresPerSec;
import arachne4.lib.units.Seconds;

public class SnapToAngleBehaviour extends DrivetrainBehaviour {
    final ProfiledPIDController rotationController = new ProfiledPIDController(
        15, 0, 0.3,
        new TrapezoidProfile.Constraints(AUTO_MAX_ROTATIONAL_VELOCITY_RADIANS_PER_SEC*2, AUTO_MAX_ROTATIONAL_ACCELERATION_RADIANS_PER_SEC_SQUARED*4),
        Constants.LOOP_PERIOD.asRaw(Seconds::convert)
    );

    final Rotation2d targetAngle;

    public SnapToAngleBehaviour(DrivetrainHardware hardware, Rotation2d targetAngle) {
        super(hardware);

        this.targetAngle = targetAngle;
        this.rotationController.enableContinuousInput(-Math.PI, Math.PI);
    }

    @Override
    public void onEnterMode() {
        rotationController.reset(hardware.drivetrain.getPosition().getHeading().getRadians(), hardware.drivetrain.getVelocityMetresAndRadiansPerSec().omegaRadiansPerSecond);
        rotationController.setGoal(targetAngle.getRadians());
    }

    @Override
    protected void acceptDriverInputs(double forward, double left, double rotate) {
        hardware.drivetrain.driveWithVelocity(
            ChassisSpeeds.fromFieldRelativeSpeeds(
                MAX_LINEAR_VELOCITY.asRaw(MetresPerSec::convert) * forward,
                MAX_LINEAR_VELOCITY.asRaw(MetresPerSec::convert) * left,
                rotationController.calculate(hardware.drivetrain.getPosition().getHeading().getRadians()),
                hardware.drivetrain.getPosition().getHeading()
            )
        );
    }
}
