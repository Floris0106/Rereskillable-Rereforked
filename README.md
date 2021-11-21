# Rereskillable Rereforked

Rereskillable Rereforked is a fork of Rereskillable, which is a fork of Reskillable, which is a fork of Skillable.

This mod allows you to lock:
- Items (using, wearing)
- Blocks (breaking, placing)
- Entities (mounting, loot tables)
Behid levels in 8 different skills.

## Configuration

Config options include:
- Disabling sheep wool drops
- Changing the starting upgrade cost in XP levels
- Changing the maximum skill level
- Specifying whether Skill Fragments should be used to level up skills instead of XP levels
- Changing how many Skill Fragments can be obtained from different kinds of advancements
- Specifying a list of skill locks

## Skill Locks

A new lock can be added by adding a string to the skillLocks list:
```
"mod:id skill:level"
```
For example:
```
"minecraft:ender_pearl magic:5"
"minecraft:bow attack:3 agility:2"
"minecraft:horse agility:10"
```

## Commands

To set a skill level:
```
/skills [player] set [skill] [level]
```
To get a skill level:
```
/skills [player] get [skill]
```
To reset all skill levels:
```
/skills [player] reset
```

## Known Issues

- You can't lock the Totem of Undying from being used.
- You can block items from being used regardless of skill only by setting the requirement to a level above max skill level (example: "minecraft:shield magic:69")
- Locking loot tables is not implemented

## Support

I forked this mod to use in a modpack i'm working on, so updates won't be frequent, but feel free to report issues or submit pull requests if you want ot help with development!

## Modpacks

Please, go ahead and use this mod in your modpack! It's what the mod was designed for!

## Credits

Credits go to Vazkii (author of Skillable), lanse505 (author of Reskillable, among others) and majik (author of Rereskillable).
