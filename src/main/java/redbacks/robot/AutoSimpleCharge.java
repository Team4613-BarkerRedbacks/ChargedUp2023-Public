package redbacks.robot;

import static arachne4.lib.sequences.Actionable.*;
import static redbacks.field.FieldLocations.*;
import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;
import static redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationConstants.*;
import static redbacks.robot.Auto.doesTimeSinceAutoStartExceed;

import java.util.function.Function;

import arachne4.lib.sequences.Actionable;
import arachne4.lib.units.Metres;
import arachne4.lib.units.MetresPerSec;
import arachne4.lib.units.Seconds;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

public class AutoSimpleCharge { static final Function<Robot, Actionable>
    RED_CENTER_1_PIECE_MOBILITY = (robot) -> centerAuto(robot, red_column5Y),
    BLUE_CENTER_1_PIECE_MOBILITY = (robot) -> centerAuto(robot, blue_column5Y),
    RED_BARRIER_SIDE = (robot) -> SEQUENCE(
            DO(() -> robot.drivetrain.setPosition(new Metres(gridDepth + ROBOT_OFFSET_X_FROM_EDGE), new Metres(red_column4Y), Rotation2d.fromDegrees(0))),
            //go over the charge station
            DO(()-> robot.cubeIntake.intakeInAuto()),
            robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner1X - ROBOT_OFFSET_X_FROM_EDGE, red_column5Y), Rotation2d.fromDegrees(0)), 
            DO(()-> robot.cubeIntake.stop()),
            robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), true, 5.3), 
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X  + ROBOT_OFFSET_X_FROM_EDGE + 0.25 + CHARGE_STATION_X_LOSS, (red_gamePiece2Y + red_gamePiece3Y)/2), Rotation2d.fromDegrees(0), new MetresPerSec(2)), 
            DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
            //go to game piece
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X + CHARGE_STATION_X_LOSS + 0.25, red_gamePiece2Y), Rotation2d.fromDegrees(0)), 
            DO(()-> robot.cubeIntake.intakeInAuto()),
            SPLIT(
                robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner3X + ROBOT_OFFSET_X_FROM_EDGE + 0.2, red_gamePiece2Y), Rotation2d.fromDegrees(0))
            ).AND(
                SEQUENCE(
                    WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.getPosition().getLocation().getX().isLessThan(new Metres(gamePiece1X - 0.2 + CHARGE_STATION_X_LOSS))),
                    WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) + CHARGE_STATION_X_LOSS < gamePiece1X - 0.5),
                    DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),
                    WAIT(new Seconds(0.3)),
                    DO(() -> robot.cubeIntake.stop())
                )
            ),
            robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner2X+ ROBOT_OFFSET_X_FROM_EDGE + CHARGE_STATION_X_LOSS, red_column5Y), Rotation2d.fromDegrees(0)), 
            //balance and shoot
            robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), false, 3.5)
                .UNSAFE_UNTIL((onBalance) -> onBalance || doesTimeSinceAutoStartExceed(new Seconds(14))),
            SPLIT(
                SEQUENCE(
                    DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT_FROM_CHARGE_STATION)),
                    WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.isLevel() || doesTimeSinceAutoStartExceed(new Seconds(14.5))),
                    DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT_FROM_CHARGE_STATION)),
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
    
        BLUE_BARRIER_SIDE = (robot) -> SEQUENCE(
            DO(() -> robot.drivetrain.setPosition(new Metres(gridDepth + ROBOT_OFFSET_X_FROM_EDGE), new Metres(blue_column4Y), Rotation2d.fromDegrees(0))),
            //go over the charge station
            DO(()-> robot.cubeIntake.intakeInAuto()),
            robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner1X - ROBOT_OFFSET_X_FROM_EDGE, blue_column5Y), Rotation2d.fromDegrees(0)), 
            DO(()-> robot.cubeIntake.stop()),
            robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), true, 5.3), 
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X  + ROBOT_OFFSET_X_FROM_EDGE + 0.25 + CHARGE_STATION_X_LOSS, (blue_gamePiece2Y + blue_gamePiece3Y)/2), Rotation2d.fromDegrees(0), new MetresPerSec(2)), 
            DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
            DO(()-> robot.cubeIntake.intakeInAuto()),
            //go to game piece
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X + CHARGE_STATION_X_LOSS + 0.25, blue_gamePiece2Y), Rotation2d.fromDegrees(0)), 
            SPLIT(robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner3X + ROBOT_OFFSET_X_FROM_EDGE + 0.2, red_gamePiece2Y), Rotation2d.fromDegrees(0))
            ).AND(
                SEQUENCE(
                    WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.getPosition().getLocation().getX().isLessThan(new Metres(gamePiece1X - 0.2 + CHARGE_STATION_X_LOSS))),
                    WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) + CHARGE_STATION_X_LOSS < gamePiece1X - 0.5),
                    DO(()-> robot.armAndCubeCoordination.goToPositions(AUTO_CUBE_FLOOR_SHOT)),
                    WAIT(new Seconds(0.3)),
                    DO(() -> robot.cubeIntake.stop())
                )
            ),
            robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner2X+ ROBOT_OFFSET_X_FROM_EDGE + CHARGE_STATION_X_LOSS, blue_column5Y), Rotation2d.fromDegrees(0)), 
            //balance and shoot
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

        RED_OPEN_SIDE = (robot) -> SEQUENCE(
            DO(() -> robot.drivetrain.setPosition(new Metres(gridDepth + ROBOT_OFFSET_X_FROM_EDGE), new Metres(red_column6Y), Rotation2d.fromDegrees(0))),
            //go over the charge station
            DO(()-> robot.cubeIntake.intakeInAuto()),
            robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner1X - ROBOT_OFFSET_X_FROM_EDGE, red_column5Y), Rotation2d.fromDegrees(0)), 
            DO(()-> robot.cubeIntake.stop()),
            robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), true, 5.3), 
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X  + ROBOT_OFFSET_X_FROM_EDGE + 0.25 + CHARGE_STATION_X_LOSS, (red_gamePiece2Y + red_gamePiece3Y)/2), Rotation2d.fromDegrees(0), new MetresPerSec(2)), 
            DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
            DO(()-> robot.cubeIntake.intakeInAuto()),
            //go to game piece
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece3X + CHARGE_STATION_X_LOSS + 0.25, red_gamePiece3Y), Rotation2d.fromDegrees(0)), 
            SPLIT(robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner3X + ROBOT_OFFSET_X_FROM_EDGE + 0.2, red_gamePiece3Y), Rotation2d.fromDegrees(0))
            ).AND(
                SEQUENCE(
                    WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.getPosition().getLocation().getX().isLessThan(new Metres(gamePiece1X - 0.2 + CHARGE_STATION_X_LOSS))),
                    WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) + CHARGE_STATION_X_LOSS < gamePiece1X - 0.5),
                    DO(()-> robot.armAndCubeCoordination.goToPositions(AUTO_CUBE_FLOOR_SHOT)),
                    WAIT(new Seconds(0.3)),
                    DO(() -> robot.cubeIntake.stop())
                )
            ),
            robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner2X+ ROBOT_OFFSET_X_FROM_EDGE + CHARGE_STATION_X_LOSS, red_column5Y), Rotation2d.fromDegrees(0)), 
            //balance and shoot
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

        BLUE_OPEN_SIDE = (robot) -> SEQUENCE(
            DO(() -> robot.drivetrain.setPosition(new Metres(gridDepth + ROBOT_OFFSET_X_FROM_EDGE), new Metres(blue_column6Y), Rotation2d.fromDegrees(0))),
            //go over the charge station
            DO(()-> robot.cubeIntake.intakeInAuto()),
            robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner1X - ROBOT_OFFSET_X_FROM_EDGE, blue_column5Y), Rotation2d.fromDegrees(0)), 
            DO(()-> robot.cubeIntake.stop()),
            robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), true, 5.3), 
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X  + ROBOT_OFFSET_X_FROM_EDGE + 0.25 + CHARGE_STATION_X_LOSS, (blue_gamePiece2Y + blue_gamePiece3Y)/2), Rotation2d.fromDegrees(0), new MetresPerSec(2)), 
            DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
            DO(()-> robot.cubeIntake.intakeInAuto()),
            //go to game piece
            robot.drivetrain.doMoveTo(new Translation2d(gamePiece3X + CHARGE_STATION_X_LOSS + 0.25, blue_gamePiece3Y), Rotation2d.fromDegrees(0)), 
            SPLIT(robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner3X + ROBOT_OFFSET_X_FROM_EDGE + 0.2, blue_gamePiece3Y), Rotation2d.fromDegrees(0))
            ).AND(
                SEQUENCE(
                    WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.getPosition().getLocation().getX().isLessThan(new Metres(gamePiece1X - 0.2 + CHARGE_STATION_X_LOSS))),
                    WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) + CHARGE_STATION_X_LOSS < gamePiece1X - 0.5),
                    DO(()-> robot.armAndCubeCoordination.goToPositions(AUTO_CUBE_FLOOR_SHOT)),
                    WAIT(new Seconds(0.3)),
                    DO(() -> robot.cubeIntake.stop())
                )
            ),
            robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner2X+ ROBOT_OFFSET_X_FROM_EDGE + CHARGE_STATION_X_LOSS, blue_column5Y), Rotation2d.fromDegrees(0)), 
            //balance and shoot
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


    private static Actionable centerAuto(Robot robot, double initialYMetres) {
        return SEQUENCE(
            DO(() -> robot.drivetrain.setPosition(new Metres(gridDepth + ROBOT_OFFSET_X_FROM_EDGE), new Metres(initialYMetres), Rotation2d.fromDegrees(0))),
            
            // Shoot cube
            DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_HIGH)),
            WAIT(new Seconds(1)),
            DO(()-> robot.cubeIntake.shoot()),
            WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
            WAIT(new Seconds(0.5)),
            DO(() -> robot.cubeIntake.stop()),
            DO(()-> robot.armAndCubeCoordination.goToPositions(STOW_ALL)),

            // Cross balance
            robot.drivetrain.doMoveTo(new Translation2d(gridDepth + ROBOT_OFFSET_X_FROM_EDGE + 0.5, initialYMetres), Rotation2d.fromDegrees(0)),
            SEQUENCE(
                robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), true, 3.5),
                robot.drivetrain.doBalanceToFacingX(Rotation2d.fromDegrees(0)).UNSAFE_UNTIL(robot.drivetrain::isLevel),
                // Reset position on top of balance
                DO(() -> robot.drivetrain.setPosition(new Metres((chargeStationCorner1X + chargeStationCorner2X) / 2), new Metres(initialYMetres), Rotation2d.fromDegrees(0))),
                robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner2X + ROBOT_OFFSET_X_FROM_EDGE + 0.5, initialYMetres), Rotation2d.fromDegrees(0))
            ).UNSAFE_UNTIL((finished) -> finished || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) > gamePiece1X),

            // Re-balance
            robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), false, 3.5),
            robot.drivetrain.doBalanceToFacingX(Rotation2d.fromDegrees(0))
        );
    }
}
