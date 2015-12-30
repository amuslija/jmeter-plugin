package com.atlantbh.jmeter.plugins.jtlparser.gui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.atlantbh.jmeter.plugins.jtlparser.JtlParserLogger;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;

public class JtlParserLoggerGui extends AbstractListenerGui {

    public static final String WIKIPAGE = "JtlParserLogger";
    private JTextField filename;
    private String lastPath = ".";

    public JtlParserLoggerGui() {
        super();
        init();
    }

    @Override
    public String getStaticLabel() {
        return "Jtl Parser Listener";
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public TestElement createTestElement() {
        TestElement testElement = JtlParserLogger.getJtlParserLogger();
        modifyTestElement(testElement);
        System.out.println("Object Created: " + testElement.toString());
        (new Throwable()).printStackTrace();
        return testElement;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        super.configureTestElement(te);
        if (te instanceof JtlParserLogger ) {
            JtlParserLogger jtlParserLogger = (JtlParserLogger) te;
            jtlParserLogger.setOuputFile(filename.getText());
        }
    }
    @Override
    public void configure(TestElement testElement) {
        super.configure(testElement);
        JtlParserLogger jtlParserLogger = (JtlParserLogger) testElement;
        filename.setText(jtlParserLogger.getOutputFile());
    }


    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        JPanel mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Filename: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, filename = new JTextField(20));
        JButton browseButton = new JButton("Browse...");
        addToPanel(mainPanel, labelConstraints, 2, 1, browseButton);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(lastPath);
                if (chooser != null) {
                    if(GuiPackage.getInstance() != null) {
                        int returnVal = chooser.showOpenDialog(GuiPackage.getInstance().getMainFrame());
                        if(returnVal == JFileChooser.APPROVE_OPTION) {
                            filename.setText(chooser.getSelectedFile().getPath());

                        }
                        lastPath = chooser.getCurrentDirectory().getPath();
                    }
                }
            }
        });

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }
}



