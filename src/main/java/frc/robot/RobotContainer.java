package frc.robot;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.auto.Autonomous;
import frc.robot.auto.Autonomous.PPEvent;
import frc.robot.auto.Routines;
import frc.robot.commands.AutoAim;
import frc.robot.commands.IntakeNote;
import frc.robot.commands.ShootTransport;
import frc.robot.commands.ToAMP;
import frc.robot.commands.ToShooter;
import frc.robot.commands.swerve.TeleopSwerve;
import frc.robot.controlboard.Controlboard;
import frc.robot.shuffleboard.ShuffleboardTabManager;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LED;

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.swerve.Swerve;

public class RobotContainer {

    /* Subsystems */
    private static final LED m_led = LED.getInstance();
    private static final Swerve m_swerve = Swerve.getInstance();
    private static final Intake m_intake = new Intake();
    private static final Shooter m_shooter = Shooter.getInstance();
    private static final Elevator m_elevator = new Elevator();
    private static final Climb m_climb = new Climb();

    private static final ShuffleboardTabManager m_shuffleboardTabManager = new ShuffleboardTabManager();

    public static Optional<Alliance> m_alliance = DriverStation.getAlliance();

    /**
     * Configures the basic robot systems, such as Shuffleboard, autonomous, default
     * commands, and button bindings.
     */
    public RobotContainer() {
        m_shuffleboardTabManager.addTabs(false);
        configButtonBindings();
        configDefaultCommands();
        configAuto();
        m_led.setUp();
    }

    /**
     * Configures button bindings from Controlboard.
     */
    private void configButtonBindings() {
        Controlboard.getZeroGyro().onTrue(new InstantCommand(() -> m_swerve.zeroGyro()));

        Controlboard.intake().toggleOnTrue(new IntakeNote(m_intake, m_led).until(Controlboard.intakeFull()));

        Controlboard.toShooter().whileTrue(new ToShooter(m_intake));

        Controlboard.toggleFlywheel().whileTrue(new AutoAim(m_shooter, Controlboard.getFieldCentric()));

        Controlboard.toElevatorAmp().onTrue(new ToAMP(m_intake, m_elevator));

        Controlboard.setElevatorHigh().onTrue(Commands.runOnce(() -> m_elevator.toUpperLimit(), m_elevator))
                .onFalse(Commands.runOnce(() -> m_elevator.toLowerLimit(), m_elevator));

        Controlboard.outtake().whileTrue(Commands.startEnd(() -> {
            m_intake.setSpeed(-0.4, -0.4, 0);
        }, () -> {
            m_intake.stop();
        }, m_intake));

        Controlboard.resetElevator().onTrue(Commands.runOnce(() -> m_elevator.resetState(), m_elevator));

        Controlboard.toggleClimb().whileTrue(m_climb.setSpeed(Controlboard.getClimbSpeed()));

        Controlboard.shootTransport().whileTrue(new ShootTransport(m_shooter));
    }

    private void configDefaultCommands() {
        /* Sets the default command for the swerve subsystem */
        m_swerve.setDefaultCommand(
                new TeleopSwerve(
                        m_swerve,
                        Controlboard.getTranslation(),
                        Controlboard.getRotation(),
                        Controlboard.getFieldCentric()));

        // m_shooter.setDefaultCommand(new ShootAim(m_shooter));
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
                new PPEvent("intake", new IntakeNote(m_intake, m_led).until(Controlboard.intakeFull())),
                new PPEvent("autoShoot",
                        Commands.parallel(new AutoAim(m_shooter, Controlboard.getFieldCentric()).withTimeout(1.5),
                                Commands.waitSeconds(1).andThen(new ToShooter(m_intake).withTimeout(0.5)))));

        Autonomous.addRoutines(

                Routines.Point1().withName("Point1"),
                Routines.Point2().withName("Point2"),
                Routines.Point3().withName("Point3"),
                Routines.Point1Shoot3().withName("Point1Shoot3"),
                Routines.Point2Shoot3().withName("Point2Shoot3"),
                Routines.Point3Shoot3().withName("Point3Shoot3"));
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
