/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AssessmentPane.java
 *
 * Created on 18-Nov-2010, 13:38:57
 */
package pet.frontend;

import pet.frontend.util.MyFocusTraversalPolicy;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import pet.usr.adapter.AssessmentListener;
import java.util.ArrayList;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.lang3.StringUtils;
import pet.annotation.AssessmentChoice;
import pet.annotation.AssessmentDescriptor;
import pet.annotation.adapter.AssessmentChoiceAdapter;
import pet.config.ContextHandler;

/**
 *
 * @author waziz
 */
public class GridAssessmentPage extends javax.swing.JDialog {

    private final AssessmentListener listener;
    private final int nAssessments;
    private final List<AssessmentDescriptor> descriptors;
    private final List<JComponent> combos;
    private final List<JTextPane> comments;
    private boolean done = false;
    
    private class GridAssessmentKeyListener implements KeyListener{

        private GridAssessmentPage page;
        
        public GridAssessmentKeyListener(GridAssessmentPage gap) {
            this.page = gap;
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_ENTER){
                this.page.btnFinish.doClick();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}
        
    }

    /** Creates new form AssessmentPane */
    public GridAssessmentPage(java.awt.Frame parent,
            boolean modal,
            final AssessmentListener listener,
            final List<MultiKey> summary,
            final List<AssessmentDescriptor> assessmentDescriptors) {
        super(parent, modal);
        this.descriptors = new ArrayList<AssessmentDescriptor>(assessmentDescriptors);
        this.listener = listener;

        final boolean showComments = !ContextHandler.disableCommentOnAssessment();
        final boolean html = ContextHandler.renderHTML();

        initComponents();
        header.setLayout(new SpringLayout());  //new GridLayout(0, 1, 2, 1));
        body.setLayout(new SpringLayout()); 
        if (ContextHandler.maximiseAssessmentPage()){
            this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        } 

        nAssessments = descriptors.size();

        for (final MultiKey pair : summary) {
            final JLabel info = new JLabel(pair.getKey(0).toString());
            final JTextPane text = new JTextPane();
            if (html) {
                text.setContentType("text/html");
            }
            text.setText(pair.getKey(1).toString());
            text.setEditable(false);
            final JScrollPane textScroll = new JScrollPane(text,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            info.setLabelFor(text);
            header.add(info);
            header.add(textScroll);
        }

        final List<Component> focusOrder = new ArrayList<Component>();
        comments = new ArrayList<JTextPane>();
        combos = new ArrayList<JComponent>();
        for (final AssessmentDescriptor descriptor : descriptors) {
            final JLabel question = new JLabel(descriptor.getQuestion());
            body.add(question);
            JComponent answers;
            if (descriptor.maxSelection() == 1) {
                answers = new JComboBox(descriptor.getAnswers().toArray());
            } else {
                answers = new JList(descriptor.getAnswers().toArray());
                ((JList) answers).setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            }
            answers.addKeyListener(new GridAssessmentKeyListener(this));
            combos.add(answers);
            JScrollPane answersPanel = new JScrollPane(answers,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            answersPanel.addKeyListener(new GridAssessmentKeyListener(this));
            body.add(answersPanel);
            //body.add(answers);
            focusOrder.add(answers);
            final JTextPane comment = new JTextPane();
            comments.add(comment);
            if (showComments) {
                final JLabel label = new JLabel("Comment on '" + descriptor.getId() + "'");
                label.setLabelFor(comment);
                body.add(label);
                JScrollPane commentPanel = new JScrollPane(comment);
                commentPanel.addKeyListener(new GridAssessmentKeyListener(this));
                body.add(commentPanel);
            }
        }
        focusOrder.add(btnFinish);
        if (showComments) {
            for (final Component comment : comments) {
                focusOrder.add(comment);
            }
            focusOrder.add(btnFinish);
        }


        SpringUtilities.makeCompactGrid(header,
                2 * summary.size(), 1, //rows, cols
                3, 3, //initX, initY
                3, 3);       //xPad, yPad
        final int factor = (showComments) ? 2 : 1;
        SpringUtilities.makeCompactGrid(body,
                2 * (descriptors.size() * factor), 1, //rows, cols
                3, 3, //initX, initY
                3, 3);       //xPad, yPad


        focusOrder.get(0).requestFocusInWindow();
        this.setFocusTraversalPolicy(new MyFocusTraversalPolicy(focusOrder));
        this.setTitle("Assessing " + StringUtils.join(descriptors.iterator(), " and "));

        //Adjust interface:
        this.header.setFocusable(false);
        this.body.setFocusable(false);
        this.addKeyListener(new GridAssessmentKeyListener(this));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnFinish = new javax.swing.JButton();
        header = new javax.swing.JPanel();
        body = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnFinish.setText("Finish");
        btnFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinishActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 689, Short.MAX_VALUE)
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 183, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 689, Short.MAX_VALUE)
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 245, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(body, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(header, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFinish))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnFinish)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        if (done) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please complete all the assessments", "Assessing", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_formWindowClosing

    private void btnFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinishActionPerformed
        // TODO add your handling code here:
        final List<AssessmentChoice> answers = new ArrayList<AssessmentChoice>();
        for (int i = 0; i < nAssessments; i++) {
            if (combos.get(i) instanceof JComboBox) {
                final JComboBox combo = (JComboBox) combos.get(i);
                if (combo.getSelectedIndex() >= 0) {
                    answers.add(new AssessmentChoiceAdapter(descriptors.get(i).getId(), combo.getSelectedItem().toString(), comments.get(i).getText()));
                }
            } else {
                final JList list = (JList) combos.get(i);
                final int selected = list.getSelectedIndices().length;
                int max = (descriptors.get(i).maxSelection() == -1)? selected : descriptors.get(i).maxSelection();
                if (selected > 0 && selected <= max) {
                    final List<String> group = new ArrayList<String>();
                    for(final Object o : list.getSelectedValues()){
                        group.add(o.toString());
                    }
                    answers.add(new AssessmentChoiceAdapter(descriptors.get(i).getId(), group, comments.get(i).getText()));
                } else if (selected > max) {
                    JOptionPane.showMessageDialog(this, descriptors.get(i).getId() + " accepts up to " + max + " answers." , "Assessing", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        done = answers.size() == nAssessments;
        if (done) {
            listener.assess(answers);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please complete all the assessments", "Assessing", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnFinishActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                AssessmentPane dialog = new AssessmentPane(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private javax.swing.JButton btnFinish;
    private javax.swing.JPanel header;
    // End of variables declaration//GEN-END:variables
}
