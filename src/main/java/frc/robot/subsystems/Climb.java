package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.config.DeviceConfig;
import frc.robot.Constants;
import frc.robot.Constants.ClimbConstants;
import frc.robot.Constants.SwerveConstants.DriveConstants;

public class Climb extends SubsystemBase {
    private TalonFX m_climbMotor1;
    private TalonFX m_climbMotor2;

    public Climb() {
        m_climbMotor1 = new TalonFX(ClimbConstants.CLIMB_MOTOR_1_ID);
        m_climbMotor2 = new TalonFX(ClimbConstants.CLIMB_MOTOR_2_ID);
        configMotors();
        m_climbMotor1.setPosition(0);
        m_climbMotor2.setControl(new Follower(m_climbMotor1.getDeviceID(), true));
    }

    public Command setSpeed(DoubleSupplier speed) {
        return this.runOnce(() -> m_climbMotor1.setControl(new VelocityTorqueCurrentFOC(speed.getAsDouble() * 35)));
    }

    private void configMotors() {
        var config = FXConfig(InvertedValue.CounterClockwise_Positive, NeutralModeValue.Brake);
        config.Slot0 = config.Slot0.withKP(60).withKD(6);
        config.TorqueCurrent.PeakForwardTorqueCurrent = 30;
        config.TorqueCurrent.PeakReverseTorqueCurrent = -30;
        DeviceConfig.configureTalonFX("Climb Motor 1", m_climbMotor1,
                config, Constants.LOOP_TIME_HZ);
        DeviceConfig.configureTalonFX("Climb Motor 2", m_climbMotor2,
                config, Constants.LOOP_TIME_HZ);
    }

    private static TalonFXConfiguration FXConfig(InvertedValue invert, NeutralModeValue neutralMode) {
        TalonFXConfiguration config = new TalonFXConfiguration();
        config.Audio.BeepOnBoot = false;
        config.Audio.BeepOnConfig = false;
        config.MotorOutput = DeviceConfig.FXMotorOutputConfig(invert, neutralMode);
        config.Slot0 = DeviceConfig.FXPIDConfig(DriveConstants.PID_CONSTANTS);
        config.OpenLoopRamps = DeviceConfig.FXOpenLoopRampConfig(DriveConstants.OPEN_LOOP_RAMP);
        config.ClosedLoopRamps = DeviceConfig.FXClosedLoopRampConfig(1);
        config.TorqueCurrent = DeviceConfig.FXTorqueCurrentConfig(DriveConstants.SLIP_CURRENT,
                -DriveConstants.SLIP_CURRENT, 0);
        return config;
    }

}
