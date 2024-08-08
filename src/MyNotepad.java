import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

public class MyNotepad {
    private Stack<String> undo1;
    private Stack<String> redo1;
    private String curr;
    private JTextArea textArea;
    private JComboBox<String> fontComboBox;
    private Color fontColor;

    public MyNotepad() {
        JFrame frame = new JFrame("MyNotepad");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
        frame.setVisible(true);

        // UNDO
        JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });

        JButton redo = new JButton("Redo");
        redo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });

        // FONT
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.lightGray);
        JMenu fontSizeMenu = new JMenu("Font Size");
        JMenuItem size12 = new JMenuItem("12");
        size12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setFontSize(12);
            }
        });
        JMenuItem size16 = new JMenuItem("16");
        size16.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setFontSize(16);
            }
        });

        JMenuItem size18 = new JMenuItem("18");
        size18.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setFontSize(18);
            }
        });

        JMenuItem size20 = new JMenuItem("20");
        size20.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setFontSize(20);
            }
        });

        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontComboBox = new JComboBox<>(fontNames);
        fontComboBox.setBackground(Color.lightGray);
        fontComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFont = (String) fontComboBox.getSelectedItem();
                setFont(selectedFont);
            }
        });

        // Font Color
        JButton colorButton = new JButton("Font Color");
        colorButton.setBackground(Color.LIGHT_GRAY);
        colorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseFontColor();
            }
        });

        JMenuItem Find = new JMenuItem("Find Word");
        Find.setBackground(Color.LIGHT_GRAY);
        Find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wordToFind = JOptionPane.showInputDialog(frame, "Enter word to find:");
                if (wordToFind != null) {
                    find(wordToFind);
                }
            }
        });

        fontSizeMenu.add(size12);
        fontSizeMenu.add(size16);
        fontSizeMenu.add(size18);
        fontSizeMenu.add(size20);

        menuBar.add(undo);
        menuBar.add(redo);
        menuBar.add(fontSizeMenu);
        menuBar.add(fontComboBox);
        menuBar.add(colorButton);
        menuBar.add(Find);
        frame.setJMenuBar(menuBar);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        buttonPanel.setForeground(Color.cyan);
        buttonPanel.add(undo);
        buttonPanel.add(redo);
        frame.add(buttonPanel, BorderLayout.NORTH);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleTextChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleTextChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleTextChange();
            }
        });

        undo1 = new Stack<>();
        redo1 = new Stack<>();
        curr = "";
        undo1.push(curr);
        fontColor = Color.BLACK;
    }

    private void handleTextChange() {
        String text = textArea.getText();
        undo1.push(text);
        redo1.clear();
    }

    public void undo() {
        if (undo1.size() > 1) {
            redo1.push(undo1.pop());
            curr = undo1.peek();
            updateTextArea();
        }
    }

    public void redo() {
        if (!redo1.isEmpty()) {
            undo1.push(redo1.pop());
            curr = undo1.peek();
            updateTextArea();
        }
    }

    public void setFont(String fontName) {
        Font currentFont = textArea.getFont();
        Font newFont = new Font(fontName, currentFont.getStyle(), currentFont.getSize());
        textArea.setFont(newFont);
    }

    public void setFontSize(int fontSize) {
        Font currentFont = textArea.getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), fontSize);
        textArea.setFont(newFont);
    }

    public void chooseFontColor() {
        fontColor = JColorChooser.showDialog(textArea, "Choose Font Color", fontColor);
        textArea.setForeground(fontColor);
    }

    private void find(String wordToFind) {
        String text = textArea.getText();
        int index = text.indexOf(wordToFind);

        if (index >= 0) {
            textArea.setSelectionStart(index);
            textArea.setSelectionEnd(index + wordToFind.length());
        } else {
            JOptionPane.showMessageDialog(textArea, "Word not found: " + wordToFind, "Find", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTextArea() {
        textArea.setText(curr);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MyNotepad();
            }
        });
    }
}
