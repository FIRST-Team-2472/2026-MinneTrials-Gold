package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {
    private final int armMotorPortId = Constants.IntakeConstants.armMotorPortId;
    private final int armSpeedLimit = Constants.IntakeConstants.armSpeedLimit;

    // Assume that there's 1 arm motor and an encoder* to limit angles
    // *If button is implemented, use `DigitalInput` instead for binding physical button presses from the arm

    public SparkMax armMotor = new SparkMax(armMotorPortId, MotorType.kBrushless);
    public RelativeEncoder armEncoder;
    //public DigitalInput armButton;

    public IntakeSubsystem() {
        SparkMaxConfig armConfig = new SparkMaxConfig();
        armConfig.smartCurrentLimit(armSpeedLimit);
        armConfig.idleMode(IdleMode.kBrake);

        armMotor.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        armEncoder = armMotor.getEncoder();
    }

    // Sets the arm motor speed
    public void setSpeed(double power) {
        armMotor.set(power);
    }

    // Gets the arm motor speed
    public double getSpeed() {
        return armMotor.get();
    }

    // Gets the amount of revolutions from the motor
    public double getArmRotations() {
        return armEncoder.getPosition();
    }
}
