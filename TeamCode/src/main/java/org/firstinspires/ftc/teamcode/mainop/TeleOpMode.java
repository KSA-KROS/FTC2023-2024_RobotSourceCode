package org.firstinspires.ftc.teamcode.mainop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.part.LinearPart;
import org.firstinspires.ftc.teamcode.part.PincerPart;

@TeleOp(name = "Pincer_TestOp", group = "")
public class TeleOpMode extends OpMode {
    private LinearPart linear_part;
    private PincerPart pincer_part;

    boolean is_emergency_mode = false;

    @Override
    public void init() {
        this.linear_part = new LinearPart(hardwareMap, telemetry);
        this.pincer_part = new PincerPart(hardwareMap, telemetry);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        // Update the parts
        this.linear_part.update();
        this.pincer_part.update();

        if (this.is_emergency_mode) {

        } else {
            this.processGamepad1();
            this.processGamepad2();
        }

        this.emergencyOnOFF();
    }

    private void processGamepad1() {

    }

    private void processGamepad2() {
        // Linear Up and Down
        if (gamepad2.dpad_up) {
            this.linear_part.startStep(LinearPart.Command.MOVE_UP);
        } else if (gamepad2.dpad_down) {
            this.linear_part.startStep(LinearPart.Command.MOVE_DOWN);
        } else {
            this.linear_part.startStep(LinearPart.Command.STOP);
        }

        // Pincer Grab and Drop
        if (gamepad2.left_bumper || gamepad2.right_bumper) {
            this.pincer_part.startStep(PincerPart.Command.GRAB_OR_DROP_PIXEL);
        }

        // Rotate the pincer
        if (gamepad2.y) { // Triangle
            this.pincer_part.startStep(PincerPart.Command.MOVE_DROP_POSITION);
        }

        // Reset
        if (gamepad2.a) { // X
            this.pincer_part.startStep(PincerPart.Command.MOVE_GRAB_POSITION);
            this.linear_part.startStep(LinearPart.Command.RESET);
        }
    }

    private void emergencyOnOFF() {
        if (this.is_emergency_mode) {
            if (gamepad1.right_stick_button && gamepad1.left_stick_button
            && gamepad2.right_stick_button && gamepad2.left_stick_button) {
                // NORMAL STATE
                this.is_emergency_mode = false;
            }
        } else {
            if (gamepad1.right_stick_button || gamepad1.left_stick_button
            || gamepad2.right_stick_button || gamepad2.left_stick_button) {
                // EMERGENCY STATE
                this.is_emergency_mode = true;
                this.pincer_part.emergencyStop();
                this.linear_part.emergencyStop();
            }
        }
    }

    private void processGamepad1WhenEmergency () {

    }

    private void processGamepad2WhenEmergency () {

    }
}
