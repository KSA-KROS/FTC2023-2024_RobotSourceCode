package org.firstinspires.ftc.teamcode.mainop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.part.DdalggakPart;
import org.firstinspires.ftc.teamcode.part.LinearPart;
import org.firstinspires.ftc.teamcode.part.PincerPart;
import org.firstinspires.ftc.teamcode.part.WheelPart;

@TeleOp(name = "TeleOpMode", group = "")
public class TeleOpMode extends OpMode {
    private LinearPart linear_part;
    private PincerPart pincer_part;
    private WheelPart wheel_part;
    private DdalggakPart ddalggak_part;

    boolean is_emergency_mode = false;

    @Override
    public void init() {
        this.linear_part = new LinearPart(hardwareMap, telemetry);
        this.pincer_part = new PincerPart(hardwareMap, telemetry);
        this.wheel_part = new WheelPart(hardwareMap, telemetry);
        this.ddalggak_part = new DdalggakPart(hardwareMap, telemetry);
    }

    @Override
    public void start() {
        //this.ddalggak_part.startStep(DdalggakPart.Command.OPEN_DDALGGAK);
    }

    @Override
    public void loop() {
        // Update the parts
        this.linear_part.update();
        this.pincer_part.update();
        this.wheel_part.update(this.linear_part.getLength());
        //this.ddalggak_part.update();

        if (this.is_emergency_mode) {
            this.processGamepad1WhenEmergency();
            this.processGamepad2WhenEmergency();
        } else {
            this.processGamepad1();
            this.processGamepad2();
        }

        this.emergencyOnOFF();

        //this.telemetry.update();
    }

    private void processGamepad1() {
        if (gamepad1.dpad_up) {
            this.wheel_part.startStep(WheelPart.Command.MOVE_FORWARD);
        } else if (gamepad1.dpad_down) {
            this.wheel_part.startStep(WheelPart.Command.MOVE_BACKWARD);
        } else if (gamepad1.dpad_left) {
            this.wheel_part.startStep(WheelPart.Command.MOVE_LEFT);
        } else if (gamepad1.dpad_right) {
            this.wheel_part.startStep(WheelPart.Command.MOVE_RIGHT);
        } else if (gamepad1.left_bumper) {
            this.wheel_part.startStep(WheelPart.Command.TURN_LEFT);
        } else if (gamepad1.right_bumper) {
            this.wheel_part.startStep(WheelPart.Command.TURN_RIGHT);
        } else if (gamepad1.triangle) {
            this.wheel_part.onAutoDistance();
            //this.wheel_part.startStep(WheelPart.Command.VIEW_FORWARD);
        } else if (gamepad1.cross) {
            this.wheel_part.onAutoDistance();
            //this.wheel_part.startStep(WheelPart.Command.VIEW_BACKWARD);
        } else if (gamepad1.square) {
            this.wheel_part.onAutoDistance();
            //this.wheel_part.startStep(WheelPart.Command.VIEW_LEFT);
        } else if (gamepad1.circle) {
            this.wheel_part.onAutoDistance();
            //this.wheel_part.startStep(WheelPart.Command.VIEW_RIGHT);
        } else {
            this.wheel_part.onAutoDistance();
            this.wheel_part.moveFreely(gamepad1.left_stick_x, gamepad1.left_stick_y);
        }
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
        if (gamepad2.left_bumper) {
            this.pincer_part.startStep(PincerPart.Command.GRAB_PIXEL_LEFT);
        }
        if (gamepad2.right_bumper) {
            this.pincer_part.startStep(PincerPart.Command.GRAB_PIXEL_RIGHT);
        }
        if (gamepad2.left_trigger > 0.5) {
            this.pincer_part.startStep(PincerPart.Command.DROP_PIXEL_LEFT);
        }
        if (gamepad2.right_trigger > 0.5) {
            this.pincer_part.startStep(PincerPart.Command.DROP_PIXEL_RIGHT);
        }


        // Rotate the pincer
        if (gamepad2.triangle) {
            this.pincer_part.startStep(PincerPart.Command.MOVE_DROP_POSITION);
        }

        // Ddalggak
        if (gamepad2.circle) {
            this.ddalggak_part.startStep(DdalggakPart.Command.CLOSE_DDALGGAK);
        }
        if (gamepad2.square) {
            this.ddalggak_part.startStep(DdalggakPart.Command.OPEN_DDALGGAK);
        }

        // Reset
        if (gamepad2.cross) {
            this.pincer_part.startStep(PincerPart.Command.MOVE_GRAB_POSITION);
        }
    }

    private void emergencyOnOFF() {
        if (this.is_emergency_mode) {
            if (gamepad1.touchpad && gamepad2.touchpad) {
                // NORMAL STATE
                this.is_emergency_mode = false;
                this.gamepad1.rumble(100);
                this.gamepad2.rumble(100);
            }
        } else {
            if ((gamepad1.right_bumper && gamepad1.right_trigger > 0.5
                    && gamepad1.left_bumper && gamepad1.left_trigger > 0.5)
                    ||(gamepad2.right_bumper && gamepad2.right_trigger > 0.5
                    && gamepad2.left_bumper && gamepad2.left_trigger > 0.5)) {
                // EMERGENCY STATE
                this.is_emergency_mode = true;
                this.pincer_part.emergencyStop();
                this.linear_part.emergencyStop();
                this.wheel_part.emergencyStop();
                this.gamepad1.rumble(500);
                this.gamepad2.rumble(500);
            }
        }
    }

    private void processGamepad1WhenEmergency () {

    }

    private void processGamepad2WhenEmergency () {

    }
}
