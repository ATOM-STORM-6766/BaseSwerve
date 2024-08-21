package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ShooterConstants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LED;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.swerve.Swerve;

public class ShootAim extends Command {
    private final Shooter m_shooter;
    private final Swerve m_swerve = Swerve.getInstance();
    private Translation2d targetPosition;
    private boolean isRed;
    private boolean isOut = true;

    public ShootAim(Shooter shooter) {
        this.m_shooter = shooter;
        addRequirements(m_shooter);
    }

    @Override
    public void initialize() {
        isRed = RobotContainer.getAlliance() == Alliance.Red;
        targetPosition = isRed ? ShooterConstants.redTargetPosition : ShooterConstants.blueTargetPosition;
    }

    @Override
    public void execute() {
        var robotPosition = m_swerve.getPose().getTranslation();
        double length = robotPosition.getDistance(targetPosition);

        if (length > 3.6 && !isOut) {
            LED.getInstance().disabled();
            isOut = true;
            return;
        }
        if (isOut) {
            LED.getInstance().enterShootingRange();
            isOut = false;
        }
    }
}