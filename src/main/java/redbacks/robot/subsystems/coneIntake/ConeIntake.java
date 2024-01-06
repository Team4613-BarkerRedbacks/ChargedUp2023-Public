package redbacks.robot.subsystems.coneIntake;

import arachne4.lib.game.GameState;
import arachne4.lib.scheduler.Scheduler;
import arachne4.lib.subsystems.Subsystem;
import arachne4.lib.units.Seconds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import redbacks.lib.AutoRumbleOnTrue;
import redbacks.robot.Robot;

import static redbacks.robot.subsystems.coneIntake.ConeIntakeConstants.*;

public class ConeIntake extends Subsystem<ConeIntakeMappings> {
    protected final Robot robot;
    protected final ConeIntakeHardware hardware;
    boolean isBeingAutomaticallyControlled = false;
    double timeWasLastNotInPosition;

    public ConeIntake(Robot robot, ConeIntakeHardware hardware, ConeIntakeMappings mappings) {
        super(robot, mappings);

        this.robot = robot;
        this.hardware = hardware;

        
        AutoRumbleOnTrue.register(getScheduler(), hardware.hasCone, new Seconds(0.5), mappings.getControllers().driver, mappings.getControllers().operator);
    }

    boolean limitSwitchPrev = false;
    { registerHandler(Scheduler.EXECUTE, GameState.DRIVER_CONTROLLED_STATES, this::intake); }
    private void intake() {
        if (!robot.isArmAtScoringTarget()) {
            timeWasLastNotInPosition = Timer.getFPGATimestamp();
        }

        var armPositionDetails = robot.getArmPositionOuttakePowerAndScoringDelay();

        if(mappings.intake.get()) {
            hardware.coneIntakeMotor.set(CONE_INTAKE_POWER);
        }
        else if(mappings.outtake.get()) {
            hardware.coneIntakeMotor.set(armPositionDetails.getFirst());
        }
        else if(robot.isArmAtScoringTarget() && Timer.getFPGATimestamp() - timeWasLastNotInPosition > armPositionDetails.getSecond().get().asRaw(Seconds::convert)) {
            hardware.coneIntakeMotor.set(armPositionDetails.getFirst());
        }
        else if(!isBeingAutomaticallyControlled) {
            hardware.coneIntakeMotor.set(HOLD_POWER);
        }
    }

    public void autoIntake() {
        hardware.coneIntakeMotor.set(CONE_INTAKE_POWER);
    }

    public void stop() {
        hardware.coneIntakeMotor.set(0);
    }

    public void hold() {
        hardware.coneIntakeMotor.set(HOLD_POWER);
    }

    public void stopAutomatedScore() {
        isBeingAutomaticallyControlled = false;
    }

    { registerHandler(Scheduler.EXECUTE, this::displayDashboard); }
    private void displayDashboard() {
        SmartDashboard.putBoolean("Has Cone", hardware.hasCone.getAsBoolean());
    }

    public boolean hasCone() {
        return hardware.hasCone.getAsBoolean();
    }

    public boolean isOperatorIntaking() {
        return mappings.intake.get();
    }
}