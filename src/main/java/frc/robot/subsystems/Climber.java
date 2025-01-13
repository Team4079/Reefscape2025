package frc.robot.subsystems;

import static frc.robot.utils.Dash.*;

import com.ctre.phoenix6.configs.ClosedLoopRampsConfigs;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.RobotParameters.*;

/**
 * The PivotSubsystem class is a subsystem that interfaces with the pivot system to provide control
 * over the pivot motors. This subsystem is a Singleton, meaning that only one instance of this
 * class is created and shared across the entire robot code.
 */
public class Climber extends SubsystemBase {
  /** Creates a new Pivot. */
  private TalonFX pivotMotor;

  // Configurations
  private TalonFXConfiguration pivotMotorConfiguration;
  private Slot0Configs pivotConfigs;
  private MotorOutputConfigs pivotOutputConfigs;
  private CurrentLimitsConfigs pivotMotorCurrentConfig;
  private ClosedLoopRampsConfigs pivotMotorRampConfig;
  private SoftwareLimitSwitchConfigs pivotMotorSoftLimitConfig;

  private PositionVoltage pos_reqest;
  private VelocityVoltage vel_voltage;

  private VoltageOut voltageOut;

  private double deadband = 0.001;

  // private double absPos = 0;

  /**
   * The Singleton instance of this PivotSubsystem. Code should use the {@link #getInstance()}
   * method to get the single instance (rather than trying to construct an instance of this class.)
   */
  private static final Climber INSTANCE = new Climber();

  /**
   * Returns the Singleton instance of this PivotSubsystem. This static method should be used,
   * rather than the constructor, to get the single instance of this class. For example: {@code
   * PivotSubsystem.getInstance();}
   */
  @SuppressWarnings("WeakerAccess")
  public static Climber getInstance() {
    return INSTANCE;
  }

  /**
   * Creates a new instance of this PivotSubsystem. This constructor is private since this class is
   * a Singleton. Code should use the {@link #getInstance()} method to get the singleton instance.
   */
  private Climber() {
    pivotMotor = new TalonFX(MotorParameters.PIVOT_MOTOR_ID);

    pivotOutputConfigs = new MotorOutputConfigs();

    pivotConfigs = new Slot0Configs();

    pivotMotorConfiguration = new TalonFXConfiguration();

    pivotMotor.getConfigurator().apply(pivotMotorConfiguration);
    pivotMotor.getConfigurator().apply(pivotConfigs);

    pivotOutputConfigs.NeutralMode = NeutralModeValue.Brake;

    pivotConfigs.kP = PivotParameters.PIVOT_PID_P;
    pivotConfigs.kI = PivotParameters.PIVOT_PID_I;
    pivotConfigs.kD = PivotParameters.PIVOT_PID_D;
    pivotConfigs.kV = PivotParameters.PIVOT_PID_V;
    // pivotConfigs.kF = PivotConstants.PIVOT_PID_F;

    pivotMotor.getConfigurator().apply(pivotConfigs);

    pivotMotorCurrentConfig = new CurrentLimitsConfigs();
    pivotMotorRampConfig = new ClosedLoopRampsConfigs();
    pivotMotorSoftLimitConfig = new SoftwareLimitSwitchConfigs();

    pivotMotorCurrentConfig.SupplyCurrentLimit = 100;
    pivotMotorCurrentConfig.SupplyCurrentLimitEnable = true;
    pivotMotorCurrentConfig.StatorCurrentLimit = 100;
    pivotMotorCurrentConfig.StatorCurrentLimitEnable = true;

    pivotMotor.getConfigurator().apply(pivotMotorCurrentConfig);

    pivotMotorRampConfig.DutyCycleClosedLoopRampPeriod = 0.1;

    pivotMotor.getConfigurator().apply(pivotMotorRampConfig);

    pivotMotorSoftLimitConfig.ForwardSoftLimitEnable = true;
    pivotMotorSoftLimitConfig.ReverseSoftLimitEnable = true;
    pivotMotorSoftLimitConfig.ForwardSoftLimitThreshold = 40;
    pivotMotorSoftLimitConfig.ReverseSoftLimitThreshold = 0.2;

    pivotMotorConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    pivotMotorConfiguration.SoftwareLimitSwitch = pivotMotorSoftLimitConfig;

    pivotMotor.getConfigurator().apply(pivotMotorSoftLimitConfig);

    // absoluteEncoder = new DigitalInput(9);

    vel_voltage = new VelocityVoltage(0);
    pos_reqest = new PositionVoltage(0);
    voltageOut = new VoltageOut(0);
    new PositionDutyCycle(0);

    pivotMotor.setPosition(0);
  }

  // This method will be called once per scheduler run
  @Override
  public void periodic() {
    log("Pivot Motor Position", pivotMotor.getPosition().getValueAsDouble());
  }

  /** Stops the pivot motor */
  public void stopMotor() {
    pivotMotor.stopMotor();
    voltageOut.Output = -0.014;
    pivotMotor.setControl(voltageOut);
  }

  /**
   * Set the position of the left and right pivot motors
   *
   * @param motorPos Motor position
   */
  public void setMotorPosition(double motorPos) {
    pivotMotor.setControl(pos_reqest.withPosition(motorPos));
  }

  /**
   * Get the position of the elevator motor
   *
   * @return double, the position of the elevator motor
   */
  public double getPivotPosValue() {
    return pivotMotor.getPosition().getValue().magnitude();
  }

  /**
   * Run distance through a best fit line and return the value
   *
   * @param distance The distance
   * @return double, the position of the pivot motor
   */
  public double shootPos(double distance) {
    // Calculates the shoot position based on the distance and line of best fit
    double y = 0.0;
    return y;
  }

  /** Soft resets the encoders on the elevator motors */
  public void resetEncoders() {
    pivotMotor.setPosition(0);
  }

  /** Toggles the soft stop for the elevator motor */
  public void toggleSoftStop() {
    PivotParameters.SOFT_LIMIT_ENABLED = !PivotParameters.SOFT_LIMIT_ENABLED;
    pivotMotorSoftLimitConfig.ReverseSoftLimitEnable = PivotParameters.SOFT_LIMIT_ENABLED;
    // leftSoftLimitConfig.ForwardSoftLimitThreshold = 1100;
    pivotMotorSoftLimitConfig.ReverseSoftLimitThreshold = 0;

    pivotMotor.getConfigurator().apply(pivotMotorSoftLimitConfig);
  }

  /**
   * Move the pivot motor based on the velocity
   *
   * @param velocity double, The velocity to move the pivot motor
   */
  public void movePivot(double velocity) {
    if (Math.abs(velocity) >= deadband) {
      pivotMotor.setControl(vel_voltage.withVelocity(velocity * 500 * 0.75));
    } else {
      this.stopMotor();
    }
  }

  /**
   * Set the pivot motor to a specific position
   *
   * @param pos double, The position to set the pivot motor to
   */
  public void setPivot(double pos) {
    pivotMotor.setControl(vel_voltage.withVelocity(pos));
  }

  // public void recalibrateEncoders() {
  //   PivotGlobalValues.offset = PivotGlobalValues.PIVOT_NEUTRAL_ANGLE - getAbsoluteEncoder();
  // }
}