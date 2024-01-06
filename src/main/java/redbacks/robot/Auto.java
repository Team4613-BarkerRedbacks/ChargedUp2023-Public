package redbacks.robot;

import static arachne4.lib.sequences.Actionable.*;
import static redbacks.field.FieldLocations.*;
import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;
import arachne4.lib.sequences.Actionable;
import arachne4.lib.units.Duration;

import arachne4.lib.units.Seconds;
import edu.wpi.first.wpilibj.Timer;

import java.util.function.Function;

public enum Auto implements Function<Robot, Actionable> {
	DO_NOTHING((robot) ->
		DO_NOTHING()
	),

	/* Barrier side autos */

	CHAMPS_RED_BARRIER_3GP(AutoSimpleBarrier.RED_NO_BALANCE_3GP), 
	CHAMPS_BLUE_BARRIER_3GP(AutoSimpleBarrier.BLUE_NO_BALANCE_3GP), 
	CHAMPS_RED_BARRIER_3GP_BALANCE(AutoSimpleBarrier.RED_WITH_BALANCE_3GP), 
	CHAMPS_BLUE_BARRIER_3GP_BALANCE(AutoSimpleBarrier.BLUE_WITH_BALANCE_3GP), 

	/* Open starting autos */

	CHAMPS_RED_OPEN_3GP(AutoSimpleOpen.RED_NO_BALANCE_3GP), 
	CHAMPS_BLUE_OPEN_3GP(AutoSimpleOpen.BLUE_NO_BALANCE_3GP), 
	CHAMPS_RED_OPEN_3GP_BALANCE(AutoSimpleOpen.RED_WITH_BALANCE_3GP), 
	CHAMPS_BLUE_OPEN_3GP_BALANCE(AutoSimpleOpen.BLUE_WITH_BALANCE_3GP), 
	CHAMPS_RED_OPEN_MID(AutoSimpleOpen.RED_NO_BALANCE_3GP_LMH),

	/* Center starting autos */

	CHAMPS_RED_CENTER_1_PLUS_MOBILITY(AutoSimpleCharge.RED_CENTER_1_PIECE_MOBILITY),
	CHAMPS_BLUE_CENTER_1_PLUS_MOBILITY(AutoSimpleCharge.BLUE_CENTER_1_PIECE_MOBILITY),
	
	DO_NOT_USE_CHAMPS_RED_CHARGE_BARRIER_SIDE(AutoSimpleCharge.RED_BARRIER_SIDE),
	DO_NOT_USE_CHAMPS_BLUE_CHARGE_BARRIER_SIDE(AutoSimpleCharge.BLUE_BARRIER_SIDE),
	DO_NOT_USE_CHAMPS_RED_CHARGE_OPEN_SIDE(AutoSimpleCharge.RED_OPEN_SIDE),
	DO_NOT_USE_CHAMPS_BLUE_CHARGE_OPEN_SIDE(AutoSimpleCharge.BLUE_OPEN_SIDE);

	private static double autoStartTime;

	private final Function<Robot, Actionable> actionableGenerator;

	private Auto(Function<Robot, Actionable> actionableGenerator) {
		this.actionableGenerator = actionableGenerator;
	}

	@Override
	public Actionable apply(Robot robot) {
		return SPLIT(
			actionableGenerator.apply(robot)
		).AND(
			DO(() -> autoStartTime = Timer.getFPGATimestamp())
		);
	}

	static boolean doesTimeSinceAutoStartExceed(Duration time) {
		return Timer.getFPGATimestamp() - autoStartTime >= time.asRaw(Seconds::convert);
	}

	public static void main(String[] args) {
		System.out.println(cableProtectorCenterX + ROBOT_OFFSET_X_FROM_EDGE + 0.3);
		System.out.println(-ROBOT_OFFSET_Y_FROM_EDGE - 0.05);
	}
}