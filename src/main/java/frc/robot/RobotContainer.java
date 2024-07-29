package frc.robot;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.auto.Autonomous;
import frc.robot.auto.Autonomous.PPEvent;
import frc.robot.auto.Routines;
import frc.robot.commands.DownElevator;
import frc.robot.commands.UpElevator;
import frc.robot.commands.swerve.TeleopSwerve;
import frc.robot.controlboard.Controlboard;
import frc.robot.shuffleboard.ShuffleboardTabManager;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.swerve.Swerve;

public class RobotContainer {

    /* Subsystems */
    private static final Swerve m_swerve = new Swerve();
    private static final Intake m_intake = new Intake();
    private static final Shooter m_shooter = new Shooter();
    private static final Elevator m_elevator = new Elevator();

    private static final ShuffleboardTabManager m_shuffleboardTabManager = new ShuffleboardTabManager();

    private static final Optional<Alliance> m_alliance = DriverStation.getAlliance();

    /**
     * Configures the basic robot systems, such as Shuffleboard, autonomous, default
     * commands, and button bindings.
     */
    public RobotContainer() {
        m_shuffleboardTabManager.addTabs(true);
        configButtonBindings();
        configDefaultCommands();
        configAuto();
    }

    /**
     * Configures button bindings from Controlboard.
     */
    private void configButtonBindings() {
        Controlboard.getZeroGyro().onTrue(new InstantCommand(() -> m_swerve.zeroGyro()));
        Controlboard.driverController.x().onTrue(new InstantCommand(() -> m_intake.setSpeed(new double[] { 0.3, 0 })))
                .onFalse(new InstantCommand(() -> m_intake.stop()));

        Controlboard.driverController.a().onTrue(
                new InstantCommand(() -> m_intake.setSpeed(new double[] { 0, -0.4 })))
                .onFalse(new InstantCommand(() -> m_intake.stop()));

        Controlboard.driverController.b()
                .onTrue(new InstantCommand(() -> m_shooter.setSpeed(new double[] { 1, 1 })))
                .onFalse(new InstantCommand(() -> m_shooter.stop()));

        Controlboard.driverController.y().onTrue(new InstantCommand(() -> m_intake.setSpeed(new double[] { 0.3, 0.3 }))
                .andThen(new InstantCommand(() -> m_elevator.setAmp(0.2)))).onFalse(
                        new InstantCommand(() -> m_intake.stop()).andThen(new InstantCommand(() -> m_elevator.stop())));

        // //Controlboard.driverController.b().onTrue(new Shoot(m_shooter, m_intake));
        // Controlboard.driverController.leftTrigger()
        // .onTrue(new UpPitch(m_shooter,
        // Controlboard.driverController.getLeftTriggerAxis()));
        // Controlboard.driverController.rightTrigger()
        // .onTrue(new DownPitch(m_shooter,
        // Controlboard.driverController.getRightTriggerAxis()));

        // Controlboard.driverController.leftBumper().onTrue(new InstantCommand(() ->
        // m_elevator.up(0.3)))
        // .onFalse(new InstantCommand(() -> m_elevator.stop()));
        Controlboard.driverController.leftBumper().onTrue(new UpElevator(m_elevator));
        Controlboard.driverController.rightBumper().onTrue(new DownElevator(m_elevator));
    }

    private void configDefaultCommands() {
        /* Sets the default command for the swerve subsystem */
        m_swerve.setDefaultCommand(
                new TeleopSwerve(
                        m_swerve,
                        Controlboard.getTranslation(),
                        Controlboard.getRotation(),
                        Controlboard.getFieldCentric()));
    }

    /**
     * Configures auto.
     * Configure default auto and named commands with configure(Command defaultAuto,
     * NamedCommand... namedCommands)
     * <p>
     * ^^ THE ABOVE STEP MUST BE DONE FIRST ^^
     * <p>
     * Add auto routines with addCommands(Command... commands)
     */
    private void configAuto() {
        Autonomous.configure(
                Commands.none().withName("Do Nothing"),
                new PPEvent("ExampleEvent", new PrintCommand("This is an example event :)")));

        Autonomous.addRoutines(
                Routines.exampleAuto().withName("Example Auto"));
    }

    /**
     * Retrieves the selected autonomous command from the autonomous chooser.
     *
     * @return The selected autonomous command.
     */
    public Command getAutonomousCommand() {
        System.out.println("[Auto] Selected auto routine: " + Autonomous.getSelected().getName());
        return Autonomous.getSelected();
    }

    /**
     * Retrieves the Swerve subsystem.
     *
     * @return The Swerve subsystem.
     */
    public static Swerve getSwerve() {
        return m_swerve;
    }

    /**
     * Retrieves the current Alliance as detected by the DriverStation.
     * Use this opposed to DriverStation.getAlliance().
     * 
     * @return The current Alliance.
     */
    public static Alliance getAlliance() {
        if (m_alliance.isPresent()) {
            return m_alliance.get();
        } else {
            DriverStation.reportError("[ERROR] Could not retrieve Alliance", true);
            return Alliance.Blue;
        }
    }
}
