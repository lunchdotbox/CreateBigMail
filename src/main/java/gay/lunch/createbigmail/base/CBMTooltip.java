package gay.lunch.createbigmail.base;

import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class CBMTooltip {
    public static void addHoldShift(boolean desc, List<Component> tooltip) {
        String[] holdDesc = CreateLang.translateDirect("tooltip.holdForDescription", "$").getString().split("\\$");
        if (holdDesc.length < 2) {
            return;
        }
        Component keyShift = CreateLang.translateDirect("tooltip.keyShift");
        MutableComponent tabBuilder = Component.literal("");
        tabBuilder.append(Component.literal(holdDesc[0]).withStyle(ChatFormatting.DARK_GRAY));
        tabBuilder.append(keyShift.plainCopy().withStyle(desc ? ChatFormatting.WHITE : ChatFormatting.GRAY));
        tabBuilder.append(Component.literal(holdDesc[1]).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(tabBuilder);
    }
}
