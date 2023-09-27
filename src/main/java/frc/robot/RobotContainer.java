// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
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
  public RobotContainer()
  {
    // Configure the trigger bindings



    AbsoluteDrive closedAbsoluteDrive = new AbsoluteDrive(drivebase,
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

    AbsoluteFieldDrive closedFieldAbsoluteDrive = new AbsoluteFieldDrive(drivebase,
                                                                         () ->
                                                                             MathUtil.applyDeadband(driverController.getLeftX(),
                                                                                                    OperatorConstants.LEFT_Y_DEADBAND),
                                                                         () -> MathUtil.applyDeadband(driverController.getLeftY(),
                                                                                                      OperatorConstants.LEFT_X_DEADBAND),
                                                                         () -> driverController.getRightX(), false);
    TeleopDrive simClosedFieldRel = new TeleopDrive(drivebase,
                                                    () -> MathUtil.applyDeadband(driverController.getLeftX(),
                                                                                 OperatorConstants.LEFT_Y_DEADBAND),
                                                    () -> MathUtil.applyDeadband(driverController.getLeftY(),
                                                                                 OperatorConstants.LEFT_X_DEADBAND),
                                                    () -> driverController.getRightX(), () -> true, false, false);
    TeleopDrive closedFieldRel = new TeleopDrive(
        drivebase,
        () -> MathUtil.applyDeadband(driverController.getLeftY()*-1, OperatorConstants.LEFT_Y_DEADBAND),
        () -> MathUtil.applyDeadband(driverController.getLeftX()*-1, OperatorConstants.LEFT_X_DEADBAND),
        () -> -driverController.getRightX(), () -> true, false, false);


    TeleopDrive closedRobotRel = new TeleopDrive(
        drivebase,
        () -> MathUtil.applyDeadband(driverController.getLeftY()*-1, OperatorConstants.LEFT_Y_DEADBAND),
        () -> MathUtil.applyDeadband(driverController.getLeftX()*-1, OperatorConstants.LEFT_X_DEADBAND),
        () -> -driverController.getRightX(), () -> false, false, false);

    TeleopDrive headingDrive = new TeleopDrive(
          drivebase,
          () -> MathUtil.applyDeadband(driverController.getLeftY()*-1, OperatorConstants.LEFT_Y_DEADBAND),
          () -> MathUtil.applyDeadband(driverController.getLeftX()*-1, OperatorConstants.LEFT_X_DEADBAND),
          () -> -driverController.getRightX(), () -> true,
           false, true);

    //drivebase.setDefaultCommand(!RobotBase.isSimulation() ? closedAbsoluteDrive : closedAbsoluteDrive);
    
    drivebase.setDefaultCommand(headingDrive);
    m_armSubsystem.setDefaultCommand(new MoveMid());

    configureBindings();


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




    // new Joysti ckButton(driverController.a()).onTrue((new InstantCommand(drivebase::zeroGyro)));
    // new JoystickButton(coDriverController, 3).onTrue(new InstantCommand(drivebase::addFakeVisionReading));
//    new JoystickButton(driverXbox, 3).whileTrue(new RepeatCommand(new InstantCommand(drivebase::lock, drivebase)));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand()
  {
    // An example command will be run in autonomous
    return new NudgeAuto().andThen(new ScoreHigh());
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
