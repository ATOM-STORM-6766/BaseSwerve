package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class DownPitch extends Command {
    private Shooter m_shooter;
    private double m_speed;

    public DownPitch(Shooter shooter, double speed) {
        m_shooter = shooter;
        m_speed = speed;
        addRequirements(m_shooter);
    }

    @Override
    public void execute() {
        m_shooter.down(-m_speed);
    }

    @Override
    public void end(boolean interrupted) {
        m_shooter.setPitch(m_shooter.getPitch());
        System.out.println("DownPitch.end()");
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
