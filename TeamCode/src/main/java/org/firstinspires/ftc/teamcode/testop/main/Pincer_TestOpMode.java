package org.firstinspires.ftc.teamcode.testop.main;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


import org.firstinspires.ftc.teamcode.hardware.ServoHW;

import org.firstinspires.ftc.teamcode.part.PincerPart;

@TeleOp(name = "Pincer_TestOp", group = "")
public class Pincer_TestOpMode extends OpMode {
    ServoHW finger1, finger2, arm1, arm2, wrist;
    PincerPart pincerPart = new PincerPart(hardwareMap, telemetry);


    @Override
    public void init() {
    }
    @Override
    public void start() {
        pincerPart.startStep(PincerPart.Command.MOVE_GRAB_POSITION);
        pincerPart.startStep(PincerPart.Command.GRAB_PIXEL);
        pincerPart.startStep(PincerPart.Command.MOVE_DROP_POSITION);
        pincerPart.startStep(PincerPart.Command.DROP_PIXEL);


    }

    @Override
    public void loop() {
        pincerPart.update();
    }
}
