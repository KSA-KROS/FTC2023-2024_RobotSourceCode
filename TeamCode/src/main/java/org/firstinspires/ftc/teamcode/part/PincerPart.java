package org.firstinspires.ftc.teamcode.part;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.ServoHW;

public class PincerPart extends Part {
    ServoHW finger1, finger2, arm1, arm2, wrist;
    private final double fingerClosePosition = 0.36;
    private final double fingerOpenPosition = 0.5;


    private final double wristDropPosition = 0.87;
    private final double wristGrabPosition = 0.5;

    private final double armGrabPosition = 0.685;
    private final double armDropPosition = 0;

    public boolean is_left_opend = true;
    public boolean is_right_opend = true;

    public enum Command implements RobotCommand {
        GRAB_PIXEL_LEFT,
        GRAB_PIXEL_RIGHT,
        MOVE_DROP_POSITION,
        DROP_PIXEL_LEFT,
        DROP_PIXEL_RIGHT,
        MOVE_GRAB_POSITION,
        GRAB_OR_DROP_PIXEL_LEFT,
        GRAB_OR_DROP_PIXEL_RIGHT,
    }

    // Constructor
    public PincerPart(HardwareMap hwm, Telemetry tel) {
        super(hwm, tel);

        this.finger1 = new ServoHW("s0", hwm, telemetry);
        this.finger2 = new ServoHW("s1", hwm, telemetry);
        this.wrist = new ServoHW("s2", hwm, telemetry);
        this.arm1 = new ServoHW("s3", hwm, telemetry);
        this.arm2 = new ServoHW("s4", hwm, telemetry);

        finger1.setDirection(Servo.Direction.FORWARD);
        finger2.setDirection(Servo.Direction.REVERSE);
        wrist.setDirection(Servo.Direction.FORWARD);
        arm1.setDirection(Servo.Direction.FORWARD);
        arm2.setDirection(Servo.Direction.REVERSE);

        finger1.setInitialPosition(fingerOpenPosition);
        finger2.setInitialPosition(fingerOpenPosition);
        wrist.setInitialPosition(wristGrabPosition);
        //arm1.moveWithInterval(armGrabPosition, 500, 0);
        //arm2.moveWithInterval(armGrabPosition, 500, 0);
        arm1.setInitialPosition(armGrabPosition);
        arm2.setInitialPosition(armGrabPosition);

        this.hardware_manager.registerHardware(this.finger1).registerHardware(this.finger2);
        this.hardware_manager.registerHardware(this.wrist);
        this.hardware_manager.registerHardware(this.arm1).registerHardware(this.arm2);

        this.is_left_opend = true;
        this.is_right_opend = true;
    }

    @Override
    protected void nextStep() {
        RobotCommand cmd = this.current_command;
        if (cmd == Command.GRAB_PIXEL_LEFT) {
            switch (this.step){
                case 0:
                    this.is_left_opend = false;
                    this.finger1.moveDirectly(fingerClosePosition);
                    this.finishStep();
                    break;
            }
        }
        else if (cmd == Command.GRAB_PIXEL_RIGHT) {
            switch (this.step){
                case 0:
                    this.is_right_opend = false;
                    this.finger2.moveDirectly(fingerClosePosition);
                    this.finishStep();
                    break;            }
        }
        else if (cmd == Command.DROP_PIXEL_LEFT) {
            switch (this.step){
                case 0:
                    this.is_left_opend = true;
                    this.finger1.moveDirectly(fingerOpenPosition);
                    this.finishStep();
                    break;
            }
        }
        else if (cmd == Command.DROP_PIXEL_RIGHT) {
            switch (this.step){
                case 0:
                    this.is_right_opend = true;
                    this.finger2.moveDirectly(fingerOpenPosition);
                    this.finishStep();
                    break;
            }
        }
        else if (cmd == Command.MOVE_DROP_POSITION) {
            switch (this.step){
                case 0:
                    this.arm1.moveWithInterval(armDropPosition, 2000);
                    this.arm2.moveWithInterval(armDropPosition, 2000);
                    break;
                case 1:
                    this.wrist.moveDirectly(wristDropPosition);
                    break;
                case 2:
                    this.finishStep();
                    break;
            }
        }
        else if (cmd == Command.MOVE_GRAB_POSITION) {
            switch (this.step){
                case 0:
                    this.wrist.moveDirectly(wristGrabPosition);
                    break;
                case 1:
                    this.arm1.moveWithInterval(armGrabPosition, 2000);
                    this.arm2.moveWithInterval(armGrabPosition, 2000);
                    break;
                case 2:
                    this.finishStep();
                    break;
            }
        }
        else if (cmd == Command.GRAB_OR_DROP_PIXEL_LEFT) {
            if (this.is_left_opend) {
                this.finishStep();
                this.startStep(Command.GRAB_PIXEL_LEFT);
            }
            else {
                this.finishStep();
                this.startStep(Command.DROP_PIXEL_LEFT);
            }
        }
        else if (cmd == Command.GRAB_OR_DROP_PIXEL_RIGHT) {
            if (this.is_right_opend) {
                this.finishStep();
                this.startStep(Command.GRAB_PIXEL_RIGHT);
            }
            else {
                this.finishStep();
                this.startStep(Command.DROP_PIXEL_RIGHT);
            }
        }
    }
}
