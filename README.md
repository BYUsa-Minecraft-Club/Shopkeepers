Shopkeepers usage:

| Command                                        | Description                                   |
|------------------------------------------------|-----------------------------------------------|
| /shopkeepers help                              | Displays available commands                   |
| /shopkeepers                                   | Equivalent to `/shopkeepers help`             |
| /shopkeepers make &lt;approved entity type&gt; | Creates a new shopkeeper, owned by you        |
| /shopkeepers shopentities                      | Lists out the entity types approved by admins |

Admin commands:

| Command                                                                    | Description                                                                               |
|----------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| /shopkeepers admin make &lt;approved entity type&gt;                       | Creates a new admin shop                                                                  |
| /shopkeepers admin shopentities list                                       | Lists out the approved entity types                                                       |
| /shopkeepers admin shopentities add &lt;summonable entity type&gt;         | Adds entity type to approved list                                                         |
| /shopkeepers admin shopentities remove &lt;summonable entity type&gt;      | Removes entity type from approved list. Does not disband existing shops with removed type |
| /shopkeepers admin playershoplimit get                                     | Displays the current default maximum number of shops players can own                      |
| /shopkeepers admin playershoplimit get &lt;player name&gt;                 | Displays the current maximum number of shops for specified player                         |
| /shopkeepers admin playershoplimit set &lt;integer&gt;                     | Sets the maximum number of shops players can own                                          |
| /shopkeepers admin playershoplimit set &lt;integer&gt; &lt;player name&gt; | Sets the maximum number of shops specified player can own                                 |
| /shopkeepers admin playershoplimit remove &lt;player name&gt;              | Removes the specific limit for specified player (reverts to default)                      |
| /shopkeepers admin playershoplimit list                                    | Lists shop limits for any player with a limit set                                         |

