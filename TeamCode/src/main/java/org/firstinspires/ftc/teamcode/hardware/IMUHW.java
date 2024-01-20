package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.util.Arrays;
public class IMUHW extends Hardware{
    private IMU imu;

    private double currentAngle = 0;
    private long prev_time = 0;
    private double[] imuAngles = {0, 0, 0};

    private RevHubOrientationOnRobot.LogoFacingDirection logoDirection;
    private RevHubOrientationOnRobot.UsbFacingDirection  usbDirection;

    public IMUHW(String name, HardwareMap hwm, Telemetry tel) {
        super(name, hwm, tel);
        this.imu = hwm.get(IMU.class, this.name);
        this.setOrientation();
    }

    private IMUHW setOrientation() {
        logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
        return this;
    }

    private int directionToInt(char rotationDirection) {
        int index = -1;
        switch (rotationDirection) {
            case 'Z':
                index = 0;
                break;
            case 'X':
                index = 1;
                break;
            case 'Y':
                index = 2;
                break;
        }
        return index;
    }

    public void resetPosition() {
        this.currentAngle = 0;
    }

    public YawPitchRollAngles getOrientation() {
        return this.imu.getRobotYawPitchRollAngles();
    }

    public AngularVelocity getAngularVelocity() {
        return this.imu.getRobotAngularVelocity(AngleUnit.DEGREES);
    }

    public double getVelocity(double rotationRate) {
        if (-0.2 < rotationRate && rotationRate< 0.2) {
            return 0;
        }
        return rotationRate;
    }

    public double getAngle() {
        return this.currentAngle;
    }

    private double calcAngle(char rotationDirection, long time) {
        // char[] directions = {'Z', 'X', 'Y'};
        int index = directionToInt(rotationDirection); // = Arrays.asList(directions).indexOf(rotationDirection);

        float[] rotationRates = {this.getAngularVelocity().zRotationRate, this.getAngularVelocity().xRotationRate, this.getAngularVelocity().yRotationRate};
        double rotationRate = rotationRates[index];
        imuAngles[index] += getVelocity(rotationRate) * time / 1000;
        if (imuAngles[index] >= 180) {
            imuAngles[index] -= 360;
        } else if (imuAngles[index] < -180) {
            imuAngles[index] += 360;
        }
        return imuAngles[index];
    }

    @Override
    public void update() {
        long timeChange = System.currentTimeMillis() - prev_time;
        prev_time = System.currentTimeMillis();
        this.currentAngle = this.calcAngle('Z', timeChange);
    }

    public boolean isFinished() {
        return false;
    }

    public void emergencyStop() {

    }
}
