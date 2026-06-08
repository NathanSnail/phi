package gdavid.phi.block.tile;

import gdavid.phi.util.IProgramTransferTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.psi.api.spell.Spell;

import java.util.UUID;

public class SpellDisplayTile extends BlockEntity implements IProgramTransferTarget {

    public static final String tagSpell = "spell";
    public static BlockEntityType<SpellDisplayTile> type;
    public Spell spell;

    public SpellDisplayTile(BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public BlockPos getPosition() {
        return worldPosition;
    }

    @Override
    public Spell getSpell() {
        return spell;
    }

    public void setSpell(Spell to) {
        if (to == null) {
            spell = null;
        } else {
            spell = to.copy();
            spell.uuid = UUID.randomUUID();
        }
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 18);
    }

    @Override
    public void setSpell(Player player, Spell spell) {
        setSpell(spell);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (spell == null) spell = Spell.createFromNBT(nbt.getCompound(tagSpell));
        else spell.readFromNBT(nbt.getCompound(tagSpell));
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        CompoundTag spellNbt = new CompoundTag();
        if (spell != null) spell.writeToNBT(spellNbt);
        nbt.put(tagSpell, spellNbt);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        var nbt = new CompoundTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        load(packet.getTag());
    }

}
