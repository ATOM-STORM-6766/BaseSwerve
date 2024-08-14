
package frc.robot.shuffleboard.tabs;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.shuffleboard.ShuffleboardTabBase;
import frc.robot.subsystems.Shooter;

public class MatchTab extends ShuffleboardTabBase {

    public MatchTab() {
    }

    private static SendableChooser<Command> m_autoChooser = new SendableChooser<Command>();
    @SuppressWarnings("unused")
    private ComplexWidget m_autoChooserEntry;

    private GenericEntry m_upFlywhell;
    private GenericEntry m_downFlywheel;
    private GenericEntry m_pitch;

    @Override
    public void createEntries() {
        m_tab = Shuffleboard.getTab("Match");

        m_autoChooserEntry = createSendableEntry("Auto Chooser", m_autoChooser, new EntryProperties(0, 0, 2, 1));
        m_upFlywhell = createNumberEntry("Up Flywheel", 0, new EntryProperties(0, 1, 1, 1));
        m_downFlywheel = createNumberEntry("Down Flywheel", 0, new EntryProperties(1, 1, 1, 1));
        m_pitch = createNumberEntry("Pitch", 0, new EntryProperties(0, 2, 1, 1));
    }

    @Override
    public void periodic() {
        Shooter.upSpeed = m_upFlywhell.getDouble(0);
        Shooter.downSpeed = m_downFlywheel.getDouble(0);
        Shooter.m_pitch = m_pitch.getDouble(0);
    }

    public static SendableChooser<Command> getAutoChooser() {
        return m_autoChooser;
    }
}
