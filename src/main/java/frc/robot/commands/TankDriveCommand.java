package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.AccelLimiter;
import frc.robot.MotorPowerController;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.TankDriveSubsystem;

public class TankDriveCommand extends Command {
    
    TankDriveSubsystem tankDriveSubsystem;
    Supplier<Double> leftJoystickY;
    Supplier<Double> rightJoystickX;

    MotorPowerController motorPowerController;
    SlewRateLimiter limiter;

    public TankDriveCommand(TankDriveSubsystem tankDriveSubsystem, Supplier<Double> leftJoystickY, Supplier<Double> rightJoystickX) {
        this.tankDriveSubsystem = tankDriveSubsystem;
        this.leftJoystickY = leftJoystickY;
        this.rightJoystickX = rightJoystickX;
        addRequirements(tankDriveSubsystem);

        motorPowerController = new MotorPowerController(0, 0, 0, 0, 0, 0, 0); //TODO: set these values
        limiter = new SlewRateLimiter(DriveConstants.kMaxSpeed);
    }
    

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        double drivePower = 0;
        double turnPower = 0;
        
        if (Math.abs(leftJoystickY.get()) > OperatorConstants.kDeadband) {
            drivePower = leftJoystickY.get();
        }
        if (Math.abs(rightJoystickX.get()) > OperatorConstants.kDeadband) {
            turnPower = rightJoystickX.get();
        }

        if (drivePower == 0) {
            motorPowerController.calculate(drivePower, -tankDriveSubsystem.getSpeed());
        } else {
            drivePower = motorPowerController.calculate(drivePower, -tankDriveSubsystem.getSpeed());
        }
        turnPower = motorPowerController.
        
        double leftPower = drivePower - turnPower;
        double rightPower = drivePower + turnPower;
        
        tankDriveSubsystem.setMotorPower(leftPower, rightPower);
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
