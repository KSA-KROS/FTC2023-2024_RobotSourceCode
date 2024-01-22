package org.firstinspires.ftc.teamcode.mainop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.part.AirplanePart;
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
    private AirplanePart airplane_part;

    private boolean is_emergency_mode = false;

    private String prev_gamepad1_state = "";
    private String prev_gamepad2_state = "";

    @Override
    public void init() {
        this.linear_part = new LinearPart(hardwareMap, telemetry);
        this.pincer_part = new PincerPart(hardwareMap, telemetry);
        this.wheel_part = new WheelPart(hardwareMap, telemetry);
        this.ddalggak_part = new DdalggakPart(hardwareMap, telemetry);
        this.airplane_part = new AirplanePart(hardwareMap, telemetry);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        // Update the parts
        this.linear_part.update();
        this.pincer_part.update();
        this.wheel_part.update(this.linear_part.getLength());
        this.ddalggak_part.update();
        this.airplane_part.update();

        if (this.is_emergency_mode) {
            this.processGamepad1WhenEmergency();
            this.processGamepad2WhenEmergency();
        } else {
            this.processGamepad1();
            this.processGamepad2();
        }

        this.emergencyOnOFF();

        this.telemetry.update();
    }

    private void processGamepad1() {
        // For optimization : do not process (start commands) if the gamepad1 state is not changed
        //String gamepad1_state = gamepad1.toString();
        //if (gamepad1_state.equals(this.prev_gamepad1_state)) return;
        //this.prev_gamepad1_state = gamepad1_state;

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
            this.wheel_part.offAutoDistance();
            this.wheel_part.moveFreely(gamepad1.left_stick_x, gamepad1.left_stick_y);
        }
    }

    private void processGamepad2() {
        // Linear Up and Down
        if (this.pincer_part.isAbleToMovingLinear() && gamepad2.dpad_up) {
            this.linear_part.startStep(LinearPart.Command.MOVE_UP);
        } else if (this.pincer_part.isAbleToMovingLinear() && gamepad2.dpad_down) {
            this.linear_part.startStep(LinearPart.Command.MOVE_DOWN);
        } else if (this.pincer_part.isAbleToMovingLinear() && gamepad2.cross) {
            this.linear_part.startStep(LinearPart.Command.MOVE_DOWN_POWERFUL);
        } else {
            this.linear_part.startStep(LinearPart.Command.STOP);
        }

        // For optimization : do not process (start commands) if the gamepad2 state is not changed
        String gamepad2_state = gamepad2.toString();
        if (gamepad2_state.equals(this.prev_gamepad2_state)) return;
        this.prev_gamepad2_state = gamepad2_state;

        // Pincer Grab and Drop
        if (gamepad2.left_bumper) {
            this.pincer_part.controlLeftFinger();
        }
        if (gamepad2.right_bumper) {
            this.pincer_part.controlRightFinger();
        }

        // Rotate the pincer
        if (gamepad2.triangle) {
            this.pincer_part.startStep(PincerPart.Command.MOVE_DROP_OR_GRAB_POSITION);
        }

        // Ddalggak
        if (gamepad2.circle) {
            this.ddalggak_part.startStep(DdalggakPart.Command.OPEN_OR_CLOSE_DDALGGAK);
        }

        // Airplane
        if (gamepad2.square) {
            this.airplane_part.startStep(AirplanePart.Command.FLY);
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
