/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */
package gui.viewer;

import automata.State;
import automata.Automaton;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 * The <CODE>StateDrawer</CODE> class handles the drawing of individual states.
 * 
 * @author Thomas Finley
 */
public class StateDrawer {

    /**
     * The default constructor for a <CODE>StateDrawer</CODE>
     */
    public StateDrawer() {
        this.radius = STATE_RADIUS;
    }

    /**
     * Creates a <CODE>StateDrawer</CODE> with states drawn to a particular
     * radius.
     * 
     * @param radius
     *            the radius of states drawn by this state drawer
     */
    public StateDrawer(int radius) {
        this.radius = radius;
    }

    /**
     * Draws an individual state with all the default modes.
     * 
     * @param g
     *            the graphics object to draw upon
     * @param automaton
     *            the automaton this state is a part of
     * @param state
     *            the state to draw
     */
    public void drawState(Graphics g, Automaton automaton, State state) {
        this.drawState(g, automaton, state, state.getPoint());
    }

    /**
     * Draws an individual state, but at the point specified rather than at the
     * point of the state's <CODE>getPoint()</CODE> method.
     * 
     * @param g
     *            the graphics object to draw upon
     * @param state
     *            the state to draw
     * @param automaton
     *            the automaton this state is a part of
     * @param point
     *            the point to draw the state at
     */
    public void drawState(Graphics g, Automaton automaton, State state,
            Point point) {
        drawState(g, automaton, state, point, STATE_COLOR);
    }

    /**
     * Draws an individual state at the point specified with the color
     * specified.
     * 
     * @param g
     *            the graphics object to draw upon
     * @param state
     *            the state to draw
     * @param automaton
     *            the automaton this state is a part of
     * @param point
     *            the point to draw the state at
     * @param color
     *            the color of the inner portion of the state
     */
    public void drawState(Graphics g, Automaton automaton, State state,
            Point point, Color color) {
        drawArea(g, automaton, state, point, color);
        return;
    }

    /**
     * @param state
     */
    private void drawArea(Graphics g, Automaton automaton, State state,
            Point point, Color color) {
        drawBackground(g, state, point, color);
        g.setColor(Color.black);
        int dx = ((int) g.getFontMetrics().getStringBounds(state.getName(), g).getWidth()) >> 1;
        int dy = ((int) g.getFontMetrics().getAscent()) >> 1;

        g.drawString(state.getName(), point.x - dx, point.y + dy);
        g.drawOval(point.x - radius - dx, point.y - radius - dx,
                2 * (radius + dx), 2 * (radius + dx));
        if (automaton.isFinalState(state)) {
            g.drawOval(point.x - radius - dx + 3, point.y - radius - dx + 3,
                    2 * (radius + dx) - 6, 2 * (radius + dx) - 6);
        }
        if (automaton.getInitialState() == state) {
            int[] x = {point.x - radius - dx, point.x - ((radius + dx) << 1),
                point.x - ((radius + dx) << 1)};
            int[] y = {point.y, point.y - radius, point.y + radius};
            g.setColor(Color.white);
            g.fillPolygon(x, y, 3);
            g.setColor(Color.black);
            g.drawPolygon(x, y, 3);
        }
    }

    /**
     * Draws the state label for a given state.
     * 
     * @param state
     *            the state whose label we must draw
     * @param point
     *            the point of the state, which is NOT the same thing as any
     *            point where the label gets drawn
     * @param color
     *            the background color of the label
     */
    public void drawStateLabel(Graphics g, State state, Point point, Color color) {
        String[] labels = state.getLabels();
        if (labels.length == 0) {
            return;
        }

        int ascent = g.getFontMetrics().getAscent();
        int textWidth = 0;

        for (int i = 0; i < labels.length; i++) {
            Rectangle2D bounds = g.getFontMetrics().getStringBounds(labels[i],
                    g);
            textWidth = Math.max((int) bounds.getWidth(), textWidth);
        }
        // Width of the box.
        int width = textWidth + (STATE_LABEL_PAD << 1);
        // Upper corner of the box.
        int x = point.x - (width >> 1);
        int y = point.y + STATE_RADIUS - STATE_LABEL_PAD;
        // Where the y point of the baseline is.
        int baseline = y;

        g.setColor(color);
        // g.fillRect(x - 26, y - 46, 2 * radius + 20, 2 * radius + 20);
        g.setColor(Color.black);
        for (int i = 0; i < labels.length; i++) {
            baseline += ascent + STATE_LABEL_PAD;
            g.drawString(labels[i], x + STATE_LABEL_PAD, baseline - 44);
        }
        // g.drawOval(x - 26, y - 46, 2 * radius + 20, 2 * radius + 20);
    }

    /**
     * Draws the background of the state.
     * 
     * @param g
     *            the graphics object to draw upon
     * @param state
     *            the state object to draw
     * @param point
     *            the point where the background should be centered
     * @param color
     *            the color of the background, if supported by this class
     * @param bool
     */
    public void drawBackground(Graphics g, State state, Point point, Color color) {
        g.setColor(color);
        if (state.isSelected()) {
            g.setColor(new Color(100, 200, 200));
        }
        int dx = ((int) g.getFontMetrics().getStringBounds(state.getName(), g).getWidth()) >> 1;
        int dy = ((int) g.getFontMetrics().getAscent()) >> 1;

        // if (state.getInternalName() == null)
        g.fillOval(point.x - radius - dx, point.y - radius - dx,
                2 * (radius + dx), 2 * (radius + dx));
        // else {
        // g.fillRect(point.x - radius, point.y - radius, 2 * radius,
        // 2 * radius);
        // }
    }

    /**
     * Returns the radius of a state as drawn by this state drawer.
     * 
     * @return the radius of a state
     */
    public int getRadius() {
        return radius;
    }
    /** The radius we should draw states at. */
    private int radius = STATE_RADIUS;
    /** The default radius of a state. */
    public static final int STATE_RADIUS = 32;
    /** The base color for states. */
    public static final Color STATE_COLOR = new Color(255, 255, 150);
    /** The label padding for states. */
    public static final int STATE_LABEL_PAD = 0;
}
