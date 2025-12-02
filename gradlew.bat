@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@rem Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >nul 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_parse
if "%_SKIP%"=="0" set _SKIP=2
if "%_SKIP%"=="1" set _SKIP=0
for /F "usebackq tokens=*" %%A in (!CMD_LINE_ARGS!) do (
    set CMD_LINE_ARGS=%%A
)
shift
if "not %1"=="" (
    set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
    shift
    goto win9xME_parse
)

:win9xME_args_slurped
if "%CMD_LINE_ARGS%"=="" (
    set CMD_LINE_ARGS=%GRADLE_OPTS%
)

@rem This is where the Gradle Wrapper is called.
"%JAVA_EXE%" "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %CMD_LINE_ARGS%

:end
@endlocal & set ERROR_CODE=%ERRORCODE%

if "%ERROR_CODE%" == "0" goto mainEnd

:fail
rem Set variable GRADLE_OPTS before calling this batch script to pass JVM options to this script.
rem e.g. change the value of GRADLE_OPTS=-Xmx1024m -Xmx2048m
if not "%GRADLE_OPTS%"=="" (
    echo.
    echo WARNING: GRADLE_OPTS environment variable is set to '%GRADLE_OPTS%'.
    echo.
)

echo ERROR: GRADLE Build failed with error code %ERRORCODE%.
exit /b %ERRORCODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal
