package gay.lunch.createbigmail.index;

import gay.lunch.createbigmail.CreateBigMail;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.function.UnaryOperator;

public class CBMDataComponents {
    public static final DataComponentType<ItemContainerContents> PACKAGE = register(
            "package",
            builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC)
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, CreateBigMail.resource(name), builder.apply(DataComponentType.builder()).build());
    }

    public static void init() {}
}
