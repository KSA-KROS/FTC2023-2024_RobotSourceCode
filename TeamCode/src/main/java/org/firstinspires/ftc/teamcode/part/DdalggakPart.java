package org.firstinspires.ftc.teamcode.part;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.DcMotorHW;
import org.firstinspires.ftc.teamcode.hardware.MagSensorHW;

public class DdalggakPart extends Part {
    DcMotorHW ddalggak1, ddalggak2;

    MagSensorHW mag1;

    public enum Command implements RobotCommand {
        OPEN_DDALGGAK,
        CLOSE_DDALGGAK
    }

    // Constructor
    public DdalggakPart(HardwareMap hwm, Telemetry tel) {
        super(hwm, tel);

        this.ddalggak1 = new DcMotorHW("ddalggak1", hwm, tel);
        this.ddalggak2 = new DcMotorHW("ddalggak2", hwm, tel);

        this.mag1 = new MagSensorHW("dgmag1", hwm, tel);
        this.mag1.notUse();

        ddalggak1.setUsingBrake(true).setUsingFixation(true).setUsingEncoder(false);
        ddalggak2.setUsingBrake(true).setUsingFixation(true).setUsingEncoder(false);

        this.hardware_manager.registerHardware(ddalggak1)/*.registerHardware(ddalggak2)*/;
        this.hardware_manager.registerHardware(mag1);
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
                    ddalggak1.move(0.05);
                    ddalggak2.move(0.05);
                    mag1.untilActivated();
                    break;
                case 1:
                    ddalggak1.stop();
                    ddalggak2.stop();
                    mag1.notUse();
                    this.finishStep();
                    break;
            }
        }
        else if (this.current_command == Command.CLOSE_DDALGGAK) {
            switch (this.step) {
                case 0 :
                    this.setCloseDirection();
                    ddalggak1.move(0.5);
                    ddalggak2.move(0.5);
                    while(!ddalggak1.isFinished()){
                        ddalggak1.update();
                    }
                    ddalggak1.stop();
                    ddalggak2.stop();
                    this.finishStep();
                    break;
            }
        }
    }

    @Override
    public void update(){
        super.update();
        this.telemetry.addData("Degree", this.ddalggak1.getCurrentTick());
    }
}
