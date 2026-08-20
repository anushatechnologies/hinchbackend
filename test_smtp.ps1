$smtpServer = "smtp.gmail.com"
$smtpPort = 587
$username = "anushatechnologies03@gmail.com"
$password = "nlyvuxzdfzptvfvv"

try {
    $mail = New-Object System.Net.Mail.MailMessage
    $mail.From = New-Object System.Net.Mail.MailAddress($username, "HinchMart Verification")
    $mail.To.Add("anushatechnologies03@gmail.com")
    $mail.Subject = "HinchMart Live Test Email"
    $mail.Body = "Testing real-time OTP delivery."
    $mail.IsBodyHtml = $true

    $smtp = New-Object System.Net.Mail.SmtpClient($smtpServer, $smtpPort)
    $smtp.EnableSsl = $true
    $smtp.Credentials = New-Object System.Net.NetworkCredential($username, $password)
    $smtp.Timeout = 10000

    Write-Host "Connecting to smtp.gmail.com:587 and sending..."
    $smtp.Send($mail)
    Write-Host ">>> EMAIL_SENT_SUCCESSFULLY!" -ForegroundColor Green
} catch {
    Write-Host ">>> EMAIL_ERROR: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.InnerException) {
        Write-Host ">>> INNER: $($_.Exception.InnerException.Message)" -ForegroundColor Yellow
    }
}
