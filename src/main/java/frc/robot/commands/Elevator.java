// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import frc.robot.Constants;
import frc.robot.subsystems.ElevatorSubsystem;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;

/** An example command that uses an example subsystem. */
public class Elevator extends Command {

  private final ElevatorSubsystem m_subsystem;
  Supplier<Double> joystickY;
  double height;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public Elevator(ElevatorSubsystem subsystem, Supplier<Double> joystickY) {
    m_subsystem = subsystem;
    this.joystickY = joystickY;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    height = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (height < Constants.ElevatorConstants.kMaxHeight && joystickY.get() > 0) {
      double speed = (Math.min(Math.max(joystickY.get(),-.5), .5));
      m_subsystem.setSpeed(speed);
      height += speed;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.setSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
