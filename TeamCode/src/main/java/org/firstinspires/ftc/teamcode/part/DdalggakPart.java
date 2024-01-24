package org.firstinspires.ftc.teamcode.part;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.DcMotorHW;
import org.firstinspires.ftc.teamcode.hardware.MagSensorHW;

public class DdalggakPart extends Part {
    DcMotorHW ddalggak1, ddalggak2;

    MagSensorHW mag1, mag2;
    private boolean isDdalggakOpen = true;

    private long time_limit = 2000;

    public enum Command implements RobotCommand {
        OPEN_OR_CLOSE_DDALGGAK,
        OPEN_OR_CLOSE_DDALGGAK_GENTLY,
        RESET_DDALGGAK
    }

    // Constructor
    public DdalggakPart(HardwareMap hwm, Telemetry tel) {
        super(hwm, tel);

        this.ddalggak1 = new DcMotorHW("ddalggak1", hwm, tel);
        this.ddalggak2 = new DcMotorHW("ddalggak2", hwm, tel);

        this.mag1 = new MagSensorHW("dgmag1", hwm, tel);
        this.mag2 = new MagSensorHW("dgmag2", hwm, tel);
        this.mag1.notUse();
        this.mag2.notUse();

        ddalggak1.setUsingBrake(true).setUsingFixation(false).setUsingEncoder(false);
        ddalggak2.setUsingBrake(true).setUsingFixation(false).setUsingEncoder(false);

        this.hardware_manager.registerHardware(ddalggak1).registerHardware(ddalggak2);
        this.hardware_manager.registerHardware(mag1).registerHardware(mag2);
    }

    private void setCloseDirection() {
        ddalggak1.setDirection(DcMotorSimple.Direction.REVERSE);
        ddalggak2.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    private void setOpenDirection() {
        ddalggak1.setDirection(DcMotorSimple.Direction.FORWARD);
        ddalggak2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public boolean isOpenState() {
        return this.isDdalggakOpen;
    }

    @Override
    protected void nextStep() {
        RobotCommand cmd = this.current_command;
        if (this.current_command == Command.OPEN_OR_CLOSE_DDALGGAK) {
            if (!isDdalggakOpen) {
                /*
                switch (this.step) {
                    case 0:
                        this.setOpenDirection();
                        ddalggak1.move(0.8, 75);
                        ddalggak2.move(0.8, 75);
                        int trigger = 0;
                        while (trigger != 3) {
                            ddalggak1.update();
                            ddalggak2.update();
                            if (trigger != 1 && ddalggak1.isFinished()) { ddalggak1.stop(); trigger |= 1;}
                            if (trigger != 2 && ddalggak2.isFinished()) { ddalggak2.stop(); trigger |= 2;}
                        }
                        isDdalggakOpen = true;
                        this.finishStep();
                        break;
                }
                */
                this.finishStep();
                this.startStep(Command.RESET_DDALGGAK);
            }
            else {
                switch (this.step) {
                    case 0 :
                        this.setCloseDirection();
                        ddalggak1.move(0.8, 75);
                        ddalggak2.move(0.8, 75);
                        int trigger = 0;
                        long begin_time = System.currentTimeMillis();
                        while (trigger != 3) {
                            ddalggak1.update();
                            ddalggak2.update();
                            if (trigger != 1 && ddalggak1.isFinished()) { ddalggak1.stop(); trigger |= 1;}
                            if (trigger != 2 && ddalggak2.isFinished()) { ddalggak2.stop(); trigger |= 2;}
                            if (System.currentTimeMillis() - begin_time > this.time_limit) {
                                break;
                            }
                        }
                        if (trigger == 3) {
                            isDdalggakOpen = false;
                            this.finishStep();
                        }
                        else {
                            this.finishStep();
                            this.startStep(Command.RESET_DDALGGAK);
                        }
                        break;
                }
            }
        }
        if (this.current_command == Command.OPEN_OR_CLOSE_DDALGGAK_GENTLY) {
            if (!isDdalggakOpen) {
                /*
                switch (this.step) {
                    case 0:
                        this.setOpenDirection();
                        ddalggak1.move(0.2, 75);
                        ddalggak2.move(0.2, 75);
                        int trigger = 0;
                        while (trigger != 3) {
                            ddalggak1.update();
                            ddalggak2.update();
                            if (trigger != 1 && ddalggak1.isFinished()) { ddalggak1.stop(); trigger |= 1;}
                            if (trigger != 2 && ddalggak2.isFinished()) { ddalggak2.stop(); trigger |= 2;}
                        }
                        isDdalggakOpen = true;
                        this.finishStep();
                        break;
                }
                */
                this.finishStep();
                this.startStep(Command.RESET_DDALGGAK);
            }
            else {
                switch (this.step) {
                    case 0 :
                        this.setCloseDirection();
                        ddalggak1.move(0.2, 75);
                        ddalggak2.move(0.2, 75);
                        int trigger = 0;
                        long begin_time = System.currentTimeMillis();
                        while (trigger != 3) {
                            ddalggak1.update();
                            ddalggak2.update();
                            if (trigger != 1 && ddalggak1.isFinished()) { ddalggak1.stop(); trigger |= 1;}
                            if (trigger != 2 && ddalggak2.isFinished()) { ddalggak2.stop(); trigger |= 2;}
                            if (System.currentTimeMillis() - begin_time > this.time_limit) {
                                break;
                            }
                        }
                        if (trigger == 3) {
                            isDdalggakOpen = false;
                            this.finishStep();
                        }
                        else {
                            this.finishStep();
                            this.startStep(Command.RESET_DDALGGAK);
                        }
                        break;
                }
            }
        }
        if (this.current_command == Command.RESET_DDALGGAK) {
            switch (this.step) {
                case 0 :
                    this.setOpenDirection();
                    ddalggak1.move(0.1);
                    ddalggak2.stop();
                    mag1.untilActivated();
                    mag2.notUse();
                    break;
                case 1:
                    mag1.untilInactivate();
                    break;
                case 2 :
                    this.setOpenDirection();
                    ddalggak1.stop();
                    ddalggak2.move(0.1);
                    mag1.notUse();
                    mag2.untilActivated();
                    break;
                case 3:
                    mag2.untilInactivate();
                    break;
                case 4:
                    ddalggak1.stop();
                    ddalggak2.stop();
                    mag1.notUse();
                    mag2.notUse();
                    this.finishStep();
                    this.isDdalggakOpen = true;
                    break;
            }
        }
    }

    @Override
    public void update(){
        super.update();
    }
}
