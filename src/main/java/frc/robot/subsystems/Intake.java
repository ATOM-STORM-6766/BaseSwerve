package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.config.DeviceConfig;
import frc.robot.Constants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.SwerveConstants.DriveConstants;

public class Intake extends SubsystemBase {
    private TalonFX m_intakeMotor;
    private TalonFX m_transportMotor;
    private TalonFX m_sortingMotor;

    public enum SortingState {
        TOAMP(0.3), TOSHOOTER(-0.5), TOSHOOTER_AMP(-0.4);

        double speed;

        SortingState(double speed) {
            this.speed = speed;
        }
    }

    public Intake() {
        m_intakeMotor = new TalonFX(IntakeConstants.INTAKE_MOTOR_ID);
        m_transportMotor = new TalonFX(IntakeConstants.TRANSPORT_MOTOR_ID);
        m_sortingMotor = new TalonFX(IntakeConstants.SORTING_MOTOR_ID);
        configMotors();
    }

    public void setSpeed(double intakeSpeed, double transportSpeed, double sortingSpeed) {
            m_intakeMotor.setControl(new VoltageOut(intakeSpeed * 12).withEnableFOC(true));
            m_transportMotor.setControl(new VoltageOut(transportSpeed * 12).withEnableFOC(true));
            m_sortingMotor.setControl(new VoltageOut(sortingSpeed * 12).withEnableFOC(true));
    }

    public void stop() {
        m_intakeMotor.setControl(new NeutralOut());
        m_transportMotor.setControl(new NeutralOut());
        m_sortingMotor.setControl(new NeutralOut());
    }

    private void configMotors() {
        DeviceConfig.configureTalonFX("Intake Intake Motor", m_intakeMotor,
                FXConfig(InvertedValue.CounterClockwise_Positive, NeutralModeValue.Brake),
                Constants.LOOP_TIME_HZ);
        DeviceConfig.configureTalonFX("Intake Transport Motor", m_transportMotor,
                FXConfig(InvertedValue.Clockwise_Positive, NeutralModeValue.Brake),
                Constants.LOOP_TIME_HZ);
        DeviceConfig.configureTalonFX("Intake Sorting Motor", m_sortingMotor,
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
        config.Slot0 = DeviceConfig.FXPIDConfig(DriveConstants.PID_CONSTANTS);
        config.OpenLoopRamps = DeviceConfig.FXOpenLoopRampConfig(DriveConstants.OPEN_LOOP_RAMP);
        config.ClosedLoopRamps = DeviceConfig.FXClosedLoopRampConfig(DriveConstants.CLOSED_LOOP_RAMP);
        config.TorqueCurrent = DeviceConfig.FXTorqueCurrentConfig(DriveConstants.SLIP_CURRENT,
                -DriveConstants.SLIP_CURRENT, 0);
        return config;
    }
}
