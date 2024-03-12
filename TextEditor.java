package Notepad;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.BadLocationException;


public class TextEditor extends JFrame 
{
    private JTextArea textArea;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenuItem newMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem duplicateMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem findMenuItem;
    private JMenuItem findReplaceMenuItem;
    private JMenuItem goToLineMenuItem;

    private JFileChooser fileChooser;
    private File currentFile;

    public TextEditor() 
    {
        setTitle("Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        newMenuItem = new JMenuItem("New");
        openMenuItem = new JMenuItem("Open");
        saveMenuItem = new JMenuItem("Save");
        saveAsMenuItem = new JMenuItem("Save As");
        duplicateMenuItem = new JMenuItem("Duplicate");
        exitMenuItem = new JMenuItem("Exit");
        findMenuItem = new JMenuItem("Find");
        findReplaceMenuItem = new JMenuItem("Find and Replace");
        goToLineMenuItem = new JMenuItem("Go to Line");

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(duplicateMenuItem);
        fileMenu.add(exitMenuItem);

        editMenu.add(findMenuItem);
        editMenu.add(findReplaceMenuItem);
        editMenu.add(goToLineMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);

        fileChooser = new JFileChooser();

        newMenuItem.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) {
                TextEditor newNotepad = new TextEditor();
                // Set the new frame position relative to the current frame
                int newX = getX() + 20; 
                int newY = getY() + 20;
                newNotepad.setLocation(newX, newY);
                newNotepad.setVisible(true);
            }
        });

        openMenuItem.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                // Pass the current frame as the parent
                int result = fileChooser.showOpenDialog(TextEditor.this); 

                if (result == JFileChooser.APPROVE_OPTION) 
                {
                    File selectedFile = fileChooser.getSelectedFile();
                    try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) 
                    {
                        StringBuilder content = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) 
                        {
                            content.append(line).append("\n");
                        }
                        TextEditor newNotepad = new TextEditor();
                        // Set the new frame position relative to the current frame
                        int newX = getX() + 20; 
                        int newY = getY() + 20;
                        newNotepad.setLocation(newX, newY);
                        newNotepad.textArea.setText(content.toString());
                        newNotepad.setVisible(true);
                    } catch (IOException ex) 
                    {
                        ex.printStackTrace();
                    }
                }
            }
        });

        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.getSelectedFile() != null) {
                    saveFile(fileChooser.getSelectedFile());
                } else {
                    saveAsMenuItem.doClick();
                }
            }
        });

        saveAsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Pass the current frame as the parent
                int result = fileChooser.showSaveDialog(TextEditor.this); 
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    saveFile(selectedFile);
                }
            }
        });

        duplicateMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Set the new frame position relative to the current frame
                TextEditor newNotepad = new TextEditor();
                int newX = getX() + 20; 
                int newY = getY() + 20;
                newNotepad.setLocation(newX, newY);
                newNotepad.textArea.setText(textArea.getText());
                newNotepad.setVisible(true);
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        findMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a custom dialog
                JDialog findDialog = new JDialog(TextEditor.this, "Find");
                JTextArea searchArea = new JTextArea(3, 20);
                JTextArea resultArea = new JTextArea(2, 20);
                JButton findButton = new JButton("Find");
                JButton nextButton = new JButton("Next");
                JButton prevButton = new JButton("Previous");
                JScrollPane searchScrollPane = new JScrollPane(searchArea);
                JScrollPane resultScrollPane = new JScrollPane(resultArea);
        
                // Define the layout of the dialog
                findDialog.setLayout(new BorderLayout());
                JPanel searchPanel = new JPanel(new BorderLayout());
                searchPanel.add(new JLabel("Search:"), BorderLayout.NORTH);
                searchPanel.add(searchScrollPane, BorderLayout.CENTER);
                findDialog.add(searchPanel, BorderLayout.NORTH);
                findDialog.add(resultScrollPane, BorderLayout.SOUTH);
                JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
                buttonPanel.add(prevButton);
                buttonPanel.add(findButton);
                findDialog.add(buttonPanel, BorderLayout.CENTER);
                findDialog.add(nextButton, BorderLayout.EAST);
        
                // Find button action listener
                findButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String searchText = searchArea.getText();
                        String content = textArea.getText();
                        int index = content.indexOf(searchText);
                        if (index != -1) {
                            textArea.requestFocus();
                            textArea.setCaretPosition(index);
                            textArea.select(index, index + searchText.length());
                            resultArea.setText("Found at index: " + index);
                        } else {
                            resultArea.setText("Text not found");
                        }
                    }
                });
        
                // Next button action listener
                nextButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String searchText = searchArea.getText();
                        String content = textArea.getText();
                        int caretPosition = textArea.getCaretPosition();
                        int index = content.indexOf(searchText, caretPosition);
                        if (index != -1) {
                            textArea.requestFocus();
                            textArea.setCaretPosition(index);
                            textArea.select(index, index + searchText.length());
                            resultArea.setText("Found at index: " + index);
                        } else {
                            index = content.indexOf(searchText);
                            if (index != -1) {
                                textArea.requestFocus();
                                textArea.setCaretPosition(index);
                                textArea.select(index, index + searchText.length());
                                resultArea.setText("No more occurrences found. Starting from the beginning.");
                            } else {
                                resultArea.setText("Text not found");
                            }
                        }
                    }
                });
        
                // Previous button action listener
                prevButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String searchText = searchArea.getText();
                        String content = textArea.getText();
                        int caretPosition = textArea.getCaretPosition() - searchText.length() - 1;
                        int index = content.lastIndexOf(searchText, caretPosition);
                        if (index != -1) {
                            textArea.requestFocus();
                            textArea.setCaretPosition(index);
                            textArea.select(index, index + searchText.length());
                            resultArea.setText("Found at index: " + index);
                        } else {
                            index = content.lastIndexOf(searchText);
                            if (index != -1) {
                                textArea.requestFocus();
                                textArea.setCaretPosition(index);
                                textArea.select(index, index + searchText.length());
                                resultArea.setText("No more previous occurrences found. Starting from the last.");
                            } else {
                                resultArea.setText("Text not found");
                            }
                        }
                    }
                });

                // Set dialog properties
                findDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                findDialog.pack();
                findDialog.setLocationRelativeTo(TextEditor.this);
                findDialog.setVisible(true);
            }
        });
        
        findReplaceMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a custom dialog
                JDialog findReplaceDialog = new JDialog(TextEditor.this, "Find and Replace");
                JTextArea searchArea = new JTextArea(3, 20);
                JTextArea replaceArea = new JTextArea(3, 20);
                JButton findButton = new JButton("Find");
                JButton replaceButton = new JButton("Replace");
                JButton replaceAllButton = new JButton("Replace All");
                JScrollPane searchScrollPane = new JScrollPane(searchArea);
                JScrollPane replaceScrollPane = new JScrollPane(replaceArea);
        
                // Define the layout of the dialog
                findReplaceDialog.setLayout(new BorderLayout());
                JPanel searchPanel = new JPanel(new BorderLayout());
                searchPanel.add(new JLabel("Search:"), BorderLayout.NORTH);
                searchPanel.add(searchScrollPane, BorderLayout.CENTER);
                findReplaceDialog.add(searchPanel, BorderLayout.NORTH);
                JPanel replacePanel = new JPanel(new BorderLayout());
                replacePanel.add(new JLabel("Replace:"), BorderLayout.NORTH);
                replacePanel.add(replaceScrollPane, BorderLayout.CENTER);
                findReplaceDialog.add(replacePanel, BorderLayout.CENTER);
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.add(findButton);
                buttonPanel.add(replaceButton);
                buttonPanel.add(replaceAllButton);
                findReplaceDialog.add(buttonPanel, BorderLayout.SOUTH);
        
                // Find button action listener
                findButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String searchText = searchArea.getText();
                        String content = textArea.getText();
                        int index = content.indexOf(searchText);
                        if (index != -1) {
                            textArea.requestFocus();
                            textArea.setCaretPosition(index);
                            textArea.select(index, index + searchText.length());
                        } else {
                            JOptionPane.showMessageDialog(TextEditor.this, "Text not found", "Find", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                });
        
                // Replace button action listener
                replaceButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String searchText = searchArea.getText();
                        String replaceText = replaceArea.getText();
                        String content = textArea.getText();
                        int index = textArea.getCaretPosition() - searchText.length();
                        if (index >= 0 && content.substring(index, index + searchText.length()).equals(searchText)) {
                            textArea.replaceRange(replaceText, index, index + searchText.length());
                        }
                        int nextIndex = content.indexOf(searchText, index + replaceText.length());
                        if (nextIndex != -1) {
                            textArea.requestFocus();
                            textArea.setCaretPosition(nextIndex);
                            textArea.select(nextIndex, nextIndex + searchText.length());
                        } else {
                            JOptionPane.showMessageDialog(TextEditor.this, "No more occurrences found", "Replace", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                });
        
                // Replace All button action listener
                replaceAllButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String searchText = searchArea.getText();
                        String replaceText = replaceArea.getText();
                        String content = textArea.getText();
                        String updatedContent = content.replaceAll(searchText, replaceText);
                        textArea.setText(updatedContent);
                        JOptionPane.showMessageDialog(TextEditor.this, "All occurrences replaced", "Replace All", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
        
                // Set dialog properties
                findReplaceDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                findReplaceDialog.pack();
                findReplaceDialog.setLocationRelativeTo(TextEditor.this);
                findReplaceDialog.setVisible(true);
            }
        });
        

        goToLineMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a custom dialog
                JDialog goToLineDialog = new JDialog(TextEditor.this, "Go to Line");
                JTextField lineField = new JTextField(10);
                JButton goButton = new JButton("Go");
                JPanel inputPanel = new JPanel();
                inputPanel.add(new JLabel("Line Number:"));
                inputPanel.add(lineField);
                inputPanel.add(goButton);
                goToLineDialog.add(inputPanel);
        
                // Enter key listener for lineField
                lineField.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        goButton.doClick();
                    }
                });
        
                // Go button action listener
                goButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String lineNumberText = lineField.getText();
                        try {
                            int lineNumber = Integer.parseInt(lineNumberText);
                            int lineCount = textArea.getLineCount();
                            if (lineNumber >= 1 && lineNumber <= lineCount) {
                                int startOffset = textArea.getLineStartOffset(lineNumber - 1);
                                textArea.setCaretPosition(startOffset);
                                textArea.requestFocus();
                            } else {
                                JOptionPane.showMessageDialog(TextEditor.this, "Invalid line number", "Go to Line", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(TextEditor.this, "Invalid line number format", "Go to Line", JOptionPane.ERROR_MESSAGE);
                        } catch (BadLocationException ex) {
                            JOptionPane.showMessageDialog(TextEditor.this, "Failed to navigate to line", "Go to Line", JOptionPane.ERROR_MESSAGE);
                        }
                        
                    }
                });
        
                // Set dialog properties
                goToLineDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                goToLineDialog.pack();
                goToLineDialog.setLocationRelativeTo(TextEditor.this);
                goToLineDialog.setVisible(true);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void saveFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textArea.getText());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TextEditor notepad = new TextEditor();
                notepad.setVisible(true);
            }
        });
    }
}
