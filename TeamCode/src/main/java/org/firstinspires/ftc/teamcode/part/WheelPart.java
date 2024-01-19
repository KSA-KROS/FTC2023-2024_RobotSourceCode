package org.firstinspires.ftc.teamcode.part;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.DcMotorHW;

public class WheelPart extends Part {
    // FR: Front-Right, FL: Front-Left, BR: Back-Right, BL: Back-Left
    DcMotorHW wheelFR, wheelFL, wheelBR, wheelBL;
    public enum Command implements RobotCommand {
        MOVE_FORWARD,
        MOVE_BACKWARD,
        MOVE_LEFT,
        MOVE_RIGHT,
        TURN_LEFT,
        TURN_RIGHT,
        STOP
    }
    public WheelPart(HardwareMap hwm, Telemetry tel) {
        super(hwm, tel);

        this.wheelFR = new DcMotorHW("wheelFR", hwm, tel);
        this.wheelFL = new DcMotorHW("wheelFL", hwm, tel);
        this.wheelBR = new DcMotorHW("wheelBR", hwm, tel);
        this.wheelBL = new DcMotorHW("wheelBL", hwm, tel);

        wheelFR.setUsingBrake(true).setUsingEncoder(false);
        wheelFL.setUsingBrake(true).setUsingEncoder(false);
        wheelBR.setUsingBrake(true).setUsingEncoder(false);
        wheelBL.setUsingBrake(true).setUsingEncoder(false);
    }

    @Override
    protected void nextStep() {
        RobotCommand cmd = this.current_command;

    }
}
