package org.firstinspires.ftc.teamcode.part;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.DcMotorHW;

public class DdalggakPart extends Part {
    DcMotorHW ddalggak1, ddalggak2;
    public int open_encoder_value = 1000;
    public int close_encoder_value = 0;

    public enum Command implements RobotCommand {
        OPEN_DDALGGAK,
        CLOSE_DDALGGAK,
        DDALGGAK_ACTION
    }

    // Constructor
    public DdalggakPart(HardwareMap hwm, Telemetry tel) {
        super(hwm, tel);

        this.ddalggak1 = new DcMotorHW("ddalggak1", hwm, tel);
        this.ddalggak2 = new DcMotorHW("ddalggak2", hwm, tel);

        ddalggak1.setUsingBrake(true).setUsingFixation(true).setUsingEncoder(false);
        ddalggak2.setUsingBrake(true).setUsingFixation(true).setUsingEncoder(false);

        ddalggak1.setDirection(DcMotorSimple.Direction.FORWARD);
        ddalggak2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    protected void nextStep() {
        RobotCommand cmd = this.current_command;
        if (this.current_command == Command.OPEN_DDALGGAK) {
            switch (this.step) {
                case 0 :
                    ddalggak1.move(1.0, open_encoder_value);
                    ddalggak2.move(1.0, open_encoder_value);
                    break;
                case 1 :
                    this.finishStep();
                    break;
            }
        }
        else if (this.current_command == Command.CLOSE_DDALGGAK) {
            switch (this.step) {
                case 0 :
                    ddalggak1.move(1.0, close_encoder_value);
                    ddalggak2.move(1.0, close_encoder_value);
                    break;
                case 1 :
                    this.finishStep();
                    break;
            }
        }
        else if (this.current_command == Command.DDALGGAK_ACTION) {
            switch (this.step) {
                case 0 :
                    ddalggak1.move(1.0, close_encoder_value);
                    ddalggak2.move(1.0, close_encoder_value);
                    break;
                case 1 :
                    ddalggak1.move(1.0, open_encoder_value);
                    ddalggak2.move(1.0, open_encoder_value);
                    break;
                case 2 :
                    this.finishStep();
                    break;
            }
        }
    }
}
