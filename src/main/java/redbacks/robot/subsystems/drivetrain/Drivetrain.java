package redbacks.robot.subsystems.drivetrain;

import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import arachne4.lib.behaviours.BehaviourManager;
import arachne4.lib.game.GameState;
import arachne4.lib.scheduler.Scheduler;
import arachne4.lib.sequences.Actionable;
import arachne4.lib.sequences.Untilable;
import arachne4.lib.sequences.actions.Action;
import arachne4.lib.subsystems.Subsystem;
import arachne4.lib.units.Distance;
import arachne4.lib.units.Metres;
import arachne4.lib.units.MetresPerSec;
import arachne4.lib.units.Velocity;
import arachne4.lib.units.concats.Position2d;
import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrajectoryGenerator.ControlVectorList;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import redbacks.robot.Robot;

public class Drivetrain extends Subsystem<DrivetrainMappings> {
    protected final Robot robot;
    protected final DrivetrainHardware hardware;

    final BehaviourManager<DrivetrainBehaviour> behaviour;
    final DrivetrainBehaviour fieldRelativeDriveBehaviour;
    //final DrivetrainBehaviour automaticCenterObjectAlign;
    // final DrivetrainBehaviour automaticSlowdownAtGridBehaviour;
    final DrivetrainBehaviour snapTo0DegreesBehaviour;
    final DrivetrainBehaviour snapTo180DegreesBehaviour;

    public Drivetrain(Robot robot, DrivetrainHardware hardware, DrivetrainMappings mappings) {
        super(robot, mappings);
        this.robot = robot;
        this.hardware = hardware;
        
        this.fieldRelativeDriveBehaviour = new FieldRelativeDriveBehaviour(hardware);
        //this.automaticCenterObjectAlign = new AutomaticCenterObjectAlign(hardware);
        // this.automaticSlowdownAtGridBehaviour = new AutomaticSlowdownAtGridBehaviour(hardware);
        this.snapTo0DegreesBehaviour = new SnapToAngleBehaviour(hardware, Rotation2d.fromDegrees(0));
        this.snapTo180DegreesBehaviour = new SnapToAngleBehaviour(hardware, Rotation2d.fromDegrees(180));

        this.behaviour = new BehaviourManager<DrivetrainBehaviour>(fieldRelativeDriveBehaviour);
        registerHandler(Scheduler.EXECUTE, behaviour);
    }

    /* Handlers */

    // Change to driver control by default whenever game state changes
    { registerHandler(Scheduler.GAME_STATE_CHANGE, this::changeToDriverControl); }
    private void changeToDriverControl() {
        behaviour.changeToMode(fieldRelativeDriveBehaviour);
    }

    // { registerHandler(mappings.activateAutoConeTrack.onPress(), this::changeToConeTrack); }
    // private void changeToConeTrack() {
    //     hardware.limelight.setPipeline(Pipelines.CONE);
    //     behaviour.changeToMode(automaticCenterObjectAlign);
    // }

    { registerHandler(mappings.activateSnapTo0.onChange(), usingToBoolean(this::snapTo0)); }
    private void snapTo0(boolean activate) {
        behaviour.changeToMode(activate ? snapTo0DegreesBehaviour : fieldRelativeDriveBehaviour);
    }

    { registerHandler(mappings.activateSnapTo180.onChange(), usingToBoolean(this::snapTo180)); }
    private void snapTo180(boolean activate) {
        behaviour.changeToMode(activate ? snapTo180DegreesBehaviour : fieldRelativeDriveBehaviour);
    }

    { registerHandler(Scheduler.EXECUTE, GameState.DRIVER_CONTROLLED_STATES, this::acceptDriverInputs); }
    private void acceptDriverInputs() {
        var inputs = mappings.driverInputs.get();

        behaviour.getCurrentMode().ifPresent((behaviour) -> behaviour.acceptDriverInputs(inputs.vX, inputs.vY, inputs.vTheta));
    }

    { registerHandler(mappings.resetHeading.onPress(), this::resetHeading); }
    private void resetHeading() {
        var currentLocation = hardware.drivetrain.getPosition().getLocation();

        hardware.drivetrain.setPosition(currentLocation.getX(), currentLocation.getY(), new Rotation2d(0));
    }

    public Position2d getPosition() {
        return hardware.drivetrain.getPosition();
    }

    { registerHandler(Scheduler.EXECUTE, this::displayOdometry); }
    private void displayOdometry() {
        SmartDashboard.putNumber("Robot X", hardware.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert));
        SmartDashboard.putNumber("Robot Y", hardware.drivetrain.getPosition().getLocation().getY().asRaw(Metres::convert));
        SmartDashboard.putNumber("Robot Heading", hardware.drivetrain.getPosition().getHeading().getDegrees());
        SmartDashboard.putNumber("pitch value", hardware.pigeon.getPitch());
        SmartDashboard.putNumber("roll value", hardware.pigeon.getRoll());

        var velocity = hardware.drivetrain.getVelocityMetresAndRadiansPerSec();
        SmartDashboard.putNumber("Drive Velocity", Math.sqrt(velocity.vxMetersPerSecond * velocity.vxMetersPerSecond + velocity.vyMetersPerSecond * velocity.vyMetersPerSecond));
    }

    /* Accessors */

    public void setPosition(Distance posX, Distance posY, Rotation2d heading) {
        hardware.drivetrain.setPosition(posX, posY, heading);
    }

    /* Actionables */

    public Untilable doMoveTo(Translation2d target, Rotation2d rotation) {
        return doMoveTo(target, (position) -> rotation);
    }

    public Untilable doVelocityTo(Translation2d target, Rotation2d targetHeading, Velocity endVelocity) {
        return doVelocityTo(target, (pos) -> targetHeading, MAX_LINEAR_VELOCITY, endVelocity, Collections.emptyList());
    }

    public Untilable doVelocityTo(Translation2d target, Rotation2d targetHeading, Velocity maxVelocity, Velocity endVelocity) {
        return doVelocityTo(target, (pos) -> targetHeading, maxVelocity, endVelocity, Collections.emptyList());
    }

    public Untilable doVelocityTo(Translation2d target, Function<Translation2d, Rotation2d> targetHeadingFromLocation, Velocity maxVelocity, Velocity endVelocity, List<Translation2d> interiorWayPoints) {
        return (host, conditionModifier) -> new Action(host, conditionModifier) {
            HolonomicDriveController controller;
            Trajectory trajectory;
            double startTimeSeconds;
            double endVelocityXMetresPerSec;
            double endVelocityYMetresPerSec;

            @Override
            protected void initialize() {
                ProfiledPIDController rotationController = new ProfiledPIDController(ROTATIONAL_KP, ROTATIONAL_KI, ROTATIONAL_KD, AUTO_ROTATION_MOTION_CONSTRAINTS);
                rotationController.enableContinuousInput(-Math.PI, Math.PI);

                controller = new HolonomicDriveController(
                    new PIDController(LINEAR_KP, LINEAR_KI, LINEAR_KD),
                    new PIDController(LINEAR_KP, LINEAR_KI, LINEAR_KD),
                    rotationController
                );

                Translation2d currentLocation = hardware.drivetrain.getPosition().asRaw(Metres::convert).getTranslation();
                Translation2d targetLocation = target;
                Rotation2d deltaDirection = target.minus(currentLocation).getAngle();

                var startVelocityMetresPerSec = Math.sqrt(Math.pow(hardware.drivetrain.getVelocityMetresAndRadiansPerSec().vxMetersPerSecond, 2) + Math.pow(hardware.drivetrain.getVelocityMetresAndRadiansPerSec().vyMetersPerSecond, 2));

                TrajectoryConfig trajectoryconfig = new TrajectoryConfig(maxVelocity.asRaw(MetresPerSec::convert), MAX_ACCELERATION_METRES_PER_SECOND_SQUARED)
                    .setKinematics(hardware.drivetrain.getKinematics())
                    .setStartVelocity(startVelocityMetresPerSec)
                    .setEndVelocity(endVelocity.asRaw(MetresPerSec::convert));

                trajectory = TrajectoryGenerator.generateTrajectory(
                    new Pose2d(currentLocation, deltaDirection),
                    interiorWayPoints,
                    new Pose2d(targetLocation, deltaDirection),
                    trajectoryconfig
                );

                endVelocityXMetresPerSec = Math.cos(deltaDirection.getRadians()) * endVelocity.asRaw(MetresPerSec::convert);
                endVelocityYMetresPerSec = Math.sin(deltaDirection.getRadians()) * endVelocity.asRaw(MetresPerSec::convert);

                startTimeSeconds = Timer.getFPGATimestamp();

                SmartDashboard.putNumberArray("Drivetrain Target", new double[] { targetLocation.getX(), targetLocation.getY() });
            }

            @Override
            protected void execute() {
                Pose2d currentPosition = hardware.drivetrain.getPosition().asRaw(Metres::convert);

                hardware.drivetrain.driveWithVelocity(controller.calculate(
                    currentPosition,
                    trajectory.sample(Timer.getFPGATimestamp() - startTimeSeconds),
                    targetHeadingFromLocation.apply(currentPosition.getTranslation())
                ));
            }

			@Override
			protected void end() {
				hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(endVelocityXMetresPerSec, endVelocityYMetresPerSec, 0));
			}

			@Override
			protected boolean isFinished() {
                Pose2d currentPosition = hardware.drivetrain.getPosition().asRaw(Metres::convert);
                double timeElapsedSeconds = Timer.getFPGATimestamp() - startTimeSeconds;

				return target.minus(currentPosition.getTranslation()).getNorm() < POSITION_TOLERANCE_METRES * 2
                    && Math.abs(targetHeadingFromLocation.apply(target).minus(currentPosition.getRotation()).getRadians()) < ROTATION_TOLERANCE_RADIANS
                    && timeElapsedSeconds >= trajectory.getTotalTimeSeconds();
			}
        };
    }

    public Untilable doVelocityToWithControlVectors(ControlVectorList controlVectors, Function<Translation2d, Rotation2d> targetHeadingFromLocation, Velocity maxVelocity, Velocity endVelocity) {
        return (host, conditionModifier) -> new Action(host, conditionModifier) {
            HolonomicDriveController controller;
            Trajectory trajectory;
            double startTimeSeconds;

            Translation2d targetLocation;
            double endVelocityXMetresPerSec;
            double endVelocityYMetresPerSec;

            @Override
            protected void initialize() {
                ProfiledPIDController rotationController = new ProfiledPIDController(ROTATIONAL_KP, ROTATIONAL_KI, ROTATIONAL_KD, AUTO_ROTATION_MOTION_CONSTRAINTS);
                rotationController.enableContinuousInput(-Math.PI, Math.PI);

                controller = new HolonomicDriveController(
                    new PIDController(LINEAR_KP, LINEAR_KI, LINEAR_KD),
                    new PIDController(LINEAR_KP, LINEAR_KI, LINEAR_KD),
                    rotationController
                );
                
                var startVelocityMetresPerSec = Math.sqrt(Math.pow(hardware.drivetrain.getVelocityMetresAndRadiansPerSec().vxMetersPerSecond, 2) + Math.pow(hardware.drivetrain.getVelocityMetresAndRadiansPerSec().vyMetersPerSecond, 2));
                
                var lastVector = controlVectors.get(controlVectors.size() - 1);
                var endAngleRadians = Math.atan2(lastVector.y[1], lastVector.x[1]);
                var endVelocityMetresPerSec = endVelocity.asRaw(MetresPerSec::convert);
                
                targetLocation = new Translation2d(lastVector.x[0], lastVector.y[0]);
                endVelocityXMetresPerSec = Math.cos(endAngleRadians) * endVelocityMetresPerSec;
                endVelocityYMetresPerSec = Math.sin(endAngleRadians) * endVelocityMetresPerSec;

                TrajectoryConfig trajectoryconfig = new TrajectoryConfig(maxVelocity.asRaw(MetresPerSec::convert), MAX_ACCELERATION_METRES_PER_SECOND_SQUARED)
                    .setKinematics(hardware.drivetrain.getKinematics())
                    .setStartVelocity(startVelocityMetresPerSec)
                    .setEndVelocity(endVelocityMetresPerSec);

                trajectory = TrajectoryGenerator.generateTrajectory(
                    controlVectors,
                    trajectoryconfig
                );
        
                startTimeSeconds = Timer.getFPGATimestamp();

                SmartDashboard.putNumberArray("Drivetrain Target", new double[] { lastVector.x[0], lastVector.y[0] });
            }

            @Override
            protected void execute() {
                Pose2d currentPosition = hardware.drivetrain.getPosition().asRaw(Metres::convert);

                hardware.drivetrain.driveWithVelocity(controller.calculate(
                    currentPosition,
                    trajectory.sample(Timer.getFPGATimestamp() - startTimeSeconds),
                    targetHeadingFromLocation.apply(currentPosition.getTranslation())
                ));
            }

			@Override
			protected void end() {
				hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(endVelocityXMetresPerSec, endVelocityYMetresPerSec, 0));
			}

			@Override
			protected boolean isFinished() {
                Pose2d currentPosition = hardware.drivetrain.getPosition().asRaw(Metres::convert);
                double timeElapsedSeconds = Timer.getFPGATimestamp() - startTimeSeconds;

				return targetLocation.minus(currentPosition.getTranslation()).getNorm() < POSITION_TOLERANCE_METRES
                    && Math.abs(targetHeadingFromLocation.apply(targetLocation).minus(currentPosition.getRotation()).getRadians()) < ROTATION_TOLERANCE_RADIANS
                    && timeElapsedSeconds >= trajectory.getTotalTimeSeconds();
			}
        };
    }

    public Untilable doMoveTo(Translation2d target, Rotation2d rotation, Velocity maxVelocity) {
        return doSplineTo(target, (position) -> rotation, maxVelocity, Arrays.asList());
    }

    public Untilable doMoveTo(Translation2d target, Function<Translation2d, Rotation2d> targetHeadingFromLocation) {
        return doSplineTo(target, targetHeadingFromLocation, MAX_LINEAR_VELOCITY, Arrays.asList());
    }

    public Untilable doSplineTo(Translation2d target, Rotation2d rotation, Velocity maxVelocity, List<Translation2d> interiorWayPoints) {
        return doSplineTo(target, (position) -> rotation, maxVelocity, interiorWayPoints);
    }
    
    public Untilable doBalanceToFacingY(Rotation2d rotation) {
        return (host, conditionModifier) -> new Action(host, conditionModifier) {
            ProfiledPIDController rotationController;
            @Override 
            protected void initialize() {
                rotationController = new ProfiledPIDController(ROTATIONAL_KP, ROTATIONAL_KI, ROTATIONAL_KD, AUTO_ROTATION_MOTION_CONSTRAINTS);
                rotationController.enableContinuousInput(-Math.PI, Math.PI);
                rotationController.reset(hardware.drivetrain.getPosition().getHeading().getRadians());
                rotationController.setGoal(rotation.getRadians());
            }
            
            @Override
            protected void execute() {
                double currentPosition = hardware.drivetrain.getPosition().getHeading().getRadians();
                double rotationSpeed = rotationController.calculate(currentPosition);

                if(-hardware.pigeon.getRoll() > 12) {
                    hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(0, -1.1, rotationSpeed));
                }
                else if(-hardware.pigeon.getRoll() > 11) {
                    hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(0, -0.35, rotationSpeed));
                }
                else if(-hardware.pigeon.getRoll() < -12) {
                    hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(0, 1.1, rotationSpeed));
                }
                else if(-hardware.pigeon.getRoll() < -11) {
                    hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(0, 0.35, rotationSpeed));
                }
                else hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(0,0, rotationSpeed));
            }

			@Override
			protected boolean isFinished() {
                return false;
            }
        };
    }

    public Untilable doBalanceToFacingX(Rotation2d rotation) {
        return (host, conditionModifier) -> new Action(host, conditionModifier) {
            ProfiledPIDController rotationController;
            @Override 
            protected void initialize() {
                rotationController = new ProfiledPIDController(ROTATIONAL_KP, ROTATIONAL_KI, ROTATIONAL_KD, AUTO_ROTATION_MOTION_CONSTRAINTS);
                rotationController.enableContinuousInput(-Math.PI, Math.PI);
                rotationController.reset(hardware.drivetrain.getPosition().getHeading().getRadians());
                rotationController.setGoal(rotation.getRadians());
            }
            
            @Override
            protected void execute() {
                double currentPosition = hardware.drivetrain.getPosition().getHeading().getRadians();
                double rotationSpeed = rotationController.calculate(currentPosition);

                if(hardware.pigeon.getPitch() > 13) {
                    hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(-0.9, 0, rotationSpeed)); //.9
                }
                else if(hardware.pigeon.getPitch() > 9) {
                    hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(-0.35, 0, rotationSpeed));//.35
                }
                else if(hardware.pigeon.getPitch() < -13) {
                    hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(0.9, 0, rotationSpeed));
                }
                else if(hardware.pigeon.getPitch() < -9) {
                    hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(0.35, 0, rotationSpeed));
                }
                else hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(0,0, rotationSpeed));
            }

			@Override
			protected boolean isFinished() {
                return false;
            }
        };
    }
    
    public Untilable doGetOntoBalanceFacingY(Rotation2d rotation, boolean inPositiveY, Double maxVelocity) {
        return (host, conditionModifier) -> new Action(host, conditionModifier) {
            ProfiledPIDController rotationController;
            @Override 
            protected void initialize() {
                rotationController = new ProfiledPIDController(ROTATIONAL_KP, ROTATIONAL_KI, ROTATIONAL_KD, AUTO_ROTATION_MOTION_CONSTRAINTS);
                rotationController.enableContinuousInput(-Math.PI, Math.PI);
                rotationController.reset(hardware.drivetrain.getPosition().getHeading().getRadians());
                rotationController.setGoal(rotation.getRadians());
            }
            
            @Override
            protected void execute() {
                double currentPosition = hardware.drivetrain.getPosition().getHeading().getRadians();
                double rotationSpeed = rotationController.calculate(currentPosition);
                hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(0, inPositiveY ? maxVelocity : -maxVelocity, rotationSpeed));
            }

			@Override
			protected boolean isFinished() {
                return -hardware.pigeon.getRoll() > 11 || -hardware.pigeon.getRoll() < -11;
            }
        };
    }

    public Untilable doGetOntoBalanceFacingX(Rotation2d rotation, boolean inPositiveX, Double maxVelocity) {
        return (host, conditionModifier) -> new Action(host, conditionModifier) {
            ProfiledPIDController rotationController;
            @Override 
            protected void initialize() {
                rotationController = new ProfiledPIDController(ROTATIONAL_KP, ROTATIONAL_KI, ROTATIONAL_KD, AUTO_ROTATION_MOTION_CONSTRAINTS);
                rotationController.enableContinuousInput(-Math.PI, Math.PI);
                rotationController.reset(hardware.drivetrain.getPosition().getHeading().getRadians());
                rotationController.setGoal(rotation.getRadians());
            }
            
            @Override
            protected void execute() {
                double currentPosition = hardware.drivetrain.getPosition().getHeading().getRadians();
                double rotationSpeed = rotationController.calculate(currentPosition);
                hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(inPositiveX ? maxVelocity : -maxVelocity, 0, rotationSpeed));
            }

			@Override
			protected boolean isFinished() {
                return hardware.pigeon.getPitch() > 11 || hardware.pigeon.getPitch() < -11;
            }
        };
    }

    public Untilable doSplineTo(Translation2d target, Function<Translation2d, Rotation2d> targetHeadingFromLocation, Velocity maxVelocity, List<Translation2d> interiorWayPoints) {
        return (host, conditionModifier) -> new Action(host, conditionModifier) {
            HolonomicDriveController controller;
            Trajectory trajectory;
            double startTimeSeconds;
            double startVelocity;

            @Override
            protected void initialize() {
                ProfiledPIDController rotationController = new ProfiledPIDController(ROTATIONAL_KP, ROTATIONAL_KI, ROTATIONAL_KD, AUTO_ROTATION_MOTION_CONSTRAINTS);
                rotationController.enableContinuousInput(-Math.PI, Math.PI);

                controller = new HolonomicDriveController(
                    new PIDController(LINEAR_KP, LINEAR_KI, LINEAR_KD),
                    new PIDController(LINEAR_KP, LINEAR_KI, LINEAR_KD),
                    rotationController
                );
                startVelocity = Math.sqrt(Math.pow(hardware.drivetrain.getVelocityMetresAndRadiansPerSec().vxMetersPerSecond, 2) + Math.pow(hardware.drivetrain.getVelocityMetresAndRadiansPerSec().vyMetersPerSecond, 2));
                Translation2d currentLocation = hardware.drivetrain.getPosition().asRaw(Metres::convert).getTranslation();
                Translation2d targetLocation = target;
                Translation2d delta = target.minus(currentLocation);
                Rotation2d deltaDirection = new Rotation2d(delta.getX(), delta.getY());
                TrajectoryConfig trajectoryconfig = new TrajectoryConfig(maxVelocity.asRaw(MetresPerSec::convert), MAX_ACCELERATION_METRES_PER_SECOND_SQUARED);
                trajectoryconfig.setKinematics(hardware.drivetrain.getKinematics());
                trajectoryconfig.setStartVelocity(startVelocity);

                trajectory = TrajectoryGenerator.generateTrajectory(
                    new Pose2d(currentLocation, deltaDirection),
                    interiorWayPoints,
                    new Pose2d(targetLocation, deltaDirection),
                    trajectoryconfig
                );
        
                startTimeSeconds = Timer.getFPGATimestamp();

                SmartDashboard.putNumberArray("Drivetrain Target", new double[] { targetLocation.getX(), targetLocation.getY() });
            }

            @Override
            protected void execute() {
                Pose2d currentPosition = hardware.drivetrain.getPosition().asRaw(Metres::convert);

                hardware.drivetrain.driveWithVelocity(controller.calculate(
                    currentPosition,
                    trajectory.sample(Timer.getFPGATimestamp() - startTimeSeconds),

                    targetHeadingFromLocation.apply(currentPosition.getTranslation())
                ));
            }

			@Override
			protected void end() {
				hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(0, 0, 0));
			}

			@Override
			protected boolean isFinished() {
                Pose2d currentPosition = hardware.drivetrain.getPosition().asRaw(Metres::convert);
                double timeElapsedSeconds = Timer.getFPGATimestamp() - startTimeSeconds;

				return target.minus(currentPosition.getTranslation()).getNorm() < POSITION_TOLERANCE_METRES
                    && Math.abs(targetHeadingFromLocation.apply(target).minus(currentPosition.getRotation()).getRadians()) < ROTATION_TOLERANCE_RADIANS
                    && timeElapsedSeconds >= trajectory.getTotalTimeSeconds();
			}
        };
    }

    public Untilable doMoveToWithLerpedRotation(Translation2d target, Rotation2d targetAngle, boolean rotateAntiClockwise) {
        return doMoveToWithLerpedRotation(target, targetAngle, MAX_LINEAR_VELOCITY, rotateAntiClockwise);
    }

    public Untilable doMoveToWithLerpedRotation(Translation2d target, Rotation2d targetAngle, Velocity maxVelocity, boolean rotateAntiClockwise) {
        return (host, conditionModifier) -> new Action(host, conditionModifier) {
            HolonomicDriveController controller;
            Trajectory trajectory;
            Function<Translation2d, Rotation2d> targetHeadingFromLocation;
            double startTimeSeconds;
            double startVelocity;

            @Override
            protected void initialize() {
                controller = new HolonomicDriveController(
                    new PIDController(LINEAR_KP, LINEAR_KI, LINEAR_KD),
                    new PIDController(LINEAR_KP, LINEAR_KI, LINEAR_KD),
                    new ProfiledPIDController(ROTATIONAL_KP, ROTATIONAL_KI, ROTATIONAL_KD, AUTO_ROTATION_MOTION_CONSTRAINTS)
                );

                startVelocity = Math.sqrt(Math.pow(hardware.drivetrain.getVelocityMetresAndRadiansPerSec().vxMetersPerSecond, 2) + Math.pow(hardware.drivetrain.getVelocityMetresAndRadiansPerSec().vyMetersPerSecond, 2));
                var robotPose = hardware.drivetrain.getPosition();
                Translation2d initialLocation = robotPose.getLocation().asRaw(Metres::convert);
                Translation2d targetLocation = target;
                Translation2d delta = target.minus(initialLocation);
                Rotation2d deltaDirection = new Rotation2d(delta.getX(), delta.getY());
                TrajectoryConfig trajectoryconfig = new TrajectoryConfig(maxVelocity.asRaw(MetresPerSec::convert), MAX_ACCELERATION_METRES_PER_SECOND_SQUARED);
                trajectoryconfig.setStartVelocity(startVelocity);
                trajectoryconfig.setKinematics(hardware.drivetrain.getKinematics());

                trajectory = TrajectoryGenerator.generateTrajectory(
                    new Pose2d(initialLocation, deltaDirection),
                    Collections.emptyList(),
                    new Pose2d(targetLocation, deltaDirection),
                    trajectoryconfig
                );

                startTimeSeconds = Timer.getFPGATimestamp();

                // Lerping logic
                var initialAngleRadians = robotPose.getHeading().getRadians();
                var workingFinalAngleRadians = targetAngle.getRadians();

                if(rotateAntiClockwise) {
                    while(workingFinalAngleRadians < initialAngleRadians) workingFinalAngleRadians += 2 * Math.PI;
                }
                else {
                    while(workingFinalAngleRadians > initialAngleRadians) workingFinalAngleRadians -= 2 * Math.PI;
                }

                final var finalAngleRadians = workingFinalAngleRadians;

                targetHeadingFromLocation = (position) -> {
                    var distanceFromStart = position.minus(initialLocation).getNorm();
                    var distanceFromEnd = position.minus(targetLocation).getNorm();

                    var percentageOfTravel = Math.min(distanceFromStart / (distanceFromStart + distanceFromEnd) + ROTATION_LERP_LEAD_PERCENTAGE, 1);

                    return Rotation2d.fromRadians(initialAngleRadians * (1 - percentageOfTravel) + finalAngleRadians * percentageOfTravel);
                };

                SmartDashboard.putNumberArray("Drivetrain Target", new double[] { targetLocation.getX(), targetLocation.getY() });
            }

            @Override
            protected void execute() {
                Pose2d currentPosition = hardware.drivetrain.getPosition().asRaw(Metres::convert);

                hardware.drivetrain.driveWithVelocity(controller.calculate(
                    currentPosition,
                    trajectory.sample(Timer.getFPGATimestamp() - startTimeSeconds),

                    targetHeadingFromLocation.apply(currentPosition.getTranslation())
                ));
            }

			@Override
			protected void end() {
				hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(0, 0, 0));
			}

			@Override
			protected boolean isFinished() {
                Pose2d currentPosition = hardware.drivetrain.getPosition().asRaw(Metres::convert);
                double timeElapsedSeconds = Timer.getFPGATimestamp() - startTimeSeconds;

				return target.minus(currentPosition.getTranslation()).getNorm() < POSITION_TOLERANCE_METRES
                    && Math.abs(targetHeadingFromLocation.apply(target).minus(currentPosition.getRotation()).getRadians()) < ROTATION_TOLERANCE_RADIANS
                    && timeElapsedSeconds >= trajectory.getTotalTimeSeconds();
			}
        };
    }

    public Untilable doPathTo(Trajectory trajectory, Function<Translation2d, Rotation2d> targetHeadingFromLocation) {
        return (host, conditionModifier) -> new Action(host, conditionModifier) {
            HolonomicDriveController controller;
            double startTimeSeconds;
            double endVelocityMetresPerSec;
            double endVelocityXMetresPerSec, endVelocityYMetresPerSec;

            @Override
            protected void initialize() {
                ProfiledPIDController rotationController = new ProfiledPIDController(ROTATIONAL_KP, ROTATIONAL_KI, ROTATIONAL_KD, AUTO_ROTATION_MOTION_CONSTRAINTS);
                rotationController.enableContinuousInput(-Math.PI, Math.PI);

                controller = new HolonomicDriveController(
                    new PIDController(LINEAR_KP, LINEAR_KI, LINEAR_KD),
                    new PIDController(LINEAR_KP, LINEAR_KI, LINEAR_KD),
                    rotationController
                );

                var states = trajectory.getStates();
                var lastState = states.get(states.size() - 1);
                double endAngleRadians = lastState.poseMeters.getRotation().getRadians();
                endVelocityMetresPerSec = lastState.velocityMetersPerSecond;
                endVelocityXMetresPerSec = Math.cos(endAngleRadians) * endVelocityMetresPerSec;
                endVelocityYMetresPerSec = Math.sin(endAngleRadians) * endVelocityMetresPerSec;

                startTimeSeconds = Timer.getFPGATimestamp();
            }

            @Override
            protected void execute() {
                Pose2d currentPosition = hardware.drivetrain.getPosition().asRaw(Metres::convert);

                hardware.drivetrain.driveWithVelocity(controller.calculate(
                    currentPosition,
                    trajectory.sample(Timer.getFPGATimestamp() - startTimeSeconds),
                    targetHeadingFromLocation.apply(currentPosition.getTranslation())
                ));
            }

			@Override
			protected void end() {
				hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(endVelocityXMetresPerSec, endVelocityYMetresPerSec, 0));
			}

			@Override
			protected boolean isFinished() {
                Pose2d currentPosition = hardware.drivetrain.getPosition().asRaw(Metres::convert);
                double timeElapsedSeconds = Timer.getFPGATimestamp() - startTimeSeconds;
                Translation2d target = trajectory.sample(timeElapsedSeconds).poseMeters.getTranslation();

				return target.minus(currentPosition.getTranslation()).getNorm() < POSITION_TOLERANCE_METRES * (endVelocityMetresPerSec == 0 ? 1 : 2)
                    && Math.abs(targetHeadingFromLocation.apply(target).minus(currentPosition.getRotation()).getRadians()) < ROTATION_TOLERANCE_RADIANS
                    && timeElapsedSeconds >= trajectory.getTotalTimeSeconds();
			}
        };
    }

    public Actionable doTurnTo(double targetAngleDegrees) {
		return doTurnTo(Rotation2d.fromDegrees(targetAngleDegrees));
	}

    public Actionable doTurnTo(Rotation2d targetAngle) {
		return (host) -> new Action(host) {
            ProfiledPIDController controller;

            @Override
            protected void initialize() {
                controller = new ProfiledPIDController(ROTATIONAL_KP, ROTATIONAL_KI, ROTATIONAL_KD, AUTO_ROTATION_MOTION_CONSTRAINTS);
                controller.enableContinuousInput(-Math.PI, Math.PI);
                controller.reset(hardware.drivetrain.getPosition().getHeading().getRadians());
                controller.setGoal(targetAngle.getRadians());
                controller.setTolerance(ROTATION_TOLERANCE_RADIANS);
            }

            @Override
            protected void execute() {
                hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(
                    0,
                    0,
                    controller.calculate(hardware.drivetrain.getPosition().getHeading().getRadians())
                ));
            }

			@Override
			protected void end() {
				hardware.drivetrain.driveWithVelocity(new ChassisSpeeds(0, 0, 0));
			}

			@Override
			protected boolean isFinished() {
				return Math.abs(targetAngle.minus(hardware.drivetrain.getPosition().getHeading()).getRadians()) < ROTATION_TOLERANCE_RADIANS;
			}
        };
	}

    public boolean isLevel() {
        return Math.abs(hardware.pigeon.getPitch()) < 5 && Math.abs(hardware.pigeon.getRoll()) < 5;
    }

    public TrajectoryConfig createTrajectoryConfig(Velocity maxVelocity) {
        return new TrajectoryConfig(maxVelocity.asRaw(MetresPerSec::convert), MAX_ACCELERATION_METRES_PER_SECOND_SQUARED)
            .setKinematics(hardware.drivetrain.getKinematics());
    }

    public TrajectoryConfig createTrajectoryConfig() {
        return createTrajectoryConfig(MAX_LINEAR_VELOCITY);
    }
}