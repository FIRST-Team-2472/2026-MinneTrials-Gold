package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {
    // Changeable values
    // TODO: (probably) implement into `Constants.java`
    private final int armMotorId = 0;
    private final int armSpeedLimit = 30;

    // Assume that there's 1 arm motor and an encoder^ to limit angles
    // TODO: ^If button is implemented, use `DigitalInput` instead for binding physical button presses from the arm

    public SparkMax armMotor = new SparkMax(armMotorId, MotorType.kBrushless);
    public RelativeEncoder armEncoder;
    //public DigitalInput armButton;

    private void setupMotorConfig() {
        SparkMaxConfig armConfig = new SparkMaxConfig();
        armConfig.smartCurrentLimit(armSpeedLimit);
        armConfig.idleMode(IdleMode.kBrake);

        armMotor.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        armEncoder = armMotor.getEncoder();
    }

    public IntakeSubsystem() {
        setupMotorConfig();
    }

    // Sets the arm motor speed
    public void setSpeed(double power) {
        armMotor.set(power);
    }

    // Gets the current arm position (from encoder)
    public double getEncoderPos() {
        return armEncoder.getPosition();
    }
}
