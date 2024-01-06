package redbacks.field;

// import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import static redbacks.robot.subsystems.drivetrain.DrivetrainConstants.*;

// X Coordinates do not change between red and blue, but the coordinate refers to the respective game object of that team
// (when you are on the red alliance, gamePiece1X refers to a game piece on the red half of the field, but when you are on blue, it refers to a blue game piece)

public final class FieldLocations {
    /* FIELD CONSTANTS */

    // Field
    public static final double
        fieldLength = Units.feetToMeters(54) + Units.inchesToMeters(3.25),
        fieldWidth = Units.feetToMeters(26) + Units.inchesToMeters(3.5),
        tapeWidth = Units.inchesToMeters(2.0);

    // Charging station + Cable protector
    public static final double 
        chargingStationLength = Units.feetToMeters(8) + Units.inchesToMeters(1.25),
        chargingStationWidth =  Units.feetToMeters(4),
        cableProtectorLength = Units.feetToMeters(5) + Units.inchesToMeters(6);

    // Grids
    public static final double 
        gridDepth = Units.feetToMeters(4) + Units.inchesToMeters(8.25),
        gridLength = Units.feetToMeters(18) + Units.inchesToMeters(0.5),
        outerGridLength = Units.feetToMeters(6) + Units.inchesToMeters(3),
        coopGridLength = Units.feetToMeters(5) + Units.inchesToMeters(6),
        lowPoleHeight = Units.feetToMeters(2) + Units.inchesToMeters(10),
        highPoleHeight = Units.feetToMeters(3) + Units.inchesToMeters(10),
        blue_communityEdgeY = 2 * outerGridLength + coopGridLength,
        red_communityEdgeY = -blue_communityEdgeY,
        communityShortEdgeX = Units.feetToMeters(11),
        communityLongEdgeX = 4.85,
        cableProtectorCenterX = 3.76;

    /* FIELD LOCATIONS */

    // Starting positions
    // Starting position 1 is the closest to Y0, and Starting position 3 is the furthest.
    public static final double 
        startingPositionBarrierX = gridDepth + ROBOT_OFFSET_X_FROM_EDGE + Units.inchesToMeters(2),
        blue_startingPositionBarrierY = Units.feetToMeters(1) + Units.inchesToMeters(8.75),
        red_startingPositionBarrierY = - blue_startingPositionBarrierY,

        startingPositionMiddleX = gridDepth + ROBOT_OFFSET_X_FROM_EDGE + Units.inchesToMeters(2),
        blue_startingPositionMiddleY = Units.feetToMeters(10) + Units.inchesToMeters(10.5),
        red_startingPositionMiddleY = - blue_startingPositionMiddleY,

        startingPositionOpenX = gridDepth + ROBOT_OFFSET_X_FROM_EDGE + Units.inchesToMeters(2),
        blue_startingPositionOpenY = Units.feetToMeters(15) + Units.inchesToMeters(17.5),
        red_startingPositionOpenY = - blue_startingPositionOpenY;

    // Grids

    // Each node shares X positions with 9 other nodes, and shares a Y position with 3 others.
    // Column 1 is the closest to Y0, while column 9 is the furthest.
    public static final double gridEdgeX = gridDepth;
    
    public static final double 
        highRowX = Units.feetToMeters(1) + Units.inchesToMeters(2.5),
        middleRowX = highRowX + Units.feetToMeters(1) + Units.inchesToMeters(5),
        hybridRowX = middleRowX + Units.feetToMeters(1) + Units.inchesToMeters(2.5),
        low_reflective_tape_height_metres = 0.610;

    public static final double columnGap = Units.feetToMeters(1) + Units.inchesToMeters(10);

    public static final double
        blue_column1Y = Units.feetToMeters(1) + Units.inchesToMeters(9),
        blue_column2Y = blue_column1Y + columnGap,
        blue_column3Y = blue_column2Y + columnGap,
        blue_column4Y = blue_column3Y + columnGap,
        blue_column5Y = blue_column4Y + columnGap,
        blue_column6Y = blue_column5Y + columnGap,
        blue_column7Y = blue_column6Y + columnGap,
        blue_column8Y = blue_column7Y + columnGap,
        blue_column9Y = blue_column8Y + columnGap,

        red_column1Y = - blue_column1Y,
        red_column2Y = - blue_column2Y,
        red_column3Y = - blue_column3Y,
        red_column4Y = - blue_column4Y,
        red_column5Y = - blue_column5Y,
        red_column6Y = - blue_column6Y,
        red_column7Y = - blue_column7Y,
        red_column8Y = - blue_column8Y,
        red_column9Y = - blue_column9Y;

    // Game pieces
    // Game piece 1 is the closest to Y0, and game piece 4 is the furthest.
    public static final double 
        gamePiece1X = Units.feetToMeters(23) + Units.inchesToMeters(2.25),
        blue_gamePiece1Y = Units.feetToMeters(3) + Units.inchesToMeters(1.25),
        red_gamePiece1Y = - blue_gamePiece1Y,

        gamePiece2X = Units.feetToMeters(23) + Units.inchesToMeters(2.25),
        blue_gamePiece2Y = Units.feetToMeters(7) + Units.inchesToMeters(1.25),
        red_gamePiece2Y = - blue_gamePiece2Y,

        gamePiece3X = Units.feetToMeters(23) + Units.inchesToMeters(2.25),
        blue_gamePiece3Y = Units.feetToMeters(11) + Units.inchesToMeters(1.25),
        red_gamePiece3Y = - blue_gamePiece3Y,

        gamePiece4X = Units.feetToMeters(23) + Units.inchesToMeters(2.25),
        blue_gamePiece4Y = Units.feetToMeters(15) + Units.inchesToMeters(1.25),
        red_gamePiece4Y = - blue_gamePiece4Y;

    // Charging station + Cable protector
    // Charging station corner 1 is the closest to (0, 0). C2 is clockwise from C1, so on.
    public static final double 
        chargeStationCorner1X = Units.feetToMeters(9) + Units.inchesToMeters(2.75),
        blue_chargeStationCorner1Y = Units.feetToMeters(5),
        red_chargeStationCorner1Y = - blue_chargeStationCorner1Y,

        chargeStationCorner2X = Units.feetToMeters(13) + Units.inchesToMeters(2.75),
        blue_chargeStationCorner2Y = Units.feetToMeters(5),
        red_chargeStationCorner2Y = - blue_chargeStationCorner2Y,

        chargeStationCorner3X = Units.feetToMeters(13) + Units.inchesToMeters(2.75),
        blue_chargeStationCorner3Y = Units.feetToMeters(13) + Units.inchesToMeters(1.25),
        red_chargeStationCorner3Y = - blue_chargeStationCorner3Y,

        chargeStationCorner4X = Units.feetToMeters(9) + Units.inchesToMeters(2.75),
        blue_chargeStationCorner4Y = Units.feetToMeters(13) + Units.inchesToMeters(1.25),
        red_chargeStationCorner4Y = - blue_chargeStationCorner4Y;

    // April Tags
    public static final double 
        // Red
        red_aprilTag1X = Units.feetToMeters(3) + Units.inchesToMeters(5),
        red_aprilTag1Y = (Units.feetToMeters(3) + Units.inchesToMeters(6.5)) * -1,

        red_aprilTag2X = Units.feetToMeters(3) + Units.inchesToMeters(5),
        red_aprilTag2Y = (Units.feetToMeters(9) + Units.inchesToMeters(0.75)) * -1,

        red_aprilTag3X = Units.feetToMeters(3) + Units.inchesToMeters(5),
        red_aprilTag3Y = (Units.feetToMeters(14) + Units.inchesToMeters(7)) * -1,

        red_aprilTag4X = Units.feetToMeters(1) + Units.inchesToMeters(2.5),
        red_aprilTag4Y = (fieldWidth - Units.feetToMeters(4) - Units.inchesToMeters(3.25)) * -1,

        red_aprilTag5X = fieldLength - Units.feetToMeters(1) + Units.inchesToMeters(2.5),
        red_aprilTag5Y = (fieldWidth - Units.feetToMeters(4) - Units.inchesToMeters(3.25)) * -1,

        red_aprilTag6X = fieldLength - Units.feetToMeters(3) - Units.inchesToMeters(5),
        red_aprilTag6Y = (Units.feetToMeters(14) + Units.inchesToMeters(7)) * -1,

        red_aprilTag7X = fieldLength - Units.feetToMeters(3) - Units.inchesToMeters(5),
        red_aprilTag7Y = (Units.feetToMeters(9) + Units.inchesToMeters(0.75)) * -1,

        red_aprilTag8X = fieldLength - Units.feetToMeters(3) - Units.inchesToMeters(5),
        red_aprilTag8Y = (Units.feetToMeters(3) + Units.inchesToMeters(6.5)) * -1,

        // // Blue

        blue_aprilTag1X = fieldLength - Units.feetToMeters(3) + Units.inchesToMeters(5),
        blue_aprilTag1Y = Units.feetToMeters(3) + Units.inchesToMeters(6.5),

        blue_aprilTag2X = fieldLength - Units.feetToMeters(3) + Units.inchesToMeters(5),
        blue_aprilTag2Y = Units.feetToMeters(9) + Units.inchesToMeters(0.75),

        blue_aprilTag3X = fieldLength - Units.feetToMeters(3) + Units.inchesToMeters(5),
        blue_aprilTag3Y = Units.feetToMeters(14) + Units.inchesToMeters(7),

        blue_aprilTag4X = fieldLength - Units.feetToMeters(1) + Units.inchesToMeters(2.5),
        blue_aprilTag4Y = fieldWidth - Units.feetToMeters(4) - Units.inchesToMeters(3.25),

        blue_aprilTag5X = Units.feetToMeters(1) + Units.inchesToMeters(2.5),
        blue_aprilTag5Y = fieldWidth - Units.feetToMeters(4) + Units.inchesToMeters(3.25),
        
        blue_aprilTag6X = Units.feetToMeters(3) + Units.inchesToMeters(5),
        blue_aprilTag6Y = Units.feetToMeters(14) + Units.inchesToMeters(7),

        blue_aprilTag7X = Units.feetToMeters(3) + Units.inchesToMeters(5),
        blue_aprilTag7Y = Units.feetToMeters(9) + Units.inchesToMeters(0.75),

        blue_aprilTag8X = Units.feetToMeters(3) + Units.inchesToMeters(5),
        blue_aprilTag8Y = Units.feetToMeters(3) - Units.inchesToMeters(6.5);

        public static double CHARGE_STATION_X_LOSS = 0;

        public static void main(String[] args) {
            // System.out.println(chargeStationCorner2X - ROBOT_OFFSET_X_FROM_EDGE - 0.1);
            // System.out.println(red_column9Y);
            // System.out.println(gridDepth + 0.695/2);
            // System.out.println(blue_column2Y);
            // System.out.println();
            // System.out.println();
            // System.out.println();
            // System.out.println(fieldWidth);

            System.out.println(gridDepth + ROBOT_OFFSET_X_FROM_EDGE);

        }
}