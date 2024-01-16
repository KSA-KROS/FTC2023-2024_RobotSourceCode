package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DcMotorHW extends Hardware {
    private DcMotor motor;
    private int target_ticks = 0;
    private boolean is_busy = false;
    private boolean using_fixation = false;
    private double fixation_power = 0.0;

    // ==================== Initialization ====================
    // Initialize the DC Motor with the standard settings
    DcMotorHW(String name, HardwareMap hwm, Telemetry tel) {
        super(name, hwm, tel);
        this.motor = hwm.get(DcMotor.class, this.name);
        this.motor.setPower(0);
        this.setDirection(DcMotor.Direction.FORWARD);
        this.setUsingEncoder(false).setUsingBrake(true);
        this.setUsingFixation(false);
    }

    void initEncoder() {
        DcMotor.RunMode mode = this.motor.getMode();
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setMode(mode);
    }

    // ==================== Settings ====================
    // Set the direction of the motor (FORWARD or REVERSE)
    public DcMotorHW setDirection(DcMotor.Direction direction) {
        this.motor.setDirection(direction);
        return this;
    }
    // Set the mode of the motor (false : RUN_WITHOUT_ENCODER or true : RUN_USING_ENCODER)
    public DcMotorHW setUsingEncoder(boolean use_encoder) {
        if (use_encoder) {
            this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else {
            this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        return this;
    }
    // Set the brake mode of the motor (false : FLOAT or true : BRAKE)
    public DcMotorHW setUsingBrake(boolean use_brake) {
        if (use_brake) {
            this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        return this;
    }
    // Set the fixation mode of the motor (false : not using or true : using)
    public DcMotorHW setUsingFixation(boolean use_fixation) {
        this.using_fixation = use_fixation;
        return this;
    }

    // ==================== Ordering Commands ====================
    // Move the motor with the given power
    public void move(double power) {
        this.initEncoder();
        this.target_ticks = 0;
        this.is_busy = false;
        this.motor.setPower(power);
    }
    // Move the motor with the given power and the given ticks
    public void move(double power, int ticks) {
        this.initEncoder();
        this.target_ticks = ticks;
        this.is_busy = true;
        this.fixation_power = 2.0; // AUTO CORRECTION
        this.motor.setPower(power);
    }
    // Move the motor with the given power and the given ticks and the given fixation power
    public void move(double power, int ticks, double fixation_power) {
        this.initEncoder();
        this.target_ticks = ticks;
        this.is_busy = is_busy;
        this.fixation_power = fixation_power;
        this.motor.setPower(power);
    }
    // Stop the motor
    public void stop() {
        this.target_ticks = 0;
        this.is_busy = false;
        this.motor.setPower(0);
    }

    @Override
    public void update() {
        if (this.is_busy) {
            if (Math.abs(this.motor.getCurrentPosition()) > this.target_ticks) {
                this.is_busy = false;
                this.stop();
            }
        }
        else if (this.using_fixation) {
            double power = this.fixation_power;
            if (power == 2.0) { // AUTO CORRECTION MODE
                // ideal_abs_power : ideal motor power (+ or -)
                // current_abs_power : current motor power (+ or -)
                double ideal_power = (double)(this.target_ticks - Math.abs(this.motor.getCurrentPosition())) / 10.0;
                double current_power = this.motor.getPower();
                if (ideal_power * current_power < 0) {
                    // if the motor is going to the opposite direction, power = ideal_power
                    power = ideal_power;
                } else {
                    // if the motor is going to the same direction, power = current_power + ideal_power
                    power = current_power + ideal_power;
                }
                // power should be between 0.0 and 1.0
                power = Math.abs(power);
                if (power > 1.0) {
                    power = 1.0;
                }
            }
            if (Math.abs(this.motor.getCurrentPosition()) > this.target_ticks) {
                this.move(-power);
            } else if (Math.abs(this.motor.getCurrentPosition()) < this.target_ticks) {
                this.move(power);
            } else {
                this.stop();
            }
        }
    }

    @Override
    public boolean isFinished() {
        return !this.is_busy;
    }

    @Override
    public void emergencyStop() {
        this.stop();
    }
}
