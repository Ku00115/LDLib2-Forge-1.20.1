package com.lowdragmc.lowdraglib2.gui.texture;

import com.lowdragmc.lowdraglib2.configurator.annotation.ConfigColor;
import com.lowdragmc.lowdraglib2.configurator.annotation.ConfigSearch;
import com.lowdragmc.lowdraglib2.configurator.annotation.Configurable;
import com.lowdragmc.lowdraglib2.configurator.ui.SearchComponentConfigurator;
import com.lowdragmc.lowdraglib2.gui.ui.utils.UIElementProvider;
import com.lowdragmc.lowdraglib2.integration.kjs.KJSBindings;
import com.lowdragmc.lowdraglib2.registry.annotation.LDLRegisterClient;
import com.lowdragmc.lowdraglib2.utils.search.IResultHandler;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/**
 * The sprite ResourceLocation should point to a registered vanilla sprite
 * (e.g. from a GUI atlas), not a raw texture file.
 */
@KJSBindings
@LDLRegisterClient(name = "vanilla_sprite_texture", registry = "ldlib2:gui_texture")
@Accessors(chain = true)
public class VanillaSpriteTexture extends TransformTexture {

    @Configurable(name = "ldlib.gui.editor.name.resource")
    @ConfigSearch(searchConfiguratorMethod = "searchSprites")
    @Getter
    @Setter
    private ResourceLocation sprite = ResourceLocation.withDefaultNamespace("toast/recipe_book");

    @Configurable
    @ConfigColor
    @Getter
    @Setter
    private int color = -1;

    public VanillaSpriteTexture() {
    }

    public VanillaSpriteTexture(ResourceLocation sprite) {
        this.sprite = sprite;
    }

    @HideFromJS
    public static VanillaSpriteTexture of(ResourceLocation sprite) {
        return new VanillaSpriteTexture(sprite);
    }

    @HideFromJS
    public static VanillaSpriteTexture of(String sprite) {
        return of(ResourceLocation.parse(sprite));
    }

    @RemapForJS("of")
    public static VanillaSpriteTexture kjs$of(ResourceLocation sprite) {
        return of(sprite);
    }

    @Override
    public VanillaSpriteTexture copy() {
        var copied = new VanillaSpriteTexture(sprite);
        copied.color = color;
        copied.copyTransform(this);
        return copied;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(GuiGraphics graphics, float mouseX, float mouseY, float x, float y, float width, float height, float partialTicks) {
        if (sprite == null || width <= 0 || height <= 0) {
            return;
        }
        graphics.setColor(
                (color >> 16 & 255) / 255.0F,
                (color >> 8 & 255) / 255.0F,
                (color & 255) / 255.0F,
                (color >> 24 & 255) / 255.0F);
        graphics.blit(sprite, Math.round(x), Math.round(y), 0, 0, Math.round(width), Math.round(height), Math.round(width), Math.round(height));
        graphics.setColor(1, 1, 1, 1);
    }

    @OnlyIn(Dist.CLIENT)
    private SearchComponentConfigurator.ISearchConfigurator<ResourceLocation> searchSprites() {
        return new SearchComponentConfigurator.ISearchConfigurator<>() {
            @Override
            @NotNull
            public ResourceLocation defaultValue() {
                return ResourceLocation.withDefaultNamespace("toast/recipe_book");
            }

            @Override
            public void search(String word, IResultHandler<ResourceLocation> searchHandler) {
                var lowerWord = word.toLowerCase();
                // Minecraft 1.20.1 has no GuiSpriteManager index to enumerate here.
            }

            @Override
            @NotNull
            public String resultText(@NotNull ResourceLocation value) {
                return value.toString();
            }

            @Override
            public UIElementProvider<ResourceLocation> candidateUIProvider() {
                return UIElementProvider.iconText(VanillaSpriteTexture::of, res -> Component.literal(res.toString()));
            }
        };
    }

}
