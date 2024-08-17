package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ShooterConstants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.swerve.Swerve;

public class ShootTransport extends Command {
    private final Shooter m_shooter;
    private final Swerve m_swerve = Swerve.getInstance();
    private Translation2d targetPosition;
    private boolean isRed;

    public ShootTransport(Shooter shooter) {
        this.m_shooter = shooter;
        addRequirements(m_shooter);
    }

    @Override
    public void initialize() {
        isRed = RobotContainer.getAlliance() == Alliance.Red;
        targetPosition = isRed ? ShooterConstants.redTransportPosition : ShooterConstants.blueTransportPosition;
    }

    @Override
    public void execute() {
        var robotPosition = m_swerve.getPose().getTranslation();
        Rotation2d angle = new Rotation2d(robotPosition.getX() - targetPosition.getX(),
                robotPosition.getY() - targetPosition.getY());
        if (isRed) {
            angle = angle.plus(Rotation2d.fromDegrees(180));
        }
        m_shooter.setPitch(30);
        m_shooter.setSpeed(0.75, 0.75);
        m_swerve.headTo(angle, true);
    }

    @Override
    public void end(boolean interrupted) {
        m_shooter.stop();
        m_shooter.setPitch(0);
        m_swerve.headTo(null, false);
    }
}