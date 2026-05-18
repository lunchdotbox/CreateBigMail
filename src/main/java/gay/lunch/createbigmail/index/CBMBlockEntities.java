package gay.lunch.createbigmail.index;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import gay.lunch.createbigmail.munitions.big_cannon.mail_shot.MailShotBlockEntity;

import static gay.lunch.createbigmail.CreateBigMail.REGISTRATE;

public class CBMBlockEntities {
    public static final BlockEntityEntry<MailShotBlockEntity> MAIL_SHOT_BLOCK = REGISTRATE
            .blockEntity("mail_shot_block", MailShotBlockEntity::new)
            .validBlocks(CBMBlocks.MAIL_SHOT)
            .register();

    public static void register() {
    }
}
