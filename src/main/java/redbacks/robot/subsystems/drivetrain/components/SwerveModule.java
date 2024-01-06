package redbacks.robot.subsystems.drivetrain.components;

import arachne4.lib.units.Metres;
import arachne4.lib.units.MetresPerSec;
import arachne4.lib.units.concats.Distance2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import redbacks.smart.hardware.SmartAngleSensor;
import redbacks.smart.hardware.SmartRotationalMotor;
import redbacks.smart.hardware.SmartWheeledMotor;

public class SwerveModule {
    private final SmartWheeledMotor driveMotor, driveFollower;
    private final SmartRotationalMotor steerMotor;
    private final SmartAngleSensor encoder;
    private final Distance2d offsetFromRobotCentre;

    public SwerveModule(SmartWheeledMotor driveMotor, SmartWheeledMotor driveFollower, SmartRotationalMotor steerMotor, SmartAngleSensor encoder, Distance2d offsetFromRobotCentre) {
        this.driveMotor = driveMotor;
        this.driveFollower = driveFollower;
        this.steerMotor = steerMotor;
        this.encoder = encoder;
        this.offsetFromRobotCentre = offsetFromRobotCentre;

        fixAngle();
    }

    public void fixAngle() {
        steerMotor.setSensorAngle(encoder.getAngle());
    }

    public void show(int moduleId) {
        SmartDashboard.putNumber("Swerve encoder " + moduleId, encoder.getAngle().getDegrees());
        SmartDashboard.putNumber("Swerve steer " + moduleId, steerMotor.getAngle().getDegrees());
    }

    public Distance2d getOffsetFromRobotCentre() {
        return offsetFromRobotCentre;
    }

    public void drive(SwerveModuleState targetState) {
        if(targetState.speedMetersPerSecond != 0) {
            SwerveModuleState optimisedState = optimize(targetState);

            steerMotor.setTargetAngle(optimisedState.angle);

            var targetVelocity = new MetresPerSec(optimisedState.speedMetersPerSecond);

            driveMotor.setTargetVelocity(targetVelocity);
            driveFollower.setTargetVelocity(targetVelocity);
        }
        else {
            driveMotor.stop();
            driveFollower.stop();
        }
    }

    private SwerveModuleState optimize(SwerveModuleState targetState) {
        double currentAngleDegrees = steerMotor.getAngle().getDegrees();
        double diff = targetState.angle.getDegrees() - currentAngleDegrees;

        double mod = diff >= 0 ? diff % 360 : 360 - (-diff % 360);

        if(mod <= 90) return new SwerveModuleState(targetState.speedMetersPerSecond, Rotation2d.fromDegrees(currentAngleDegrees + mod));
        else if(mod <= 270) return new SwerveModuleState(-targetState.speedMetersPerSecond, Rotation2d.fromDegrees(currentAngleDegrees + mod - 180));
        else return new SwerveModuleState(targetState.speedMetersPerSecond, Rotation2d.fromDegrees(currentAngleDegrees + mod - 360));
    }

    public SwerveModulePosition getModulePosition() {
        return new SwerveModulePosition(driveMotor.getPosition().asRaw(Metres::convert), steerMotor.getAngle());
    }

    public SwerveModuleState getModuleState() {
        return new SwerveModuleState(driveMotor.getVelocity().asRaw(MetresPerSec::convert), steerMotor.getAngle());
    }
}
