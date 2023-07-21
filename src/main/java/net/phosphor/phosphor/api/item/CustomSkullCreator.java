package net.phosphor.phosphor.api.item;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PlayerHeadMeta;

import java.util.Base64;
import java.util.UUID;

public class CustomSkullCreator {

    public static ItemStack getSkullWithSkinValue(String value) {
        PlayerSkin playerSkin = new PlayerSkin(value, "");
        return ItemStack.of(Material.PLAYER_HEAD).withMeta(PlayerHeadMeta.class, meta -> meta
                .skullOwner(UUID.randomUUID())
                .playerSkin(playerSkin)
                .build());
    }

    /**
     * @param textureId The Texture ID from minecraft-heads.com (Minecraft-URL)
     * @return The skull itemstack
     */
    public static ItemStack getSkullByLink(String textureId) {
        PlayerSkin playerSkin = new PlayerSkin(Base64.getEncoder().encodeToString(("{textures:{SKIN:{url:\"https://textures.minecraft.net/texture/" + textureId + "\"}}}").getBytes()), "");
        return ItemStack.of(Material.PLAYER_HEAD).withMeta(PlayerHeadMeta.class, meta -> meta
                .skullOwner(UUID.randomUUID())
                .playerSkin(playerSkin)
                .build());
    }
}
