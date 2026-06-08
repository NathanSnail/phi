package gdavid.phi.spell.trick.evaluation;

import gdavid.phi.spell.Errors;
import gdavid.phi.spell.Param;
import gdavid.phi.spell.param.ReferenceParam;
import gdavid.phi.util.EvalHelper;
import gdavid.phi.util.ParamHelper;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.List;

public class ReevaluateTrick extends PieceTrick {

    ReferenceParam target;
    SpellParam<Number> condition;

    public ReevaluateTrick(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ReferenceParam(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false).preventLoop());
        addParam(condition = new ParamNumber(Param.condition.name, SpellParam.BLUE, true, false));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addToTooltipAfterShift(List<Component> tooltip) {
        ParamHelper.outputTooltip(this, super::addToTooltipAfterShift, tooltip);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
        if (EvalHelper.isLoop(this)) Errors.compile(SpellCompilationException.INFINITE_LOOP);
        SpellPiece piece = spell.grid.getPieceAtSideWithRedirections(x, y, paramSides.get(target));
        if (piece == null || !paramSides.get(target).isEnabled())
            Errors.compile(SpellCompilationException.INVALID_PARAM);
        piece.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (Math.abs(getParamValueOrDefault(context, condition, 0).doubleValue()) >= 1) return null;
        try {
            SpellPiece piece = spell.grid.getPieceAtSideWithRedirections(x, y, paramSides.get(target));
            EvalHelper.reevaluate(piece, context);
        } catch (SpellCompilationException e) {
            Errors.errored.runtime();
        }
        return null;
    }

}
