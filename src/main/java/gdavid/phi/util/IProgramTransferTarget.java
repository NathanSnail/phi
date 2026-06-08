package gdavid.phi.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import vazkii.psi.api.spell.Spell;

import java.util.List;

public interface IProgramTransferTarget {

    BlockPos getPosition();

    Spell getSpell();

    void setSpell(Player player, Spell spell);

    default boolean hasSlots() {
        return false;
    }

    default List<Integer> getSlots() {
        return null;
    }

    default List<ResourceLocation> getSlotIcons() {
        return null;
    }

    default void selectSlot(int id) {
    }

}
