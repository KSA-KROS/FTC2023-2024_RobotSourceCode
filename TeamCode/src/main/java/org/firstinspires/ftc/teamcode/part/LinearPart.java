package org.firstinspires.ftc.teamcode.part;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.DcMotorHW;

public class LinearPart extends Part {
    DcMotorHW linear1, linear2;
    public boolean expand = true;
    public double linearSpeed = 0.5;
    public double linearFixPower = 0.3;
    public int linearLength = 2000;
    public double ticks_per_rotation_1, ticks_per_rotation_2;
    public boolean is_completed_1 = false, is_completed_2 = false;

    public enum Command implements RobotCommand {
        MOVE_UP,
        MOVE_DOWN,
        STOP
    }

    // Constructor
    public LinearPart(HardwareMap hwm, Telemetry tel) {
        super(hwm, tel);

        this.linear1 = new DcMotorHW("linear1", hwm, telemetry);
        this.linear2 = new DcMotorHW("linear2", hwm, telemetry);

        linear1.setUsingBrake(true).setUsingFixation(true).setUsingEncoder(false);
        linear2.setUsingBrake(true).setUsingFixation(true).setUsingEncoder(false);
    }

    public void moveLinear(double magnitude, int targetTicks, double fixationPower) {
        linear1.setDirection(expand ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        linear2.setDirection(expand ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        linear1.move(magnitude, targetTicks, fixationPower);
        linear2.move(magnitude, targetTicks, fixationPower);
    }

    @Override
    protected void nextStep() {
        RobotCommand cmd = this.current_command;
        if (cmd == Command.MOVE_UP) {
            switch (this.step) {
                case 0:
                    expand = true;
                    moveLinear(linearSpeed, linearLength, linearFixPower);
                    break;
                case 1:
                    this.finish_step();
                    break;
            }
        }
        else if (cmd == Command.MOVE_DOWN) {
            switch (this.step) {
                case 0:
                    expand = false;
                    moveLinear(linearSpeed, linearLength, linearFixPower);
                    break;
                case 1:
                    this.finish_step();
                    break;
            }
        }
        else if (cmd == Command.STOP) {
            switch (this.step) {
                case 0:
                    linear1.stop();
                    linear2.stop();
                    break;
                case 1:
                    this.finish_step();
                    break;
            }
        }
    }
}
