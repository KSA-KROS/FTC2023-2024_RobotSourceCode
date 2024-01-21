package org.firstinspires.ftc.teamcode.hardware;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.DcMotorHW;
public class MagSensorHW extends Hardware {
    private TouchSensor mag;

    public MagSensorHW(String name, HardwareMap hwm, Telemetry tel) {
        super(name, hwm, tel);
        this.mag = hwm.get(TouchSensor.class, this.name);
    }

    public boolean isActivated() {
        return this.mag.isPressed();
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void emergencyStop() {

    }
}
