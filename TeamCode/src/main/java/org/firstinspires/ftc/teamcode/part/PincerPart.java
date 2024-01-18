package org.firstinspires.ftc.teamcode.part;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.ServoHW;

public class PincerPart extends Part {
    ServoHW finger1, finger2, arm1, arm2, wrist;
    public double fingerClosePosition = 0.4;
    public double fingerOpenPosition = 0.6;
    public double wristDropPosition = 0.4;
    public double wristGrabPosition = 0.6;

    public double armGrabPosition = 0.4;
    public double armDropPosition = 0.6;

    public enum Command implements RobotCommand {
        GRAB_PIXEL,
        DROP_POSITION,
        DROP_PIXEL,
        GRAB_POSITION
    }

    // Constructor
    public PincerPart(HardwareMap hwm, Telemetry tel) {
        super(hwm, tel);
    }
    public void initPincer(HardwareMap hardwareMap) {
        this.wrist = new ServoHW("s2", hardwareMap, telemetry);
        wrist.setDirection(Servo.Direction.FORWARD);
        wrist.setInitialPosition(0.5);
        this.finger1 = new ServoHW("s0", hardwareMap, telemetry);
        this.finger2 = new ServoHW("s1", hardwareMap, telemetry);
        this.arm1 = new ServoHW("s3", hardwareMap, telemetry);
        this.arm2 = new ServoHW("s4", hardwareMap, telemetry);

        finger1.setDirection(Servo.Direction.FORWARD);
        finger2.setDirection(Servo.Direction.REVERSE);
        arm1.setDirection(Servo.Direction.FORWARD);
        arm2.setDirection(Servo.Direction.REVERSE);
        finger1.setInitialPosition(0.5);
        finger2.setInitialPosition(0.5);
        arm1.setInitialPosition(0.5);
        arm2.setInitialPosition(0.5);

        this.hardware_manager.registerHardware(this.finger1).registerHardware(this.finger2).registerHardware(this.wrist).registerHardware(this.arm1).registerHardware(this.arm2);
    }
    // if isGrab == true, pincer will close
    public void pincer(boolean isGrab) {
        double fingerPosition = fingerOpenPosition;
        if (isGrab) {
            fingerPosition = fingerClosePosition;
        }
        while (finger1.isFinished() || finger2.isFinished()) {
            if (finger1.isFinished()) finger1.moveDirectly(fingerClosePosition, 1000);
            if (finger2.isFinished()) finger2.moveDirectly(fingerClosePosition, 1000);
        }
    }


    public void rotateArm(boolean isGrab) {
        double armPosition;
        if (isGrab) {
            arm1.setDirection(Servo.Direction.FORWARD);
            arm2.setDirection(Servo.Direction.REVERSE);
            armPosition = armGrabPosition;
        }
        else {
            arm1.setDirection(Servo.Direction.REVERSE);
            arm2.setDirection(Servo.Direction.FORWARD);
            armPosition = armDropPosition;
        }

        while (arm1.isFinished() || arm2.isFinished()) {
            if (arm1.isFinished()) arm1.moveWithInterval(armPosition, 2000, 1000);
            if (arm2.isFinished()) arm2.moveWithInterval(armPosition, 2000, 1000);
        }

    }

    public void rotateWrist(boolean isGrab) {
        double wristPosition;
        if (isGrab) {
            wrist.setDirection(Servo.Direction.FORWARD);
            wristPosition = wristGrabPosition;
        }
        else {
            wrist.setDirection(Servo.Direction.REVERSE);
            wristPosition = wristDropPosition;
        }

        while (wrist.isFinished()) {
            wrist.moveWithInterval(wristPosition, 1000, 1000);
        }
    }

    @Override
    protected void nextStep() {
        RobotCommand cmd = this.current_command;
        if (cmd == Command.GRAB_PIXEL) {
            pincer(true);
        }
        else if (cmd == Command.DROP_POSITION) {
            rotateArm(false);
            rotateWrist(false);

        }
        else if (cmd == Command.DROP_PIXEL) {
            pincer(false);
        }
        else if (cmd == Command.GRAB_POSITION) {
            pincer(true);
            rotateWrist(true);
            rotateArm(true);

            pincer(false);
        }
    }
}
