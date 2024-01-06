package redbacks.robot.subsystems.arm;

import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import arachne4.lib.units.Metres;
import arachne4.lib.units.RotationalVelocity;
import arachne4.lib.units.RotationsPerSec;
import arachne4.lib.units.concats.Distance2d;
import edu.wpi.first.math.geometry.Rotation2d;
import redbacks.robot.subsystems.arm.Arm.ArmPosition;
import redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationConstants;
import redbacks.smart.hardware.GearRatio;
import redbacks.smart.hardware.PIDConfig;
import redbacks.smart.hardware.SmartTalonFx;

public class ArmConstants {
    static final Rotation2d 
        LOWER_ARM_ENCODER_OFFSET = Rotation2d.fromDegrees(-161.1 - 38.4),
        UPPER_ARM_ENCODER_OFFSET = Rotation2d.fromDegrees(-334.5 + 90); //+90
        
    static final GearRatio 
        LOWER_ARM_GEAR_RATIO = new GearRatio(225, 1),
        UPPER_ARM_GEAR_RATIO = new GearRatio(144, 1);

    static final PIDConfig PID_CONFIG = new PIDConfig(
        0.4,
        0,
        0,
        SmartTalonFx.VELOCITY_MAX_OUTPUT * 0.4 / 7800
    );
    
    static final double MAX_PID_OUTPUT = 0.8;

    static final RotationalVelocity
        CRUISE_VELOCITY = new RotationsPerSec(0.8),
        MAX_ACCELERATION_PER_SEC = CRUISE_VELOCITY.multipliedBy(1.5);

    static final TalonFXConfiguration 
        LOWER_MOTOR_CONFIG = new TalonFXConfiguration() {{
            slot0.closedLoopPeakOutput = MAX_PID_OUTPUT;
            motionCruiseVelocity = SmartTalonFx.getVelocityForConfig(CRUISE_VELOCITY, LOWER_ARM_GEAR_RATIO);
            motionAcceleration = SmartTalonFx.getVelocityForConfig(MAX_ACCELERATION_PER_SEC, LOWER_ARM_GEAR_RATIO);
        }},
        UPPER_MOTOR_CONFIG = new TalonFXConfiguration() {{
            slot0.closedLoopPeakOutput = MAX_PID_OUTPUT;
            motionCruiseVelocity = SmartTalonFx.getVelocityForConfig(CRUISE_VELOCITY, UPPER_ARM_GEAR_RATIO);
            motionAcceleration = SmartTalonFx.getVelocityForConfig(MAX_ACCELERATION_PER_SEC, UPPER_ARM_GEAR_RATIO);
        }};

    static final double
        LOWER_ARM_MASS_KG = 0.2,
        UPPER_ARM_MASS_KG = 1.28, // Intake Weight 1.08 kg
        LOWER_ARM_LENGTH_METRES = 0.697,
        UPPER_ARM_LENGTH_METRES = 0.33, // measured from the pivot to the tip of the tubing
        LOWER_ARM_CENTRE_OF_MASS = 1/2 * LOWER_ARM_LENGTH_METRES,
        UPPER_ARM_CENTRE_OF_MASS = 3/4 * UPPER_ARM_LENGTH_METRES,
        GRAVITATIONAL_ACCELERATION_METRES_PER_SEC_SQUARED = -9.81;

    public static final double CONE_MASS_KG = 0.655;

    public static final double MAX_SPEED = 0.2;
    public static final double STALL_TORQUE = 4.69;

    public static final Distance2d ARM_BASE_OFFSET = new Distance2d(new Metres(0.045), new Metres(0.1737));

    public static final Rotation2d
        LOWER_ARM_LOWER_LIMIT = Rotation2d.fromDegrees(-30),
        LOWER_ARM_UPPER_LIMIT = Rotation2d.fromDegrees(32),
        UPPER_ARM_LOWER_LIMIT_OFFSET_FROM_LOWER_ARM = Rotation2d.fromDegrees(37), // RELATIVE TO LOWER ARM'S POSITION
        UPPER_ARM_UPPER_LIMIT_OFFSET_FROM_LOWER_ARM = Rotation2d.fromDegrees(176); // RELATIVE TO LOWER ARM'S POSITION

    public static final Rotation2d
        ALIGNMENT_EXTENDED_ANGLE = Rotation2d.fromDegrees(70),
        ALIGNMENT_STOW_WITH_CONE_ANGLE = Rotation2d.fromDegrees(34.7),
        ALIGNMENT_RETRACTED_ANGLE = Rotation2d.fromDegrees(0);

    public static final ArmPosition
        STOW_POSITION = new ArmPosition(Rotation2d.fromDegrees(8), Rotation2d.fromDegrees(180.7), ArmConstants.ALIGNMENT_RETRACTED_ANGLE);

    static final Rotation2d
        LOWER_ARM_TARGET_TOLERANCE = Rotation2d.fromDegrees(3),
        UPPER_ARM_TARGET_TOLERANCE = Rotation2d.fromDegrees(3);
    
    static final GearRatio ALIGNMENT_GEAR_RATIO = new GearRatio(60, 1);

    static final PIDConfig ALIGNMENT_PID_CONFIG = new PIDConfig(
        0.7, 
        0,
        0.2, 
        SmartTalonFx.VELOCITY_MAX_OUTPUT * 0.4 / 7800 
    );

    static final double ALIGNMENT_MAX_PID_OUTPUT = 1;

    static final RotationalVelocity
        ALIGNMENT_CRUISE_VELOCITY = new RotationsPerSec(1),
        ALIGNMENT_MAX_ACCELERATION_PER_SEC = CRUISE_VELOCITY.multipliedBy(2);

    static final TalonFXConfiguration 
        ALIGNMENT_MOTOR_CONFIG = new TalonFXConfiguration() {{
            slot0.closedLoopPeakOutput = ALIGNMENT_MAX_PID_OUTPUT;
            motionCruiseVelocity = SmartTalonFx.getVelocityForConfig(ALIGNMENT_CRUISE_VELOCITY, ALIGNMENT_GEAR_RATIO);
            motionAcceleration = SmartTalonFx.getVelocityForConfig(ALIGNMENT_MAX_ACCELERATION_PER_SEC, ALIGNMENT_GEAR_RATIO);
        }};

    public static void main(String[] args) {
        var pos = ArmAndCubeCoordinationConstants.CONE_SCORE_MID.armPosition;

        System.out.println(pos.lowerArmAngle);
        System.out.println(pos.upperArmAngle);

        System.out.println(Arm.calculateTargetXYFromArmPosition(pos.lowerArmAngle, pos.upperArmAngle));
    }
}