package redbacks.robot;

import static arachne4.lib.sequences.Actionable.*;
import static redbacks.field.FieldLocations.*;
import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;
import static redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationConstants.*;
import static redbacks.robot.Auto.doesTimeSinceAutoStartExceed;

import java.util.function.Function;

import arachne4.lib.sequences.Actionable;
import arachne4.lib.units.Distance;
import arachne4.lib.units.Metres;
import arachne4.lib.units.MetresPerSec;
import arachne4.lib.units.Seconds;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

public class AutoSimpleBarrier { static final Function<Robot, Actionable>
    RED_NO_BALANCE_3GP = (robot) -> SEQUENCE(
        doRedFirstTwoGP(robot, new Metres(red_column3Y)),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X+1,red_gamePiece1Y), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X+1,red_gamePiece2Y - 0.33), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doVelocityTo(new Translation2d(cableProtectorCenterX+ROBOT_OFFSET_X_FROM_EDGE+0.7, -ROBOT_OFFSET_Y_FROM_EDGE - 0.15), Rotation2d.fromDegrees(0), new MetresPerSec(1))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                WAIT(new Seconds(0.2)),
                DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
                WAIT(new Seconds(0.3)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        robot.drivetrain.doVelocityTo(new Translation2d(cableProtectorCenterX-ROBOT_OFFSET_X_FROM_EDGE, -ROBOT_OFFSET_Y_FROM_EDGE - 0.15), Rotation2d.fromDegrees(0), new MetresPerSec(1), new MetresPerSec(1)),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_SLOW_LOW)),
        robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE+0.3,red_column1Y + 0.1), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE+0.42,red_column2Y - 0.05), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.3)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
        robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX-ROBOT_OFFSET_X_FROM_EDGE, -ROBOT_OFFSET_Y_FROM_EDGE-0.25), Rotation2d.fromDegrees(0))
    ),
    
    RED_WITH_BALANCE_3GP = (robot) -> SEQUENCE(
        doRedFirstTwoGP(robot, new Metres(red_column3Y)),
        doRedThirdCubeAndBalance(robot)
    ),

    RED_4GP_NO_BALANCE = (robot) -> SEQUENCE(
        doRedFirstTwoGP(robot, new Metres(red_column7Y)), 
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X + 1, -ROBOT_OFFSET_Y_FROM_EDGE+0.04), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X + 1,red_gamePiece2Y - 0.05), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doVelocityTo(new Translation2d(cableProtectorCenterX+ROBOT_OFFSET_X_FROM_EDGE+0.7,-ROBOT_OFFSET_Y_FROM_EDGE+0.05), Rotation2d.fromDegrees(0), new MetresPerSec(1))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                WAIT(new Seconds(0.2)),
                DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
                WAIT(new Seconds(0.3)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece3X + 1, red_gamePiece2Y), Rotation2d.fromDegrees(0)), 
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece3X + 1, red_gamePiece3Y), Rotation2d.fromDegrees(0)), 
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner2X + ROBOT_OFFSET_X_FROM_EDGE, red_chargeStationCorner2Y - 1), Rotation2d.fromDegrees(0))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                WAIT(new Seconds(0.2)),
                DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
                WAIT(new Seconds(0.3)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece3X + 1, red_gamePiece3Y + 1), Rotation2d.fromDegrees(0))
    ),

    BLUE_NO_BALANCE_3GP = (robot) -> SEQUENCE(
        doBlueFirstTwoGP(robot, new Metres(blue_column3Y)),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X+0.8,blue_gamePiece1Y), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X+0.8,blue_gamePiece2Y + 0.2), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doVelocityTo(new Translation2d(cableProtectorCenterX+ROBOT_OFFSET_X_FROM_EDGE+0.7,ROBOT_OFFSET_Y_FROM_EDGE+0.15), Rotation2d.fromDegrees(0), new MetresPerSec(1))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                WAIT(new Seconds(0.2)),
                DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
                WAIT(new Seconds(0.4)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        robot.drivetrain.doVelocityTo(new Translation2d(cableProtectorCenterX-ROBOT_OFFSET_X_FROM_EDGE,ROBOT_OFFSET_Y_FROM_EDGE+0.15), Rotation2d.fromDegrees(0), new MetresPerSec(1), new MetresPerSec(1)),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_SLOW_LOW)),
        robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE+0.06,blue_column1Y-0.1), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE+0.10,blue_column2Y + 0.05), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.3)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
        robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX-ROBOT_OFFSET_X_FROM_EDGE,ROBOT_OFFSET_Y_FROM_EDGE+0.25), Rotation2d.fromDegrees(0))
    ),

    BLUE_WITH_BALANCE_3GP = (robot) -> SEQUENCE(
        doBlueFirstTwoGP(robot, new Metres(blue_column3Y)),
        doBlueThirdCubeAndBalance(robot)
    );

    /* Helper functions */

    //Closed actionables
    private static Actionable doRedFirstTwoGP(Robot robot, Distance startingColumn) {
        return SEQUENCE(
            DO(()-> robot.drivetrain.setPosition(new Metres(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE), startingColumn, Rotation2d.fromDegrees(0))),
            // //score preload cube + drive to 4GP
            DO(()-> robot.cubeIntake.intakeInAuto()),
            robot.drivetrain.doVelocityTo(new Translation2d(cableProtectorCenterX-ROBOT_OFFSET_X_FROM_EDGE, -ROBOT_OFFSET_Y_FROM_EDGE-0.25), Rotation2d.fromDegrees(0), new MetresPerSec(1)),
            DO(()-> robot.cubeIntake.stop()),
            robot.drivetrain.doVelocityTo(new Translation2d(cableProtectorCenterX+ROBOT_OFFSET_X_FROM_EDGE+0.7,-ROBOT_OFFSET_Y_FROM_EDGE-0.25), Rotation2d.fromDegrees(0), new MetresPerSec(1), new MetresPerSec(1)),
            DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X+1,-ROBOT_OFFSET_Y_FROM_EDGE+0.06), Rotation2d.fromDegrees(0)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X+1,red_gamePiece1Y - 0.23), Rotation2d.fromDegrees(0)),
            DO(()-> robot.cubeIntake.intakeInAuto()),
            SPLIT(
                robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX+ROBOT_OFFSET_X_FROM_EDGE+0.5, -ROBOT_OFFSET_Y_FROM_EDGE), Rotation2d.fromDegrees(0))
            ).AND(
                SEQUENCE(
                    WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                    DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LOW_LONG_SHOT)),
                    WAIT(new Seconds(0.4)),
                    DO(() -> robot.cubeIntake.stop())
                )
            ),
            DO(()-> robot.cubeIntake.shoot()),
            WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
            WAIT(new Seconds(0.2)),
            DO(()-> robot.cubeIntake.stop())
        );
    }

    private static Actionable doBlueFirstTwoGP(Robot robot, Distance startingColumn) {
        return SEQUENCE(
            DO(()-> robot.drivetrain.setPosition(new Metres(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE), startingColumn, Rotation2d.fromDegrees(0))),
            // //score preload cube + drive to 4GP
            DO(()-> robot.cubeIntake.intakeInAuto()),
            robot.drivetrain.doVelocityTo(new Translation2d(cableProtectorCenterX-ROBOT_OFFSET_X_FROM_EDGE,ROBOT_OFFSET_Y_FROM_EDGE+0.25), Rotation2d.fromDegrees(0), new MetresPerSec(1)),
            DO(()-> robot.cubeIntake.stop()),
            robot.drivetrain.doVelocityTo(new Translation2d(cableProtectorCenterX+ROBOT_OFFSET_X_FROM_EDGE+0.7,ROBOT_OFFSET_Y_FROM_EDGE+0.25), Rotation2d.fromDegrees(0), new MetresPerSec(1), new MetresPerSec(1)),
            DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X+0.8,ROBOT_OFFSET_Y_FROM_EDGE-0.06), Rotation2d.fromDegrees(0)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X+0.8,blue_gamePiece1Y + 0.28), Rotation2d.fromDegrees(0)),
            DO(()-> robot.cubeIntake.intakeInAuto()),
            SPLIT(
                robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX+ROBOT_OFFSET_X_FROM_EDGE+0.5,ROBOT_OFFSET_Y_FROM_EDGE), Rotation2d.fromDegrees(0))
            ).AND(
                SEQUENCE(
                    WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                    DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LOW_LONG_SHOT)),
                    WAIT(new Seconds(0.4)),
                    DO(() -> robot.cubeIntake.stop())
                )
            ),
            DO(()-> robot.cubeIntake.shoot()),
            WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
            WAIT(new Seconds(0.2)),
            DO(()-> robot.cubeIntake.stop())
        );
    }

    protected static Actionable doRedThirdCubeAndBalance(Robot robot) {
        return SEQUENCE(
            DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X + 1, red_gamePiece1Y), Rotation2d.fromDegrees(0)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X + 1, red_gamePiece2Y-0.10), Rotation2d.fromDegrees(0)),

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
                    WAIT(new Seconds(0.7)),
                    DO(() -> robot.cubeIntake.holdCube())
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

    protected static Actionable doBlueThirdCubeAndBalance(Robot robot) {
        return SEQUENCE(
            DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X+1, blue_gamePiece1Y), Rotation2d.fromDegrees(0)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X + 1, blue_gamePiece2Y), Rotation2d.fromDegrees(0)),

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
                    WAIT(new Seconds(0.7)),
                    DO(() -> robot.cubeIntake.holdCube())
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
    
}
