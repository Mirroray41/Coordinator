also avaible at:

  https://www.spigotmc.org/resources/coordinator.115735/
  
  https://legacy.curseforge.com/minecraft/bukkit-plugins/coordinator


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
