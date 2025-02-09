package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.StaticBrake;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.config.DeviceConfig;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
    private static Shooter instance = null;

    private TalonFX m_upFlyWhell;
    private TalonFX m_downFlyWhell;
    private TalonFX m_pitchMotor;

    public static double upSpeed = 0;
    public static double downSpeed = 0;
    public static double m_pitch = 0;

    private VelocityVoltage m_Control = new VelocityVoltage(0);// .withEnableFOC(true);

    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
        }
        return instance;
    }

    public Shooter() {
        m_pitchMotor = new TalonFX(ShooterConstants.PITCH_MOTOR_ID);
        m_upFlyWhell = new TalonFX(ShooterConstants.UP_FLYWHELL_ID);
        m_downFlyWhell = new TalonFX(ShooterConstants.DOWN_FLYWHELL_ID);
        configMotors();
        m_pitchMotor.setPosition(0);
    }

    public void run() {
        setPitch(m_pitch);
        setSpeed(upSpeed, downSpeed);
    }

    public void autoRun(double length) {
        double upspeed = ShooterConstants.upperMap.get(length);
        double downspeed = ShooterConstants.lowerMap.get(length);
        double pitch = ShooterConstants.angleMap.get(length);
        // double time = length / ((upspeed + downspeed) / 2 *
        // ShooterConstants.flywheelCircumference);
        setSpeed(upspeed, downspeed);
        setPitch(pitch);
    }

    public void setSpeed(double upSpeed, double downSpeed) {
        m_upFlyWhell.setControl(m_Control.withVelocity(upSpeed * 100));
        m_downFlyWhell.setControl(m_Control.withVelocity(downSpeed * 100));
    }

    public void setPitch(Rotation2d pitch) {
        m_pitchMotor.setControl(new PositionTorqueCurrentFOC(pitch.getRotations()));
    }

    public void setPitch(double pitch) {
        setPitch(Rotation2d.fromDegrees(pitch));
    }

    public void up(double speed) {
        m_pitchMotor.setControl(new VoltageOut(speed * 4).withEnableFOC(true));
    }

    public void down(double speed) {
        m_pitchMotor.setControl(new VoltageOut(-speed * 3).withEnableFOC(true));
    }

    public Rotation2d getPitch() {
        return Rotation2d.fromRotations(m_pitchMotor.getPosition().getValue());
    }

    public void stop() {
        m_upFlyWhell.setControl(new NeutralOut());
        m_downFlyWhell.setControl(new NeutralOut());
    }

    public void holdPitch() {
        m_pitchMotor.setControl(new StaticBrake());
    }

    private void configMotors() {
        var cfg = FXConfig(InvertedValue.Clockwise_Positive, NeutralModeValue.Brake);
        cfg.Slot0.kP = 60 * Constants.ShooterConstants.RATIO;
        cfg.Slot0.kD = 6 * Constants.ShooterConstants.RATIO;
        cfg.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        cfg.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 47.0 / 360;
        cfg.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
        cfg.SoftwareLimitSwitch.ReverseSoftLimitThreshold = 0;
        cfg.Feedback.SensorToMechanismRatio = Constants.ShooterConstants.RATIO;
        cfg.TorqueCurrent.PeakForwardTorqueCurrent = 30;
        cfg.TorqueCurrent.PeakReverseTorqueCurrent = -30;
        DeviceConfig.configureTalonFX("Shooter Pitch Motor", m_pitchMotor, cfg, Constants.LOOP_TIME_HZ);

        cfg = FXConfig(InvertedValue.Clockwise_Positive, NeutralModeValue.Coast);
        cfg.Slot0.kP = 0.001;
        cfg.Slot0.kS = 0.14434;
        cfg.Slot0.kV = 0.11802;
        cfg.Slot0.kA = 0.0078855;
        DeviceConfig.configureTalonFX("Shooter On Whell Motor", m_upFlyWhell, cfg, Constants.LOOP_TIME_HZ);

        cfg.Slot0.kP = 0.001;
        cfg.Slot0.kS = 0.14945;
        cfg.Slot0.kV = 0.11839;
        cfg.Slot0.kA = 0.0070825;
        DeviceConfig.configureTalonFX("Shooter Down Whell Motor", m_downFlyWhell, cfg, Constants.LOOP_TIME_HZ);
    }

    private static TalonFXConfiguration FXConfig(InvertedValue invert, NeutralModeValue neutralMode) {
        TalonFXConfiguration config = new TalonFXConfiguration();
        config.Audio.BeepOnBoot = false;
        config.Audio.BeepOnConfig = false;
        config.CurrentLimits = DeviceConfig.FXCurrentLimitsConfig(true, 30, 30, 0);
        config.MotorOutput = DeviceConfig.FXMotorOutputConfig(invert, neutralMode);
        config.OpenLoopRamps = DeviceConfig.FXOpenLoopRampConfig(0.1);
        config.ClosedLoopRamps = DeviceConfig.FXClosedLoopRampConfig(1);
        return config;
    }
}
