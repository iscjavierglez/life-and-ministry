# Project Instructions & Coding Standards

## Project Overview
This project is a Spring Boot MVC application using Thymeleaf, Bootstrap, and MongoDB.  
It is designed to facilitate the management of a Jehovah’s Witness congregation's Life and Ministry meeting programs.  
Key features:
- Create new programs and assignations (meeting agenda items).
- Assign or rotate program sections to publishers (participants).
- Track assignment history for balanced rotation.
- Display both current and future Life and Ministry programs.

## Coding Standards
- **Code Style:** Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
- **Lombok:** Use Lombok annotations such as `@Data`, `@Builder`, `@Slf4j`, etc., to reduce boilerplate.
- **Constructor Injection:** Prefer constructor-based dependency injection for all components.
- **Testing:** Use Mockito for mocking dependencies and unit testing.
- **Documentation:** Provide meaningful JavaDoc for classes and public methods; maintain Markdown documentation for major features.

## Domain Knowledge

### Core Entities
- **Publisher:** Person eligible for meeting assignments. Store personal and status info. Used to track and rotate assignments.
- **Assignation:** An item (bullet) in the meeting agenda, assigned to a Publisher for a specific date/type.
- **LifeAndMinistry:** Represents a monthly meeting program, containing multiple assignations for various sections.

### Rules & Relationships
- Each `LifeAndMinistry` program is identified by month.
- Each `Assignation` references a `Publisher` and has associated metadata (type, date, duration, etc.).
- Publishers can be filtered by status or privilege.
- Tracking previous assignations for fair assignment rotation is important.

## Data Access Layer
- Use the **Repository pattern** for all data access.
- Leverage Spring Data MongoDB's derived query method naming for repository interfaces.
- Prefer interfaces for repositories that extend `MongoRepository<Entity, ObjectId>`, and define custom queries when appropriate.

## Application Structure
- **Layered Architecture:**  
  - **Controller:** Handles HTTP requests and view interactions (Thymeleaf).
  - **Service:** Encapsulates business logic and application workflows.
  - **Repository:** Handles data access.
  - **DTOs:** Use DTO classes to communicate between Service and Controller layers and for view rendering (avoid exposing entities directly to UI).
- **Logging:** Use Lombok’s `@Slf4j` for logging within services/controllers.

## Common Practices
- **Dependency Injection:** Always use constructor injection.
- **Lombok Usage:** Use appropriate Lombok annotations to reduce boilerplate (e.g., `@Data`, `@Builder`, etc.).
- **Testing:** Write unit tests with Mockito for business logic/service layer.
- **JavaDoc:** All public classes and methods should have JavaDoc.

## Security, Validation, and Best Practices
- Follow Spring Boot’s **standard security practices** (such as using Spring Security if access control is needed, configure CSRF protection, etc.).
- Validate all user inputs on both server and client-side.
- Use DTO validation with annotations like `@Valid`, `@NotNull`, etc.
- Sanitize and escape data in views to prevent injection/XSS vulnerabilities.
- Prefer using Spring’s exception handling mechanisms (`@ControllerAdvice`, custom exceptions) for global error handling.

## Internationalization and Localization
- Design the application to support internationalization and localization as needed, using Spring’s message resource mechanisms.

---

**Summary:**  
Follow best practices for a layered, maintainable, and secure MVC application with clear domain modeling, repository-based persistence, and Google Java Style.