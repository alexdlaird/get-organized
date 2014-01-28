title Compress Get Organized Source

:: Declare necessary path variables
set toolsTop=..\..\..\Tools
set commonTop=..\..\Common
set sourceTop=..
set tempTop=..\..\..\temp
set tempTools=..\..\..\temp\Tools
set tempSource=..\..\..\temp\Source
set tempCommonTop=..\..\..\temp\Source\Common
set tempSourceTop=..\..\..\temp\Source\GetOrganized

:: Remove any temporary files and folders from a previous copy or zip
if exist source.zip del source.zip
if exist "%tempTop%" rmdir "%tempTop%" /s /q
mkdir "%tempTop%"
mkdir "%tempTools%"
mkdir "%tempSource%"
mkdir "%tempCommonTop%"
mkdir "%tempSourceTop%"

:: Copy all source and dependencies to temporary location
xcopy "%toolsTop%" "%tempTools%" /e /i /k
xcopy "%commonTop%" "%tempCommonTop%" /e /i /k
xcopy "%sourceTop%" "%tempSourceTop%" /e /i /k

:: Specify files to keep from Tools
for %%f in ("%tempTools%\*.*") do (
   if not "%%~f" == "%tempTools%\7za.exe" (
      if not "%%~f" == "%tempTools%\freeze" (
         if not "%%~f" == "%tempTools%\jarbundler-2.2.0.jar" (
            if not "%%~f" == "%tempTools%\UIManagerDefaults.jnlp" (
               del "%%~f"
            )
         )
      )
   )
)

:: Specify folders to keep from Tools
for /d %%f in ("%tempTools%\*") do (
   if not "%%~f" == "%tempTools%\InnoSetup" (
      if not "%%~f" == "%tempTools%\Launch4j" (
         if not "%%~f" == "%tempTools%\Python" (
            rmdir "%%~f" /s /q
         )
      )
   )
)

:: Specify folders to keep from Source\Common
for /d %%f in ("%tempCommonTop%\*") do (
   if not "%%~f" == "%tempCommonTop%\JCalendar_GO" (
      if not "%%~f" == "%tempCommonTop%\lib" (
         rmdir "%%~f" /s /q
      )
   )
)

:: Specify files to keep from Source\Common\lib
for %%f in ("%tempCommonTop%\lib\*.*") do (
   if not "%%~f" == "%tempCommonTop%\lib\ansir_tristate.jar" (
      if not "%%~f" == "%tempCommonTop%\lib\AppleJavaExtensions.jar" (
         if not "%%~f" == "%tempCommonTop%\lib\commons-net-2.2.jar" (
            if not "%%~f" == "%tempCommonTop%\lib\JCalendar_GO.jar" (
               del "%%~f"
            )
         )
      )
   )
)

:: Remove build folders from the source
if exist "%tempSourceTop%\build" rmdir "%tempSourceTop%\build" /s /q
if exist "%tempSourceTop%\dist" rmdir "%tempSourceTop%\dist" /s /q
if exist "%tempSourceTop%\store" rmdir "%tempSourceTop%\store" /s /q
:: Remove Python compiled binaries from Source/scripts
if exist "%tempSourceTop%\scripts\__pycache__" rmdir "%tempSourceTop%\scripts\__pycache__" /s /q
if exist "%tempSourceTop%\scripts\ValidateVersion.pyc" del "%tempSourceTop%\scripts\ValidateVersion.pyc"
:: Remove private configuration frmo the source
if exist "%tempSourceTop%\nbproject\configs" rmdir "%tempSourceTop%\nbproject\configs" /s /q
if exist "%tempSourceTop%\nbproject\private\config.properties" del "%tempSourceTop%\nbproject\private\config.properties"

:: Zip everything
"%toolsTop%\7za.exe" a source.zip "%tempTools%" "%tempSource%"

:: Remove temporary folder
rmdir %tempTop% /s /q