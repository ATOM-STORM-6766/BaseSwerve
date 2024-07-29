package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class ShootAMP extends Command {
    private Shooter m_shooter;
    private Intake m_intake;

    public ShootAMP(Shooter shooter, Intake intake) {
        m_shooter = shooter;
        m_intake = intake;
        addRequirements(m_shooter, m_intake);
    }

    @Override
    public void execute() {
        m_intake.setSpeed(new double[] { 0.3, 0.3 });
        m_shooter.setSpeed(new double[] { 0.11, 0.23 });
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
