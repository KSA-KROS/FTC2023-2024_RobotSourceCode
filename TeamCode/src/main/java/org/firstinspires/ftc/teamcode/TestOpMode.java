package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.hardware.DcMotorHW;

@TeleOp(name = "Main TestOp", group = "")
public class TestOpMode extends OpMode {
    DcMotorHW motor;

    @Override
    public void init() {
        this.motor = new DcMotorHW("motor", hardwareMap, telemetry);
        motor.setDirection(DcMotor.Direction.FORWARD);
        motor.setUsingBrake(true).setUsingEncoder(false).setUsingFixation(false);
    }

    @Override
    public void start() {
        motor.move(1.0, 5000);
    }

    @Override
    public void loop() {
        motor.update();
    }
}
