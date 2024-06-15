package ruben_artz.world.commands;

import ruben_artz.world.commands.SubCommands.*;
import ruben_artz.world.utils.commands.MainCommand.MainCommand;

public class RegisterCommand extends MainCommand {
    public RegisterCommand() {
        super("DeluxeVoidWorld.Admin", 1, new Editor(), new Help(), new Reload(), new SetVoid(), new Teleport(), new Toggle());
    }
}
