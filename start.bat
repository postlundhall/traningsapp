@echo off
title Trainingsregister App Starter
echo =====================================
echo   Starting Trainingsregister App
echo =====================================
echo.

REM Kontrollera att Java är installerat
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java is not installed or not in PATH.
    echo Please install Java 17+ and try again.
    pause
    exit /b 1
)

REM Kontrollera att Maven Wrapper finns
if not exist mvnw.cmd (
    echo [ERROR] mvnw.cmd not found!
    echo Make sure you are in the project root folder.
    pause
    exit /b 1
)

echo.
echo Starting Spring Boot application with embedded H2 database...
echo (This may take a few seconds)
echo.

REM Starta browser
start "" http://localhost:8080

REM Kör Spring Boot via Maven Wrapper
call mvnw.cmd spring-boot:run

echo.
echo Application stopped.
pause
