package gay.lunch.createbigmail.index;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import gay.lunch.createbigmail.munitions.big_cannon.mail_shot.MailShotProjectile;
import gay.lunch.createbigmail.munitions.big_cannon.mail_shot.MailShotProjectileRenderer;
import net.minecraft.world.entity.MobCategory;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.multiloader.EntityTypeConfigurator;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;
import rbasamoyai.ritchiesprojectilelib.RPLTags;

import java.util.function.Consumer;

import static gay.lunch.createbigmail.CreateBigMail.REGISTRATE;

public class CBMEntityTypes     {
    public static final EntityEntry<MailShotProjectile> MAIL_SHOT = REGISTRATE
            .entity("mail_shot", MailShotProjectile::new, MobCategory.MISC)
            .properties(cannonProperties())
            .renderer(() -> MailShotProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, CBCMunitionPropertiesHandlers.INERT_BIG_CANNON_PROJECTILE))
            .register();

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
