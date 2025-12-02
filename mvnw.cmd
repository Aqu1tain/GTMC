@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF)
@REM under one or more contributor license agreements.
@REM See the NOTICE file distributed with this work for additional
@REM information regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------

@echo off
setlocal enabledelayedexpansion

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@setlocal

set WRAPPER_JAR=%APP_HOME%\.mvn\wrapper\maven-wrapper.jar
set WRAPPER_PROPERTIES=%APP_HOME%\.mvn\wrapper\maven-wrapper.properties

if exist "%WRAPPER_JAR%" (
    for %%i in ("%WRAPPER_JAR%") do set WRAPPER_JAR_PATH=%%~fi
)

if exist "%WRAPPER_PROPERTIES%" (
    for /f "delims=" %%i in (%WRAPPER_PROPERTIES%) do (
        if "%%i" neq "" (
            for /f "tokens=1,* delims==" %%a in ("%%i") do (
                if "%%a"=="distributionUrl" set DOWNLOAD_URL=%%b
                if "%%a"=="wrapperUrl" set WRAPPER_URL=%%b
            )
        )
    )
)

set MAVEN_OPTS=%MAVEN_OPTS% "-Dmaven.wagon.http.ssl.insecure=true"

for /f "tokens=1,* delims= " %%a in ("%*") do (
    set first_arg=%%a
    set other_args=%%b
)

if "%DOWNLOAD_URL%"=="" (
    set DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip
)

if "%WRAPPER_URL%"=="" (
    set WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
)

setlocal

if "%first_arg%" == "-h" goto help
if "%first_arg%" == "--help" goto help

set MVN_CMD="%JAVA_HOME%\bin\java.exe" %MAVEN_OPTS% -cp "%WRAPPER_JAR_PATH%" org.apache.maven.wrapper.MavenWrapperMain %*

%MVN_CMD%
goto end

:help
echo Maven wrapper for Windows
echo Usage: mvnw clean package
goto end

:end
endlocal & set ERROR_CODE=%ERRORLEVEL%
exit /b %ERROR_CODE%
