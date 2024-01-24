# Back Weapon Slot
BackSlot adds extra slots which are easily accessible for storing your sword on your back.
Curse

### Installation
Back Weapon Slot is a made on Forge. It requires [Curios API](https://www.curseforge.com/minecraft/mc-mods/curios) to be installed.

### Tip
[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/B0B6TMVJ8)

### Resource Pack
To adjust the position and size of the rendered items, a resource pack is needed. Back Weapon Slot uses the head renderer which can be set or configured in the models folder and looks like this:

```json
"head": {
			"rotation": [ 0, 0, 270 ],
			"translation": [ 0, 3.5, 0.05],
			"scale":[ 1.05, 1.05, 1.0 ]
		}
```

A full example can be found below:
```json
{
    "parent": "item/generated",
    "textures": {
        "layer0": "minecraft:item/iron_sword"
    },
    "display": {
        "head": {
			"rotation": [ 0, 0, 270 ],
			"translation": [ 0, 3.5, 0.05],
			"scale":[ 1.05, 1.05, 1.0 ]
		}
    }
}
```
