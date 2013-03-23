package org.toj.dnd.irctoolkit.engine.observers;

import org.toj.dnd.irctoolkit.engine.ReadonlyContext;

public interface PcViewObserver {
    void update(ReadonlyContext context);
}
