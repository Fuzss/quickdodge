package fuzs.quickdodge.neoforge;

import fuzs.quickdodge.QuickDodge;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(QuickDodge.MOD_ID)
public class QuickDodgeNeoForge {

    public QuickDodgeNeoForge() {
        ModConstructor.construct(QuickDodge.MOD_ID, QuickDodge::new);
    }
}
