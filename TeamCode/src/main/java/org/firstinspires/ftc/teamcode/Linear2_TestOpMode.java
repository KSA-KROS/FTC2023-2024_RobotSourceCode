package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.teamcode.hardware.DcMotorHW;
import org.firstinspires.ftc.teamcode.hardware.ServoHW;

import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.part.LinearPart;
import org.firstinspires.ftc.teamcode.part.RobotCommand;

@TeleOp(name = "Linear2_TestOp", group = "")
public class Linear2_TestOpMode extends OpMode {
    DcMotorHW linear1, linear2;
    LinearPart linearPart = new LinearPart(hardwareMap, telemetry);


    @Override
    public void init() {
    }
    @Override
    public void start() {
        linearPart.startStep(LinearPart.Command.MOVE_UP);
        linearPart.startStep(LinearPart.Command.MOVE_DOWN);



    }

    @Override
    public void loop() {
        linearPart.update();
    }
}
