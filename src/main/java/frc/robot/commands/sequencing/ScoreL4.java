package frc.robot.commands.sequencing;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.utils.emu.CoralState;
import frc.robot.utils.emu.Direction;
import frc.robot.utils.emu.ElevatorState;

import static frc.robot.commands.Kommand.*;
import static frc.robot.utils.RobotParameters.ElevatorParameters.elevatorToBeSetState;
import static frc.robot.utils.emu.ElevatorState.DEFAULT;

/**
 * This command group is used to automatically score the coral manipulator. It aligns the swerve
 * drive, moves the elevator to the correct level, starts the coral manipulator, waits for a second,
 * stops the coral manipulator, sets the elevator to the first level, and moves the elevator to the
 * first level.
 */
public class ScoreL4 extends SequentialCommandGroup {
  public ScoreL4() {
    addCommands(
        moveElevatorState(ElevatorState.L4),
        setCoralState(CoralState.CORAL_RELEASE),
        waitCmd(0.3),
        setElevatorState(DEFAULT),
        coralIntaking());
  }
}
