package gdavid.phi.cable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public interface ICableSegment {

    @Nullable
    BlockPos getConnection();

    void setConnection(@Nullable BlockPos connection, Predicate<BlockPos> connected);

    Iterable<BlockPos> getNeighbours();

    boolean canConnect(Direction side);

}
