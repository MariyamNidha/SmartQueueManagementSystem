$ErrorActionPreference = 'Stop'
$source = 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip'
$target = 'C:\Tools\apache-maven-3.9.9-bin.zip'
$extractDir = 'C:\Tools'

New-Item -ItemType Directory -Force -Path 'C:\Tools' | Out-Null
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
$wc = New-Object System.Net.WebClient
$wc.DownloadFile($source, $target)
Expand-Archive -Path $target -DestinationPath $extractDir -Force
Write-Host 'Maven downloaded and extracted to C:\Tools'
