package redbacks.robot.subsystems.coneIntake;

import java.util.function.BooleanSupplier;

import arachne4.lib.scheduler.SchedulerProvider;
import arachne4.lib.scheduler.mappings.ButtonMapping;
import arachne4.lib.scheduler.mappings.MappingManager;

import redbacks.robot.Controllers;
import redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationMappings;

public class ConeIntakeMappings extends MappingManager<Controllers> {
    private final ArmAndCubeCoordinationMappings coordinationMappings;

    public ConeIntakeMappings(SchedulerProvider schedulerProvider, Controllers controllers, ArmAndCubeCoordinationMappings coordinationMappings) {
        super(schedulerProvider, controllers);

        this.coordinationMappings = coordinationMappings;
    }

    ButtonMapping intake = inConeModeOnly(() -> Controllers.isTriggerPressed(controllers.operator.getLeftTriggerAxis()));

    ButtonMapping outtake = inConeModeOnly(() -> Controllers.isTriggerPressed(controllers.driver.getRightTriggerAxis())
        || Controllers.isTriggerPressed(controllers.operator.getRightTriggerAxis()));

    private final ButtonMapping inConeModeOnly(BooleanSupplier button) {
        return mapping(() -> !coordinationMappings.inCubeMode.get() && button.getAsBoolean());
    }

    Controllers getControllers() {
        return controllers;
    }
}