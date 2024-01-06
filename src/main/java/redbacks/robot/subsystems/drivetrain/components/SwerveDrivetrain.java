package redbacks.robot.subsystems.drivetrain.components;

import arachne4.lib.game.GameState;
import arachne4.lib.scheduler.Scheduler;
import arachne4.lib.scheduler.SchedulerProviderBase;
import arachne4.lib.units.Distance;
import arachne4.lib.units.Metres;
import arachne4.lib.units.concats.Position2d;
import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import redbacks.smart.hardware.SmartAngleSensor;

public class SwerveDrivetrain extends SchedulerProviderBase {
    private final SmartAngleSensor yawSensor;
    private final SwerveModule[] modules;
    private final SwerveDriveKinematics kinematics;
    private final SwerveDrivePoseEstimator odometry;
    public final SwerveDriveKinematics getKinematics() {
        return kinematics;
    } 
    
    private Position2d currentPosition;
    private ChassisSpeeds currentVelocityMetresAndRadiansPerSec;

    public SwerveDrivetrain(Scheduler scheduler, SwerveDrivePoseEstimatorConfig poseEstimatorConfig, SmartAngleSensor yawSensor, SwerveModule... modules) {
        super(scheduler);

        var locations = new Translation2d[modules.length];
        for(int i = 0; i < modules.length; i++) locations[i] = modules[i].getOffsetFromRobotCentre().asRaw(Metres::convert);

        this.yawSensor = yawSensor;
        this.modules = modules.clone();
        this.kinematics = new SwerveDriveKinematics(locations);

        this.odometry = new SwerveDrivePoseEstimator(
            kinematics, 
            yawSensor.getAngle(), 
            getModulePositions(), 
            new Pose2d(), 
            poseEstimatorConfig.getStateStdDevsMetresMetresRadians(),
            poseEstimatorConfig.getVisionStdDevsMetresMetresRadians()
        );
    }

    /* Handlers */

    { registerHandler(Scheduler.PRE_EXECUTE, this::updateOdometry); }
    private void updateOdometry() {
        SwerveModulePosition[] modulePositions = getModulePositions();

        try {
            // If the method call does not throw an exception, update the last known pose
            // We store the pose to reset to in case of errors due to timing in updateWithTime()
            currentPosition = new Position2d(
                odometry.updateWithTime(Timer.getFPGATimestamp(), yawSensor.getAngle(), modulePositions),
                Metres::new
            );
        }
        catch(RuntimeException e) {
            // If updateWithTime() throws an exception, reset to the last known position
            odometry.resetPosition(yawSensor.getAngle(), modulePositions, currentPosition.asRaw(Metres::convert));
        }

        // Rotate robot-relative speeds by the negative of the heading to get field-relative speeds
        currentVelocityMetresAndRadiansPerSec = ChassisSpeeds.fromFieldRelativeSpeeds(
            kinematics.toChassisSpeeds(getModuleStates()),
            currentPosition.getHeading().unaryMinus());
    }

    private boolean hasBeenEnabled = false;
    { registerHandler(Scheduler.GAME_STATE_CHANGE, from(GameState.DISABLED), () -> hasBeenEnabled = true); }
    { registerHandler(Scheduler.INITIALIZE, this::fixModuleAngles); }
    { registerHandler(Scheduler.EXECUTE, GameState.DISABLED, this::fixModuleAngles); }
    private void fixModuleAngles() {
        if (!hasBeenEnabled) {
            for(var module : modules) module.fixAngle();
        } 
    }

    { registerHandler(Scheduler.EXECUTE, this::displayModuleOffsets); }
    private void displayModuleOffsets() {
        for(int i = 0; i < 4; i++) {
            modules[i].show(i);
        }
    }
    
    /* Public interface */

    public void driveWithVelocity(ChassisSpeeds velocitiesMetresAndRadiansPerSec) {
        SwerveModuleState[] moduleStates = kinematics.toSwerveModuleStates(velocitiesMetresAndRadiansPerSec);
        for(int i = 0; i < modules.length; i++) modules[i].drive(moduleStates[i]);
    }

    public Position2d getPosition() {
        return currentPosition;
    }

    /**
     * @param posX The position along long end of the field (The centre is 0, your side is negative)
     * @param posY The position along short end of field (The centre is 0, the right is negative)
     * @param angle The heading of the robot (Facing the opposing alliance station is 0)
     */
    public void setPosition(Distance posX, Distance posY, Rotation2d heading) {
        odometry.resetPosition(
            yawSensor.getAngle(),
            getModulePositions(),
            new Pose2d(
                posX.asRaw(Metres::convert),
                posY.asRaw(Metres::convert),
                heading
            )
        );
        currentPosition = new Position2d(posX, posY, heading);
    }

    public ChassisSpeeds getVelocityMetresAndRadiansPerSec() {
        return currentVelocityMetresAndRadiansPerSec;
    }

    public void addVisionMeasurement(Pose2d visionCalculatedPositionMetres, double timestampSeconds) {
        odometry.addVisionMeasurement(visionCalculatedPositionMetres, timestampSeconds, new MatBuilder<>(Nat.N3(), Nat.N1()).fill(1, 1, 1));
    }

    private SwerveModulePosition[] getModulePositions() {
        SwerveModulePosition[] modulePositions = new SwerveModulePosition[modules.length];
        for(int i = 0; i < modules.length; i++) modulePositions[i] = modules[i].getModulePosition();

        return modulePositions;
    }

    private SwerveModuleState[] getModuleStates() {
        SwerveModuleState[] moduleStates = new SwerveModuleState[modules.length];
        for(int i = 0; i < modules.length; i++) moduleStates[i] = modules[i].getModuleState();

        return moduleStates;
    }
}
