Shopkeepers usage:

| Command                                  | Description                                   |
|------------------------------------------|-----------------------------------------------|
| /shopkeepers help                        | Displays available commands                   |
| /shopkeepers                             | Equivalent to `/shopkeepers help`             |
| /shopkeepers make <approved entity type> | Creates a new shopkeeper, owned by you        |
| /shopkeepers shopentities                | Lists out the entity types approved by admins |

Admin commands:

| Command                                                         | Description                                                                               |
|-----------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| /shopkeepers admin make <approved entity type>                  | Creates a new admin shop                                                                  |
| /shopkeepers admin shopentities list                            | Lists out the approved entity types                                                       |
| /shopkeepers admin shopentities add <summonable entity type>    | Adds entity type to approved list                                                         |
| /shopkeepers admin shopentities remove <summonable entity type> | Removes entity type from approved list. Does not disband existing shops with removed type |
| /shopkeepers admin playershoplimit get                          | Displays the current default maximum number of shops players can own                      |
| /shopkeepers admin playershoplimit get <player name>            | Displays the current maximum number of shops for specified player                         |
| /shopkeepers admin playershoplimit set <integer>                | Sets the maximum number of shops players can own                                          |
| /shopkeepers admin playershoplimit set <integer> <player name>  | Sets the maximum number of shops specified player can own                                 |
| /shopkeepers admin playershoplimit remove <player name>         | Removes the specific limit for specified player (reverts to default)                      |
| /shopkeepers admin playershoplimit list                         | Lists shop limits for any player with a limit set                                         |

