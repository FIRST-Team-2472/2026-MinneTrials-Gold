package frc.robot.subsystems;

import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.Odometry;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkAbsoluteEncoder;
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
    DifferentialDriveOdometry odometry;
    PoseEstimator poseEstimator;

    public TankDriveSubsystem() {
        SparkMaxConfig config = new SparkMaxConfig();
        config.smartCurrentLimit(35);
        config.idleMode(IdleMode.kBrake);
        leftDriveMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightDriveMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        odometry = new DifferentialDriveOdometry(getRotation(), getLeftDist(), getRightDist());
        poseEstimator = new PoseEstimator<>(kinematics, odometry, null, null);
    }

    public void setMotorPower(double powerLeft, double powerRight) {
        leftDriveMotor.set(powerLeft);
        rightDriveMotor.set(powerRight);
    }

    @Override
    public void periodic() {
        odometry.update(getRotation(), leftEncoder.getPosition() * DriveConstants.kWheelCircumference, rightEncoder.getPosition() * DriveConstants.kWheelCircumference);
    }

    public Pose2d getPosition() {
        return poseEstimator.getEstimatedPosition();
    }

    public Rotation2d getRotation() {
        return gyro.getRotation2d();
    }

    public double getLeftDist() {
        return leftEncoder.getPosition() * DriveConstants.kWheelCircumference;
    }

    public double getRightDist() {
        return rightEncoder.getPosition() * DriveConstants.kWheelCircumference;
    }

}
