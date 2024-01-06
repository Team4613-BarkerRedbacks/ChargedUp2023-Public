package redbacks.robot.subsystems.cubeIntake;

import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import arachne4.lib.logging.ArachneLogger;
import arachne4.lib.units.RotationalVelocity;
import arachne4.lib.units.RotationsPerSec;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import redbacks.smart.hardware.GearRatio;
import redbacks.smart.hardware.PIDConfig;
import redbacks.smart.hardware.SmartTalonFx;

public class CubeIntakeConstants {
    /*
     * Positions and powers
     */
    static final double
        INTAKE_POWER = 0.5,
        SLOW_INTAKE_POWER = 0.3,
        HOLD_POWER = 0.07,
        FAST_OUTTAKE = -0.7;

    public static final double DEFAULT_OUTTAKE_POWER = -1;

    static final Rotation2d
        MIN_TARGET_ANGLE = Rotation2d.fromDegrees(10),
        MAX_TARGET_ANGLE = Rotation2d.fromDegrees(113.9),
        TARGET_ANGLE_TOLERANCE = Rotation2d.fromDegrees(2);

    public static final Rotation2d STOW_ANGLE = MIN_TARGET_ANGLE;

    public static final IntakePosition
        STARTING_POS = new IntakePosition(
            Rotation2d.fromDegrees(10),
            DEFAULT_OUTTAKE_POWER,
            DEFAULT_OUTTAKE_POWER,
            false
        ),
        STOW_POS = new IntakePosition(
            MIN_TARGET_ANGLE,
            DEFAULT_OUTTAKE_POWER
        );

    /*
     * Motor configuration
     */
    static final GearRatio PIVOT_GEAR_RATIO = new GearRatio(60, 1);

    static final PIDConfig PID_CONFIG = new PIDConfig(
        0.7, 
        0,
        0.2, 
        SmartTalonFx.VELOCITY_MAX_OUTPUT * 0.4 / 7800 
    );

    static final double MAX_PID_OUTPUT = 1;

    static final RotationalVelocity
        CRUISE_VELOCITY = new RotationsPerSec(1),
        MAX_ACCELERATION_PER_SEC = CRUISE_VELOCITY.multipliedBy(2);

    static final TalonFXConfiguration 
        PIVOT_MOTOR_CONFIG = new TalonFXConfiguration() {{
            slot0.closedLoopPeakOutput = MAX_PID_OUTPUT;
            motionCruiseVelocity = SmartTalonFx.getVelocityForConfig(CRUISE_VELOCITY, PIVOT_GEAR_RATIO);
            motionAcceleration = SmartTalonFx.getVelocityForConfig(MAX_ACCELERATION_PER_SEC, PIVOT_GEAR_RATIO);
        }};

    /*
     * Helper classes
     */
    public static final class IntakePosition {
        final Rotation2d angle;
        final double regularOuttakePower;
        final double fastOuttakePower;

        private IntakePosition(Rotation2d angle, double outtakePower, double secondOuttakePower, boolean clampIfOutsideBounds) {
            if(angle.getRadians() < MIN_TARGET_ANGLE.getRadians() || angle.getRadians() > MAX_TARGET_ANGLE.getRadians()) {
                if(clampIfOutsideBounds) {
                    ArachneLogger.getInstance().warn("Intake position of " + angle.getDegrees() + " degrees is outside of limits and will be clamped");
                    angle = Rotation2d.fromRadians(MathUtil.clamp(angle.getRadians(), MIN_TARGET_ANGLE.getRadians(), MAX_TARGET_ANGLE.getRadians()));
                }
                else {
                    ArachneLogger.getInstance().info("Intake position of " + angle.getDegrees() + " degrees is outside of settable limits, but has been flagged to not be clamped");
                }
            }

            this.angle = angle;
            this.regularOuttakePower = outtakePower;
            this.fastOuttakePower = secondOuttakePower;
        }
        public IntakePosition(Rotation2d angle, double regularOuttakePower, double fastOuttakePower) {
            this(angle, regularOuttakePower, fastOuttakePower, true);
        }

        public IntakePosition(Rotation2d angle, double outtakePower) {
            this(angle, outtakePower, outtakePower, true);
        }
    }
}
