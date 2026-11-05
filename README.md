# Product Catalogue MVP - Android Client & Node.js Backend

A production-grade Product Catalogue MVP built with **Android Clean Architecture** (Jetpack Compose, Material 3, Dagger Hilt, Retrofit, Kotlin Coroutines/Flow) and an intermediary **Node.js Express Backend** proxying the Fake Store API.

## Tech Stack Selection & Justification

Per the challenge requirements, we chose the following combination:
- **Client**: Android app in **Kotlin** (Jetpack Compose & Material 3)
- **Backend**: Intermediary **Node.js** (JavaScript / Express)

### Why Node.js (JavaScript) Backend?
1. **Rapid Prototyping & Minimal Overhead**: Express enables setting up robust proxy routing and enquiry handling with zero heavy boilerplate, perfectly matching the 2–3 hour time-box.
2. **First-Class JSON Support**: Native JavaScript object handling makes parsing and transforming payloads from the external Fake Store API lightweight and efficient.
3. **Asynchronous Non-Blocking I/O**: Event-driven architecture handles concurrent API calls and sales inbox email simulations smoothly.
4. **AWS & Docker Compatibility**: Extremely small container memory footprint and fast startup times, making it ideal for AWS ECS, Lambda, or EC2 deployment.

### Why Android (Kotlin) Client?
1. **Modern Declarative UI**: Jetpack Compose and Material 3 deliver a responsive, fluid user experience with built-in edge-to-edge support.
2. **Robust Architecture**: Strict multi-module Clean Architecture (`:app`, `:domain`, `:repository`) ensures clear separation of concerns, testability, and scalability.
3. **Dependency Injection & Coroutines**: Dagger Hilt for lifecycle-aware DI and Kotlin Coroutines/Flow for reactive state management.

---

## Key Features & UI Enhancements
- **Home Screen**: Displays top 5 rated featured products, categories horizontal row, and search bar with instant filtering.
- **Search Experience**: Includes a convenient inline **Clear button** inside the search box to reset queries instantly.
- **Error Handling & Retry**: Robust connection error states featuring a **Retry** button for quick recovery when offline or unable to connect to the server.
- **Category & Product Details**: Seamless navigation to category product lists and individual product detail views.
- **Product Enquiry Flow**: Contact details submission form with validation and simulated email notification to the sales inbox ("Thank you" confirmation state).
- **Edge-to-Edge Design**: Fully respects system window insets (`navigationBarsPadding`) so action buttons are never obscured by the system navigation bar.

## Architecture & Modules
The solution follows a strict multi-module clean architecture:
- **`:app`**: Presentation layer (Jetpack Compose UI, ViewModels, Navigation Compose, Hilt DI).
- **`:domain`**: Business logic layer (Pure Kotlin module: Domain models, repository interfaces, use cases).
- **`:repository`**: Data layer (Retrofit DTOs, API service implementation, repository implementation, Hilt DI).
- **`/backend`**: Node.js intermediary server with comprehensive event logging middleware routing all client requests, proxying Fake Store API, and handling enquiry submissions with email simulation.

---

## Setup & Run Instructions (Under 10 Minutes)

### 1. Run the Node.js Backend
Prerequisites: Node.js installed.
```bash
cd backend
npm install
npm start
```
The server will run on `http://localhost:3000`.
Alternatively, run with Docker:
```bash
cd backend
docker compose up --build
```

### 2. Run the Android App
1. Open the project in Android Studio.
2. Ensure the Android Emulator is running (API 26+).
3. The Retrofit client is configured to connect to `http://10.0.2.2:3000/api/` (which maps to localhost:3000 on the host machine from the Android Emulator).
4. Build and run the app (`:app`).

---

## Configuration & Environment
- Backend environment variables template: [`backend/.env.example`](file:///Users/pradyumn/AndroidStudioProjects/AiApp/backend/.env.example)
- No paid services or accounts required.
