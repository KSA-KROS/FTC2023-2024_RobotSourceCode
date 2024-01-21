package org.firstinspires.ftc.teamcode.part;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.DcMotorHW;

public class DdalggakPart extends Part {
    DcMotorHW ddalggak1, ddalggak2;
    public int encoder_value = 100;

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

        this.hardware_manager.registerHardware(ddalggak1).registerHardware(ddalggak2);
    }

    private void setCloseDirection() {
        ddalggak1.setDirection(DcMotorSimple.Direction.FORWARD);
        ddalggak2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    private void setOpenDirection() {
        ddalggak1.setDirection(DcMotorSimple.Direction.REVERSE);
        ddalggak2.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    protected void nextStep() {
        RobotCommand cmd = this.current_command;
        if (this.current_command == Command.OPEN_DDALGGAK) {
            switch (this.step) {
                case 0 :
                    this.setOpenDirection();
                    ddalggak1.move(0.5, encoder_value);
                    ddalggak2.move(0.5, encoder_value);
                case 1:
                    this.finishStep();
                    break;
            }
        }
        else if (this.current_command == Command.CLOSE_DDALGGAK) {
            switch (this.step) {
                case 0 :
                    this.setCloseDirection();
                    ddalggak1.move(0.5, encoder_value);
                    ddalggak2.move(0.5, encoder_value);
                    break;
                case 1 :
                    this.finishStep();
                    break;
            }
        }
        else if (this.current_command == Command.DDALGGAK_ACTION) {
            switch (this.step) {
                case 0 :
                    this.setCloseDirection();
                    ddalggak1.move(0.5, encoder_value);
                    ddalggak2.move(0.5, encoder_value);
                    break;
                case 1 :
                    this.setOpenDirection();
                    ddalggak1.move(0.5, encoder_value);
                    ddalggak2.move(0.5, encoder_value);
                    break;
                case 2 :
                    this.finishStep();
                    break;
            }
        }
    }
}
