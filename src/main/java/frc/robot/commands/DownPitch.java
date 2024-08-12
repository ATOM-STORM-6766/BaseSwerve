package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class DownPitch extends Command {
    private Shooter m_shooter;
    private Supplier<Double> m_speed;

    public DownPitch(Shooter shooter, Supplier<Double> speed) {
        m_shooter = shooter;
        m_speed = speed;
        addRequirements(m_shooter);
    }

    @Override
    public void execute() {
        m_shooter.down(m_speed.get());
    }

    @Override
    public void end(boolean interrupted) {
        m_shooter.setPitch(m_shooter.getPitch());
    }
}
