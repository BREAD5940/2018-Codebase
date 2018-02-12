package frc.team5940.codebase2018.robot;

public interface RobotConfig {

	// REQUIRES UPDATE
	public static final boolean INTAKE_TALONS_REQUIRE_UPDATE = true;

	public static final boolean INTAKE_PNEUMATICS_REQUIRE_UPDATE = true;

	public static final boolean ELEVATOR_TALON_REQUIRE_UPDATE = true;

	public static final boolean DRIVETRAIN_TALONS_REQUIRE_UPDATE = true;

	public static final boolean DRIVETRAIN_SHIFTING_REQUIRE_UPDATE = true;

	// TALON PORTS
	public static final int SLAVE_LEFT_TALON_PORT = 1;

	public static final int MASTER_LEFT_TALON_PORT = 2;

	public static final int SLAVE_RIGHT_TALON_PORT = 3;

	public static final int MASTER_RIGHT_TALON_PORT = 4;

	// TODO
	public static final int MASTER_ELEVATOR_TALON_PORT = 5;

	// TODO
	public static final int SLAVE_ELEVATOR_TALON_PORT = 6;

	// TODO
	public static final int LEFT_INTAKE_TALON_PORT = 7;

	// TODO
	public static final int RIGHT_INTAKE_TALON_PORT = 8;

	// ELEVATOR
	// TODO
	public static final double MAX_ELEVATOR_HEIGHT = 7.75;

	// TODO
	public static final double ELEVATOR_CONTROL_SHAFT_DIAMETER = 7.75;

	// TODO
	public static final int RAISE_ELEVATOR_SLOT_ID = 1;

	// TODO
	public static final int LOWER_ELEVATOR_SLOT_ID = 0;

	// ELEVATOR PID
	// TODO
	public static final double RAISE_ELEVATOR_TALON_P = 0;

	// TODO
	public static final double RAISE_ELEVATOR_TALON_I = 0;

	// TODO
	public static final double RAISE_ELEVATOR_TALON_D = 0;

	// TODO
	public static final double RAISE_ELEVATOR_TALON_F = 0;

	// TODO
	public static final double LOWER_ELEVATOR_TALON_P = 0;

	// TODO
	public static final double LOWER_ELEVATOR_TALON_I = 0;

	// TODO
	public static final double LOWER_ELEVATOR_TALON_D = 0;

	// TODO
	public static final double LOWER_ELEVATOR_TALON_F = 0;

	// INTAKE
	// TODO
	public static final int INTAKE_SOLENOID_FORWARD_CHANNEL = 4;

	// TODO
	public static final int INTAKE_SOLENOID_REVERSE_CHANNEL = 5;

	// DRIVETRAIN
	// TODO
	public static final int DRIVETRAIN_SOLENOID_SHIFTING_FORWARD_CHANNEL = 6;

	// TODO
	public static final int DRIVETRAIN_SOLENOID_SHIFTING_REVERSE_CHANNEL = 7;

	// TODO
	public static final float WHEEL_DIAMETER = 3.5f / 12;

	// TODO
	public static final int LOW_GEAR_VELOCITY_SLOT_ID = 1;

	// TODO
	public static final int HIGH_GEAR_VELOCITY_SLOT_ID = 2;

	// TODO
	public static final int LOW_GEAR_POSITION_SLOT_ID = 3;

	// DRIVETRAIN PID
	// TODO
	public static final float NEW_ROBOT_LOW_GEAR_POSITION_P = 0.5f;

	// TODO
	public static final float NEW_ROBOT_LOW_GEAR_POSITION_I = 0f;

	// TODO
	public static final float NEW_ROBOT_LOW_GEAR_POSITION_D = 20f;

	// TODO
	public static final float NEW_ROBOT_LOW_GEAR_POSITION_F = 0f;

	// TODO
	public static final float NEW_ROBOT_LOW_GEAR_VELOCITY_P = 0.9f;

	// TODO
	public static final float NEW_ROBOT_LOW_GEAR_VELOCITY_I = 0.0025f;

	// TODO
	public static final float NEW_ROBOT_LOW_GEAR_VELOCITY_D = 40f;

	// TODO
	public static final float NEW_ROBOT_HIGH_GEAR_VELOCITY_F = 0.15f;

	// TODO
	public static final float NEW_ROBOT_HIGH_GEAR_VELOCITY_P = 0.9f;

	// TODO
	public static final float NEW_ROBOT_HIGH_GEAR_VELOCITY_I = 0.0025f;

	// TODO
	public static final float NEW_ROBOT_HIGH_GEAR_VELOCITY_D = 40f;

	// TODO
	public static final float NEW_ROBOT_LOW_GEAR_VELOCITY_F = 0.15f;

	// TODO
	public static final float NEW_ROBOT_LOW_GEAR_MAX_VELOCITY = 0.15f;

	// TODO
	public static final float NEW_ROBOT_HIGH_GEAR_MAX_VELOCITY = 0.15f;

	// GENERAL
	// TODO
	public static final double VELOCITY_PULSES_PER_ROTATION = 409.6f;

	// TODO
	public static final double POSITION_PULSES_PER_ROTATION = 4096f;

	// TODO
	public static final int SOLENOID_MODULE_NUMBER = 9;

	// CIABATTA
	// TODO
	public static final float CIABATTA_LOW_GEAR_POSITION_P = 0.5f;

	// TODO
	public static final float CIABATTA_LOW_GEAR_POSITION_I = 0f;

	// TODO
	public static final float CIABATTA_LOW_GEAR_POSITION_D = 20f;

	// TODO
	public static final float CIABATTA_LOW_GEAR_POSITION_F = 0f;

	// TODO
	public static final float CIABATTA_LOW_GEAR_VELOCITY_P = 0.9f;

	// TODO
	public static final float CIABATTA_LOW_GEAR_VELOCITY_I = 0.0025f;

	// TODO
	public static final float CIABATTA_LOW_GEAR_VELOCITY_D = 40f;

	// TODO
	public static final float CIABATTA_LOW_GEAR_VELOCITY_F = 0.15f;
}
