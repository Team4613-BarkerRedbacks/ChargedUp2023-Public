package redbacks.smart.hardware;

import java.util.function.Supplier;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderStatusFrame;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;

public class SmartCancoder {
    private static final int
        CONFIG_TIMEOUT = 0,
        STATUS_FRAME_PERIOD = 250;

    private final CANCoder cancoder;

    public SmartCancoder(CANCoder cancoder, Rotation2d offset) {
        this.cancoder = cancoder;

        retryOnError(() -> cancoder.configFactoryDefault(CONFIG_TIMEOUT), createLabel("Factory Default"));
        retryOnError(() -> cancoder.configAbsoluteSensorRange(AbsoluteSensorRange.Signed_PlusMinus180, CONFIG_TIMEOUT), createLabel("Range"));
        retryOnError(() -> cancoder.configMagnetOffset(offset.getDegrees(), CONFIG_TIMEOUT), createLabel("Offset"));
        retryOnError(() -> cancoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, STATUS_FRAME_PERIOD), createLabel("Status Frame - Data"));
        retryOnError(() -> cancoder.setStatusFramePeriod(CANCoderStatusFrame.VbatAndFaults, STATUS_FRAME_PERIOD), createLabel("Status Frame - VBAT"));
    }

    public SmartAngleSensor createAngleSensor() {
        return new SmartAngleSensor() {
            @Override
            public Rotation2d getAngle() {
                return Rotation2d.fromDegrees(cancoder.getAbsolutePosition());
            }
        };
    }

    private String createLabel(String task) {
        return "CANCoder " + cancoder.getDeviceID() + " [" + task + "]";
    }

    private void retryOnError(Supplier<ErrorCode> configure, String label) {
        ErrorCode result = configure.get();

        while(result != ErrorCode.OK) {
            System.err.println("Configuration of " + label + " failed with result '" + result + "', retrying in 100ms...");
            Timer.delay(0.1);
            result = configure.get();
        }
    }
}
