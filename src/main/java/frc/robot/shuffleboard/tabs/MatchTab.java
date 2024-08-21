
package frc.robot.shuffleboard.tabs;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.shuffleboard.ShuffleboardTabBase;
import frc.robot.subsystems.swerve.Swerve;

public class MatchTab extends ShuffleboardTabBase {

    public MatchTab() {
    }

    private static SendableChooser<Command> m_autoChooser = new SendableChooser<Command>();
    @SuppressWarnings("unused")
    private ComplexWidget m_autoChooserEntry;
    private GenericEntry m_odometryY;
    private GenericEntry m_odometryX;
    private Swerve m_swerve = Swerve.getInstance();
    private Timer time = new Timer();

    @Override
    public void createEntries() {
        m_tab = Shuffleboard.getTab("Match");
        time.start();
        m_autoChooserEntry = createSendableEntry("Auto Chooser", m_autoChooser, new EntryProperties(0, 0, 2, 1));
        m_odometryX = createNumberEntry("Odometry X", 0, new EntryProperties(0, 1));
        m_odometryY = createNumberEntry("Odometry Y", 0, new EntryProperties(1, 1));
    }

    @Override
    public void periodic() {
        if (time.hasElapsed(0.5)) {
            m_odometryX.setDouble(m_swerve.getPose().getX());
            m_odometryY.setDouble(m_swerve.getPose().getY());
        }
    }

    public static SendableChooser<Command> getAutoChooser() {
        return m_autoChooser;
    }
}
