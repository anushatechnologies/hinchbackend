@echo off
setlocal enabledelayedexpansion
title GitHub Push - HinchMart Backend
cls
echo ======================================================================
echo                HINCHMART BACKEND - GITHUB PUSH WIZARD
echo ======================================================================
echo Repository: https://github.com/anushatechnologies/hinchbackend.git
echo Branch:     pavansai/feature-branch
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
echo Pushing code to https://github.com/anushatechnologies/hinchbackend.git (branch: pavansai/feature-branch) ...
echo ----------------------------------------------------------------------
echo.

git push https://%GITHUB_USER%:%GITHUB_TOKEN%@github.com/anushatechnologies/hinchbackend.git pavansai/feature-branch

echo.
if %errorlevel% equ 0 (
    echo ==================================================================
    echo [SUCCESS] All code has been successfully pushed to GitHub!
    echo Check it at: https://github.com/anushatechnologies/hinchbackend/tree/pavansai/feature-branch
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

