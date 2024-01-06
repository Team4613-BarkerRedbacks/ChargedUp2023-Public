// package redbacks.robot;

// import java.util.function.Consumer;

// public enum UnusedHawaiiAutos {
    // RED_BARRIER_3_CUBE_BALANCE((robot) ->
	// 	SEQUENCE(
	// 		DO(()-> robot.drivetrain.setPosition(new Metres(communityLongEdgeX - DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE), new Metres(-DrivetrainConstants.ROBOT_OFFSET_Y_FROM_EDGE - 0.05), Rotation2d.fromDegrees(0))),
	// 		// //score preload cube + drive to 4GP
	// 		DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
	// 		WAIT().UNSAFE_UNTIL(robot.cubeIntake::isOnTarget),
	// 		WAIT(new Seconds(0.2)),
	// 		DO(()-> robot.cubeIntake.shoot()),
	// 		WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
	// 		WAIT(new Seconds(0.2)),
	// 		DO(()-> robot.cubeIntake.stop()),
	// 		DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
	// 		robot.drivetrain.doPathTo(Trajectories.goToGP1_RED, (pos) -> Rotation2d.fromDegrees(0)),
	// 		DO(()-> robot.cubeIntake.intakeInAuto()),
	// 		SPLIT(
	// 			robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX+ROBOT_OFFSET_X_FROM_EDGE, red_chargeStationCorner2Y), Rotation2d.fromDegrees(0))
	// 		).AND(
	// 			SEQUENCE(
	// 				WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
	// 				DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
	// 				WAIT(new Seconds(0.2)),
	// 				DO(() -> robot.cubeIntake.stop())
	// 			)
	// 		),
	// 		DO(()-> robot.cubeIntake.shoot()),
	// 		WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
	// 		WAIT(new Seconds(0.2)),
	// 		DO(()-> robot.cubeIntake.stop()),

	// 		robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8, red_chargeStationCorner2Y), Rotation2d.fromDegrees(0)),
	// 		DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
	// 		robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8, red_chargeStationCorner2Y - 0.60), Rotation2d.fromDegrees(0)),
	// 		DO(()-> robot.cubeIntake.intakeInAuto()),
	// 		SPLIT(
	// 			robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE, (red_chargeStationCorner2Y + red_chargeStationCorner3Y) / 2), Rotation2d.fromDegrees(0))
	// 		).AND(
	// 			SEQUENCE(
	// 				WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
	// 				DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
	// 				WAIT(new Seconds(0.2)),
	// 				DO(() -> robot.cubeIntake.stop())
	// 			)
	// 		),
	// 		DO(()-> robot.cubeIntake.shoot()),
	// 		WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()), 
	// 		WAIT(new Seconds(0.2)),
	// 		DO(()-> robot.cubeIntake.stop()),

	// 		// //balance
	// 		robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE + 0.2, (red_chargeStationCorner2Y + red_chargeStationCorner3Y) / 2), Rotation2d.fromDegrees(-90)),
	// 		robot.drivetrain.doGetOntoBalanceFacingY(Rotation2d.fromDegrees(-90), false, 4.0),
	// 		robot.drivetrain.doBalanceToFacingY(Rotation2d.fromDegrees(-90))
	// 	)
	// ),
	// BLUE_BARRIER_3_CUBE_BALANCE((robot) ->
	// 	SEQUENCE(
	// 		DO(()-> robot.drivetrain.setPosition(new Metres(communityLongEdgeX - DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE), new Metres(DrivetrainConstants.ROBOT_OFFSET_Y_FROM_EDGE + 0.05), Rotation2d.fromDegrees(0))),
	// 		// //score preload cube + drive to 1GP
	// 		DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)), 	
	// 		WAIT().UNSAFE_UNTIL(robot.cubeIntake::isOnTarget),
	// 		WAIT(new Seconds(0.2)),
	// 		DO(()-> robot.cubeIntake.shoot()),
	// 		WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
	// 		WAIT(new Seconds(0.2)),
	// 		DO(()-> robot.cubeIntake.stop()),
	// 		DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
	// 		robot.drivetrain.doPathTo(Trajectories.goToGP1_BLUE, (pos) -> Rotation2d.fromDegrees(0)),
	// 		DO(()-> robot.cubeIntake.intakeInAuto()),
	// 		SPLIT(
	// 			robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX+ROBOT_OFFSET_X_FROM_EDGE, blue_chargeStationCorner2Y), Rotation2d.fromDegrees(0))
	// 		).AND(
	// 			SEQUENCE(
	// 				WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
	// 				DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
	// 				WAIT(new Seconds(0.2)),
	// 				DO(() -> robot.cubeIntake.stop())
	// 			)
	// 		),
	// 		DO(()-> robot.cubeIntake.shoot()),
	// 		WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
	// 		WAIT(new Seconds(0.2)),
	// 		DO(()-> robot.cubeIntake.stop()),

	// 		robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8, blue_chargeStationCorner2Y), Rotation2d.fromDegrees(0)),
	// 		DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND)),
	// 		robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8, blue_chargeStationCorner2Y + 0.60), Rotation2d.fromDegrees(0)),
	// 		DO(()-> robot.cubeIntake.intakeInAuto()),
	// 		SPLIT(
	// 			robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE, (blue_chargeStationCorner2Y + blue_chargeStationCorner3Y) / 2), Rotation2d.fromDegrees(0))
	// 		).AND(
	// 			SEQUENCE(
	// 				WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
	// 				DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
	// 				WAIT(new Seconds(0.2)),
	// 				DO(() -> robot.cubeIntake.stop())
	// 			)
	// 		),
	// 		DO(()-> robot.cubeIntake.shoot()),
	// 		WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()), 
	// 		WAIT(new Seconds(0.2)),
	// 		DO(()-> robot.cubeIntake.stop()),

	// 		// //balance
	// 		robot.drivetrain.doMoveTo(new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE + 0.2, (blue_chargeStationCorner2Y + blue_chargeStationCorner3Y) / 2), Rotation2d.fromDegrees(-90)),
	// 		robot.drivetrain.doGetOntoBalanceFacingY(Rotation2d.fromDegrees(-90), false, 4.0),
	// 		robot.drivetrain.doBalanceToFacingY(Rotation2d.fromDegrees(-90))
	// 	)
	// ),
// 	// RED_BARRIER_HIGH_2_CONE_AND_LOW_CUBE((robot) ->
// 	// 	SEQUENCE(
// 	 		//doBarrierSideHighConeAndLowCube(robot),
// 	// 		// Collect second cone
// 	// 		SPLIT(
// 	// 			SEQUENCE(
// 	// 				WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 	// 				WAIT(new Seconds(0.5)),
// 	// 				DO(robot.cubeIntake::stop),
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND))
// 	// 			)
// 	// 		).AND(
// 	// 			robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8, red_chargeStationCorner2Y + ROBOT_OFFSET_Y_FROM_EDGE + 0.05), Rotation2d.fromDegrees(0))
// 	// 		),
// 	// 		// Collect second cube
// 	// 		robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X + 0.8, red_gamePiece2Y), Rotation2d.fromDegrees(0)),
// 	// 		DO(() -> robot.cubeIntake.intakeInAuto()),
// 	// 		// Move to balance
// 	// 		SPLIT(
// 	// 			robot.drivetrain.doMoveTo(
// 	// 				new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE, red_gamePiece2Y),
// 	// 				Rotation2d.fromDegrees(0)
// 	// 			)
// 	// 		).AND(
// 	// 			SEQUENCE(
// 	// 				WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
// 	// 				DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
// 	// 				WAIT(new Seconds(0.2)),
// 	// 				DO(() -> robot.cubeIntake.stop())
// 	// 			)
// 	// 		),
// 	// 		// Balance and shoot
// 	// 		robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), false, 3.5)
// 	// 			.UNSAFE_UNTIL((onBalance) -> onBalance || doesTimeSinceAutoStartExceed(new Seconds(14))),
// 	// 		SPLIT(
// 	// 			SEQUENCE(
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT_FROM_CHARGE_STATION)),
// 	// 				WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.isLevel() || doesTimeSinceAutoStartExceed(new Seconds(14.5))),
// 	// 				WAIT(new Seconds(0.5)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || doesTimeSinceAutoStartExceed(new Seconds(14.5))),
// 	// 				DO(() -> robot.cubeIntake.shoot()),
// 	// 				WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 	// 				WAIT(new Seconds(0.5)),
// 	// 				DO(() -> robot.cubeIntake.stop())
// 	// 			)
// 	// 		).AND(
// 	// 			robot.drivetrain.doBalanceToFacingX(Rotation2d.fromDegrees(0))
// 	// 		)
// 	// 	)
// 	// ),
// 	// // RED_BARRIER_HIGH_2_CONE_AND_LOW_CUBE((robot) ->
// 	// // 	SEQUENCE(
// 	// // 		doBarrierSideHighConeAndLowCube(robot),
// 	// // 		// Collect second cone
// 	// // 		SPLIT(
// 	// // 			SEQUENCE(
// 	// // 				WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 	// // 				WAIT(new Seconds(0.5)),
// 	// // 				DO(robot.cubeIntake::stop),
// 	// // 				DO(() -> robot.armAndCubeCoordination.goToPositions(AUTO_PREPARE_CONE_FROM_GROUND))
// 	// // 			)
// 	// // 		).AND(
// 	// // 			robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X - ROBOT_OFFSET_X_FROM_EDGE + X_ODOMETRY_LOSS_OVER_BARRIER_METRES, red_gamePiece2Y - Y_ODOMETRY_LOSS_OVER_BARRIER_METRES), Rotation2d.fromDegrees(0))
// 	// // 		),
// 	// // 		DO(robot.coneIntake::autoIntake),
// 	// // 		DO(() -> robot.armAndCubeCoordination.goToPositions(AUTO_CONE_FROM_GROUND)),
// 	// // 		robot.drivetrain.doMoveTo(new Translation2d(gamePiece2X - ROBOT_OFFSET_X_FROM_EDGE + X_ODOMETRY_LOSS_OVER_BARRIER_METRES - 0.5, red_gamePiece2Y - Y_ODOMETRY_LOSS_OVER_BARRIER_METRES), Rotation2d.fromDegrees(0), new MetresPerSec(2)),
// 	// // 		DO(() -> robot.armAndCubeCoordination.goToPositions(STOW_WITH_CONE)),
// 	// // 		SPLIT(
// 	// // 			SEQUENCE(
// 	// // 				WAIT(new Seconds(0.5)),
// 	// // 				DO(robot.coneIntake::hold)
// 	// // 			)
// 	// // 		).AND(
// 	// // 			robot.drivetrain.doMoveTo(new Translation2d(cableProtectorCenterX + ROBOT_OFFSET_X_FROM_EDGE + X_ODOMETRY_LOSS_OVER_BARRIER_METRES + 0.6, red_column1Y - Y_ODOMETRY_LOSS_OVER_BARRIER_METRES), Rotation2d.fromDegrees(0))
// 	// // 		)
// 	// // 		// ,
// 	// // 		// robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.3, red_column1Y), Rotation2d.fromDegrees(0), new MetresPerSec(3)),
// 	// // 		// robot.drivetrain.doTurnTo(Rotation2d.fromDegrees(180)),
// 	// // 		// DO(() -> robot.armAndCubeCoordination.goToPositions(CONE_FROM_HUMAN_PLAYER)),
// 	// // 		// robot.drivetrain.doMoveTo(new Translation2d(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE + 0.3, red_column3Y), Rotation2d.fromDegrees(180)),
// 	// // 		// WAIT().UNSAFE_UNTIL(() -> robot.arm.getPosition().getY().asRaw(Metres::convert) > CONE_FROM_HUMAN_PLAYER.armPosition.getCoordinates().getY().asRaw(Metres::convert) - 0.15),
// 	// // 		// DO(() -> robot.armAndCubeCoordination.goToPositions(CONE_SCORE_HIGH)),
// 	// // 		// WAIT(new Seconds(2)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || robot.arm.isAtTarget()),
// 	// // 		// DO(robot.coneIntake::autoOuttake),
// 	// // 		// WAIT(new Seconds(0.5)),
// 	// // 		// DO(() -> robot.armAndCubeCoordination.goToPositions(AUTO_RETRACT_AFTER_HIGH_GOAL)),
// 	// // 		// WAIT().UNSAFE_UNTIL(robot.arm::isAtTarget),
// 	// // 		// DO(robot.coneIntake::stop),
// 	// // 		// DO(() -> robot.armAndCubeCoordination.goToPositions(CONE_FROM_HUMAN_PLAYER)),
// 	// // 		// WAIT().UNSAFE_UNTIL(() -> robot.arm.getPosition().getX().asRaw(Metres::convert) < CONE_FROM_HUMAN_PLAYER.armPosition.getCoordinates().getX().asRaw(Metres::convert) + 0.15),
// 	// // 		// DO(() -> robot.armAndCubeCoordination.goToPositions(STOW_ALL))
// 	// // 	)
// 	// // ),
// 	// RED_BARRIER_HIGH_CONE_AND_2_LOW_CUBE_BALANCE((robot) ->
// 	// 	SEQUENCE(
// 	// 		doRedBarrierSideHighConeAndLowCube(robot),
// 	// 		// Collect second cube
// 	// 		SPLIT(
// 	// 			SEQUENCE(
// 	// 				WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 	// 				WAIT(new Seconds(0.5)),
// 	// 				DO(robot.cubeIntake::stop),
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND))
// 	// 			)
// 	// 		).AND(
// 	// 			robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8 + X_BARRIER_LOSS_METRES, red_chargeStationCorner2Y - Y_BARRIER_LOSS_METRES), Rotation2d.fromDegrees(0))
// 	// 		),
// 	// 		robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8 + X_BARRIER_LOSS_METRES, red_gamePiece2Y - Y_BARRIER_LOSS_METRES - 0.1), Rotation2d.fromDegrees(0)),
// 	// 		DO(() -> robot.cubeIntake.intakeInAuto()),
// 	// 		// Move to balance
// 	// 		SPLIT(
// 	// 			robot.drivetrain.doMoveTo(
// 	// 				new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE + X_BARRIER_LOSS_METRES, red_gamePiece2Y - Y_BARRIER_LOSS_METRES - 0.1),
// 	// 				Rotation2d.fromDegrees(0)
// 	// 			)
// 	// 		).AND(
// 	// 			SEQUENCE(
// 	// 				WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
// 	// 				DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
// 	// 				WAIT(new Seconds(0.2)),
// 	// 				DO(() -> robot.cubeIntake.stop())
// 	// 			)
// 	// 		),
// 	// 		// Balance and shoot
// 	// 		robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), false, 3.5)
// 	// 			.UNSAFE_UNTIL((onBalance) -> onBalance || doesTimeSinceAutoStartExceed(new Seconds(14))),
// 	// 		SPLIT(
// 	// 			SEQUENCE(
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT_FROM_CHARGE_STATION)),
// 	// 				WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.isLevel() || doesTimeSinceAutoStartExceed(new Seconds(14.5))),
// 	// 				WAIT(new Seconds(0.5)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || doesTimeSinceAutoStartExceed(new Seconds(14.5))),
// 	// 				DO(() -> robot.cubeIntake.shoot()),
// 	// 				WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 	// 				WAIT(new Seconds(0.5)),
// 	// 				DO(() -> robot.cubeIntake.stop())
// 	// 			)
// 	// 		).AND(
// 	// 			robot.drivetrain.doBalanceToFacingX(Rotation2d.fromDegrees(0))
// 	// 		)
// 	// 	)
// 	// ),
// 	// BLUE_BARRIER_HIGH_CONE_AND_2_LOW_CUBE_BALANCE((robot) ->
// 	// 	SEQUENCE(
// 	// 		doBlueBarrierSideHighConeAndLowCube(robot),
// 	// 		// Collect second cube
// 	// 		SPLIT(
// 	// 			SEQUENCE(
// 	// 				WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 	// 				WAIT(new Seconds(0.5)),
// 	// 				DO(robot.cubeIntake::stop),
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND))
// 	// 			)
// 	// 		).AND(
// 	// 			robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8 + X_BARRIER_LOSS_METRES, blue_chargeStationCorner2Y + Y_BARRIER_LOSS_METRES), Rotation2d.fromDegrees(0))
// 	// 		),
// 	// 		robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8 + X_BARRIER_LOSS_METRES, blue_gamePiece2Y + Y_BARRIER_LOSS_METRES + 0.1 - 0.15), Rotation2d.fromDegrees(0)),
// 	// 		DO(() -> robot.cubeIntake.intakeInAuto()),
// 	// 		// Move to balance
// 	// 		SPLIT(
// 	// 			robot.drivetrain.doMoveTo(
// 	// 				new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE + X_BARRIER_LOSS_METRES, blue_gamePiece2Y + Y_BARRIER_LOSS_METRES + 0.1),
// 	// 				Rotation2d.fromDegrees(0)
// 	// 			)
// 	// 		).AND(
// 	// 			SEQUENCE(
// 	// 				WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
// 	// 				DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
// 	// 				WAIT(new Seconds(0.2)),
// 	// 				DO(() -> robot.cubeIntake.stop())
// 	// 			)
// 	// 		),
// 	// 		// Balance and shoot
// 	// 		robot.drivetrain.doGetOntoBalanceFacingX(Rotation2d.fromDegrees(0), false, 3.5)
// 	// 			.UNSAFE_UNTIL((onBalance) -> onBalance || doesTimeSinceAutoStartExceed(new Seconds(14))),
// 	// 		SPLIT(
// 	// 			SEQUENCE(
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT_FROM_CHARGE_STATION)),
// 	// 				WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.isLevel() || doesTimeSinceAutoStartExceed(new Seconds(14.5))),
// 	// 				WAIT(new Seconds(0.5)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || doesTimeSinceAutoStartExceed(new Seconds(14.5))),
// 	// 				DO(() -> robot.cubeIntake.shoot()),
// 	// 				WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 	// 				WAIT(new Seconds(0.5)),
// 	// 				DO(() -> robot.cubeIntake.stop())
// 	// 			)
// 	// 		).AND(
// 	// 			robot.drivetrain.doBalanceToFacingX(Rotation2d.fromDegrees(0))
// 	// 		)
// 	// 	)
// 	// ),
// 	// NO_BALANCE_RED_BARRIER_HIGH_CONE_AND_2_LOW_CUBE((robot) ->
// 	// 	SEQUENCE(
// 	// 		doRedBarrierSideHighConeAndLowCube(robot),
// 	// 		// Collect second cube
// 	// 		SPLIT(
// 	// 			SEQUENCE(
// 	// 				WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 	// 				WAIT(new Seconds(0.5)),
// 	// 				DO(robot.cubeIntake::stop),
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND))
// 	// 			)
// 	// 		).AND(
// 	// 			robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8 + X_BARRIER_LOSS_METRES, red_chargeStationCorner2Y - Y_BARRIER_LOSS_METRES), Rotation2d.fromDegrees(0))
// 	// 		),
// 	// 		robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8 + X_BARRIER_LOSS_METRES, red_gamePiece2Y - Y_BARRIER_LOSS_METRES - 0.1), Rotation2d.fromDegrees(0)),
// 	// 		DO(() -> robot.cubeIntake.intakeInAuto()),
// 	// 		// Move to shoot
// 	// 		SPLIT(
// 	// 			robot.drivetrain.doMoveTo(
// 	// 				new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE + X_BARRIER_LOSS_METRES - 0.2, red_gamePiece2Y - Y_BARRIER_LOSS_METRES - 0.1),
// 	// 				Rotation2d.fromDegrees(0)
// 	// 			)
// 	// 		).AND(
// 	// 			SEQUENCE(
// 	// 				WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
// 	// 				DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
// 	// 				WAIT(new Seconds(0.2)),
// 	// 				DO(() -> robot.cubeIntake.stop())
// 	// 			)
// 	// 		),
// 	// 		DO(() -> robot.cubeIntake.shoot()),
// 	// 		WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 	// 		WAIT(new Seconds(0.5)),
// 	// 		DO(() -> robot.cubeIntake.stop()),
// 	// 		// Move away from community and lower cube intake
// 	// 		SPLIT(
// 	// 			SEQUENCE(
// 	// 				WAIT(new Seconds(0.5)),
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND))
// 	// 			)
// 	// 		).AND(
// 	// 			robot.drivetrain.doMoveToWithLerpedRotation(
// 	// 				new Translation2d(gamePiece2X + X_BARRIER_LOSS_METRES, red_gamePiece2Y - Y_BARRIER_LOSS_METRES - 0.1),
// 	// 				Rotation2d.fromDegrees(180),
// 	// 				true
// 	// 			)
// 	// 		)
// 	// 	)
// 	// ),
// 	// NO_BALANCE_BLUE_BARRIER_HIGH_CONE_AND_2_LOW_CUBE((robot) ->
// 	// 	SEQUENCE(
// 	// 		doBlueBarrierSideHighConeAndLowCube(robot),
// 	// 		// Collect second cube
// 	// 		SPLIT(
// 	// 			SEQUENCE(
// 	// 				WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 	// 				WAIT(new Seconds(0.5)),
// 	// 				DO(robot.cubeIntake::stop),
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND))
// 	// 			)
// 	// 		).AND(
// 	// 			robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8 + X_BARRIER_LOSS_METRES, blue_chargeStationCorner2Y + Y_BARRIER_LOSS_METRES), Rotation2d.fromDegrees(0))
// 	// 		),
// 	// 		robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X + 0.8 + X_BARRIER_LOSS_METRES, blue_gamePiece2Y + Y_BARRIER_LOSS_METRES + 0.1 - 0.15), Rotation2d.fromDegrees(0)),
// 	// 		DO(() -> robot.cubeIntake.intakeInAuto()),
// 	// 		// Move to shoot
// 	// 		SPLIT(
// 	// 			robot.drivetrain.doMoveTo(
// 	// 				new Translation2d(communityLongEdgeX + ROBOT_OFFSET_X_FROM_EDGE + X_BARRIER_LOSS_METRES - 0.2, blue_gamePiece2Y + Y_BARRIER_LOSS_METRES + 0.1),
// 	// 				Rotation2d.fromDegrees(0)
// 	// 			)
// 	// 		).AND(
// 	// 			SEQUENCE(
// 	// 				WAIT().UNSAFE_UNTIL(() -> robot.cubeIntake.hasCube() || robot.drivetrain.getPosition().getLocation().getX().asRaw(Metres::convert) < gamePiece1X),
// 	// 				DO(()-> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT)),
// 	// 				WAIT(new Seconds(0.2)),
// 	// 				DO(() -> robot.cubeIntake.stop())
// 	// 			)
// 	// 		),
// 	// 		DO(() -> robot.cubeIntake.shoot()),
// 	// 		WAIT().UNSAFE_UNTIL(() -> !robot.cubeIntake.hasCube()),
// 	// 		WAIT(new Seconds(0.5)),
// 	// 		DO(() -> robot.cubeIntake.stop()),
// 	// 		// Move away from community and lower cube intake
// 	// 		SPLIT(
// 	// 			SEQUENCE(
// 	// 				WAIT(new Seconds(0.5)),
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND))
// 	// 			)
// 	// 		).AND(
// 	// 			robot.drivetrain.doMoveToWithLerpedRotation(
// 	// 				new Translation2d(gamePiece2X + X_BARRIER_LOSS_METRES, blue_gamePiece2Y + Y_BARRIER_LOSS_METRES + 0.1),
// 	// 				Rotation2d.fromDegrees(180),
// 	// 				false
// 	// 			)
// 	// 		)
// 	// 	)
// 	// ),
//     ;

//     private UnusedHawaiiAutos(Consumer<Robot> unused) {}

// 	// private static Actionable doRedBarrierSideHighConeAndLowCube(Robot robot) {
// 	// 	return SEQUENCE(
// 	// 		// Score first cone
// 	// 		DO(() -> robot.drivetrain.setPosition(new Metres(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE), new Metres(red_column1Y), Rotation2d.fromDegrees(180))),
// 	// 		DO(() -> robot.armAndCubeCoordination.goToPositions(CONE_FROM_HUMAN_PLAYER)),
// 	// 		WAIT().UNSAFE_UNTIL(() -> robot.arm.getPosition().getY().asRaw(Metres::convert) > CONE_FROM_HUMAN_PLAYER.armPosition.getCoordinates().getY().asRaw(Metres::convert) - 0.4),
// 	// 		DO(() -> robot.armAndCubeCoordination.goToPositions(AUTO_CONE_SCORE_HIGH)),
// 	// 		WAIT(new Seconds(2)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || robot.arm.isAtTarget()),
// 	// 		DO(robot.coneIntake::autoOuttake),
// 	// 		WAIT(new Seconds(0.7)),
// 	// 		// Cross barrier and collect cube
// 	// 		SPLIT(
// 	// 			SEQUENCE(
// 	// 				WAIT(new Seconds(0.5)),
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CONE_FROM_HUMAN_PLAYER)),
// 	// 				WAIT().UNSAFE_UNTIL(() -> robot.arm.getPosition().getX().asRaw(Metres::convert) < gridDepth - middleRowX + ROBOT_OFFSET_X_FROM_EDGE),
// 	// 				DO(robot.coneIntake::stop),
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_LOW)),
// 	// 				WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.getPosition().getLocation().getX()
// 	// 					.isGreaterThan(new Metres(cableProtectorCenterX + ROBOT_OFFSET_X_FROM_EDGE + X_BARRIER_LOSS_METRES))),
// 	// 				DO(robot.cubeIntake::intakeInAuto),
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND))
// 	// 			)
// 	// 		).AND(
// 	// 			robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X - ROBOT_OFFSET_X_FROM_EDGE + X_BARRIER_LOSS_METRES, red_gamePiece1Y - Y_BARRIER_LOSS_METRES), Rotation2d.fromDegrees(180), new MetresPerSec(2.5))
// 	// 		),
// 	// 		// Score cube
// 	// 		SPLIT(
// 	// 			SEQUENCE(
// 	// 				WAIT(new Seconds(0.3)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || robot.cubeIntake.hasCube()),
// 	// 				DO(() -> robot.cubeIntake.stop()),
// 	// 				DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT))
// 	// 			)
// 	// 		).AND(
// 	// 			robot.drivetrain.doMoveToWithLerpedRotation(
// 	// 				new Translation2d(cableProtectorCenterX + ROBOT_OFFSET_X_FROM_EDGE + X_BARRIER_LOSS_METRES + 0.6, red_column1Y + 0.1 - Y_BARRIER_LOSS_METRES - 0.2), //-0.2 is seperate to blue
// 	// 				Rotation2d.fromDegrees(-2),
// 	// 				false
// 	// 			)
// 	// 		),
// 	// 		WAIT(new Seconds(0.2)),
// 	// 		DO(robot.cubeIntake::shoot)
// 	// 	);
// 	// }

// 	private static Actionable doBlueBarrierSideHighConeAndLowCube(Robot robot) {
// 		return SEQUENCE(
// 			// Score first cone
// 			DO(() -> robot.drivetrain.setPosition(new Metres(gridDepth + DrivetrainConstants.ROBOT_OFFSET_X_FROM_EDGE), new Metres(blue_column1Y), Rotation2d.fromDegrees(180))),
// 			DO(() -> robot.armAndCubeCoordination.goToPositions(CONE_FROM_HUMAN_PLAYER)),
// 			WAIT().UNSAFE_UNTIL(() -> robot.arm.getPosition().getY().asRaw(Metres::convert) > CONE_FROM_HUMAN_PLAYER.armPosition.getCoordinates().getY().asRaw(Metres::convert) - 0.4),
// 			DO(() -> robot.armAndCubeCoordination.goToPositions(AUTO_CONE_SCORE_HIGH)),
// 			WAIT(new Seconds(2)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || robot.arm.isAtTarget()),
// 			DO(robot.coneIntake::autoOuttake),
// 			WAIT(new Seconds(0.7)),
// 			// Cross barrier and collect cube
// 			SPLIT(
// 				SEQUENCE(
// 					WAIT(new Seconds(0.5)),
// 					DO(() -> robot.armAndCubeCoordination.goToPositions(CONE_FROM_HUMAN_PLAYER)),
// 					WAIT().UNSAFE_UNTIL(() -> robot.arm.getPosition().getX().asRaw(Metres::convert) < gridDepth - middleRowX + ROBOT_OFFSET_X_FROM_EDGE),
// 					DO(robot.coneIntake::stop),
// 					DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_SCORE_LOW)),
// 					WAIT().UNSAFE_UNTIL(() -> robot.drivetrain.getPosition().getLocation().getX()
// 						.isGreaterThan(new Metres(cableProtectorCenterX + ROBOT_OFFSET_X_FROM_EDGE + X_BARRIER_LOSS_METRES))),
// 					DO(robot.cubeIntake::intakeInAuto),
// 					DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_FROM_GROUND))
// 				)
// 			).AND(
// 				robot.drivetrain.doMoveTo(new Translation2d(gamePiece1X - ROBOT_OFFSET_X_FROM_EDGE + X_BARRIER_LOSS_METRES, blue_gamePiece1Y + Y_BARRIER_LOSS_METRES - 0.15), Rotation2d.fromDegrees(180), new MetresPerSec(2.5))
// 			),
// 			// Score cube
// 			SPLIT(
// 				SEQUENCE(
// 					WAIT(new Seconds(0.3)).UNSAFE_UNTIL((timeElapsed) -> timeElapsed || robot.cubeIntake.hasCube()),
// 					DO(() -> robot.cubeIntake.stop()),
// 					DO(() -> robot.armAndCubeCoordination.goToPositions(CUBE_LONG_SHOT))
// 				)
// 			).AND(
// 				robot.drivetrain.doMoveToWithLerpedRotation(
// 					new Translation2d(cableProtectorCenterX + ROBOT_OFFSET_X_FROM_EDGE + X_BARRIER_LOSS_METRES + 0.6, blue_column1Y - 0.1 + Y_BARRIER_LOSS_METRES),
// 					Rotation2d.fromDegrees(2),
// 					true
// 				)
// 			),
// 			WAIT(new Seconds(0.2)),
// 			DO(robot.cubeIntake::shoot)
// 		);
// 	}
// }
