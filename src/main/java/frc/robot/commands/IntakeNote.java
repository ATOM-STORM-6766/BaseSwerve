package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LED;

public class IntakeNote extends Command {
    private final Intake m_intake;
    private final LED m_led;

    public IntakeNote(Intake m_intake, LED m_led) {
        this.m_intake = m_intake;
        this.m_led = m_led;
        addRequirements(m_intake, m_led);
    }

    @Override
    public void initialize() {
        m_intake.setSpeed(0.4, 0.4, 0);
        m_led.isIntake();
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.stop();
        m_led.intaked();
    }

}
