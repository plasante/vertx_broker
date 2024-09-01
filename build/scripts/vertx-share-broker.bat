@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  vertx-share-broker startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and VERTX_SHARE_BROKER_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\vertx-share-broker-1.0.0-SNAPSHOT.jar;%APP_HOME%\lib\vertx-web-4.5.9.jar;%APP_HOME%\lib\log4j-slf4j18-impl-2.17.1.jar;%APP_HOME%\lib\slf4j-api-2.0.7.jar;%APP_HOME%\lib\log4j-core-2.17.1.jar;%APP_HOME%\lib\log4j-api-2.17.1.jar;%APP_HOME%\lib\jackson-annotations-2.16.1.jar;%APP_HOME%\lib\vertx-web-client-4.5.9.jar;%APP_HOME%\lib\vertx-web-common-4.5.9.jar;%APP_HOME%\lib\vertx-auth-common-4.5.9.jar;%APP_HOME%\lib\vertx-bridge-common-4.5.9.jar;%APP_HOME%\lib\vertx-uri-template-4.5.9.jar;%APP_HOME%\lib\vertx-core-4.5.9.jar;%APP_HOME%\lib\jackson-core-2.16.1.jar;%APP_HOME%\lib\jackson-databind-2.16.1.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.111.Final.jar;%APP_HOME%\lib\netty-codec-http2-4.1.111.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.111.Final.jar;%APP_HOME%\lib\netty-resolver-dns-4.1.111.Final.jar;%APP_HOME%\lib\netty-handler-4.1.111.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.111.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.111.Final.jar;%APP_HOME%\lib\netty-codec-dns-4.1.111.Final.jar;%APP_HOME%\lib\netty-codec-4.1.111.Final.jar;%APP_HOME%\lib\netty-transport-4.1.111.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.111.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.111.Final.jar;%APP_HOME%\lib\netty-common-4.1.111.Final.jar


@rem Execute vertx-share-broker
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %VERTX_SHARE_BROKER_OPTS%  -classpath "%CLASSPATH%" io.vertx.core.Launcher %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable VERTX_SHARE_BROKER_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%VERTX_SHARE_BROKER_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
