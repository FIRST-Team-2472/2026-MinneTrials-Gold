package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeSubsystem;

public class ClawCommand extends Command {
    private final int maxClawAngle = Constants.IntakeConstants.maxArmAngle;

    private IntakeSubsystem intakeSubsystem;
    private Supplier<Boolean> openClawButton;

    public ClawCommand(IntakeSubsystem intakeSubsystem, Supplier<Boolean> openClawButton) {
        this.intakeSubsystem = intakeSubsystem;
        this.openClawButton = openClawButton;
        addRequirements(intakeSubsystem);
    }

    // Gets the current angle based on amount of revolutions
    private double getAngle(double rev) {
        // Assume if `pos` (or revolutions) is 1,
        // then the angle would be 360 degrees;
        // 0.5 revolutions would be 180 degrees...
        return Math.toDegrees(rev * Math.PI * 2);
    }

    @Override
    public void initialize() {
        intakeSubsystem.setSpeed(0);
    }

    @Override
    public void execute() {
        // Arm movement handler
        // If "openClawButton" is held, the arm opens; otherwise it closes
        if (openClawButton.get()) {
            intakeSubsystem.setSpeed(1);
        } else {
            intakeSubsystem.setSpeed(-1);
        }

        // Arm limiter
        // Limits arm from exceeding angles (by encoder)
        double encoderPos = intakeSubsystem.getArmRotations();
        double clawAngle = getAngle(encoderPos);
        double armSpeed = intakeSubsystem.getSpeed();
        // Testing if angles are right
        SmartDashboard.putNumber("Arm Angle (test)", clawAngle);

        // I'll put down the conditions as variables for readability

        // The claw motor stops if it exceeds certain angles
        // and ONLY if the motor direction attempts to exceed them
        boolean clawExceedsClosedAngle = (armSpeed < 0 && clawAngle < 0);
        boolean clawExceedsOpenAngle = (armSpeed > 0 && clawAngle > maxClawAngle);
        if (clawExceedsClosedAngle || clawExceedsOpenAngle) {
            intakeSubsystem.setSpeed(0);
        }
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.setSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
