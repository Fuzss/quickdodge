package fuzs.quickdodge.attachment;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.apache.commons.lang3.mutable.MutableInt;

public record DodgeData(MutableInt remainingDodgeTicks, IntOpenHashSet bashedEntityIds) {

    public DodgeData() {
        this(new MutableInt(), new IntOpenHashSet(5));
    }
}
