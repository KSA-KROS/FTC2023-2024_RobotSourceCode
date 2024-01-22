package org.firstinspires.ftc.teamcode.mainop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.DistSensorHW;
import org.firstinspires.ftc.teamcode.part.HardwareManager;
import org.firstinspires.ftc.teamcode.part.LinearPart;
import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.part.PincerPart;
import org.firstinspires.ftc.teamcode.part.RobotCommand;
import org.firstinspires.ftc.teamcode.part.WheelPart;

@Autonomous(name = "AutoOpMode", group = "")
public class AutoOpMode extends LinearOpMode {
    public LinearPart linear_part;
    public PincerPart pincer_part;
    public WheelPart wheel_part;
    public DistSensorHW dist;

    protected Telemetry telemetry;
    protected HardwareMap hardware_map;
    protected HardwareManager hardware_manager;
    protected RobotCommand current_command = Part.Command.NONE;
    protected int step = 0;
    private boolean finish = true;
    private long delay_time = 0;
    private boolean run = false;

    public final static int detectPosLength = 1000;

    @Override
    public void runOpMode() throws InterruptedException {
        // init
        this.linear_part = new LinearPart(hardware_map, telemetry);
        this.pincer_part = new PincerPart(hardware_map, telemetry);
        this.wheel_part = new WheelPart(hardware_map, telemetry);

        // start
        startStep(Command.DETECT_PIXELS);

        // Command Procedure
        Command[] command_procedure = {
                /* 1. */ Command.DETECT_PIXELS,
                /* 2. */ Command.DROP_PIXELS,
                /* 3. */ Command.PARK
        };
        int procedure_step = 0;

        // loop
        run = true;
        while (run) {
            this.linear_part.update();
            this.pincer_part.update();
            this.wheel_part.update();
            this.update();

            if (this.isFinished()) {
                this.startStep(command_procedure[procedure_step++]);
                if (procedure_step >= command_procedure.length) run = false;
            }
        }
    }

    public enum Command implements RobotCommand {
        DETECT_PIXELS,
        DROP_PIXELS,
        PARK
    }

    // Begin the specific step
    public void startStep(RobotCommand cmd){
        if(this.isFinished()) {
            this.current_command = cmd;
            this.step = 0;
            this.finish = false;
            this.nextStep();
        }
    }

    public void nextStep() {
        RobotCommand cmd = this.current_command;
        if (cmd == Command.DETECT_PIXELS) {
            switch (this.step) {
                case 0:
                    // move to detect position
                    wheel_part.startStep(WheelPart.Command.MOVE_DETECT_POS);
                    break;
                case 1:
                    // turn and detect pixels
                    if (dist.isObjectDetected()) {
                        this.step++;
                    } else {
                        wheel_part.startStep(WheelPart.Command.VIEW_RIGHT);
                        if (!dist.isObjectDetected()) {
                            wheel_part.startStep(WheelPart.Command.VIEW_LEFT);
                        }
                        this.step++;
                    }
                    break;
                case 2:
                    // drop pixel and turn to original position
                    pincer_part.startStep(PincerPart.Command.DROP_PIXEL_LEFT);
                    wheel_part.startStep(WheelPart.Command.VIEW_LEFT);
                    break;
                case 3:
                    this.finishCommand();
                    break;
            }
        } else if (cmd == Command.DROP_PIXELS) {
            switch (this.step) {
                case 0:
                    // turn arm
                    linear_part.startStep(LinearPart.Command.MOVE_DROP_POSITION);
                    pincer_part.startStep(PincerPart.Command.MOVE_DROP_OR_GRAB_POSITION);
                    break;
                case 1:
                    // move to board
                    break;
                case 2:
                    // move to drop position
                    break;
                case 3:
                    // drop pixels
                    pincer_part.startStep(PincerPart.Command.DROP_PIXEL_RIGHT);
                    break;
                case 4:
                    this.finishCommand();
                    break;
            }
        } else if (cmd == Command.PARK) {
            switch (this.step) {
                case 0:
                    // move back a little
                    break;
                case 1:
                    // rotate arm
                    pincer_part.startStep(PincerPart.Command.MOVE_DROP_OR_GRAB_POSITION);
                    break;
                case 2:
                    // park;
                    break;
                case 3:
                    this.finishCommand();
                    break;
            }
        }
    }

    protected void delayTime(long delay) {
        delay_time = System.currentTimeMillis() + delay;
    }

    // Change to the next step
    private void changeToTheNextStep() {
        this.nextStep();
        this.step++;
    }

    // Update the hardware objects of the part and check the step was finished
    public void update(){
        if(this.linear_part.isFinished() && this.pincer_part.isFinished()
                && this.wheel_part.isFinished() && System.currentTimeMillis() > this.delay_time) {
            this.changeToTheNextStep();
        }
    }

    // Check that the assigned command is finished
    private boolean isFinished(){
        return this.finish;
    }

    private void finishCommand() { this.finish = true; }
}




