package redbacks.robot;

import static arachne4.lib.sequences.Actionable.*;
import static redbacks.field.FieldLocations.*;
import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;
import static redbacks.robot.subsystems.cubeIntake.CubeIntakeConstants.*;
import static redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationConstants.*;

import java.util.function.Function;

import arachne4.lib.sequences.Actionable;
import arachne4.lib.units.Metres;
import arachne4.lib.units.Seconds;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import redbacks.field.Trajectories;

public class Auto4Cubes { static final Function<Robot, Actionable>
    RED = (robot) -> SEQUENCE(
        DO(()-> robot.drivetrain.setPosition(new Metres(communityLongEdgeX - ROBOT_OFFSET_X_FROM_EDGE), new Metres(-ROBOT_OFFSET_Y_FROM_EDGE - 0.05), Rotation2d.fromDegrees(0))),
        // //score preload cube + drive to 4GP
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
        WAIT().UNSAFE_UNTIL(robot.cubeIntake::isOnTarget),
        WAIT(new Seconds(0.2)),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.2)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doPathTo(Trajectories.goToGP1_RED, (pos) -> Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE, red_chargeStationCorner2Y), Rotation2d.fromDegrees(0))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
                WAIT(new Seconds(0.2)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.2)),
        DO(()-> robot.cubeIntake.stop()),
        // go to 2GP
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8, red_chargeStationCorner2Y), Rotation2d.fromDegrees(0)),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8, red_chargeStationCorner2Y - 0.90), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE, red_chargeStationCorner2Y), Rotation2d.fromDegrees(0))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
                WAIT(new Seconds(0.2)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.2)),
        DO(()-> robot.cubeIntake.stop()),
        // go to 3GP
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doPathTo(Trajectories.barrier3GP_RED, (pos) -> Rotation2d.fromDegrees(0)),
        
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE -0.05, red_chargeStationCorner3Y), Rotation2d.fromDegrees(0))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
                WAIT(new Seconds(0.2)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.2)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.cubeIntake.moveToPos(STOW_POS)),
        robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX + 0.3 + ROBOT_OFFSET_X_FROM_EDGE, red_chargeStationCorner3Y), Rotation2d.fromDegrees(0))
    ),

    BLUE = (robot) -> SEQUENCE(
        DO(()-> robot.drivetrain.setPosition(new Metres(communityLongEdgeX - ROBOT_OFFSET_X_FROM_EDGE), new Metres(ROBOT_OFFSET_Y_FROM_EDGE + 0.05), Rotation2d.fromDegrees(0))),
        // //score preload cube + drive to 4GP
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
        WAIT().UNSAFE_UNTIL(robot.cubeIntake::isOnTarget),
        WAIT(new Seconds(0.2)),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.2)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doPathTo(Trajectories.goToGP1_BLUE, (pos) -> Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE, blue_chargeStationCorner2Y), Rotation2d.fromDegrees(0))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
                WAIT(new Seconds(0.2)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.2)),
        DO(()-> robot.cubeIntake.stop()),
        // go to 2GP
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8, blue_chargeStationCorner2Y), Rotation2d.fromDegrees(0)),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8, blue_chargeStationCorner2Y + 0.90), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE, blue_chargeStationCorner2Y), Rotation2d.fromDegrees(0))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
                WAIT(new Seconds(0.2)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.2)),
        DO(()-> robot.cubeIntake.stop()),
        // go to 3GP
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doPathTo(Trajectories.barrier3GP_BLUE, (pos) -> Rotation2d.fromDegrees(0)),
        
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE - 0.05, blue_chargeStationCorner3Y), Rotation2d.fromDegrees(0))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
                WAIT(new Seconds(0.2)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.2)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.cubeIntake.moveToPos(STOW_POS)),
        robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX + 0.3 + ROBOT_OFFSET_X_FROM_EDGE, blue_chargeStationCorner3Y), Rotation2d.fromDegrees(0))
    );
}
