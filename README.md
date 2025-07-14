# ☕ CafeHub Dashboard

A desktop-based Java Swing application for managing a simple café interface — including menu items, order tracking, quantity updates, and a live receipt panel.

---

## 🚀 Features

- 🖥️ Interactive Dashboard UI using **Java Swing**
- 📋 Menu panel displaying café items
- ➕➖ Increment/decrement item quantity using buttons
- 🔄 Real-time **database updates** using MySQL
- 🧾 Receipt panel that displays selected items and quantity
- 💰 Live total price calculation
- ✅ Clean, modular structure with OOP principles

---

## 🛠️ Tech Stack

- **Java Swing** – for building the GUI
- **MySQL** – to store item quantities and prices
- **JDBC** – for Java–MySQL connection
- **Maven / JDK 17+** – for project build & run (optional)

---

## 🗃️ Database Schema

```sql
CREATE TABLE cafe_items (
    name VARCHAR(50) PRIMARY KEY,
    quantity INT,
    price INT
);
