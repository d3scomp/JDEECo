@echo off
REM
REM very simplistic ant batch file for Windows (just for bootstrapping purposes)
REM

REM we don't try fancy things to locate tools.jar - you need to set JAVA_HOME
if "%JAVA_HOME%" == "" goto noJavaHome

REM Set the JPF_HOME directory
set JPF_HOME=%~dp0..

REM where to find javac
set CP=%JAVA_HOME%\lib\tools.jar

REM this is the common ant stuff
set CP=%CP%;%JPF_HOME%\tools\ant-junit.jar;%JPF_HOME%\tools\ant-launcher.jar;%JPF_HOME%\tools\ant-nodeps.jar;%JPF_HOME%\tools\ant.jar

REM other libraries
set CP=%CP%;%JPF_HOME%\lib\junit-4.10.jar


set JVM_FLAGS=-Xmx1024m
java %JVM_FLAGS% -classpath "%CP%" org.apache.tools.ant.Main %1 %2 %3 %4 %5 %6 %7 %8 %9
goto end

:noJavaHome
echo UNABLE TO LOCATE %JAVA_HOME%\lib\tools.jar - PLEASE CHECK %JAVA_HOME%

:end
