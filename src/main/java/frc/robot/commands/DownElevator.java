package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;

public class DownElevator extends Command {
    private Elevator m_elevator;

    public DownElevator(Elevator elevator) {
        m_elevator = elevator;
        addRequirements(m_elevator);
    }

    @Override
    public void execute() {
        m_elevator.down(0.3);
        System.out.println("DownElevator.execute()");
    }

    @Override
    public void end(boolean interrupted) {
        m_elevator.upToHeight(0);
    }

    @Override
    public boolean isFinished() {
        return m_elevator.getHeight() <= 2;
    }

}
