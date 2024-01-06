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

public class AutoCenterChargeStation { static final Function<Robot, Actionable>
    RED = (robot) -> SEQUENCE(
        DO(()-> robot.drivetrain.setPosition(new Metres(gridDepth + ROBOT_OFFSET_X_FROM_EDGE), new Metres(red_column5Y), Rotation2d.fromDegrees(0))),
        //go to balance 
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_HIGH)),
        WAIT(new Seconds(2)),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT(new Seconds(2)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.cubeIntake.moveToPos(STOW_POS)),
        robot.drivetrain.doMoveTo(new Translation2d(gridDepth + ROBOT_OFFSET_X_FROM_EDGE + 0.5, red_column5Y), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doTurnTo(90),
        robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner1X - ROBOT_OFFSET_X_FROM_EDGE, (red_chargeStationCorner1Y + red_chargeStationCorner4Y)/2), Rotation2d.fromDegrees(90)),
        //balance
        robot.drivetrain.doGetOntoBalanceFacingY(Rotation2d.fromDegrees(90), false, 3.5),
        robot.drivetrain.doBalanceToFacingY(Rotation2d.fromDegrees(90))

        // // go outside community 
        // 	.UNSAFE_UNTIL(robot.drivetrain::isLevel),
        // robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner3X + 4, (red_chargeStationCorner1Y + red_chargeStationCorner4Y)/2), Rotation2d.fromDegrees(90), new MetresPerSec(0.75)),
        // WAIT(new Seconds(2)),
        // // go onto charge station
        // robot.drivetrain.doGetOntoBalance(Rotation2d.fromDegrees(90), true, 3.5),
        // robot.drivetrain.doBalanceTo(Rotation2d.fromDegrees(90))
    ),

    BLUE = (robot) -> SEQUENCE(
        DO(()-> robot.drivetrain.setPosition(new Metres(gridDepth + ROBOT_OFFSET_X_FROM_EDGE), new Metres(blue_column5Y), Rotation2d.fromDegrees(0))),
        //go to balance 
        DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_HIGH)),
        WAIT(new Seconds(2)),
        DO(()-> robot.cubeIntake.shoot()),
        WAIT(new Seconds(2)),
        DO(()-> robot.cubeIntake.stop()),
        DO(()-> robot.cubeIntake.moveToPos(STOW_POS)),
        robot.drivetrain.doMoveTo(new Translation2d(gridDepth + ROBOT_OFFSET_X_FROM_EDGE + 0.5, blue_column5Y), Rotation2d.fromDegrees(0)),
        robot.drivetrain.doTurnTo(-90),
        robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner1X - ROBOT_OFFSET_X_FROM_EDGE, (blue_chargeStationCorner1Y + blue_chargeStationCorner4Y)/2), Rotation2d.fromDegrees(-90)),
        //balance
        robot.drivetrain.doGetOntoBalanceFacingY(Rotation2d.fromDegrees(-90), true, 3.5),
        robot.drivetrain.doBalanceToFacingY(Rotation2d.fromDegrees(-90))

        // // go outside community 
        // 	.UNSAFE_UNTIL(robot.drivetrain::isLevel),
        // robot.drivetrain.doMoveTo(new Translation2d(chargeStationCorner3X + 4, (blue_chargeStationCorner1Y + blue_chargeStationCorner4Y)/2), Rotation2d.fromDegrees(-90), new MetresPerSec(0.75)),
        // WAIT(new Seconds(2)),s
        // // go onto charge station
        // robot.drivetrain.doGetOntoBalance(Rotation2d.fromDegrees(-90), false, 3.5),
        // robot.drivetrain.doBalanceTo(Rotation2d.fromDegrees(-90))
    );
}
