[Setup]
AppName=Get Organized
AppMutex=getorganized,Global\getorganized
AppUpdatesURL=http://alexlaird.com/projects/get-organized
AppCopyright=Copyright (c) 2010-2012 Alex Laird.
AppContact=Alex Laird
AppSupportURL=http://alexlaird.com/projects/get-organized/support
AppVersion=1.09
AppVerName=Get Organized 1.09
AppReadmeFile={app}\releasenotes.html
PrivilegesRequired=none
LicenseFile=license.txt
DefaultDirName={code:DefDirRoot}\Get Organized
DefaultGroupName=Get Organized
DisableProgramGroupPage=yes
UninstallDisplayIcon={app}\uninstall.ico
OutputDir=.

[Files]
Source: Get Organized.exe; DestDir: {app}
Source: go.ico; DestDir: {app}
Source: uninstall.ico; DestDir: {app}
Source: releasenotes.html; DestDir: {app}
Source: license.html; DestDir: {app}

[CustomMessages]
CreateDesktopIcon=Create a &desktop icon

[Tasks]
Name: desktopicon; Description: {cm:CreateDesktopIcon}

[InstallDelete]
Type: files; Name: {app}\license.txt
Type: files; Name: {app}\releasenotes.rtf

[UninstallDelete]
Type: files; Name: {app}\*.*
Type: files; Name: {app}\lib\*.*
Type: filesandordirs; Name: {app}\lib

[Icons]
Name: {group}\Get Organized; Filename: {app}\Get Organized.exe; IconFileName: {app}\go.ico; WorkingDir: {app}
Name: {group}\Uninstall Get Organized; Filename: {uninstallexe}; IconFileName: {app}\uninstall.ico; WorkingDir: {app}
Name: {commondesktop}\Get Organized; Filename: {app}\Get Organized.exe; IconFileName: {app}\go.ico; Tasks: desktopicon; WorkingDir: {app}

[Run]
Filename: {app}\Get Organized.exe; Description: Launch Get Organized; Flags: postinstall nowait skipifsilent
Filename: "http://alexlaird.com/projects/get-organized/donate"; Description: Donate to Get Organized; Flags: postinstall shellexec skipifsilent runasoriginaluser
Filename: {app}\releasenotes.html; Description: View the Release Notes; Flags: postinstall shellexec skipifsilent unchecked

[Code]
function IsRegularUser(): Boolean;
begin
Result := not (IsAdminLoggedOn or IsPowerUserLoggedOn);
end;

function DefDirRoot(Param: String): String;
begin
if IsRegularUser then
Result := ExpandConstant('{localappdata}')
else
Result := ExpandConstant('{pf}')
end;

function getVersion() : String;
begin
   RegQueryStringValue(HKLM, 'SOFTWARE\JavaSoft\Java Runtime Environment', 'CurrentVersion', Result);
end;

function InitializeSetup() : Boolean;
var
   ErrorCode: Integer;
   userVersion:  String;

begin
   Result := true;
   userVersion := getVersion();

   if(userVersion = '') then
   begin
      MsgBox('Java is not installed on this system, and it is required to run this program.' #13#13 'Please install the latest version of Java and then re-install this program.', mbError, MB_OK);

      Result := false;

      ShellExec('open','http://www.java.com/download', '', '', SW_SHOW, ewNoWait, ErrorCode);
   end
   else if(userVersion < '1.6') then
   begin
      MsgBox('Java 1.6 or higher is required to run this program. The installed Java version is ' + userVersion + '.' #13#13 'Please install the latest version of Java and then re-install this program.', mbError, MB_OK);

      Result := false;

      ShellExec('open','http://www.java.com/download', '', '', SW_SHOW, ewNoWait, ErrorCode);
   end
   else
      Result := true;
end;
