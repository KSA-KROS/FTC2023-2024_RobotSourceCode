package org.firstinspires.ftc.teamcode.part;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LinearPart extends Part {

    enum Command implements RobotCommand {
        MOVE_UP,
        MOVE_DOWN,
        STOP
    }

    // Constructor
    LinearPart(HardwareMap hwm, Telemetry tel) {
        super(hwm, tel);
    }

    @Override
    protected void nextStep() {
        RobotCommand cmd = this.current_command;
        if (cmd == Command.MOVE_UP) {

        }
        else if (cmd == Command.MOVE_DOWN) {

        }
        else if (cmd == Command.STOP) {

        }
    }
}
