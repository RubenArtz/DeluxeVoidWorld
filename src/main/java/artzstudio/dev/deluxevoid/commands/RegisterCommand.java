/*
 *
 *  Copyright (c) 2026 Ruben_Artz and Artz Studio. All rights reserved.
 *
 *  This code is proprietary software. It is strictly prohibited to
 *  copy, modify, distribute, or use this code for any purpose
 *  without the express written permission of the owner.
 *
 *  Project: Deluxe Void World
 *
 */

package artzstudio.dev.deluxevoid.commands;

import artzstudio.dev.deluxevoid.commands.SubCommands.*;
import artzstudio.dev.deluxevoid.utils.commands.MainCommand.MainCommand;

public class RegisterCommand extends MainCommand {
    public RegisterCommand() {
        super("DeluxeVoidWorld.Admin", 1, new Editor(), new Help(), new Reload(), new SetVoid(), new Teleport(), new Toggle());
    }
}
