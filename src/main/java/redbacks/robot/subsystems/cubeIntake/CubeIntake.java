package redbacks.robot.subsystems.cubeIntake;

import static redbacks.robot.subsystems.cubeIntake.CubeIntakeConstants.*;
import static redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationConstants.*;

import arachne4.lib.game.GameState;
import arachne4.lib.scheduler.Scheduler;
import arachne4.lib.subsystems.Subsystem;
import arachne4.lib.units.Seconds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import redbacks.lib.AutoRumbleOnTrue;
import redbacks.robot.Robot;

public class CubeIntake extends Subsystem<CubeIntakeMappings> {
    final Robot robot;
    final CubeIntakeHardware hardware;

    IntakePosition currentTargetPosition = STARTING_POS;
    boolean isAvoidingArm = false;
    boolean wasOuttaking = false;

    public CubeIntake(Robot robot, CubeIntakeHardware hardware, CubeIntakeMappings mappings) {
        super(robot, mappings);

        this.robot = robot;
        this.hardware = hardware;

        AutoRumbleOnTrue.register(getScheduler(), hardware.hasCube, new Seconds(0.5), mappings.getControllers().driver, mappings.getControllers().operator);
    }

    { registerHandler(Scheduler.INITIALIZE, this::setStartingPosition); }
    private void setStartingPosition() {
        hardware.pivot.setSensorAngle(STARTING_POS.angle);
    }

    { registerHandler(Scheduler.EXECUTE, this::displayDashboard); }
    private void displayDashboard() {
        SmartDashboard.putNumber("Cube Intake Position", hardware.pivot.getAngle().getDegrees());
        SmartDashboard.putBoolean("Has Cube", hardware.hasCube.getAsBoolean());
    }

    { registerHandler(Scheduler.EXECUTE, GameState.DRIVER_CONTROLLED_STATES, this::intake); }
    private void intake() {
        if(mappings.intake.get()) {
            if(!hardware.hasCube.getAsBoolean()) {
                hardware.intake.setPercentageOutput(INTAKE_POWER);
            }
            else {
                if (currentTargetPosition == CUBE_FROM_GROUND.cubeIntakePosition) {
                    moveToPos(STOW_POS);
                }
                hardware.intake.setPercentageOutput(0);
            }
        }
        else if(mappings.outtake.get()) {
            hardware.intake.setPercentageOutput(currentTargetPosition.regularOuttakePower);

            wasOuttaking = true;
        }
        else if(mappings.fastOuttake.get()) {
            hardware.intake.setPercentageOutput(currentTargetPosition.fastOuttakePower);

            wasOuttaking = true;
        }
        else {
            hardware.intake.setPercentageOutput(HOLD_POWER);

            if(wasOuttaking && !hardware.hasCube.getAsBoolean()) /*moveToPos(STOW_POS)*/;

            wasOuttaking = false;
        }
    }

    /* See ArmAndCubeCoordination for mappings */
    public void moveToPos(IntakePosition targetPosition) {
        hardware.pivot.setTargetAngle(targetPosition.angle);
        currentTargetPosition = targetPosition;
    }

    public void shoot() {
        hardware.intake.setPercentageOutput(currentTargetPosition.regularOuttakePower);
    }

    public void intakeInAuto() {
        hardware.intake.setPercentageOutput(INTAKE_POWER);
    }
    public void slowIntakeInAuto() {
        hardware.intake.setPercentageOutput(SLOW_INTAKE_POWER);
    }

    public void stop() {
        hardware.intake.setPercentageOutput(HOLD_POWER);
    }

    public void holdCube() {
        hardware.intake.setPercentageOutput(0.1);
    }

    public boolean isOnTarget() {
        return Math.abs(hardware.pivot.getAngle().getRadians() - currentTargetPosition.angle.getRadians()) < TARGET_ANGLE_TOLERANCE.getRadians();
    }

    public boolean hasCube() {
        return hardware.hasCube.getAsBoolean();
    }
}