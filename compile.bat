@echo off
REM Simple compile script for SeasonalBundle plugin

echo Compiling SeasonalBundle plugin...

REM Create directories
if not exist "build\classes" mkdir build\classes

REM Compile with javac
javac -encoding UTF-8 -d build\classes ^
    src\main\java\com\gtmc\seasonalbundle\SeasonalBundlePlugin.java ^
    src\main\java\com\gtmc\seasonalbundle\data\SeasonDataManager.java ^
    src\main\java\com\gtmc\seasonalbundle\util\BundleUtil.java ^
    src\main\java\com\gtmc\seasonalbundle\listeners\PlayerJoinListener.java ^
    src\main\java\com\gtmc\seasonalbundle\listeners\ItemValidationListener.java ^
    src\main\java\com\gtmc\seasonalbundle\commands\SeasonCommand.java -cp ".mvn\wrapper\*"

if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo Copy build\classes contents to your server plugins folder with plugin.yml
pause
