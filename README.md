# OSNR Server

When you add a new plugin class, you need to run `com.zenyte.plugins.PluginScanner` to update the `data/plugins.dat`
file so it can be loaded.

---

# Kotlin Scripting
For ease of creating plugins we provide a scripting framework that uses Kotlin.
A kotlin script (in how we use it) is essentially the body of a class that implements a specific script definition.
We currently support the following script definitions:

| Script name                     | extension                 |
|---------------------------------|---------------------------|
| <b>Ground Item Spawn Script</b> | <i>.grounditems.kts</i>   |
| <b>User Interface Script</b>    | <i>.userinterface.kts</i> |
| <b>Item Action Script</b>       | <i>.itemaction.kts</i>    |
| <b>Item Definitions Script</b>  | <i>.items.kts</i>         |
| <b>Npc Action Script</b>        | <i>.npcaction.kts</i>     |
| <b>Npc Definitions Script</b>   | <i>.npcs.kts</i>          |
| <b>Npc Drops Script</b>         | <i>.drops.kts</i>         |
| <b>Npc Spawns Script</b>        | <i>.spawns.kts</i>        |
| <b>Object Action Script</b>     | <i>.objectaction.kts</i>  |
| <b>Shop Script</b>              | <i>.shop.kts</i>          |

Kotlin scripting is still quite experimental and there might be some hiccups
with the detection of script definitions by Intellij.

Intellij tries to associate a kotlin script file (so a file with one of the extensions in the above table)
to a kotlin script definition (one of the classes defined in a sub-module in the `scripts` module).

### Trouble Shooting

#### Intellij fails to detect a script file 
make sure u have the following in the build folder of the module containing the script definition:
`resources > main > META-INF > kotlin > script > templates > com.near_reality.scripts.object.actions.SomeScript`.
where `SomeScript` is the class nane of the script definition.

You can generate this by executing the gradle `assemble` task in the module containing the script definition.

---

# Reloadable Modules
To use jrebel you need to activate the license by running `activate.sh` script inside jrebel/bin directory, 
more info on that you can find at: [JRebel Activation Guide](https://manuals.jrebel.com/jrebel/standalone/activate.html#activation-wizard)

By using `runJrebel` task to start a server, you enable reloadable server.

To make your module reloadable, make sure in your gradle.kts you specify this configuration:

For non-kilim modules just put: 

```kotlin
apply(plugin = "org.zeroturnaround.gradle.jrebel")
```
