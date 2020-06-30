 $FileName = "app.db"
 if (Test-Path $FileName){
    Remove-Item  $FileName
 }
./sqlite3.exe $FileName -cmd ".read initialize.sql"
