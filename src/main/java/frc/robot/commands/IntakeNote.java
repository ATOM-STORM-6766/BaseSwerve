package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class IntakeNote extends Command {
    private final Intake m_intake;

    public IntakeNote(Intake m_intake) {
        this.m_intake = m_intake;
        addRequirements(m_intake);
    }

    @Override
    public void initialize() {
        m_intake.setSpeed(0.4, 0.4, 0);
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.stop();
    }

}
