# AI Usage Notes - Product Catalogue MVP

## 1. Tools and Models Used
- AI-assisted agentic coding environment within Android Studio.
- Models utilized for architectural design, code generation, multi-module structuring, and runtime debugging.

## 2. Agent Instruction Files
- Implemented and followed strict multi-module Android Clean Architecture patterns, Jetpack Compose Material 3 best practices, and Node.js Express backend standards.

## 3. Key Prompts that Shaped the Solution
- "Create a strict 3-module Clean Architecture structure (`:app`, `:domain`, `:repository`) in Kotlin with Hilt, Retrofit, and Coroutines."
- "Build an intermediary Node.js Express backend proxying Fake Store API with product catalog endpoints and enquiry email simulation."
- "Implement Jetpack Compose UI screens for Home (featured products & categories), Category list, Product Details, and Enquiry submission with success state."
- "Add comprehensive event logging in server.js and fix enquiry form navigation arguments & button padding."

## 4. AI Errors Caught & Corrected
- **Gradle Version Catalog Aliases**: Ensured explicit dependency strings and robust plugin configurations were used to avoid accessor mismatches between modules.
- **Emulator Networking**: Configured cleartext traffic (`usesCleartextTraffic="true"`) and `10.0.2.2:3000` base URL for Android Emulator communication with the Node.js backend.
- **Hilt Integration**: Configured custom `@HiltAndroidApp` Application class (`AiApplication`) in `AndroidManifest.xml`.
- **Edge-to-Edge Insets**: Added `navigationBarsPadding` to the product details enquiry button to prevent overlap with the system navigation bar.
- **Navigation Arguments**: Correctly extracted `productId` and `productTitle` from `backStackEntry` to ensure enquiry submissions pass valid product IDs.
