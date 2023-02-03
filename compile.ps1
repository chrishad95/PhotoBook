

$sourceFile = "ImageMontage.cs"
$exeFile = "ImageMontage.exe"

# check if the source file exists
if (!(Test-Path $sourceFile)) {
    Write-Output "Source file not found: $sourceFile"
    exit
}

# check if the executable file already exists
if (Test-Path $exeFile) {
    # create a backup of the executable file
    $timestamp = Get-Date -Format yyyy-MM-dd_HH-mm-ss
    $backupFile = $exeFile + "_" + $timestamp + ".bak"
    Move-Item $exeFile $backupFile
    Write-Output "Backed up $exeFile to $backupFile"
}

# compile the source file
$compiler = "C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe"
& $compiler /out:$exeFile $sourceFile

# check if the executable file was created
if (!(Test-Path $exeFile)) {
    Write-Output "Failed to create executable file: $exeFile"
    exit
}

# output success message
Write-Output "Successfully compiled $sourceFile into $exeFile"
