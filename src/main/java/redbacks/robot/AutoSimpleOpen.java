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
import arachne4.lib.units.Seconds;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

public class AutoSimpleOpen { static final Function<Robot, Actionable>
    RED_NO_BALANCE_3GP_LMH = (robot) -> SEQUENCE(
        DO(()-> robot.drivetrain.setPosition(new Metres(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE), new Metres(red_column7Y), Rotation2d.fromDegrees(0))),
        // //score preload cube + drive to 4GP
        DO(()-> robot.cubeIntake.intakeInAuto()),
        robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX-ROBOT_OFFSET_X_FROM_EDGE, red_communityEdgeY + ROBOT_OFFSET_Y_FROM_EDGE + 0.25), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + ROBOT_OFFSET_X_FROM_EDGE + 0.7, red_gamePiece4Y - ROBOT_OFFSET_Y_FROM_EDGE - 0.1), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + ROBOT_OFFSET_X_FROM_EDGE + 0.7, red_gamePiece4Y + 0.35), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doMoveTo(new Translation2d(gridDepth + ROBOT_OFFSET_X_FROM_EDGE + 0.1, red_column8Y), Rotation2d.fromDegrees(0))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_HIGH)),
                WAIT(new Seconds(0.3)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        WAIT(new Seconds(0.5)),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.3)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + ROBOT_OFFSET_X_FROM_EDGE + 0.7,red_gamePiece4Y), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece3X + ROBOT_OFFSET_X_FROM_EDGE + 0.7,red_gamePiece3Y + 0.45 + 0.23), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX + ROBOT_OFFSET_X_FROM_EDGE + 0.7,red_communityEdgeY+ROBOT_OFFSET_Y_FROM_EDGE+0.35), Rotation2d.fromDegrees(0))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                WAIT(new Seconds(0.2)),
                DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
                WAIT(new Seconds(0.3)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_MID)),
        // robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE + 0.3, red_column9Y + 0.05), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE + 0.28, red_column8Y + 0.1), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.5)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
        robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX-ROBOT_OFFSET_X_FROM_EDGE,red_communityEdgeY+ROBOT_OFFSET_Y_FROM_EDGE+0.25), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + 1.15, red_communityEdgeY+ROBOT_OFFSET_Y_FROM_EDGE+0.25), Rotation2d.fromDegrees(0))
        ), 

    RED_NO_BALANCE_3GP = (robot) -> SEQUENCE(
    doRedFirstCubeOpen(robot, new Metres(red_column7Y)),
    DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
    robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + 1.15,red_gamePiece4Y), Rotation2d.fromDegrees(0)),
    robot.drivetrain.doMoveTo(new Translation2d(gamePiece3X + 1.15,red_gamePiece3Y + 0.38), Rotation2d.fromDegrees(0)),
    DO(()-> robot.cubeIntake.intakeInAuto()),
    SPLIT(
        robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX + ROBOT_OFFSET_X_FROM_EDGE + 0.7,red_communityEdgeY+ROBOT_OFFSET_Y_FROM_EDGE+0.05), Rotation2d.fromDegrees(0))
    ).AND(
        SEQUENCE(
            WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
            WAIT(new Seconds(0.2)),
            DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
            WAIT(new Seconds(0.3)),
            DO(() -> robot.cubeIntake.stop())
        )
    ),
    DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_SLOW_LOW)),
    robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE + 0.3, red_column9Y + 0.05), Rotation2d.fromDegrees(0)),
    robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE + 0.42, red_column8Y + 0.25), Rotation2d.fromDegrees(0)),
    DO(()-> robot.cubeIntake.shoot()),
    WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
    WAIT(new Seconds(0.3)),
    DO(()-> robot.cubeIntake.stop()),
    DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
    robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX - ROBOT_OFFSET_X_FROM_EDGE, red_communityEdgeY + ROBOT_OFFSET_Y_FROM_EDGE + 0.25), Rotation2d.fromDegrees(0)),
    robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + 1.15, red_communityEdgeY + ROBOT_OFFSET_Y_FROM_EDGE + 0.25), Rotation2d.fromDegrees(0))
    ), 
    
    RED_WITH_BALANCE_3GP = (robot) -> SEQUENCE(
        doRedFirstCubeOpen(robot, new Metres(red_column7Y)),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + 1.15,red_gamePiece4Y), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece3X + 1.15,red_gamePiece3Y + 0.3), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX + ROBOT_OFFSET_X_FROM_EDGE + 0.7,red_communityEdgeY+ROBOT_OFFSET_Y_FROM_EDGE+0.35), Rotation2d.fromDegrees(0))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                WAIT(new Seconds(0.2)),
                DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
                WAIT(new Seconds(0.3)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_SLOW_LOW)),
        robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE + 0.3, red_column9Y + 0.05), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE + 0.48, red_column8Y + 0.05), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.3)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
        robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX-ROBOT_OFFSET_X_FROM_EDGE,red_communityEdgeY+ROBOT_OFFSET_Y_FROM_EDGE+0.25), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + 1.15, red_communityEdgeY+ROBOT_OFFSET_Y_FROM_EDGE+0.25), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner4X-ROBOT_OFFSET_X_FROM_EDGE, red_chargeStationCorner4Y+ROBOT_OFFSET_Y_FROM_EDGE+0.3), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), false, 3.5)
            .UNSAFE_UNTIL((onBalance) -> onBalance || doesTimeSinceAutoStartExceed(new Seconds(14))),
        robot.drivetrain.doBalanceToFacingX(Rotation2d.fromDegrees(0))   
    ), 

    BLUE_NO_BALANCE_3GP = (robot) -> SEQUENCE(
    doBlueFirstCubeOpen(robot, new Metres(blue_column7Y)),
    DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
    robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + 1.15,blue_gamePiece4Y), Rotation2d.fromDegrees(0)),
    robot.drivetrain.doMoveTo(new Translation2d(gamePiece3X + 1.15,blue_gamePiece3Y - 0.35), Rotation2d.fromDegrees(0)),
    DO(()-> robot.cubeIntake.intakeInAuto()),
    SPLIT(
        robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX + ROBOT_OFFSET_X_FROM_EDGE + 0.7,blue_communityEdgeY-ROBOT_OFFSET_Y_FROM_EDGE-0.05), Rotation2d.fromDegrees(0))
    ).AND(
        SEQUENCE(
            WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
            WAIT(new Seconds(0.2)),
            DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
            WAIT(new Seconds(0.3)),
            DO(() -> robot.cubeIntake.stop())
        )
    ),
    DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_SLOW_LOW)),
    robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE + 0.3, blue_column9Y - 0.05), Rotation2d.fromDegrees(0)),
    robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE + 0.42,blue_column8Y - 0.05), Rotation2d.fromDegrees(0)),
    DO(()-> robot.cubeIntake.shoot()),
    WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
    WAIT(new Seconds(0.3)),
    DO(()-> robot.cubeIntake.stop()),
    DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
    robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX - ROBOT_OFFSET_X_FROM_EDGE, blue_communityEdgeY - ROBOT_OFFSET_Y_FROM_EDGE - 0.25), Rotation2d.fromDegrees(0)),
    robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + 1.15, blue_communityEdgeY - ROBOT_OFFSET_Y_FROM_EDGE - 0.25), Rotation2d.fromDegrees(0))
    ), 

    BLUE_WITH_BALANCE_3GP = (robot) -> SEQUENCE(
        doBlueFirstCubeOpen(robot, new Metres(blue_column7Y)),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X+0.3,blue_gamePiece4Y), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gamePiece3X+0.3,blue_gamePiece3Y-0.15), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.intakeInAuto()),
        SPLIT(
            robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX+ROBOT_OFFSET_X_FROM_EDGE+0.7,blue_communityEdgeY-ROBOT_OFFSET_Y_FROM_EDGE-0.05), Rotation2d.fromDegrees(0))
        ).AND(
            SEQUENCE(
                WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                WAIT(new Seconds(0.2)),
                DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
                WAIT(new Seconds(0.3)),
                DO(() -> robot.cubeIntake.stop())
            )
        ),
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_SLOW_LOW)),
        robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE+0.18,blue_column9Y-0.05), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doMoveTo(new Translation2d(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE+0.48,blue_column8Y-0.05), Rotation2d.fromDegrees(0)),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
        WAIT(new Seconds(0.3)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
        robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner4X-ROBOT_OFFSET_X_FROM_EDGE, blue_chargeStationCorner4Y-ROBOT_OFFSET_Y_FROM_EDGE-0.2), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), false, 3.5)
            .UNSAFE_UNTIL((onBalance) -> onBalance || doesTimeSinceAutoStartExceed(new Seconds(14))),
        robot.drivetrain.doBalanceToFacingX(Rotation2d.fromDegrees(0))   
    );

    //open actionables
    private static Actionable doRedFirstCubeOpen(Robot robot, Distance startingColumn) {
        return SEQUENCE(
            DO(()-> robot.drivetrain.setPosition(new Metres(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE), startingColumn, Rotation2d.fromDegrees(0))),
            // //score preload cube + drive to 4GP
            DO(()-> robot.cubeIntake.intakeInAuto()),
            robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX-ROBOT_OFFSET_X_FROM_EDGE, red_communityEdgeY + ROBOT_OFFSET_Y_FROM_EDGE + 0.25), Rotation2d.fromDegrees(0)),
            DO(()-> robot.cubeIntake.stop()),
            DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + ROBOT_OFFSET_X_FROM_EDGE + 0.65, red_gamePiece4Y - ROBOT_OFFSET_Y_FROM_EDGE - 0.1), Rotation2d.fromDegrees(0)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + ROBOT_OFFSET_X_FROM_EDGE + 0.65, red_gamePiece4Y + 0.35), Rotation2d.fromDegrees(0)),
            DO(()-> robot.cubeIntake.intakeInAuto()),
            SPLIT(
                robot.drivetrain.doMoveTo(new Translation2d(gridDepth + ROBOT_OFFSET_X_FROM_EDGE + 0.1, red_column9Y), Rotation2d.fromDegrees(0))
            ).AND(
                SEQUENCE(
                    WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                    DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_LOW)),
                    WAIT(new Seconds(0.3)),
                    DO(() -> robot.cubeIntake.stop())
                )
            ),
            DO(()-> robot.cubeIntake.shoot()),
            WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
            WAIT(new Seconds(0.3)),
            DO(()-> robot.cubeIntake.stop())
        );
    }

    private static Actionable doBlueFirstCubeOpen(Robot robot, Distance startingColumn) {
        return SEQUENCE(
            DO(()-> robot.drivetrain.setPosition(new Metres(gridEdgeX + ROBOT_OFFSET_X_FROM_EDGE), startingColumn, Rotation2d.fromDegrees(0))),
            // //score preload cube + drive to 4GP
            DO(()-> robot.cubeIntake.intakeInAuto()),
            robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX-ROBOT_OFFSET_X_FROM_EDGE, blue_communityEdgeY - ROBOT_OFFSET_Y_FROM_EDGE - 0.25), Rotation2d.fromDegrees(0)),
            DO(()-> robot.cubeIntake.stop()),
            DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + ROBOT_OFFSET_X_FROM_EDGE + 0.5, blue_gamePiece4Y + ROBOT_OFFSET_Y_FROM_EDGE + 0.1), Rotation2d.fromDegrees(0)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X + ROBOT_OFFSET_X_FROM_EDGE + 0.5, blue_gamePiece4Y - 0.3), Rotation2d.fromDegrees(0)),
            DO(()-> robot.cubeIntake.intakeInAuto()),
            SPLIT(
                robot.drivetrain.doMoveTo(new Translation2d(gridDepth + ROBOT_OFFSET_X_FROM_EDGE + 0.1, blue_column9Y), Rotation2d.fromDegrees(0))
            ).AND(
                SEQUENCE(
                    WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
                    DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_LOW)),
                    WAIT(new Seconds(0.3)),
                    DO(() -> robot.cubeIntake.stop())
                )
            ),
            DO(()-> robot.cubeIntake.shoot()),
            WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
            WAIT(new Seconds(0.3)),
            DO(()-> robot.cubeIntake.stop())
        );
    }

    protected static Actionable doRedThirdCubeAndBalance(Robot robot) {
        return SEQUENCE(
            DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X + 1.15, red_gamePiece1Y), Rotation2d.fromDegrees(0)),
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X + 1.15, red_gamePiece2Y), Rotation2d.fromDegrees(0)),

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
