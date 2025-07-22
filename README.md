# 🕹️ Code Hunter - JavaFX Educational Game

**Code Hunter** is an engaging 2D educational game built with **JavaFX**. The game combines learning and fun through two exciting modes: **Reading Mode** and **Gaming Mode**. It features user authentication, secure login/signup, and multiplayer/team-up options, creating an immersive and rewarding coding adventure.

---

## 👥 Contributors

###🔹 Hamim
📚 Reading Mode:
Designed and implemented the entire reading experience — including interactive tutorials, multi-slide navigation, and integrated coding quizzes to assess player knowledge.

🧩 Gaming Mode Integration:
Developed the logic to unlock and transition between Reading and Gaming Modes based on the user’s knowledge level.

🌐 Multiplayer Mode (Socket Handling):
Implemented real-time multiplayer gameplay using Java Sockets and multi-threading. Handled server-client architecture for player synchronization, team-up coding challenges, and communication flow.

###🔸 Atika Amin
🕹️ Gaming Mode (Single Player):
Built the core gameplay mechanics — player movement, map transitions, enemy AI, item collection, and cursed box behavior.

🗺️ Game Map Design:
Designed and structured all 2D Tiled maps for the game environment, including roads, terrains, buildings, obstacles, and level zones. Integrated path restrictions and logic zones using Tiled map properties.

👾 Player & Enemy Handling:
Developed player animation, controls, collision detection, and enemy patrol behavior. Managed enemy-player interaction logic (lives lost, hints triggered, etc.).

🤝 Multiplayer Map & Game Handling:
Designed the dedicated multiplayer maps and coordinated enemy handling logic for synchronized gameplay across clients. Focused on cooperative/competitive map interactions and balance.

---

## 📋 Table of Contents
- [Features](#features)
- [Game Modes](#game-modes)
- [Extra Features](#extra-features)
- [Authentication](#authentication)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Credits](#credits)

---

## ✨ Features

- User login/signup with secure authentication
- Reading Mode with books and coding quizzes
- Unlocking levels based on knowledge
- Single Player treasure hunt with code challenges
- Multiplayer coding battle or team-up quests
- Character switching and power-ups
- Instructor hints and cursed box mechanics
- Earn points and knowledge to level up

---

## 🧠 Game Modes

### 📖 Reading Mode
- Players read coding books/tutorials.
- Attend quizzes to test knowledge.
- Gain **Knowledge Level** to unlock gaming levels.

### 🎮 Gaming Mode
#### 🔹 Single Player
- Explore the 2D map to find treasures.
- Face enemies and cursed boxes.
- Solve coding problems to unlock treasure and earn **points**.
- Unlock higher game levels based on your knowledge level.
- Meet the **Instructor** for solving hints.

#### 🔸 Multiplayer
- Compete against others or
- Team-up to solve time-bound code challenges.
- Real-time communication with players via sockets.

---

## 🌟 Extra Features

- 🔐 **Authentication System** (Login/Signup with validation)
- 🔊 **Audio Communication** (voice chat in team mode - optional/future)
- ⌨️ **Typing Practice** mini-game
- 🧙‍♂️ **Instructor Hints** in maps
- 🧍 **Character Customization**
- 🔋 **Power-ups** using earned points
- 📊 **Progress Tracking** (knowledge, points, levels)

---

## 🔐 Authentication

- JavaFX login/signup interface
- Passwords stored securely using hashing
- MySQL database for storing user information
- Validations for:
  - Empty fields
  - Existing usernames/emails
  - Incorrect credentials
- Session state management after login

---

## 🛠️ Technologies Used

- Java 17+  
- JavaFX 21 (UI and game scenes)  
- FXML (UI design with Scene Builder)  
- MySQL (database)  
- JDBC (Java Database Connectivity)  
- CSS (JavaFX UI Styling)  
- Sockets & Threads (Multiplayer Mode)





