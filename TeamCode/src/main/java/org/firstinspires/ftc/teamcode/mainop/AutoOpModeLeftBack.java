package org.firstinspires.ftc.teamcode.mainop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.part.Part;
import org.firstinspires.ftc.teamcode.part.RobotCommand;

@Autonomous(name = "[BLUE] AutoOpMode : Back", group = "")
public class AutoOpModeLeftBack extends LinearOpMode {
    protected RobotCommand current_command = Part.Command.NONE;

    @Override
    public void runOpMode() throws InterruptedException {
        AutoOpModeRight opmode = new AutoOpModeRight();

        // init
        waitForStart();

        // Command Procedure
        AutoOpModeRight.Command[] command_procedure = {
                AutoOpModeRight.Command.RESET,
                AutoOpModeRight.Command.DETECT_PIXELS
        };
        int procedure_step = -1;

        // loop
        while (true) {
            opmode.linear_part.update();
            opmode.pincer_part.update();
            opmode.wheel_part.update();
            opmode.ddalggak_part.update();
            opmode.update();

            //this.telemetry.update();

            if (opmode.isFinished()) {
                if (++procedure_step >= command_procedure.length) break;
                opmode.startStep(command_procedure[procedure_step]);
            }
        }
    }
}




