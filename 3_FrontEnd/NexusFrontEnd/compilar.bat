@echo off
setlocal

cd /d "%~dp0"

echo Instalando dependencias...
call npm install
if errorlevel 1 (
    echo Error al ejecutar npm install
    pause
    exit /b 1
)

echo Compilando proyecto...
call npm run build
if errorlevel 1 (
    echo Error al ejecutar npm run build
    pause
    exit /b 1
)

echo.
echo Proceso completado correctamente.
pause