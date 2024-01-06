package redbacks.robot.subsystems.cubeIntake;

import static redbacks.robot.subsystems.cubeIntake.CubeIntakeConstants.*;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DigitalInput;
import redbacks.smart.hardware.SmartRotationalMotor;
import redbacks.smart.hardware.SmartTalonFx;
import redbacks.smart.hardware.builders.SmartTalonFxBuilder;

public class CubeIntakeHardware {
    final SmartTalonFx intake = SmartTalonFxBuilder
        .fromId(6, NeutralMode.Coast, false)
        .build();

    final SmartRotationalMotor pivot = SmartTalonFxBuilder
        .fromId(7, NeutralMode.Brake, true)
        .withConfig(PIVOT_MOTOR_CONFIG)
        .build()
        .createRotationalMotor(PIVOT_GEAR_RATIO, PID_CONFIG, true);

    private final DigitalInput _hasCube = new DigitalInput(0);
    final BooleanSupplier hasCube = () -> !_hasCube.get();
}