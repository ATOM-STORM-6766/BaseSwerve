package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class UpPitch extends Command {
    private Shooter m_shooter;
    private double m_speed;

    public UpPitch(Shooter shooter, double speed) {
        m_shooter = shooter;
        m_speed = speed;
        addRequirements(m_shooter);
    }

    @Override
    public void execute() {
        m_shooter.up(m_speed);
    }

    @Override
    public void end(boolean interrupted) {
        m_shooter.stop();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
