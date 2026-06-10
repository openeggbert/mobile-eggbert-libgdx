package com.openeggbert.mobileeggbert;

public class Jauge {
    private Pixmap m_pixmap;
    private Sound m_sound;
    private boolean m_bHide;
    private TinyPoint m_pos;
    private TinyPoint m_dim;
    private int m_mode;
    private int m_level;
    private boolean m_bMinimizeRedraw;
    private boolean m_bRedraw;
    private double m_zoom;

    public double GetZoom() { return m_zoom; }
    public void SetZoom(double v) { m_zoom = v; }

    public Jauge() {
        m_mode = 0;
        m_bHide = true;
        m_bMinimizeRedraw = false;
        m_bRedraw = false;
        m_zoom = 1.0;
    }

    public boolean Create(Pixmap pixmap, Sound sound, TinyPoint pos, int mode, boolean bMinimizeRedraw) {
        m_pixmap = pixmap;
        m_sound = sound;
        m_mode = mode;
        m_bMinimizeRedraw = bMinimizeRedraw;
        m_bHide = true;
        m_pos = pos.Copy();
        m_dim = new TinyPoint(124, 22);
        m_level = 0;
        m_bRedraw = true;
        return true;
    }

    public void Draw() {
        if (m_bMinimizeRedraw && !m_bRedraw) return;
        m_bRedraw = false;
        if (!m_bHide) {
            int filledWidth = m_level * 114 / 100;
            TinyRect rect = new TinyRect(0, 124, 0, 22);
            m_pixmap.DrawPart(5, m_pos, rect, m_zoom);
            if (filledWidth > 0) {
                rect = new TinyRect(0, 6 + filledWidth, 22 * m_mode, 22 * (m_mode + 1));
                m_pixmap.DrawPart(5, m_pos, rect, m_zoom);
            }
        }
    }

    public void Redraw() { m_bRedraw = true; }

    public int GetLevel() { return m_level; }
    public void SetLevel(int level) {
        level = Math.max(0, Math.min(100, level));
        if (m_level != level) m_bRedraw = true;
        m_level = level;
    }

    public int GetMode() { return m_mode; }
    public void SetMode(int mode) {
        if (m_mode != mode) m_bRedraw = true;
        m_mode = mode;
    }

    public boolean GetHide() { return m_bHide; }
    public void SetHide(boolean bHide) {
        if (m_bHide != bHide) m_bRedraw = true;
        m_bHide = bHide;
    }

    public TinyPoint GetPos() { return m_pos; }
    public void SetRedraw() { m_bRedraw = true; }
}
