package frc.robot.subsystems;

import java.util.Queue;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LED extends SubsystemBase {
    private static LED instance;

    public static LED getInstance() {
        if (instance == null) {
            instance = new LED();
        }
        return instance;
    }

    private AddressableLED m_led = new AddressableLED(0);
    private AddressableLEDBuffer m_ledBuffer;
    private AddressableLEDBuffer m_blackLedBuffer;
    private Queue<AddressableLEDBuffer> m_ledBufferQueue = new java.util.LinkedList<>();
    private Timer time = new Timer();

    public LED() {
        m_ledBuffer = new AddressableLEDBuffer(30);
        m_led.setLength(m_ledBuffer.getLength());
        m_led.start();
        m_blackLedBuffer = new AddressableLEDBuffer(30);

        m_ledBuffer.forEach((index, r, g, b) -> {
            if (index < 20)
                m_ledBuffer.setRGB(index, 150, 150, 150);
        });
        m_led.setData(m_ledBuffer);
        time.start();
    }

    public void setUp() {
        m_ledBufferQueue.clear();
        m_ledBuffer.forEach((index, r, g, b) -> {
            if (index < 20)
                m_ledBuffer.setRGB(index, 42, 204, 163);
        });
        m_ledBufferQueue.add(m_ledBuffer);
    }

    public void auto() {
        m_ledBufferQueue.clear();
        m_ledBuffer.forEach((index, r, g, b) -> {
            if (index < 20)
                m_ledBuffer.setRGB(index, 42, 204, 163);
        });
        m_ledBufferQueue.add(m_ledBuffer);
        m_ledBufferQueue.add(m_blackLedBuffer);
    }

    public void isIntake() {
        m_ledBufferQueue.clear();
        m_ledBuffer.forEach((index, r, g, b) -> {
            if (index < 20)
                m_ledBuffer.setRGB(index, 124, 200, 85);
        });
        m_ledBufferQueue.add(m_ledBuffer);
        m_ledBufferQueue.add(m_blackLedBuffer);

    }

    public void intaked() {
        m_ledBufferQueue.clear();
        m_ledBuffer.forEach((index, r, g, b) -> {
            if (index < 20)
                m_ledBuffer.setRGB(index, 124, 200, 85);
        });
        m_ledBufferQueue.add(m_ledBuffer);
    }

    public void enterShootingRange() {
        for (int i = 20; i < 30; i++) {
            m_ledBuffer.setRGB(i, 100, 7, 247);
            m_blackLedBuffer.setRGB(i, 100, 7, 247);
        }
    }

    // public void outShootingRange() {
    // for (int i = 20; i < 30; i++) {
    // m_ledBuffer.setRGB(i, 0, 0, 0);
    // m_blackLedBuffer.setRGB(i, 0, 0, 0);
    // }
    // }

    public void outShootingRange() {
        m_ledBufferQueue.clear();
        m_ledBuffer.forEach((index, r, g, b) -> {
            if (index < 20)
                m_ledBuffer.setRGB(index, 200, 0, 0);
        });
        m_ledBufferQueue.add(m_ledBuffer);
        m_ledBufferQueue.add(m_blackLedBuffer);
    }

    public void prepareToShoot() {
        m_ledBufferQueue.clear();
        m_ledBuffer.forEach((index, r, g, b) -> {
            if (index < 20)
                m_ledBuffer.setRGB(index, 247, 80, 82);
        });
        m_ledBufferQueue.add(m_ledBuffer);
        m_ledBufferQueue.add(m_blackLedBuffer);
    }

    public void readyToShoot() {
        m_ledBufferQueue.clear();
        m_ledBuffer.forEach((index, r, g, b) -> {
            if (index < 20)
                m_ledBuffer.setRGB(index, 247, 80, 82);
        });
        m_ledBufferQueue.add(m_ledBuffer);
    }

    public void isAMP() {
        m_ledBufferQueue.clear();
        m_ledBuffer.forEach((index, r, g, b) -> {
            if (index < 20)
                m_ledBuffer.setRGB(index, 232, 183, 13);
        });
        m_ledBufferQueue.add(m_ledBuffer);
        m_ledBufferQueue.add(m_blackLedBuffer);
    }

    public void AMPed() {
        m_ledBufferQueue.clear();
        m_ledBuffer.forEach((index, r, g, b) -> {
            if (index < 20)
                m_ledBuffer.setRGB(index, 232, 183, 13);
        });
        m_ledBufferQueue.add(m_ledBuffer);
    }

    public void disabled() {
        m_ledBufferQueue.clear();
        m_led.setData(m_blackLedBuffer);
    }

    @Override
    public void periodic() {
        if (time.hasElapsed(0.3) && !m_ledBufferQueue.isEmpty()) {
            var temp = m_ledBufferQueue.poll();
            m_led.setData(temp);
            m_ledBufferQueue.add(temp);
            time.reset();
        }
    }

}
