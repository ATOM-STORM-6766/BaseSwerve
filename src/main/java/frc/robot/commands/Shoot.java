package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class Shoot extends Command {
    private Shooter m_shooter;
    private Intake m_intake;

    public Shoot(Shooter shooter, Intake intake) {
        m_shooter = shooter;
        m_intake = intake;
        addRequirements(m_shooter, m_intake);
    }

    @Override
    public void execute() {
        m_intake.setSpeed(0, 0.3, -0.4);
        m_shooter.setSpeed(0.7, 0.7);
    }

    @Override
    public void end(boolean interrupted) {
        m_shooter.stop();
        m_intake.stop();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
