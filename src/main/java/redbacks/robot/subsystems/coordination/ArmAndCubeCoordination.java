package redbacks.robot.subsystems.coordination;

import arachne4.lib.scheduler.SchedulerProvider;
import arachne4.lib.subsystems.Subsystem;
import redbacks.robot.subsystems.arm.Arm;
import redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationConstants.ArmAndCubePosition;
import redbacks.robot.subsystems.cubeIntake.CubeIntake;

public class ArmAndCubeCoordination extends Subsystem<ArmAndCubeCoordinationMappings> {
    final Arm arm;
    final CubeIntake cubeIntake;

    public ArmAndCubeCoordination(SchedulerProvider schedulerProvider, ArmAndCubeCoordinationMappings mappings, Arm arm, CubeIntake cubeIntake) {
        super(schedulerProvider, mappings);

        this.arm = arm;
        this.cubeIntake = cubeIntake;
    }

    { mappings.forEachPositionMapping((mapping) -> registerHandler(mapping.control.onPress(), () -> goToPositions(mapping.position))); }
    public void goToPositions(ArmAndCubePosition positions) {
        // FIXME Remove below line when re-enabling arm
        cubeIntake.moveToPos(positions.cubeIntakePosition);

        var positionsAfterArmSafety = arm.getSafeTarget(positions);

        arm.goToPosition(positionsAfterArmSafety.armPosition);
        cubeIntake.moveToPos(positionsAfterArmSafety.cubeIntakePosition);
    }
}
