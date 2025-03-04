$output = azd env get-values

foreach ($line in $output) {
    if (!($line)){
      break
    }
      $name = $line.Split('=')[0]
      $value = $line.Split('=')[1].Trim('"')
      Set-Item -Path "env:\$name" -Value $value
}

Write-Host "Environment variables set."

$tools = @("az", "func")

foreach ($tool in $tools) {
  if (!(Get-Command $tool -ErrorAction SilentlyContinue)) {
    Write-Host "Error: $tool command line tool is not available"
    exit 1
  }
}

Set-Location ./target/azure-functions/$env:AZURE_FUNCTION_NAME
func azure functionapp publish $env:AZURE_FUNCTION_NAME

Write-Host "Deployment completed."