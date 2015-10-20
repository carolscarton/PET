/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pet.frontend.components;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import pet.annotation.Segment;
import pet.annotation.adapter.StringSentence;

/**
 *
 * @author waziz
 */
public abstract class AbstractUnitGUI extends JTextPane implements UnitGUI {

    private static interface Handler {

        String tip(final String tip);
    }

    public enum Tip {

        SHOW(new Handler() {

            public String tip(String tip) {
                return tip;
            }
        }),
        HIDE(new Handler() {

            public String tip(String tip) {
                return null;
            }
        });

        private final Handler handler;

        Tip(final Handler handler) {
            this.handler = handler;
        }

        public String get(final String tip) {
            return handler.tip(tip);
        }
    }

    protected Segment displaying;
    protected final Tip tipHandler;
    protected final UnitGUIType type;
    protected final int id;

    public AbstractUnitGUI(final Tip tipHandler, final UnitGUIType type, final int id) {
        super();
        this.displaying = StringSentence.getEmptySentence();
        this.tipHandler = tipHandler;
        this.type = type;
        this.id = id;
    }

    protected void updateFacade() {
        super.setText(this.displaying.toString());
//        Highlighter highlighter = super.getHighlighter();
//        HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.blue);
//        try {
//            highlighter.addHighlight(0, 5, painter);
//        } catch (BadLocationException ex) {
//            Logger.getLogger(AbstractUnitGUI.class.getName()).log(Level.SEVERE, null, ex);
//        }

        setCaretPosition(0);
        super.setToolTipText(this.tipHandler.get(this.displaying.getProducer()));
    }

    /**
     * Underlying JTextPane
     *
     * @return
     */
    public JTextPane underlying() {
        return (JTextPane) this;
    }

    @Override
    public UnitGUIType getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

}
