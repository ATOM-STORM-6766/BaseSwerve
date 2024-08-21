package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LED;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.swerve.Swerve;

public class AutoAim extends Command {
    private final Shooter m_shooter;
    private final Swerve m_swerve = Swerve.getInstance();
    private Translation2d targetPosition;
    private boolean isRed;
    private BooleanSupplier fieldRelativeSup;

    public AutoAim(Shooter shooter, BooleanSupplier fieldRelativeSup) {
        this.m_shooter = shooter;
        this.fieldRelativeSup = fieldRelativeSup;
        addRequirements(m_shooter);
    }

    @Override
    public void initialize() {
        isRed = RobotContainer.getAlliance() == Alliance.Red;
        targetPosition = isRed ? ShooterConstants.redTargetPosition : ShooterConstants.blueTargetPosition;
        LED.getInstance().prepareToShoot();
    }

    @Override
    public void execute() {
        if (!fieldRelativeSup.getAsBoolean()) {
            m_shooter.autoRun(1.35);
            return;
        }
        var robotPosition = m_swerve.getPose().getTranslation();
        double length = robotPosition.getDistance(targetPosition);
        if (length > 3.6) {
            m_shooter.stop();
            m_shooter.holdPitch();
            m_swerve.headTo(null, false);
            LED.getInstance().outShootingRange();
            return;
        }
        m_shooter.autoRun(length);

        Rotation2d angle = new Rotation2d(robotPosition.getX() - targetPosition.getX(),
                robotPosition.getY() - targetPosition.getY());
        if (isRed) {
            angle = angle.plus(Rotation2d.fromDegrees(180));
        }
        if (setAngle(angle) < 0.05) {
            LED.getInstance().readyToShoot();
        }
        if (DriverStation.isAutonomous()) {
            m_swerve.setChassisSpeeds(
                    ChassisSpeeds.discretize(
                            0, 0, setAngle(angle),
                            Constants.LOOP_TIME_SEC),
                    true);
            return;
        }
        m_swerve.headTo(angle, true);
    }

    private double setAngle(Rotation2d lastAngle) {
        return m_swerve.calculateHeadingCorrection(MathUtil.inputModulus(m_swerve.getYaw().getDegrees(), -180, 180),
                MathUtil.inputModulus(lastAngle.getDegrees(), -180, 180));
    }

    @Override
    public void end(boolean interrupted) {
        m_shooter.stop();
        m_shooter.setPitch(0);
        m_swerve.headTo(null, false);
    }
}
