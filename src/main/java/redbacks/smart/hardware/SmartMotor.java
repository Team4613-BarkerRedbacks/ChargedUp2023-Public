package redbacks.smart.hardware;

public interface SmartMotor {
    void setPercentageOutput(double power);
    void setToCoast();
    void setToBrake();
}
