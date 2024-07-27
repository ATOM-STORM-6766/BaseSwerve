# BaseSwerve </br>

<br>This template includes everything we require for a basic Swerve Drive robot.
<br>
<br><b>Includes:</b>
   * Phoenix Pro Implementation
   * Basic Swerve Code
   * PathPlanner functionality
   * Basic examples for autonomous routines
   * Shuffleboard functionality
   * Various Utilty classes

This is based on Team 364's (dirtbikerxz) [BaseTalonFXSwerve](https://github.com/dirtbikerxz/BaseTalonFXSwerve), though it is heavily modified.

<br><br>**CHANGE TEAM NUMBER**
----
Open the Command Palette (Ctrl+Shift+P) then type ```Set Team Number```.


<br><br>**Setting Constants**
----
The following things must be adjusted to your robot and module's specific constants in the [```Constants.java```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java) file (all distance units must be in meters, and rotation units in radians)</br>
1. Gyro Settings: [```PIGEON_ID```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java) and [```GYRO_INVERT```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java) (ensure that the gyro rotation is CCW+ (Counter Clockwise Positive)
2. [```MODULE_TYPE```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 
<br>Set the module and drive ratio you are using here.
<br>For our uses, it will typically be the SDS MK4/MK4i Module. Make sure to select the correct gear ratio - for us it will most likely be Level 3.
<br>This will automatically set these constants required for the module to function properly:
    * Wheel Circumference
    * Steer Motor Invert
    * Drive Motor Invert
    * CANcoder Sensor Invert
    * Steer Motor Gear Ratio
    * Drive Motor Gear Ratio
    * Steer Falcon Motor PID Values
    
4. [```TRACK_WIDTH```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): Center to Center distance of left and right modules in meters.
5. [```WHEEL_BASE```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): Center to Center distance of front and rear module wheels in meters.
6. [```GEAR_RATIO```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): Total gear ratio for the drive motor. <br><b>This value will be automatically set by the selected module.</b>
7. [```GEAR_RATIO```(Steer)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): Total gear ratio for the steer motor. <br><b>This value will be automatically set by the selected module.</b>
8. [```MOTOR_INVERT```(Steer)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): Must be set such that it is CCW+. <br><b>This value will be automatically set by the selected module, but checking is recommended.</b>
9. [```MOTOR_INVERT```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): This can always remain false, since you set your offsets in step #11 such that a positive input to the drive motor will cause the robot to drive forwards. <br><b>This value will be automatically set by the selected module.</b>

10. [```ModuleConstants```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): set the Can Id's of the motors and CANcoders for the respective modules, see the next step for setting offsets.
11. Setting Offsets
    * Open Phoenix Tuner X
    * Select the CANCoder you want to offset
    * Click the "Zero CANcoder" button
    * Go to the config tab and copy the value in the "Magnet Offset" config. (If it shows 0, refresh using "Refresh/Reload Configs")
    * Paste the value in the ```angleOffset``` parameter of the corresponding module constants.
    <br> <b> Note: The offset value from Phoenix Tuner X is in rotations. You must use ```Rotation2d.fromRotations(value_here)```.

12. Angle Motor PID Values: <br><b>This value will be automatically set through the selected module. If you prefer it to be more or less aggressive, see instructions below</b> 
    * To tune start with a low P value (0.01).
    * Multiply by 10 until the module starts oscilating around the set point
    * Scale back by searching for the value (for example, if it starts oscillating at a P of 10, then try (10 -> 5 -> 7.5 -> etc)) until the module doesn't oscillate around the setpoint.
    * If there is any overshoot you can add in some D by repeating the same process, leave at 0 if not. Always leave I at 0.

14. [```MAX_SPEED```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): In Meters Per Second. [```MAX_ANGULAR_VELOCITY```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): In Radians Per Second. For these you can use the theoretical values, but it is better to physically drive the robot and find the actual max values.

15. [```KS```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java), [```KV```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java), and [```KA```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java)
<br>Leave these as the default values. If for some reason they require a change, you can use the WPILib characterization tool, found [here](https://docs.wpilib.org/en/stable/docs/software/wpilib-tools/robot-characterization/introduction.html). You will need to lock the modules straight forward, and complete the characterization as if it was a standard tank drive.
17. [```KP```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 
<br>After inserting the KS, KV, and KA values into the code, tune the drive motor kP until it doesn't overshoot or oscillate around a target velocity.
<br>Leave [```KI```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java), [```KD```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java), and [```KF```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java) at 0.0.


<br><br>**Controller Mappings**
----
The code is natively setup to use a Xbox controller, though other controllers will work. </br>
<br><b>Note: To add additional button bindings, create methods in [```Controlboard.java```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/controlboard/Controlboard.java) and reference them in [```RobotContainer.java```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/RobotContainer.java).
See [```configButtonBindings```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/RobotContainer.java).</b>
* Left Stick: Translation Control (forwards and sideways movement)
* Right Stick: Rotation Control </br>
* Back Button: Zero Gyro (useful if the gyro drifts mid match, just rotate the robot forwards, and press Back to rezero)
* Start Button: Toggles field-centric mode

<br><br>**Shuffleboard Configuration**
----
<br>The following relates to using tabs and entries with Shuffleboard.
<br>Refer to the [WPILib Wiki](https://docs.wpilib.org/en/stable/docs/software/dashboards/shuffleboard/index.html) for additional help.

<br><b>Creating tabs and entries</b>
* Create a class extending ShuffleboardTabBase and structure it as follows:
   * GenericEntry objects for each of the values you want to display/get with Shuffleboard.
   * Initialize the tab and use create methods from [```ShuffleboardTabBase.java```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/shuffleboard/ShuffleboardTabBase.java) to create entries with the respective data types in [```createEntries()```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/shuffleboard/ShuffleboardTabBase.java)
   * Either set or get the entry's value in [```periodic()```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/shuffleboard/ShuffleboardTabManager.java)
   * See [```SwerveTab.java```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/shuffleboard/tabs/SwerveTab.java) for an example of this structure.
 
<br><b>Putting tabs on Shuffleboard</b>
* Add tabs to the list in [```ShuffleboardTabManager.java```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/shuffleboard/ShuffleboardTabManager.java), either as a debug tab or regular tab.
   * Debug tabs will only be shown if [```includeDebug```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/shuffleboard/ShuffleboardTabManager.java) is true
      * Displaying to Shuffleboard is resource-intensive, so make sure it is true only when you are debugging/developing code.

<br><b>Using the Shuffleboard application</b>
* Shuffleboard should be automatically installed, but you may have to select it in DriverStation.
* All tabs and corresponding entries should appear with the values/names they are set with, assuming you have deployed the code.
   * If they do not appear, simply restart the robot code and re-open Shuffleboard.


<br><br>**设置常量**
----
以下内容必须根据您的机器人和模块的具体常量在[```Constants.java```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java)文件中进行调整（所有距离单位必须为米，旋转单位为弧度）：</br>
1. 陀螺仪设置：[```PIGEON_ID```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java) 和 [```GYRO_INVERT```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java)（确保陀螺仪旋转为逆时针+）
2. [```MODULE_TYPE```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 
<br>在此设置您使用的模块和驱动比率。
<br>对我们的使用而言，通常是 SDS MK4/MK4i 模块。确保选择正确的齿轮比 - 对我们来说，很可能是3级。
<br>这将自动设置模块正常运行所需的以下常量：
    * 车轮周长
    * 转向电机反转
    * 驱动电机反转
    * CANcoder 传感器反转
    * 转向电机齿轮比
    * 驱动电机齿轮比
    * 转向 Falcon 电机 PID 值
    
4. [```TRACK_WIDTH```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 左右模块中心到中心的距离，以米为单位。
5. [```WHEEL_BASE```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 前后模块车轮的中心到中心的距离，以米为单位。
6. [```GEAR_RATIO```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 驱动电机的总齿轮比。<br><b>此值将由所选模块自动设置。</b>
7. [```GEAR_RATIO```(Steer)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 转向电机的总齿轮比。<br><b>此值将由所选模块自动设置。</b>
8. [```MOTOR_INVERT```(Steer)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 必须设置为逆时针+。<br><b>此值将由所选模块自动设置，但建议检查。</b>
9. [```MOTOR_INVERT```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 由于在步骤#11中设置了偏移量，因此始终可以保持为false，这样驱动电机的正输入会导致机器人向前行驶。<br><b>此值将由所选模块自动设置。</b>

10. [```ModuleConstants```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 设置各个模块的电机和CANcoder的Can Id，参见下一步以设置偏移量。
11. 设置偏移量
    * 打开 Phoenix Tuner X
    * 选择要偏移的 CANCoder
    * 点击“Zero CANcoder”按钮
    * 转到配置选项卡并复制“Magnet Offset”配置中的值。（如果显示0，请使用“Refresh/Reload Configs”刷新）
    * 将值粘贴到相应模块常量的 ```angleOffset``` 参数中。
    <br> <b> 注意：Phoenix Tuner X 中的偏移值以旋转为单位。必须使用 ```Rotation2d.fromRotations(value_here)```。

12. 角度电机 PID 值：<br><b>此值将通过所选模块自动设置。如果您希望其更或更不激进，请参见以下说明</b> 
    * 调整时从较低的P值（0.01）开始。
    * 乘以10，直到模块开始围绕设定点振荡
    * 缩小搜索值（例如，如果在P值为10时开始振荡，则尝试（10 -> 5 -> 7.5 -> 等等）），直到模块不再围绕设定点振荡。
    * 如果有任何超调，可以通过重复相同过程添加一些D值，如果没有则保持为0。始终将I保持为0。

14. [```MAX_SPEED```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 每秒米数。[```MAX_ANGULAR_VELOCITY```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 每秒弧度数。可以使用理论值，但最好物理驾驶机器人以找到实际最大值。

15. [```KS```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java), [```KV```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java), 和 [```KA```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java)
<br>将这些保持为默认值。如果出于某种原因需要更改，可以使用 WPILib 表征工具，见[这里](https://docs.wpilib.org/en/stable/docs/software/wpilib-tools/robot-characterization/introduction.html)。需要将模块直锁，完成表征如同标准坦克驱动。
17. [```KP```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java): 
<br>将 KS、KV 和 KA 值插入代码后，调整驱动电机的 kP 值，直到它不会超调或围绕目标速度振荡。
<br>将 [```KI```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java), [```KD```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java), 和 [```KF```(Drive)](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/Constants.java) 保持为0.0。


<br><br>**控制器映射**
----
代码本身设置为使用Xbox控制器，尽管其他控制器也可以使用。 </br>
<br><b>注意：要添加其他按钮绑定，请在[```Controlboard.java```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/controlboard/Controlboard.java)中创建方法，并在[```RobotContainer.java```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/RobotContainer.java)中引用它们。参见[```configButtonBindings```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/RobotContainer.java)。</b>
* 左摇杆：平移控制（前后和侧向移动）
* 右摇杆：旋转控制 </br>
* 后退按钮：零陀螺仪（如果比赛中陀螺仪漂移，旋转机器人向前，然后按下后退按钮以重新归零）
* 开始按钮：切换字段中心模式

<br><br>**Shuffleboard 配置**
----
<br>以下内容涉及使用 Shuffleboard 的标签和条目。
<br>如需更多帮助，请参阅 [WPILib Wiki](https://docs.wpilib.org/en/stable/docs/software/dashboards/shuffleboard/index.html)。

<br><b>创建标签和条目</b>
* 创建一个继承 ShuffleboardTabBase 的类，并按如下结构：
   * 每个要显示/获取的值的 GenericEntry 对象。
   * 初始化标签并使用 [```ShuffleboardTabBase.java```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/shuffleboard/ShuffleboardTabBase.java) 中的 create 方法在 [```createEntries()```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/shuffleboard/ShuffleboardTabBase.java) 中使用相应的数据类型创建条目
   * 在 [```periodic()```](https://github.com/TeamSCREAM4522/Base

Swerve/blob/main/src/main/java/frc/robot/shuffleboard/ShuffleboardTabManager.java) 中设置或获取条目的值
   * 参见 [```SwerveTab.java```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/shuffleboard/tabs/SwerveTab.java) 以了解此结构的示例。
 
<br><b>在 Shuffleboard 上放置标签</b>
* 在 [```ShuffleboardTabManager.java```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/shuffleboard/ShuffleboardTabManager.java) 的列表中添加标签，作为调试标签或常规标签。
   * 调试标签只有在 [```includeDebug```](https://github.com/TeamSCREAM4522/BaseSwerve/blob/main/src/main/java/frc/robot/shuffleboard/ShuffleboardTabManager.java) 为 true 时才会显示
      * 显示到 Shuffleboard 是资源密集型的，因此仅在调试/开发代码时才将其设置为 true。

<br><b>使用 Shuffleboard 应用程序</b>
* Shuffleboard 应该会自动安装，但您可能需要在 DriverStation 中选择它。
* 假设您已部署代码，所有标签及相应条目应以其设置的值/名称出现。
   * 如果没有出现，只需重新启动机器人代码并重新打开 Shuffleboard。