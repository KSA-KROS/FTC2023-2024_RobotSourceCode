// Linear Test Code

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.time.Duration;
import java.time.Instant;

@TeleOp(name = "IMU_TestOp", group = "")
public class IMU_TestOpMode extends OpMode {
    IMU imu;
    RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
    RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

    public double currentAngle = 0;
    public double start = 0;
    public double end = 0;

    @Override
    public void init() {
        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        imu.initialize(new IMU.Parameters(orientationOnRobot));
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        start = System.currentTimeMillis();
        double timeChange = end - start;
        telemetry.addData("Hub orientation", "Logo=%s   USB=%s\n ", logoDirection, usbDirection);

        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        AngularVelocity angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES);

        telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)", orientation.getYaw(AngleUnit.DEGREES));
        telemetry.addData("Pitch (X)", "%.2f Deg.", orientation.getPitch(AngleUnit.DEGREES));
        telemetry.addData("Roll (Y)", "%.2f Deg.\n", orientation.getRoll(AngleUnit.DEGREES));

        telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)\n", getAngle(angularVelocity.zRotationRate, timeChange));
        //telemetry.addData("Pitch (X)", "%.2f Deg.", getAngle(angularVelocity.xRotationRate, timeChange));
        //telemetry.addData("Roll (Y)", "%.2f Deg.\n", getAngle(angularVelocity.yRotationRate, timeChange));
        telemetry.addData("Yaw (Z) velocity", "%.2f Deg/Sec", angularVelocity.zRotationRate);
        telemetry.addData("Pitch (X) velocity", "%.2f Deg/Sec", angularVelocity.xRotationRate);
        telemetry.addData("Roll (Y) velocity", "%.2f Deg/Sec\n", angularVelocity.yRotationRate);
        telemetry.addData("Yaw (Z) velocity", "%.2f Deg/Sec", getVelocity(angularVelocity.zRotationRate));
        telemetry.addData("Pitch (X) velocity", "%.2f Deg/Sec", getVelocity(angularVelocity.xRotationRate));
        telemetry.addData("Roll (Y) velocity", "%.2f Deg/Sec", getVelocity(angularVelocity.yRotationRate));
        telemetry.update();
        end = System.currentTimeMillis();
    }

    public double getVelocity(double rotationRate) {
        if (-0.2 < rotationRate && rotationRate< 0.2) {
            return 0;
        }
        return rotationRate;
    }

    public double getAngle(double rotationRate, double time) {
        // time is in milliseconds
        currentAngle += getVelocity(rotationRate) * time / 100;
        return currentAngle;
    }
}
