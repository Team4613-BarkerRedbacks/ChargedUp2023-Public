package redbacks.robot.subsystems.cubeIntake;

import java.util.function.BooleanSupplier;

import arachne4.lib.scheduler.SchedulerProvider;
import arachne4.lib.scheduler.mappings.ButtonMapping;
import arachne4.lib.scheduler.mappings.MappingManager;
import redbacks.robot.Controllers;
import redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationMappings;

public class CubeIntakeMappings extends MappingManager<Controllers> {
    private final ArmAndCubeCoordinationMappings coordinationMappings;

    public CubeIntakeMappings(SchedulerProvider schedulerProvider, Controllers controllers, ArmAndCubeCoordinationMappings coordinationMappings) {
        super(schedulerProvider, controllers);

        this.coordinationMappings = coordinationMappings;
    }

    final ButtonMapping stow = inCubeModeOnly(controllers.operator::getXButton);
    final ButtonMapping goToIntake = inCubeModeOnly(controllers.operator::getRightBumper);
    final ButtonMapping goToLongShot = inCubeModeOnly(controllers.operator::getStartButton);

    final ButtonMapping goToHigh = inCubeModeOnly(controllers.operator::getYButton);
    final ButtonMapping goToMid = inCubeModeOnly(controllers.operator::getBButton);
    final ButtonMapping goToGround = inCubeModeOnly(controllers.operator::getAButton);

    final ButtonMapping intake = inCubeModeOnly(() -> Controllers.isTriggerPressed(controllers.operator.getLeftTriggerAxis()));
    final ButtonMapping outtake = inCubeModeOnly(() -> Controllers.isTriggerPressed(controllers.driver.getRightTriggerAxis()));

    final ButtonMapping fastOuttake = inCubeModeOnly(controllers.driver::getRightBumper);
    private final ButtonMapping inCubeModeOnly(BooleanSupplier button) {
        return mapping(() -> coordinationMappings.inCubeMode.get() && button.getAsBoolean());
    }

    Controllers getControllers() {
        return controllers;
    }
}