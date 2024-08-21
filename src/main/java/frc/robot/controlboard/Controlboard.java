package frc.robot.controlboard;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * A utility class that contains button bindings.
 * 
 * Controlboard allows easy reference of custom button associations.
 */
public class Controlboard {

    public static final double STICK_DEADBAND = 0.05;

    public static final CommandXboxController driverController = new CommandXboxController(0);
    public static final CommandXboxController operatorController = new CommandXboxController(1);

    private static DigitalInput intakelimitSwitch = new DigitalInput(0);

    private static boolean fieldCentric = true;

    /**
     * Retrieves the swerve translation from the driver controller.
     * 
     * @return A DoubleSupplier array representing the x and y values from the
     *         controller.
     */
    public static DoubleSupplier[] getTranslation() {
        return new DoubleSupplier[] {
                () -> -MathUtil.applyDeadband(driverController.getLeftY(), STICK_DEADBAND),
                () -> -MathUtil.applyDeadband(driverController.getLeftX(), STICK_DEADBAND)
        };
    }

    /**
     * Retrieves the rotation value from the driver controller.
     *
     * @return A DoubleSupplier representing the rotation.
     */
    public static DoubleSupplier getRotation() {
        return () -> -MathUtil.applyDeadband(Math.pow(driverController.getRightX(), 3), STICK_DEADBAND);
    }

    /**
     * Retrieves whether to zero the gyro from the driver controller.
     *
     * @return A Trigger representing the state of the start button.
     */
    public static Trigger getZeroGyro() {
        return driverController.back();
    }

    /**
     * Retreives the current field-centric mode.
     *
     * @return True if field-centric; false if robot-centric
     */
    public static BooleanSupplier getFieldCentric() {
        /*
         * Toggles field-centric mode between true and false when the start button is
         * pressed
         */
        driverController.start().onTrue(new InstantCommand(() -> fieldCentric = !fieldCentric));
        return () -> fieldCentric;
    }

    /**
     * Retrieves the intake button from the driver controller.
     *
     * @return A Trigger representing the state of the x button.
     */
    public static Trigger intake() {
        return driverController.x();
    }

    public static Trigger outtake() {
        return driverController.y();
    }

    public static Trigger toggleFlywheel() {
        return driverController.b();
    }

    public static Trigger toShooter() {
        return driverController.a();
    }

    public static Trigger shootTransport() {
        return driverController.leftBumper();
    }

    public static Trigger toElevatorAmp() {
        return operatorController.y();
    }

    public static Trigger resetElevator() {
        return operatorController.x();
    }

    public static Trigger setElevatorHigh() {
        return operatorController.leftBumper();
    }

    public static BooleanSupplier intakeFull() {
        return () -> !intakelimitSwitch.get();
    }

    public static Trigger toggleClimb() {
        return new Trigger(() -> Math.abs(getClimbSpeed().getAsDouble()) > 0.1);
    }

    public static DoubleSupplier getClimbSpeed() {
        return () -> -MathUtil.applyDeadband(operatorController.getLeftY(), 0.1);
    }
}
