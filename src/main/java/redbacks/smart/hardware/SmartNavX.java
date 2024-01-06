package redbacks.smart.hardware;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;

public class SmartNavX {
    private final AHRS navx;

    public SmartNavX(AHRS navx) {
        this.navx = navx;
    }

    public SmartAngleSensor asCartesianYawSensor() {
        return new SmartAngleSensor() {
            @Override
            public Rotation2d getAngle() {
                return Rotation2d.fromDegrees(-navx.getYaw());
            }
        };
    }

    public SmartAngleSensor asPitchSensor() {
        return new SmartAngleSensor() {
            @Override
            public Rotation2d getAngle() {
                return Rotation2d.fromDegrees(navx.getPitch());
            }
        };
    }
}
