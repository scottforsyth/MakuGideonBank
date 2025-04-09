@echo off
echo Banking Application Runner
echo ========================

REM Create directories if they don't exist
if not exist "target\classes" mkdir target\classes

REM Compile all Java files
echo Compiling Java files...
javac -d target\classes src\main\java\com\example\helloworld\model\*.java src\main\java\com\example\helloworld\service\*.java src\main\java\com\example\helloworld\*.java

REM Check if compilation was successful
if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

REM Run the application
echo Running Banking Application...
java -cp target\classes com.example.helloworld.BankingAppLauncher

pause
