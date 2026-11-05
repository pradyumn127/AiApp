# Decisions & Assumptions - Product Catalogue MVP

## 1. Assumptions Made
- **Product Data Source**: Used Fake Store API (`https://fakestoreapi.com`) via the intermediary Node.js backend as permitted by the challenge guidelines.
- **Email Provider**: Implemented console logging / sales inbox simulation on the Node.js backend for enquiry submissions, avoiding paid external email services.
- **Featured Products**: Defined "top rated" featured products as the top 5 products sorted by rating rate descending.

## 2. Requirements Dropped or Simplified
- **Persistence / Caching**: Local Room database caching was omitted in favor of direct network fetch via repository for simplicity in the MVP, but repository interface pattern allows seamless addition of local caching later.
- **Authentication**: User accounts and login were omitted as the acceptance criteria focus strictly on catalogue browsing, search, product details, and enquiries.

## 3. How Design Supports Growth Beyond PoC
- **Strict Multi-Module Clean Architecture**: Clear separation between UI (`:app`), Business Rules (`:domain`), and Data/Networking (`:repository`) enables independent testing, modular feature scaling, and swappable data sources.
- **Intermediary Backend**: Routing all requests through Node.js ensures business logic, rate limiting, authentication token injection, and database migrations can be introduced without modifying the Android client.

## 4. What To Do Next (With Another Day)
- Add local caching with Room database and Offline-First repository synchronization.
- Implement comprehensive unit test suites for UseCases, ViewModels, and UI Compose tests.
- Add push notifications or real email dispatch integration (e.g. SendGrid / AWS SES) in the Node.js backend.
