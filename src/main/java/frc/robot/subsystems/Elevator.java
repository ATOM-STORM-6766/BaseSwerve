package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.config.DeviceConfig;
import frc.robot.Constants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.SwerveConstants.DriveConstants;

public class Elevator extends SubsystemBase {
    private TalonFX m_elevatorMotor1;
    private TalonFX m_elevatorMotor2;
    private TalonFX m_ampMotor;
    private PositionTorqueCurrentFOC m_positionControl = new PositionTorqueCurrentFOC(0);
    private ElevatorState state = ElevatorState.DOWN_EMPTY;
    private Timer timer = new Timer();

    public DigitalInput limitSwitch = new DigitalInput(1);

    public Elevator() {
        m_elevatorMotor1 = new TalonFX(ElevatorConstants.ELEVATOR_MOTOR_1_ID);
        m_elevatorMotor2 = new TalonFX(ElevatorConstants.ELEVATOR_MOTOR_2_ID);
        m_ampMotor = new TalonFX(ElevatorConstants.AMP_MOTOR_ID);
        configMotors();
        m_elevatorMotor1.setPosition(0);
        m_elevatorMotor2.setControl(new Follower(m_elevatorMotor1.getDeviceID(), false));
    }

    public void toUpperLimit() {
        m_elevatorMotor1.setControl(m_positionControl.withPosition(24.664));
        state = state.setUp();
    }

    public void toLowerLimit() {
        m_elevatorMotor1.setControl(m_positionControl.withPosition(0));
        state = state.setDown();
    }

    public double getHeight() {
        return m_elevatorMotor1.getPosition().getValue();
    }

    public void stopAMP() {
        timer.stop();
        timer.reset();
        m_ampMotor.setControl(new NeutralOut());
    }

    public void setAmp(double speed) {
        m_ampMotor.setControl(new VoltageOut(speed * 12).withEnableFOC(true));
    }

    public void putAMP(Intake m_intake) {
        if (state == ElevatorState.DOWN_EMPTY) {
            timer.start();
            if (timer.get() < 0.3) {
                m_intake.setSpeed(0, 0.4, 0.4);
            } else if (timer.get() < 0.6) {
                m_intake.setSpeed(0.3, 0.4, 0.4);
            } else if (timer.get() < 0.7) {
                m_intake.setSpeed(0, -0.3, 0.4);
            } else {
                m_intake.setSpeed(0, 0.4, 0.4);
            }
            setAmp(0.4);
        }
        if (state == ElevatorState.DOWN_FULL) {
            m_intake.stop();
            stopAMP();
        }
        if (state == ElevatorState.UP_FULL) {
            setAmp(0.4);
        }
        if (state == ElevatorState.UP_EMPTY) {
            stopAMP();
        }
        updateState();
    }

    private void updateState() {
        if (state == ElevatorState.DOWN_EMPTY && !limitSwitch.get()) {
            state = state.setFull();
        }
        if (state == ElevatorState.UP_FULL && limitSwitch.get()) {
            state = state.setEmpty();
        }
    }

    public void resetState() {
        state = ElevatorState.DOWN_EMPTY;
    }

    private void configMotors() {
        var config = FXConfig(InvertedValue.CounterClockwise_Positive, NeutralModeValue.Brake);
        config.Slot0 = config.Slot0.withKP(60).withKD(6);
        config.TorqueCurrent.PeakForwardTorqueCurrent = 30;
        config.TorqueCurrent.PeakReverseTorqueCurrent = -30;
        config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 0;
        DeviceConfig.configureTalonFX("Elevator Elevator Motor 1", m_elevatorMotor1,
                config, Constants.LOOP_TIME_HZ);
        DeviceConfig.configureTalonFX("Elevator Elevator Motor 2", m_elevatorMotor2,
                config, Constants.LOOP_TIME_HZ);
        DeviceConfig.configureTalonFX("Elevator Amp Motor", m_ampMotor,
                FXConfig(InvertedValue.CounterClockwise_Positive, NeutralModeValue.Brake),
                Constants.LOOP_TIME_HZ);
    }

    private static TalonFXConfiguration FXConfig(InvertedValue invert, NeutralModeValue neutralMode) {
        TalonFXConfiguration config = new TalonFXConfiguration();
        config.Audio.BeepOnBoot = false;
        config.Audio.BeepOnConfig = false;
        config.MotorOutput = DeviceConfig.FXMotorOutputConfig(invert, neutralMode);
        config.CurrentLimits = DeviceConfig.FXCurrentLimitsConfig(
                DriveConstants.CURRENT_LIMIT_ENABLE,
                DriveConstants.SUPPLY_CURRENT_LIMIT,
                DriveConstants.SUPPLY_CURRENT_THRESHOLD,
                DriveConstants.SUPPLY_TIME_THRESHOLD);
        config.Slot0 = DeviceConfig.FXPIDConfig(DriveConstants.PID_CONSTANTS).withKP(50).withKD(6);
        config.OpenLoopRamps = DeviceConfig.FXOpenLoopRampConfig(DriveConstants.OPEN_LOOP_RAMP);
        config.ClosedLoopRamps = DeviceConfig.FXClosedLoopRampConfig(1);
        config.TorqueCurrent = DeviceConfig.FXTorqueCurrentConfig(DriveConstants.SLIP_CURRENT,
                -DriveConstants.SLIP_CURRENT, 0);
        return config;
    }

    private enum ElevatorState {
        UP_EMPTY, DOWN_EMPTY, DOWN_FULL, UP_FULL;

        public ElevatorState setUp() {
            if (this == DOWN_EMPTY) {
                return UP_EMPTY;
            }
            if (this == DOWN_FULL) {
                return UP_FULL;
            }
            return this;
        }

        public ElevatorState setDown() {
            if (this == UP_EMPTY) {
                return DOWN_EMPTY;
            }
            if (this == UP_FULL) {
                return DOWN_FULL;
            }
            return this;
        }

        public ElevatorState setFull() {
            if (this == UP_EMPTY) {
                return UP_FULL;
            }
            if (this == DOWN_EMPTY) {
                return DOWN_FULL;
            }
            return this;
        }

        public ElevatorState setEmpty() {
            if (this == UP_FULL) {
                return UP_EMPTY;
            }
            if (this == DOWN_FULL) {
                return DOWN_EMPTY;
            }
            return this;
        }
    }
}