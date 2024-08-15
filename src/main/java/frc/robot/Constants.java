package frc.robot;

import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import frc.lib.pid.ScreamPIDConstants;
import frc.lib.util.COTSFalconSwerveConstants;

/**
 * A class for constants used in various places in the project.
 */
public final class Constants {

    public record MotionMagicConstants(double cruiseVelocity, double acceleration, int sCurveStrength) {
    }

    /* Robot loop time */
    public static final double LOOP_TIME_SEC = 0.02;
    public static final double LOOP_TIME_HZ = 1 / LOOP_TIME_SEC;

    public static final class Ports {
        /**
         * Possible CAN bus strings are:
         * "rio" for the native roboRIO CAN bus
         * CANivore name or serial number
         * "*" for any CANivore seen by the program
         */
        public static final String CAN_BUS_NAME = "CANivore";
        /* Pigeon2 */
        public static final int PIGEON_ID = 10;
    }

    public static final class ShuffleboardConstants {

        /* For updating values like PID from Shuffleboard */
        public static final boolean UPDATE_SWERVE = true;
    }

    public static final class SwerveConstants {

        /* Drivebase Constants */
        public static final double TRACK_WIDTH = 0.51685; // Distance from left wheels to right wheels
        public static final double WHEEL_BASE = 0.51685; // Distance from front wheels to back wheels

        /* Gyro Constants */
        public static final boolean GYRO_INVERT = false; // Always ensure gyro reads CCW+ CW-

        /* Swerve Kinematics */
        public static final double MAX_SPEED = 5.7349; // m/s
        public static final double MAX_ANGULAR_VELOCITY = 8.0; // rad/s

        /* Swerve Kinematics */
        // No need to ever change this unless there are more than four modules.
        public static final SwerveDriveKinematics KINEMATICS = new SwerveDriveKinematics(
                new Translation2d(WHEEL_BASE / 2.0, TRACK_WIDTH / 2.0),
                new Translation2d(WHEEL_BASE / 2.0, -TRACK_WIDTH / 2.0),
                new Translation2d(-WHEEL_BASE / 2.0, TRACK_WIDTH / 2.0),
                new Translation2d(-WHEEL_BASE / 2.0, -TRACK_WIDTH / 2.0));

        /* Selected Module Constants */
        public static final COTSFalconSwerveConstants MODULE_TYPE = COTSFalconSwerveConstants
                .SDSMK4i(COTSFalconSwerveConstants.driveGearRatios.L3);

        /* Swerve Heading Correction */
        public static final ScreamPIDConstants HEADING_CONSTANTS = new ScreamPIDConstants(0.07, 0.003, 0.0);
        public static final double CORRECTION_TIME_THRESHOLD = 0.2;

        /* PathPlanner Constants */
        public static final ScreamPIDConstants PATH_TRANSLATION_CONSTANTS = new ScreamPIDConstants(20, 0.0, 0.0);
        public static final ScreamPIDConstants PATH_ROTATION_CONSTANTS = new ScreamPIDConstants(45, 0.0, 0.0);

        public static final HolonomicPathFollowerConfig PATH_FOLLOWER_CONFIG = new HolonomicPathFollowerConfig(
                PATH_TRANSLATION_CONSTANTS.toPathPlannerPIDConstants(),
                PATH_ROTATION_CONSTANTS.toPathPlannerPIDConstants(),
                MAX_SPEED,
                new Translation2d(WHEEL_BASE / 2, TRACK_WIDTH / 2).getNorm(),
                new ReplanningConfig(),
                LOOP_TIME_SEC);

        public static final class DriveConstants {
            /* Gear Ratio */
            public static final double GEAR_RATIO = MODULE_TYPE.driveGearRatio;

            /* Neutral Mode */
            public static final NeutralModeValue NEUTRAL_MODE = NeutralModeValue.Brake;

            /* Motor Invert */
            public static final InvertedValue MOTOR_INVERT = MODULE_TYPE.driveMotorInvert;

            /* Current Limit Constants */
            public static final int SUPPLY_CURRENT_LIMIT = 35;
            public static final int SUPPLY_CURRENT_THRESHOLD = 60;
            public static final double SUPPLY_TIME_THRESHOLD = 0.1;
            public static final boolean CURRENT_LIMIT_ENABLE = true;
            public static final double SLIP_CURRENT = 400;

            /* Ramps */
            public static final double OPEN_LOOP_RAMP = 0.25;
            public static final double CLOSED_LOOP_RAMP = 0.0;

            /* PID Constants */
            public static final double KP = 2.0; // TODO ROBOT SPECIFIC
            public static final double KI = 0.0;
            public static final double KD = 0.5;
            public static final double KF = 0.0;
            public static final ScreamPIDConstants PID_CONSTANTS = new ScreamPIDConstants(KP, KI, KD, KF);

            /* Feedforward Constants */
            public static final double KS = 1.0; // TODO ROBOT SPECIFIC
            public static final double KV = 0.0;
            public static final double KA = 0.0;
            public static SimpleMotorFeedforward FOWARD = new SimpleMotorFeedforward(DriveConstants.KS,
                    DriveConstants.KV,
                    DriveConstants.KA);
        }

        public static final class SteerConstants {
            /* Gear Ratio */
            public static final double GEAR_RATIO = MODULE_TYPE.steerGearRatio;

            /* Motor Invert */
            public static final InvertedValue MOTOR_INVERT = MODULE_TYPE.steerMotorInvert;

            /* Neutral Modes */
            public static final NeutralModeValue NEUTRAL_MODE = NeutralModeValue.Brake;

            /* Current Limits */
            public static final int SUPPLY_CURRENT_LIMIT = 25;
            public static final int SUPPLY_CURRENT_THRESHOLD = 40;
            public static final double SUPPLY_TIME_THRESHOLD = 0.1;
            public static final boolean CURRENT_LIMIT_ENABLE = true;

            /* PID Constants */
            public static final double KP = MODULE_TYPE.steerKP;
            public static final double KI = MODULE_TYPE.steerKI;
            public static final double KD = MODULE_TYPE.steerKD;
            public static final double KF = MODULE_TYPE.steerKF;
            public static final ScreamPIDConstants PID_CONSTANTS = new ScreamPIDConstants(KP, KI, KD, KF);
        }

        public static class ModuleConstants {

            public record SwerveModuleConstants(int driveMotorID, int steerMotorID, int encoderID,
                    Rotation2d angleOffset) {
            }

            public enum ModuleLocation {
                FRONT_LEFT(0),
                FRONT_RIGHT(1),
                BACK_LEFT(2),
                BACK_RIGHT(3);

                private int number;

                private ModuleLocation(int number) {
                    this.number = number;
                }

                public int getNumber() {
                    return number;
                }
            }

            /* Front Left */
            public static final SwerveModuleConstants MODULE_3 = new SwerveModuleConstants(
                    2,
                    3,
                    11,
                    Rotation2d.fromRotations(-0.359)); // ROBOT SPECIFIC

            /* Front Right */
            public static final SwerveModuleConstants MODULE_2 = new SwerveModuleConstants(
                    8,
                    9,
                    14,
                    Rotation2d.fromRotations(-0.735)); // ROBOT SPECIFIC

            /* Back Left */
            public static final SwerveModuleConstants MODULE_1 = new SwerveModuleConstants(
                    4,
                    5,
                    12,
                    Rotation2d.fromRotations(-0.187)); // ROBOT SPECIFIC

            /* Back Right */
            public static final SwerveModuleConstants MODULE_0 = new SwerveModuleConstants(
                    6,
                    7,
                    13,
                    Rotation2d.fromRotations(-0.662)); // ROBOT SPECIFIC
        }
    }

    public static final class IntakeConstants {
        public static final int INTAKE_MOTOR_ID = 20;
        public static final int TRANSPORT_MOTOR_ID = 21;
        public static final int SORTING_MOTOR_ID = 22;
    }

    public static final class ShooterConstants {
        public static double RATIO = 7.0 / 1 * 30 / 18 * 86 / 10;
        public static final int PITCH_MOTOR_ID = 23;
        public static final int UP_FLYWHELL_ID = 24;
        public static final int DOWN_FLYWHELL_ID = 25;

        public static final double flywheelCircumference = Units.inchesToMeters(3) * Math.PI;

        public static final Translation2d redTargetPosition = new Translation2d(16.54175, 5.547868);// 红方低音炮底部，正对4号标签
        public static final Translation2d blueTargetPosition = new Translation2d(0.0, 5.547868);// 蓝方低音炮底部，正对7号标签

        public static InterpolatingDoubleTreeMap upperMap = new InterpolatingDoubleTreeMap();
        public static InterpolatingDoubleTreeMap lowerMap = new InterpolatingDoubleTreeMap();
        public static InterpolatingDoubleTreeMap angleMap = new InterpolatingDoubleTreeMap();

        static {
            upperMap.put(1.325, 0.6);
            lowerMap.put(1.325, 0.6);
            angleMap.put(1.325, 47.5);

            upperMap.put(1.425, 0.6);
            lowerMap.put(1.425, 0.6);
            angleMap.put(1.425, 47.0);

            upperMap.put(1.525, 0.6);
            lowerMap.put(1.525, 0.6);
            angleMap.put(1.525, 46.5);

            upperMap.put(1.625, 0.6);
            lowerMap.put(1.625, 0.6);
            angleMap.put(1.625, 46.0);

            upperMap.put(1.725, 0.6);
            lowerMap.put(1.725, 0.6);
            angleMap.put(1.725, 45.5);

            upperMap.put(1.825, 0.6);
            lowerMap.put(1.825, 0.6);
            angleMap.put(1.825, 45.0);

            upperMap.put(1.925, 0.6);
            lowerMap.put(1.925, 0.6);
            angleMap.put(1.925, 40.0);

            upperMap.put(2.025, 0.6);
            lowerMap.put(2.025, 0.6);
            angleMap.put(2.025, 39.0);

            upperMap.put(2.125, 0.6);
            lowerMap.put(2.125, 0.6);
            angleMap.put(2.125, 37.0);

            upperMap.put(2.225, 0.6);
            lowerMap.put(2.225, 0.6);
            angleMap.put(2.225, 35.5);

            upperMap.put(2.325, 0.6);
            lowerMap.put(2.325, 0.6);
            angleMap.put(2.325, 34.0);

            upperMap.put(2.425, 0.6);
            lowerMap.put(2.425, 0.6);
            angleMap.put(2.425, 33.0);

            upperMap.put(2.525, 0.6);
            lowerMap.put(2.525, 0.6);
            angleMap.put(2.525, 32.0);

            upperMap.put(2.625, 0.6);
            lowerMap.put(2.625, 0.6);
            angleMap.put(2.625, 31.0);

            upperMap.put(2.725, 0.6);
            lowerMap.put(2.725, 0.6);
            angleMap.put(2.725, 30.5);

            upperMap.put(2.825, 0.6);
            lowerMap.put(2.825, 0.6);
            angleMap.put(2.825, 30.0);

            upperMap.put(2.925, 0.6);
            lowerMap.put(2.925, 0.6);
            angleMap.put(2.925, 29.5);

            upperMap.put(3.025, 0.6);
            lowerMap.put(3.025, 0.6);
            angleMap.put(3.025, 29.0);

            upperMap.put(3.125, 0.6);
            lowerMap.put(3.125, 0.6);
            angleMap.put(3.125, 28.5);

            upperMap.put(3.225, 0.6);
            lowerMap.put(3.225, 0.6);
            angleMap.put(3.225, 28.0);

            upperMap.put(3.325, 0.6);
            lowerMap.put(3.325, 0.6);
            angleMap.put(3.325, 27.2);

            upperMap.put(3.425, 0.6);
            lowerMap.put(3.425, 0.6);
            angleMap.put(3.425, 26.8);

            upperMap.put(3.525, 0.64);
            lowerMap.put(3.525, 0.64);
            angleMap.put(3.525, 25.2);

            upperMap.put(3.625, 0.64);
            lowerMap.put(3.625, 0.68);
            angleMap.put(3.625, 25.3);

            upperMap.put(3.725, 0.65);
            lowerMap.put(3.725, 0.7);
            angleMap.put(3.725, 25.3);

            upperMap.put(3.825, 0.65);
            lowerMap.put(3.825, 0.7);
            angleMap.put(3.825, 25.3);
        }

    }

    public static final class ElevatorConstants {
        public static final int ELEVATOR_MOTOR_1_ID = 26;
        public static final int ELEVATOR_MOTOR_2_ID = 27;
        public static final int AMP_MOTOR_ID = 28;
    }

    public static class Vision {
        public static final String kCameraName = "Arducam_OV9281_Right_Front";
        // Cam mounted facing forward, half a meter forward of center, half a meter up
        // from center.
        public static final Transform3d kRobotToCam = new Transform3d(new Translation3d(0.155312, 0, 0.607577),
                new Rotation3d(0, -0.401426, 3.106686));

        // The layout of the AprilTags on the field
        public static final AprilTagFieldLayout kTagLayout = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();

        // The standard deviations of our vision estimated poses, which affect
        // correction rate
        // (Fake values. Experiment and determine estimation noise on an actual robot.)

        public static final Matrix<N3, N1> kSingleTagStdDevs = VecBuilder.fill(4, 4, 8);
        public static final Matrix<N3, N1> kMultiTagStdDevs = VecBuilder.fill(0.5, 0.5, 1);
    }
}
