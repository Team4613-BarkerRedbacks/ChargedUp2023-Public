package redbacks.robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.Pigeon2;
import com.ctre.phoenixpro.signals.NeutralModeValue;

import edu.wpi.first.math.geometry.Rotation2d;

import arachne4.lib.scheduler.SchedulerProvider;
import arachne4.lib.scheduler.SchedulerProviderBase;
import arachne4.lib.units.concats.Distance2d;
import redbacks.robot.Constants;
import redbacks.robot.subsystems.drivetrain.components.SwerveDrivetrain;
import redbacks.robot.subsystems.drivetrain.components.SwerveModule;
import redbacks.smart.hardware.SmartAngleSensor;
import redbacks.smart.hardware.SmartCancoder;
import redbacks.smart.hardware.SmartRotationalMotor;
import redbacks.smart.hardware.SmartWheeledMotor;
import redbacks.smart.hardware.builders.SmartTalonFxBuilder;
import redbacks.smart.hardware.builders.SmartTalonFxProBuilder;

import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;

public class DrivetrainHardware extends SchedulerProviderBase {
    public DrivetrainHardware(SchedulerProvider schedulerProvider) {
        super(schedulerProvider.getScheduler());
    }

    public final Pigeon2 pigeon = new Pigeon2(0);

    final SmartAngleSensor
        yawSensor = () -> Rotation2d.fromDegrees(pigeon.getYaw()),
        pitchSensor = () -> Rotation2d.fromDegrees(pigeon.getPitch() + PITCH_OFFSET),
        rollSensor = () -> Rotation2d.fromDegrees(pigeon.getRoll() + ROLL_OFFSET),
        frontLeftEncoder = new SmartCancoder(new CANCoder(0, Constants.CANIVORE_BUS_NAME), FRONT_LEFT_OFFSET).createAngleSensor(),
        backLeftEncoder = new SmartCancoder(new CANCoder(1, Constants.CANIVORE_BUS_NAME), BACK_LEFT_OFFSET).createAngleSensor(),
        backRightEncoder = new SmartCancoder(new CANCoder(2, Constants.CANIVORE_BUS_NAME), BACK_RIGHT_OFFSET).createAngleSensor(),
        frontRightEncoder = new SmartCancoder(new CANCoder(3, Constants.CANIVORE_BUS_NAME), FRONT_RIGHT_OFFSET).createAngleSensor();

    final SmartWheeledMotor
        frontLeftDriveMotor = createDriveMotor(0),
        backLeftDriveMotor = createDriveMotor(1),
        backRightDriveMotor = createDriveMotor(2),
        frontRightDriveMotor = createDriveMotor(3),

        frontLeftDriveFollower = createSlaveDriveMotor(20),
        backLeftDriveFollower = createSlaveDriveMotor(21),
        backRightDriveFollower = createSlaveDriveMotor(22),
        frontRightDriveFollower = createSlaveDriveMotor(23);

    private static SmartWheeledMotor createDriveMotor(int id) {
        return SmartTalonFxProBuilder
            .fromId(id, Constants.CANIVORE_BUS_NAME, NeutralModeValue.Brake, false)
            .withConfig(DRIVE_CONFIG)
            .build()
            .createWheeledMotor(WHEEL_DIAMETER, DRIVE_GEAR_RATIO, DRIVE_WHEEL_PID_CONFIG, false);
    }

    private static SmartWheeledMotor createSlaveDriveMotor(int id) {
        return SmartTalonFxProBuilder
            .fromId(id, Constants.CANIVORE_BUS_NAME, NeutralModeValue.Coast, false)
            .withConfig(DRIVE_CONFIG)
            .build()
            .createWheeledMotor(WHEEL_DIAMETER, DRIVE_GEAR_RATIO, DRIVE_WHEEL_PID_CONFIG, false);
    }

    final SmartRotationalMotor
        frontLeftSteerMotor = createSteerMotor(10),
        backLeftSteerMotor = createSteerMotor(11),
        backRightSteerMotor = createSteerMotor(12),
        frontRightSteerMotor = createSteerMotor(13);

    private static SmartRotationalMotor createSteerMotor(int id) {
        return SmartTalonFxBuilder
            .fromId(id, Constants.CANIVORE_BUS_NAME, NeutralMode.Brake, true)
            .build()
            .createRotationalMotor(STEER_GEAR_RATIO, STEER_WHEEL_PID_CONFIG, false);
    }

    final SwerveDrivetrain drivetrain = new SwerveDrivetrain(
        getScheduler(),
        POSE_ESTIMATOR_CONFIG,
        yawSensor,
        new SwerveModule(frontLeftDriveMotor, frontLeftDriveFollower, frontLeftSteerMotor, frontLeftEncoder, new Distance2d(MODULE_DISTANCE_FROM_CENTRE_X, MODULE_DISTANCE_FROM_CENTRE_Y)),
        new SwerveModule(backLeftDriveMotor, backLeftDriveFollower, backLeftSteerMotor, backLeftEncoder, new Distance2d(MODULE_DISTANCE_FROM_CENTRE_X.negative(), MODULE_DISTANCE_FROM_CENTRE_Y)),
        new SwerveModule(backRightDriveMotor, backRightDriveFollower, backRightSteerMotor, backRightEncoder, new Distance2d(MODULE_DISTANCE_FROM_CENTRE_X.negative(), MODULE_DISTANCE_FROM_CENTRE_Y.negative())),
        new SwerveModule(frontRightDriveMotor, frontRightDriveFollower, frontRightSteerMotor, frontRightEncoder, new Distance2d(MODULE_DISTANCE_FROM_CENTRE_X, MODULE_DISTANCE_FROM_CENTRE_Y.negative()))
    );
}
