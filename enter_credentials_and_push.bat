@echo off
setlocal enabledelayedexpansion
title GitHub Push - HinchMart Backend

for /f "tokens=*" %%i in ('git rev-parse --abbrev-ref HEAD') do set CURRENT_BRANCH=%%i
if "%CURRENT_BRANCH%"=="" set CURRENT_BRANCH=pavansai/backend-updates

cls
echo ======================================================================
echo                HINCHMART BACKEND - GITHUB PUSH WIZARD
echo ======================================================================
echo Repository: https://github.com/anushatechnologies/hinchbackend.git
echo Branch:     %CURRENT_BRANCH%
echo.
echo NOTE: GitHub requires a Personal Access Token (PAT) instead of your
echo       regular password. If you need to generate one:
echo       1. Open: https://github.com/settings/tokens/new
echo       2. Name it "HinchMart" and check the "repo" checkbox.
echo       3. Click "Generate token" and copy it.
echo.
echo ======================================================================
echo.

set /p GITHUB_USER="Enter your GitHub Username (e.g. anushatechnologies): "
if "%GITHUB_USER%"=="" (
    echo [ERROR] Username cannot be empty.
    pause
    exit /b 1
)

set /p GITHUB_TOKEN="Enter your GitHub Personal Access Token: "
if "%GITHUB_TOKEN%"=="" (
    echo [ERROR] Token cannot be empty.
    pause
    exit /b 1
)

echo.
echo ----------------------------------------------------------------------
echo Pushing code to https://github.com/anushatechnologies/hinchbackend.git (branch: %CURRENT_BRANCH%) ...
echo ----------------------------------------------------------------------
echo.

git push -u https://%GITHUB_USER%:%GITHUB_TOKEN%@github.com/anushatechnologies/hinchbackend.git %CURRENT_BRANCH%

echo.
if %errorlevel% equ 0 (
    echo ==================================================================
    echo [SUCCESS] All code has been successfully pushed to GitHub!
    echo Check it at: https://github.com/anushatechnologies/hinchbackend/tree/%CURRENT_BRANCH%
    echo ==================================================================
) else (
    echo ==================================================================
    echo [FAILED] Push failed. Please verify that:
    echo  1. Your username has write access to anushatechnologies/hinchbackend
    echo  2. Your Personal Access Token has the "repo" permission scope.
    echo ==================================================================
)

echo.
pause


