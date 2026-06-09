# Main Class

This is the Main class. This program was made using JDK 25.
You can use ./gradlew to build and run the program.

To see where the App is drawn, look inside [./game](./game), 
while the Rendering process is inside [./render](./render).

The Backtracking algorithm can be found inside [./maze/solver](./maze/solver).

# Tile Reference

| ID  | Tile        | Collision | Description                                                                                                                                                                 |
|-----|-------------|-----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `0` | Air         | ❌         | Empty space.                                                                                                                                                                |
| `B` | BushWall    | ✅         | Solid obstacle.                                                                                                                                                             |
| `S` | Spike       | ❌         | Deals **5 damage** when stepped on.                                                                                                                                         |
| `H` | Hole        | Dynamic   | Before touched: no collision. <br>On first touch: deals **5 damage** and becomes a solid obstacle.                                                                          |
| `K` | Key         | ❌         | Increases `keyCount`. <br>Collected key disappears. <br>Used to unlock `L` tiles and is consumed after use.                                                                 |
| `P` | Player      | -         | Win condition: collect all `F` and `L` flags. <br>Optimal path: highest remaining HP. <br>Lose condition: HP reaches 0.                                                     |
| `N` | Ninja       | ✅         | Deals **5 damage**. <br>Attack range: **3×3**. <br>Attacks immediately when player enters range, then every **2 tile counts** while player remains inside range.            |
| `R` | Fire        | ❌         | Deals **5 damage** when stepped on.                                                                                                                                         |
| `W` | Wizard      | ✅         | Deals **10 damage**. <br>Attack range: **5×5**. <br>Attacks immediately when player enters range, then every **4 tile counts** while player remains inside range.           |
| `M` | FireMonster | ✅         | Deals **20 damage**. <br>Attack range: **6×5**. <br>Attacks immediately when player enters range, then waits until player leaves and re-enters. <br>Occupies **2×1 tiles**. |
| `F` | FlagGreen   | ❌         | Must collect all green flags to win.                                                                                                                                        |
| `f` | FlagRed     | ❌         | Treated as a green flag by the backtracking algorithm, but not required for victory.                                                                                        |
| `L` | FlagLocked  | ❌         | Same as green flag, but requires a key to collect.                                                                                                                          |
| `s` | SpeedSpell  | ❌         | Grants **2× movement speed** for **20 tile counts**. <br>Tile count increases every **2 tiles walked** instead of every tile.                                               |
| `p` | PoisonSpell | ❌         | Deals **1 damage per tile count** for **10 tile counts**. <br>Picking up another poison refreshes duration to 10 but does not stack damage.                                 |
| `h` | HealSpell   | ❌         | Restores **20 HP**.                                                                                                                                                         |
| `E` | Elf         | ✅         | Heals player to **full HP**. <br>Has a **3×3 range**.                                                                                                                       |
| `O` | Portal      | ❌         | Teleports player to its connected portal (`O1 ↔ O1`, `O2 ↔ O2`, etc.).                                                                                                      |