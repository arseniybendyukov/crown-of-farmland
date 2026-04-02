### Weighted Random Selection

Several parts of the game use weighted random selection.

Given an ordered list of integer weights:
- negative weights are treated as `0`,
- each option receives an interval with size equal to its weight,
- a random integer is drawn across the full interval range,
- the option whose interval contains that number is selected.

Example:
- weights `(300, 400, 500)` produce intervals `[1,300]`, `[301,700]`, `[701,1200]`
- `500` selects the second option
- `800` selects the third option

### Reverse Weighted Random Selection

Some parts of the game use reverse-weighted selection, where larger original weights mean lower selection probability.

This works by:
- taking the maximum original weight `m`,
- converting each weight `w` to `m - w`,
- then applying standard weighted random selection.

Options with the original maximum weight receive weight `0` after conversion and therefore cannot be selected.

## Opponent Bot

The opposing team is controlled by a simple AI. Each AI turn follows these steps.

### 1. Move the Farmer King

The AI evaluates all legal Farmer King moves, including staying in place.

The score depends on:
- distance to the enemy Farmer King,
- number of adjacent enemy units in the eight surrounding directions,
- number of adjacent friendly units in the eight surrounding directions,
- whether the target field is already occupied by a friendly unit.

The AI chooses the move with the highest score. If several fields tie, one is selected by weighted random selection with equal weights in the order: up, right, down, left.

### 2. Choose a placement field

If a placement is possible, the AI evaluates all legal fields around its Farmer King.

The score depends on:
- the number of steps needed to reach the enemy Farmer King,
- the number of adjacent enemy units in the four cardinal directions,
- the number of adjacent friendly units in the four cardinal directions, including the Farmer King.

The highest-scoring field is chosen. Ties are resolved by weighted random selection with equal weights, starting above the Farmer King and then continuing clockwise.

If there is no free legal field, the AI skips placement for that turn and does not make up for it later in the same turn.

### 3. Choose which unit to place

The AI selects the unit to place by weighted random selection.

- Each card on the hand uses its `ATK` value as weight.
- The hand order determines the option order.

### 4. Move units

For each movable unit, the AI evaluates moving up, right, down, left, blocking, and staying in place.

For movement into a friendly unit, it scores:
- a possible merge positively based on the merged unit's resulting strength,
- an impossible merge negatively based on the target unit's stats.

For movement into an enemy unit, it scores:
- attacks on the enemy Farmer King positively,
- attacks on hidden units with a penalty,
- attacks on blocking units based on attacker `ATK` versus defender `DEF`,
- normal attacks based on the attack difference.

For movement into an empty field, it scores advancement toward the enemy Farmer King and nearby enemy pressure.

Blocking and staying in place are also scored using the strongest straight-line enemy threat around the unit.

The unit with the highest total score is chosen. Then the concrete action is chosen by weighted random selection using the action scores as weights, in this order:
- up,
- right,
- down,
- left,
- block,
- stay in place.

If no positive move is available, the AI starts a block instead.

After each action, the process starts again for all units that can still move.

### 5. End the turn

Once all unit movement is finished, the AI ends its turn.

If it must discard because its hand is full, the discarded unit is chosen by reverse-weighted random selection.

- The weight of each card is `ATK + DEF`.
- The hand order determines the option order.