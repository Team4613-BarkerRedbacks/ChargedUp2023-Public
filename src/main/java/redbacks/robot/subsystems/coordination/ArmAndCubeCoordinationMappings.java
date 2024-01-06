package redbacks.robot.subsystems.coordination;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import arachne4.lib.scheduler.SchedulerProvider;
import arachne4.lib.scheduler.mappings.ButtonMapping;
import arachne4.lib.scheduler.mappings.MappingManager;
import redbacks.robot.Controllers;
import redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationConstants.ArmAndCubePosition;

import static redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationConstants.*;

public class ArmAndCubeCoordinationMappings extends MappingManager<Controllers> {
    public ArmAndCubeCoordinationMappings(SchedulerProvider schedulerProvider, Controllers controllers) {
        super(schedulerProvider, controllers);
    }

    public final ButtonMapping inCubeMode = mapping(controllers.operator::getLeftBumper);

    final PositionMapping cubeIntakeToStow = new PositionMapping(true, controllers.operator::getXButton, STOW_ALL);
    final PositionMapping cubeIntakeToIntakeFromGround = new PositionMapping(true, controllers.operator::getRightBumper, CUBE_FROM_GROUND);
    final PositionMapping cubeIntakeToLongShot = new PositionMapping(true, controllers.operator::getStartButton, CUBE_LONG_SHOT);
    final PositionMapping cubeIntakeToHigh = new PositionMapping(true, controllers.operator::getYButton, CUBE_SCORE_HIGH);
    final PositionMapping cubeIntakeToMid = new PositionMapping(true, controllers.operator::getBButton, CUBE_SCORE_MID);
    final PositionMapping cubeIntakeToLow = new PositionMapping(true, controllers.operator::getAButton, CUBE_SCORE_LOW);

    final PositionMapping armToStowWithCone = new PositionMapping(false, controllers.operator::getXButton, STOW_WITH_CONE);
    final PositionMapping armToMidPole = new PositionMapping(false, controllers.operator::getBButton, CONE_SCORE_MID);
    final PositionMapping armToHighPole = new PositionMapping(false, controllers.operator::getYButton, CONE_SCORE_HIGH);
    final PositionMapping armToHumanPlayer = new PositionMapping(false, controllers.operator::getRightBumper, CONE_FROM_HUMAN_PLAYER);
    final PositionMapping armToLow = new PositionMapping(false, controllers.operator::getAButton, CONE_SCORE_LOW);

    private static List<PositionMapping> positionMappings = new ArrayList<>();

    final class PositionMapping {
        final ButtonMapping control;
        final ArmAndCubePosition position;

        PositionMapping(boolean isCubeMapping, BooleanSupplier button, ArmAndCubePosition position) {
            this.control = mapping(() -> inCubeMode.get() == isCubeMapping && button.getAsBoolean());
            this.position = position;

            positionMappings.add(this);
        }
    }

    final void forEachPositionMapping(Consumer<PositionMapping> mapping) {
        positionMappings.forEach(mapping);
    }
}
