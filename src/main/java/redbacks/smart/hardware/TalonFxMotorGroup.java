package redbacks.smart.hardware;

import java.util.Collection;
import java.util.List;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class TalonFxMotorGroup {
    private final TalonFX primaryMotor;
    private final List<TalonFX> motors;

    public TalonFxMotorGroup(TalonFX primaryMotor, Collection<TalonFX> motors) {
        this.primaryMotor = primaryMotor;
        this.motors = List.copyOf(motors);
    }

    public void set(TalonFXControlMode mode, double value) {
        primaryMotor.set(mode, value);
    }

    public void set(TalonFXControlMode mode, double value, DemandType complementaryControlType, double complementaryValue) {
        primaryMotor.set(mode, value, complementaryControlType, complementaryValue);
    }

    public double getSelectedSensorPosition() {
        return primaryMotor.getSelectedSensorPosition();
    }

    public void setSelectedSensorPosition(double position) {
        primaryMotor.setSelectedSensorPosition(position);
    }

    public double getSelectedSensorVelocity() {
        return primaryMotor.getSelectedSensorVelocity();
    }

    public void setNeutralMode(NeutralMode neutralMode) {
        for(var motor : motors) motor.setNeutralMode(neutralMode);
    }

    public void configPid(PIDConfig config) {
        primaryMotor.config_kP(0, config.kP);
        primaryMotor.config_kI(0, config.kI);
        primaryMotor.config_kD(0, config.kD);
        primaryMotor.config_kF(0, config.kF);
    }
}
