// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.arm;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.ArmPIDConstants;

public class MoveLow extends CommandBase {
  
  
  public PIDController armController;

  
  /** Creates a new MoveLow. */
  public MoveLow() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.m_armSubsystem);
    armController = new PIDController(ArmPIDConstants.kP, ArmPIDConstants.kI, ArmPIDConstants.kD);
    armController.setSetpoint(ArmPIDConstants.kSetpointLow);
    armController.setTolerance(0);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    armController.reset();
    System.out.println("Moving Arm Low");

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double speed = MathUtil.clamp(armController.calculate(RobotContainer.m_armSubsystem.getArmPosition()), ArmPIDConstants.kSpeedMin, ArmPIDConstants.kSpeedMax);
    RobotContainer.m_armSubsystem.setArmSpeed(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.m_armSubsystem.setArmSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return armController.atSetpoint();
  }
}
