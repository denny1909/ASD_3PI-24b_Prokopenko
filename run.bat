@echo off
echo ========================================
echo Запуск Triangle Finder
echo ========================================
echo.

cd src
java Main

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================
    echo Помилка запуску!
    echo ========================================
    echo Спочатку скомпілюйте проект: compile.bat
    echo.
    pause
)
