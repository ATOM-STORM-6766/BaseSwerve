package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.config.DeviceConfig;
import frc.robot.Constants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.SwerveConstants.DriveConstants;

public class Elevator extends SubsystemBase {
        private TalonFX m_elevatorMotor1;
        private TalonFX m_elevatorMotor2;
        private TalonFX m_ampMotor;

        public Elevator() {
                m_elevatorMotor1 = new TalonFX(ElevatorConstants.ELEVATOR_MOTOR_1_ID);
                m_elevatorMotor2 = new TalonFX(ElevatorConstants.ELEVATOR_MOTOR_2_ID);
                m_ampMotor = new TalonFX(ElevatorConstants.AMP_MOTOR_ID);
                configMotors();
                m_elevatorMotor1.setPosition(0);
                m_elevatorMotor2.setControl(new Follower(m_elevatorMotor1.getDeviceID(), false));
        }

        public void up(double speed) {
                m_elevatorMotor1.setControl(new VoltageOut(speed * 12).withEnableFOC(true));
        }

        public void upToHeight(double height) {
                m_elevatorMotor1.setControl(new PositionVoltage(height).withEnableFOC(true));
        }

        public void down(double speed) {
                m_elevatorMotor1.setControl(new VoltageOut(-speed * 12).withEnableFOC(true));
        }

        public double getHeight() {
                return m_elevatorMotor1.getPosition().getValue();
        }

        public void stop() {
                m_elevatorMotor1.setControl(new NeutralOut());
                m_ampMotor.setControl(new NeutralOut());
        }

        public void setAmp(double speed) {
                m_ampMotor.setControl(new VoltageOut(speed * 12).withEnableFOC(true));
        }

        private void configMotors() {
                DeviceConfig.configureTalonFX("Elevator Elevator Motor 1", m_elevatorMotor1,
                                FXConfig(InvertedValue.CounterClockwise_Positive, NeutralModeValue.Brake),
                                Constants.LOOP_TIME_HZ);
                DeviceConfig.configureTalonFX("Elevator Elevator Motor 2", m_elevatorMotor2,
                                FXConfig(InvertedValue.CounterClockwise_Positive, NeutralModeValue.Brake),
                                Constants.LOOP_TIME_HZ);
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
                config.Slot0 = DeviceConfig.FXPIDConfig(DriveConstants.PID_CONSTANTS).withKP(2.4);
                config.OpenLoopRamps = DeviceConfig.FXOpenLoopRampConfig(DriveConstants.OPEN_LOOP_RAMP);
                config.ClosedLoopRamps = DeviceConfig.FXClosedLoopRampConfig(DriveConstants.CLOSED_LOOP_RAMP);
                config.TorqueCurrent = DeviceConfig.FXTorqueCurrentConfig(DriveConstants.SLIP_CURRENT,
                                -DriveConstants.SLIP_CURRENT, 0);
                return config;
        }
}
