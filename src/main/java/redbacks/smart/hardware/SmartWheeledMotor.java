package redbacks.smart.hardware;

import arachne4.lib.units.Distance;
import arachne4.lib.units.Velocity;

public interface SmartWheeledMotor extends SmartMotor {
    Distance getPosition();
    Velocity getVelocity();

    void setCurrentPosition(Distance position);

    void setTargetPosition(Distance position);
    void setTargetPosition(Distance position, double arbitraryFeedForwardPercent);
    void setTargetVelocity(Velocity velocity);

    void stop();
}
