package frc.robot.subsystems;

import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.Constants.DriveConstants;

import com.ctre.phoenix6.hardware.Pigeon2;

public class TankDriveSubsystem extends SubsystemBase {

    private SparkMax leftDriveMotor = new SparkMax(DriveConstants.kLeftDriveMotorId, MotorType.kBrushless);
    private SparkMax rightDriveMotor = new SparkMax(DriveConstants.kRightDriveMotorId, MotorType.kBrushless);
    private RelativeEncoder leftEncoder = leftDriveMotor.getEncoder();
    private RelativeEncoder rightEncoder = rightDriveMotor.getEncoder();
    private Pigeon2 gyro = new Pigeon2(DriveConstants.kPigeonID);
    DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(Units.inchesToMeters(DriveConstants.kTrackWidth));
    DifferentialDrivePoseEstimator poseEstimator;

    public TankDriveSubsystem() {
        SparkMaxConfig config = new SparkMaxConfig();
        config.smartCurrentLimit(35);
        config.idleMode(IdleMode.kBrake);
        leftDriveMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightDriveMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        poseEstimator = new DifferentialDrivePoseEstimator(kinematics, getRotation(), Units.inchesToMeters(getLeftDist()), Units.inchesToMeters(getRightDist()), new Pose2d());
    }

    public void setMotorPower(double powerLeft, double powerRight) { // sets power of the drive motors
        leftDriveMotor.set(powerLeft);
        rightDriveMotor.set(powerRight);
    }

    @Override
    public void periodic() { // periodically update the pose
        poseEstimator.update(getRotation(), Units.inchesToMeters(getLeftDist()), Units.inchesToMeters(getRightDist()));
    }

    public Pose2d getPosition() {
        return poseEstimator.getEstimatedPosition();
    }

    public Rotation2d getRotation() {
        return gyro.getRotation2d();
    }

    public double getLeftDist() { // gets left wheel's distance traveled
        return leftEncoder.getPosition() * DriveConstants.kWheelCircumference / DriveConstants.kGearRatio;
    }

    public double getRightDist() { // gets right wheel's distance traveled
        return rightEncoder.getPosition() * DriveConstants.kWheelCircumference / DriveConstants.kGearRatio;
    }

    public double getSpeed() {
        double leftV = leftEncoder.getVelocity();
        double rightV = rightEncoder.getVelocity(); // how fast wheel move yes

        double inchSpeed = (leftV + rightV) / 2 / DriveConstants.kGearRatio * DriveConstants.kWheelCircumference; // calculate speed in inches per minute
        return Units.inchesToMeters(inchSpeed) / 60; // divide by 60 to convert from per minute to per second
    }
}
