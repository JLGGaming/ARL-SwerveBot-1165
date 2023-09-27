// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new IntakeSubsystem. */

  CANSparkMax intakeLeft = new CANSparkMax(MotorConstants.kIntakeLeft, MotorType.kBrushless);
  CANSparkMax intakeRight = new CANSparkMax(MotorConstants.kIntakeRight, MotorType.kBrushless);

  public IntakeSubsystem() {
    configMotors();
  }

  public void configMotors(){
    // intakeLeft.restoreFactoryDefaults();
    // intakeRight.restoreFactoryDefaults();

    intakeRight.setIdleMode(IdleMode.kBrake);
    intakeLeft.setIdleMode(IdleMode.kBrake);

    intakeRight.setInverted(true);
    intakeLeft.setInverted(false);

    intakeLeft.setSmartCurrentLimit(MotorConstants.kIntakeSmartCurrent);
    intakeRight.setSmartCurrentLimit(MotorConstants.kIntakeSmartCurrent);

    intakeLeft.setClosedLoopRampRate(0);
    intakeRight.setClosedLoopRampRate(0);

    // intakeRight.burnFlash();
    // intakeLeft.burnFlash();
  }

  public void Intake(double speed){
    intakeLeft.set(-speed*.9);
    intakeRight.set(-speed);
  }
  
  public void Outtake(double speed){
    intakeLeft.set(speed*1);
    intakeRight.set(speed*0.8);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
