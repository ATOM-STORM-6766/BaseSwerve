package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;

public class UpElevator extends Command {
    private Elevator m_elevator;

    public UpElevator(Elevator elevator) {
        m_elevator = elevator;
        addRequirements(m_elevator);
    }

    @Override
    public void execute() {
        m_elevator.up(0.3);
        System.out.println("UpElevator.execute()");
    }

    @Override
    public void end(boolean interrupted) {
        m_elevator.upToHeight(24.664);
    }

    @Override
    public boolean isFinished() {
        return m_elevator.getHeight() >= 23.5;
    }
}
