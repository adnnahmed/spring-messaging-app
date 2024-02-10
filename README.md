# Messaging App with Spring Boot, Cassandra, and Astra DB

## Overview

Welcome to the Messaging App, a robust platform for real-time communication built using cutting-edge technologies. This application leverages Spring Boot for the backend, Apache Cassandra and Astra DB for data storage, Spring Data Cassandra for efficient data layer operations, and Spring Security for securing user interactions. The view layer is crafted with Thymeleaf, providing a seamless user experience, and OAuth2 login via GitHub ensures a secure and streamlined authentication process.

## Technologies Used

-   **Spring Boot**: A powerful framework for building Java-based enterprise applications, simplifying development and deployment.
    
-   **Apache Cassandra**: A highly scalable NoSQL database, chosen for its ability to handle large datasets with high availability.
    
-   **Astra DB**: A managed service for Apache Cassandra by DataStax, enhancing scalability and ease of use.
    
-   **Spring Data Cassandra**: Simplifies the interaction with Cassandra database through the Spring application.
    
-   **Spring Security**: Provides robust security features to safeguard user authentication and interactions.
    
-   **Thymeleaf**: A modern server-side Java template engine for web and standalone environments, ensuring a dynamic and responsive view layer.
    
-   **OAuth2 Login (GitHub)**: Streamlines user authentication through GitHub, enhancing security and user experience.
    

## Features

-   **Instant Messaging**: Engage in real-time text conversations for quick and efficient communication.
    
-   **User Authentication**: Ensure data privacy and account integrity through secure user authentication via GitHub.
    

## Setup Instructions

1.  **Clone the Repository:**
    
    `git clone https://github.com/your-username/messaging-app.git` 
    
2.  **Configure Astra DB and GitHub OAuth:**
    
    -   Sign up for an Astra DB account and obtain the connection details.
    -   Set up OAuth2 login with GitHub and update the credentials in `src/main/resources/application.properties`.
3.  **Run the Application:**
    
    `./mvnw spring-boot:run` 
    
4.  **Access the App:** Open your browser and go to http://localhost:8080.

## Contribution Guidelines

Contributions are welcome! Feel free to submit pull requests for improvements or new features.

## License

This messaging app is licensed under the MIT License - see the LICENSE file for details.
