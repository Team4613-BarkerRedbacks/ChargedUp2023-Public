package redbacks.robot.subsystems.coordination;

import arachne4.lib.units.Duration;
import arachne4.lib.units.Metres;
import arachne4.lib.units.concats.Distance2d;
import edu.wpi.first.math.geometry.Rotation2d;
import redbacks.robot.subsystems.arm.ArmConstants;
import redbacks.robot.subsystems.arm.Arm.ArmPosition;
import redbacks.robot.subsystems.coneIntake.ConeIntakeConstants;
import redbacks.robot.subsystems.cubeIntake.CubeIntakeConstants;
import redbacks.robot.subsystems.cubeIntake.CubeIntakeConstants.IntakePosition;

public class ArmAndCubeCoordinationConstants {
    public static final ArmAndCubePosition
        // Cone positions
        CONE_SCORE_HIGH = new ArmAndCubePosition(
            new ArmPosition(
                new Distance2d(new Metres(0.537), new Metres(0.95)),
                false,
                ConeIntakeConstants.SHOOTING_OUTTAKE_POWER,
                Duration.ZERO,
                ArmConstants.ALIGNMENT_EXTENDED_ANGLE
            ),
            CubeIntakeConstants.STOW_POS
        ),
        CONE_SCORE_MID = new ArmAndCubePosition(
            new ArmPosition(
                new Distance2d(new Metres(0.732), new Metres(0.874)),
                false,
                ConeIntakeConstants.DEFAULT_OUTTAKE_POWER,
                Duration.ZERO,
                ArmConstants.ALIGNMENT_STOW_WITH_CONE_ANGLE
            ),
            CubeIntakeConstants.STOW_POS
        ),
        CONE_SCORE_LOW = new ArmAndCubePosition(
            new ArmPosition(
                new Distance2d(new Metres(0.476), new Metres(0.500)),
                false, // Change to true for automatic outtake
                ConeIntakeConstants.DEFAULT_OUTTAKE_POWER,
                Duration.ZERO, // Ignored when isScoringPosition is false
                ArmConstants.ALIGNMENT_STOW_WITH_CONE_ANGLE
            ),
            CubeIntakeConstants.STOW_POS
        ),
        CONE_PICKUP_FLOOR = new ArmAndCubePosition(
            new ArmPosition(
                new Distance2d(new Metres(0.353), new Metres(0.187+0.01)),
                false, // Change to true for automatic outtake
                ConeIntakeConstants.DEFAULT_OUTTAKE_POWER,
                Duration.ZERO, // Ignored when isScoringPosition is false
                ArmConstants.ALIGNMENT_STOW_WITH_CONE_ANGLE
            ),
            new IntakePosition(Rotation2d.fromDegrees(16.6), CubeIntakeConstants.DEFAULT_OUTTAKE_POWER)
        ),
        REVERSE_CONE_SCORE = new ArmAndCubePosition(
            new ArmPosition(
                new Distance2d(new Metres(-0.523), new Metres(1.04)),
                false, // Change to true for automatic outtake
                ConeIntakeConstants.DEFAULT_OUTTAKE_POWER,
                Duration.ZERO, // Ignored when isScoringPosition is false
                ArmConstants.ALIGNMENT_STOW_WITH_CONE_ANGLE
            ),
            new IntakePosition(Rotation2d.fromDegrees(110), CubeIntakeConstants.DEFAULT_OUTTAKE_POWER)
        ),
        CONE_FROM_HUMAN_PLAYER = new ArmAndCubePosition(
            new ArmPosition(new Distance2d(new Metres(0.537), new Metres(0.929)), ArmConstants.ALIGNMENT_EXTENDED_ANGLE),
            CubeIntakeConstants.STOW_POS
        ),
        STOW_WITH_CONE = new ArmAndCubePosition(
            ArmConstants.STOW_POSITION,
            // new ArmPosition(new Distance2d(new Metres(0.13), new Metres(0.4)), ArmConstants.ALIGNMENT_STOW_WITH_CONE_ANGLE),
            CubeIntakeConstants.STOW_POS
        ),

        // Cube positions
        CUBE_SCORE_HIGH = new ArmAndCubePosition(
            ArmConstants.STOW_POSITION,
            new IntakePosition(Rotation2d.fromDegrees(32), -0.95)
        ),
        CUBE_SCORE_MID = new ArmAndCubePosition(
            ArmConstants.STOW_POSITION,
            new IntakePosition(Rotation2d.fromDegrees(34), -0.55)
        ),
        CUBE_SCORE_LOW = new ArmAndCubePosition(
            ArmConstants.STOW_POSITION,
            new IntakePosition(Rotation2d.fromDegrees(76), -0.15, -0.5)
        ),
        CUBE_SCORE_SLOW_LOW = new ArmAndCubePosition(
            ArmConstants.STOW_POSITION,
            new IntakePosition(Rotation2d.fromDegrees(76), -0.15)
        ),
        CUBE_FROM_GROUND = new ArmAndCubePosition(
            ArmConstants.STOW_POSITION,
            new IntakePosition(Rotation2d.fromDegrees(111), CubeIntakeConstants.DEFAULT_OUTTAKE_POWER)
        ),
        CUBE_LONG_SHOT = new ArmAndCubePosition(
            ArmConstants.STOW_POSITION,
            new IntakePosition(Rotation2d.fromDegrees(51), -1)
        ),
        CUBE_LOW_LONG_SHOT = new ArmAndCubePosition(
            ArmConstants.STOW_POSITION,
            new IntakePosition(Rotation2d.fromDegrees(90), -1)
        ),
        CUBE_LONG_SHOT_FROM_CHARGE_STATION = new ArmAndCubePosition(
            ArmConstants.STOW_POSITION,
            new IntakePosition(Rotation2d.fromDegrees(60), -1)
        ),

        // Auto positions
        AUTO_PREPARE_CONE_FROM_GROUND = new ArmAndCubePosition(
            new ArmPosition(new Distance2d(new Metres(0.50), new Metres(0.50)), ArmConstants.ALIGNMENT_EXTENDED_ANGLE),
            CubeIntakeConstants.STOW_POS
        ),
        AUTO_CONE_FROM_GROUND = new ArmAndCubePosition(
            new ArmPosition(new Distance2d(new Metres(0.409), new Metres(0.379)), ArmConstants.ALIGNMENT_EXTENDED_ANGLE),
            CubeIntakeConstants.STOW_POS
        ),
        AUTO_RETRACT_AFTER_HIGH_GOAL = new ArmAndCubePosition(
            new ArmPosition(new Distance2d(new Metres(1.00), new Metres(1.15)), ArmConstants.ALIGNMENT_EXTENDED_ANGLE),
            CubeIntakeConstants.STOW_POS
        ),

        AUTO_CONE_SCORE_HIGH = new ArmAndCubePosition(
            new ArmPosition(new Distance2d(new Metres(1.139), new Metres(1.101)), ArmConstants.ALIGNMENT_EXTENDED_ANGLE),
            CubeIntakeConstants.STOW_POS
        ),

        AUTO_CUBE_FLOOR_SHOT = new ArmAndCubePosition(
            ArmConstants.STOW_POSITION,
            new IntakePosition(Rotation2d.fromDegrees(80), -1)
        ),

        // Shared positions
        STOW_ALL = new ArmAndCubePosition(
            ArmConstants.STOW_POSITION,
            CubeIntakeConstants.STOW_POS
        );

    public static final class ArmAndCubePosition {
        public ArmPosition armPosition;
        public IntakePosition cubeIntakePosition;

        ArmAndCubePosition(ArmPosition armPosition, IntakePosition cubeIntakePosition) {
            this.armPosition = armPosition;
            this.cubeIntakePosition = cubeIntakePosition;
        }
    }
}
