# Pair Coding Plan: Rogue Game Implementation

Great approach! Let's break this down into small, testable steps. We'll build from the ground up, testing each piece before moving to the next.

---

## Phase 1: Foundation - The Site Class

**Goal:** Create a simple class to represent a position (i, j) on the grid.

**Steps:**
1. Create `Site.java` with two integer fields for coordinates
2. Add a constructor that takes `(i, j)`
3. Add getter methods `i()` and `j()`
4. Add `equals()` method to compare two sites
5. Add `manhattan()` method for distance calculation
6. Add a `toString()` for easy debugging

**Test:** Write a `main` method in Site.java that:
- Creates a few sites
- Prints their coordinates
- Tests if two sites are equal
- Calculates Manhattan distance between them

---

## Phase 2: Reading the Dungeon - The Dungeon Class

**Goal:** Store and represent the N×N grid of characters.

**Steps:**
1. Create `Dungeon.java` with a 2D char array and size N
2. Add a constructor that takes N and the grid data
3. Add `size()` method to return N
4. Add `isRoom(Site v)` - checks if character is `.`
5. Add `isCorridor(Site v)` - checks if character is `+`
6. Add `isWall(Site v)` - checks if character is space
7. Add a method to display/print the dungeon

**Test:** Write a `main` method in Dungeon.java that:
- Hardcode a small 5×5 dungeon
- Create a Dungeon object
- Test `isRoom`, `isCorridor`, `isWall` on various sites
- Print the dungeon to console

---

## Phase 3: Movement Rules - Legal Moves

**Goal:** Implement the movement logic in Dungeon.

**Steps:**
1. Add `isLegalMove(Site v, Site w)` method
2. Handle the "stay in place" case (v equals w)
3. Check if destination is not a wall
4. If source is a **room**: allow 8 directions to room, 4 directions to corridor
5. If source is a **corridor**: allow only 4 directions (N, S, E, W)
6. Add helper method to check if move is diagonal

**Test:** Write a `main` method that:
- Creates a dungeon with rooms and corridors
- Tests various moves (valid and invalid)
- Tests diagonal moves from room vs corridor
- Print "LEGAL" or "ILLEGAL" for each test case

---

## Phase 4: Getting All Legal Moves

**Goal:** Given a site, return all possible sites a player can move to.

**Steps:**
1. Add `getLegalMoves(Site v)` method to Dungeon
2. Loop through all 8 possible neighbors
3. Use `isLegalMove` to filter valid ones
4. Return as an array or ArrayList of Sites

**Test:** Write a `main` method that:
- For a given site, print all legal moves
- Test from a room site (should have up to 8 moves)
- Test from a corridor site (should have up to 4 moves)
- Test from a corner (fewer moves)

---

## Phase 5: The Game Class - Bringing It Together

**Goal:** Create the Game class that manages the dungeon, rogue, and monster positions.

**Steps:**
1. Create `Game.java` with fields for Dungeon, rogue Site, monster Site
2. Add constructor that takes input (we'll hardcode for now)
3. Parse the input to find `@` (rogue) and uppercase letter (monster)
4. Add `getRogueSite()` and `getMonsterSite()` methods
5. Add `getDungeon()` method
6. Add `setRogueSite()` and `setMonsterSite()` for updating positions

**Test:** Write a `main` method that:
- Creates a Game with a hardcoded dungeon
- Prints the rogue and monster positions
- Prints the dungeon

---

## Phase 6: Display the Game State

**Goal:** Visualize the dungeon with current player positions.

**Steps:**
1. Add a `display()` method to Game
2. Print the grid, but substitute `@` at rogue's position
3. Substitute monster letter at monster's position
4. Add row/column numbers for debugging (optional)

**Test:** Write a `main` method that:
- Creates a Game
- Displays the initial state
- Manually moves rogue/monster
- Displays again to see the change

---

## Phase 7: Reading Input from User

**Goal:** Allow user to input a dungeon from console.

**Steps:**
1. Modify Game constructor to read from `Scanner` (System.in)
2. Read N (size)
3. Read N lines of 2N characters each
4. Parse and store the dungeon

**Test:** Write a `main` method that:
- Prompts user for dungeon input
- Creates the Game
- Displays the parsed dungeon
- Prints rogue and monster positions

---

## Phase 8: Basic Monster and Rogue Classes (No Strategy Yet)

**Goal:** Create placeholder classes that can make moves.

**Steps:**
1. Create `Monster.java` with constructor `Monster(Game g)`
2. Add `move()` method that returns the current site (stay in place for now)
3. Create `Rogue.java` with same structure
4. Both classes store reference to Game

**Test:** Write a `main` method in Game that:
- Creates Monster and Rogue
- Calls their `move()` methods
- Prints the returned sites

---

## Phase 9: Game Loop - Taking Turns

**Goal:** Implement the turn-based game loop.

**Steps:**
1. Add a `play()` method to Game
2. Loop: Monster moves first, then Rogue
3. Update positions after each move
4. Check for collision (monster catches rogue)
5. Display state after each turn
6. Add a turn counter
7. Add a maximum turn limit (to prevent infinite loops during testing)

**Test:** Write a `main` method that:
- Creates a Game
- Runs `play()` with "stay in place" monsters/rogues
- Verify the loop runs and displays correctly

---

## Phase 10: User-Controlled Rogue (Interactive Mode)

**Goal:** Let the user control the rogue manually for testing.

**Steps:**
1. Modify Rogue's `move()` to ask user for direction (N, S, E, W, etc.)
2. Read input and calculate new site
3. Validate the move is legal
4. If illegal, ask again

**Test:** Play the game manually:
- Move the rogue around
- Try illegal moves (into walls)
- See monster stay in place

---

## Next Steps (After Basics Work)

Once all the above is working, we'll implement:
- **Monster strategy:** BFS/shortest path to chase the rogue
- **Rogue strategy:** Escape logic, finding cycles

---

## Summary of Build Order

```
1. Site.java          ← coordinates
2. Dungeon.java       ← grid + isRoom/isCorridor
3. Dungeon.java       ← isLegalMove
4. Dungeon.java       ← getLegalMoves
5. Game.java          ← basic setup
6. Game.java          ← display
7. Game.java          ← user input
8. Monster.java       ← placeholder
   Rogue.java         ← placeholder
9. Game.java          ← game loop
10. Rogue.java        ← user control
```

---

## Progress Tracker

| Phase | Description | Status |
|-------|-------------|--------|
| 1 | Site.java - coordinates | ⬜ Not Started |
| 2 | Dungeon.java - grid basics | ⬜ Not Started |
| 3 | Dungeon.java - legal moves | ⬜ Not Started |
| 4 | Dungeon.java - get all moves | ⬜ Not Started |
| 5 | Game.java - basic setup | ⬜ Not Started |
| 6 | Game.java - display | ⬜ Not Started |
| 7 | Game.java - user input | ⬜ Not Started |
| 8 | Monster.java & Rogue.java - placeholders | ⬜ Not Started |
| 9 | Game.java - game loop | ⬜ Not Started |
| 10 | Rogue.java - user control | ⬜ Not Started |
| 11 | Monster strategy (BFS) | ⬜ Not Started |
| 12 | Rogue strategy (escape) | ⬜ Not Started |
