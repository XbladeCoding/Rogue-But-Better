# Rogue - Programming Assignment

## Overview

Design and implement an effective strategy to seek out and intercept a moving adversary. Design and implement an effective counter-strategy to avoid being intercepted by such an adversary.

---

## Historical Perspective

In the late 1970s, Ken Arnold designed a C library (later to be named **curses**) that enabled the user to put a character at a specific location on the screen, enabling a crude form of interactive graphics. While experimenting with this new library, Michael Toy and Glen Wichman designed a graphical adventure game coined [Rogue](http://www.hut.fi/~eye/roguelike/rogue.html).

The purpose of Rogue is *"to descend into the Dungeons of Doom, defeat monsters, find treasure, and return to the surface with the amulet of Yendor using its levitation capabilities."* This revolutionary game became the undisputed most popular game on college campuses after it was included in BSD Unix 4.2 in 1980. It would later spawn the role-playing game (RPG) genre, including its direct descendant NetHack.

In 1984, a group of grad students at CMU developed a computer program named [Rog-o-matic](http://www.cs.princeton.edu/~appel/papers/rogomatic.html) to automatically play the game of Rogue. At one time, the Rog-o-matic player was the highest rated Rogue player at CMU. A crucial component of the Rog-o-matic algorithm was to avoid contact with the monster for as long as possible, so that your health could regenerate before confronting the monster in a battle to the death. This involves an interesting graph search problem, and is the inspiration for this programming assignment.

---

## Rules of the Game

The game of Rogue is played on an **N-by-N grid** that represents the dungeon.

### Characters
- **Rogue** (typically a human player) is represented with the character `@`
- **Monster** is represented by an uppercase letter (A-Z, each with different capabilities)

### Turn System
The monster and rogue take turns making moves, with the **monster going first**. If the monster intercepts the rogue (occupies the same site), then the monster burns/freezes/bludgeons the rogue to death, and the game ends.

### Site Types
In each turn, a player either remains stationary or moves to an adjacent site. There are three types of sites:

| Site Type | Character | Description |
|-----------|-----------|-------------|
| Room      | `.`       | Open room space |
| Corridor  | `+`       | Passageway between rooms |
| Wall      | ` ` (space) | Impenetrable barrier |

### Movement Rules

**From a Room Site:**
- Can move to an adjacent room site in one of **8 compass directions** (N, E, S, W, NW, NE, SW, SE)
- Can move to a corridor site in one of **4 directions** (N, E, S, W)

**From a Corridor Site:**
- Can move to an adjacent room or corridor site in one of **4 directions** (N, E, S, W)

**Walls are impenetrable.**

### Example Dungeons

```
     + + + + +          . . . . . . . . A .
    +         +         . . . . . . . . . .
    . . . . .   +       . . . . . . . . . .
    . . . @ .   +       . . . . . . . . . .
    . . I . .   +       . . . . @ . . . . .
    . . . . .   +       . . . . . . . . . .
    . . . . .   +       . . . . . . . . . .
    +         +         . . . . . . . . . .
    +         +         . . . . . . . . . .
    + + + + +           . . . . . . . . . .
```

- In the first dungeon, the rogue can avoid the **Ice Monster (I)** indefinitely by moving N and running around the corridor.
- In the second dungeon, the **Aquator Monster (A)** can use diagonal moves to trap the rogue in a corner.

---

## Monster's Strategy

The monster is tenacious and its sole mission is to chase and intercept the rogue.

### Natural Strategy
A natural strategy for the monster is to always take one step toward the rogue. In terms of the underlying graph, this means that the monster should:
1. Compute a **shortest path** between itself and the rogue
2. Take one step along such a path

### Strategy Considerations
This strategy is not necessarily optimal, since there may be ties, and taking a step along one shortest path may be better than taking a step along another shortest path.

**Examples:**
- The **Bat Monster (B)**: The only optimal strategy may be to take a step in the NE direction. Moving N or E would enable the rogue to make a mad dash for the opposite corridor entrance.
- The **Centaur Monster (C)**: Can guarantee to intercept the rogue by moving E.

**Your first task is to implement an effective strategy for the monster.**

---

## Rogue's Strategy

The rogue's goal is to **avoid the monster for as long as possible**.

### Naive Strategies (and their weaknesses)

1. **Move to the farthest adjacent site from the monster's current location**
   - May lead to a quick and unnecessary death
   - Example: Against the **Jabberwock Monster (J)**, the rogue should move SE instead

2. **Go to the nearest corridor**
   - May also be deadly
   - Example: To avoid the **Flytrap Monster (F)**, the rogue must move towards a northern hallway

### More Effective Strategy

Identify a sequence of adjacent corridor and room sites which the rogue can run around in circles forever, thereby avoiding the monster indefinitely. This involves identifying and following certain **cycles** in the underlying graph.

Of course, such cycles may not always exist, in which case your goal is to **survive for as long as possible**.

---

## Input Format

The input dungeon consists of:
1. An integer **N**
2. Followed by **N rows** of **2N characters** each

### Example Input
```
10
    + + + + + + + +
    +             +
. . . . . . .     +
. . . . . . .     +
. . . . @ . .     +
. . B . . . .     +
. . . . . . .     +
. . . . . . . + + +
. . . . . . .      
. . . . . . .      
```

### Dungeon Rules
- A **room** is a contiguous rectangular block of room sites
- Because of dungeon construction safety requirements, rooms may **not connect directly** with each other
- Any path from one room to another will use **at least one corridor site**
- There will be exactly **one monster** and **one rogue**
- Each will start in some **room site**

---

## API Reference

### Site.java
A tiny data type that represents a location site in the N-by-N dungeon.

```java
public Site(int i, int j)           // instantiate a new Site for location (i, j)
public int i()                      // get i coordinate
public int j()                      // get j coordinate
public int manhattan(Site w)        // return Manhattan distance from invoking site to w
public boolean equals(Site w)       // is invoking site equal to w?
```

### Dungeon.java
Represents an N-by-N dungeon.

```java
public boolean isLegalMove(Site v, Site w)  // is moving from site v to w legal?
public boolean isCorridor(Site v)           // is site v a corridor site?
public boolean isRoom(Site v)               // is site v a room site?
public int size()                           // return N = dimension of dungeon
```

### Game.java
Reads in the dungeon from standard input and does the game playing and refereeing.

```java
public Site getMonsterSite()    // return the site occupied by the monster
public Site getRogueSite()      // return the site occupied by the rogue
public Dungeon getDungeon()     // return the dungeon
```

### Monster.java (Your Task)

```java
public Monster(Game g)    // create a new monster who is playing a game g
public Site move()        // return the adjacent site to which you are moving
```

### Rogue.java (Your Task)

```java
public Rogue(Game g)      // create a new rogue who is playing a game g
public Site move()        // return the adjacent site to which you are moving
```

---

## Deliverables

Submit your version of:
- `Monster.java`
- `Rogue.java`
- Any accompanying code
- `readme.txt`

**Justify your strategies, explaining their strengths and weaknesses.**

---

## Extra Credit

Submit some interesting dungeons that are useful for:
- Debugging
- Testing
- Developing strategies

Describe each dungeon and why it is useful or interesting. **Creativity will also be a factor.**

---

## Challenges for the Bored

There are limitless additional possibilities for creativity:

1. **Multiple Monsters**: Allow two monsters that can coordinate their attack
2. **Arbitrary Graphs**: Make it work on arbitrary graphs
3. **Fog of War**: In the real game, you don't know the whole graph until you start walking through it - account for this
4. **Item Collection**: In the real game, the rogue collects treasure, potions, and powerful weapons while traveling through the dungeon. Modify your strategy to maximize the amount of material collected, while still avoiding the monster.

---

## Credits

This assignment was developed by **Andrew Appel** and **Kevin Wayne**.

Copyright © 2004.

Source: [Princeton COS 226 - Rogue Assignment](https://introcs.cs.princeton.edu/java/assignments/rogue.html)