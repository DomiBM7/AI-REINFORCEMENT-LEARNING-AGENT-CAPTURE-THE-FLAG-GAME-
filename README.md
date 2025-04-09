# Capture The Flag AI Agent

This project implements a **Q-learning based AI agent** in a grid-based Capture The Flag environment using Java. The agent is trained to navigate obstacles, avoid enemies, collect power-ups, and reach the flag while learning optimal policies through reinforcement learning.

## 🧠 Features

- Grid-based 2D environment using Java Swing
- Reinforcement learning agent using Q-learning
- Environment elements:
  - `F` – Flag (goal)
  - `T` – Traps (negative reward)
  - `L` – Lifelines (restore health)
  - `B` – Bullets (obstacles)
  - `S` – Weapon boosts
- Dynamic learning vs execution phases
- Visualization of:
  - Agent movement and path
  - Q-values per state
  - Enemies and projectiles
- Bullet-shooting behavior from agent
- Health, ammo, and score tracking

## 📁 Project Structure

```bash
├── Agent.java              # Main Q-learning agent logic
├── Environment.java        # Grid setup and environment behavior
├── CaptureTheFlagGame.java # GUI and game rendering using Swing
├── Bullet.java             # Handles bullet position and movement
├── Enemy.java              # Handles enemy movement and logic
├── Main.java               # Entry point of the program
