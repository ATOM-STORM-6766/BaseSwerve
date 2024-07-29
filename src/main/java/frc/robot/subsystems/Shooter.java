package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.config.DeviceConfig;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.SwerveConstants.DriveConstants;

public class Shooter extends SubsystemBase {
    private TalonFX m_upFlyWhell;
    private TalonFX m_downFlyWhell;
    private TalonFX m_pitchMotor;

    private VelocityTorqueCurrentFOC m_Control = new VelocityTorqueCurrentFOC(0);

    public Shooter() {
        m_pitchMotor = new TalonFX(ShooterConstants.PITCH_MOTOR_ID);
        m_upFlyWhell = new TalonFX(ShooterConstants.UP_FLYWHELL_ID);
        m_downFlyWhell = new TalonFX(ShooterConstants.DOWN_FLYWHELL_ID);
        configMotors();
        m_pitchMotor.setPosition(0);
    }

    public void setSpeed(double[] speed) {
        m_upFlyWhell.setControl(m_Control.withVelocity(speed[0] * 108));
        m_downFlyWhell.setControl(m_Control.withVelocity(speed[1] * 108));
    }

    public void setPitch(double pitch) {
        m_pitchMotor.setControl(new PositionTorqueCurrentFOC(pitch / ShooterConstants.RATIO / 360));
    }

    public void setPitch(Rotation2d pitch) {
        setPitch(pitch.getDegrees());
    }

    public void up(double speed) {
        m_pitchMotor.setControl(new VoltageOut(speed * 4).withEnableFOC(true));
    }

    public void down(double speed) {
        m_pitchMotor.setControl(new VoltageOut(-speed * 4).withEnableFOC(true));
    }

    public Rotation2d getPitch() {
        return Rotation2d.fromDegrees(m_pitchMotor.getPosition().getValue() * ShooterConstants.RATIO * 360);
    }

    public void stop() {
        m_upFlyWhell.setControl(new NeutralOut());
        m_downFlyWhell.setControl(new NeutralOut());
        m_pitchMotor.setControl(new NeutralOut());
    }

    private void configMotors() {
        DeviceConfig.configureTalonFX("Shooter Pitch Motor", m_pitchMotor,
                FXConfig(InvertedValue.Clockwise_Positive, NeutralModeValue.Brake),
                Constants.LOOP_TIME_HZ);
        DeviceConfig.configureTalonFX("Shooter On Whell Motor", m_upFlyWhell,
                FXConfig(InvertedValue.Clockwise_Positive, NeutralModeValue.Coast),
                Constants.LOOP_TIME_HZ);
        DeviceConfig.configureTalonFX("Shooter Down Whell Motor", m_downFlyWhell,
                FXConfig(InvertedValue.Clockwise_Positive, NeutralModeValue.Coast),
                Constants.LOOP_TIME_HZ);
    }

    private static TalonFXConfiguration FXConfig(InvertedValue invert, NeutralModeValue neutralMode) {
        TalonFXConfiguration config = new TalonFXConfiguration();
        config.Audio.BeepOnBoot = false;
        config.Audio.BeepOnConfig = false;
        config.MotorOutput = DeviceConfig.FXMotorOutputConfig(invert, neutralMode);

        // config.CurrentLimits = DeviceConfig.FXCurrentLimitsConfig(
        // DriveConstants.CURRENT_LIMIT_ENABLE,
        // DriveConstants.SUPPLY_CURRENT_LIMIT,
        // DriveConstants.SUPPLY_CURRENT_THRESHOLD,
        // DriveConstants.SUPPLY_TIME_THRESHOLD);
        config.Slot0 = DeviceConfig.FXPIDConfig(DriveConstants.PID_CONSTANTS);
        config.OpenLoopRamps = DeviceConfig.FXOpenLoopRampConfig(0.1);
        config.ClosedLoopRamps = DeviceConfig.FXClosedLoopRampConfig(DriveConstants.CLOSED_LOOP_RAMP);
        return config;
    }
}
