package redbacks.robot.subsystems.drivetrain;

import arachne4.lib.behaviours.Behaviour;

public class DrivetrainBehaviour implements Behaviour {
    protected final DrivetrainHardware hardware;

    protected DrivetrainBehaviour(DrivetrainHardware hardware) {
        this.hardware = hardware;
    }

    protected void acceptDriverInputs(double forward, double left, double rotate) {}
}
