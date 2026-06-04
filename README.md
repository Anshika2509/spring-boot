# Java + Spring Boot + MCP Learning Notes

## Phase 1: Environment Setup to First Working MCP Tool

**Author:** Anshika
**Project:** weather-mcp-server
**Java Version:** 21
**Spring Boot Version:** 3.5.14
**Spring AI Version:** 1.0.1

---

# 1. Goal

Build a Java MCP (Model Context Protocol) server using Spring Boot and connect it to Claude Desktop.

Final outcome:

```text
Claude Desktop
    ↓
MCP Protocol
    ↓
Spring AI MCP Server
    ↓
Java Tool Method
    ↓
Response back to Claude
```

---

# 2. Java Installation

Installed:

```text
JDK 21
```

Downloaded:

```text
x64 Installer (.exe)
```

Reason:

* Simplest installation
* Automatically configures many settings
* Easier than ZIP distribution

---

# 3. Verify Java Installation

Command:

```powershell
java -version
```

Expected:

```text
java version "21.x.x"
```

Verify compiler:

```powershell
javac -version
```

Expected:

```text
javac 21.x.x
```

---

# 4. Maven Installation

Downloaded:

```text
apache-maven-3.9.16-bin.zip
```

Reason:

* Binary distribution
* Ready to use
* No need to build Maven from source

---

# 5. MAVEN_HOME

Correct:

```text
C:\Users\anshika\Downloads\apache-maven-3.9.16-bin\apache-maven-3.9.16
```

Incorrect:

```text
C:\Users\anshika\Downloads\apache-maven-3.9.16-bin
```

MAVEN_HOME must point to the folder containing:

```text
bin
conf
lib
```

---

# 6. Environment Variables

## JAVA_HOME

Example:

```text
C:\Program Files\Java\jdk-21.0.11
```

---

## MAVEN_HOME

Example:

```text
C:\Users\anshika\Downloads\apache-maven-3.9.16-bin\apache-maven-3.9.16
```

---

## PATH

Added:

```text
%JAVA_HOME%\bin
```

```text
%MAVEN_HOME%\bin
```

---

# 7. Verify Maven

Command:

```powershell
mvn -version
```

Expected:

```text
Apache Maven 3.9.16
Java version: 21
```

---

# 8. Maven Wrapper

Generated automatically by Spring Initializer.

Files:

```text
mvnw
mvnw.cmd
.maven/
```

Purpose:

Allows project to use its own Maven version.

Instead of:

```powershell
mvn clean package
```

can use:

```powershell
.\mvnw.cmd clean package
```

Recommended for projects.

---

# 9. Creating Spring Boot Project

Created using:

```text
Spring Initializr
```

Configuration:

```text
Project: Maven
Language: Java
Spring Boot: 3.5.14
Packaging: Jar
Java: 21
```

Metadata:

```text
Group: com.anshika
Artifact: weather-mcp-server
Package: com.anshika.weathermcp
```

---

# 10. Properties vs YAML

Options:

```text
application.properties
```

or

```text
application.yml
```

Selected:

```text
YAML
```

Reason:

Cleaner structure.

Example:

```yaml
spring:
  application:
    name: weather-mcp-server
```

Instead of:

```properties
spring.application.name=weather-mcp-server
```

---

# 11. Spring Boot Main Class

Generated:

```java
@SpringBootApplication
public class WeatherMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            WeatherMcpServerApplication.class,
            args
        );
    }
}
```

Purpose:

Application entry point.

Equivalent to Java's:

```java
public static void main()
```

---

# 12. First REST Controller

Created:

```java
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Anshika";
    }
}
```

---

## What happens?

Request:

```http
GET /hello
```

Spring executes:

```java
hello()
```

Response:

```text
Hello Anshika
```

---

# 13. Running Spring Boot

Command:

```powershell
.\mvnw.cmd spring-boot:run
```

Expected:

```text
Started WeatherMcpServerApplication
```

---

# 14. Why "Started WeatherMcpServerApplication" Appears

You did not write any logger.

Spring Boot automatically logs startup information.

Generated internally by:

```java
SpringApplication.run(...)
```

---

# 15. Default Port

Spring Boot default:

```text
8080
```

Example:

```text
http://localhost:8080/hello
```

---

# 16. Change Port

application.yml

```yaml
server:
  port: 8081
```

Application becomes:

```text
http://localhost:8081/hello
```

---

# 17. Port Conflict

Error:

```text
Port 8080 already in use
```

Investigation:

```powershell
netstat -ano | findstr :8080
```

Found:

```text
TNSLSNR.EXE
```

Oracle Listener.

Solution:

Changed application port:

```yaml
server:
  port: 8081
```

---

# 18. Spring Properties Usage

Defined:

```yaml
spring:
  application:
    name: weather-mcp-server
```

Access in Java:

```java
@Value("${spring.application.name}")
private String appName;
```

or

```java
@ConfigurationProperties
```

Equivalent to MuleSoft:

```yaml
host: localhost
```

accessed using:

```text
${host}
```

---

# 19. MCP Introduction

MCP =

```text
Model Context Protocol
```

Purpose:

Allows AI clients to call external tools.

Examples:

```text
Claude Desktop
Cursor
Windsurf
Custom AI Apps
```

---

# 20. Spring AI MCP Dependency

Added:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server</artifactId>
</dependency>
```

---

# 21. Spring AI BOM

Added:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-bom</artifactId>
            <version>1.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Purpose:

Manages Spring AI dependency versions.

---

# 22. First MCP Tool

Created:

```java
@Service
public class GreetingTools {

    @Tool(description = "Greets a person")
    public String greet(String name) {
        return "Hello " + name;
    }
}
```

---

# 23. Understanding @Service

Marks class as Spring Bean.

Spring automatically creates:

```java
GreetingTools greetingTools
```

inside Application Context.

Equivalent idea in Mule:

```text
Global configuration automatically managed by runtime
```

---

# 24. Understanding @Tool

Marks method as MCP Tool.

Without it:

```java
public String greet(...)
```

is normal Java.

With it:

```java
@Tool
public String greet(...)
```

becomes callable by Claude.

---

# 25. Tool Registration

Important:

Tool isn't automatically exposed.

Needed:

```java
@Bean
public ToolCallbackProvider mcpToolProvider(
        GreetingTools greetingTools){

    return MethodToolCallbackProvider.builder()
            .toolObjects(greetingTools)
            .build();
}
```

---

# 26. Common Mistake

Created:

```java
@Service
public class GreetingTools
```

AND

```java
@Bean
public GreetingTools greetingTools()
```

Result:

```text
Bean already defined
```

Because Spring tried creating two GreetingTools objects.

Solution:

Keep only:

```java
@Service
```

---

# 27. MCP Configuration

application.yml

```yaml
spring:
  ai:
    mcp:
      server:
        name: weather-server
        version: 1.0.0
```

---

# 28. Building JAR

Command:

```powershell
.\mvnw.cmd clean package
```

Creates:

```text
target/
    weather-mcp-server-0.0.1-SNAPSHOT.jar
```

---

# 29. Claude Desktop Configuration

Added:

```json
{
  "mcpServers": {
    "weather-server": {
      "command": "C:\\Program Files\\Java\\jdk-21.0.11\\bin\\java.exe",
      "args": [
        "-Dspring.ai.mcp.server.stdio=true",
        "-jar",
        "C:\\Users\\anshika\\Documents\\spring-boot\\weather-mcp-server\\target\\weather-mcp-server-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

---

# 30. Why Absolute Java Path Was Needed

Claude initially failed:

```text
java is not recognized
```

Reason:

Claude could not resolve PATH.

Solution:

Use:

```text
C:\Program Files\Java\jdk-21.0.11\bin\java.exe
```

directly.

---

# 31. Successful MCP Startup

Logs:

```text
Server started and connected successfully
```

Meaning:

✅ Claude launched Java

✅ Java launched Spring Boot

✅ Spring Boot launched MCP server

---

# 32. Tool Discovery

Initially:

```json
{
  "tools": []
}
```

Meaning:

No MCP tools registered.

After ToolCallbackProvider:

```json
{
  "tools": [
    {
      "name": "greet"
    }
  ]
}
```

Success.

---

# 33. MCP STDIO Rule

Never use:

```java
System.out.println(...)
```

in STDIO MCP servers.

Reason:

stdout carries JSON-RPC protocol messages.

Corrupts communication.

---

# 34. Working MCP Architecture

```text
Claude Desktop
        |
        v
MCP Protocol
        |
        v
Spring AI MCP Server
        |
        v
GreetingTools
        |
        v
greet()
        |
        v
Hello Anshika
```

---

# Current Status

✅ Java installed

✅ Maven installed

✅ Spring Boot application created

✅ REST endpoint created

✅ YAML configuration understood

✅ MCP dependency configured

✅ MCP server configured

✅ Claude Desktop connected

✅ First MCP Tool created

✅ First MCP Tool successfully discovered and invoked

---

# Next Phase

Implement actual Weather MCP Server:

```java
getWeatherForecastByLocation()
```

```java
getAlerts()
```

using:

```java
RestClient
```

and expose them to Claude as MCP tools.

