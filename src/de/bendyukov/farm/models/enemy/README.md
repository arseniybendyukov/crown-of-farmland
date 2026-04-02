# Enemy Bot

This document describes the behavior of the computer-controlled opponent in *Crown of Farmland*.

## Random Selection

Some bot decisions are made by **weighted random selection**.

- negative weights are treated as `0`
- options with larger weights are more likely to be selected
- the order of the options is preserved

Example:

- weights `(300, 400, 500)` make the third option more likely than the second, and the second more likely than the first

Some decisions use **reverse-weighted selection** instead.

- options with smaller original weights become more likely
- the option with the largest original weight cannot be selected

Example:

- original weights `(300, 400, 500)` are treated like `(200, 100, 0)`

## Turn Logic

The opposing team is controlled by a simple bot. Each bot turn follows these steps.

### 1. Move the Farmer King

The bot evaluates all legal Farmer King moves, including staying in place.

The score is built from:

- `distance`: distance to the enemy Farmer King
- `enemies`: number of adjacent enemy units in the eight surrounding directions
- `fellows`: number of adjacent friendly units in the eight surrounding directions, excluding the Farmer King
- `fellowPresent`: whether the target field is already occupied by a friendly unit, excluding the Farmer King

The score is:

`score = fellows - 2 * enemies - distance - 3 * fellowPresent`

The bot chooses the move with the highest score. If several fields tie, one is selected randomly with equal weight in the order: **up, right, down, left**.

### 2. Choose a placement field

If a placement is possible, the bot evaluates all legal fields around its Farmer King.

The score is built from:

- `steps`: number of steps needed to reach the enemy Farmer King
- `enemies`: number of adjacent enemy units in the four cardinal directions
- `fellows`: number of adjacent friendly units in the four cardinal directions, including the Farmer King

The score is:

`score = -steps + 2 * enemies - fellows`

The highest-scoring field is chosen. Ties are resolved randomly with equal weight, starting above the Farmer King and then continuing clockwise.

If there is no free legal field, the bot skips placement for that turn and does not make up for it later in the same turn.

### 3. Choose which unit to place

The bot selects the unit to place by weighted random selection.

- each card in hand uses its `ATK` value as weight
- the hand order determines the option order

### 4. Move units

For each movable unit `A`, the bot evaluates these six actions:

- **up**
- **right**
- **down**
- **left**
- **block**
- **stay in place**

#### Movement into a friendly unit

If the target field contains a friendly unit `B`, the score depends on whether a merge is possible.

If a merge into `AB` is possible:

`score = ATK_AB + DEF_AB - ATK_A - DEF_A`

If a merge is not possible:

`score = -ATK_B - DEF_B`

#### Movement into an enemy unit

If the target field contains an enemy unit `B`, the score is:

- against the enemy Farmer King:

`score = ATK_A`

- against a hidden unit:

`score = ATK_A - 500`

- against a blocking unit:

`score = ATK_A - DEF_B`

- in a standard duel:

`score = 2 * (ATK_A - ATK_B)`

#### Movement into an empty field

If the target field is empty, the score is built from:

- `steps`: number of steps needed to reach the enemy Farmer King
- `enemies`: number of enemy units adjacent to that field in the four cardinal directions

The score is:

`score = 10 * steps - enemies`

#### Blocking and staying in place

Let `ATK*_B` be the greatest attack value among all enemy units that surround `A` in a straight line.

Then:

- for **block**:

`score = max(1, (DEF_A - ATK*_B) / 100)`

- for **stay in place**:

`score = max(0, (ATK_A - ATK*_B) / 100)`

#### Choosing the acting unit and action

The six action scores are summed up to get the unit’s **total score**.

The unit with the highest total score acts next.

Then the concrete action is chosen by weighted random selection using those six action scores as weights, in the same order as above.

If no positive move is available, the bot starts a block instead.

After each action, the process starts again for all units that can still move.

### 5. End the turn

Once all unit movement is finished, the bot ends its turn.

If it must discard because its hand is full, the discarded unit is chosen by reverse-weighted random selection.

- the weight of each card is `ATK + DEF`
- the hand order determines the option order