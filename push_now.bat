@echo off
set "PATH=%LOCALAPPDATA%\MinGit\cmd;%PATH%"
echo ========================================================
echo Pushing HinchMart Backend to GitHub...
echo Target: https://github.com/anushatechnologies/hinchbackend.git
echo ========================================================
echo.
git push -u origin main
echo.
echo ========================================================
if %errorlevel% equ 0 (
    echo [SUCCESS] Your code has been pushed to GitHub successfully!
) else (
    echo [ERROR] Push encountered an issue. See details above.
)
echo ========================================================
echo.
pause
