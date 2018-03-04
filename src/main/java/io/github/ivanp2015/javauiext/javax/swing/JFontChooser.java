/*
 * Java UI Extensions project.
 * https://ivanp2015.github.io/javauiext
 * 
 * Copyright (c) 2018, Ivan Pizhenko. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Ivan Pizhenko designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Ivan Pizhenko in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */

/*
 * Copyright 2004-2005,2007-2008 Masahiko SAWAI All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.ivanp2015.javauiext.javax.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;

/**
 * The {@code JFontChooser} class is a Swing component for a font selection.
 * This class has a {@link javax.swing.JColorChooser}-like APIs.
 * 
 * The following code pops up a font chooser dialog
 * and prints out selected font, of user has pressed OK.
 * {@code
 *   JFontChooser fontChooser = new JFontChooser();
 *   int result = fontChooser.showDialog(parent);
 *   if (result == JFontChooser.OK_OPTION)
 *   {
 *       Font font = fontChooser.getSelectedFont(); 
 *       System.out.println("Selected Font: " + font); 
 *   }
 * }
 * 
 * @see javax.swing.JColorChooser
 */
public class JFontChooser extends JComponent
{
    private static final long serialVersionUID = 1L;

    /**
     * Return value from {@code showDialog()}.
     * @see #showDialog
     */
    public static final int OK_OPTION = 0;

    /**
     * Return value from {@code showDialog()}.
     * @see #showDialog
     */
    public static final int CANCEL_OPTION = 1;

    /**
     * Return value from {@code showDialog()}.
     * @see #showDialog
     */
    public static final int ERROR_OPTION = -1;

    private static final Font DEFAULT_SELECTED_FONT = new Font("Serif", Font.PLAIN, 12);
    
    private static final int[] FONT_STYLE_CODES = {
        Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC
    };

    private static final String[] DEFAULT_FONT_SIZE_STRINGS = {
        "8", "9", "10", "11", "12", "14", "16", "18", "20",
        "22", "24", "26", "28", "36", "48", "72",
    };

    private static final Object bundleRegistrationLock = new Object();
    private static volatile boolean bundleRegistered;

    protected int dialogResultValue = ERROR_OPTION;    
    private String[] fontStyleNames;
    private String[] fontFamilyNames;
    private String[] fontSizeStrings;
    private JTextField fontFamilyTextField;
    private JTextField fontStyleTextField;
    private JTextField fontSizeTextField;
    private JList<String> fontNameList;
    private JList<String> fontStyleList;
    private JList<String> fontSizeList;
    private JPanel fontNamePanel;
    private JPanel fontStylePanel;
    private JPanel fontSizePanel;
    private JPanel samplePanel;
    private JTextField sampleText;

    /**
     * Shows a modal font chooser dialog and blocks until the
     * dialog is hidden.  If the user presses the "OK" button, then
     * this method hides/disposes the dialog and returns the selected color.
     * If the user presses the "Cancel" button or closes the dialog without
     * pressing "OK", then this method hides/disposes the dialog and returns
     * {@code null}.
     *
     * @param component          the parent component for the dialog.
     * @param title              the String containing the dialog's title.
     * @param initialFont        the initial font set when the font chooser is shown.
     * @param fontSizeStrings    array of font size strings. 
     * @return the selected color or {@code null} if the user opted out.
     * @exception HeadlessException if {@link GraphicsEnvironment#isHeadless()}
     * returns {@code true}.
     * @see java.awt.GraphicsEnvironment#isHeadless()
     */
    public static Font showDialog(Component component,
        String title, Font initialFont, String[] fontSizeStrings) throws HeadlessException {

        final JFontChooser pane = new JFontChooser(initialFont != null 
                ? initialFont : DEFAULT_SELECTED_FONT);
        final FontTracker ok = new FontTracker(pane);
        JDialog dialog = createDialog(component, title, true, pane, ok, null);
        dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
        dialog.setVisible(true); // blocks until user brings dialog down...
        return ok.getFont();
    }

    /**
     * Creates and returns a new dialog containing the specified
     * {@code JColorChooser} pane along with "OK", "Cancel", and "Reset"
     * buttons. If the "OK" or "Cancel" buttons are pressed, the dialog is
     * automatically hidden (but not disposed).  If the "Reset"
     * button is pressed, the font chooser's font will be reset to the
     * font which was set the last time {@code show()} was invoked on the
     * dialog and the dialog will remain showing.
     *
     * @param component      the parent component for the dialog.
     * @param title          the title for the dialog.
     * @param modal          a boolean. When true, the remainder of the program
     *                       is inactive until the dialog is closed.
     * @param chooserPane    the color-chooser to be placed inside the dialog.
     * @param okListener     the ActionListener invoked when "OK" is pressed.
     * @param cancelListener the ActionListener invoked when "Cancel" is pressed.
     * @return a new dialog containing the font chooser pane.
     * @exception HeadlessException if {@link GraphicsEnvironment#isHeadless()}
     * returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless()
     */
    public static JDialog createDialog(Component component, String title, boolean modal,
            JFontChooser chooserPane, ActionListener okListener,
            ActionListener cancelListener) throws HeadlessException {
        final Window window = JOptionPaneLocal.getWindowForComponent1(component);
        FontChooserDialog dialog;
        if (window instanceof Frame) {
            dialog = new FontChooserDialog((Frame)window, title, modal, component, chooserPane,
                    okListener, cancelListener);
        } else {
            dialog = new FontChooserDialog((Dialog)window, title, modal, component, chooserPane,
                    okListener, cancelListener);
        }
        dialog.getAccessibleContext().setAccessibleDescription(title);
        return dialog;
    }

    /**
     * Constructs a {@code JFontChooser} object.
     */
    public JFontChooser() {
        this(DEFAULT_SELECTED_FONT, DEFAULT_FONT_SIZE_STRINGS);
    }

    /**
     * Constructs a {@code JFontChooser} object.
     *
     * @param initialFont initial font.
     */
    public JFontChooser(Font initialFont) {
        this(initialFont, DEFAULT_FONT_SIZE_STRINGS);
    }

    /**
     * Constructs a {@code JFontChooser} object using the given font size array.
     *
     * @param initialFont initial font.
     * @param fontSizeStrings  the array of font size string.
     */
    public JFontChooser(Font initialFont, String[] fontSizeStrings) {
        if (fontSizeStrings == null) {
            fontSizeStrings = DEFAULT_FONT_SIZE_STRINGS;
        }
        this.fontSizeStrings = fontSizeStrings;

        final JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.X_AXIS));
        selectPanel.add(getFontFamilyPanel());
        selectPanel.add(getFontStylePanel());
        selectPanel.add(getFontSizePanel());

        final JPanel contentsPanel = new JPanel();
        contentsPanel.setLayout(new GridLayout(2, 1));
        contentsPanel.add(selectPanel, BorderLayout.NORTH);
        contentsPanel.add(getSamplePanel(), BorderLayout.CENTER);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(contentsPanel);
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setSelectedFont(initialFont != null ? initialFont : DEFAULT_SELECTED_FONT);
    }

    /**
     * Gets the family name of the selected font.
     *
     * @return  the font family of the selected font.
     * @see #setSelectedFontFamily
     */
    public String getSelectedFontFamily() {
        return getFontFamilyList().getSelectedValue();
    }

    /**
     * Gets the style of the selected font.
     * 
     * @return  the style of the selected font.
     *          {@code Font.PLAIN}, {@code Font.BOLD},
     *          {@code Font.ITALIC}, {@code Font.BOLD|Font.ITALIC}
     *
     * @see java.awt.Font#PLAIN
     * @see java.awt.Font#BOLD
     * @see java.awt.Font#ITALIC
     * @see #setSelectedFontStyle
     */
    public int getSelectedFontStyle() {
        final int index = getFontStyleList().getSelectedIndex();
        return FONT_STYLE_CODES[index];
    }

    /**
     * Gets the size of the selected font.
     * 
     * @return  the size of the selected font
     * @see #setSelectedFontSize
     */
    public int getSelectedFontSize() {
        int fontSize = 1;
        String fontSizeString = getFontSizeTextField().getText();
        while (true) {
            try {
                fontSize = Integer.parseInt(fontSizeString);
                break;
            } catch (NumberFormatException e) {
                fontSizeString = getFontSizeList().getSelectedValue();
                getFontSizeTextField().setText(fontSizeString);
            }
        }

        return fontSize;
    }

    /**
     * Gets the selected font.
     * 
     * @return the selected font
     * @see #setSelectedFont
     * @see java.awt.Font
     */
    public Font getSelectedFont() {
        return new Font(getSelectedFontFamily(),
            getSelectedFontStyle(), getSelectedFontSize());
    }

    /**
     * Sets the family name of the selected font.
     * 
     * @param name  the family name of the selected font. 
     * @see #getSelectedFontFamily
     */
    public void setSelectedFontFamily(String name)
    {
        final String[] names = getFontFamilies();
        for (int i = 0; i < names.length; i++) {
            if (names[i].toLowerCase().equals(name.toLowerCase())) {
                getFontFamilyList().setSelectedIndex(i);
                break;
            }
        }
        updateSampleFont();
    }

    /**
     * Sets the style of the selected font.
     * 
     * @param style  the size of the selected font.
     *               {@code Font.PLAIN}, {@code Font.BOLD},
     *               {@code Font.ITALIC}, or
     *               {@code Font.BOLD|Font.ITALIC}.
     * @see java.awt.Font#PLAIN
     * @see java.awt.Font#BOLD
     * @see java.awt.Font#ITALIC
     * @see #getSelectedFontStyle
     */
    public void setSelectedFontStyle(int style)
    {
        for (int i = 0; i < FONT_STYLE_CODES.length; i++) {
            if (FONT_STYLE_CODES[i] == style) {
                getFontStyleList().setSelectedIndex(i);
                break;
            }
        }
        updateSampleFont();
    }

    /**
     * Set the size of the selected font.
     * @param size the size of the selected font
     *
     * @see #getSelectedFontSize
     */
    public void setSelectedFontSize(int size) {
        String sizeString = String.valueOf(size);
        for (int i = 0; i < this.fontSizeStrings.length; i++) {
            if (this.fontSizeStrings[i].equals(sizeString)) {
                getFontSizeList().setSelectedIndex(i);
                break;
            }
        }
        getFontSizeTextField().setText(sizeString);
        updateSampleFont();
    }

    /**
     * Set the selected font.
     * @param font the selected font
     *
     * @see #getSelectedFont
     * @see java.awt.Font
     */
    public void setSelectedFont(Font font) {
        setSelectedFontFamily(font.getFamily());
        setSelectedFontStyle(font.getStyle());
        setSelectedFontSize(font.getSize());
    }

    protected JTextField getFontFamilyTextField() {
        if (fontFamilyTextField == null) {
            fontFamilyTextField = new JTextField();
            fontFamilyTextField.addFocusListener(
                new TextFieldFocusHandlerForTextSelection(fontFamilyTextField));
            fontFamilyTextField.addKeyListener(
                new TextFieldKeyHandlerForListSelectionUpDown(getFontFamilyList()));
            fontFamilyTextField.getDocument().addDocumentListener(
                new ListSearchTextFieldDocumentHandler(getFontFamilyList()));

        }
        return fontFamilyTextField;
    }

    protected JTextField getFontStyleTextField() {
        if (fontStyleTextField == null) {
            fontStyleTextField = new JTextField();
            fontStyleTextField.addFocusListener(
                new TextFieldFocusHandlerForTextSelection(fontStyleTextField));
            fontStyleTextField.addKeyListener(
                new TextFieldKeyHandlerForListSelectionUpDown(getFontStyleList()));
            fontStyleTextField.getDocument().addDocumentListener(
                new ListSearchTextFieldDocumentHandler(getFontStyleList()));
        }
        return fontStyleTextField;
    }

    protected JTextField getFontSizeTextField() {
        if (fontSizeTextField == null) {
            fontSizeTextField = new JTextField();
            fontSizeTextField.addFocusListener(
                new TextFieldFocusHandlerForTextSelection(fontSizeTextField));
            fontSizeTextField.addKeyListener(
                new TextFieldKeyHandlerForListSelectionUpDown(getFontSizeList()));
            fontSizeTextField.getDocument().addDocumentListener(
                new ListSearchTextFieldDocumentHandler(getFontSizeList()));
        }
        return fontSizeTextField;
    }

    protected JList<String> getFontFamilyList() {
        if (fontNameList == null) {
            fontNameList = new JList<String>(getFontFamilies());
            fontNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            fontNameList.addListSelectionListener(
                new ListSelectionHandler(getFontFamilyTextField()));
            fontNameList.setSelectedIndex(0);
            //fontNameList.setFont(DEFAULT_FONT);
            fontNameList.setFocusable(false);
        }
        return fontNameList;
    }

    protected JList<String> getFontStyleList() {
        if (fontStyleList == null) {
            fontStyleList = new JList<String>(getFontStyleNames());
            fontStyleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            fontStyleList.addListSelectionListener(
                new ListSelectionHandler(getFontStyleTextField()));
            fontStyleList.setSelectedIndex(0);
            //fontStyleList.setFont(DEFAULT_FONT);
            fontStyleList.setFocusable(false);
        }
        return fontStyleList;
    }

    protected JList<String> getFontSizeList() {
        if (fontSizeList == null) {
            fontSizeList = new JList<String>(this.fontSizeStrings);
            fontSizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            fontSizeList.addListSelectionListener(
                new ListSelectionHandler(getFontSizeTextField()));
            fontSizeList.setSelectedIndex(0);
            //fontSizeList.setFont(DEFAULT_FONT);
            fontSizeList.setFocusable(false);
        }
        return fontSizeList;
    }

    protected class ListSelectionHandler implements ListSelectionListener {
        private JTextComponent textComponent;

        ListSelectionHandler(JTextComponent textComponent) {
            this.textComponent = textComponent;
        }

        @SuppressWarnings("unchecked")
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {
                final JList<String> list = (JList<String>) e.getSource();
                final String selectedValue = (String) list.getSelectedValue();
                final String oldValue = textComponent.getText();
                textComponent.setText(selectedValue);
                if (!oldValue.equalsIgnoreCase(selectedValue))
                {
                    textComponent.selectAll();
                    textComponent.requestFocus();
                }

                updateSampleFont();
            }
        }
    }

    protected class TextFieldFocusHandlerForTextSelection extends FocusAdapter {
        private JTextComponent textComponent;

        public TextFieldFocusHandlerForTextSelection(JTextComponent textComponent) {
            this.textComponent = textComponent;
        }

        public void focusGained(FocusEvent e) {
            textComponent.selectAll();
        }

        public void focusLost(FocusEvent e) {
            textComponent.select(0, 0);
            updateSampleFont();
        }
    }

    protected class TextFieldKeyHandlerForListSelectionUpDown extends KeyAdapter {
        private JList<String> targetList;

        public TextFieldKeyHandlerForListSelectionUpDown(JList<String> list) {
            this.targetList = list;
        }

        public void keyPressed(KeyEvent e) {
            int i = targetList.getSelectedIndex();
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP: {
                    i = targetList.getSelectedIndex() - 1;
                    if (i < 0) {
                        i = 0;
                    }
                    targetList.setSelectedIndex(i);
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    final int listSize = targetList.getModel().getSize();
                    i = targetList.getSelectedIndex() + 1;
                    if (i >= listSize) {
                        i = listSize - 1;
                    }
                    targetList.setSelectedIndex(i);
                    break;
                }
                default: break;
            }
        }
    }

    protected class ListSearchTextFieldDocumentHandler implements DocumentListener {
        JList<String> targetList;

        public ListSearchTextFieldDocumentHandler(JList<String> targetList) {
            this.targetList = targetList;
        }

        public void insertUpdate(DocumentEvent e) {
            update(e);
        }

        public void removeUpdate(DocumentEvent e) {
            update(e);
        }

        public void changedUpdate(DocumentEvent e) {
            update(e);
        }

        private void update(DocumentEvent event) {
            String newValue = "";
            try {
                Document doc = event.getDocument();
                newValue = doc.getText(0, doc.getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            if (newValue.length() > 0) {
                int index = targetList.getNextMatch(newValue, 0, Position.Bias.Forward);
                if (index < 0) {
                    index = 0;
                }
                targetList.ensureIndexIsVisible(index);

                final String matchedName = targetList.getModel().getElementAt(index).toString();
                if (newValue.equalsIgnoreCase(matchedName)) {
                    if (index != targetList.getSelectedIndex()) {
                        SwingUtilities.invokeLater(new ListSelector(index));
                    }
                }
            }
        }

        public class ListSelector implements Runnable {
            private int index;

            public ListSelector(int index) {
                this.index = index;
            }

            public void run() {
                targetList.setSelectedIndex(this.index);
            }
        }
    }

    protected class DialogOKAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        protected static final String ACTION_NAME = "OK";
        private JDialog dialog;

        protected DialogOKAction(JDialog dialog) {
            this.dialog = dialog;
            putValue(Action.DEFAULT, ACTION_NAME);
            putValue(Action.ACTION_COMMAND_KEY, ACTION_NAME);
            putValue(Action.NAME, getLocalizedMessage("JFontChooser.okText"));
        }

        public void actionPerformed(ActionEvent e) {
            dialogResultValue = OK_OPTION;
            dialog.setVisible(false);
        }
    }

    protected class DialogCancelAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        protected static final String ACTION_NAME = "Cancel";
        private JDialog dialog;

        protected DialogCancelAction(JDialog dialog) {
            this.dialog = dialog;
            putValue(Action.DEFAULT, ACTION_NAME);
            putValue(Action.ACTION_COMMAND_KEY, ACTION_NAME);
            putValue(Action.NAME, getLocalizedMessage("JFontChooser.cancelText"));
        }

        public void actionPerformed(ActionEvent e) {
            dialogResultValue = CANCEL_OPTION;
            dialog.setVisible(false);
        }
    }

    protected void updateSampleFont() {
        getSampleTextField().setFont(getSelectedFont());
    }

    protected JPanel getFontFamilyPanel() {
        if (fontNamePanel == null) {
            fontNamePanel = new JPanel();
            fontNamePanel.setLayout(new BorderLayout());
            fontNamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            fontNamePanel.setPreferredSize(new Dimension(180, 130));

            final JScrollPane scrollPane = new JScrollPane(getFontFamilyList());
            scrollPane.getVerticalScrollBar().setFocusable(false);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            final JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            p.add(getFontFamilyTextField(), BorderLayout.NORTH);
            p.add(scrollPane, BorderLayout.CENTER);

            final JLabel label = new JLabel(getLocalizedMessage("JFontChooser.fontNameText"));
            label.setHorizontalAlignment(JLabel.LEFT);
            label.setHorizontalTextPosition(JLabel.LEFT);
            label.setLabelFor(getFontFamilyTextField());
            label.setDisplayedMnemonic('F');

            fontNamePanel.add(label, BorderLayout.NORTH);
            fontNamePanel.add(p, BorderLayout.CENTER);
        }
        return fontNamePanel;
    }

    protected JPanel getFontStylePanel() {
        if (fontStylePanel == null) {
            fontStylePanel = new JPanel();
            fontStylePanel.setLayout(new BorderLayout());
            fontStylePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            fontStylePanel.setPreferredSize(new Dimension(140, 130));

            final JScrollPane scrollPane = new JScrollPane(getFontStyleList());
            scrollPane.getVerticalScrollBar().setFocusable(false);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            final JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            p.add(getFontStyleTextField(), BorderLayout.NORTH);
            p.add(scrollPane, BorderLayout.CENTER);

            final JLabel label = new JLabel(getLocalizedMessage("JFontChooser.fontStyleText"));
            label.setHorizontalAlignment(JLabel.LEFT);
            label.setHorizontalTextPosition(JLabel.LEFT);
            label.setLabelFor(getFontStyleTextField());
            label.setDisplayedMnemonic('Y');

            fontStylePanel.add(label, BorderLayout.NORTH);
            fontStylePanel.add(p, BorderLayout.CENTER);
        }
        return fontStylePanel;
    }

    protected JPanel getFontSizePanel() {
        if (fontSizePanel == null) {
            fontSizePanel = new JPanel();
            fontSizePanel.setLayout(new BorderLayout());
            fontSizePanel.setPreferredSize(new Dimension(70, 130));
            fontSizePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            final JScrollPane scrollPane = new JScrollPane(getFontSizeList());
            scrollPane.getVerticalScrollBar().setFocusable(false);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            final JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            p.add(getFontSizeTextField(), BorderLayout.NORTH);
            p.add(scrollPane, BorderLayout.CENTER);

            final JLabel label = new JLabel(getLocalizedMessage("JFontChooser.fontSizeText"));
            label.setHorizontalAlignment(JLabel.LEFT);
            label.setHorizontalTextPosition(JLabel.LEFT);
            label.setLabelFor(getFontSizeTextField());
            label.setDisplayedMnemonic('S');

            fontSizePanel.add(label, BorderLayout.NORTH);
            fontSizePanel.add(p, BorderLayout.CENTER);
        }
        return fontSizePanel;
    }

    protected JPanel getSamplePanel() {
        if (samplePanel == null) {
            final Border titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), getLocalizedMessage("JFontChooser.sampleText"));
            final Border empty = BorderFactory.createEmptyBorder(5, 10, 10, 10);
            final Border border = BorderFactory.createCompoundBorder(titledBorder, empty);

            samplePanel = new JPanel();
            samplePanel.setLayout(new BorderLayout());
            samplePanel.setBorder(border);
            samplePanel.add(getSampleTextField(), BorderLayout.CENTER);
        }
        return samplePanel;
    }

    protected JTextField getSampleTextField() {
        if (sampleText == null) {
            final Border lowered = BorderFactory.createLoweredBevelBorder();
            sampleText = new JTextField(getLocalizedMessage("JFontChooser.sampleStringText"));
            sampleText.setEditable(false);
            sampleText.setBorder(lowered);
            sampleText.setPreferredSize(new Dimension(300, 100));
        }
        return sampleText;
    }

    protected String[] getFontFamilies() {
        if (fontFamilyNames == null) {
            final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            fontFamilyNames = env.getAvailableFontFamilyNames();
        }
        return fontFamilyNames;
    }

    protected String[] getFontStyleNames() {
        if (fontStyleNames == null) {
            int i = 0;
            fontStyleNames = new String[4];
            fontStyleNames[i++] = getLocalizedMessage("JFontChooser.plainText");
            fontStyleNames[i++] = getLocalizedMessage("JFontChooser.boldText");
            fontStyleNames[i++] = getLocalizedMessage("JFontChooser.italicText");
            fontStyleNames[i++] = getLocalizedMessage("JFontChooser.boldItalicText");
        }
        return fontStyleNames;
    }

    static String getLocalizedMessage(String key) {
        if (!bundleRegistered) {
            synchronized(bundleRegistrationLock) {
                if (!bundleRegistered) {
                    UIManager.getDefaults().addResourceBundle(
                        JFontChooser.class.getCanonicalName());
                    bundleRegistered = true;
                }
            }
        }
        return UIManager.getDefaults().getString(key);
    }
}

class FontChooserDialog extends JDialog {

    private static final long serialVersionUID = 1L;

	private static class CancelButton extends JButton {
        private static final long serialVersionUID = 1L;

		CancelButton(String text) {
            super(text);
        }

        void fireActionPerformed1(ActionEvent event) {
            fireActionPerformed(event);
        }
    };

    private Font initialFont = Font.getFont("Serif");
    private JFontChooser chooserPane;
    private JButton cancelButton;

    FontChooserDialog(Dialog owner, String title, boolean modal, Component c,
            JFontChooser chooserPane, ActionListener okListener, ActionListener cancelListener) 
            throws HeadlessException {
        super(owner, title, modal);
        initFontChooserDialog(c, chooserPane, okListener, cancelListener);
    }

    FontChooserDialog(Frame owner, String title, boolean modal, Component c,
            JFontChooser chooserPane, ActionListener okListener, ActionListener cancelListener) 
            throws HeadlessException {
        super(owner, title, modal);
        initFontChooserDialog(c, chooserPane, okListener, cancelListener);
    }

    void reset() {
        chooserPane.setSelectedFont(initialFont);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void show() {
        initialFont = chooserPane.getFont();
        super.show();
    }

    protected void initFontChooserDialog(Component owner, JFontChooser chooserPane, 
            ActionListener okListener, ActionListener cancelListener) {
        //setResizable(false);

        this.chooserPane = chooserPane;

        final Locale locale = getLocale();
        final String okString = UIManager.getString("ColorChooser.okText", locale);
        final String cancelString = UIManager.getString("ColorChooser.cancelText", locale);
        final String resetString = UIManager.getString("ColorChooser.resetText", locale);

        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(chooserPane, BorderLayout.CENTER);

        /*
         * Create Lower button panel
         */
        final JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        final JButton okButton = new JButton(okString);
        getRootPane().setDefaultButton(okButton);
        okButton.getAccessibleContext().setAccessibleDescription(okString);
        okButton.setActionCommand("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        if (okListener != null) {
            okButton.addActionListener(okListener);
        }
        buttonPane.add(okButton);

        cancelButton = new CancelButton(cancelString);
        cancelButton.getAccessibleContext().setAccessibleDescription(cancelString);

        // The following few lines are used to register esc to close the dialog
        final Action cancelKeyAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                ((CancelButton)e.getSource()).fireActionPerformed1(e);
            }
        };
        final KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        final InputMap inputMap = cancelButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        final ActionMap actionMap = cancelButton.getActionMap();
        if (inputMap != null && actionMap != null) {
            inputMap.put(cancelKeyStroke, "cancel");
            actionMap.put("cancel", cancelKeyAction);
        }
        // end esc handling

        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener((ActionEvent e) -> {
                setVisible(false);
        });
        if (cancelListener != null) {
            cancelButton.addActionListener(cancelListener);
        }
        buttonPane.add(cancelButton);

        JButton resetButton = new JButton(resetString);
        resetButton.getAccessibleContext().setAccessibleDescription(resetString);
        resetButton.addActionListener((ActionEvent e) -> {
            reset();
        });
        final String mnemonicStr = UIManager.getString("JFontChooser.resetMnemonic", locale);
        final int mnemonic = (mnemonicStr == null) ? -1 : Integer.parseInt(mnemonicStr);
        if (mnemonic != -1) {
            resetButton.setMnemonic(mnemonic);
        }
        buttonPane.add(resetButton);
        contentPane.add(buttonPane, BorderLayout.SOUTH);

        if (JDialog.isDefaultLookAndFeelDecorated()) {
            boolean supportsWindowDecorations =
            UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (supportsWindowDecorations) {
                getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
            }
        }
        applyComponentOrientation(((owner == null) ? getRootPane() : owner).getComponentOrientation());

        pack();
        setLocationRelativeTo(owner);

        this.addWindowListener(new Closer());
    }

    class Closer extends WindowAdapter implements Serializable{
        private static final long serialVersionUID = 1L;

        public void windowClosing(WindowEvent e) {
            cancelButton.doClick(0);
            Window w = e.getWindow();
            w.setVisible(false);
        }
    }

    static class DisposeOnClose extends ComponentAdapter implements Serializable{
        private static final long serialVersionUID = 1L;

        public void componentHidden(ComponentEvent e) {
            Window w = (Window)e.getComponent();
            w.dispose();
        }
    }
}

class FontTracker implements ActionListener, Serializable {
    private static final long serialVersionUID = 1L;
    
    JFontChooser chooser;
    Font font;

    public FontTracker(JFontChooser c) {
        chooser = c;
    }

    public void actionPerformed(ActionEvent e) {
        font = chooser.getFont();
    }

    public Font getFont() {
        return font;
    }
}

class JOptionPaneLocal extends JOptionPane {
    private static final long serialVersionUID = 1L;

	static Window getWindowForComponent1(Component parentComponent)
            throws HeadlessException {
        if (parentComponent == null)
            return getRootFrame();
        if (parentComponent instanceof Frame || parentComponent instanceof Dialog)
            return (Window)parentComponent;
        return getWindowForComponent1(parentComponent.getParent());
    }
}
