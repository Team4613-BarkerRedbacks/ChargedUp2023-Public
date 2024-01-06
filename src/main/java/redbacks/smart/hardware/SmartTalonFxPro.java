package redbacks.smart.hardware;

import com.ctre.phoenixpro.controls.DutyCycleOut;
import com.ctre.phoenixpro.controls.MotionMagicDutyCycle;
import com.ctre.phoenixpro.controls.PositionDutyCycle;
import com.ctre.phoenixpro.controls.VelocityDutyCycle;
import com.ctre.phoenixpro.signals.NeutralModeValue;

import arachne4.lib.units.Distance;
import arachne4.lib.units.Metres;
import arachne4.lib.units.MetresPerSec;
import arachne4.lib.units.RotationalVelocity;
import arachne4.lib.units.RotationsPerSec;
import arachne4.lib.units.Velocity;
import edu.wpi.first.math.geometry.Rotation2d;

public class SmartTalonFxPro {
    private final TalonFxProMotorGroup motors;
    private final boolean useFieldOrientedControl;

    public SmartTalonFxPro(TalonFxProMotorGroup motors, boolean useFieldOrientedControl) {
        this.motors = motors;
        this.useFieldOrientedControl = useFieldOrientedControl;
    }

    public void setPercentageOutput(double power) {
        motors.setPercentageOutput(new DutyCycleOut(power, useFieldOrientedControl, false));
    }

    public SmartRotationalMotor createRotationalMotor(GearRatio gearRatio, PIDConfig pidConfig, boolean useMotionMagicForPositionControl) {
        motors.configPid(pidConfig);

        return new SmartRotationalMotor() {
            @Override
            public void setPercentageOutput(double power) {
                SmartTalonFxPro.this.setPercentageOutput(power);
            }

            @Override
            public void setToCoast() {
                motors.setNeutralMode(NeutralModeValue.Coast);
            }

            @Override
            public void setToBrake() {
                motors.setNeutralMode(NeutralModeValue.Brake);
            }

            @Override
            public Rotation2d getAngle() {
                return fromRevolutions(motors.getSelectedSensorPosition(), gearRatio);
            }

            @Override
            public void setSensorAngle(Rotation2d angle) {
                motors.setSelectedSensorPosition(toRevolutions(angle, gearRatio));
            }

            @Override
            public void setTargetAngle(Rotation2d angle) {
                setTargetAngle(angle, 0);
            }

            @Override
            public void setTargetAngle(Rotation2d angle, double arbitraryFeedForwardPercent) {
                double target = toRevolutions(angle, gearRatio);

                if(useMotionMagicForPositionControl) {
                    motors.setTargetRotation(new MotionMagicDutyCycle(
                        target,
                        useFieldOrientedControl,
                        arbitraryFeedForwardPercent,
                        0,
                        false
                    ));
                }
                else {
                    motors.setTargetRotation(new PositionDutyCycle(
                        target,
                        useFieldOrientedControl,
                        arbitraryFeedForwardPercent,
                        0,
                        false
                    ));
                }
            }
        };
    }

    public SmartWheeledMotor createEmpiricallyDistancedMotor(double revolutionsPerMetre, PIDConfig pidConfig, boolean useMotionMagicForPositionControl) {
        motors.configPid(pidConfig);

        return new SmartWheeledMotor() {
            @Override
            public void setPercentageOutput(double power) {
                SmartTalonFxPro.this.setPercentageOutput(power);
            }

            @Override
            public void setToCoast() {
                motors.setNeutralMode(NeutralModeValue.Coast);
            }

            @Override
            public void setToBrake() {
                motors.setNeutralMode(NeutralModeValue.Brake);
            }

            @Override
            public Distance getPosition() {
                return new Metres(motors.getSelectedSensorPosition() / revolutionsPerMetre);
            }

            @Override
            public void setCurrentPosition(Distance position) {
                motors.setSelectedSensorPosition(position.asRaw(Metres::convert) * revolutionsPerMetre);
            }

            @Override
            public void setTargetPosition(Distance position) {
                setTargetPosition(position, 0);
            }

            @Override
            public void setTargetPosition(Distance position, double arbitraryFeedForwardPercent) {
                double target = position.asRaw(Metres::convert) * revolutionsPerMetre;

                if(useMotionMagicForPositionControl) {
                    motors.setTargetRotation(new MotionMagicDutyCycle(
                        target,
                        useFieldOrientedControl,
                        arbitraryFeedForwardPercent,
                        0,
                        false
                    ));
                }
                else {
                    motors.setTargetRotation(new PositionDutyCycle(
                        target,
                        useFieldOrientedControl,
                        arbitraryFeedForwardPercent,
                        0,
                        false
                    ));
                }
            }

            @Override
            public Velocity getVelocity() {
                return new MetresPerSec(motors.getSelectedSensorVelocity() / revolutionsPerMetre);
            }

            @Override
            public void setTargetVelocity(Velocity velocity) {
                motors.setTargetVelocityRps(new VelocityDutyCycle(
                    velocity.asRaw(MetresPerSec::convert) * revolutionsPerMetre,
                    useFieldOrientedControl,
                    0,
                    0,
                    false
                ));
            }

            @Override
            public void stop() {
                motors.setPercentageOutput(new DutyCycleOut(0));
            }
        };
    }

    public SmartWheeledMotor createWheeledMotor(Distance wheelDiameter, GearRatio gearRatio, PIDConfig pidConfig, boolean useMotionMagicForPositionControl) {
        var wheelCircumference = wheelDiameter.multipliedBy(Math.PI);
        var revolutionsPerMetre = gearRatio.getInputCountFromOutput(1) / wheelCircumference.asRaw(Metres::convert);

        return createEmpiricallyDistancedMotor(revolutionsPerMetre, pidConfig, useMotionMagicForPositionControl);
    }

    public static double getVelocityForConfig(RotationalVelocity velocity, GearRatio motorGearRatio) {
        return toRevolutions(Rotation2d.fromRotations(velocity.asRaw(RotationsPerSec::convert)), motorGearRatio);
    }

    private static double toRevolutions(Rotation2d angle, GearRatio motorGearRatio) {
        return motorGearRatio.getInputCountFromOutput(angle.getRotations());
    }

    private static Rotation2d fromRevolutions(double revolutions, GearRatio motorGearRatio) {
        return Rotation2d.fromRotations(motorGearRatio.getOutputCountFromInput(revolutions));
    }
}
