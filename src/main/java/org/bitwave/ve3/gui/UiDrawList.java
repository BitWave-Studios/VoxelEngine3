package org.bitwave.ve3.gui;

import java.util.ArrayList;
import java.util.List;

public final class UiDrawList {
    private final List<UiDrawCommand> commands = new ArrayList<>();

    public void clear() {
        commands.clear();
    }

    public void addFilledRect(UiRect rect, UiColor color) {
        commands.add(new UiDrawCommand.FilledRect(rect, color, 0f));
    }

    public void addFilledRect(UiRect rect, UiColor color, float cornerRadius) {
        commands.add(new UiDrawCommand.FilledRect(rect, color, cornerRadius));
    }

    public void addText(float x, float y, String text, UiColor color, float fontSize) {
        commands.add(new UiDrawCommand.Text(x, y, text, color, fontSize));
    }

    public void addLine(float x1, float y1, float x2, float y2, UiColor color, float thickness) {
        commands.add(new UiDrawCommand.Line(x1, y1, x2, y2, color, thickness));
    }

    public void pushClipRect(UiRect rect) {
        commands.add(new UiDrawCommand.PushClip(rect));
    }

    public void popClipRect() {
        commands.add(new UiDrawCommand.PopClip());
    }

    public List<UiDrawCommand> commands() {
        return commands;
    }
}
