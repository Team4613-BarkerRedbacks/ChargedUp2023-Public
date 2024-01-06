package redbacks.smart.hardware.builders;

import java.util.LinkedList;
import java.util.List;

import com.ctre.phoenixpro.configs.TalonFXConfiguration;
import com.ctre.phoenixpro.controls.Follower;
import com.ctre.phoenixpro.hardware.TalonFX;
import com.ctre.phoenixpro.signals.NeutralModeValue;

import edu.wpi.first.math.Pair;
import redbacks.smart.hardware.SmartTalonFxPro;
import redbacks.smart.hardware.TalonFxProMotorGroup;

public class SmartTalonFxProBuilder {
    private final TalonFX primaryMotor;
    private final NeutralModeValue neutralMode;

    private final List<Pair<TalonFX, Boolean>> followers = new LinkedList<>();

    private TalonFXConfiguration config = new TalonFXConfiguration();
    private boolean useFieldOrientedControl = true;

    private SmartTalonFxProBuilder(TalonFX primaryMotor, NeutralModeValue neutralMode) {
        this.primaryMotor = primaryMotor;
        this.neutralMode = neutralMode;
    }

    public static final SmartTalonFxProBuilder fromMotor(TalonFX primaryMotor, NeutralModeValue neutralMode, boolean invert) {
        primaryMotor.setInverted(invert);
        return new SmartTalonFxProBuilder(primaryMotor, neutralMode);
    }

    public static final SmartTalonFxProBuilder fromId(int id, NeutralModeValue neutralMode, boolean invert) {
        return fromMotor(new TalonFX(id), neutralMode, invert);
    }

    public static final SmartTalonFxProBuilder fromId(int id, String busName, NeutralModeValue neutralMode, boolean invert) {
        return fromMotor(new TalonFX(id, busName), neutralMode, invert);
    }

    public final SmartTalonFxProBuilder withConfig(TalonFXConfiguration config) {
        this.config = config;

        return this;
    }

    public final SmartTalonFxProBuilder disableFieldOrientedControl() {
        useFieldOrientedControl = false;

        return this;
    }

    public final SmartTalonFxProBuilder addFollower(TalonFX follower, boolean opposeMaster) {
        this.followers.add(Pair.of(follower, opposeMaster));

        return this;
    }

    public final SmartTalonFxPro build() {
        var motors = new LinkedList<TalonFX>();

        configureMotor(primaryMotor, config, neutralMode);
        motors.add(primaryMotor);

        for(var motorAndShouldOppose : followers) {
            var follower = motorAndShouldOppose.getFirst();
            var shouldOppose = motorAndShouldOppose.getSecond();

            configureMotor(follower, config, neutralMode);

            follower.setControl(new Follower(primaryMotor.getDeviceID(), shouldOppose));

            motors.add(follower);
        }

        return new SmartTalonFxPro(new TalonFxProMotorGroup(primaryMotor, motors), useFieldOrientedControl);
    }

    private static final void configureMotor(TalonFX motor, TalonFXConfiguration config, NeutralModeValue neutralMode) {
        config.MotorOutput.NeutralMode = neutralMode;
        motor.getConfigurator().apply(config);
    }
}
