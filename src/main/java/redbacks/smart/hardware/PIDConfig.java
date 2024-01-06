package redbacks.smart.hardware;

public class PIDConfig {
    public final double kP, kI, kD, kF;

    public PIDConfig(double kP, double kI, double kD) {
        this(kP, kI, kD, 0);
    }

    public PIDConfig(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }
}
