package redbacks.robot.subsystems.arm;

import static redbacks.robot.subsystems.arm.ArmConstants.*;

import java.util.Optional;

import arachne4.lib.scheduler.Scheduler;
import arachne4.lib.subsystems.Subsystem;
import arachne4.lib.units.Duration;
import arachne4.lib.units.Metres;
import arachne4.lib.units.concats.Distance2d;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import redbacks.robot.Robot;
import redbacks.robot.subsystems.coneIntake.ConeIntakeConstants;
import redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationConstants;
import redbacks.robot.subsystems.coordination.ArmAndCubeCoordinationConstants.ArmAndCubePosition;

public class Arm extends Subsystem<ArmMappings> {
    protected final Robot robot;
    protected final ArmHardware hardware;

    protected ArmPosition targetPosition = STOW_POSITION;

    public Arm(Robot robot, ArmHardware hardware, ArmMappings mappings) {
        super(robot, mappings);

        this.robot = robot;
        this.hardware = hardware; 

        goToPosition(STOW_POSITION);
    }

    { registerHandler(Scheduler.INITIALIZE, this::setMotorOffsetsFromEncoders); }
    { registerHandler(mappings.resetArmEncoders.onPress(), this::setMotorOffsetsFromEncoders); }
    private void setMotorOffsetsFromEncoders() {
        hardware.lowerArmMotor.setSensorAngle(hardware.lowerArmEncoder.getAngle());
        hardware.upperArmMotor.setSensorAngle(hardware.upperArmEncoder.getAngle());
    }

    { registerHandler(Scheduler.INITIALIZE, this::setStartingPosition); }
    private void setStartingPosition() {
        hardware.mandibles.setSensorAngle(Rotation2d.fromDegrees(0));
    }

    // Mappings

    /* See ArmAndCubeCoordination for mappings */
    public void goToPosition(ArmPosition position) {
        targetPosition = position;
    }

    { registerHandler(Scheduler.EXECUTE, this::displayDashboard); }
    private void displayDashboard() {
        SmartDashboard.putNumber("Lower Arm Encoder Value", hardware.lowerArmEncoder.getAngle().getDegrees());
        SmartDashboard.putNumber("Upper Arm Encoder Value", hardware.upperArmEncoder.getAngle().getDegrees());

        SmartDashboard.putNumber("Lower Arm Motor Position", hardware.lowerArmMotor.getAngle().getDegrees());
        SmartDashboard.putNumber("Upper Arm Motor Position", hardware.upperArmMotor.getAngle().getDegrees());

        SmartDashboard.putNumber("Lower Arm Target", targetPosition.lowerArmAngle.getDegrees());
        SmartDashboard.putNumber("Upper Arm Target", targetPosition.upperArmAngle.getDegrees());

        var currentPosition = calculateTargetXYFromArmPosition(hardware.lowerArmMotor.getAngle(), hardware.upperArmMotor.getAngle());

        SmartDashboard.putNumber("Current Arm Position X", currentPosition.getX().asRaw(Metres::convert));
        SmartDashboard.putNumber("Current Arm Position Y", currentPosition.getY().asRaw(Metres::convert));

        SmartDashboard.putNumber("Mandibles angle", hardware.mandibles.getAngle().getDegrees());
    }

    { registerHandler(Scheduler.EXECUTE, this::updatePidControllers); }
    private void updatePidControllers() {
        var armTorque = calculateArmTorque(hardware.lowerArmEncoder.getAngle(), hardware.upperArmEncoder.getAngle());

        SmartDashboard.putNumber("Lower Arm Gravity Compensation", armTorque.getFirst() / LOWER_ARM_GEAR_RATIO.getInputCountFromOutput(STALL_TORQUE));
        SmartDashboard.putNumber("Upper Arm Gravity Compensation", armTorque.getSecond() / UPPER_ARM_GEAR_RATIO.getInputCountFromOutput(STALL_TORQUE));

        hardware.lowerArmMotor.setTargetAngle(targetPosition.lowerArmAngle, armTorque.getFirst() / LOWER_ARM_GEAR_RATIO.getInputCountFromOutput(STALL_TORQUE));
        hardware.upperArmMotor.setTargetAngle(targetPosition.upperArmAngle, armTorque.getSecond() / UPPER_ARM_GEAR_RATIO.getInputCountFromOutput(STALL_TORQUE));
    }

    // Calculations
    protected static Pair<Double, Double> calculateTargetArmPositions(Distance2d target) {
         // For lowerArm 0 is vertical, increases as arm moves downwards, the calculation relies on the angle being relative to the horizontal, hence the 90 - ...
         // For upperArm output 0 is vertical, however in the calculation it is relative to lower arm Angle, increases as arm moves downwards   
        double targetX = target.getX().asRaw(Metres::convert) - ARM_BASE_OFFSET.getX().asRaw(Metres::convert);
        double targetY = target.getY().asRaw(Metres::convert) - ARM_BASE_OFFSET.getY().asRaw(Metres::convert);

        double upperArmAngleRadians = Math.acos((Math.pow(targetX, 2) + Math.pow(targetY, 2) - Math.pow(LOWER_ARM_LENGTH_METRES, 2) - Math.pow(UPPER_ARM_LENGTH_METRES, 2)) / (2 * LOWER_ARM_LENGTH_METRES * UPPER_ARM_LENGTH_METRES));
        double lowerArmAngleRadians = Math.PI / 2 - (Math.atan2(targetY, targetX) + Math.atan2(UPPER_ARM_LENGTH_METRES * Math.sin(upperArmAngleRadians), LOWER_ARM_LENGTH_METRES + UPPER_ARM_LENGTH_METRES * Math.cos(upperArmAngleRadians)));
        
        upperArmAngleRadians += lowerArmAngleRadians; // Adding lower arm angle to make it relative to the vertical as opposed to relative to lower arm

        return new Pair<Double, Double>(lowerArmAngleRadians, upperArmAngleRadians);
    }

    protected static Distance2d calculateTargetXYFromArmPosition(Rotation2d lowerArmAngle, Rotation2d upperArmAngle) {
        return new Distance2d(
            new Metres(LOWER_ARM_LENGTH_METRES * Math.sin(lowerArmAngle.getRadians()) + UPPER_ARM_LENGTH_METRES * Math.sin(upperArmAngle.getRadians()) + ARM_BASE_OFFSET.getX().asRaw(Metres::convert)),
            new Metres(LOWER_ARM_LENGTH_METRES * Math.cos(lowerArmAngle.getRadians()) + UPPER_ARM_LENGTH_METRES * Math.cos(upperArmAngle.getRadians()) + ARM_BASE_OFFSET.getY().asRaw(Metres::convert))
        );
    }

    protected static Pair<Double, Double> calculateArmTorque(Rotation2d lowerArmAngle, Rotation2d upperArmAngle) {
        Translation2d
            lowerArmTranslation = new Translation2d(LOWER_ARM_LENGTH_METRES, lowerArmAngle),
            lowerArmMassOffset = new Translation2d(LOWER_ARM_CENTRE_OF_MASS, lowerArmAngle),
            upperArmMassOffset = new Translation2d(UPPER_ARM_CENTRE_OF_MASS, upperArmAngle);

        Translation2d lowerArmTorqueVector = lowerArmTranslation.plus(upperArmMassOffset)
            .times(UPPER_ARM_MASS_KG)
            .plus(lowerArmMassOffset.times(LOWER_ARM_MASS_KG));

        double lowerArmTorque = GRAVITATIONAL_ACCELERATION_METRES_PER_SEC_SQUARED * lowerArmTorqueVector.getY();
        double upperArmTorque = GRAVITATIONAL_ACCELERATION_METRES_PER_SEC_SQUARED * UPPER_ARM_MASS_KG * upperArmMassOffset.getY();

        return new Pair<Double, Double>(lowerArmTorque, upperArmTorque);
    }

    public ArmAndCubePosition getSafeTarget(ArmAndCubePosition originalTarget) {
        var currentPosition = getPosition();

        if(currentPosition.getX().isGreaterThan(new Metres(0.85)) && originalTarget.armPosition.getCoordinates().getY().isLessThan(new Metres(0.5))) {
            return ArmAndCubeCoordinationConstants.CONE_FROM_HUMAN_PLAYER;
        }

        return originalTarget;
    }

    public boolean isAtScoringTarget() {
        return targetPosition.isScoringPosition && isAtTarget();
    }

    public boolean isAtTarget() {
        return Math.abs(targetPosition.lowerArmAngle.getRadians() - hardware.lowerArmMotor.getAngle().getRadians()) < LOWER_ARM_TARGET_TOLERANCE.getRadians()
            && Math.abs(targetPosition.upperArmAngle.getRadians() - hardware.upperArmMotor.getAngle().getRadians()) < UPPER_ARM_TARGET_TOLERANCE.getRadians();
    }

    public Rotation2d getLowerArmAngle() {
        return hardware.lowerArmMotor.getAngle();
    }

    public Distance2d getPosition() {
        return calculateTargetXYFromArmPosition(hardware.lowerArmMotor.getAngle(), hardware.upperArmMotor.getAngle());
    }

    public Pair<Double, Optional<Duration>> getPositionOuttakePowerAndScoringDelay() {
        return new Pair<Double,Optional<Duration>>(
            targetPosition.outtakeSpeed,
            targetPosition.isScoringPosition ? Optional.of(targetPosition.automaticOuttakeDelay) : Optional.empty()
        );
    }

    public static class ArmPosition {
        final Rotation2d lowerArmAngle, upperArmAngle;
        final boolean isScoringPosition;
        final double outtakeSpeed;
        final Duration automaticOuttakeDelay;
        final Rotation2d mandibleTargetAngle;

        public ArmPosition(Rotation2d lowerArmAngle, Rotation2d upperArmAngle, boolean isScoringPosition, double outtakeSpeed, Duration automaticOuttakeDelay, Rotation2d mandibleTargetAngle) {
            this.lowerArmAngle = lowerArmAngle;
            this.upperArmAngle = upperArmAngle;

            this.isScoringPosition = isScoringPosition;
            this.outtakeSpeed = outtakeSpeed;
            this.automaticOuttakeDelay = automaticOuttakeDelay;
            this.mandibleTargetAngle = mandibleTargetAngle;
        }

        public ArmPosition(Rotation2d lowerArmAngle, Rotation2d upperArmAngle, Rotation2d mandibleTargetAngle) {
            this(lowerArmAngle, upperArmAngle, false, ConeIntakeConstants.DEFAULT_OUTTAKE_POWER, Duration.ZERO, mandibleTargetAngle);
        }

        public ArmPosition(Distance2d location, boolean isScoringPosition, double outtakeSpeed, Duration automaticOuttakeDelay, Rotation2d mandibleTargetAngle) {
            this(calculateTargetArmPositions(location), isScoringPosition, outtakeSpeed, automaticOuttakeDelay, mandibleTargetAngle);
        }

        public ArmPosition(Distance2d location, Rotation2d mandibleTargetAngle) {
            this(calculateTargetArmPositions(location), mandibleTargetAngle);
        }

        private ArmPosition(Pair<Double, Double> angleRadianPair, boolean isScoringPosition, double outtakeSpeed, Duration automaticOuttakeDelay, Rotation2d mandibleTargetAngle) {
            this(Rotation2d.fromRadians(angleRadianPair.getFirst()), Rotation2d.fromRadians(angleRadianPair.getSecond()), isScoringPosition, outtakeSpeed, automaticOuttakeDelay, mandibleTargetAngle);
        }

        private ArmPosition(Pair<Double, Double> angleRadianPair, Rotation2d mandibleTargetAngle) {
            this(Rotation2d.fromRadians(angleRadianPair.getFirst()), Rotation2d.fromRadians(angleRadianPair.getSecond()), mandibleTargetAngle);
        }

        public final Distance2d getCoordinates() {
            return calculateTargetXYFromArmPosition(lowerArmAngle, upperArmAngle);
        }
    }

    public static void main(String[] args) {
        Distance2d target = new Distance2d(new Metres(0.537), new Metres(0.945 -0.008 - 0.012));
        double targetX = target.getX().asRaw(Metres::convert) - ARM_BASE_OFFSET.getX().asRaw(Metres::convert);
        double targetY = target.getY().asRaw(Metres::convert) - ARM_BASE_OFFSET.getY().asRaw(Metres::convert);
        // double upperArmAngleRadians = Math.acos((Math.pow(targetX, 2) + Math.pow(targetY, 2) - Math.pow(0.98, 2) - Math.pow(0.756, 2)) / (2 * 0.98 * 0.756));
        // double lowerArmAngleRadians = Math.PI / 2 - (Math.atan2(targetY, targetX) + Math.atan2(0.756 * Math.sin(upperArmAngleRadians), 0.98 + 0.756 * Math.cos(upperArmAngleRadians)));

        // System.out.println("OLD DIMENSIONS");
        // System.out.println("Lower Arm: " + Rotation2d.fromRadians(lowerArmAngleRadians));
        // System.out.println("Upper Arm: " + Rotation2d.fromRadians(upperArmAngleRadians));

        double upperArmAngleRadians = Math.acos((Math.pow(targetX, 2) + Math.pow(targetY, 2) - Math.pow(LOWER_ARM_LENGTH_METRES, 2) - Math.pow(UPPER_ARM_LENGTH_METRES, 2)) / (2 * LOWER_ARM_LENGTH_METRES * UPPER_ARM_LENGTH_METRES));
        double lowerArmAngleRadians = Math.PI / 2 - (Math.atan2(targetY, targetX) + Math.atan2(UPPER_ARM_LENGTH_METRES * Math.sin(upperArmAngleRadians), LOWER_ARM_LENGTH_METRES + UPPER_ARM_LENGTH_METRES * Math.cos(upperArmAngleRadians)));

        System.out.println("Lower Arm: " + Rotation2d.fromRadians(lowerArmAngleRadians));
        System.out.println("Upper Arm: " + Rotation2d.fromRadians(upperArmAngleRadians));

        System.out.println(LOWER_ARM_LENGTH_METRES * Math.sin(lowerArmAngleRadians) + UPPER_ARM_LENGTH_METRES * Math.sin(upperArmAngleRadians) + ARM_BASE_OFFSET.getX().asRaw(Metres::convert));
        System.out.println(LOWER_ARM_LENGTH_METRES * Math.cos(lowerArmAngleRadians) + UPPER_ARM_LENGTH_METRES * Math.cos(upperArmAngleRadians) + ARM_BASE_OFFSET.getY().asRaw(Metres::convert));
    }
}