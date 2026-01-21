# Pair Coding Plan: Rogue Game Implementation

> **Last Updated:** January 20, 2026

This document tracks our step-by-step implementation of the Rogue game simulation. We're building from the ground up, testing each piece before moving to the next.

---

## Progress Tracker

| Phase | Description | Status |
|-------|-------------|--------|
| 1 | Site.java - coordinates | ✅ Complete |
| 2 | Dungeon.java - grid basics | ✅ Complete |
| 3 | Dungeon.java - legal moves | ✅ Complete |
| 4 | Dungeon.java - get all moves | ✅ Complete |
| 5 | Game.java - basic setup | ✅ Complete |
| 6 | Game.java - display | ✅ Complete |
| 7 | Game.java - test dungeons | ✅ Complete |
| 8 | Monster.java & Rogue.java - placeholders | ✅ Complete |
| 9 | Game.java - game loop | ✅ Complete |
| 10 | Monster strategy (BFS) | ✅ Complete |
| 11 | Rogue.java - user control | ⬜ Not Started |
| 12 | Rogue strategy (escape) | ⬜ Not Started |

---

## What's Been Implemented

### Site.java ✅
- Position class with `(i, j)` coordinates
- `i()`, `j()` getters
- `equals()` for comparison
- `manhattan()` for distance calculation
- `toString()` for debugging

### Dungeon.java ✅
- 2D char grid storage
- `isRoom()`, `isCorridor()`, `isWall()` - site type checking
- `inBounds()` - boundary checking
- `isLegalMove()` - validates moves including diagonal rules
- `getLegalMoves()` - returns all valid moves from a site
- `display()` - prints the dungeon
- `getChar()` - gets character at a site

### Game.java ✅
- Stores dungeon, rogue position, monster position
- Parses input to find `@` (rogue) and `A-Z` (monster)
- `getRogueSite()`, `getMonsterSite()`, `getDungeon()` getters
- `setRogueSite()`, `setMonsterSite()` for position updates
- `display()` - shows game state with player positions
- `isGameOver()` - checks if monster caught rogue
- `play(monster, rogue, maxTurns)` - full game loop
- **5 Pre-defined test dungeons:**
  - `DUNGEON_SMALL` (5×5)
  - `DUNGEON_CORRIDORS` (7×7)
  - `DUNGEON_TWO_ROOMS` (8×8)
  - `DUNGEON_L_CORRIDOR` (10×10)
  - `DUNGEON_TRAP` (6×6)
- `fromTestDungeon()` - create game from test dungeon
- `fromUserInput()` - create game from console input

### Monster.java ✅
- **BFS (Breadth-First Search) Strategy implemented!**
- Finds shortest path to rogue
- Uses queue for level-by-level exploration
- Uses `visited[][]` to avoid revisiting sites
- Uses `parent[][]` to trace back the path
- Returns the first step toward the rogue

### Rogue.java ⏳
- Basic structure in place
- Currently just stays in place (placeholder)
- **Strategy not yet implemented**

---

## How the Game Works Now

```
1. Create a Game (from test dungeon or user input)
2. Create Monster and Rogue objects
3. Call game.play(monster, rogue, maxTurns)
4. Each turn:
   - Monster uses BFS to move toward rogue
   - Rogue stays in place (no strategy yet)
   - Check for collision → game over
5. Monster catches rogue in minimal moves!
```

### Example Output:
```
=== GAME START ===
 +++ 
+.@.+    ← Rogue
+...+
+.M.+    ← Monster
 +++ 

Turn 1: Monster moves (3,2) → (2,2)
Turn 2: Monster moves (2,2) → (1,2)
*** GAME OVER! Monster caught the rogue on turn 2! ***
```

---

## Next Steps to Complete the Simulation

### Phase 11: User-Controlled Rogue (Optional but Fun)
**Goal:** Let user manually control the rogue to test gameplay.

**Steps:**
1. Add a `UserControlledRogue` class or modify `Rogue.java`
2. Prompt user for direction input (N, S, E, W, NE, NW, SE, SW, STAY)
3. Validate the move is legal
4. If illegal, prompt again

**Why:** Lets you "play" the game and understand how the monster chases you.

---

### Phase 12: Rogue Escape Strategy
**Goal:** Implement an AI strategy for the rogue to survive as long as possible.

**Strategy Options (from simplest to most advanced):**

#### Option A: Simple "Run Away" Strategy
- Look at all legal moves
- Pick the one that **maximizes distance** from monster
- Easy to implement, but not optimal

#### Option B: "Look Ahead" Strategy
- For each possible move, simulate monster's response
- Pick the move that keeps rogue safest after monster moves
- Better than Option A

#### Option C: Cycle Detection Strategy (Best)
- Find cycles in the dungeon (loops rogue can run around)
- If rogue can reach a cycle, it can survive forever!
- Most complex but matches the assignment's goal

**Recommended approach:** Start with Option A, then upgrade to B or C.

---

### Phase 13: Final Testing & Polish
**Goal:** Make sure everything works together.

**Steps:**
1. Test on all 5 pre-defined dungeons
2. Test edge cases (rogue trapped in corner, etc.)
3. Add user input mode for custom dungeons
4. Clean up output formatting
5. Add option to choose rogue strategy (manual vs AI)

---

### Phase 14: Extra Credit Ideas (Optional)
From the assignment:
- Create interesting test dungeons
- Multiple monsters coordinating attacks
- Fog of war (don't know full map)
- Item collection while avoiding monster

---

## Files Overview

```
RogueButBetter/
├── Site.java       ✅ Position (i, j) with distance/equality
├── Dungeon.java    ✅ Grid, movement rules, legal moves
├── Game.java       ✅ Game state, loop, test dungeons
├── Monster.java    ✅ BFS chase strategy
├── Rogue.java      ⏳ Placeholder (needs escape strategy)
├── game.md         ✅ Assignment specification
└── implementation.md  ← You are here!
```

---

## How to Run

```bash
# Compile all files
javac Site.java Dungeon.java Game.java Monster.java Rogue.java

# Run the game
java Game

# Test individual components
java Site      # Test Site class
java Dungeon   # Test Dungeon class
java Monster   # Test Monster BFS
java Rogue     # Test Rogue (placeholder)
```

---

## Key Concepts Learned

| Concept | Where Used |
|---------|------------|
| 2D Arrays | Dungeon grid, visited[][], parent[][] |
| Classes & Objects | Site, Dungeon, Game, Monster, Rogue |
| ArrayList | `getLegalMoves()` returns list of sites |
| Queue (LinkedList) | BFS exploration in Monster |
| BFS Algorithm | Monster finding shortest path |
| Game Loop | `play()` method in Game |
| Encapsulation | private fields, public getters/setters |
