package redbacks.robot.subsystems.coneIntake;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class ConeIntakeHardware {
    final MotorController coneIntakeMotor = new WPI_VictorSPX(8) {{
        configFactoryDefault();
        setNeutralMode(NeutralMode.Brake);
        setInverted(true);
    }};

    private final DigitalInput _hasCone = new DigitalInput(1);
    final BooleanSupplier hasCone = () -> _hasCone.get();
}