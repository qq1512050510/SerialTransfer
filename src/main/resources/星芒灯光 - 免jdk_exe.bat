@echo off
echo Please waiting for open full-screen chrome page...
echo current: %~d0
echo ��ǰ�̷���·����%~dp0
echo ��ǰ������ȫ·����%~f0
set current=%~d0
set currentPath=%~dp0
echo %current%
timeout /t 3

:: pause

set JAVA_HOME=%currentPath%jdk1.8.0_251
set CLASSPATH=.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOMe%\lib\tools.jar;
set Path=%JAVA_HOME%\bin;

echo %JAVA_HOME%

:: �˴�Ϊbat�ļ��̷�
:: C:
%current%
:: �˴�Ϊjar��λ��
:: cd C:\Light
cd %currentPath%
java -jar SerialTransfer-1.0-SNAPSHOT.jar 
exit