package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "TeleOp", group = "")
public class TestOpMode extends OpMode {
    DcMotor wheel1, wheel2, wheel3, wheel4;
    double ticks_per_rotation_1, ticks_per_rotation_2, ticks_per_rotation_3, ticks_per_rotation_4;

    @Override
    public void init() {
        wheel1 = hardwareMap.get(DcMotor.class, "wheel1");
        wheel2 = hardwareMap.get(DcMotor.class, "wheel2");
        wheel3 = hardwareMap.get(DcMotor.class, "wheel3");
        wheel4 = hardwareMap.get(DcMotor.class, "wheel4");

        ticks_per_rotation_1 = wheel1.getMotorType().getTicksPerRev();
        ticks_per_rotation_2 = wheel2.getMotorType().getTicksPerRev();
        ticks_per_rotation_3 = wheel3.getMotorType().getTicksPerRev();
        ticks_per_rotation_4 = wheel4.getMotorType().getTicksPerRev();

        //Test 1 : Get the ticks per rotation of the motor
        telemetry.addData("tpr1", ticks_per_rotation_1);
        telemetry.addData("tpr2", ticks_per_rotation_2);
        telemetry.addData("tpr3", ticks_per_rotation_3);
        telemetry.addData("tpr4", ticks_per_rotation_4);
        telemetry.update();

        DcMotor.RunMode mode;

        mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
        wheel1.setMode(mode);
        wheel2.setMode(mode);
        wheel3.setMode(mode);
        wheel4.setMode(mode);

        mode = DcMotor.RunMode.RUN_USING_ENCODER;
        wheel1.setMode(mode);
        wheel2.setMode(mode);
        wheel3.setMode(mode);
        wheel4.setMode(mode);

        wheel1.setPower(0.5);
        wheel2.setPower(0.5);
        wheel3.setPower(0.5);
        wheel4.setPower(0.5);
    }

    @Override
    public void loop() {
        // Test 2 : Check the speed of each motor and the accuracy of the encoder
        double target = 1000;
        if (wheel1.getCurrentPosition() > target) wheel1.setPower(0);
        if (wheel2.getCurrentPosition() > target) wheel2.setPower(0);
        if (wheel3.getCurrentPosition() > target) wheel3.setPower(0);
        if (wheel4.getCurrentPosition() > target) wheel4.setPower(0);
    }
}
