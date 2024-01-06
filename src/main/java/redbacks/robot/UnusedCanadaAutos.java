// package redbacks.robot;

// import static arachne4.lib.sequences.Actionable.*;
// import static redbacks.field.FieldLocations.*;
// import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;
// import static redbacks.robot.subsystems.cubeIntake.CubeIntakeConstants.*;
// import static redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationConstants.*;
// import arachne4.lib.sequences.Actionable;
// import arachne4.lib.units.Metres;
// import edu.wpi.first.math.geometry.Rotation2d;

// import arachne4.lib.units.MetresPerSec;
// import arachne4.lib.units.Seconds;
// import redbacks.field.Trajectories;
// import redbacks.robot.subsystems.arm.ArmConstants;
// import redbacks.robot.subsystems.drivetrain.DrivetrainConstants;
// import redbacks.robot.subsystems.drivetrain.components.Pipelines;
// import edu.wpi.first.math.geometry.Translation2d;

// import java.util.function.Consumer;
// import java.util.function.Function;

// public enum UnusedCanadaAutos {

// 	/* Open side autos */

// 	RED_OPEN((robot) ->
// 		SEQUENCE(
// 			DO(()-> robot.drivetrain.setPosition(new Metres(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE), new Metres(red_column8Y + 0.04), Rotation2d.fromDegrees(0))),
// 			//score preload cube + drive to 4GP
// 			DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_HIGH)),
// 			WAIT().UNSAFE_UNTIL(robot.cubeIntake::isOnTarget),
// 			WAIT(new Seconds(0.2)),
// 			DO(()-> robot.cubeIntake.shoot()),
// 			WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 			WAIT(new Seconds(0.2)),
// 			DO(()-> robot.cubeIntake.stop()),
// 			DO(()-> robot.cubeIntake.moveToPos(STOW_POS)),
// 			DO(() -> robot.arm.goToIntakePosition()),
// 			DO(() -> robot.coneIntake.autoIntake()),
// 			robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X - ROBOT_OFFSET_X_FROM_EDGE - 0.5, red_gamePiece4Y + 0.15), Rotation2d.fromDegrees(0), new MetresPerSec(5)),
// 			SPLIT(
// 				robot.drivetrain.doTrackTo(Pipelines.CONE, gamePiece4X - ROBOT_OFFSET_X_FROM_EDGE + 0.35, Rotation2d.fromDegrees(0), new MetresPerSec(1.5))
// 			).AND(
// 				SEQUENCE(
// 					// WAIT().UNSAFE_UNTIL(robot.coneIntake::hasCone),
// 					WAIT(new Seconds(2)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || robot.coneIntake.hasCone()),
// 					WAIT(new Seconds(1)),
// 					DO(() -> robot.coneIntake.hold())
// 				)
// 			),
// 			//intake + drive to column 9
// 			robot.drivetrain.doTurnTo(180),
// 			DO(() -> robot.arm.goToPosition(ArmConstants.MID_POLE_PREPARING_POSITION)),
// 			robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 1.5, red_column9Y + 0.25), Rotation2d.fromDegrees(180), new MetresPerSec(3)),
// 			SPLIT(
// 				robot.drivetrain.doTrackTo(Pipelines.REFLECTIVE_TAPE, gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.15, Rotation2d.fromDegrees(180), new MetresPerSec(1))
// 			).AND(
// 				SEQUENCE(
// 					WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < communityShortEdgeX - DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE),
// 					DO(robot.arm::goToMidGoalPosition)
// 				)
// 			),
// 			WAIT(new Seconds(0.3)),
// 			DO(() -> robot.coneIntake.autoOuttake()),
// 			WAIT().UNSAFE_UNTIL(() -> !robot.coneIntake.hasCone()),
// 			WAIT(new Seconds(0.2)),
// 			DO(() -> robot.coneIntake.stop()),
// 			SPLIT(
// 				robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.7, red_column5Y - 0.10), Rotation2d.fromDegrees(90))
// 			).AND(
// 				SEQUENCE(
// 					WAIT().UNSAFE_UNTIL(() -> Math.abs(robot.drivetrain.getPosition().getHeading().getDegrees()) < 120),
// 					DO(robot.arm::goToStowPosition)
// 				)
// 			),
// 			robot.drivetrain.doGetOntoBalance(Rotation2d.fromDegrees(90), false, 3.5),
// 			robot.drivetrain.doBalanceTo(Rotation2d.fromDegrees(90))
// 		)
// 	),
// 	BLUE_OPEN((robot) ->
// 		SEQUENCE(
// 			DO(()-> robot.drivetrain.setPosition(new Metres(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE), new Metres(blue_column8Y - 0.04), Rotation2d.fromDegrees(0))),
// 			//score preload cube + drive to 4GP
// 			DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_HIGH)),
// 			WAIT().UNSAFE_UNTIL(robot.cubeIntake::isOnTarget),
// 			WAIT(new Seconds(0.2)),
// 			DO(()-> robot.cubeIntake.shoot()),
// 			WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 			WAIT(new Seconds(0.2)),
// 			DO(()-> robot.cubeIntake.stop()),
// 			DO(()-> robot.cubeIntake.moveToPos(STOW_POS)),
// 			DO(() -> robot.arm.goToIntakePosition()),
// 			DO(() -> robot.coneIntake.autoIntake()),
// 			robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X - ROBOT_OFFSET_X_FROM_EDGE - 0.5, blue_gamePiece4Y - 0.15), Rotation2d.fromDegrees(0), new MetresPerSec(5)),
// 			SPLIT(
// 				robot.drivetrain.doTrackTo(Pipelines.CONE, gamePiece4X - ROBOT_OFFSET_X_FROM_EDGE + 0.35, Rotation2d.fromDegrees(0), new MetresPerSec(1.5))
// 			).AND(
// 				SEQUENCE(
// 					// WAIT().UNSAFE_UNTIL(robot.coneIntake::hasCone),
// 					WAIT(new Seconds(2)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || robot.coneIntake.hasCone()),
// 					WAIT(new Seconds(1)),
// 					DO(() -> robot.coneIntake.hold())
// 				)
// 			),
// 			//intake + drive to column 9
// 			robot.drivetrain.doTurnTo(180),
// 			DO(() -> robot.arm.goToPosition(ArmConstants.MID_POLE_PREPARING_POSITION)),
// 			robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 1.5, blue_column9Y - 0.25), Rotation2d.fromDegrees(180), new MetresPerSec(3)),
// 			SPLIT(
// 				robot.drivetrain.doTrackTo(Pipelines.REFLECTIVE_TAPE, gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.15, Rotation2d.fromDegrees(180), new MetresPerSec(1))
// 			).AND(
// 				SEQUENCE(
// 					WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < communityShortEdgeX - DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE),
// 					DO(robot.arm::goToMidGoalPosition)
// 				)
// 			),
// 			WAIT(new Seconds(0.3)),
// 			DO(() -> robot.coneIntake.autoOuttake()),
// 			WAIT().UNSAFE_UNTIL(() -> !robot.coneIntake.hasCone()),
// 			WAIT(new Seconds(0.2)),
// 			DO(() -> robot.coneIntake.stop()),
// 			SPLIT(
// 				robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.7, blue_column5Y + 0.10), Rotation2d.fromDegrees(-90))
// 			).AND(
// 				SEQUENCE(
// 					WAIT().UNSAFE_UNTIL(() -> Math.abs(robot.drivetrain.getPosition().getHeading().getDegrees()) < 120),
// 					DO(robot.arm::goToStowPosition)
// 				)
// 			),
// 			robot.drivetrain.doGetOntoBalance(Rotation2d.fromDegrees(-90), true, 3.5),
// 			robot.drivetrain.doBalanceTo(Rotation2d.fromDegrees(-90))
// 		)
// 	),
// 	RED_OPEN_NO_CHARGE((robot) ->
// 		SEQUENCE(
// 			DO(()-> robot.drivetrain.setPosition(new Metres(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE), new Metres(red_column8Y + 0.04), Rotation2d.fromDegrees(0))),
// 			//score preload cube + drive to 4GP
// 			DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_HIGH)),
// 			WAIT().UNSAFE_UNTIL(robot.cubeIntake::isOnTarget),
// 			WAIT(new Seconds(0.2)),
// 			DO(()-> robot.cubeIntake.shoot()),
// 			WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 			WAIT(new Seconds(0.2)),
// 			DO(()-> robot.cubeIntake.stop()),
// 			DO(()-> robot.cubeIntake.moveToPos(STOW_POS)),
// 			DO(() -> robot.arm.goToIntakePosition()),
// 			DO(() -> robot.coneIntake.autoIntake()),
// 			robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X - ROBOT_OFFSET_X_FROM_EDGE - 0.5, red_gamePiece4Y + 0.15), Rotation2d.fromDegrees(0), new MetresPerSec(5)),
// 			SPLIT(
// 				robot.drivetrain.doTrackTo(Pipelines.CONE, gamePiece4X - ROBOT_OFFSET_X_FROM_EDGE + 0.35, Rotation2d.fromDegrees(0), new MetresPerSec(1.5))
// 			).AND(
// 				SEQUENCE(
// 					// WAIT().UNSAFE_UNTIL(robot.coneIntake::hasCone),
// 					WAIT(new Seconds(2)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || robot.coneIntake.hasCone()),
// 					WAIT(new Seconds(1)),
// 					DO(() -> robot.coneIntake.hold())
// 				)
// 			),
// 			//intake + drive to column 9
// 			robot.drivetrain.doTurnTo(180),
// 			DO(() -> robot.arm.goToPosition(ArmConstants.MID_POLE_PREPARING_POSITION)),
// 			robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 1.5, red_column9Y + 0.25), Rotation2d.fromDegrees(180), new MetresPerSec(3)),
// 			SPLIT(
// 				robot.drivetrain.doTrackTo(Pipelines.REFLECTIVE_TAPE, gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.15, Rotation2d.fromDegrees(180), new MetresPerSec(1))
// 			).AND(
// 				SEQUENCE(
// 					WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < communityShortEdgeX - DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE),
// 					DO(robot.arm::goToMidGoalPosition)
// 				)
// 			),
// 			WAIT(new Seconds(0.3)),
// 			DO(() -> robot.coneIntake.autoOuttake()),
// 			WAIT().UNSAFE_UNTIL(() -> !robot.coneIntake.hasCone()),
// 			WAIT(new Seconds(0.2)),
// 			DO(() -> robot.coneIntake.stop()),
// 			SPLIT(
// 				robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 1, red_column9Y - 0.10), Rotation2d.fromDegrees(90))
// 			).AND(
// 				SEQUENCE(
// 					WAIT().UNSAFE_UNTIL(() -> Math.abs(robot.drivetrain.getPosition().getHeading().getDegrees()) < 120),
// 					DO(robot.arm::goToStowPosition)
// 				)
// 			)
// 		)
// 	),
// 	BLUE_OPEN_NO_CHARGE((robot) ->
// 		SEQUENCE(
// 			DO(()-> robot.drivetrain.setPosition(new Metres(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE), new Metres(blue_column8Y - 0.04), Rotation2d.fromDegrees(0))),
// 			//score preload cube + drive to 4GP
// 			DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_HIGH)),
// 			WAIT().UNSAFE_UNTIL(robot.cubeIntake::isOnTarget),
// 			WAIT(new Seconds(0.2)),
// 			DO(()-> robot.cubeIntake.shoot()),
// 			WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 			WAIT(new Seconds(0.2)),
// 			DO(()-> robot.cubeIntake.stop()),
// 			DO(()-> robot.cubeIntake.moveToPos(STOW_POS)),
// 			DO(() -> robot.arm.goToIntakePosition()),
// 			DO(() -> robot.coneIntake.autoIntake()),
// 			robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X - ROBOT_OFFSET_X_FROM_EDGE - 0.5, blue_gamePiece4Y - 0.15), Rotation2d.fromDegrees(0), new MetresPerSec(5)),
// 			SPLIT(
// 				robot.drivetrain.doTrackTo(Pipelines.CONE, gamePiece4X - ROBOT_OFFSET_X_FROM_EDGE + 0.35, Rotation2d.fromDegrees(0), new MetresPerSec(1.5))
// 			).AND(
// 				SEQUENCE(
// 					// WAIT().UNSAFE_UNTIL(robot.coneIntake::hasCone),
// 					WAIT(new Seconds(2)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || robot.coneIntake.hasCone()),
// 					WAIT(new Seconds(1)),
// 					DO(() -> robot.coneIntake.hold())
// 				)
// 			),
// 			//intake + drive to column 9
// 			robot.drivetrain.doTurnTo(180),
// 			DO(() -> robot.arm.goToPosition(ArmConstants.MID_POLE_PREPARING_POSITION)),
// 			robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 1.5, blue_column9Y - 0.25), Rotation2d.fromDegrees(180), new MetresPerSec(3)),
// 			SPLIT(
// 				robot.drivetrain.doTrackTo(Pipelines.REFLECTIVE_TAPE, gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.15, Rotation2d.fromDegrees(180), new MetresPerSec(1))
// 			).AND(
// 				SEQUENCE(
// 					WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < communityShortEdgeX - DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE),
// 					DO(robot.arm::goToMidGoalPosition)
// 				)
// 			),
// 			WAIT(new Seconds(0.3)),
// 			DO(() -> robot.coneIntake.autoOuttake()),
// 			WAIT().UNSAFE_UNTIL(() -> !robot.coneIntake.hasCone()),
// 			WAIT(new Seconds(0.2)),
// 			DO(() -> robot.coneIntake.stop()),
// 			SPLIT(
// 				robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 1.0, blue_column9Y - 0.25), Rotation2d.fromDegrees(180))
// 			).AND(
// 				SEQUENCE(
// 					WAIT().UNSAFE_UNTIL(() -> Math.abs(robot.drivetrain.getPosition().getHeading().getDegrees()) < 120),
// 					DO(robot.arm::goToStowPosition)
// 				)
// 			)
// 		)
// 	),

// 	/* Barrier side autos */

// 	RED_BARRIER((robot) ->
//         SEQUENCE(
//             DO(()-> robot.drivetrain.setPosition(new Metres(communityLongEdgeX - DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE), new Metres(-DrivetrainConstants.ROBOT_OFFSET_Y_FROM_EDGE), Rotation2d.fromDegrees(0))),
//             // //score preload cube + drive to 4GP
//             DO(()-> robot.cubeIntake.moveToPos(LONG_SHOT_POS)),
//             WAIT().UNSAFE_UNTIL(robot.cubeIntake::isOnTarget),
//             WAIT(new Seconds(0.2)),
//             DO(()-> robot.cubeIntake.shoot()),
//             WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
//             WAIT(new Seconds(0.2)),
//             DO(()-> robot.cubeIntake.stop()),
//             DO(()-> robot.cubeIntake.moveToPos(STOW_POS)),
//             DO(() -> robot.arm.goToIntakePosition()),
//             DO(() -> robot.coneIntake.autoIntake()),
//             robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X - ROBOT_OFFSET_X_FROM_EDGE - 1, red_gamePiece1Y), Rotation2d.fromDegrees(0)),
//             SPLIT(
//                 robot.drivetrain.doTrackTo(Pipelines.CONE, gamePiece1X - ROBOT_OFFSET_X_FROM_EDGE + 0.35, Rotation2d.fromDegrees(0), new MetresPerSec(1.5))
//             ).AND(
//                 SEQUENCE(
//                     WAIT().UNSAFE_UNTIL(robot.coneIntake::hasCone),
//                     WAIT(new Seconds(0.2)),
//                     DO(() -> robot.coneIntake.hold())
//                 )
//             ),
//             // //intake + drive to column 9
//             robot.drivetrain.doTurnTo(180),
//             robot.drivetrain.doMoveTo(new Translation2d(communityShortEdgeX - DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE, red_column1Y), Rotation2d.fromDegrees(180), new MetresPerSec(1.5)),
//             DO(() -> robot.arm.goToHighGoalPosition()),
//             WAIT().UNSAFE_UNTIL(robot.arm::isAtTarget),
//             robot.drivetrain.doTrackTo(Pipelines.REFLECTIVE_TAPE, gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.15, Rotation2d.fromDegrees(180), new MetresPerSec(1.5)),
//             robot.drivetrain.doTrackToWithDistance(Pipelines.REFLECTIVE_TAPE, Rotation2d.fromDegrees(180), 0.5),
//             WAIT(new Seconds(0.3)),
//             DO(() -> robot.coneIntake.autoOuttake()),
//             WAIT().UNSAFE_UNTIL(() -> !robot.coneIntake.hasCone()),
//             WAIT(new Seconds(0.2)),
//             DO(() -> robot.coneIntake.stop())
//         )
//     ),
//     BLUE_BARRIER((robot) ->
//         SEQUENCE(
//             DO(()-> robot.drivetrain.setPosition(new Metres(communityLongEdgeX - DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE), new Metres(DrivetrainConstants.ROBOT_OFFSET_Y_FROM_EDGE), Rotation2d.fromDegrees(0))),
//             // //score preload cube + drive to 4GP
//             DO(()-> robot.cubeIntake.moveToPos(LONG_SHOT_POS)),
//             WAIT().UNSAFE_UNTIL(robot.cubeIntake::isOnTarget),
//             WAIT(new Seconds(0.2)),
//             DO(()-> robot.cubeIntake.shoot()),
//             WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
//             WAIT(new Seconds(0.2)),
//             DO(()-> robot.cubeIntake.stop()),
//             DO(()-> robot.cubeIntake.moveToPos(STOW_POS)),
//             DO(() -> robot.arm.goToIntakePosition()),
//             DO(() -> robot.coneIntake.autoIntake()),
//             robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X - ROBOT_OFFSET_X_FROM_EDGE - 1, blue_gamePiece1Y), Rotation2d.fromDegrees(0)),
//             SPLIT(
//                 robot.drivetrain.doTrackTo(Pipelines.CONE, gamePiece1X - ROBOT_OFFSET_X_FROM_EDGE + 0.35, Rotation2d.fromDegrees(0), new MetresPerSec(1.5))
//             ).AND(
//                 SEQUENCE(
//                     WAIT().UNSAFE_UNTIL(robot.coneIntake::hasCone),
//                     WAIT(new Seconds(0.2)),
//                     DO(() -> robot.coneIntake.hold())
//                 )
//             ),
//             // //intake + drive to column 9
//             robot.drivetrain.doTurnTo(180),
//             robot.drivetrain.doMoveTo(new Translation2d(communityShortEdgeX - DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE, blue_column1Y), Rotation2d.fromDegrees(180), new MetresPerSec(1.5)),
//             DO(() -> robot.arm.goToHighGoalPosition()),
//             WAIT().UNSAFE_UNTIL(robot.arm::isAtTarget),
//             robot.drivetrain.doTrackTo(Pipelines.REFLECTIVE_TAPE, gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.15, Rotation2d.fromDegrees(180), new MetresPerSec(1.5)),
//             robot.drivetrain.doTrackToWithDistance(Pipelines.REFLECTIVE_TAPE, Rotation2d.fromDegrees(180), 0.5),
//             WAIT(new Seconds(0.3)),
//             DO(() -> robot.coneIntake.autoOuttake()),
//             WAIT().UNSAFE_UNTIL(() -> !robot.coneIntake.hasCone()),
//             WAIT(new Seconds(0.2)),
//             DO(() -> robot.coneIntake.stop())
//         )
//     ),

// 	/*
// 	 * Open side autos without limelight
// 	 * Use at your own risk
// 	 */

// 	NO_LIMELIGHT_BLUE_OPEN((robot) ->
//         SEQUENCE(
//             DO(()-> robot.drivetrain.setPosition(new Metres(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE), new Metres(blue_column8Y), Rotation2d.fromDegrees(0))),
//             //score preload cube + drive to 4GP
//             DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_HIGH)),
//             WAIT().UNSAFE_UNTIL(robot.cubeIntake::isOnTarget),
//             WAIT(new Seconds(0.2)),
//             DO(()-> robot.cubeIntake.shoot()),
//             WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
//             DO(()-> robot.cubeIntake.stop()),
//             DO(()-> robot.cubeIntake.moveToPos(STOW_POS)),
//             DO(() -> robot.arm.goToIntakePosition()),
//             DO(() -> robot.coneIntake.autoIntake()),
//             robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X - ROBOT_OFFSET_X_FROM_EDGE, blue_gamePiece4Y - 0.05), Rotation2d.fromDegrees(0), new MetresPerSec(5)),
//             SPLIT(
//                 robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X - ROBOT_OFFSET_X_FROM_EDGE + 0.3, blue_gamePiece4Y - 0.05), Rotation2d.fromDegrees(0), new MetresPerSec(1))
//             ).AND(
//                 SEQUENCE(
//                     WAIT().UNSAFE_UNTIL(robot.coneIntake::hasCone),
//                     DO(() -> robot.coneIntake.hold())
//                 )
//             ),
//             //intake + drive to column 9
//             robot.drivetrain.doTurnTo(180),
//             DO(() -> robot.arm.goToHighGoalPosition()),
//             robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 1.5, blue_column9Y - 0.10), Rotation2d.fromDegrees(180), new MetresPerSec(3)),
//             robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.15, blue_column9Y - 0.10), Rotation2d.fromDegrees(180), new MetresPerSec(1.5)),
//             DO(() -> robot.coneIntake.autoOuttake()),
//             WAIT().UNSAFE_UNTIL(() -> !robot.coneIntake.hasCone()),
//             WAIT(new Seconds(0.2)),
//             DO(() -> robot.coneIntake.stop()),
//             robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.5, blue_column9Y - 0.10), Rotation2d.fromDegrees(180), new MetresPerSec(2.5)),
//             DO(robot.arm::goToStowPosition),
//             WAIT().UNSAFE_UNTIL(() -> robot.arm.getUpperArmAngle().getDegrees() > 135),
//             robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.7, blue_column5Y + 0.40), Rotation2d.fromDegrees(-90)),
//             robot.drivetrain.doGetOntoBalance(Rotation2d.fromDegrees(-90), true, 3.5),
//             robot.drivetrain.doBalanceTo(Rotation2d.fromDegrees(-90))
//         )
//     ),
//     NO_LIMELIGHT_RED_OPEN((robot) ->
//         SEQUENCE(
//             DO(()-> robot.drivetrain.setPosition(new Metres(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE), new Metres(red_column8Y), Rotation2d.fromDegrees(0))),
//             //score preload cube + drive to 4GP
//             DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_HIGH)),
//             WAIT().UNSAFE_UNTIL(robot.cubeIntake::isOnTarget),
//             WAIT(new Seconds(0.2)),
//             DO(()-> robot.cubeIntake.shoot()),
//             WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
//             DO(()-> robot.cubeIntake.stop()),
//             DO(()-> robot.cubeIntake.moveToPos(STOW_POS)),
//             DO(() -> robot.arm.goToIntakePosition()),
//             DO(() -> robot.coneIntake.autoIntake()),
//             robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X - ROBOT_OFFSET_X_FROM_EDGE, red_gamePiece4Y), Rotation2d.fromDegrees(0), new MetresPerSec(5)),
//             SPLIT(
//                 robot.drivetrain.doMoveTo(new Translation2d(gamePiece4X - ROBOT_OFFSET_X_FROM_EDGE + 0.3, red_gamePiece4Y), Rotation2d.fromDegrees(0), new MetresPerSec(1))
//             ).AND(
//                 SEQUENCE(
//                     WAIT().UNSAFE_UNTIL(robot.coneIntake::hasCone),
//                     DO(() -> robot.coneIntake.hold())
//                 )
//             ),
//             //intake + drive to column 9
//             robot.drivetrain.doTurnTo(180),
//             DO(() -> robot.arm.goToHighGoalPosition()),
//             robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 1.5, red_column9Y + 0.33), Rotation2d.fromDegrees(180), new MetresPerSec(3)),
//             robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.15, red_column9Y + 0.33), Rotation2d.fromDegrees(180), new MetresPerSec(1.5)),
//             DO(() -> robot.coneIntake.autoOuttake()),
//             WAIT().UNSAFE_UNTIL(() -> !robot.coneIntake.hasCone()),
//             WAIT(new Seconds(0.2)),
//             DO(() -> robot.coneIntake.stop()),
//             robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.5, red_column9Y + 0.10), Rotation2d.fromDegrees(180), new MetresPerSec(2.5)),
//             DO(robot.arm::goToStowPosition),
//             WAIT().UNSAFE_UNTIL(() -> robot.arm.getUpperArmAngle().getDegrees() > 135),
//             robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.7, red_column5Y - 0.40), Rotation2d.fromDegrees(90)),
//             robot.drivetrain.doGetOntoBalance(Rotation2d.fromDegrees(90), false, 3.5),
//             robot.drivetrain.doBalanceTo(Rotation2d.fromDegrees(90))
//         )
//     );

//     private UnusedCanadaAutos(Consumer<Robot> unused) {}
// }
