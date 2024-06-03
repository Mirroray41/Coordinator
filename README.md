also avaible at:

  https://www.spigotmc.org/resources/coordinator.115735/
  
  https://www.curseforge.com/minecraft/bukkit-plugins/coordinator

# Discord support server here:
  https://discord.gg/gcgNDWWkwm


## Papi placeholder: %coordinator_title% -> returns bossbar title text for player

## Migration from config version 3 to 4:

### Add:
```
legacy: false
```
<details>
  <summary>Default config file for reference</summary>
  
  ```yaml
#DO NOT CHANGE THE FILE FORMAT VERSION WITHOUT KNOWING WHAT YOU'RE DOING!
file_format: 4

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

legacy: false
#time zone offset from the timezone of the server host
time_offset: 0
#how often the bossbar refreshes for all players
bossbar_refresh_interval: 1

#cr command has reload sub subcommand requiring the "cr.reload" permission
  ```
  
</details>

## Migration from config version 2 to 3:

### Add:
```
time_offset: 0
```
<details>
  <summary>Default config file for reference</summary>
  
  ```yaml
#DO NOT CHANGE THE FILE FORMAT VERSION WITHOUT KNOWING WHAT YOU'RE DOING!
file_format: 3

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


#time zone offset from the timezone of the server host
time_offset: 0
#how often the bossbar refreshes for all players
bossbar_refresh_interval: 1

#cr command has reload sub subcommand requiring the "cr.reload" permission
  ```
  
</details>

## Migration from translations version 2 to 3:

### Add:
```
  errors:
    cr_command_missing_player: "&cThis player isn't online or is console or has a missing config"
    cr_command_incorrect_parameter: "&cThe value you're trying to set doesn't exist in the player config"
 info:
    cr_command_reload: "&aConfiguration reloaded!"
```

### Replace:
```
prefixes:
  location: ""
  direction: ""
  time: ""

```
with:
```
bossbar: "{x} {y} {z} {direction} {time}"
```
-------------------------------------------------------------------
```
    selected: "&7● "
    unselected: "&8● "
    on_val: " &a&nOn"
    off_val: " &c&nOff"
```
with:
```
state: "|&c&nOff|&a&nOn|"
select_state: "/&8●/&7●/"
```

### Modify:
```
visibility:
  switch_label: "&fVisibility Switch:" -> "&fVisibility Switch: {state}"
location:
  switch_label: "&fLocation Switch:" -> "&fLocation Switch: {state}"
  type_selection_1: "Whole rounded coordinates" -> "{select_state} Whole rounded coordinates"
  type_selection_2: "Round to 2 decimal places" -> "{select_state} Round to 2 decimal places"
direction:
  switch_label: "&fDirection Switch:" -> "&fDirection Switch: {state}"
  type_selection_1: "Cardinal direction" -> "{select_state} Cardinal direction"
  type_selection_2: "Coordinate direction" -> "{select_state} Coordinate direction"
  type_selection_3: "Both" -> "{select_state} Both"
time:
  switch_label: "&fTime Switch:" -> "&fTime Switch: {state}"
  type_selection_1: "In-game time" -> "{select_state} In-game time"
  type_selection_2: "Real time" -> "{select_state} Real time"
```

<details>
  <summary>Default translation file for reference</summary>
  
  ```yaml
#DO NOT CHANGE THE FILE FORMAT VERSION WITHOUT KNOWING WHAT YOU'RE DOING!
file_format: 3

#hex color support using "#001122"
bossbar: "{x} {y} {z} {direction} {time}"
# the "|" will get replaced on applying translations
state: "|&c&nOff|&a&nOn|"
select_state: "/&8●/&7●/"

translations:
  gui:
    title: "Coordinator Player Settings:"
    loading: "Loading..."
    disabled: "&8Disabled "

    visibility:
      switch_label: "&fVisibility Switch: {state}"
      switch_tooltip: "&7Switch to hide or show the bossbar"

    location:
      switch_label: "&fLocation Switch: {state}"
      switch_tooltip: "&7Switch to hide or show the location of the player"
      type_selector_label: "Location Type:"
      type_selection_1: "{select_state} Whole rounded coordinates"
      type_selection_2: "{select_state} Round to 2 decimal places"

    direction:
      switch_label: "&fDirection Switch: {state}"
      switch_tooltip: "&7Switch to hide or show the direction the player is facing"
      type_selector_label: "Direction Type:"
      type_selection_1: "{select_state} Cardinal direction"
      type_selection_2: "{select_state} Coordinate direction"
      type_selection_3: "{select_state} Both"

    time:
      switch_label: "&fTime Switch: {state}"
      switch_tooltip: "&7Switch to hide or show the time"
      type_selector_label: "Time Type:"
      type_selection_1: "{select_state} In-game time"
      type_selection_2: "{select_state} Real time"

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
    cr_command_missing_player: "&cThis player isn't online or is console or has a missing config"
    cr_command_incorrect_parameter: "&cThe value you're trying to set doesn't exist in the player config"

  info:
    cr_command_reload: "&aConfiguration reloaded!"
  ```
  
</details>


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

<details>
  <summary>Default config file for reference</summary>
  
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
  
</details>


## Migration from translations version 1 to 2:

### Add:
```
translations:  gui:  disabled: "&8Disabled "
```

<details>
  <summary>Default translations file for reference</summary>
  
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
  
</details>
