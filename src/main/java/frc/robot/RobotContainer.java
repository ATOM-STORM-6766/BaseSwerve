package frc.robot;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.auto.Autonomous;
import frc.robot.auto.Autonomous.PPEvent;
import frc.robot.auto.Routines;
import frc.robot.commands.DownPitch;
import frc.robot.commands.UpPitch;
import frc.robot.commands.swerve.TeleopSwerve;
import frc.robot.controlboard.Controlboard;
import frc.robot.shuffleboard.ShuffleboardTabManager;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LED;

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.swerve.Swerve;

public class RobotContainer {

    /* Subsystems */
    private static final Swerve m_swerve = Swerve.getInstance();
    private static final Intake m_intake = new Intake();
    private static final Shooter m_shooter = Shooter.getInstance();
    private static final Elevator m_elevator = new Elevator();
    private static final LED m_led = new LED();

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

        Controlboard.intake()
                .toggleOnTrue(Commands.startEnd(() -> {
                    m_intake.setSpeed(0.4, 0.4, 0);
                    // m_led.isIntake();
                }, () -> m_intake.stop(), m_intake));

        Controlboard.intakeFull()
                .onTrue(Commands.runOnce(() -> {
                    m_intake.stop();
                    // m_led.intaked();
                }, m_intake));
        Controlboard.toShooter()
                .whileTrue(Commands.startEnd(() -> m_intake.setSpeed(0.2, 0.5, -0.5), () -> m_intake.stop(),
                        m_intake));

        Controlboard.toggleFlywheel()
                .whileTrue(Commands.runEnd(
                        () -> m_shooter.autoRun(m_swerve.getPose().getTranslation(), getAlliance() == Alliance.Red
                                ? new Translation2d(16.57934 - 0.0381, 5.547868)// 红方低音炮底部，正对4号标签
                                : new Translation2d(0.0, 5.547868), m_swerve), // 蓝方低音炮底部，正对7号标签
                        () -> {
                            m_shooter.stop();
                            m_shooter.holdPitch();
                            m_swerve.headTo(null, false);
                        },
                        m_shooter));

        Controlboard.pitchUp().whileTrue(new UpPitch(m_shooter, Controlboard.getPitchUpSpeed()));
        Controlboard.pitchDown().whileTrue(new DownPitch(m_shooter, Controlboard.getPitchDownSpeed()));

        Controlboard.getElevatorAmp()
                .onTrue(Commands.runEnd(() -> m_elevator.putAMP(m_intake), () -> {
                    m_elevator.stopAMP();
                    m_intake.stop();
                }, m_elevator, m_intake).withTimeout(1.5));

        Controlboard.setElevatorHigh().onTrue(Commands.runOnce(() -> m_elevator.toUpperLimit(), m_elevator))
                .onFalse(Commands.runOnce(() -> m_elevator.toLowerLimit(), m_elevator));
        Controlboard.setElevatorLow().whileTrue(Commands.startEnd(() -> {
            m_elevator.setAmp(0.4);
            m_intake.setSpeed(-0.4, -0.4, 0);
        }, () -> {
            m_elevator.stopAMP();
            m_intake.stop();
        }, m_elevator, m_intake));
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
