// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.arm.MoveOverride;
import frc.robot.commands.arm.ScoreHigh;
import frc.robot.commands.arm.ScoreLow;
import frc.robot.commands.arm.ScoreMid;
import frc.robot.commands.arm.TrimEncoderPosition;
import frc.robot.commands.swervedrive.auto.AutoBalanceCommand;
import frc.robot.commands.swervedrive.auto.Autos;
import frc.robot.commands.swervedrive.drivebase.AbsoluteDrive;
import frc.robot.commands.swervedrive.drivebase.AbsoluteFieldDrive;
import frc.robot.commands.swervedrive.drivebase.TeleopDrive;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.commands.arm.ScoreHigh;
import frc.robot.commands.arm.ScoreLow;
import frc.robot.commands.arm.ScoreMid;
import frc.robot.commands.intake.Intake;
import frc.robot.commands.arm.LoadIn;
import frc.robot.commands.arm.MoveHigh;
import frc.robot.commands.arm.MoveMid;
import frc.robot.commands.arm.MoveOverride;
import frc.robot.commands.arm.NudgeAuto;

import java.io.File;

import javax.swing.AbstractAction;

import com.pathplanner.lib.PathConstraints;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{

  // The robot's subsystems and commands are defined here...
  public final static SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                                                                         "swerve/neo"));
                                                                         
  public final static ArmSubsystem m_armSubsystem = new ArmSubsystem();
  public final static IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  // CommandJoystick rotationController = new CommandJoystick(1);
  // Replace with CommandPS4Controller or CommandJoystick if needed
  CommandXboxController driverController = new CommandXboxController(0);

  // CommandJoystick driverController   = new CommandJoystick(3);//(OperatorConstants.DRIVER_CONTROLLER_PORT);
  // XboxController driverXbox = new XboxController(1);
  public static CommandXboxController coDriverController = new CommandXboxController(1);
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */

  AbsoluteDrive closedAbsoluteDrive;
  AbsoluteFieldDrive closedFieldAbsoluteDrive;
  TeleopDrive simClosedFieldRel, closedFieldRel, closedRobotRel, closedHeadingDrive;

  public RobotContainer()
  {
    // Configure the trigger bindings



    closedAbsoluteDrive = new AbsoluteDrive(drivebase,
                                                          // Applies deadbands and inverts controls because joysticks
                                                          // are back-right positive while robot
                                                          // controls are front-left positive
                                                          () -> MathUtil.applyDeadband(driverController.getLeftY(),
                                                                                       OperatorConstants.LEFT_Y_DEADBAND),
                                                          () -> MathUtil.applyDeadband(driverController.getLeftX(),
                                                                                       OperatorConstants.LEFT_X_DEADBAND),
                                                          () -> -driverController.getRightX(),
                                                          () -> -driverController.getRightY(),
                                                          false);

    closedFieldAbsoluteDrive = new AbsoluteFieldDrive(drivebase,
                                                                    () ->
                                                                        MathUtil.applyDeadband(driverController.getLeftX(),
                                                                                              OperatorConstants.LEFT_Y_DEADBAND),
                                                                    () -> MathUtil.applyDeadband(driverController.getLeftY(),
                                                                                                OperatorConstants.LEFT_X_DEADBAND),
                                                                    () -> driverController.getRightX(), false);
    simClosedFieldRel = new TeleopDrive(drivebase,
                                                    () -> MathUtil.applyDeadband(driverController.getLeftX(),
                                                                                 OperatorConstants.LEFT_Y_DEADBAND),
                                                    () -> MathUtil.applyDeadband(driverController.getLeftY(),
                                                                                 OperatorConstants.LEFT_X_DEADBAND),
                                                    () -> driverController.getRightX(), () -> true, false, false);
    closedFieldRel = new TeleopDrive(
        drivebase,
        () -> MathUtil.applyDeadband(driverController.getLeftY()*-1, OperatorConstants.LEFT_Y_DEADBAND),
        () -> MathUtil.applyDeadband(driverController.getLeftX()*-1, OperatorConstants.LEFT_X_DEADBAND),
        () -> -driverController.getRightX(), () -> true, false, false);


    closedRobotRel = new TeleopDrive(
        drivebase,
        () -> MathUtil.applyDeadband(driverController.getLeftY()*-1, OperatorConstants.LEFT_Y_DEADBAND),
        () -> MathUtil.applyDeadband(driverController.getLeftX()*-1, OperatorConstants.LEFT_X_DEADBAND),
        () -> -driverController.getRightX(), () -> false, false, false);

    closedHeadingDrive = new TeleopDrive(
        drivebase,
        () -> MathUtil.applyDeadband(driverController.getLeftY()*-1, OperatorConstants.LEFT_Y_DEADBAND),
        () -> MathUtil.applyDeadband(driverController.getLeftX()*-1, OperatorConstants.LEFT_X_DEADBAND),
        () -> -driverController.getRightX(), () -> true,
           false, true);

    //drivebase.setDefaultCommand(!RobotBase.isSimulation() ? closedAbsoluteDrive : closedAbsoluteDrive);
    drivebase.setDefaultCommand(closedFieldRel);
    m_armSubsystem.setDefaultCommand(new MoveMid());

    configureBindings();
  }

  TeleopDrive driveMode;
  public void cycleDriveMode(){
    Command currentCommand = drivebase.getCurrentCommand();
    if (currentCommand.equals(closedFieldRel)) driveMode = closedRobotRel;
    else if (currentCommand.equals(closedRobotRel)) driveMode = closedHeadingDrive;
    else driveMode = closedFieldRel;  
    setDriveMode(driveMode); 
  }

  public void setDriveMode(TeleopDrive driveMode){
    System.out.println("BBB");
    driveMode.schedule();
  }


  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary predicate, or via the
   * named factories in {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight joysticks}.
   */
  private void configureBindings()
  {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`

    driverController.a().onTrue(new InstantCommand(drivebase::zeroGyro));
    driverController.rightTrigger(0.2).whileTrue(new LoadIn());

    driverController.povUp().onTrue(new ScoreHigh());
    driverController.povLeft().onTrue(new ScoreMid());
    driverController.povRight().onTrue(new ScoreMid());
    driverController.povDown().onTrue(new ScoreLow());

    driverController.leftBumper().onTrue(new TrimEncoderPosition(-1));
    driverController.rightBumper().onTrue(new TrimEncoderPosition(1));

    driverController.b().onTrue(new InstantCommand(() -> cycleDriveMode()));

    // new Joysti ckButton(driverController.a()).onTrue((new InstantCommand(drivebase::zeroGyro)));
    // new JoystickButton(coDriverController, 3).onTrue(new InstantCommand(drivebase::addFakeVisionReading));
//    new JoystickButton(driverXbox, 3).whileTrue(new RepeatCommand(new InstantCommand(drivebase::lock, drivebase)));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand(String cube, boolean bump, String end)
  {
    // An example command will be run in autonomous
    // return new NudgeAuto()

    if (cube.equals("kNone")){
      return null;
    }

    if (cube.equals("kOneCube")) //If one cube is selected
      if (bump){ //If robot starts on the side with the black cable protector bump
        if (end.equals("kBalance")) return new ScoreHigh().andThen(new AutoBalanceCommand(drivebase)); //Score cube high, then balance on the near side
        else if (end.equals("kTaxi")) return Autos.FollowPath(drivebase, "One Cube Bump Taxi", new PathConstraints(3, 3), false); //Score cube high, then cross auto line
        else if (end.equals("kPickup")) return Autos.FollowPath(drivebase, "One Cube Bump Pickup", new PathConstraints(3, 3), false); //Score cube high, then pickup seconod cube
        else if (end.equals("kNone")) return new ScoreHigh(); //Don't move and score high
        }

      else if (bump == false){ //If robot starts on the side without the cable bump (Closest to the other teams substation)
        if (end.equals("kBalance")) return new ScoreHigh().andThen(new AutoBalanceCommand(drivebase)); //Score cube high, then balance on the near side
        else if (end.equals("kTaxi")) return Autos.FollowPath(drivebase, "One Cube NoBump Taxi", new PathConstraints(3, 3), false); //Score cube high, then cross auto line
        else if (end.equals("kPickup")) return Autos.FollowPath(drivebase, "One Cube NoBump Pickup", new PathConstraints(3, 3), false); //Score cube high, then pickup seconod cube
        else if (end.equals("kNone")) return new ScoreHigh(); //Don't move and score high
        }

    if (cube.equals("kTwoCube")) //If Two cube is selected
      if (bump){ //If robot starts on the side with the black cable protector bump
        if (end.equals("kBalance")) return Autos.FollowPath(drivebase, "2 Cube Bump Balance", new PathConstraints(3, 3), false).andThen(new AutoBalanceCommand(drivebase)); //Score cubes on the high and mid nodes, then balance on the far side of the charge station 
        else if (end.equals("kTaxi")) return Autos.FollowPath(drivebase, "2 Cube Bump Pickup", new PathConstraints(3, 3), false); //Score cubes on the high and mid nodes, then approach the field center line
        else if (end.equals("kPickup")) return Autos.FollowPath(drivebase, "2 Cube Bump Pickup", new PathConstraints(3, 3), false); //Score cubes on the high and mid nodes, then pickup a 3rd cube
        else if (end.equals("kNone")) return Autos.FollowPath(drivebase, "2 Cube Bump", new PathConstraints(3, 3), false); //Score two cubes on the high and mid nodes, then wait in the community
      }

      else if (bump == false){ //If robot starts on the side without the cable bump (Closest to the other teams substation)
        if (end.equals("kBalance")) return Autos.FollowPath(drivebase, "2 Cube NoBump Balance", new PathConstraints(3, 3), false).andThen(new AutoBalanceCommand(drivebase)); //Score cubes on the high and mid nodes, then balance on the far side of the charge station
        else if (end.equals("kTaxi")) return Autos.FollowPath(drivebase, "2 Cube NoBump Taxi", new PathConstraints(3, 3), false); //Score cubes on the high and mid nodes, then approach the field center line
        else if (end.equals("kPickup")) return Autos.FollowPath(drivebase, "2 Cube NoBump Pickup", new PathConstraints(3, 3), false); //Score cubes on the high and mid nodes, then pickup a 3rd cube
        else if (end.equals("kNone")) return Autos.FollowPath(drivebase, "2 Cube NoBump", new PathConstraints(3, 3), false); //Score two cubes on the high and mid nodes, then wait in the community
      }

    if (cube.equals("kThreeCube")){ //If Three cube is selected
      if (bump){ //If robot starts on the side with the black cable protector bump
        if (end.equals("kBalance")) return Autos.FollowPath(drivebase, "3 Cube Bump Balance", new PathConstraints(3, 3), false).andThen(new AutoBalanceCommand(drivebase));
        else if (end.equals("kTaxi")) return Autos.FollowPath(drivebase, "3 Cube Bump Taxi", new PathConstraints(3, 3), false);
        else if (end.equals("kPickup")) return Autos.FollowPath(drivebase, "3 Cube Bump Pickup", new PathConstraints(3, 3), false);
        else if (end.equals("kNone")) return Autos.FollowPath(drivebase, "3 Cube Bump", new PathConstraints(3, 3), false);
      }


      else if (bump == false){ //If robot starts on the side without the cable bump (Closest to the other teams substation)
        if (end.equals("kBalance")) return Autos.FollowPath(drivebase, "3 Cube NoBump Balance", new PathConstraints(3, 3), false).andThen(new AutoBalanceCommand(drivebase));
        else if (end.equals("kTaxi")) return Autos.FollowPath(drivebase, "3 Cube NoBump Taxi", new PathConstraints(3, 3), false);
        else if (end.equals("kPickup")) return Autos.FollowPath(drivebase, "3 Cube NoBump Pickup", new PathConstraints(3, 3), false);
        else if (end.equals("kNone")) return Autos.FollowPath(drivebase, "3 Cube NoBump", new PathConstraints(3, 3), false);
      }
    }

    return null;

  }

  public void setDriveMode()
  {
    //drivebase.setDefaultCommand();
  }

  public void setMotorBrake(boolean brake)
  {
    drivebase.setMotorBrake(brake);
  }
}
