package io.github.kurrycat.mpkmod.module.macros;

public class TickScheduler {
    private final Macro.It iterator;

    private Macro.Tick tick = null;
    private int count = 0;

    public TickScheduler(Macro.It iterator) {
        this.iterator = iterator;
        loadNextTick();
    }

    private void loadNextTick() {
        do {
            if (iterator.hasNext()) {
                tick = iterator.next();
                count = tick.tickInput.getCount();
            } else {
                tick = null;
                count = 0;
                return;
            }
        } while (count <= 0);
    }

    public Macro.Tick nextTick() {
        if (tick == null) return null;

        Macro.Tick curr = tick;

        count--;
        if (count <= 0)
            loadNextTick();

        return curr;
    }

    public Macro.Tick getCurrentTick() {
        return tick;
    }

    public int getCurrentCount() {
        return count;
    }
}
