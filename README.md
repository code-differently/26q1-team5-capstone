# 26q1-team5-capstone
# AI Grant & Financial Aid Matcher

> *Find the funding you actually qualify for — powered by AI.*

---

##  Overview

The **AI Grant & Financial Aid Matcher** is a full-stack web application that helps students discover and manage funding opportunities using intelligent matching.

Instead of relying on fragmented scholarship websites, the platform integrates real grant data from **:contentReference[oaicite:0]{index=0}** and uses AI to connect users with opportunities they are most likely qualified for.

By combining structured grant data with personalized student profiles, the system transforms a complex and overwhelming search process into a streamlined, data-driven experience.

---

## 💡 Key Value
Unlike traditional scholarship search tools, this platform provides:

-  Real-time grant data from government sources  
-  AI-powered personalized matching  
-  Deadline-aware prioritization  
-  Application tracking system  

---

##  Problem Statement

There is no widely available, free, and comprehensive public API for scholarships or financial aid data, making it difficult to build a centralized discovery platform.

To solve this, our system uses publicly available data from **:contentReference[oaicite:1]{index=1}** as the primary source of funding opportunities. However, raw grant data is not personalized and can still overwhelm users.

The core challenge is:

> How can we use AI to transform raw government grant data into personalized recommendations that match students with opportunities they are actually qualified for, while helping them prioritize and manage applications effectively?

---

##  Objectives

- Allow students to build a detailed profile including:
  - Academic background (GPA, major, enrollment status)
  - Demographics and location
  - Career goals and interests
  - Financial need

- Integrate with **:contentReference[oaicite:2]{index=2}** to fetch real, up-to-date grant opportunities 

- Provide a searchable and filterable grant directory by:
  - Field of study
  - State/location
  - Deadline and eligibility

- Implement AI-powered matching to:
  - Analyze user profiles against grant requirements
  - Identify relevant opportunities
  - Estimate qualification likelihood

- Rank opportunities based on:
  - AI-generated fit score
  - Application deadline urgency

- Track application progress:
  - Saved
  - In Progress
  - Submitted
  - Awarded
  - Rejected

- Continuously surface new matching opportunities as they become available

- Ensure user privacy:
  - Secure authentication
  - Protected profile data
  - User-controlled account deletion

---

## System Architecture

### Core Entities

- **User** – authentication and account management  
- **Profile** – student data used for AI matching  
- **Scholarship/Grant** – funding opportunities from external sources  
- **Application** – tracks user engagement and status  

---

### Relationships

- A **User** owns one **Profile**
- A **User** can have many **Applications**
- A **Grant/Scholarship** can have many **Applications**

---

### Backend Design

- **Controllers** – REST API endpoints  
- **Services** – business logic layer  
- **Repositories** – database access via Spring Data JPA  
- **AI Layer** – matching and recommendation engine  

---

### AI Matching Flow

1. Retrieve user profile  
2. Fetch grant opportunities from database  
3. Build AI prompt using profile + grants  
4. Send request via AI client  
5. Parse and extract relevant matches  
6. Rank results by fit score and deadline priority  

---

## Core Features

### 👤 User & Profile Management
- Secure authentication system  
- Create, update, and delete user profiles  
- Store academic and personal information securely  

---

### Grant Discovery
- Search and filter funding opportunities  
- Filter by:
  - Field of study  
  - State  
  - Deadline  

---

###  AI-Powered Matching
- Personalized recommendations per user  
- AI evaluates eligibility based on:
  - Profile data  
  - Grant requirements  
- Ranked match results with relevance scoring  

---

### Application Tracking
- Track application lifecycle:
  - Saved  
  - In Progress  
  - Submitted  
  - Awarded  
  - Rejected  
- Add notes and deadlines per application  

---

###  Data Synchronization
- Fetch real-time grant data from **:contentReference[oaicite:3]{index=3}**
- Periodically refresh dataset to ensure up-to-date listings  
- No hardcoded opportunities  

---

##  Tech Stack

### Backend
- Java  
- Spring Boot  
- Spring Data JPA  
- REST APIs  

### AI Integration
- External AI API via `AIClient`  
- Prompt-based matching system  

### Database
- (Add your DB here: PostgreSQL )

### Frontend
- frontend framework: React 

---

## Getting Started

### Prerequisites
- Java 17+  
- Maven  
- Node.js  
- API key for AI service or a source to retrieve data


---

### Installation

```bash
git clone https://github.com/code-differently/26q1-team5.git


# Backend setup
mvn clean install
mvn spring-boot:run

Screenshots

Dashboard
Profile setup
AI match results
Application tracker