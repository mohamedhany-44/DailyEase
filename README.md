# **DailyEase – Advanced Daily Tasks & Habits Module**

### *Android · Kotlin · Jetpack Compose*

![Android](https://img.shields.io/badge/Android-Compose-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blueviolet)
![Status](https://img.shields.io/badge/Status-Active-green)

---

## **Overview**

**DailyEase** is a modern productivity toolkit powered by **Jetpack Compose**, featuring both an **Advanced Tasks System** and a complete **Daily Habits Dashboard**.

This module includes habit tracking, analytics, smooth animations, and clean MVVM architecture, making it ready for integration into any Android application.

---

## **Features**

---

## 📝 **Daily Task Management**

* Add tasks (title, description, time, priority, repeat)
* Edit existing tasks
* Delete with confirmation dialog
* Mark tasks complete/incomplete
* View full details via BottomSheet
* Sorting & Filtering by time, priority, or completion

---

## 🔁 **Habits Dashboard (NEW)**

A complete habit-tracking experience:

### **📌 Habit List**

* Display:

  * Habit name
  * Daily target (e.g., 3/5)
  * Progress bar
  * **(+1)** progress button
* Smooth progress animations

### **➕ Add Habit / ✏️ Edit Habit**

* Dialogs with fields:

  * Name
  * Daily goal
  * Icon picker (predefined icons)

### **🗑 Swipe to Delete**

* Material swipe gesture with delete reveal animation

### **🔄 Reset System**

* Reset daily progress to 0
* Shows a confirmation Toast
* UI updates with animation

---

## 📊 **Analytics Mini Dashboard**

* Daily completion percentage
* Best habit of the day
* Streak counter
---

## 🎨 **Modern UI & UX**

* Material 3 components
* Smooth animations & transitions
* Progress micro-interactions
* Clean layout aligned with the existing app theme
* Responsive across device sizes

---

## 🧩 **Architecture & Data Layer**

* **MVVM architecture**
* **ViewModel + State**
* Managed using:

  * `MutableStateList` or `ArrayList` (in-memory)
* No database (placeholder for future storage)
* Ready for Room/DataStore integration

---

## **Tech Stack**

* **Kotlin**
* **Jetpack Compose**
* **Material 3**
* **ViewModel / State**
* **Navigation Compose**
* **Coroutines**

---

## 🚀 **How to Run**

```bash
git clone https://github.com/your-username/DailyEase.git
```

1. Open in **Android Studio Hedgehog or newer**
2. Sync Gradle
3. Run on an emulator or physical device
