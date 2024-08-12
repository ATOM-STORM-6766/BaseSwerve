package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

public class PutAMP extends Command {
    private Intake m_intake;
    private Elevator m_elevator;

    public PutAMP(Intake intake, Elevator elevator) {
        m_intake = intake;
        m_elevator = elevator;
        addRequirements(m_intake, m_elevator);
    }

    @Override
    public void execute() {
        m_intake.setSpeed(0, 0.3, 0.3);
        m_elevator.setAmp(0.3);
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.stop();
        m_elevator.stopAMP();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
