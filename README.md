# 🍰 Bake Box – Themed Snack Box Purchase System

A Spring Boot + HTML/CSS/JS based web application where users can purchase surprise snack boxes, view their purchase history, and interact with a themed UI featuring animations, music, and dark mode.

---

## 🌟 Features

### 🎁 Box Selection
- View themed snack boxes (e.g. Matcha Lover Box, Bakery Delight Box).
- Each box displays the items inside and their remaining quantities.
- Set quantity per box (limited to available item stock).
- Click sound feedback and animated interactions.

### 🎉 Box Opening Experience
- Purchases open a popup animation with confetti and sound.
- Items are revealed one-by-one with sparkle sound.
- Duplicate items are grouped using `×N` notation for clarity.
- Paginated summary popup grouped by box.

### 📜 Purchase History
- Access full purchase history with date and time.
- Items sorted newest-first.
- History page supports music and dark mode as well.

### 🌗 Dark Mode & 🎵 Music Toggle
- Persistent across pages via `localStorage`.
- Toggleable UI theme and background music on/off.

---

## 🛠️ Technologies Used

### Backend
- Java 21
- Spring Boot 3.4.4
- Spring Data JPA
- Spring Web
- H2 In-Memory Database
- Spring HATEOAS

### Frontend
- HTML, CSS (custom with pastel + dark themes)
- Vanilla JavaScript
- REST API integration with Fetch
- Audio & animation using native HTML5 and Canvas Confetti

---

## 🚀 How to Run

### Prerequisites
- Java 21+
- Maven

### Backend (Spring Boot)
```bash
cd bakebox
mvn spring-boot:run
