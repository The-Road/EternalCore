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

    private static final ILootCondition.IBuilder HAS_HAMMER = ModMatchTool.toolMatches(
            ModToolPredicate.Builder.tool().of(ModToolType.HAMMER)
    );

    public GLMDataProvider(DataGenerator gen){
        super(gen, Utils.MOD_ID);
    }

    @Override
    protected void start(){
        // 使用锤子挖掘的方块会自动掉落被锤子处理后的物品
        add("hammer_smash", hammerSmash, new HammerSmashModifier(
                new ILootCondition[]{HAS_HAMMER.build()}
        ));
    }
}
