package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
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
    TalonFX m_intakeMotor;
    TalonFX m_transportMotor;
    TalonFX m_sortingMotor;

    public Intake() {
        m_intakeMotor = new TalonFX(IntakeConstants.INTAKE_MOTOR_ID);
        m_transportMotor = new TalonFX(IntakeConstants.TRANSPORT_MOTOR_ID);
        m_sortingMotor = new TalonFX(IntakeConstants.SORTING_MOTOR_ID);
        configMotors();
        m_transportMotor.setControl(new Follower(m_intakeMotor.getDeviceID(), true));
    }

    public void setSpeed(double[] speed) {
        m_intakeMotor.setControl(new VoltageOut(speed[0] * 12).withEnableFOC(true));
        m_sortingMotor.setControl(new VoltageOut(speed[1] * 12).withEnableFOC(true));
    }

    public void stop() {
        m_intakeMotor.setControl(new NeutralOut());
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
