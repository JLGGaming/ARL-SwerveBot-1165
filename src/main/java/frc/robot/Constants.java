// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import swervelib.math.Matter;
import swervelib.parser.PIDFConfig;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean constants. This
 * class should not be used for any other purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants
{

  public static final double ROBOT_MASS = (148 - 20.3) * 0.453592; // 32lbs * kg per pound
  public static final Matter CHASSIS    = new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), ROBOT_MASS);
  public static final double LOOP_TIME  = 0.13; //s, 20ms + 110ms sprk max velocity lag

  public static final class Auton
  {

    public static final PIDFConfig xAutoPID     = new PIDFConfig(0.7, 0, 0);
    public static final PIDFConfig yAutoPID     = new PIDFConfig(0.7, 0, 0);
    public static final PIDFConfig angleAutoPID = new PIDFConfig(0.4, 0, 0.01);

    public static final double MAX_SPEED        = 4;
    public static final double MAX_ACCELERATION = 2;
  }

  public static final class Drivebase
  {

    // Hold time on motor brakes when disabled
    public static final double WHEEL_LOCK_TIME = 10; // seconds
  }

  public static final class MotorConstants {
    public static final int kArmPort = 17;
    public static final int kIntakeRight = 15;
    public static final int kIntakeLeft = 16;
    public static final int kArmSmartCurrent = 40;
    public static final int kIntakeSmartCurrent = 40;
  }

  public static class ArmPIDConstants {
    public static final int kZeroPosition = 0;

    public static final int kSetpointAutoZero = -36;
    public static final int kSetpointGround = 4;
    public static final int kSetpointLow = 50;
    public static final int kSetpointMid = 50;
    public static final int kSetpointHigh = 50;

    public static final double kP = 0.02;
    public static final double kI = 0;
    public static final double kD = 0;

    public static final double kTolerance = 0;

    public static final double kSpeedMax = 0.4;
    public static final double kSpeedMin = -0.4;

    public static final double kVelocityMax = 0;
    public static final double kVelocityMin = 0;
  }

  public static class IntakeConstants {
    public static final double kHighSpeed = 0.4;
    public static final double kMidSpeed = 0.3;
    public static final double kLowSpeed = 0.5;

    public static final double kIntakeSpeed = 0.25;
  }

  public static class OperatorConstants
  {

    // Joystick Deadband
    public static final double LEFT_X_DEADBAND = 0.01;
    public static final double LEFT_Y_DEADBAND = 0.01;
  }
}
