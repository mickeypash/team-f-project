package javaball.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.regex.Pattern;

import javaball.controller.JavaBallController;
import javaball.enums.Location;
import javaball.enums.RefQualification;
import javaball.model.Referee;
import javaball.model.RefereeList;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * Team Foxtrot
 * JavaBall Referee Allocation System
 * <p>
 * Window to input or edit referee information; or delete an existing referees
 * <p>
 * University of Glasgow
 * MSc/PGDip Information Technology/Software Development
 * Team Project 2014/15
 *
 * @author Miroslav Pashov (1005139p)
 * @author Andrew Lowson (0800685l)
 * @author Marco Cook (2152599c)
 * @author Raoul Rothfeld (2164502r)
 * 
 * @version 1.3
 * @since 13-02-2015
 */
public final class RefereeFrame extends JFrame implements ActionListener {
	/** JFrame and spacing dimensions in pixels */
	private static final int WIDTH = 350, HEIGHT = 500, SPACING = 5;

	/** Predefined set of colours for uniform component colouring */
	private final Color background = Color.decode("0xDDDDDD"),
			highlight = Color.decode("0xFFCCCC"), border = Color.GRAY;

	/** Reference to the JavaBallController */
	private final JavaBallController controller;
	
	/** Reference to the referee to be edited */
	private Referee referee;
	
	/** Main/interactive GUI components */
	private JButton btnSave, btnRemove, btnCancel;
	private JLabel lblRefIDLabel, lblRefID;
	private JTextField fldPrevAlloc, fldFirstName, fldLastName;
	private JComboBox<RefQualification> cmbType;
	private JComboBox<Integer> cmbLevel;
	private JComboBox<Location> cmbHome;
	private JCheckBox chbxNorth, chbxCentral, chbxSouth;
	private JPanel topPanel, centrePanel, bottomPanel, travelPanel;

	/**
	 * Constructor to create JFrame and add GUI components for adding a referee
	 * @param controller the JavaBallController
	 */
	public RefereeFrame(JavaBallController controller) {
		// Store reference to JavaBallController
		this.controller = controller;

		// Set JFrame properties
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Add Referee");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null); // centres JFrame on desktop
		setResizable(false);
		
		// Establish GUI framework for holding components
		layoutFramework();
		
		// Create GUI components
		layoutRefereeDetails();
		layoutAllocations();
		layoutQualification();
		layoutLocations();
		layoutButtons();
		
		// Populate JComboBoxes
		populateComboBoxes();
		
		// Make the referee ID label update as one enters referee names
		updatableIDLabel();
		
		// Ensure that the home location is always enabled under travel
		// preferences
		protectHomeLocation();
	}

	/**
	 * Constructor to create JFrame and add GUI components for editing a referee
	 * @param controller the JavaBallController
	 * @param referee the referee whose details are to be edited
	 */
	public RefereeFrame(JavaBallController controller, Referee referee) {
		// Utilise default constructor
		this(controller);

		// Store reference to referee to be edited
		this.referee = referee;
		
		// Adjust JFrame title to represent which referee is edited
		setTitle("Edit Referee: " + referee.getID());

		// Fill GUI components with existing referee information
		setDetails();
	}
	
	/**
	 * Creates and lays out the JFrame's overall framework for GUI components
	 */
	private void layoutFramework() {
		// Create JPanels
		topPanel = new JPanel();
		centrePanel = new JPanel();
		bottomPanel = new JPanel();

		// Set JPanel layouts
		topPanel.setLayout(new BorderLayout(0, 0));
		centrePanel.setLayout(new BorderLayout(0, 0));
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, SPACING,
				SPACING));

		// Add framework JPanels to JFrame
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(centrePanel, BorderLayout.CENTER);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Creates and lays out the GUI component for entering referee names
	 */
	private void layoutRefereeDetails() {
		// Create wrapper panel (uniform design)
		JPanel refDetailsWrapper = setComponentLayout(topPanel,
				BorderLayout.NORTH, "Referee Details");

		// Create JPanels
		JPanel RefDetailsPanel = new JPanel();
		JPanel refIDPanel = new JPanel();
		JPanel firstNamePanel = new JPanel();
		JPanel lastNamePanel = new JPanel();

		// Set JPanel layouts
		RefDetailsPanel.setLayout(new GridLayout(0, 2, 0, 0));
		RefDetailsPanel.setBackground(background);
		setFlowLayout(refIDPanel, SPACING, true);
		setFlowLayout(firstNamePanel, SPACING, false);
		setFlowLayout(lastNamePanel, SPACING, false);

		// Create JLabels
		lblRefIDLabel = new JLabel("Referee ID");
		JLabel lblFirstName = new JLabel("First Name");
		JLabel lblLastName = new JLabel("Last Name");
		lblRefID = new JLabel("");

		// Set JLabel properties
		lblRefIDLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFirstName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLastName.setHorizontalAlignment(SwingConstants.RIGHT);

		// Create JTextFields
		fldFirstName = new JTextField();
		fldLastName = new JTextField();

		// Set JTextFields properties
		fldFirstName.setColumns(12);
		fldLastName.setColumns(12);

		// Add components to the content panels
		refDetailsWrapper.add(RefDetailsPanel, BorderLayout.CENTER);
		RefDetailsPanel.add(lblRefIDLabel);
		RefDetailsPanel.add(refIDPanel);
		refIDPanel.add(lblRefID);
		RefDetailsPanel.add(lblFirstName);
		RefDetailsPanel.add(firstNamePanel);
		firstNamePanel.add(fldFirstName);
		RefDetailsPanel.add(lblLastName);
		RefDetailsPanel.add(lastNamePanel);
		lastNamePanel.add(fldLastName);
	}
	
	/**
	 * Creates and lays out the GUI component for entering referee allocations
	 */
	private void layoutAllocations() {
		// Create wrapper panel (uniform design)
		JPanel allocsWrapper = setComponentLayout(topPanel,
				BorderLayout.SOUTH, "Match Allocations");
		
		// Create JPanels
		JPanel allocPanel = new JPanel();
		JPanel prevAllocPanel = new JPanel();
		
		// Set JPanel layouts/properties
		allocPanel.setLayout(new GridLayout(0, 2, 0, 0));
		allocPanel.setBackground(background);
		setFlowLayout(prevAllocPanel, SPACING, false);

		// Create JLabel and set its properties
		JLabel lblPrevAlloc = new JLabel("Previous Allocations");
		lblPrevAlloc.setHorizontalAlignment(SwingConstants.RIGHT);
		
		// Create JTextField and set its properties
		fldPrevAlloc = new JTextField();
		fldPrevAlloc.setColumns(SPACING);

		// Add components to the content panels
		allocsWrapper.add(allocPanel, BorderLayout.CENTER);
		allocPanel.add(lblPrevAlloc);
		allocPanel.add(prevAllocPanel);
		prevAllocPanel.add(fldPrevAlloc);
	}
	
	/**
	 * Creates and lays out the GUI component for entering referee qualification
	 */
	private void layoutQualification() {
		// Create wrapper panel (uniform design)
		JPanel qualificationWrapper = setComponentLayout(centrePanel,
				BorderLayout.NORTH, "Referee Qualification");

		// Create JPanels
		JPanel qualificationPanel = new JPanel();
		JPanel qualTypePanel = new JPanel();
		JPanel qualLevelPanel = new JPanel();

		// Set JPanel layouts/properties
		qualificationPanel.setLayout(new GridLayout(0, 2, 0, 0));
		qualificationPanel.setBackground(background);
		setFlowLayout(qualTypePanel, SPACING, false);
		setFlowLayout(qualLevelPanel, SPACING, false);
		
		// Create JLabels
		JLabel lblQualificationType = new JLabel("Qualification Type");
		JLabel lblQualificationLevel = new JLabel("Qualification Level");

		// Set JLabel properties
		lblQualificationType.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQualificationLevel.setHorizontalAlignment(SwingConstants.RIGHT);

		// Create JComboBoxes
		cmbType = new JComboBox<RefQualification>();
		cmbLevel = new JComboBox<Integer>();

		// Add components to the content panels
		qualificationWrapper.add(qualificationPanel, BorderLayout.CENTER);
		qualificationPanel.add(lblQualificationType);
		qualificationPanel.add(qualTypePanel);
		qualTypePanel.add(cmbType);
		qualificationPanel.add(lblQualificationLevel);
		qualificationPanel.add(qualLevelPanel);
		qualLevelPanel.add(cmbLevel);
	}
	
	/**
	 * Creates and lays out the GUI component for entering referee home location
	 * and travel preferences
	 */
	private void layoutLocations() {
		// Create wrapper panel (uniform design)
		JPanel locationWrapper = setComponentLayout(centrePanel,
				BorderLayout.CENTER, "Location Details");

		// Create JPanels
		JPanel locationPanel = new JPanel();
		JPanel homeWrapper = new JPanel();
		JPanel homePanel = new JPanel();
		JPanel travelLabelWrapper = new JPanel();
		JPanel checkBoxWrapper = new JPanel();
		travelPanel = new JPanel();

		// Set JPanel layouts/properties
		locationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		locationPanel.setBackground(background);
		checkBoxWrapper.setBackground(background);
		travelLabelWrapper.setBackground(background);
		homeWrapper.setBackground(background);
		homePanel.setBackground(background);
		homeWrapper.setLayout(new GridLayout(0, 2, 0, 0));
		travelLabelWrapper.setLayout(new GridLayout(0, 2, 0, 0));
		travelPanel.setLayout(new GridLayout(0, 1, 0, 0));
		setFlowLayout(homePanel, SPACING, false);
		setFlowLayout(checkBoxWrapper, 3, false);
		
		// Create JLabels
		JLabel lblHome = new JLabel("Home Location");
		JLabel lblTravel = new JLabel("Travel Preferences");
		
		// Set JLabels properties
		lblHome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTravel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		// Create JComboBox
		cmbHome = new JComboBox<Location>();
		
		// Create JCheckBoxes for selecting the referee's travel preferences
		chbxNorth = new JCheckBox(Location.NORTH.toString());
		chbxCentral= new JCheckBox(Location.CENTRAL.toString());
		chbxSouth = new JCheckBox(Location.SOUTH.toString());
		
		// Set JCheckBoxes properties
		chbxNorth.setBackground(background);
		chbxCentral.setBackground(background);
		chbxSouth.setBackground(background);
		
		// Add components to the content panels
		locationWrapper.add(locationPanel, BorderLayout.CENTER);
		locationPanel.add(homeWrapper);
		homeWrapper.add(lblHome);
		homeWrapper.add(homePanel);
		homePanel.add(cmbHome);
		locationPanel.add(travelLabelWrapper);
		travelLabelWrapper.add(lblTravel);
		travelLabelWrapper.add(checkBoxWrapper);
		checkBoxWrapper.add(travelPanel);
		travelPanel.add(chbxNorth);
		travelPanel.add(chbxCentral);
		travelPanel.add(chbxSouth);
	}
	
	/**
	 * Creates and adds JButtons to the bottom JPanel; includes action listeners
	 */
	private void layoutButtons() {
		// Create buttons
		btnSave = new JButton("Save");
		btnRemove = new JButton("Remove");
		btnCancel = new JButton("Cancel");

		// Add action listeners
		btnSave.addActionListener(this);
		btnRemove.addActionListener(this);
		btnCancel.addActionListener(this);
		
		// Add buttons to the bottom JPanel
		bottomPanel.add(btnSave);
		bottomPanel.add(btnRemove);
		bottomPanel.add(btnCancel);
	}
	
	/**
	 * Populates GUI JComboBoxes with content
	 */
	private void populateComboBoxes() {
		// Populate JComboBox for selecting referee's qualification type		
		cmbType.setModel(new DefaultComboBoxModel<RefQualification>(
				RefQualification.values()));
		
		// Populate JComboBox for selecting referee's qualification level
		for (int level = 1; level <= 4; level++)
			cmbLevel.addItem(level);
		
		// Populate JComboBox for selecting referee's home location
		cmbHome.setModel(new DefaultComboBoxModel<Location>(Location.values()));
	}

	/**
	 * Adds FocusListeners to the referee name fields to make the ID update as
	 * one types the names of a referee to be added
	 */
	private void updatableIDLabel() {
		// Create new FocusAdapter
		FocusAdapter idUpdater = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				// Only update ID if referee does not already have a defined ID
				if (referee == null) {
					// Retrieve user input for referee names
					String firstName = fldFirstName.getText().trim();
					String lastName = fldLastName.getText().trim();
					
					// If names were entered update referee ID
					if ((firstName != null && lastName != null)) {
						if (!(firstName.equals("") || lastName.equals(""))) {
							// Retrieve ID and set label to that ID
							String ID = controller
									.createID(firstName, lastName);
							lblRefID.setText(ID);
						}
					}
				}
			}
		};
		
		// Add the defined FocusListener to the name fields
		fldFirstName.addFocusListener(idUpdater);
		fldLastName.addFocusListener(idUpdater);
	}
	
	/**
	 * TODO COMMENT
	 */
	private void protectHomeLocation() {
		// NORTH IS DEFAULT
		chbxNorth.setSelected(true);
		chbxNorth.setEnabled(false);
		
		cmbHome.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				Location selectedLocation = (Location) cmbHome
						.getSelectedItem();
				
				if (selectedLocation.equals(Location.NORTH)) {
					chbxCentral.setEnabled(true);
					chbxSouth.setEnabled(true);

					chbxNorth.setSelected(true);
					chbxNorth.setEnabled(false);
				} else if ((selectedLocation.equals(Location.CENTRAL))) {
					chbxNorth.setEnabled(true);
					chbxSouth.setEnabled(true);

					chbxCentral.setSelected(true);
					chbxCentral.setEnabled(false);
				} else {
					chbxNorth.setEnabled(true);
					chbxCentral.setEnabled(true);

					chbxSouth.setSelected(true);
					chbxSouth.setEnabled(false);
				}
			}
		});
	}
	
	/**
	 * Creates the wrapper for a standard GUI component with a header and
	 * predefined borders, spacing, and colours; used to create a uniform design
	 * @param panel the JPanel to which the wrapper shall be added
	 * @param borderLayout the direction in which the wrapper shall be added
	 * @param title the title displayed as the wrapper's heading
	 * @return the inner JPanel to which further GUI components can be added
	 */
	private JPanel setComponentLayout(JPanel panel, String borderLayout,
			String title) {
		// Create JPanels
		JPanel wrapper = new JPanel();
		JPanel wrapperHeader = new JPanel();
		JPanel content = new JPanel();
		
		// Set JPanel layouts/properties
		wrapper.setLayout(new BorderLayout(0, 0));
		wrapperHeader.setLayout(new BorderLayout(0, 0));
		content.setLayout(new BorderLayout(0, 0));
		content.setBackground(background);
		content.setBorder(new LineBorder(border));

		// Create spacers
		Component headerSpacer = Box.createHorizontalStrut(SPACING);
		Component innerTop = Box.createVerticalStrut(SPACING);
		Component innerRight = Box.createHorizontalStrut(SPACING);
		Component innerBottom = Box.createVerticalStrut(SPACING);
		Component innerLeft = Box.createHorizontalStrut(SPACING);
		Component outerTop = Box.createVerticalStrut(SPACING);
		Component outerRight = Box.createHorizontalStrut(SPACING);
		Component outerBottom = Box.createVerticalStrut(SPACING);
		Component outerLeft = Box.createHorizontalStrut(SPACING);

		// Create header JLabel
		JLabel lblTitle = new JLabel(title);

		// Add wrapper panel to the framework panel
		panel.add(wrapper, borderLayout);

		// Add components to the wrapper and content panels
		wrapper.add(wrapperHeader, BorderLayout.NORTH);
		wrapperHeader.add(outerTop, BorderLayout.NORTH);
		wrapperHeader.add(headerSpacer, BorderLayout.WEST);
		wrapperHeader.add(lblTitle, BorderLayout.CENTER);
		wrapper.add(content, BorderLayout.CENTER);
		wrapper.add(outerRight, BorderLayout.EAST);
		wrapper.add(outerLeft, BorderLayout.WEST);
		wrapper.add(outerBottom, BorderLayout.SOUTH);
		content.add(innerTop, BorderLayout.NORTH);
		content.add(innerRight, BorderLayout.EAST);
		content.add(innerBottom, BorderLayout.SOUTH);
		content.add(innerLeft, BorderLayout.WEST);

		// Return the content panel which is nested within the wrapper panel
		return content;
	}

	/**
	 * Applies the defined FlowLayout and colouring to a JPanel
	 * @param panel the JPanel to which the standard FlowLayout is applied
	 * @param pixels the number of pixels used for spacing
	 * @param vGap whether vertical spacing shall be applied
	 */
	private void setFlowLayout(JPanel panel, int pixels, boolean vGap) {
		// Set JPanel background to the defined background colour
		panel.setBackground(background);

		// Apply standard FlowLayout settings
		FlowLayout fl_refIDPanel = (FlowLayout) panel.getLayout();
		fl_refIDPanel.setHgap(pixels * 2); // Horizontal spacing
		fl_refIDPanel.setAlignment(FlowLayout.LEFT); // Left text alignment
		
		// Add vertical spacing if specified
		if (vGap)
			fl_refIDPanel.setVgap(pixels + 2); // Vertical spacing
	}

	/**
	 * TODO REWORK
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// Test which button has been pressed
		if (ae.getSource() == btnSave) {
			// Test if more referees can be added
			if (controller.indexCounter() == RefereeList.MAX_REFEREES
					&& referee == null)
				JOptionPane.showMessageDialog(null,
						"No more referees can be added.");
			else
				// Execute saving/editing procedure
				processReferee();
		} else if (ae.getSource() == btnRemove) {
			// Remove referee and close window
			controller.removeReferee(referee);
			dispose();
		} else {
			// Close window as cancel button has been pressed
			dispose();
		}
	}
	
	/**
	 * TODO REWORK
	 */
	public void processReferee() {
		// REVERT COLOURS
		fldFirstName.setBackground(Color.WHITE);
		fldLastName.setBackground(Color.WHITE);
		fldPrevAlloc.setBackground(Color.WHITE);
		
		boolean validInput = true;
		
		// Get travel locations for referee
		String n = chbxNorth.isSelected() ? "Y" : "N";
		String c = chbxCentral.isSelected() ? "Y" : "N";
		String s = chbxSouth.isSelected() ? "Y" : "N";
		
		String travel = n + c + s;
		
		RefQualification qual = (RefQualification) cmbType.getSelectedItem();
		
		int qualLevel = cmbLevel.getSelectedIndex() + 1;
		
		Location home = (Location) cmbHome.getSelectedItem();

		if (this.referee == null) {
			try {

				String firstName = fldFirstName.getText().trim();
				String lastName = fldLastName.getText().trim();
				

				if (firstName == null || firstName.equals("")
						|| Pattern.matches("[\\dA-Z]+", firstName)
						|| firstName.contains(" ")) {
					validInput = false;
					invalidInput(fldFirstName);
				}
				
				if (lastName == null || lastName.equals("")
						|| Pattern.matches("[\\dA-Z]+", lastName)
						|| lastName.contains(" ")) {
					validInput = false;
					invalidInput(fldLastName);
				}
				
				int prevAlloc = Integer.parseInt(fldPrevAlloc.getText().trim());
				
				if (validInput) {
					controller.addReferee(firstName, lastName, qual, qualLevel,
							prevAlloc, home, travel);
					
					controller.updateTable();
					
					dispose();
				} else {
					JOptionPane.showMessageDialog(null,
							"Please enter valid data.");
				}
			} catch (NumberFormatException e) {
				invalidInput(fldPrevAlloc);
				JOptionPane.showMessageDialog(null, "Please enter valid data.");
			}
		} else {
			controller.editReferee(referee, qual, qualLevel, home, travel);
			
			controller.updateTable();
			
			dispose();
		}
	}
	
	/**
	 * TODO COMMENT
	 * @param field
	 */
	private void invalidInput(JTextField field) {
		field.setText("");
		field.setBackground(highlight);
	}
	
	/**
	 * Sets the GUI component for home location to the referee's home location
	 */
	public void setHomeLocation() {
		cmbHome.setSelectedItem(referee.getHomeLocation());
	}

	/**
	 * Enables or disables the remove button 
	 * @param enabled whether the remove button should be enabled or not
	 */
	public void setRemoveButtonEnabled(boolean enabled) {
		btnRemove.setEnabled(enabled);
	}

	/**
	 * Displays the referee details according to the given referee if a referee
	 * is to be edited (instead of added)
	 */
	private void setDetails() {
		// Set ID, names, and the number of previous allocations according to
		// the referee's information
		lblRefID.setText(referee.getID());
		fldFirstName.setText(referee.getFirstName());
		fldLastName.setText(referee.getLastName());
		fldPrevAlloc.setText(Integer.toString(referee.getAllocations()));

		// Set the qualification type according to the referee's qualification
		if (referee.getQualification().equals(RefQualification.IJB))
			cmbType.setSelectedItem(RefQualification.IJB);
		else
			cmbType.setSelectedItem(RefQualification.NJB);

		// Set the qualification level according to the referee's qualification
		cmbLevel.setSelectedItem(referee.getQualificationLevel());

		// Prohibit editing the name or previous allocations of a given referee
		fldFirstName.setEditable(false);
		fldLastName.setEditable(false);
		fldPrevAlloc.setEditable(false);
	}

	/**
	 * Sets the GUI component for travel preferences to the referee's travel
	 * preferences
	 */
	public void setLocations() {
		// Set north JCombobox according to referee's travel preference
		chbxNorth.setSelected(controller.travelPreference(referee,
				Location.NORTH));
		
		// Set central JCombobox according to referee's travel preference
		chbxCentral.setSelected(controller.travelPreference(referee,
				Location.CENTRAL));
		
		// Set south JCombobox according to referee's travel preference
		chbxSouth.setSelected(controller.travelPreference(referee,
				Location.SOUTH));
	}
}