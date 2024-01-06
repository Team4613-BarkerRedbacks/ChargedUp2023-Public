package redbacks.robot.subsystems.arm;

import java.util.function.BooleanSupplier;

import arachne4.lib.scheduler.SchedulerProvider;
import arachne4.lib.scheduler.mappings.ButtonMapping;
import arachne4.lib.scheduler.mappings.MappingManager;
import redbacks.robot.Controllers;
import redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationMappings;

public class ArmMappings extends MappingManager<Controllers> {
    private final ArmAndCubeCoordinationMappings coordinationMappings;

    final ButtonMapping resetArmEncoders = mapping(controllers.operator::getBackButton);

    public ArmMappings(SchedulerProvider schedulerProvider, Controllers controllers, ArmAndCubeCoordinationMappings coordinationMappings) {
        super(schedulerProvider, controllers);

        this.coordinationMappings = coordinationMappings;
    }

    private final ButtonMapping inConeModeOnly(BooleanSupplier button) {
        return mapping(() -> !coordinationMappings.inCubeMode.get() && button.getAsBoolean());
    }
}