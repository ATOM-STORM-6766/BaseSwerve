package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class RunIntake extends Command {
    private Intake m_Intake;
    // private boolean m_isFinished = false;

    public RunIntake(Intake intake) {
        m_Intake = intake;
        addRequirements(m_Intake);
    }

    @Override
    public void execute() {
        m_Intake.setSpeed(new double[] { 0.3, 0 });
    }

    @Override
    public void end(boolean interrupted) {
        m_Intake.stop();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
