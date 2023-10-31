package net.phosphor.phosphor.api.permission;

import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.permission.Permission;
import net.minestom.server.permission.PermissionVerifier;
import net.phosphor.phosphor.api.PhosphorServerAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author : Lisa
 * @created : 30. Okt.. 2023 | 18:30
 * @contact : @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/
public class PhosphorPermissionPlayer extends Player {


    public PhosphorPermissionPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        return super.hasPermission(permission) || PhosphorServerAPI.getInstance().isOp(getUsername());
    }

    @Override
    public boolean hasPermission(@NotNull String permissionName) {
        return super.hasPermission(permissionName) || PhosphorServerAPI.getInstance().isOp(getUsername());
    }

    @Override
    public boolean hasPermission(@NotNull String permissionName, @Nullable PermissionVerifier permissionVerifier) {
        return super.hasPermission(permissionName) || PhosphorServerAPI.getInstance().isOp(getUsername());
    }
}
