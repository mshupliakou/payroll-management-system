# Payroll Management System 💰
Payroll Management System is a comprehensive web application built with Java Spring Boot designed to automate human resources management, time tracking, and payroll processing. Developed as an academic project (AGH), it demonstrates a classic multi-tier architecture utilizing JDBC for direct database interactions.

## 📋 Key Features
### 👤 HR Management
- Employee CRUD: Add, edit, and terminate employee records.

- Organizational Structure: Manage Departments and Job Positions.

- Role-Based Access Control: Secure access (Administrator, Employee, Manager) using Spring Security.

### ⏱ Time Tracking
- Work Logging: Employees can log work hours, specifying work types (e.g., Remote, Office) and assigned projects.

- Approval Workflow: System for managers to approve or reject time logs.

- Statistics: View aggregated work data (total hours, weekly averages) per employee.

### 💸 Payroll Automation
- Automated Payouts: A background task (Scheduled) automatically triggers salary calculations on the 1st of every month.

- Salary History: Audit trail of salary changes via SalaryChangeHistory.

- Payment Processing: Generate and track payment statuses (Pending, Paid, Rejected).

- Database Logic: Utilizes SQL stored procedures (e.g., generuj_wyplaty_za_miesiac) for heavy lifting and transactional integrity.

### 📂 Project Management
- Create and manage projects.

- Assign employees to specific projects with defined roles.

### 🛠 Tech Stack
- Language: Java (JDK 17+)

- Framework: Spring Boot (Web, Security, JDBC)

- Database: PostgreSQL (Uses JdbcTemplate and native SQL queries)

- Frontend: Thymeleaf, HTML5, CSS3, JavaScript

- Build Tool: Maven

### 📚 Documentation (Polish)
- ⚠️ Note: The project contains detailed technical documentation in Polish. This includes the database schema, entity diagrams, and implementation details.

- 📄 File location:

documentation/Mikhail_Shupliakou_Dokumentacja_Projekt_BD1.pdf

### 🚀 Getting Started
Prerequisites
- Java 17 or higher.

- PostgreSQL (Database must be created and schema imported).

- Maven (or use the included mvnw wrapper).

###Installation Steps
1. Clone the repository:

````

git clone https://github.com/your-username/payroll-management-system.git
cd payroll-management-system
````
2. Database Configuration:

- Ensure your PostgreSQL service is running.

- Create a database (e.g., payroll_db).

- Important: You must import the database schema and stored procedures before running the app. Check the documentation folder or provided SQL scripts.

- Update src/main/resources/application.properties with your credentials:

3. Properties

- spring.datasource.url=jdbc:postgresql://localhost:5432/payroll_db
- spring.datasource.username=postgres
- spring.datasource.password=your_password

4. Build and Run:

````

./mvnw spring-boot:run
Access the Application: Open your browser and navigate to: http://localhost:8080

🗂 Project Structure
src/main/java/.../controller — REST and MVC controllers (HTTP request handling).

src/main/java/.../repositories/jdbc — Repository layer implemented with raw JDBC (direct SQL).

src/main/java/.../service — Business logic (including AutoPayoutService).

src/main/java/.../entities — POJO classes representing database tables.

src/main/resources/templates — Thymeleaf HTML templates.
````

### 👨‍💻 Author
Developed as part of the Database Systems course (Projekt BD1). Mikhail Shupliakou