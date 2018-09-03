@ECHO off
TITLE NextGenCore - Game Server Panel
:MENU
CLS
ECHO.
ECHO   ^*--------------------------------------------------------------------------^*
ECHO   ^|                      NextGenCore - Game Server Panel   	               ^|
ECHO   ^*--------------------------------------------------------------------------^*
ECHO   ^|                                                                          ^|
ECHO   ^|    1 - DevModus                                4 - Production X3         ^|
ECHO   ^|    2 - Production                              5 - Production Highend    ^|
ECHO   ^|    3 - Production X2                           6 - Exit                  ^|
ECHO   ^|                                                                          ^|
ECHO   ^*--------------------------------------------------------------------------^*
ECHO.
SET /P OPTION=Type your option and press ENTER: 
IF %OPTION% == 1 (
SET MODE=DevModus
SET JAVA_OPTS=-Xms700m -Xmx1000m -Xdebug -Xrunjdwp:transport=dt_socket,address=8998,server=y,suspend=n -ea
CALL StartGS.bat
)
IF %OPTION% == 2 (
SET MODE=PRODUCTION
SET JAVA_OPTS=-Xms1536m -Xmx1536m -server
CALL StartGS.bat
)
IF %OPTION% == 3 (
SET MODE=PRODUCTION X2
SET JAVA_OPTS=-Xms3072m -Xmx3072m -server
CALL StartGS.bat
)
IF %OPTION% == 4 (
SET MODE=PRODUCTION X3
SET JAVA_OPTS=-Xms6144m -Xmx6144m -server
CALL StartGS.bat
)
IF %OPTION% == 5 (
SET MODE=PRODUCTION HIGHEND
SET JAVA_OPTS=-Xms12288 -Xmx12288 -server
CALL StartGS.bat
)
IF %OPTION% == 6 (
EXIT
)
IF %OPTION% GEQ 5 (
GOTO :MENU
)