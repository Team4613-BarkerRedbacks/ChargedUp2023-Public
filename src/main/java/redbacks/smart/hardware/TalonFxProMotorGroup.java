package redbacks.smart.hardware;

import java.util.Collection;
import java.util.List;

import com.ctre.phoenixpro.configs.MotorOutputConfigs;
import com.ctre.phoenixpro.configs.Slot0Configs;
import com.ctre.phoenixpro.controls.DutyCycleOut;
import com.ctre.phoenixpro.controls.MotionMagicDutyCycle;
import com.ctre.phoenixpro.controls.PositionDutyCycle;
import com.ctre.phoenixpro.controls.VelocityDutyCycle;
import com.ctre.phoenixpro.hardware.TalonFX;
import com.ctre.phoenixpro.signals.NeutralModeValue;

public class TalonFxProMotorGroup {
    private final TalonFX primaryMotor;
    private final List<TalonFX> motors;

    public TalonFxProMotorGroup(TalonFX primaryMotor, Collection<TalonFX> motors) {
        this.primaryMotor = primaryMotor;
        this.motors = List.copyOf(motors);
    }

    public void setPercentageOutput(DutyCycleOut output) {
        primaryMotor.setControl(output);
    }

    public void setTargetVelocityRps(VelocityDutyCycle output) {
        primaryMotor.setControl(output);
    }

    public void setTargetRotation(MotionMagicDutyCycle output) {
        primaryMotor.setControl(output);
    }

    public void setTargetRotation(PositionDutyCycle output) {
        primaryMotor.setControl(output);
    }

    public double getSelectedSensorPosition() {
        return primaryMotor.getRotorPosition().getValue();
    }

    public void setSelectedSensorPosition(double position) {
        primaryMotor.setRotorPosition(position);
    }

    public double getSelectedSensorVelocity() {
        return primaryMotor.getRotorVelocity().getValue();
    }

    public void setNeutralMode(NeutralModeValue neutralMode) {
        for(var motor : motors) {
            var config = new MotorOutputConfigs();

            motor.getConfigurator().refresh(config);
            config.NeutralMode = neutralMode;

            motor.getConfigurator().apply(config);
        }
    }

    public void configPid(PIDConfig config) {
        var pidConfig = new Slot0Configs() {{
            kP = config.kP;
            kI = config.kI;
            kD = config.kD;
            kV = config.kF;
        }};

        primaryMotor.getConfigurator().apply(pidConfig);
    }
}
