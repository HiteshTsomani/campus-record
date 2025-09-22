# Campus Course Records Manager (CCRM)

A console-based Java SE application for managing student records, courses, and grades.

## How to Run

### Prerequisites
- Java JDK 11 or higher
- Command line or IDE (Eclipse recommended)

### Steps
1. **Compile**: `javac -d out src/edu/ccrm/**/*.java`
2. **Run**: `java -cp out edu.ccrm.cli.CCRMApplication`
3. **Enable Assertions**: `java -ea -cp out edu.ccrm.cli.CCRMApplication`

## Java Concept Mapping

| Concept | Location | Description |
|---------|----------|-------------|
| **Encapsulation** | `Student.java`, `Course.java` | Private fields with getters/setters |
| **Inheritance** | `Person.java` → `Student.java`, `Instructor.java` | Abstract base class |
| **Polymorphism** | `Person.getRole()` | Method overriding |
| **Abstraction** | `Person.java`, `Persistable.java` | Abstract class and interface |
| **Singleton** | `AppConfig.java` | Thread-safe singleton |
| **Builder** | `Course.Builder` | Fluent object construction |
| **Static Nested** | `Student.GPAStatistics` | Encapsulated statistics |
| **Inner Class** | `StudentService.TranscriptService` | Access to outer class |
| **Enums** | `Grade.java`, `Semester.java` | Type-safe constants |
| **Lambdas** | Service classes | Filtering and sorting |
| **Streams** | Various | Data processing |
| **NIO.2** | `FileIOService.java` | Modern file I/O |
| **DateTime** | Domain classes | Timestamps |
| **Recursion** | `RecursiveUtils.java` | Directory size calculation |
| **Exceptions** | `DuplicateEnrollmentException.java` | Custom exceptions |

## Evolution of Java

- **1995**: Java 1.0 - Basic OOP
- **1998**: Java 1.2 - Collections Framework
- **2004**: Java 5 - Generics, Enums, Annotations
- **2014**: Java 8 - Lambda expressions, Stream API
- **2017**: Java 9 - Module system
- **2021**: Java 17 LTS - Current long-term support

## Java Platform Comparison

| Platform | Target | Use Cases |
|----------|--------|-----------|
| **Java ME** | Mobile/Embedded | IoT devices, smart cards |
| **Java SE** | Desktop/Server | This CCRM application |
| **Java EE** | Enterprise | Web applications, microservices |

## JDK/JRE/JVM Architecture

- **JVM**: Executes bytecode (platform-specific)
- **JRE**: JVM + runtime libraries
- **JDK**: JRE + development tools (javac, debugger)

## Features

- Student management (add/update/list/deactivate)
- Course management with Builder pattern
- Enrollment with business rules validation
- Grade recording and GPA calculation
- CSV import/export functionality
- Backup system with timestamps
- Stream-based reporting

## Directory Structure

