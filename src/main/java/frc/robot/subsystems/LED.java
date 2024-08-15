package frc.robot.subsystems;

import java.util.Queue;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LED extends SubsystemBase {
    // private static LED instance;

    // public static LED getInstance() {
    // if (instance == null) {
    // instance = new LED();
    // }
    // return instance;
    // }

    private AddressableLED m_led = new AddressableLED(0);
    private AddressableLEDBuffer m_ledBuffer;
    private AddressableLEDBuffer m_blackLedBuffer;
    private Queue<AddressableLEDBuffer> m_ledBufferQueue = new java.util.LinkedList<>();
    private Timer m_timer = new Timer();

    public LED() {
        m_ledBuffer = new AddressableLEDBuffer(30);
        m_led.setLength(m_ledBuffer.getLength());
        m_led.start();

        m_blackLedBuffer = new AddressableLEDBuffer(30);
    }

    public void isIntake() {
        if (!m_ledBufferQueue.isEmpty()) {
            m_ledBufferQueue.clear();
        }
        m_ledBuffer.forEach((index, r, g, b) -> {
            m_ledBuffer.setRGB(index, 255, 255, 255);
        });
        m_led.setData(m_ledBuffer);
        m_ledBufferQueue.add(m_blackLedBuffer);
        m_ledBufferQueue.add(m_ledBuffer);
        m_timer.restart();

    }

    public void intaked() {
        if (!m_ledBufferQueue.isEmpty()) {
            m_ledBufferQueue.clear();
        }
        m_ledBuffer.forEach((index, r, g, b) -> {
            m_ledBuffer.setRGB(index, 255, 255, 255);
        });
        m_led.setData(m_ledBuffer);
        m_timer.stop();
        m_timer.reset();
    }

    @Override
    public void periodic() {
        if (m_timer.get() > 0.5 && !m_ledBufferQueue.isEmpty()) {
            var temp = m_ledBufferQueue.poll();
            m_led.setData(temp);
            m_ledBufferQueue.add(temp);
            m_timer.restart();
        }
    }

}
