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

public class EarlyEvaluateTrick extends PieceTrick {

    ReferenceParam target;
    SpellParam<Number> condition;

    public EarlyEvaluateTrick(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ReferenceParam(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
        addParam(condition = new ParamNumber(Param.condition.name, SpellParam.BLUE, false, false));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addToTooltipAfterShift(List<Component> tooltip) {
        ParamHelper.outputTooltip(this, super::addToTooltipAfterShift, tooltip);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (Math.abs(getNonnullParamValue(context, condition).doubleValue()) >= 1) return null;
        try {
            SpellPiece piece = spell.grid.getPieceAtSideWithRedirections(x, y, paramSides.get(target));
            EvalHelper.hoist(piece, context);
        } catch (SpellCompilationException e) {
            Errors.errored.runtime();
        }
        return null;
    }

}
