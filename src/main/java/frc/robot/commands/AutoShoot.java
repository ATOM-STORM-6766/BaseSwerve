package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.swerve.Swerve;

public class AutoShoot extends Command {
    private final Shooter m_shooter;
    private final Swerve m_swerve = Swerve.getInstance();

    public AutoShoot(Shooter shooter) {
        this.m_shooter = shooter;
        addRequirements(m_shooter);
    }

    @Override
    public void execute() {
        m_shooter.autoRun(m_swerve.getPose().getTranslation(), RobotContainer.getAlliance() == Alliance.Red, m_swerve);
    }

    @Override
    public void end(boolean interrupted) {
        m_shooter.stop();
        m_shooter.holdPitch();
        m_swerve.headTo(null, false);
    }
}
