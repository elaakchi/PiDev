$ErrorActionPreference = 'Stop'

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectRoot

$mvn = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvn) {
    $fallback = Join-Path $env:USERPROFILE '.tools\apache-maven-3.9.6\bin\mvn.cmd'
    if (Test-Path $fallback) {
        $mvn = Get-Command $fallback
    }
}

if (-not $mvn) {
    Write-Host "Maven not found. Install Maven or reopen the terminal after install." -ForegroundColor Yellow
    Write-Host "Expected mvn on PATH or at: $env:USERPROFILE\.tools\apache-maven-3.9.6\bin\mvn.cmd"
    exit 1
}

& $mvn.Source -q -DskipTests compile javafx:run
