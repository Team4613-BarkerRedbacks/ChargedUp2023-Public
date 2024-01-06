package redbacks.smart.hardware;

import edu.wpi.first.math.geometry.Rotation2d;

public interface SmartRotationalMotor extends SmartMotor {
    Rotation2d getAngle();

    void setSensorAngle(Rotation2d angle);
    void setTargetAngle(Rotation2d angle);
    void setTargetAngle(Rotation2d angle, double arbitraryFeedForwardPercent);
}
