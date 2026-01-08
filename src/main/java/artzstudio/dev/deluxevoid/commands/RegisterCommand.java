package artzstudio.dev.deluxevoid.commands;

import artzstudio.dev.deluxevoid.commands.SubCommands.*;
import artzstudio.dev.deluxevoid.utils.commands.MainCommand.MainCommand;

public class RegisterCommand extends MainCommand {
    public RegisterCommand() {
        super("DeluxeVoidWorld.Admin", 1, new Editor(), new Help(), new Reload(), new SetVoid(), new Teleport(), new Toggle());
    }
}
