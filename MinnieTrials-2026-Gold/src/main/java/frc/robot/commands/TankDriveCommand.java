package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.TankDriveSubsystem;

public class TankDriveCommand extends Command {
    
    TankDriveSubsystem tankDriveSubsystem;
    Supplier<Double> leftJoystickY;
    Supplier<Double> rightJoystickX;
    int leftMotorPower;
    int rightMotorPower;

    public TankDriveCommand(TankDriveSubsystem tankDriveSubsystem, Supplier<Double> leftJoystickY, Supplier<Double> rightJoystickX) {
        this.tankDriveSubsystem = tankDriveSubsystem;
        this.leftJoystickY = leftJoystickY;
        this.rightJoystickX = rightJoystickX;
        addRequirements(tankDriveSubsystem);
    }
    

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        leftMotorPower = 0;
        rightMotorPower = 0;
        if (Math.abs(rightJoystickX.get()) > DriveConstants.kDeadband) {
            leftMotorPower += rightJoystickX.get();
            rightMotorPower -= rightJoystickX.get();
        }
        leftMotorPower += leftJoystickY.get();
        rightMotorPower += leftJoystickY.get();

        tankDriveSubsystem.setMotorPower(leftMotorPower, rightMotorPower);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
    @Override
    public void end(boolean param) {
        tankDriveSubsystem.setMotorPower(0, 0);
    }
}
