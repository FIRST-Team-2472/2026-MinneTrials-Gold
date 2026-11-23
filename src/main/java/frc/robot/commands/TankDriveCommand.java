package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.MotorPowerController;
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

        motorPowerController = new MotorPowerController(0, 0, 0, 0, 0, 0, 0); //TODO: tune PID aaaah scary
        limiter = new SlewRateLimiter(DriveConstants.kMaxAcceleration);
    }
    

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        double drivePower = 0;
        double turnPower = 0;
        
        //set motor power based on joysticks
        if (Math.abs(leftJoystickY.get()) > OperatorConstants.kDeadband) {
            drivePower = leftJoystickY.get();
        }
        if (Math.abs(rightJoystickX.get()) > OperatorConstants.kDeadband) {
            turnPower = rightJoystickX.get();
        }

        if (drivePower == 0) { //if robot isn't moving don't set speed based on motor power controller
            motorPowerController.calculate(drivePower, -tankDriveSubsystem.getSpeed());
        } else { //if robot is moving, use motor power controller based on joystick values as set point
            drivePower = motorPowerController.calculate(drivePower, -tankDriveSubsystem.getSpeed());
        }

        drivePower = limiter.calculate(drivePower); // limit acceleration
        
        // factor in the turn power to create turning
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
        tankDriveSubsystem.setMotorPower(0, 0); // stop moving when the command ends (robot is disabled)
    }
}
