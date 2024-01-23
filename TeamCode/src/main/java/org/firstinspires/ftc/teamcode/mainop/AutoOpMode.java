package org.firstinspires.ftc.teamcode.mainop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.DistSensorHW;
import org.firstinspires.ftc.teamcode.part.DdalggakPart;
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
    public DistSensorHW dist, distright;
    public DdalggakPart ddalggak_part;

    protected HardwareManager hardware_manager;
    protected RobotCommand current_command = Part.Command.NONE;
    protected int step = 0;
    private boolean finish = true;
    private long delay_time = 0;
    private boolean run = false;

    public final static int detectPosLength = 1000;

    public static WheelPart.Direction wheelMoveDir;
    public static int wheelMoveLength;
    public int robotPixelPos = 0;
    public int pixelPos = -1;

    @Override
    public void runOpMode() throws InterruptedException {
        // init
        this.linear_part = new LinearPart(hardwareMap, telemetry);
        this.pincer_part = new PincerPart(hardwareMap, telemetry);
        this.wheel_part = new WheelPart(hardwareMap, telemetry);
        this.ddalggak_part = new DdalggakPart(hardwareMap, telemetry);

        dist = new DistSensorHW("backboard", hardwareMap, telemetry);
        distright = new DistSensorHW("distright", hardwareMap, telemetry);






        waitForStart();

        // start
        // startStep(Command.DETECT_PIXELS);

        // Command Procedure
        Command[] command_procedure = {
                Command.RESET,
                /* 1. */ Command.DETECT_PIXELS,
                /* 2. */ Command.DROP_PIXELS,
                /* 3. */ Command.PARK
        };
        int procedure_step = -1;

        // loop
        run = true;
        while (run) {
            this.linear_part.update();
            this.pincer_part.update();
            this.wheel_part.update();
            this.update();

            this.telemetry.update();

            if (this.isFinished()) {
                this.startStep(command_procedure[++procedure_step]);
                telemetry.addData("Procedure Step", procedure_step);
                if (procedure_step >= command_procedure.length) run = false;
            }
        }
    }

    public enum Command implements RobotCommand {
        DETECT_PIXELS,
        DROP_PIXELS,
        PARK,
        RESET
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
        if (cmd == Command.RESET) {
            switch (this.step) {
                case 0:
                    pincer_part.startStep(PincerPart.Command.GRAB_PIXEL_LEFT);
                    pincer_part.startStep(PincerPart.Command.GRAB_PIXEL_RIGHT);
                    delayTime(1000);
                    break;
                    /*
                case 1:
                    ddalggak_part.startStep(DdalggakPart.Command.RESET_DDALGGAK);
                case 2:
                    this.finishCommand();
                    break;

                     */
                case 1:
                    this.finishCommand();
                    break;
            }
        }
        else if (cmd == Command.DETECT_PIXELS) {
            switch (this.step) {
                case 0:
                    // move to detect position
                    wheelMoveDir = WheelPart.Direction.Forward;
                    wheelMoveLength = 1100;
                    wheel_part.startStep(WheelPart.Command.AUTO_MOVE);
                    break;
                    /*
                case 1:
                    // turn and detect pixels
                    if (!dist.isObjectDetected()) {
                        wheel_part.startStep(WheelPart.Command.VIEW_RIGHT);
                    }
                    break;
                case 2:
                    if (!dist.isObjectDetected()) {
                        wheel_part.startStep(WheelPart.Command.VIEW_LEFT);
                    }
                    break;
                case 3:
                    // drop pixel and turn to original position
                    pincer_part.startStep(PincerPart.Command.DROP_PIXEL_LEFT);
                    break;
                case 4:
                    wheel_part.startStep(WheelPart.Command.VIEW_LEFT);
                    break;
                case 5:
                    this.finishCommand();
                    break;

                     */
                case 1:
                    if (dist.isObjectDetected()) {

                        wheel_part.startStep(WheelPart.Command.VIEW_BACKWARD);
                        wheelMoveDir = WheelPart.Direction.Forward;
                        pixelPos = 1;
                    } else if (distright.isObjectDetected()) {
                        wheelMoveLength = 50;
                        wheelMoveDir = WheelPart.Direction.Forward;
                        wheel_part.startStep(WheelPart.Command.AUTO_MOVE);

                        pixelPos = 2;
                    } else {
                        wheelMoveLength = 50;
                        wheelMoveDir = WheelPart.Direction.Forward;
                        wheel_part.startStep(WheelPart.Command.AUTO_MOVE);

                        pixelPos = 0;
                    }
                    break;
                case 2:
                    if (pixelPos == 2) {
                        wheel_part.startStep(WheelPart.Command.VIEW_LEFT);
                    } else if (pixelPos == 0) {
                        wheel_part.startStep(WheelPart.Command.VIEW_RIGHT);
                    }
                    break;
                case 3:

                    if (pixelPos == 2) {
                        wheelMoveDir = WheelPart.Direction.Backward;
                        wheelMoveLength = 50;
                        wheel_part.startStep(WheelPart.Command.AUTO_MOVE);
                        wheelMoveDir = WheelPart.Direction.Left;
                        robotPixelPos = 100;
                    } else if (pixelPos == 0) {
                        wheelMoveDir = WheelPart.Direction.Backward;
                        wheelMoveLength = 50;
                        wheel_part.startStep(WheelPart.Command.AUTO_MOVE);
                        wheelMoveDir = WheelPart.Direction.Right;
                        robotPixelPos = 100;
                    }
                    break;
                case 4:
                    pincer_part.startStep(PincerPart.Command.DROP_PIXEL_LEFT);
                    pincer_part.startStep(PincerPart.Command.MOVE_DROP_OR_GRAB_POSITION);
                    break;
                    /*
                case 3:
                    wheel_part.startStep(WheelPart.Command.VIEW_LEFT);
                    break;
                case 4:
                    this.finishCommand();
                    break;
                    */
                case 5:
                    wheelMoveLength = 800 + robotPixelPos;
                    wheel_part.startStep(WheelPart.Command.AUTO_MOVE);
                    break;
                case 6:
                    wheel_part.startStep(WheelPart.Command.VIEW_LEFT);
                    break;
                case 7:
                    this.finishCommand();
                    break;


            }
        } else if (cmd == Command.DROP_PIXELS) {
            switch (this.step) {
                case 0:
                    // turn arm
                    // pincer_part.startStep(PincerPart.Command.MOVE_DROP_OR_GRAB_POSITION);
                    linear_part.startStep(LinearPart.Command.MOVE_DROP_POSITION);
                    break;
                case 1:
                    wheelMoveDir = WheelPart.Direction.Forward;
                    wheelMoveLength = 900;
                    wheel_part.startStep(WheelPart.Command.AUTO_MOVE);
                    break;
                case 2:
                    // move to board
                    while (!dist.isObjectDetected(50)) {
                        wheel_part.move(wheel_part.wheelSpeed, WheelPart.Direction.Right);
                    }
                    delayTime(1000);
                    // wheel_part.startStep(WheelPart.Command.STOP);
                    break;
                case 3:
                    wheelMoveDir = WheelPart.Direction.Right;
                    wheelMoveLength = 50 + pixelPos * 40;
                    wheel_part.startStep(WheelPart.Command.AUTO_MOVE);
                    // move to drop position
                    break;
                case 4:
                    while (!dist.isObjectDetected(2)) {
                        wheel_part.move(wheel_part.wheelSpeed, WheelPart.Direction.Forward);
                    }
                    wheel_part.startStep(WheelPart.Command.STOP);
                    break;
                case 5:
                    // drop pixels
                    delayTime(1000);
                    pincer_part.startStep(PincerPart.Command.DROP_PIXEL_RIGHT);
                    break;
                case 6:
                    this.finishCommand();
                    break;
            }
        } else if (cmd == Command.PARK) {
            switch (this.step) {
                case 0:
                    // move back a little
                    telemetry.addLine("case0");
                    delayTime(1000);
                    wheelMoveDir = WheelPart.Direction.Left;
                    wheelMoveLength = 200 + pixelPos * 300 + 400;
                    wheel_part.startStep(WheelPart.Command.AUTO_MOVE);
                    delayTime(1000);
                    break;
                case 1:
                    // rotate arm
                    linear_part.startStep(LinearPart.Command.MOVE_ORIGINAL_POSITION);
                    pincer_part.startStep(PincerPart.Command.MOVE_DROP_OR_GRAB_POSITION);
                    break;
                case 2:
                    // park;
                    break;
                case 3:
                    telemetry.addLine("FINISH");
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
        this.step++;
        this.nextStep();
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




