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

    public boolean is_opend = true;

    public enum Command implements RobotCommand {
        GRAB_PIXEL,
        MOVE_DROP_POSITION,
        DROP_PIXEL,
        MOVE_GRAB_POSITION,
        GRAB_OR_DROP_PIXEL
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

        finger1.setInitialPosition(0.5);
        finger2.setInitialPosition(0.5);
        wrist.setInitialPosition(0.5);
        arm1.setInitialPosition(0.5);
        arm2.setInitialPosition(0.5);

        this.hardware_manager.registerHardware(this.finger1).registerHardware(this.finger2);
        this.hardware_manager.registerHardware(this.wrist);
        this.hardware_manager.registerHardware(this.arm1).registerHardware(this.arm2);

        this.is_opend = true;
    }

    @Override
    protected void nextStep() {
        RobotCommand cmd = this.current_command;
        if (cmd == Command.GRAB_PIXEL) {
            switch (this.step){
                case 0:
                    this.finger1.moveDirectly(fingerClosePosition, 1000);
                    this.finger2.moveDirectly(fingerClosePosition, 1000);
                    break;
                case 1:
                    this.finishStep();
                    break;
            }
        }
        else if (cmd == Command.MOVE_DROP_POSITION) {
            switch (this.step){
                case 0:
                    this.arm1.moveWithInterval(armDropPosition, 2000, 1000);
                    this.arm2.moveWithInterval(armDropPosition, 2000, 1000);
                    break;
                case 1:
                    this.wrist.moveDirectly(wristDropPosition, 1000);
                    break;
                case 2:
                    this.finishStep();
                    break;
            }
        }
        else if (cmd == Command.DROP_PIXEL) {
            switch (this.step){
                case 0:
                    this.finger1.moveDirectly(fingerOpenPosition, 1000);
                    this.finger2.moveDirectly(fingerOpenPosition, 1000);
                    break;
                case 1:
                    this.finishStep();
                    break;
            }
        }
        else if (cmd == Command.MOVE_GRAB_POSITION) {
            switch (this.step){
                case 0:
                    this.wrist.moveDirectly(wristGrabPosition, 1000);
                    break;
                case 1:
                    this.arm1.moveWithInterval(armGrabPosition, 2000, 1000);
                    this.arm2.moveWithInterval(armGrabPosition, 2000, 1000);
                    break;
                case 2:
                    this.finishStep();
                    break;
            }
        }
        else if (cmd == Command.GRAB_OR_DROP_PIXEL) {
            if(this.finger1.getPosition() == fingerOpenPosition && this.finger2.getPosition() == fingerOpenPosition) {
                this.startStep(Command.GRAB_PIXEL);
            }
            else {
                this.startStep(Command.DROP_PIXEL);
            }
        }
    }
}
