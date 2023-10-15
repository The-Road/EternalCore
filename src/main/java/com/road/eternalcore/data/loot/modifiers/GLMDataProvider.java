package com.road.eternalcore.data.loot.modifiers;

import com.road.eternalcore.Utils;
import com.road.eternalcore.api.advancements.criterion.ModToolPredicate;
import com.road.eternalcore.common.item.tool.ModToolType;
import com.road.eternalcore.data.loot.conditions.ModMatchTool;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;

public class GLMDataProvider extends GlobalLootModifierProvider {
    public static final GlobalLootModifierSerializer<HammerSmashModifier> hammerSmash = GLMRegistry.hammerSmash.get();
    public static final GlobalLootModifierSerializer<SawModifier> saw = GLMRegistry.saw.get();
    private static final ILootCondition.IBuilder HAS_HAMMER = ModMatchTool.toolMatches(
            ModToolPredicate.Builder.tool().of(ModToolType.HAMMER)
    );
    private static final ILootCondition.IBuilder HAS_SAW = ModMatchTool.toolMatches(
            ModToolPredicate.Builder.tool().of(ModToolType.SAW)
    );

    public GLMDataProvider(DataGenerator gen){
        super(gen, Utils.MOD_ID);
    }

    @Override
    protected void start(){
        add("hammer_smash", hammerSmash, new HammerSmashModifier(
                new ILootCondition[]{HAS_HAMMER.build()}
        ));
        add("saw", saw, new SawModifier(
                new ILootCondition[]{HAS_SAW.build()}
        ));
    }
}
