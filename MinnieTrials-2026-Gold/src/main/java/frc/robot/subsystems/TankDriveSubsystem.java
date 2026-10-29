package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.Constants.DriveConstants;

import com.ctre.phoenix6.hardware.Pigeon2;

public class TankDriveSubsystem extends SubsystemBase {

    private SparkMax leftDriveMotor = new SparkMax(DriveConstants.kLeftDriveMotorId, MotorType.kBrushless);
    private SparkMax rightDriveMotor = new SparkMax(DriveConstants.kRightDriveMotorId, MotorType.kBrushless);
    private Pigeon2 gyro = new Pigeon2(DriveConstants.kPigeonID);

    public TankDriveSubsystem() {
        SparkMaxConfig config = new SparkMaxConfig();
        config.smartCurrentLimit(35);
        config.idleMode(IdleMode.kBrake);
        leftDriveMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightDriveMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void setMotorPower(double powerLeft, double powerRight) {
        leftDriveMotor.set(powerLeft);
        rightDriveMotor.set(powerRight);
    }
    
}
