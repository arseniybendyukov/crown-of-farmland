# Crown of Farmland

A text-based Java strategy game played on a 7x7 board. Two teams place, move, reveal, merge, and sacrifice units in order to defeat the opposing team and take control of Farmland.

Originally developed as a university final assignment in WS 2025/26, then refined for this repository.

## Game Elements

### Board

The game is played on a 7x7 board. Columns are labeled `A` to `G`, rows are labeled `1` to `7`, and `A1` is the bottom-left field. At any time, a field can contain at most one unit.

- own team: `x` or `X`
- opposing team: `y` or `Y`
- Farmer King: `X` or `Y`
- other units: `x` or `y`
- blocking: suffix `b`
- unit can still move in the current turn: prefix `*`

The game supports two board styles: the standard symbol set and a custom symbol set. If a board symbol file is provided at startup, the custom symbol set is used; otherwise, the standard symbol set is used.

At startup, the output modes `all` and `compact` can be selected. Without an explicit value, `all` is used. In `compact` mode, the lines that contain only connector symbols are omitted.

#### Standard board
```text
  +---+---+---+---+---+---+---+
7 |   |   |   | Y |   |   |   |
  +---+---+---+---+---+---+---+
6 |   |   |   |   |   |   |   |
  +---+---+---+---+---+---+---+
5 |   |   |   |   |   |   |   |
  +---+---+---+---+---+---+---+
4 |   |   |   |   |   |   |   |
  +---+---+---+---#===#---+---+
3 |   |   |   |   N   N   |   |
  +---+---+---+---#===#---+---+
2 |   |   |   |   |   |   |   |
  +---+---+---+---+---+---+---+
1 |   |   |   |*X |   |   |   |
  +---+---+---+---+---+---+---+
    A   B   C   D   E   F   G
```

#### Custom board example
```text
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───╆━━━╅───┼───┤
3 │   │   │   │   ┃   ┃   │   │
  ├───┼───┼───┼───╄━━━╃───┼───┤
2 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │*X │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
```

#### Compact board (without rows)
```text
7 |   |   |   | Y |   |   |   |
6 |   |   |   |   |   |   |   |
5 |   |   |   |   |   |   |   |
4 |   |   |   |   |   |   |   |
3 |   |   |   |   N   N   |   |
2 |   |   |   |   |   |   |   |
1 |   |   |   |*X |   |   |   |
    A   B   C   D   E   F   G
```

### Units

Units are the main actors of the game.

During a turn, each unit may normally move once by at most one field up, right, down, or left. Diagonal movement is not allowed. Moving to the same field is allowed and still counts as movement.

If a unit moves onto:
- an enemy unit, a duel starts,
- a friendly unit, a merge starts.

A unit may also start or end a block as long as it has not already moved in the current turn. Blocking lasts until the unit moves again. Starting or ending a block counts as movement.

Each unit has a non-negative attack value and a non-negative defense value (**ATK** and **DEF**, collectively referred to as stats), which determine the outcome of a duel.  In addition, each unit has a two-part name consisting of a **qualifier** and a **role**. In program output, these two parts are always separated by a space.

Newly placed units are hidden at first, meaning the opposing team cannot see their name or stats. A unit may be flipped by its own team if it has not moved in the same turn. Flipping does not count as movement. Once flipped, a unit cannot become hidden again. When a duel starts, both participating units are flipped.

### Farmer King

Each team has a special unit called the **Farmer King**.

- Team 1 starts with its Farmer King on `D1`.
- Team 2 starts with its Farmer King on `D7`.
- Farmer Kings are revealed from the start.
- If a Farmer King moves onto a field occupied by a friendly unit, that unit is removed from the game.
- A Farmer King cannot move onto a field occupied by an enemy unit (cannot initiate a duel).
- A Farmer King cannot block.
- A Farmer King has no `ATK` or `DEF` value.
- In program output, its name is always `Farmer King`.

### Duels

A duel happens when an **attacking** unit `A` moves onto a **defending** unit `B` or onto a Farmer King.

There are three cases:

#### `B` is blocking
- `A` wins only if `ATK_A > DEF_B`.
- If `A` wins, `B` is removed.
- If `ATK_A < DEF_B`, the attacking team takes damage equal to `DEF_B - ATK_A`.
- If `ATK_A = DEF_B`, no team takes damage and no unit is removed.
- If `A` does not win, it stays on its original field.

#### `A` attacks a Farmer King
- The defending team takes damage equal to `ATK_A`.
- `A` stays on its original field.

#### Standard duel
- If `B` is not blocking, the unit with the higher attack value wins.
- The losing team takes damage equal to `|ATK_A - ATK_B|`.
- The losing unit is removed.
- If `ATK_A = ATK_B`, both units are removed.

### Hand

Each team has a hand of up to 5 units.

- At the start of the game, each team draws 4 units from its own deck.
- At the start of each of its turns, a team draws one more unit.
- During a turn, a team may place units from its hand at most once.
- Placement is only allowed on one of the up to eight fields adjacent to the team's Farmer King, including diagonals.
- Only units from the hand can be placed on the board.
- Placed units are removed from the hand.

If a team places a unit while already having five of its own non-king units on the board, the newly placed unit is immediately removed from the game.

It is illegal to place a unit on a field occupied by an enemy unit or the enemy Farmer King.

### Deck

Each team has its own deck of 40 units.

- Units are always drawn from the top of the deck.
- If a team ends its turn with five cards in hand, it must discard one unit from its hand and remove it from the game.
- At the start of each turn, the team draws one new unit.
- If a team cannot draw because its deck is empty, it loses the game.

### Life Points

Both teams start with `8000 LP`.

If a team's life points drop to `0`, that team loses.

### End of the Game

The game ends immediately when one team wins.

### Merging

Units may merge if they are compatible.

A merge is triggered when:
- a unit moves onto a field occupied by a friendly unit, or
- a unit from the hand is placed onto a field occupied by a friendly unit.

Merged units:
- become a new single unit,
- cannot split again,
- still count as one unit,
- may continue moving in the current turn if the merged unit was created by movement.

If an attempted merge is incompatible, the merge fails and the unit that was already on the target field is removed from the game.

The merged name is built from:
- the qualifier of the unit already on the target field,
- the qualifier of the moving or placed unit,
- the role of the unit already on the target field.

Examples:
- `Harvest Keeper` + `Iron Scout` -> `Iron Harvest Scout`
- `Amber Warden` + `Thorn Herald` -> `Thorn Amber Herald`

If at least one of the merged units was hidden, the merged unit is hidden as well.

Merged units may merge again later if they remain compatible with other units.

### Compatibility

Units with the **same name** can never merge.

Let `g := max{gcd(ATK_A, ATK(B)), gcd(DEF_A, DEF(B))}`.



Compatibility is then checked in the following order:

| Type | Condition | Resulting `ATK` | Resulting `DEF` |
|---|---|---:|---:|
| **Symbiotic** | `ATK_A = DEF_B` and `ATK_B = DEF_A` | `ATK_A` | `DEF_B` |
| **Conspirative** | `g > 100` | `ATK_A + ATK_B - g` | `DEF_A + DEF_B - g` |
| **Prime** | `g = 100` and either `ATK_A / 100` and `ATK_B / 100` are both prime, or `DEF_A / 100` and `DEF_B / 100` are both prime | `ATK_A + ATK_B` | `DEF_A + DEF_B` |
| **Incompatible** | none of the conditions above apply | – | – |

### Available Units and Deck Composition

The list of available units and the deck composition for both teams are loaded from input files during program startup.

## Opponent Bot

The game includes a simple computer-controlled opponent that plays full turns on its own. Its behavior is based on a rule-driven evaluation of the current game state rather than advanced search or learning.

During its turn, the bot evaluates legal Farmer King moves, possible placement fields, units in hand, and available unit actions such as moving, attacking, merging, blocking, or staying in place. When several options are similarly suitable, it uses **weighted random selection** to avoid fully deterministic behavior. In some cases, such as discarding from a full hand, it uses **reverse-weighted selection** to make stronger units less likely to be removed.

This keeps the opponent predictable enough to understand, while still allowing for some variation between games.

For a more detailed description of the bot logic, scoring rules, and random selection mechanisms, see the [detailed enemy README](./src/de/bendyukov/farm/models/enemy/README.md).


## Winning Conditions

A team loses if its **life points (LP)** drop to `0`.

A team also loses if it must draw a card but its **deck is empty**.


## Program Start

### Usage

```bash
java -jar <Main> [<key>=<value>...]
```

The game is initialized from key-value arguments.

### Supported Arguments

| Key | Type | Meaning |
| --- | --- | --- |
| `seed` | integer | Seed for the random generator |
| `board` | path | Optional board symbol file |
| `units` | path | Available unit definitions |
| `deck` | path | Deck composition for both teams |
| `deck1` | path | Deck composition for team 1 |
| `deck2` | path | Deck composition for team 2 |
| `team1` | string | Team 1 name, up to 14 characters |
| `team2` | string | Team 2 name, up to 14 characters |
| `verbosity` | string | Output mode: `all` or `compact` |

### Rules for Arguments

- `seed` and `units` are required.
- You must use either:
  - `deck`, or
  - `deck1` and `deck2`.
- `deck` cannot be combined with `deck1` or `deck2`.
- `verbosity` is optional.
- If `team1` is missing, the default name is `Player`.
- If `team2` is missing, the default name is `Enemy`.
- No key may appear more than once.
- Arguments may be passed in any order, but they are processed in the fixed order defined by the specification.

Before validation, the content of the provided files is printed unchanged. If a path is missing, a file contains invalid data, an integer is invalid, or any other argument is malformed, the program terminates with an error before processing any later arguments.

### Board Symbol File

The optional board symbol file defines a custom board rendering.

- It must contain exactly 29 characters.
- All 29 characters must appear on a single line.
- No separators are allowed.
- UTF-8 encoded files are supported.

If no board symbol file is provided, the standard board rendering is used.

### Units File

The units file defines up to 80 available units.

Each line contains one unit in this format:

```text
Qualifier;Role;ATK;DEF
```

Rules:
- one unit per line,
- fields are separated by semicolons,
- lines must not end with an extra semicolon,
- no extra whitespace is allowed,
- unit names do not contain semicolons,
- program output prints qualifier and role with a space in between.

If a line has invalid data types or the wrong number of fields, startup fails.

### Deck File

A deck file defines how many copies of each unit are used in a deck.

Rules:
- one non-negative integer per line,
- line order matches the order of the unit definitions,
- `0` means the unit is not included,
- the number of lines must match the number of defined units,
- the total number of units in each deck must be exactly `40`.

If one shared `deck` file is used, both teams receive the same deck composition. If `deck1` and `deck2` are used, each team gets its own composition.

## Commands Guide

After startup, the game accepts commands through standard input. Command names and arguments are case-insensitive.

### Placeholder Definitions

- `<atk>`: attack value of the affected unit
- `<bc>`: number of units of the affected team on the board (**board count**), excluding the Farmer King
- `<board>`: the output of the `board` command
- `<damage>`: damage to the life points of the attacking or defending team resulting from a duel
- `<dc>`: size of the affected team's deck (**deck count**)
- `<def>`: defense value of the affected unit
- `<field>`: a field identifier from `A1` to `G7`
- `<idx>`: 1-based index of units in the hand
- `<lp>`: remaining life points of the affected team
- `<name>`: name of the affected unit, or `???` if the unit is hidden and belongs to the team that is not currently taking its turn
- `<padding>`: a sequence of spaces used for alignment
- `<show>`: the output of the `show` command
- `<team>`: name of the affected team

### `select <field>`

Selects a field on the board. Afterwards, the board and the current selection are shown.

Selecting a field is a prerequisite for several other commands. The previous selection is removed if necessary. In the following, the phrase ****selected unit**** refers to the unit located on the selected field.

### `board`

Displays the game board on the console.

### `move <field>`

Moves the **selected unit** to the specified field. The target field must be at most one field away horizontally or vertically; staying in place is also allowed. This may trigger a merge or a duel. If the unit is currently blocking, the block may end.

### `flip`

Flips the **selected unit**. Afterwards, the board and the current selection are shown. Flipping does not count as movement.

### `block`

Starts a block for the **selected unit**. Afterwards, the board and the current selection are shown. Starting a block counts as movement.

### `show`

Shows information about the **selected unit**. If the selected field is empty, that is shown as well.

If the unit belongs to the team that is not currently taking its turn and the unit is hidden, its name, `ATK`, and `DEF` are shown as `???`.

### `hand`

Shows a numbered 1-based list of the units currently in the active team's hand.

### `place <idx> [<idx> [<idx> ...]]`

Places one or more units on the selected field. Afterwards, the board and the current selection are shown.

If more than one index is given, each index refers to the state of the hand before placing. The specified units are placed onto the field in the order in which their indices are given. This may trigger merges, including with a unit already on the selected field.

Placing is illegal if:

- the team has already placed units in the current turn
- the target field is occupied by an opposing unit

### `state`

Shows the current game state, consisting of:

- both team names
- both teams' life points
- both deck counts
- both board counts, excluding the Farmer Kings
- the board
- the contents of the selected field, if a field is selected

### `yield [<idx>]`

Ends the current turn and clears the selected field.

If an index `<idx>` is provided, the `<idx>`-th unit in the hand is discarded from the hand and removed from the game.

If the deck of the team whose turn starts next is empty and that team therefore cannot draw, that team loses the game.

The command is illegal if:

- the team whose turn it currently is has five units in hand and no index is given
- an index is given and the team whose turn it currently is has fewer than five units in hand

If the command is illegal, the same team remains active. Until the end of that turn, only `hand` and `yield` may be used. In particular, no units may be placed or moved anymore.

### `quit`

Terminates the program.


## Example interaction

```
%> java Main seed=8715026 deck=input/deck_custom.txt units=input/units_custom.txt board=input/board_boxes.txt verbosity=all
┌┐└┘┬┤┴├─│┼┏┓┗┛┱┲┩┪┹┺┡┢━┃╃╄╅╆
Harvest;Keeper;200;600
Ember;Scout;500;300
Moss;Warden;400;700
Flint;Sower;700;500
Willow;Guard;600;800
Copper;Miller;900;400
Thorn;Rider;800;900
Frost;Herald;1000;600
Cinder;Builder;300;1300
Oak;Forager;1100;700
River;Smith;1200;900
Storm;Brewer;1000;1200
Iron;Sentinel;1300;1000
Amber;Weaver;900;1500
Granite;Driver;1500;1100
Meadow;Seer;1400;1400
Ashen;Marshal;1600;1300
Silver;Planner;1700;1500
Raven;Captain;1800;1600
Golden;Engineer;2000;1700
Wildroot;Architect;2100;1900
Moonlit;Harvester;2300;2100
Sunforge;Overseer;2600;2400
Starfall;Champion;2900;2800
2
3
1
2
2
1
3
1
2
2
1
2
2
1
2
1
2
1
2
1
2
1
2
1
Use one of the following commands: select, board, move, flip, block, hand, place, show, yield, state, quit.
>hand
[1] Granite Driver (1500/1100)
[2] Meadow Seer (1400/1400)
[3] Copper Miller (900/400)
[4] Ember Scout (500/300)
[5] Ember Scout (500/300)
>select D1
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │   │   │   │   │
  ├───┼───┼───╆━━━╅───┼───┼───┤
1 │   │   │   ┃*X ┃   │   │   │
  └───┴───┴───┺━━━┹───┴───┴───┘
    A   B   C   D   E   F   G
Player's Farmer King
>move D2
Farmer King moves to D2.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───╆━━━╅───┼───┼───┤
2 │   │   │   ┃ X ┃   │   │   │
  ├───┼───┼───╄━━━╃───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Player's Farmer King
>select D3
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───╆━━━╅───┼───┼───┤
3 │   │   │   ┃   ┃   │   │   │
  ├───┼───┼───╄━━━╃───┼───┼───┤
2 │   │   │   │ X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
<no unit>
>place 2
Player places Meadow Seer on D3.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───╆━━━╅───┼───┼───┤
3 │   │   │   ┃*x ┃   │   │   │
  ├───┼───┼───╄━━━╃───┼───┼───┤
2 │   │   │   │ X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Meadow Seer (Team Player)
ATK: 1400
DEF: 1400
>block
Meadow Seer (D3) blocks!
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───╆━━━╅───┼───┼───┤
3 │   │   │   ┃ xb┃   │   │   │
  ├───┼───┼───╄━━━╃───┼───┼───┤
2 │   │   │   │ X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Meadow Seer (Team Player)
ATK: 1400
DEF: 1400
>yield
It is Enemy's turn!
Farmer King moves to D7.
  ┌───┬───┬───┲━━━┱───┬───┬───┐
7 │   │   │   ┃ Y ┃   │   │   │
  ├───┼───┼───╄━━━╃───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │ xb│   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │ X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Enemy's Farmer King
Enemy places Storm Brewer on E6.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───╆━━━╅───┼───┤
6 │   │   │   │   ┃*y ┃   │   │
  ├───┼───┼───┼───╄━━━╃───┼───┤
5 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │ xb│   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │ X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Storm Brewer (Team Enemy)
ATK: 1000
DEF: 1200
Storm Brewer moves to E5.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───╆━━━╅───┼───┤
5 │   │   │   │   ┃ y ┃   │   │
  ├───┼───┼───┼───╄━━━╃───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │ xb│   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │ X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Storm Brewer (Team Enemy)
ATK: 1000
DEF: 1200
It is Player's turn!
>select D3
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │ y │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───╆━━━╅───┼───┼───┤
3 │   │   │   ┃*xb┃   │   │   │
  ├───┼───┼───╄━━━╃───┼───┼───┤
2 │   │   │   │*X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Meadow Seer (Team Player)
ATK: 1400
DEF: 1400
>move D4
Meadow Seer no longer blocks.
Meadow Seer moves to D4.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │ y │   │   │
  ├───┼───┼───╆━━━╅───┼───┼───┤
4 │   │   │   ┃ x ┃   │   │   │
  ├───┼───┼───╄━━━╃───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │*X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Meadow Seer (Team Player)
ATK: 1400
DEF: 1400
>select E3
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │ y │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │ x │   │   │   │
  ├───┼───┼───┼───╆━━━╅───┼───┤
3 │   │   │   │   ┃   ┃   │   │
  ├───┼───┼───┼───╄━━━╃───┼───┤
2 │   │   │   │*X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
<no unit>
>hand
[1] Granite Driver (1500/1100)
[2] Copper Miller (900/400)
[3] Ember Scout (500/300)
[4] Ember Scout (500/300)
[5] Ashen Marshal (1600/1300)
>place 5
Player places Ashen Marshal on E3.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │ y │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │ x │   │   │   │
  ├───┼───┼───┼───╆━━━╅───┼───┤
3 │   │   │   │   ┃*x ┃   │   │
  ├───┼───┼───┼───╄━━━╃───┼───┤
2 │   │   │   │*X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Ashen Marshal (Team Player)
ATK: 1600
DEF: 1300
>move E4
Ashen Marshal moves to E4.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │ y │   │   │
  ├───┼───┼───┼───╆━━━╅───┼───┤
4 │   │   │   │ x ┃ x ┃   │   │
  ├───┼───┼───┼───╄━━━╃───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │*X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Ashen Marshal (Team Player)
ATK: 1600
DEF: 1300
>yield
It is Enemy's turn!
Farmer King moves to D6.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │   │   │   │   │
  ├───┼───┼───╆━━━╅───┼───┼───┤
6 │   │   │   ┃ Y ┃   │   │   │
  ├───┼───┼───╄━━━╃───┼───┼───┤
5 │   │   │   │   │*y │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │ x │ x │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │ X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Enemy's Farmer King
Enemy places Oak Forager on E5.
Oak Forager and Storm Brewer on E5 join forces!
Union failed. Storm Brewer was eliminated.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───╆━━━╅───┼───┤
5 │   │   │   │   ┃*y ┃   │   │
  ├───┼───┼───┼───╄━━━╃───┼───┤
4 │   │   │   │ x │ x │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │ X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Oak Forager (Team Enemy)
ATK: 1100
DEF: 700
Oak Forager moves to E6.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───╆━━━╅───┼───┤
6 │   │   │   │ Y ┃ y ┃   │   │
  ├───┼───┼───┼───╄━━━╃───┼───┤
5 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │ x │ x │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │ X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Oak Forager (Team Enemy)
ATK: 1100
DEF: 700
It is Player's turn!
>state
  Player                  Enemy
  8000/8000 LP     8000/8000 LP
  DC: 33/40           DC: 34/40
  BC: 2/5               BC: 1/5
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │ Y │ y │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │*x │*x │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │*X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
>select D4
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │ Y │ y │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │   │   │   │   │
  ├───┼───┼───╆━━━╅───┼───┼───┤
4 │   │   │   ┃*x ┃*x │   │   │
  ├───┼───┼───╄━━━╃───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │*X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Meadow Seer (Team Player)
ATK: 1400
DEF: 1400
>move D5
Meadow Seer moves to D5.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │ Y │ y │   │   │
  ├───┼───┼───╆━━━╅───┼───┼───┤
5 │   │   │   ┃ x ┃   │   │   │
  ├───┼───┼───╄━━━╃───┼───┼───┤
4 │   │   │   │   │*x │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │*X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Meadow Seer (Team Player)
ATK: 1400
DEF: 1400
>select E4
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │ Y │ y │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │ x │   │   │   │
  ├───┼───┼───┼───╆━━━╅───┼───┤
4 │   │   │   │   ┃*x ┃   │   │
  ├───┼───┼───┼───╄━━━╃───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │*X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Ashen Marshal (Team Player)
ATK: 1600
DEF: 1300
>move E5
Ashen Marshal moves to E5.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
6 │   │   │   │ Y │ y │   │   │
  ├───┼───┼───┼───╆━━━╅───┼───┤
5 │   │   │   │ x ┃ x ┃   │   │
  ├───┼───┼───┼───╄━━━╃───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │*X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Ashen Marshal (Team Player)
ATK: 1600
DEF: 1300
>yield 1
Player discarded Granite Driver (1500/1100).
It is Enemy's turn!
Farmer King moves to D7.
  ┌───┬───┬───┲━━━┱───┬───┬───┐
7 │   │   │   ┃ Y ┃   │   │   │
  ├───┼───┼───╄━━━╃───┼───┼───┤
6 │   │   │   │   │*y │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
5 │   │   │   │ x │ x │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │ X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Enemy's Farmer King
Enemy places Willow Guard on E6.
Willow Guard and Oak Forager on E6 join forces!
Union failed. Oak Forager was eliminated.
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───╆━━━╅───┼───┤
6 │   │   │   │   ┃*y ┃   │   │
  ├───┼───┼───┼───╄━━━╃───┼───┤
5 │   │   │   │ x │ x │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │ X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
Willow Guard (Team Enemy)
ATK: 600
DEF: 800
Willow Guard (600/800) attacks ??? on E5!
Willow Guard (600/800) was flipped on E6!
Ashen Marshal (1600/1300) was flipped on E5!
Willow Guard was eliminated!
Enemy takes 1000 damage!
  ┌───┬───┬───┬───┬───┬───┬───┐
7 │   │   │   │ Y │   │   │   │
  ├───┼───┼───┼───╆━━━╅───┼───┤
6 │   │   │   │   ┃   ┃   │   │
  ├───┼───┼───┼───╄━━━╃───┼───┤
5 │   │   │   │ x │ x │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
4 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
3 │   │   │   │   │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
2 │   │   │   │ X │   │   │   │
  ├───┼───┼───┼───┼───┼───┼───┤
1 │   │   │   │   │   │   │   │
  └───┴───┴───┴───┴───┴───┴───┘
    A   B   C   D   E   F   G
<no unit>
It is Player's turn!
```
