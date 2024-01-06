package redbacks.robot;

import static arachne4.lib.sequences.Actionable.*;
import static redbacks.field.FieldLocations.*;
import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;
import static redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationConstants.*;
import static redbacks.robot.Auto.doesTimeSinceAutoStartExceed;

import java.util.function.Function;

import arachne4.lib.sequences.Actionable;
import arachne4.lib.units.Metres;
import arachne4.lib.units.Seconds;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import redbacks.field.Trajectories;

public class OldAuto3CubeShootBalance { static final Function<Robot, Actionable>
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
            robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX - ROBOT_OFFSET_X_FROM_EDGE -0.25, red_chargeStationCorner2Y + ROBOT_OFFSET_Y_FROM_EDGE + 0.05), Rotation2d.fromDegrees(5))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
                WAIT(new Seconds(0.2)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        DO(()-> robot.cubeIntake.shoot()),
        // Collect second cube
        SPLIT(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
                WAIT(new Seconds(0.5)),
                DO(robot.cubeIntake::stop),
                DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND))
            )
        ).AND(
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8, red_chargeStationCorner2Y + ROBOT_OFFSET_Y_FROM_EDGE + 0.05), Rotation2d.fromDegrees(0))
        ),
        // Collect second cube
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X + 0.8, red_gamePiece2Y), Rotation2d.fromDegrees(0)),
        DO(() -> robot.cubeIntake.intakeInAuto()),
        // Move to balance
        SPLIT(
            robot.drivetrain.doMoveTo(
                new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE, red_gamePiece2Y),
                Rotation2d.fromDegrees(0)
            )
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
                WAIT(new Seconds(0.2)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        // Balance and shoot
        robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), false, 3.5)
            .UNSAFE_UNTIL((onBalance) -> onBalance || doesTimeSinceAutoStartExceed(new Seconds(14))),
        SPLIT(
            SEQUENCE(
                DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT_FROM_CHARGE_STATION)),
                WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.isLevel() || doesTimeSinceAutoStartExceed(new Seconds(14.5))),
                WAIT(new Seconds(0.5)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || doesTimeSinceAutoStartExceed(new Seconds(14.5))),
                DO(() -> robot.cubeIntake.shoot()),
                WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
                WAIT(new Seconds(0.5)),
                DO(() -> robot.cubeIntake.stop())
            )
        ).AND(
            robot.drivetrain.doBalanceToFacingX(Rotation2d.fromDegrees(0))
        )
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
            robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX - ROBOT_OFFSET_X_FROM_EDGE -0.25, blue_chargeStationCorner2Y - ROBOT_OFFSET_Y_FROM_EDGE - 0.05), Rotation2d.fromDegrees(-5))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
                WAIT(new Seconds(0.2)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        DO(()-> robot.cubeIntake.shoot()),
        // Collect second cube
        SPLIT(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
                WAIT(new Seconds(0.5)),
                DO(robot.cubeIntake::stop),
                DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND))
            )
        ).AND(
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8, blue_chargeStationCorner2Y - ROBOT_OFFSET_Y_FROM_EDGE - 0.05), Rotation2d.fromDegrees(0))
        ),
        // Collect second cube
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X + 0.8, blue_gamePiece2Y), Rotation2d.fromDegrees(0)),
        DO(() -> robot.cubeIntake.intakeInAuto()),
        // Move to balance
        SPLIT(
            robot.drivetrain.doMoveTo(
                new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE, blue_gamePiece2Y),
                Rotation2d.fromDegrees(0)
            )
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
                WAIT(new Seconds(0.2)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        // Balance and shoot
        robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), false, 3.5)
            .UNSAFE_UNTIL((onBalance) -> onBalance || doesTimeSinceAutoStartExceed(new Seconds(14))),
        SPLIT(
            SEQUENCE(
                DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT_FROM_CHARGE_STATION)),
                WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.isLevel() || doesTimeSinceAutoStartExceed(new Seconds(14.5))),
                WAIT(new Seconds(0.5)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || doesTimeSinceAutoStartExceed(new Seconds(14.5))),
                DO(() -> robot.cubeIntake.shoot()),
                WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
                WAIT(new Seconds(0.5)),
                DO(() -> robot.cubeIntake.stop())
            )
        ).AND(
            robot.drivetrain.doBalanceToFacingX(Rotation2d.fromDegrees(0))
        )
    );
}
