package redbacks.robot;

import java.util.Optional;

import arachne4.lib.ArachneRobot4;
import arachne4.lib.AutoManager;
import arachne4.lib.game.GameState;
import arachne4.lib.scheduler.Scheduler;
import arachne4.lib.scheduler.SchedulerProvider;
import arachne4.lib.subsystems.Subsystem;
import arachne4.lib.units.Duration;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Rotation2d;
import redbacks.robot.subsystems.arm.ArmHardware;
import redbacks.robot.subsystems.arm.ArmMappings;
import redbacks.robot.subsystems.arm.Arm;
import redbacks.robot.subsystems.coneIntake.ConeIntake;
import redbacks.robot.subsystems.coneIntake.ConeIntakeHardware;
import redbacks.robot.subsystems.coneIntake.ConeIntakeMappings;
import redbacks.robot.subsystems.coordination.ArmAndCubeCoordination;
import redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationMappings;
import redbacks.robot.subsystems.cubeIntake.CubeIntakeHardware;
import redbacks.robot.subsystems.cubeIntake.CubeIntakeMappings;
import redbacks.robot.subsystems.cubeIntake.CubeIntake;
import redbacks.robot.subsystems.drivetrain.Drivetrain;
import redbacks.robot.subsystems.drivetrain.DrivetrainHardware;
import redbacks.robot.subsystems.drivetrain.DrivetrainMappings;

public class Robot extends Subsystem<Void> {
    public static void main(String[] args) {
        ArachneRobot4.startArachneRobot(Robot::new, Constants.LOOP_PERIOD);
    }

    private Controllers controllers = new Controllers();
    private ArmAndCubeCoordinationMappings armAndCubeCoordinationMappings = new ArmAndCubeCoordinationMappings(this, controllers);

    Drivetrain drivetrain = new Drivetrain(
        this,
        new DrivetrainHardware(this),
        new DrivetrainMappings(this, controllers)
    );

    ConeIntake coneIntake = new ConeIntake(
        this,
        new ConeIntakeHardware(),
        new ConeIntakeMappings(this, controllers, armAndCubeCoordinationMappings)
    );

    CubeIntake cubeIntake = new CubeIntake(
        this,
        new CubeIntakeHardware(),
        new CubeIntakeMappings(this, controllers, armAndCubeCoordinationMappings)
    );

    Arm arm = new Arm(
        this,
        new ArmHardware(this),
        new ArmMappings(this, controllers, armAndCubeCoordinationMappings)
    );

    ArmAndCubeCoordination armAndCubeCoordination = new ArmAndCubeCoordination(
        this,
        armAndCubeCoordinationMappings,
        arm,
        cubeIntake
    );

    AutoManager<Robot, Auto> autos = new AutoManager<>(this, Auto.DO_NOTHING, Auto.values());
    { registerHandler(Scheduler.EXECUTE, autos); }
    { registerHandler(Scheduler.GAME_STATE_CHANGE, to(GameState.AUTO), autos::startAuto); }
    { registerHandler(Scheduler.GAME_STATE_CHANGE, from(GameState.AUTO), autos::stopAuto); }

    public Robot(SchedulerProvider schedulerProvider) {
        super(schedulerProvider, null);
    }

    /*
     * Inter-system communication functions
     */

    public boolean isArmAtScoringTarget() {
        return arm.isAtScoringTarget();
    }

    public Pair<Double, Optional<Duration>> getArmPositionOuttakePowerAndScoringDelay() {
        return arm.getPositionOuttakePowerAndScoringDelay();
    }

    public Rotation2d getLowerArmAngle() {
        return arm.getLowerArmAngle();
    }

    public void stopAutomatedScoreCone() {
        coneIntake.stopAutomatedScore();
    }

    public Rotation2d getHeading() {
        return drivetrain.getPosition().getHeading();
    }

    public boolean hasOrTryingToIntakeCone() {
        return coneIntake.hasCone() || coneIntake.isOperatorIntaking();
    }
}