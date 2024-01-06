package redbacks.lib;

import static arachne4.lib.sequences.Actionable.*;

import java.util.function.BooleanSupplier;

import arachne4.lib.game.GameState;
import arachne4.lib.scheduler.Scheduler;
import arachne4.lib.scheduler.SchedulerProviderBase;
import arachne4.lib.sequences.ActionConductor;
import arachne4.lib.units.Duration;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import redbacks.robot.Constants;

public class AutoRumbleOnTrue extends SchedulerProviderBase {
    boolean lastValue = false;

    public static void register(Scheduler scheduler, BooleanSupplier sensor, Duration rumbleDuration, XboxController... controllers) {
        new AutoRumbleOnTrue(scheduler, sensor, rumbleDuration, controllers);
    }

    AutoRumbleOnTrue(Scheduler scheduler, BooleanSupplier sensor, Duration rumbleDuration, XboxController... controllers) {
        super(scheduler);

        var conductor = new ActionConductor();

        var rumbleActionable = SEQUENCE(
            DO(() -> rumbleControllers(controllers, true)),
            WAIT(rumbleDuration),
            DO(() -> rumbleControllers(controllers, false))
        );

        registerHandler(Scheduler.EXECUTE, (gameState) -> {
            var newValue = sensor.getAsBoolean();
            if(newValue && !lastValue && gameState != GameState.DISABLED) conductor.add(rumbleActionable);
            lastValue = newValue;

            conductor.run();
        });
    }

    static void rumbleControllers(XboxController[] controllers, boolean shouldRumble) {
        for(XboxController controller : controllers) controller.setRumble(RumbleType.kLeftRumble, shouldRumble ? Constants.RUMBLE_STRENGTH : 0);
    }
}
