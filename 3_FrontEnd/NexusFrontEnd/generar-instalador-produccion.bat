@echo off
setlocal

cd /d "%~dp0"

echo Generando build de produccion para /NexusFrontEnd/...
call npx ng build --configuration production --base-href /NexusFrontEnd/
if errorlevel 1 (
    echo Error al generar el build de produccion.
    pause
    exit /b 1
)

echo.
echo Build generado correctamente.
echo Ruta esperada
echo dist\NexusFrontEnd\browser
pause