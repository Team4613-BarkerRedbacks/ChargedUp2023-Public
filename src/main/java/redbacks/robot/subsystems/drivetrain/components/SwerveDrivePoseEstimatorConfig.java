package redbacks.robot.subsystems.drivetrain.components;

import arachne4.lib.units.Duration;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;

public class SwerveDrivePoseEstimatorConfig {
    private final Matrix<N3, N1> stateStdDevsMetresMetresRadians, visionStdDevsMetresMetresRadians;
    private final Matrix<N1, N1> gyroStdDevsRadians;
    private final Duration nominalDeltaTime;

    public SwerveDrivePoseEstimatorConfig(
        Matrix<N3, N1> stateStdDevsMetresMetresRadians,
        Matrix<N1, N1> gyroStdDevsRadians,
        Matrix<N3, N1> visionStdDevsMetresMetresRadians,
        Duration nominalDeltaTime
    ) {
        this.stateStdDevsMetresMetresRadians = stateStdDevsMetresMetresRadians.copy();
        this.gyroStdDevsRadians = gyroStdDevsRadians.copy();
        this.visionStdDevsMetresMetresRadians = visionStdDevsMetresMetresRadians.copy();
        this.nominalDeltaTime = nominalDeltaTime;
    }

    public Matrix<N3, N1> getStateStdDevsMetresMetresRadians() {
        return stateStdDevsMetresMetresRadians.copy();
    }

    public Matrix<N1, N1> getGyroStdDevsRadians() {
        return gyroStdDevsRadians.copy();
    }

    public Matrix<N3, N1> getVisionStdDevsMetresMetresRadians() {
        return visionStdDevsMetresMetresRadians.copy();
    }

    public Duration getNominalDeltaTime() {
        return nominalDeltaTime;
    }
}
