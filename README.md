# Regex to Minimal DFA Converter
## Description
This program is an exe file that allows the user to convert a regular expression to a minimal deterministic finite automaton (DFA). It provides two modes: GUI mode and CLI mode.

The GUI mode allows the user to enter a regular expression in a text box, select which intermediate stages they would like to visualize (NFA, DFA and minimal DFA), and select whether or not all DFA states must have transitions for each relevant symbol. The minimal DFA XML is then displayed in the last tab.

The CLI mode allows the user to convert a number of regular expressions to minimal DFA XML. The user can specify how many regular expressions they would like to convert, select whether or not all DFA states must have transitions for each relevant symbol, and enter the regular expression to convert. The program will then display the XML for the minimal DFA.

## Requirements
Java version 1.8 or higher must be installed on the system.
Launching the program

## Launching
The program is an exe file and can be launched on a Windows system by clicking on it.

## Using the GUI mode
1. Launch the program and select "GUI mode."
2. Enter a regular expression in the text box at the top.
3. Select which intermediate stages you would like to visualize (NFA, DFA and minimal DFA).
4. Select whether or not all DFA states must have transitions for each relevant symbol.
5. Click "Go."
6. Navigate using the tabs.
7. The final minimal DFA XML is displayed in the last tab.
Note: The GUI mode is recommended for ease of use.

![Screenshot of GUI](https://github.com/GT-GillesTeuwen/COS341-P1/blob/master/COS341-P1_GUI.PNG)

## Using the CLI mode
1. Launch the program and select "CLI mode."
2. Enter the number of regular expressions you would like to convert.
3. Select whether or not all DFA states must have transitions for each relevant symbol.
4. Type in the regular expression you would like to convert and press enter.
5. The XML for the minimal DFA is displayed.
## Credits
This program was created by Gilles Teuwen (u21465772) for COS341 Compiler Construction at the University of Pretoria.
