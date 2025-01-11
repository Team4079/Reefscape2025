package frc.robot.utils

import com.pathplanner.lib.commands.PathPlannerAuto
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.WaitCommand
import frc.robot.commands.sequencing.AutomaticScore
import frc.robot.subsystems.CoralManipulator
import frc.robot.subsystems.Elevator
import frc.robot.subsystems.Swerve
import frc.robot.utils.RobotParameters.SwerveParameters

/**
 * The [Kommand] object provides factory methods to create various commands
 * used in the robot's operation.
 */
object Kommand {
    /**
     * Creates an [InstantCommand] to set the state of the elevator.
     *
     * @param state The desired state of the elevator.
     * @return An [InstantCommand] that sets the elevator state.
     */
    @JvmStatic
    fun setElevatorState(state: ElevatorState) = InstantCommand({ Elevator.getInstance().state = state })

    /**
     * Creates an [InstantCommand] to move the elevator to a specific level.
     *
     * @return An [InstantCommand] that moves the elevator to a specific level.
     */
    @JvmStatic
    fun moveElevatorToLevel() = InstantCommand({ Elevator.getInstance().moveElevatorToLevel() })

    /**
     * Creates an [InstantCommand] to start the coral manipulator motors.
     *
     * @return An [InstantCommand] that starts the coral manipulator motors.
     */
    @JvmStatic
    fun startCoralManipulator() = InstantCommand({ CoralManipulator.getInstance().startMotors() })

    /**
     * Creates an [InstantCommand] to stop the coral manipulator motors.
     *
     * @return An [InstantCommand] that stops the coral manipulator motors.
     */
    @JvmStatic
    fun stopCoralManipulator() = InstantCommand({ CoralManipulator.getInstance().stopMotors() })

    /**
     * Creates an [InstantCommand] to score in a specified direction.
     *
     * @param dir The direction in which to score.
     * @return An [InstantCommand] that performs the scoring action.
     */
    @JvmStatic
    fun score(dir: Direction) = InstantCommand({ AutomaticScore(dir) })

    /**
     * Creates an [InstantCommand] to reset the Pidgey sensor.
     *
     * @return An [InstantCommand] that resets the Pidgey sensor.
     */
    @JvmStatic
    fun resetPidgey() = InstantCommand({ Swerve.getInstance().resetPidgey() })

    /**
     * Creates an [InstantCommand] to set the teleoperation PID.
     *
     * @return An [InstantCommand] that sets the teleoperation PID.
     */
    @JvmStatic
    fun setTelePid() = InstantCommand({ Swerve.getInstance().setTelePID() })

    /**
     * Creates a [PathPlannerAuto] command for autonomous operation.
     *
     * @return A [PathPlannerAuto] command for autonomous operation.
     */
    @JvmStatic
    fun autonomousCommand() = PathPlannerAuto(SwerveParameters.PATHPLANNER_AUTO_NAME)

    /**
     * Creates a [WaitCommand] to wait for a specified number of seconds.
     *
     * @param seconds The number of seconds to wait.
     * @return A [WaitCommand] that waits for the specified number of seconds.
     */
    @JvmStatic
    fun waitCmd(seconds: Double) = WaitCommand(seconds)
}