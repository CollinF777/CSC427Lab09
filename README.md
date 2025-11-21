# CSC427 Lab 09
**Authors:** Collin Fair, Matthew Robinson, Corey Suhr, Samprada Pradhan  
**Course:** CSC 427 – Fall 2025

## Overview
This repository contains the Lab 09 assignment for CSC 427. The project is implemented in Java using Maven for build and dependency management. This lab shows our patient portal with controllers, services, and a CLI 

## Features
- Java-based implementation
- Maven project structure
- `commands.txt` file with useful commands for building and running the project
- Organized source code under `src/`
- `.gitignore` included to ignore build artifacts and IDE files

## Prerequisites
- Java JDK 11 or higher
- Maven installed and available on your PATH
- JUnit Installed

## Build and Run
1. Clone the repository:
```bash
git clone https://github.com/CollinF777/CSC427Lab09.git
```

2. Open it in InteliJ and either  
2a. Run main  
2b. Run the tests

## Project Structure
```
CSC427Lab09/  
│  pom.xml  
│  commands.txt  
│  .gitignore  
└─ src/  
   └─ main/  
      └─ java/  
         └─ edu.secourse  
           └─ controllers  
             └─ AppointmentController  
             └─ UserController  
           └─ exceptions  
             └─ AppointmentDoesNotExistException  
             └─ InvalidIdException  
           └─ models  
             └─ Admin  
             └─ Appointment  
             └─ Doctor  
             └─ Patient  
             └─ User  
           └─ services  
             └─ AppointmentService  
             └─ UserService  
           └─ types  
             └─ InputHandler  
             └─ Pair  
           └─ Main  
         └─ resources  
   └─ test  
     └─ java  
       └─ edu.secourse  
         └─ controllers  
          └─ AppointmentControllerTest  
          └─ UserControllerTest  
         └─ services  
          └─ AppointmentServiceTest  
          └─ UserServiceTest  
```
