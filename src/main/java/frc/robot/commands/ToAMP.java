package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

public class ToAMP extends Command {
    private static Intake m_intake;
    private static Elevator m_elevator;

    public ToAMP(Intake intake, Elevator elevator) {
        m_intake = intake;
        m_elevator = elevator;
        withTimeout(2);
        addRequirements(m_intake, m_elevator);
    }

    @Override
    public void execute() {
        m_elevator.putAMP(m_intake);
    }

    @Override
    public void end(boolean interrupted) {
        m_elevator.stopAMP();
        m_intake.stop();
    }
}
