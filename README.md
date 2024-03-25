also avaible at:

  https://www.spigotmc.org/resources/coordinator.115735/
  
  https://www.curseforge.com/minecraft/bukkit-plugins/coordinator


## Migration from config version 1 to 2:

### Replace:
```
visibility: true -> visibility:  is_visible: true
direction: true -> direction:  is_visible: true
location: true -> location:  is_visible: true
time: true -> time:  is_visible: true

direction_type: x -> direction:  default_type: x
location_type: x -> location:  default_type: x
time_type: x -> time:  default_type: x
```

### Add:
```
globals:
    visibility:
        is_enabled: true
    direction:
        is_enabled: true
    location:
        is_enabled: true
    time:
        is_enabled: true
```


### Default config for reference
```yaml
file_format: 2

default_settings:
    visibility:
        is_visible: true

    direction:
        is_visible: true
        default_type: 0

    location:
        is_visible: true
        default_type: 0

    time:
        is_visible: true
        default_type: 0

globals:
    visibility:
        is_enabled: true
    direction:
        is_enabled: true
    location:
        is_enabled: true
    time:
        is_enabled: true

#how often the bossbar refreshes for all players
bossbar_refresh_interval: 1

#cr command has reload sub subcommand requiring the "cr.reload" permission

```

## Migration from translations version 1 to 2:

### Add:
```
translations:  gui:  disabled: "&8Disabled "
```

### Default translations for reference:
```yaml
#DO NOT CHANGE THE FILE FORMAT VERSION WITHOUT KNOWING WHAT YOU'RE DOING!
file_format: 2

#hex color support using "#001122"
prefixes:
  location: ""
  direction: ""
  time: ""

translations:
  gui:
    title: "Coordinator Player Settings:"
    loading: "Loading..."
    selected: "&7● "
    unselected: "&8● "
    on_val: " &a&nOn"
    off_val: " &c&nOff"
    disabled: "&8Disabled "

    visibility:
      switch_label: "&fVisibility Switch:"
      switch_tooltip: "&7Switch to hide or show the bossbar"

    location:
      switch_label: "&fLocation Switch:"
      switch_tooltip: "&7Switch to hide or show the location of the player"
      type_selector_label: "Location Type:"
      type_selection_1: "Whole rounded coordinates"
      type_selection_2: "Round to 2 decimal places"

    direction:
      switch_label: "&fDirection Switch:"
      switch_tooltip: "&7Switch to hide or show the direction the player is facing"
      type_selector_label: "Direction Type:"
      type_selection_1: "Cardinal direction"
      type_selection_2: "Coordinate direction"
      type_selection_3: "Both"

    time:
      switch_label: "&fTime Switch:"
      switch_tooltip: "&7Switch to hide or show the time"
      type_selector_label: "Time Type:"
      type_selection_1: "In-game time"
      type_selection_2: "Real time"

  directions:
    north: "North"
    north_east: "North-East"
    east: "East"
    south_east: "South-East"
    south: "South"
    south_west: "South-West"
    west: "West"
    north_west: "North-West"

    unknown: "Unknown-Direction"

  errors:
    cr_command_run_by_non_player: "&cThis command can only be run by a player"
    cr_command_wrong_arguments: "&cUnknown arguments!"
```
