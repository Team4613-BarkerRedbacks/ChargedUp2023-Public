package redbacks.robot.subsystems.drivetrain;

import com.ctre.phoenixpro.configs.CurrentLimitsConfigs;
import com.ctre.phoenixpro.configs.TalonFXConfiguration;

import arachne4.lib.units.Distance;
import arachne4.lib.units.Metres;
import arachne4.lib.units.MetresPerSec;
import arachne4.lib.units.Millimetres;
import arachne4.lib.units.RotationalVelocity;
import arachne4.lib.units.RotationsPerSec;
import arachne4.lib.units.Velocity;
import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import redbacks.robot.Constants;
import redbacks.robot.subsystems.drivetrain.components.SwerveDrivePoseEstimatorConfig;
import redbacks.smart.hardware.GearRatio;
import redbacks.smart.hardware.PIDConfig;

public class DrivetrainConstants {
    // Controls
	static final double
        JOYSTICK_DEADZONE_RADIUS = 0.05,
        JOYSTICK_EXPONENT_FOR_LINEAR = 2.5,
        JOYSTICK_EXPONENT_FOR_ROTATION = 3.5;

    // Modules
    static final Distance
        DISTANCE_BETWEEN_MODULE_X = new Metres(0.42665),
        DISTANCE_BETWEEN_MODULE_Y = new Metres(0.42665),
        MODULE_DISTANCE_FROM_CENTRE_X = DISTANCE_BETWEEN_MODULE_X.half(),
        MODULE_DISTANCE_FROM_CENTRE_Y = DISTANCE_BETWEEN_MODULE_Y.half();

    static final Distance WHEEL_DIAMETER = new Millimetres(100);

    static final GearRatio
        DRIVE_GEAR_RATIO = new GearRatio(5.25, 1),
        STEER_GEAR_RATIO = new GearRatio(150, 7);

    static final PIDConfig
        DRIVE_WHEEL_PID_CONFIG = new PIDConfig(2.4e-5 * 0.2, 0, 3.5e-4 * 0.0002, 0.0467 * 0.2),
        STEER_WHEEL_PID_CONFIG = new PIDConfig(0.25, 0, 0);

    static final Rotation2d
    FRONT_LEFT_OFFSET = Rotation2d.fromDegrees(-47.73),
    BACK_LEFT_OFFSET = Rotation2d.fromDegrees(33.06),
    BACK_RIGHT_OFFSET = Rotation2d.fromDegrees(167.68),
    FRONT_RIGHT_OFFSET = Rotation2d.fromDegrees(-65.54);

    // Drivetrain
    static final Velocity MAX_LINEAR_VELOCITY = new MetresPerSec(8);
    static final RotationalVelocity MAX_ROTATIONAL_VELOCITY = new RotationsPerSec(1.42);

    // Dimensions
    public static final double ROBOT_WIDTH_METRES = 0.695 + 0.035; // Including 17cm bumpers
    public static final double ROBOT_LENGTH_METRES = 0.695 + 0.035; // Including 17cm bumpers

    public static final double
        ROBOT_OFFSET_X_FROM_EDGE = ROBOT_LENGTH_METRES / 2,
        ROBOT_OFFSET_Y_FROM_EDGE = ROBOT_WIDTH_METRES / 2;

    static final SwerveDrivePoseEstimatorConfig POSE_ESTIMATOR_CONFIG = new SwerveDrivePoseEstimatorConfig(
        new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0.1, 0.1, 0.1),
        new MatBuilder<>(Nat.N1(), Nat.N1()).fill(0.01),
        new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0.02, 0.02, 0.01),
        Constants.LOOP_PERIOD
    );

    static final double
        LINEAR_KP = 0.7,
        LINEAR_KI = 0.2,
        LINEAR_KD = 0.4,
        ROTATIONAL_KP = 10, // previously 9
        ROTATIONAL_KI = 0, //previously 0.7
        ROTATIONAL_KD = 0;//previously 0.5

    static final double
        AUTO_MAX_ROTATIONAL_VELOCITY_RADIANS_PER_SEC = 5,
        MAX_ACCELERATION_METRES_PER_SECOND_SQUARED = MAX_LINEAR_VELOCITY.asRaw(Metres::convert) / 1,
        AUTO_MAX_ROTATIONAL_ACCELERATION_RADIANS_PER_SEC_SQUARED = AUTO_MAX_ROTATIONAL_VELOCITY_RADIANS_PER_SEC / 1.5;
    
    static final double
        POSITION_TOLERANCE_METRES = 0.2,
        ROTATION_TOLERANCE_RADIANS = Math.toRadians(3),
        STRAFE_TOLERANCE_DEGREES = 3,
        STRAFE_TOLERANCE_METRES = 0.1;
    static final TrapezoidProfile.Constraints
        LINEAR_MOTION_CONSTRAINTS = new TrapezoidProfile.Constraints(MAX_LINEAR_VELOCITY.asRaw(Metres::convert), MAX_ACCELERATION_METRES_PER_SECOND_SQUARED),
        AUTO_ROTATION_MOTION_CONSTRAINTS = new TrapezoidProfile.Constraints(AUTO_MAX_ROTATIONAL_VELOCITY_RADIANS_PER_SEC, AUTO_MAX_ROTATIONAL_ACCELERATION_RADIANS_PER_SEC_SQUARED);

    static final double AUTO_SCORE_CONE_YAW_TOLERANCE_DEGREES = 3;

    static final double ROTATION_LERP_LEAD_PERCENTAGE = 0.2;

    // Pigeon offsets
    static final double
        ROLL_OFFSET = 2.64,
        PITCH_OFFSET = -0.97;

    static final TalonFXConfiguration DRIVE_CONFIG = new TalonFXConfiguration() {{
        CurrentLimits = new CurrentLimitsConfigs() {{
            StatorCurrentLimitEnable = true;
            StatorCurrentLimit = 90; // Decrease if browning out, increase for more acceleration if not browning out
        }};
    }};
}
