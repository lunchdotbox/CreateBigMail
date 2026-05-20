package gay.lunch.createbigmail.index;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import gay.lunch.createbigmail.CreateBigMail;
import gay.lunch.createbigmail.munitions.big_cannon.mail_shot.MailShotProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.multiloader.EntityTypeConfigurator;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import net.minecraft.world.entity.EntityType.EntityFactory;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.config.PropertiesTypeHandler;
import rbasamoyai.ritchiesprojectilelib.RPLTags;

import java.util.function.Consumer;

import static gay.lunch.createbigmail.CreateBigMail.REGISTRATE;

public class CBMEntityTypes     {
    public static final EntityEntry<MailShotProjectile> MAIL_SHOT = cannonProjectile("mail_shot", MailShotProjectile::new, CBCMunitionPropertiesHandlers.INERT_BIG_CANNON_PROJECTILE);

    private static <T extends AbstractBigCannonProjectile> EntityEntry<T>
    cannonProjectile(String id, EntityFactory<T> factory, PropertiesTypeHandler<EntityType<?>, ?> handler) {
        return REGISTRATE
                .entity(id, factory, MobCategory.MISC)
                .properties(cannonProperties())
                .renderer(() -> BigCannonProjectileRenderer::new)
                .tag(RPLTags.PRECISE_MOTION)
                .onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
                .register();
    }

    public static void register() {}

    private static <T> NonNullConsumer<T> configure(Consumer<EntityTypeConfigurator> cons) {
        return b -> cons.accept(EntityTypeConfigurator.of(b));
    }

    private static <T> NonNullConsumer<T> cannonProperties() {
        return configure(c -> c.size(0.8f, 0.8f)
                .fireImmune()
                .updateInterval(1)
                .updateVelocity(false) // Ditto
                .trackingRange(16));
    }
}
