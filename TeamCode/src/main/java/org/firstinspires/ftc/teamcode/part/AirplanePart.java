package org.firstinspires.ftc.teamcode.part;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.ServoHW;

public class AirplanePart extends Part {
    double airplaneInitPos = 0.5;
    double airplaneFlyPos = 0.3;
    ServoHW airplane;
    public enum Command implements RobotCommand {
        FLY
    }
    public AirplanePart(HardwareMap hwm, Telemetry tel) {
        super(hwm, tel);

        this.airplane = new ServoHW("airplane", hwm, tel);
        this.hardware_manager.registerHardware(airplane);
    }
    @Override
    protected void nextStep() {
        RobotCommand cmd = this.current_command;
        if (cmd == Command.FLY) {
            switch (this.step) {
                case 0:
                    airplane.moveDirectly(airplaneFlyPos);
                    this.finishStep();
                    break;
            }
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
