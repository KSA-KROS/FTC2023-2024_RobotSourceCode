package org.firstinspires.ftc.teamcode.mainop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.hardware.IMUHW;
import org.firstinspires.ftc.teamcode.part.LinearPart;
import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.part.PincerPart;
import org.firstinspires.ftc.teamcode.part.WheelPart;

@TeleOp(name = "Pincer_TestOp", group = "")
public class TeleOpMode extends OpMode {
    IMUHW imu;

    public double currentAngle = 0;
    public long prev_time = 0;

    public YawPitchRollAngles orientation;
    public AngularVelocity angularVelocity;
    public double yaw_init;
    public double pitch_init;
    public double roll_init;

    private LinearPart linear_part;
    private PincerPart pincer_part;
    private WheelPart wheel_part;

    boolean is_emergency_mode = false;

    @Override
    public void init() {
        this.linear_part = new LinearPart(hardwareMap, telemetry);
        this.pincer_part = new PincerPart(hardwareMap, telemetry);
        this.wheel_part = new WheelPart(hardwareMap, telemetry);

        this.imu = new IMUHW("imu", hardwareMap, telemetry);
        imu.setOrientation();
    }

    @Override
    public void start() {
        prev_time = System.currentTimeMillis();
        orientation = imu.getOrientation();
        angularVelocity = imu.getAngularVelocity();
        yaw_init = orientation.getYaw(AngleUnit.DEGREES);
        pitch_init = orientation.getPitch(AngleUnit.DEGREES);
        roll_init = orientation.getRoll(AngleUnit.DEGREES);
    }

    @Override
    public void loop() {
        // Update the parts
        this.linear_part.update();
        this.pincer_part.update();
        this.wheel_part.update();

        if (this.is_emergency_mode) {
            this.processGamepad1WhenEmergency();
            this.processGamepad2WhenEmergency();
        } else {
            this.processGamepad1();
            this.processGamepad2();
        }

        this.emergencyOnOFF();

        long timeChange = System.currentTimeMillis() - prev_time;
        prev_time = System.currentTimeMillis();
        telemetry.addData("Hub orientation", "Logo=%s   USB=%s\n ", imu.logoDirection, imu.usbDirection);

        orientation = imu.getOrientation();
        angularVelocity = imu.getAngularVelocity();

        telemetry.addData("Yaw (Z)", "%.2f Deg.", orientation.getYaw(AngleUnit.DEGREES) - yaw_init);
        telemetry.addData("Pitch (X)", "%.2f Deg.", orientation.getPitch(AngleUnit.DEGREES) - pitch_init);
        telemetry.addData("Roll (Y)", "%.2f Deg.\n", orientation.getRoll(AngleUnit.DEGREES) - roll_init);

        telemetry.addData("Yaw (Z)", "%.2f Deg.", imu.getAngle('Z', timeChange));
        telemetry.addData("Pitch (X)", "%.2f Deg.", imu.getAngle('X', timeChange));
        telemetry.addData("Roll (Y)", "%.2f Deg.", imu.getAngle('Y', timeChange));
        telemetry.update();

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
        } else {
            this.wheel_part.startStep(WheelPart.Command.STOP);
        }

        if (gamepad1.triangle) {
            this.wheel_part.startStep(WheelPart.Command.VIEW_FORWARD);
        } else if (gamepad1.cross) {
            this.wheel_part.startStep(WheelPart.Command.VIEW_BACKWARD);
        } else if (gamepad1.square) {
            this.wheel_part.startStep(WheelPart.Command.VIEW_LEFT);
        } else if (gamepad1.circle) {
            this.wheel_part.startStep(WheelPart.Command.VIEW_RIGHT);
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
        if (gamepad2.left_bumper || gamepad2.right_bumper) {
            this.pincer_part.startStep(PincerPart.Command.GRAB_OR_DROP_PIXEL);
        }

        // Rotate the pincer
        if (gamepad2.triangle) {
            this.pincer_part.startStep(PincerPart.Command.MOVE_DROP_POSITION);
        }

        // Reset
        if (gamepad2.cross) {
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
                this.gamepad1.rumble(100);
                this.gamepad2.rumble(100);
            }
        } else {
            if (gamepad1.right_stick_button || gamepad1.left_stick_button
            || gamepad2.right_stick_button || gamepad2.left_stick_button) {
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
