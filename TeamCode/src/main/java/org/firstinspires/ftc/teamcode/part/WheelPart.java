package org.firstinspires.ftc.teamcode.part;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.DcMotorHW;
import org.firstinspires.ftc.teamcode.hardware.IMUHW;

public class WheelPart extends Part {
    // FR: Front-Right, FL: Front-Left, BR: Back-Right, BL: Back-Left
    DcMotorHW wheelFR, wheelFL, wheelBR, wheelBL;
    public IMUHW imuhw;
    public enum Command implements RobotCommand {
        MOVE_FORWARD,
        MOVE_BACKWARD,
        MOVE_LEFT,
        MOVE_RIGHT,
        TURN_LEFT,
        TURN_RIGHT,
        VIEW_RIGHT,
        VIEW_LEFT,
        VIEW_FORWARD,
        VIEW_BACKWARD,
        STOP
    }

    public enum Direction {
        Forward(new DirectionData(1,1,1,1)),
        Backward(new DirectionData(-1,-1,-1, -1)),
        Left(new DirectionData(-1,1,1,-1)),
        Right(new DirectionData(1,-1,-1,1)),
        TurnLeft(new DirectionData(-1,1,-1,1)),
        TurnRight(new DirectionData(1,-1,1,-1));

        private final DirectionData value;
        Direction(DirectionData i) {this.value = i;}
        DirectionData get_value() {return this.value;}

        public static class DirectionData{
            private double front_left_speed = 1.0;
            private double front_right_speed = 1.0;
            private double back_left_speed = 1.0;
            private double back_right_speed = 1.0;

            public double front_left, front_right, back_left, back_right;
            public DirectionData(double front_left, double front_right, double back_left, double back_right){
                this.front_left = front_left * front_left_speed;
                this.front_right = front_right * front_right_speed;
                this.back_left = back_left * back_left_speed;
                this.back_right = back_right * back_right_speed;
            }
        }
    }

    public double wheelSpeed = 0.5;
    public WheelPart(HardwareMap hwm, Telemetry tel) {
        super(hwm, tel);

        this.wheelFR = new DcMotorHW("wheelFR", hwm, tel);
        this.wheelFL = new DcMotorHW("wheelFL", hwm, tel);
        this.wheelBR = new DcMotorHW("wheelBR", hwm, tel);
        this.wheelBL = new DcMotorHW("wheelBL", hwm, tel);

        this.imuhw = new IMUHW("imu", hwm, tel);

        wheelFR.setUsingBrake(true).setUsingEncoder(false).setDirection(DcMotorSimple.Direction.FORWARD);
        wheelFL.setUsingBrake(true).setUsingEncoder(false).setDirection(DcMotorSimple.Direction.REVERSE);
        wheelBR.setUsingBrake(true).setUsingEncoder(false).setDirection(DcMotorSimple.Direction.FORWARD);
        wheelBL.setUsingBrake(true).setUsingEncoder(false).setDirection(DcMotorSimple.Direction.REVERSE);

        this.hardware_manager.registerHardware(this.wheelFR).registerHardware(this.wheelFL);
        this.hardware_manager.registerHardware(this.wheelBR).registerHardware(this.wheelBL);
        this.hardware_manager.registerHardware(this.imuhw);
    }

    public void stop() {
        wheelFR.stop();
        wheelFL.stop();
        wheelBR.stop();
        wheelBL.stop();
    }


    public void move(double speed, Direction dir){
        this.wheelFL.move(speed * dir.get_value().front_left);
        this.wheelFR.move(speed * dir.get_value().front_right);
        this.wheelBL.move(speed * dir.get_value().back_left);
        this.wheelBR.move(speed * dir.get_value().back_right);
    }

    public boolean turn = false;

    public int move(double speed, double angle) {
        double currentAngle = imuhw.currentAngle;
        Direction dir;
        double right = (currentAngle - angle + 360) % 360;
        double left = (angle - currentAngle + 360) % 360;
        double level = Math.min(left, right);
        this.telemetry.addData("Current Angle", currentAngle);
        this.telemetry.addData("Left", left);
        this.telemetry.addData("Right", right);
        this.telemetry.addData("Delta Angle", level);
        if (left < right) {
            dir = Direction.TurnLeft;
        } else {
            dir = Direction.TurnRight;
        }
        if (currentAngle != angle) {
            if (level < 10) {
                speed *= level / 10;
            }
            move(speed, dir);
        }
        return 0;
    }

    @Override
    protected void nextStep() {
        RobotCommand cmd = this.current_command;

        // Move
        if (cmd == WheelPart.Command.MOVE_FORWARD) {
            switch (this.step) {
                case 0:
                    this.move(wheelSpeed, Direction.Forward);
                    this.finishStep();
                    break;
            }
        } else if (cmd == WheelPart.Command.MOVE_BACKWARD) {
            switch (this.step) {
                case 0:
                    this.move(wheelSpeed, Direction.Backward);
                    this.finishStep();
                    break;
            }
        } else if (cmd == WheelPart.Command.MOVE_LEFT) {
            switch (this.step) {
                case 0:
                    this.move(wheelSpeed, Direction.Left);
                    this.finishStep();
                    break;
            }
        } else if (cmd == WheelPart.Command.MOVE_RIGHT) {
            switch (this.step) {
                case 0:
                    this.move(wheelSpeed, Direction.Right);
                    this.finishStep();
                    break;
            }
        }

        // Turn
        else if (cmd == WheelPart.Command.TURN_LEFT) {
            switch (this.step) {
                case 0:
                    this.move(wheelSpeed, Direction.TurnLeft);
                    this.finishStep();
                    break;
            }
        } else if (cmd == WheelPart.Command.TURN_RIGHT) {
            switch (this.step) {
                case 0:
                    this.move(wheelSpeed, Direction.TurnRight);
                    this.finishStep();
                    break;
            }
        } else if (cmd == WheelPart.Command.VIEW_RIGHT) {
            switch (this.step) {
                case 0:
                    this.move(wheelSpeed, -90.0);
                    this.finishStep();
                    break;
            }
        } else if (cmd == WheelPart.Command.VIEW_LEFT) {
            switch (this.step) {
                case 0:
                    this.move(wheelSpeed, 90.0);
                    this.finishStep();
                    break;
            }
        } else if (cmd == WheelPart.Command.VIEW_FORWARD) {
            switch (this.step) {
                case 0:
                    this.move(wheelSpeed, 0.0);
                    this.finishStep();
                    break;
            }
        } else if (cmd == WheelPart.Command.VIEW_BACKWARD) {
            switch (this.step) {
                case 0:
                    this.move(wheelSpeed, 180.0);
                    this.finishStep();
                    break;
            }
        }

        // Stop
        else if (cmd == WheelPart.Command.STOP) {
            switch (this.step) {
                case 0:
                    this.stop();
                    this.finishStep();
                    break;
            }
        }

    }
}
