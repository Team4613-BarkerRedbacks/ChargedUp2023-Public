package redbacks.robot.subsystems.arm;

import static redbacks.robot.subsystems.arm.ArmConstants.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import arachne4.lib.scheduler.SchedulerProvider;
import arachne4.lib.scheduler.SchedulerProviderBase;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.AnalogInput;
import redbacks.smart.hardware.SmartAnalogPotentiometer;
import redbacks.smart.hardware.SmartAngleSensor;
import redbacks.smart.hardware.SmartRotationalMotor;
import redbacks.smart.hardware.SmartAnalogPotentiometer.AngleMode;
import redbacks.smart.hardware.builders.SmartTalonFxBuilder;

public class ArmHardware extends SchedulerProviderBase {
    public ArmHardware(SchedulerProvider schedulerProvider) {
        super(schedulerProvider.getScheduler());
    }

    //FIXME make correct - check id, inversion, constants
    final SmartRotationalMotor mandibles = SmartTalonFxBuilder
        .fromId(9, NeutralMode.Brake, false)
        .withConfig(ALIGNMENT_MOTOR_CONFIG)
        .build()
        .createRotationalMotor(ALIGNMENT_GEAR_RATIO, ALIGNMENT_PID_CONFIG, true);

    final SmartRotationalMotor
        lowerArmMotor = SmartTalonFxBuilder
            .fromId(4, NeutralMode.Brake, true)
            .withConfig(LOWER_MOTOR_CONFIG)
            .build()
            .createRotationalMotor(LOWER_ARM_GEAR_RATIO, PID_CONFIG, true),

        upperArmMotor = SmartTalonFxBuilder
            .fromId(5, NeutralMode.Brake, true)
            .withConfig(UPPER_MOTOR_CONFIG)
            .build()
            .createRotationalMotor(UPPER_ARM_GEAR_RATIO, PID_CONFIG, true);

    private final SmartAngleSensor
        _lowerArmEncoder = new SmartAnalogPotentiometer(new AnalogInput(0), AngleMode.FROM_MINUS_180_TO_180, LOWER_ARM_ENCODER_OFFSET, false),
        _upperArmEncoder = new SmartAnalogPotentiometer(new AnalogInput(1), AngleMode.FROM_0_TO_360, UPPER_ARM_ENCODER_OFFSET, false);

    final SmartAngleSensor
        lowerArmEncoder = () -> _lowerArmEncoder.getAngle(),
        upperArmEncoder = () -> Rotation2d.fromDegrees(MathUtil.inputModulus(
            _upperArmEncoder.getAngle().getDegrees()
            + _lowerArmEncoder.getAngle().getDegrees(),
            0, 360
        ));
}