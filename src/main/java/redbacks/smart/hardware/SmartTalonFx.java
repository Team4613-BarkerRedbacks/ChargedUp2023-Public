package redbacks.smart.hardware;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;

import arachne4.lib.units.Distance;
import arachne4.lib.units.Metres;
import arachne4.lib.units.MetresPerSec;
import arachne4.lib.units.RadiansPerSec;
import arachne4.lib.units.RotationalVelocity;
import arachne4.lib.units.Velocity;
import edu.wpi.first.math.geometry.Rotation2d;

public class SmartTalonFx {
    private static final int TICKS_PER_REVOLUTION = 2048;
    public static final int VELOCITY_MAX_OUTPUT = 1023;

    private final TalonFxMotorGroup motors;

    public SmartTalonFx(TalonFxMotorGroup motors) {
        this.motors = motors;
    }

    public void setPercentageOutput(double power) {
        motors.set(TalonFXControlMode.PercentOutput, power);
    }

    public SmartRotationalMotor createRotationalMotor(GearRatio gearRatio, PIDConfig pidConfig, boolean useMotionMagicForPositionControl) {
        motors.configPid(pidConfig);

        return new SmartRotationalMotor() {
            @Override
            public void setPercentageOutput(double power) {
                motors.set(TalonFXControlMode.PercentOutput, power);
            }

            @Override
            public void setToCoast() {
                motors.setNeutralMode(NeutralMode.Coast);
            }

            @Override
            public void setToBrake() {
                motors.setNeutralMode(NeutralMode.Brake);
            }

            @Override
            public Rotation2d getAngle() {
                return fromTicks(motors.getSelectedSensorPosition(), gearRatio);
            }

            @Override
            public void setSensorAngle(Rotation2d angle) {
                motors.setSelectedSensorPosition(toTicks(angle, gearRatio));
            }

            @Override
            public void setTargetAngle(Rotation2d angle) {
                setTargetAngle(angle, 0);
            }

            @Override
            public void setTargetAngle(Rotation2d angle, double arbitraryFeedForwardPercent) {
                motors.set(
                    useMotionMagicForPositionControl ? TalonFXControlMode.MotionMagic : TalonFXControlMode.Position,
                    toTicks(angle, gearRatio),
                    DemandType.ArbitraryFeedForward,
                    arbitraryFeedForwardPercent
                );
            }
        };
    }

    public SmartWheeledMotor createEmpiricallyDistancedMotor(double ticksPerMetre, PIDConfig pidConfig, boolean useMotionMagicForPositionControl) {
        motors.configPid(pidConfig);

        return new SmartWheeledMotor() {
            @Override
            public void setPercentageOutput(double power) {
                motors.set(TalonFXControlMode.PercentOutput, power);
            }

            @Override
            public void setToCoast() {
                motors.setNeutralMode(NeutralMode.Coast);
            }

            @Override
            public void setToBrake() {
                motors.setNeutralMode(NeutralMode.Brake);
            }

            @Override
            public Distance getPosition() {
                return new Metres(motors.getSelectedSensorPosition() / ticksPerMetre);
            }

            @Override
            public void setCurrentPosition(Distance position) {
                motors.setSelectedSensorPosition(position.asRaw(Metres::convert) * ticksPerMetre);
            }

            @Override
            public void setTargetPosition(Distance position) {
                setTargetPosition(position, 0);
            }

            @Override
            public void setTargetPosition(Distance position, double arbitraryFeedForwardPercent) {
                motors.set(
                    useMotionMagicForPositionControl ? TalonFXControlMode.MotionMagic : TalonFXControlMode.Position,
                    position.asRaw(Metres::convert) * ticksPerMetre,
                    DemandType.ArbitraryFeedForward,
                    arbitraryFeedForwardPercent
                );
            }

            @Override
            public Velocity getVelocity() {
                return new MetresPerSec(motors.getSelectedSensorVelocity() * 10 / ticksPerMetre);
            }

            @Override
            public void setTargetVelocity(Velocity velocity) {
                motors.set(TalonFXControlMode.Velocity, velocity.asRaw(MetresPerSec::convert) / 10 * ticksPerMetre);
            }

            @Override
            public void stop() {
                motors.set(TalonFXControlMode.PercentOutput, 0);
            }
        };
    }

    public SmartWheeledMotor createWheeledMotor(Distance wheelDiameter, GearRatio gearRatio, PIDConfig pidConfig, boolean useMotionMagicForPositionControl) {
        var wheelCircumference = wheelDiameter.multipliedBy(Math.PI);
        var ticksPerMetre = TICKS_PER_REVOLUTION * gearRatio.getInputCountFromOutput(1) / wheelCircumference.asRaw(Metres::convert);

        return createEmpiricallyDistancedMotor(ticksPerMetre, pidConfig, useMotionMagicForPositionControl);
    }

    public static double getVelocityForConfig(RotationalVelocity velocity, GearRatio motorGearRatio) {
        return toTicks(Rotation2d.fromRadians(velocity.asRaw(RadiansPerSec::convert) / 10), motorGearRatio);
    }

    private static double toTicks(Rotation2d angle, GearRatio motorGearRatio) {
        double revolutions = motorGearRatio.getInputCountFromOutput(angle.getDegrees() / 360);
        return revolutions * TICKS_PER_REVOLUTION;
    }

    private static Rotation2d fromTicks(double ticks, GearRatio motorGearRatio) {
        double revolutions = motorGearRatio.getOutputCountFromInput(ticks / TICKS_PER_REVOLUTION);
        return Rotation2d.fromDegrees(revolutions * 360);
    }
}
