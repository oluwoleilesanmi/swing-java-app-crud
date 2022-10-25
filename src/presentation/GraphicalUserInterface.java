package presentation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import common.Constant;
import persistence.Manager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * This class is used to create user interface components.
 * This class is used to attach listeners to individual ui components and class wide.
 * This class button components follow a pattern of ending in caps with the component class name.
 * Examples L - JLabel, TF - JTextField, RB - JRadioButton.
 *
 * @author  OLUWOLE-ILESANMI
 * @version Version 1
 * @since   Today
 */

@SuppressWarnings("serial")
public class GraphicalUserInterface extends JFrame implements ItemListener, 
				 						   					  ActionListener, 
				 						   					  ListSelectionListener,
				 						   					  TableModelListener,
				 						   					  FocusListener{
	
	private Font font;
	private JTable table;
	private JLabel headerL;
	private String str = null;
	private JTextArea paneTA;
	private ButtonGroup group;
	private JPanel nestedPane;
	private Manager mgr = null;
	private BorderLayout paneBL;
	private JTextField userIdTF;
	private JScrollPane tableScrollPane;
	private DefaultTableModel tableModel;
	private JRadioButton ascendOrderRB, descendOrderRB;
	private JButton exitB, submitB, ageFilterB, refreshB;
	private GraphicalUserInterface thisClassInstance = this;
	
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public GraphicalUserInterface(Manager mgr){
		
		// Instantiation of user interface components.
		this.mgr = mgr;
		group = new ButtonGroup();
        nestedPane = new JPanel();
        userIdTF = new JTextField();
        exitB = new JButton(Constant.EXIT);
        headerL = new JLabel(Constant.HEADER_TEXT);
        submitB = new JButton(Constant.SUBMIT);
        ageFilterB = new JButton(Constant.FILETR);
        refreshB = new JButton(Constant.REFRESH);
        paneTA = new JTextArea(Constant.DIMENSION, 50);
        paneBL = new BorderLayout(Constant.DIMENSION, Constant.DIMENSION);
        ascendOrderRB = new JRadioButton(Constant.ASCENDING_SORT); 
        descendOrderRB = new JRadioButton(Constant.DESCENDING_SORT);
        table = new JTable(tableModel);
        tableScrollPane = new JScrollPane(table);
       
        // Setting the font.
        refreshB.setFont(new Font("San Serif", Font.BOLD, 14));
        headerL.setFont(new Font("Serif", Font.BOLD, 18));
        
        // Adding listeners to ui components.
        submitB.addActionListener(this);
        exitB.addActionListener(this);
        ascendOrderRB.addItemListener(this);
        descendOrderRB.addItemListener(this);
        userIdTF.addFocusListener(this);
        table.getSelectionModel().addListSelectionListener(this);
        
        // Set ordering as asending by default.
        ascendOrderRB.setSelected(true);
       
        // Adding table to scrollpane to make it scrollable.
        tableScrollPane.setPreferredSize(new Dimension(500, 150));		
       
        // Configuration of the main pane for all layouts and ui components.
        setTitle(Constant.TITLE);
        setSize(Constant.WIDTH, Constant.HEIGHT);
        Container pane = getContentPane();
       
        // Set layout for main pane as borderlayout.
        pane.setLayout(paneBL);
		
        // Styling header label.
       
        headerL.setHorizontalAlignment(SwingConstants.CENTER);
        font = headerL.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        headerL.setFont(font.deriveFont(attributes));
        
        // Configuration of nested pane.
        nestedPane.setPreferredSize(new Dimension(320, 150));
        nestedPane.setBorder(BorderFactory.createCompoundBorder(Constant.border,
        		BorderFactory.createEmptyBorder(Constant.DIMENSION, Constant.DIMENSION, Constant.DIMENSION, Constant.DIMENSION)));
        nestedPane.setLayout(new FlowLayout());
        userIdTF.setText(Constant.HINT);
        userIdTF.setPreferredSize(new Dimension(100, 27));
     
        // Adding radio buttons to buttongroup to make both unselectable at the sametime(mutually exclusive)
        group.add(ascendOrderRB); 
        group.add(descendOrderRB);
       
        // Adding user interface components into a nested pane.
        nestedPane.add(userIdTF);
        nestedPane.add(submitB);
        nestedPane.add(ascendOrderRB);
        nestedPane.add(descendOrderRB);
        nestedPane.add(exitB);
        nestedPane.add(ageFilterB);
        nestedPane.add(refreshB);
        
        // This is a button listener for filtering table to display competitors older than 40.
        ageFilterB.addActionListener(new ActionListener() {
        	Object[][] objArr = mgr.getTableStore();
			ArrayList<Object[]> objList = new ArrayList<Object[]>();
        	@Override
			public void actionPerformed(ActionEvent e) {
        		if(isitAnumStoredAs(str)) {
	        		for(int i = 0; i < objArr.length; i++) {
	        			if(Integer.parseInt((String)objArr[i][3]) == Integer.parseInt(str)) {objList.add(objArr[i]);}
	        		}
	        		if(objList.isEmpty()) {
	        			paneTA.setText("Age requested is not in table"+ "\n");
	        			Object[][] b = new Object[objList.size()][8];
		        		for(int i = 0; i < objList.size(); i++) {b[i] = objList.get(i);}
		        		//reset list.
		        		objList = new ArrayList<Object[]>();
		        		refreshTableModel(b);
		        		table.getModel().addTableModelListener(thisClassInstance);
	        		}
	        		else {
		        		Object[][] b = new Object[objList.size()][8];
		        		for(int i = 0; i < objList.size(); i++) {b[i] = objList.get(i);}
		        		//reset list.
		        		objList = new ArrayList<Object[]>();
		        		refreshTableModel(b);
		        		table.getModel().addTableModelListener(thisClassInstance);
		        		paneTA.setText("");
	        		}
        		}else {
        			paneTA.setText("no Age was entered"+ "\n");
        		}
			}
        });
        
        // This is a button listener for refreshing the table. 
        refreshB.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent arg0) {
        		refreshTableModel(mgr.getTableStore());
        		paneTA.setText("");
        		table.getModel().addTableModelListener(thisClassInstance);
			}
        });
        
        // Format textarea to have border.
        paneTA.setBorder(BorderFactory.createCompoundBorder(Constant.border,
        		BorderFactory.createEmptyBorder(Constant.DIMENSION, Constant.DIMENSION, Constant.DIMENSION, Constant.DIMENSION)));
      
        // Adding user interface components to the frame.
        pane.add(tableScrollPane, BorderLayout.SOUTH);
        pane.add(headerL, BorderLayout.NORTH);
        pane.add(paneTA, BorderLayout.EAST);
        pane.add(nestedPane, BorderLayout.WEST);
        
        setVisible(true);
        // Prevents the input cursor from defaulting to the text-box on start-up.
        requestFocusInWindow();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	// Check text field input, only numbered strings are acceptable.
	public boolean isitAnumStoredAs(String str) {
		if (str == "") {return false;}
	    try {
	    	Integer.parseInt(str);
	    } catch (NumberFormatException e) {return false;}
	    return true;
	}
	
	// Check text field input, only numbered strings are acceptable.
	public boolean isListOfScoresValid(Object str) {
		String scores = String.valueOf(str);
		List<String> list = new ArrayList<String>();
		// copy all the elements of string array to an ArrayList using the asList() method 
		list = Arrays.asList(scores.split(" "));
		for(String string: list){ 
			if (list.size() != 5 || !isitAnumStoredAs(string) || Integer.parseInt(string) > 5 || Integer.parseInt(string) < 1 ) {
				refreshTableModel (this.mgr.getTableStore());
	        	table.getModel().addTableModelListener(this);
				return false;
				
			}
		}
		return true;
	}
	
	// This is button listener.
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
        if (action.equals(Constant.EXIT)) {
        	if (isitAnumStoredAs(str)) {
        		mgr.createCompleteReport(Integer.parseInt(str));
        		System.exit(0); 
        	}else {
        		paneTA.setText("Please remember to enter number greater than zero before exiting"+ "\n");
        	}
        }
	}
	
	// This is a radio-button listener.
	@Override
	public void itemStateChanged(ItemEvent e) {
		JRadioButton button = (JRadioButton) e.getSource();
		// e.getStateChange() == ItemEvent.SELECTED prevents double events
        if (button.getText().equals(Constant.ASCENDING_SORT) && e.getStateChange() == ItemEvent.SELECTED){
        	// Sort array in ascending order using comparator.
        	Comparator<Object[]> comparator = new Comparator<Object[]>() {
				@Override
				public int compare(Object[] a, Object[] b) {
					Integer c = Integer.parseInt((String)a[0]);
					Integer d = Integer.parseInt((String)b[0]);
					return c.compareTo(d);
				}
    		};
    		Arrays.sort(this.mgr.getTableStore(), comparator);
    		refreshTableModel(this.mgr.getTableStore());
        }else if(button.getText().equals(Constant.DESCENDING_SORT) && e.getStateChange() == ItemEvent.SELECTED){
        	// Sort array in descending order using comparator.
        	Comparator<Object[]> comparator = new Comparator<Object[]>() {
				@Override
				public int compare(Object[] a, Object[] b) {
					Integer c = Integer.parseInt((String)a[0]);
					Integer d = Integer.parseInt((String)b[0]);
					return d.compareTo(c);
				}
    		};
    		Arrays.sort(this.mgr.getTableStore(), comparator);
    		refreshTableModel(this.mgr.getTableStore());
        }
        // Re-attach the table modifier listener
        table.getModel().addTableModelListener(this);
	}

	// This is a table listener.
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// The function getValueIsAdjusting prevents double events.
		if (!e.getValueIsAdjusting()) {
			paneTA.setText("Remember to enter a number between 1 & 5." +"\n" 
								+ "Each player must have 5 scores and proper spacing. "+"\n" 
										+ "All other columns are Uneditable.");
	    }
	 }
		
	// This is a textfield listener.
	@Override
	public void focusGained(FocusEvent e) {userIdTF.setText("");}
	
	// This is a textfield listener.
	@Override
	public void focusLost(FocusEvent e) {
		str = userIdTF.getText();
		if(isitAnumStoredAs(str)){
	    	paneTA.setText(this.mgr.createCompleteReportUI(Integer.parseInt(str)));
	    }
		else if(!isitAnumStoredAs(str)) {
			paneTA.setText("Please remember to enter number greater than or equal to zero next time:"+ "\n");
		}
		userIdTF.setText(Constant.HINT);
	}
	
	// This is a table listener.
	@Override
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow(), column = e.getColumn();
        DefaultTableModel model = (DefaultTableModel)e.getSource();
        Object data = model.getValueAt(row, column);
        //get the unique id, for update in mainstore & tablestore.
        Object uniqueId = model.getValueAt(row, 0);
        if (isListOfScoresValid(data)) {
        	 mgr.updateMainAndTableDs(uniqueId, data);
        	 refreshTableModel (this.mgr.getTableStore());
        	 table.getModel().addTableModelListener(this);
        } else{
        	paneTA.setText("Please remember to enter score properly"+ "\n");
        }
	}
	
	/*
	 * Refresh the UI table. 
	 * This functionality is repeatedly hence the need for abstraction.
	 * strObj holds multi-dimensional array of strings from table.
	 */
	private void refreshTableModel (Object [][] strObjs) {
		tableModel = new DefaultTableModel(strObjs, Constant.TABLE_COLUMN_NAMES){
			@Override
			public boolean isCellEditable(int row, int column){return column == 4;}
		};
    	table.setModel(tableModel);
	}
}