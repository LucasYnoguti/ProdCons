# 🧵 Producer–Consumer Project (Java)

This project was developed for the **Concurrent Programming** course and presents a progressive exploration of the classic **Producer–Consumer synchronization problem** in Java.

Through six versions (`v1` to `v6`), the project evolves from basic monitor-based synchronization to advanced concurrency patterns, showcasing different Java synchronization mechanisms and increasing levels of complexity.

---

## 📌 Problem Overview

The **Producer–Consumer problem** (also known as the *bounded-buffer problem*) models the interaction between two types of threads:

- **Producers** generate data and insert it into a shared buffer.
- **Consumers** remove and process data from the buffer.

The core challenge is ensuring correct synchronization so that:
- Producers do **not** insert data into a **full** buffer.
- Consumers do **not** remove data from an **empty** buffer.
- No race conditions, deadlocks, or starvation occur.

---

## 📂 Project Structure

The project is organized by versions, each located in its own package:
```
src/prodcons/
├── v1/
├── v2/
├── v3/
├── v4/
├── v5/
└── v6/
```

Each version contains a **complete and independent implementation**, composed of:

- **`IProdConsBuffer.java`**  
  Interface defining the buffer operations.

- **`ProdConsBuffer.java`**  
  Concrete buffer implementation with version-specific synchronization logic.

- **`Producer.java`**  
  Producer thread logic.

- **`Consumer.java`**  
  Consumer thread logic.

- **`Message.java`**  
  Data structure exchanged between producers and consumers.

- **`TestProdCons.java`**  
  Main class used to execute and test the concurrent system.

---

## 🧩 Version Breakdown

### 🔹 v1 — Basic Monitors
**Concepts:** `synchronized`, `wait()`, `notifyAll()`

- Uses Java’s built-in monitor mechanism.
- Producers block when the buffer is full.
- Consumers block when the buffer is empty.
- Establishes the foundational solution.

---

### 🔹 v2 — Graceful Termination
**Concepts:** Active Producers, termination signaling

- Introduces the concept of **active producers**.
- Consumers terminate gracefully once all producers finish.
- Prevents indefinite blocking and program hangs.

---

### 🔹 v3 — Semaphores (Classic Dijkstra Solution)
**Concepts:** `Semaphore`, mutual exclusion, resource counting

- Replaces monitors with semaphores:
    - `notFull`
    - `notEmpty`
    - `mutex`
- Explicit management of buffer capacity.
- Canonical bounded-buffer solution using semaphores.

---

### 🔹 v4 — Explicit Locks and Conditions
**Concepts:** `ReentrantLock`, `Condition`

- Uses `java.util.concurrent.locks` instead of `synchronized`.
- `Condition` variables replace `wait()` / `notify()`.
- Enables fine-grained signaling and reduces unnecessary wake-ups.

---

### 🔹 v5 — Bulk Consumption (Multiple Get)
**Concepts:** Atomic operations, consumer coordination

- Extends the buffer API with `get(int k)`.
- Allows consumers to atomically retrieve multiple messages.
- Introduces a `consumerMutex` to prevent interleaving between bulk requests.
- Ensures consistency and correctness during batch consumption.

---

### 🔹 v6 — Broadcast / Multicast Messages
**Concepts:** Barrier synchronization, advanced coordination

- Producers can insert messages with multiple copies (`nCopies`).
- A message remains in the buffer until **N different consumers** consume it.
- Implements barrier-like synchronization:
    - Producers block using `waitUntilFinished`.
- The **last consumer**:
    - Frees the buffer slot.
    - Signals the producer to resume execution.
- Demonstrates complex coordination patterns between threads.

---

## 🎯 Learning Outcomes

This project demonstrates:

- Multiple synchronization strategies in Java
- Trade-offs between monitors, semaphores, and explicit locks
- Correct handling of thread termination
- Advanced coordination and barrier synchronization
- Incremental and structured concurrent system design

---

## 🚀 Technologies & Concepts

- Java Threads
- `synchronized`, `wait()`, `notifyAll()`
- `Semaphore`
- `ReentrantLock` and `Condition`
- Barrier synchronization
- Concurrent system design

---

