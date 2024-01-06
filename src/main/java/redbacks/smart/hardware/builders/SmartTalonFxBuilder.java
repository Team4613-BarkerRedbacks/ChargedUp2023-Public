package redbacks.smart.hardware.builders;

import java.util.LinkedList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import edu.wpi.first.math.Pair;
import redbacks.smart.hardware.SmartTalonFx;
import redbacks.smart.hardware.TalonFxMotorGroup;

public class SmartTalonFxBuilder {
    private final TalonFX primaryMotor;
    private final NeutralMode neutralMode;

    private final List<Pair<TalonFX, TalonFXInvertType>> followers = new LinkedList<>();

    private TalonFXConfiguration config = new TalonFXConfiguration();

    private SmartTalonFxBuilder(TalonFX primaryMotor, NeutralMode neutralMode) {
        this.primaryMotor = primaryMotor;
        this.neutralMode = neutralMode;
    }

    public static final SmartTalonFxBuilder fromMotor(TalonFX primaryMotor, NeutralMode neutralMode, boolean invert) {
        primaryMotor.setInverted(invert);
        return new SmartTalonFxBuilder(primaryMotor, neutralMode);
    }

    public static final SmartTalonFxBuilder fromId(int id, NeutralMode neutralMode, boolean invert) {
        return fromMotor(new TalonFX(id), neutralMode, invert);
    }

    public static final SmartTalonFxBuilder fromId(int id, String busName, NeutralMode neutralMode, boolean invert) {
        return fromMotor(new TalonFX(id, busName), neutralMode, invert);
    }

    public final SmartTalonFxBuilder withConfig(TalonFXConfiguration config) {
        this.config = config;

        return this;
    }

    public final SmartTalonFxBuilder addFollower(TalonFX follower, TalonFXInvertType invertType) {
        this.followers.add(Pair.of(follower, invertType));

        return this;
    }

    public final SmartTalonFx build() {
        var motors = new LinkedList<TalonFX>();

        configureMotor(primaryMotor, config, neutralMode);
        motors.add(primaryMotor);

        for(var motorAndInvertType : followers) {
            var follower = motorAndInvertType.getFirst();
            var invertType = motorAndInvertType.getSecond();

            configureMotor(follower, config, neutralMode);

            follower.follow(primaryMotor);
            follower.setInverted(invertType);

            motors.add(follower);
        }

        return new SmartTalonFx(new TalonFxMotorGroup(primaryMotor, motors));
    }

    private static final void configureMotor(TalonFX motor, TalonFXConfiguration config, NeutralMode neutralMode) {
        motor.configFactoryDefault();
        motor.configAllSettings(config);
        motor.setNeutralMode(neutralMode);
    }
}
