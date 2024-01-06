package redbacks.field;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrajectoryGenerator.ControlVectorList;

import static redbacks.field.PathweaverUtils.*;

public class Trajectories {
    private static final String goTo3GP = "pathweaver/output/goTo3GP.wpilib.json";
    private static final String goToColumn7 = "pathweaver/output/goToColumn7.wpilib.json";
    private static final String goToColumn3 = "pathweaver/output/goToColumn3.wpilib.json";
    private static final String goToChargeStation = "pathweaver/output/goToChargeStation.wpilib.json";
    private static final String goToGP1 = "pathweaver/output/goToGP1.wpilib.json";
    private static final String barrier3GP = "pathweaver/output/barrier3GP.wpilib.json";
    private static final String testTrackPath = "pathweaver/output/testTrackPath.wpilib.json";
    private static final String column2ToBarrier = "pathweaver/output/column2ToBarrier.wpilib.json";

    private static final String COLUMN_2_TO_BARRIER_SOURCENAME = "pathweaver/Paths/column2ToBarrier.path";
    private static final String COLLECT_FIRST_CUBE_SOURCENAME_BLUE = "pathweaver/Paths/collectFirstCube.path";
    private static final String COLLECT2GP_SOURCENAME = "pathweaver/Paths/collect2GP.path";
    private static final String COLLECT2GP_SOURCENAME_RED = "pathweaver/Paths/collect2GP_0.path";
    private static final String MIDDLEFIELD_SOURCENAME = "pathweaver/Paths/middleField.path";
    private static final String COLLECT_FIRST_CUBE_SOURCENAME_RED = "pathweaver/Paths/collectFirstCube_RED.path";

    public static final Trajectory
        goTo3GP_RED = readForRed(goTo3GP),
        goTo3GP_BLUE = readForBlue(goTo3GP),
        goToColumn7_RED = readForRed(goToColumn7),
        goToColumn7_BLUE = readForBlue(goToColumn7), 
        goToColumn3_RED = readForRed(goToColumn3),
        goToColumn3_BLUE = readForBlue(goToColumn3),
        goToChargeStation_RED = readForRed(goToChargeStation),
        goToChargeStation_BLUE = readForBlue(goToChargeStation),
        goToGP1_RED = readForRed(goToGP1),
        goToGP1_BLUE = readForBlue(goToGP1),
        barrier3GP_RED = readForRed(barrier3GP),
        barrier3GP_BLUE = readForBlue(barrier3GP),
        testTrackPath_RED = readForRed(testTrackPath),
        testTrackPath_BLUE = readForBlue(testTrackPath),
        column2ToBarrier_RED = readForRed(column2ToBarrier),
        column2ToBarrier_BLUE = readForBlue(column2ToBarrier);

    public static final ConfigurableTrajectory
        COLUMN_2_TO_BARRIER_RED = new ConfigurableTrajectory(readControlVectorsForRed(COLUMN_2_TO_BARRIER_SOURCENAME)),
        COLLECT_FIRST_CUBE_RED = new ConfigurableTrajectory(readControlVectorsForRed(COLLECT_FIRST_CUBE_SOURCENAME_RED)),
        MIDDLEFIELD_RED = new ConfigurableTrajectory(readControlVectorsForRed(MIDDLEFIELD_SOURCENAME)),
        COLUMN_2_TO_BARRIER_BLUE = new ConfigurableTrajectory(readControlVectorsForBlue(COLUMN_2_TO_BARRIER_SOURCENAME)),
        COLLECT_FIRST_CUBE_BLUE = new ConfigurableTrajectory(readControlVectorsForBlue(COLLECT_FIRST_CUBE_SOURCENAME_BLUE)),
        COLLECT2GP_BLUE = new ConfigurableTrajectory(readControlVectorsForBlue(COLLECT2GP_SOURCENAME)),
        MIDDLEFIELD_BLUE = new ConfigurableTrajectory(readControlVectorsForBlue(MIDDLEFIELD_SOURCENAME)),
        COLLECT2GP_RED = new ConfigurableTrajectory(readControlVectorsForRed(COLLECT2GP_SOURCENAME_RED));


    public static final class ConfigurableTrajectory {
        private final ControlVectorList controlVectors;

        private ConfigurableTrajectory(ControlVectorList controlVectors) {
            this.controlVectors = controlVectors;
        }

        public Trajectory withConfiguration(TrajectoryConfig config) {
            return TrajectoryGenerator.generateTrajectory(controlVectors, config);
        }
    }

    public static void main(String[] args) {
        var trajectory = COLUMN_2_TO_BARRIER_RED.withConfiguration(new TrajectoryConfig(3, 3)
            .setEndVelocity(1)
        );

        System.out.println(trajectory);
    }
}
