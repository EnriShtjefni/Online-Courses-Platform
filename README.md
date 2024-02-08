# README.md

## Project Overview

This project aims to create a web application that manages courses, their categories, and users using a Spring Boot backend and HTML/JavaScript frontend. The application allows users to perform CRUD operations on courses and includes features such as filtering, user authentication, and authorization.

## Technical Requirements

### Database Setup

1. Create a Table in SQL database (MySQL, PostgreSQL, etc.):
    - **Table Name:** Course
    - **Fields:** id, title, content, category_id, date, author, author_ip_address
    - **Primary Key:** id
    - **Not Null Constraints:** title, content, category_id
    - **Unique Constraint:** title
    - **Foreign Key:** category_id references CourseCategory

2. Create a CourseCategory table:
    - **Table Name:** CourseCategory
    - **Fields:** id, name
    - **Primary Key:** id
    - **Not Null Constraints:** name

3. Create a Users table:
    - **Table Name:** Users
    - **Fields:** id, username, password, role_id
    - **Primary Key:** id
    - **Not Null Constraints:** username, password, role_id

4. Create a Role table:
    - **Table Name:** Role
    - **Fields:** id, name
    - **Primary Key:** id
    - **Not Null Constraints:** name

5. Populate the tables with test data.

### Spring Boot Backend

1. Expose the following API endpoints:

    - **GET:** `/api/courses` (returns a list of all Courses)
    - **GET:** `/api/courses/{id}` (returns a single course, dependent on the parameter passed in)
    - **POST:** `/api/courses` (adds a course)
    - **PUT:** `/api/courses` (modifies a course)
    - **DELETE:** `/api/courses` (deletes a course)
    - **GET:** `/api/courses/filter` (returns a list of filtered courses)

2. Use Spring Boot to create RestControllers to handle these endpoints.

3. Connect to the database using JPA, with Spring Boot configuration.

4. Use Spring Data Repositories (JPA) to access the database and retrieve information.

5. Apply Spring Security to authorize all endpoints.

### Frontend

1. Use HTML and JavaScript for the frontend.

2. Apply CSS styling for an enhanced user interface.

3. Use asynchronous JavaScript to consume the provided endpoints.

4. Implement the following functional requirements:

    - Content of the course can be HTML.
    - Form to create a new course.
    - Table to show all courses and their attribute values. Include filters to search for courses by title or category.
    - The table should have an actions column with buttons to edit and delete.
    - The edit button redirects to the course editing view, a form with fields filled with course data.
    - The delete button is used for deleting the course, with a confirmation dialog.

### Additional Features

1. Add a new field to the Course entity named 'author'. Store the logged-in user when creating and updating a course.

2. Add a new field to the Course entity named 'author_ip_address'. Fill the information on this new field automatically when saving and updating a course.

## Getting Started

1. Clone the repository:

    ```bash
    git clone <repository_url>
    ```

2. Set up and configure the database according to the specifications above.

3. Run the Spring Boot application.

4. Open the frontend HTML file in a browser.

5. Start managing and exploring courses!