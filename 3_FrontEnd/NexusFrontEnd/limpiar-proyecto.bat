@echo off
setlocal

cd /d "%~dp0"

echo Limpiando cache de Angular...
call npx ng cache clean

echo Eliminando carpetas generadas...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Remove-Item -Recurse -Force 'dist','.angular','coverage','node_modules' -ErrorAction SilentlyContinue"

echo Eliminando archivos temporales y paquetes...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Get-ChildItem -Path . -Recurse -Force -File | Where-Object { $_.Name -match '\.zip$|\.tar\.gz$|\.tgz$|\.rar$|\.7z$|\.log$' } | Remove-Item -Force -ErrorAction SilentlyContinue"

echo.
echo Limpieza completada.
pause