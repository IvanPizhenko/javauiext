/*
Copyright 2004-2005,2007-2008 Masahiko SAWAI All Rights Reserved. 

Permission is hereby granted, free of charge, to any person obtaining 
a copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation 
the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom 
the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included 
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS 
OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL 
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE 
OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

/*
Java UI Extensions project. 
https://ivanp2015.github.io/javauiext

Copyright (c) 2018, Ivan Pizhenko. All rights reserved.

Redistribution and use in source and binary forms, with or without 
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

//package say.swing;

package io.github.ivanp2015.javauiext.javax.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
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
 * The {@code JFontChooser} class is a swing component 
 * for font selection.
 * This class has {@code JFileChooser} like APIs.
 * The following code pops up a font chooser dialog.
 * {@code
 *   JFontChooser fontChooser = new JFontChooser();
 *   int result = fontChooser.showDialog(parent);
 *   if (result == JFontChooser.OK_OPTION)
 *   {
 *   	Font font = fontChooser.getSelectedFont(); 
 *   	System.out.println("Selected Font : " + font); 
 *   }
 * }
 **/
public class JFontChooser extends JComponent
{
	private static final long serialVersionUID = 1L;

	// class variables
	/**
	 * Return value from {@code showDialog()}.
	 * @see #showDialog
	 **/
	public static final int OK_OPTION = 0;

	/**
	 * Return value from {@code showDialog()}.
	 * @see #showDialog
	 **/
	public static final int CANCEL_OPTION = 1;

	/**
	 * Return value from {@code showDialog()}.
	 * @see #showDialog
	 **/
	public static final int ERROR_OPTION = -1;
	private static final Font DEFAULT_SELECTED_FONT = new Font("Serif", Font.PLAIN, 12);
	//private static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 10);
	
	private static final int[] FONT_STYLE_CODES = {
		Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC
	};

	private static final String[] DEFAULT_FONT_SIZE_STRINGS = {
		"8", "9", "10", "11", "12", "14", "16", "18", "20",
		"22", "24", "26", "28", "36", "48", "72",
	};

	// instance variables
	protected int dialogResultValue = ERROR_OPTION;
	private ResourceBundle messageCatalog = ResourceBundle.getBundle(
		JFontChooser.class.getName(), getLocale());
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
	 * Constructs a {@code JFontChooser} object.
	 **/
	public JFontChooser() {
		this(DEFAULT_FONT_SIZE_STRINGS);
	}

	/**
	 * Constructs a {@code JFontChooser} object using the given font size array.
	 * @param fontSizeStrings  the array of font size string.
	 **/
	public JFontChooser(String[] fontSizeStrings) {
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
		this.setSelectedFont(DEFAULT_SELECTED_FONT);
	}

	/**
	 * @return JFontChooser version.
	 */
	public static String getVersionString() {
		return "2.0.0";
	}

	/**
	 * @return Font family text field.
	 */
	public JTextField getFontFamilyTextField() {
		if (fontFamilyTextField == null) {
			fontFamilyTextField = new JTextField();
			fontFamilyTextField.addFocusListener(
				new TextFieldFocusHandlerForTextSelection(fontFamilyTextField));
			fontFamilyTextField.addKeyListener(
				new TextFieldKeyHandlerForListSelectionUpDown(getFontFamilyList()));
			fontFamilyTextField.getDocument().addDocumentListener(
				new ListSearchTextFieldDocumentHandler(getFontFamilyList()));
			//fontFamilyTextField.setFont(DEFAULT_FONT);

		}
		return fontFamilyTextField;
	}

	/**
	 * @return Font style text field.
	 */
	public JTextField getFontStyleTextField() {
		if (fontStyleTextField == null) {
			fontStyleTextField = new JTextField();
			fontStyleTextField.addFocusListener(
				new TextFieldFocusHandlerForTextSelection(fontStyleTextField));
			fontStyleTextField.addKeyListener(
				new TextFieldKeyHandlerForListSelectionUpDown(getFontStyleList()));
			fontStyleTextField.getDocument().addDocumentListener(
				new ListSearchTextFieldDocumentHandler(getFontStyleList()));
			//fontStyleTextField.setFont(DEFAULT_FONT);
		}
		return fontStyleTextField;
	}

	/**
	 * @return Font size text field.
	 */
	public JTextField getFontSizeTextField() {
		if (fontSizeTextField == null) {
			fontSizeTextField = new JTextField();
			fontSizeTextField.addFocusListener(
				new TextFieldFocusHandlerForTextSelection(fontSizeTextField));
			fontSizeTextField.addKeyListener(
				new TextFieldKeyHandlerForListSelectionUpDown(getFontSizeList()));
			fontSizeTextField.getDocument().addDocumentListener(
				new ListSearchTextFieldDocumentHandler(getFontSizeList()));
			//fontSizeTextField.setFont(DEFAULT_FONT);
		}
		return fontSizeTextField;
	}

	/**
	 * @return Font family list.
	 */
	public JList<String> getFontFamilyList() {
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

	/**
	 * @return Font style list.
	 */
	public JList<String> getFontStyleList() {
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

	/**
	 * @return Font size list.
	 */
	public JList<String> getFontSizeList() {
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

	/**
	 * Get the family name of the selected font.
	 * @return  the font family of the selected font.
	 *
	 * @see #setSelectedFontFamily
	 **/
	public String getSelectedFontFamily() {
		return getFontFamilyList().getSelectedValue();
	}

	/**
	 * Get the style of the selected font.
	 * @return  the style of the selected font.
	 *          {@code Font.PLAIN}, {@code Font.BOLD},
	 *          {@code Font.ITALIC}, {@code Font.BOLD|Font.ITALIC}
	 *
	 * @see java.awt.Font#PLAIN
	 * @see java.awt.Font#BOLD
	 * @see java.awt.Font#ITALIC
	 * @see #setSelectedFontStyle
	 **/
	public int getSelectedFontStyle() {
		final int index = getFontStyleList().getSelectedIndex();
		return FONT_STYLE_CODES[index];
	}

	/**
	 * Get the size of the selected font.
	 * @return  the size of the selected font
	 *
	 * @see #setSelectedFontSize
	 **/
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
	 * Get the selected font.
	 * @return  the selected font
	 *
	 * @see #setSelectedFont
	 * @see java.awt.Font
	 **/
	public Font getSelectedFont() {
		return new Font(getSelectedFontFamily(),
			getSelectedFontStyle(), getSelectedFontSize());
	}

	/**
	 * Set the family name of the selected font.
	 * @param name  the family name of the selected font. 
	 *
	 * @see getSelectedFontFamily
	 **/
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
	 * Set the style of the selected font.
	 * @param style  the size of the selected font.
	 *               {@code Font.PLAIN}, {@code Font.BOLD},
	 *               {@code Font.ITALIC}, or
	 *               {@code Font.BOLD|Font.ITALIC}.
	 *
	 * @see java.awt.Font#PLAIN
	 * @see java.awt.Font#BOLD
	 * @see java.awt.Font#ITALIC
	 * @see #getSelectedFontStyle()
	 **/
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
	 * @see #getSelectedFontSize()
	 **/
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
	 * @see #getSelectedFont()
	 * @see java.awt.Font
	 **/
	public void setSelectedFont(Font font) {
		setSelectedFontFamily(font.getFamily());
		setSelectedFontStyle(font.getStyle());
		setSelectedFontSize(font.getSize());
	}

	/**
	 *  Show font selection dialog.
	 *  @param parent Dialog's Parent component.
	 *  @return OK_OPTION, CANCEL_OPTION or ERROR_OPTION
	 *
	 *  @see #OK_OPTION 
	 *  @see #CANCEL_OPTION
	 *  @see #ERROR_OPTION
	 **/
	public int showDialog(Component parent) {
		dialogResultValue = ERROR_OPTION;
		final JDialog dialog = createDialog(parent);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dialogResultValue = CANCEL_OPTION;
			}
		});

		dialog.setVisible(true);
		dialog.dispose();

		return dialogResultValue;
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

	protected class ListSearchTextFieldDocumentHandler implements DocumentListener
	{
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

			public ListSelector(int index)
			{
				this.index = index;
			}

			public void run()
			{
				targetList.setSelectedIndex(this.index);
			}
		}
	}

	protected class DialogOKAction extends AbstractAction {
		protected static final String ACTION_NAME = "OK";
		private JDialog dialog;

		protected DialogOKAction(JDialog dialog) {
			this.dialog = dialog;
			putValue(Action.DEFAULT, ACTION_NAME);
			putValue(Action.ACTION_COMMAND_KEY, ACTION_NAME);
			putValue(Action.NAME, getLocalizedMessage(ACTION_NAME));
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
			putValue(Action.NAME, getLocalizedMessage(ACTION_NAME));
		}

		public void actionPerformed(ActionEvent e) {
			dialogResultValue = CANCEL_OPTION;
			dialog.setVisible(false);
		}
	}

	protected JDialog createDialog(Component parent) {
		Frame frame = parent instanceof Frame 
			? (Frame) parent
			: (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);
		final JDialog dialog = new JDialog(frame, getLocalizedMessage("SelectFont"), true);

		final Action okAction = new DialogOKAction(dialog);
		final Action cancelAction = new DialogCancelAction(dialog);

		final JButton okButton = new JButton(okAction);
		//okButton.setFont(DEFAULT_FONT);
		final JButton cancelButton = new JButton(cancelAction);
		//cancelButton.setFont(DEFAULT_FONT);

		final JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(2, 1));
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 10));

		final ActionMap actionMap = buttonsPanel.getActionMap();
		actionMap.put(cancelAction.getValue(Action.DEFAULT), cancelAction);
		actionMap.put(okAction.getValue(Action.DEFAULT), okAction);
		final InputMap inputMap = buttonsPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), cancelAction.getValue(Action.DEFAULT));
		inputMap.put(KeyStroke.getKeyStroke("ENTER"), okAction.getValue(Action.DEFAULT));

		final JPanel dialogEastPanel = new JPanel();
		dialogEastPanel.setLayout(new BorderLayout());
		dialogEastPanel.add(buttonsPanel, BorderLayout.NORTH);

		dialog.getContentPane().add(this, BorderLayout.CENTER);
		dialog.getContentPane().add(dialogEastPanel, BorderLayout.EAST);
		dialog.pack();
		dialog.setLocationRelativeTo(frame);
		return dialog;
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

			final JLabel label = new JLabel(getLocalizedMessage("FontName"));
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

			final JLabel label = new JLabel(getLocalizedMessage("FontStyle"));
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

			final JLabel label = new JLabel(getLocalizedMessage("FontSize"));
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
				BorderFactory.createEtchedBorder(), getLocalizedMessage("Sample"));
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
			sampleText = new JTextField(getLocalizedMessage("SampleString"));
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
			fontStyleNames[i++] = getLocalizedMessage("Plain");
			fontStyleNames[i++] = getLocalizedMessage("Bold");
			fontStyleNames[i++] = getLocalizedMessage("Italic");
			fontStyleNames[i++] = getLocalizedMessage("BoldItalic");
		}
		return fontStyleNames;
	}

	protected String getLocalizedMessage(String key) {
		try {
			return messageCatalog.getString(key);
		}
		catch (MissingResourceException e) {
			return key;
		}
	}
}
