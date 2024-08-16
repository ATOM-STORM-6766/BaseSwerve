package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ElevatorState;
import frc.robot.subsystems.Intake;

public class ToAMP extends Command {
    private final Intake m_intake;
    private final Elevator m_elevator;
    private Timer timer = new Timer();

    public ToAMP(Intake intake, Elevator elevator) {
        m_intake = intake;
        m_elevator = elevator;
        addRequirements(m_intake, m_elevator);
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        if (m_elevator.state == ElevatorState.DOWN_EMPTY) {
            if (timer.get() < 0.3) {
                m_intake.setSpeed(0, 0.35, 0.35);
            } else if (timer.get() < 0.6) {
                m_intake.setSpeed(0.3, 0.35, 0.35);
            } else if (timer.get() < 0.7) {
                m_intake.setSpeed(0, -0.3, 0.35);
            } else {
                m_intake.setSpeed(0, 0.35, 0.35);
            }
            m_elevator.setAmp(0.35);
        }
        if (m_elevator.state == ElevatorState.DOWN_FULL) {
            m_intake.stop();
            m_elevator.stopAMP();
        }
        if (m_elevator.state == ElevatorState.UP_FULL) {
            m_elevator.setAmp(0.35);
        }
        if (m_elevator.state == ElevatorState.UP_EMPTY) {
            m_elevator.stopAMP();
        }
        m_elevator.updateState();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(2);
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
        timer.reset();
        m_elevator.stopAMP();
        m_intake.stop();
    }
}
