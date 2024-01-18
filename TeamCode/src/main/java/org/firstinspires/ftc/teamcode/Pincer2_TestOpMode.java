package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.teamcode.hardware.ServoHW;

import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.part.PincerPart;
import org.firstinspires.ftc.teamcode.part.RobotCommand;

@TeleOp(name = "Pincer2_TestOp", group = "")
public class Pincer2_TestOpMode extends OpMode {
    ServoHW finger1, finger2, arm1, arm2, wrist;
    PincerPart pincerPart = new PincerPart(hardwareMap, telemetry);


    @Override
    public void init() {
        pincerPart.initPincer(hardwareMap);





    }
    @Override
    public void start() {
        //pincerPart.startStep(PincerPart.Command.GRAB_PIXEL);
        pincerPart.rotateWrist(true);
        pincerPart.rotateWrist(false);
    }

    @Override
    public void loop() {
        pincerPart.update();
    }
}
