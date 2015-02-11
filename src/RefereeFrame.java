import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
 * @version 1.2
 * @since 11-02-2015
 */
public final class RefereeFrame extends JFrame implements ActionListener {

	// GUI components for referee frame
	private JPanel mainPanel, refereePanel, qualificationsPanel, locationsPanel;
	private JLabel idLabel,refIDLabel,fnameLabel,lnameLabel,allocationLabel,
	qualLevelLabel,homeLabel;
	private JTextField refFnameField, refLnameField, refMatchesField;
	private JComboBox qualLevel;
	private JComboBox homeLoc;
	private JRadioButton njbButton, ijbButton;
	private JCheckBox northCheckbox, centralCheckbox, southCheckbox;
	private JButton saveButton, removeButton, cancelButton;
	private ButtonGroup qualificationGroup;
	// Title for referee frame
	private String refFrameTitle;

	private Referee referee;
	private final JavaBallController controller;
	// Dimensions of referee frame
	private final int FRAME_WIDTH = 400;
	private final int FRAME_HEIGHT = 400;

	/**
	 * Constructor to add components and create frame.
	 * @param controller
	 */
	public RefereeFrame(JavaBallController controller) {

		this.controller = controller;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(refFrameTitle);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null); //centres JFrame on desktop
		setResizable(false);
		mainPanel = new JPanel(new BorderLayout());
		// Adds top GUI components
		refLayout();
		qualificationsLayout();
		locationsAndButtonsLayout();
		add(mainPanel);

	}

	/**
	 *
	 * @param controller
	 * @param referee
	 */
	public RefereeFrame(JavaBallController controller, Referee referee) {

		// Calls the default constructor
		this(controller);
		this.referee = referee;
	}

	/**
	 *
	 * @param referee
	 */
	public void setReferee(Referee referee) {
		this.referee = referee;
		setDetails();
	}

	/**
	 * Method to create and add referee detail components to main panel in the 
	 * RefereeFrame GUI. Details include referee ID, first name, second name,
	 * and number of match allocations.
	 */
	public void refLayout() {

		refereePanel = new JPanel(new BorderLayout());
		JPanel refereeSubPanel = new JPanel(new GridLayout(4,2)); 
		refereeSubPanel.setBackground(Color.lightGray);
		JPanel eastPanel = new JPanel();
		JPanel westPanel = new JPanel();
		JPanel southPanel = new JPanel();

		idLabel = new JLabel();
		idLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		idLabel.setText("ID:");
		fnameLabel = new JLabel();
		fnameLabel.setText("First Name:");
		fnameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lnameLabel = new JLabel();
		lnameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lnameLabel.setText("Last Name:");
		allocationLabel = new JLabel("Match Allocations:");
		allocationLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		refIDLabel = new JLabel();
		refFnameField = new JTextField(5);
		refLnameField = new JTextField(5);
		refMatchesField = new JTextField(5);

		// update ID while writing
		FocusAdapter idUpdater = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String firstName = refFnameField.getText().trim();
				String lastName = refLnameField.getText().trim();
				if ((firstName != null && lastName != null)) {
					if (!(firstName.equals("") || lastName.equals(""))) {
						String ID = controller.createID(firstName, lastName);
						refIDLabel.setText(ID);
					}
				}
			}
		};
		refLnameField.addFocusListener(idUpdater);
		refFnameField.addFocusListener(idUpdater);

		refereeSubPanel.add(idLabel);
		refereeSubPanel.add(refIDLabel);
		refereeSubPanel.add(fnameLabel);
		refereeSubPanel.add(refFnameField);
		refereeSubPanel.add(lnameLabel);
		refereeSubPanel.add(refLnameField);
		refereeSubPanel.add(allocationLabel);
		refereeSubPanel.add(refMatchesField);

		// Creates panel for panel title
		JPanel titlePanel = new JPanel();
		// Creates label for title panel
		JLabel title = new JLabel("Referee Details");
		title.setFont(new Font("Monospaced",Font.BOLD,14));
		// Adds label to panel
		titlePanel.add(title);


		refereeSubPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		refereePanel.add(eastPanel, BorderLayout.EAST);
		refereePanel.add(westPanel, BorderLayout.WEST);
		refereePanel.add(southPanel, BorderLayout.SOUTH);
		refereePanel.add(titlePanel, BorderLayout.NORTH);
		refereePanel.add(refereeSubPanel, BorderLayout.CENTER);
		add(refereePanel, BorderLayout.NORTH);
	}

	/**
	 * Method to create and add referee qualification detail components to main
	 *  panel in the RefereeFrame GUI.
	 */
	public void qualificationsLayout() {

		qualificationsPanel = new JPanel(new BorderLayout());
		qualificationsPanel.setBackground(Color.lightGray);

		JPanel qualSubPanel = new JPanel(new GridLayout(2,2)); 
		qualSubPanel.setBackground(Color.lightGray);
		JPanel east = new JPanel();
		JPanel west = new JPanel();
		JPanel south = new JPanel();

		njbButton = new JRadioButton(Referee.Qualifications.NJB.toString());
		ijbButton = new JRadioButton(Referee.Qualifications.IJB.toString());
		qualificationGroup = new ButtonGroup();
		qualificationGroup.add(njbButton);
		qualificationGroup.add(njbButton);

		njbButton.setHorizontalAlignment(SwingConstants.RIGHT);
		njbButton.setFont(new Font("Monospaced",Font.PLAIN,14));

		ijbButton.setHorizontalAlignment(SwingConstants.RIGHT);
		ijbButton.setFont(new Font("Monospaced",Font.PLAIN,14));

		qualLevelLabel = new JLabel("Level");
		qualLevel = new JComboBox<Integer>();
		
		// Adds level values to qualification level JComboBox 
		for (int i = 1; i <= Referee.MAX_QUAL_LENGTH; i++) {
			qualLevel.addItem(i + "");
		}
		qualLevelLabel.setHorizontalAlignment(SwingConstants.CENTER);

		ButtonGroup qualButtons = new ButtonGroup();
		qualButtons.add(njbButton);
		qualButtons.add(ijbButton);

		qualSubPanel.add(njbButton);
		qualSubPanel.add(qualLevelLabel);
		qualSubPanel.add(ijbButton);
		qualSubPanel.add(qualLevel);

		// Creates panel for panel title
		JPanel titlePanel = new JPanel();
		// Creates label for title panel
		JLabel title = new JLabel("Qualification Details");
		title.setFont(new Font("Monospaced",Font.BOLD,14));
		// Adds label to panel
		titlePanel.add(title);

		qualSubPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		qualificationsPanel.add(east, BorderLayout.EAST);
		qualificationsPanel.add(west, BorderLayout.WEST);
		qualificationsPanel.add(south, BorderLayout.SOUTH);
		qualificationsPanel.add(titlePanel, BorderLayout.NORTH);
		qualificationsPanel.add(qualSubPanel, BorderLayout.CENTER);
		mainPanel.add(qualificationsPanel, BorderLayout.CENTER);
	}


	/**
	 * Method to create and add referee location detail components and the 
	 * buttons to the main panel in the RefereeFrame GUI.
	 */
	public void locationsAndButtonsLayout() {

		locationsPanel = new JPanel(new BorderLayout());
		JPanel locationSubPanel = new JPanel(new BorderLayout());

		JPanel homePanel = new JPanel();
		JPanel visitPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		JPanel eastPanel = new JPanel();
		JPanel westPanel = new JPanel();
		JPanel southPanel = new JPanel();

		homeLabel = new JLabel("Home");
		homeLoc = new JComboBox();
		homeLoc.addItem("North");
		homeLoc.addItem("Central");
		homeLoc.addItem("South");


		northCheckbox = new JCheckBox("North");
		centralCheckbox = new JCheckBox("Central");
		southCheckbox = new JCheckBox("South");

		saveButton = new JButton("Save");
		removeButton = new JButton("Remove");
		cancelButton = new JButton("Cancel");

		saveButton.addActionListener(this);
		removeButton.addActionListener(this);
		cancelButton.addActionListener(this);

		homeLoc.setModel(new DefaultComboBoxModel(
				JavaBallController.Location.values()));

		homeLoc.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JavaBallController.Location selectedLocation = 
						(JavaBallController.Location) homeLoc
						.getSelectedItem();
				if (selectedLocation.equals(homeLoc.getItemAt(0))) 
				{
					northCheckbox.setSelected(true);
					centralCheckbox.setSelected(false);
					southCheckbox.setSelected(false);
				} else if ((selectedLocation.equals(homeLoc.getItemAt(1)))) 
				{
					northCheckbox.setSelected(false);
					southCheckbox.setSelected(false);
					centralCheckbox.setSelected(true);
				} else 
				{
					northCheckbox.setSelected(false);
					centralCheckbox.setSelected(false);
					southCheckbox.setSelected(true);
				}
			}
		});

		homePanel.add(homeLabel);
		homePanel.add(homeLoc);
		visitPanel.add(northCheckbox);
		visitPanel.add(centralCheckbox);
		visitPanel.add(southCheckbox);
		buttonPanel.add(saveButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(cancelButton);

		// Creates panel for panel title
		JPanel titlePanel = new JPanel();
		// Creates label for title panel
		JLabel title = new JLabel("Location Details");
		title.setFont(new Font("Monospaced",Font.BOLD,14));
		// Adds label to panel
		titlePanel.add(title);

		homePanel.setBackground(Color.lightGray);
		visitPanel.setBackground(Color.lightGray);
		//buttonPanel.setBackground(Color.lightGray);

		locationSubPanel.add(homePanel, BorderLayout.NORTH);
		locationSubPanel.add(visitPanel, BorderLayout.CENTER);
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		locationSubPanel.add(buttonPanel, BorderLayout.SOUTH);

		locationsPanel.add(titlePanel, BorderLayout.NORTH);
		locationsPanel.add(eastPanel, BorderLayout.EAST);
		locationsPanel.add(westPanel, BorderLayout.WEST);
		locationsPanel.add(southPanel, BorderLayout.SOUTH);

		locationSubPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		locationsPanel.add(locationSubPanel, BorderLayout.CENTER);
		mainPanel.add(locationsPanel, BorderLayout.SOUTH);

	}


	/** 
	 * Helper method to set the Remove referee button state
	 * @param state 
	 */
	public void setRemoveButtonEnabled(boolean state) {
		removeButton.setEnabled(state);
	}

	/**
	 * 
	 */
	private void setDetails(){

		refIDLabel.setText(referee.getID());
		refFnameField.setText(referee.getFirstName());
		refLnameField.setText(referee.getLastName());
		refMatchesField.setText(Integer.toString(referee.getAllocations()));

		refFnameField.setEditable(false);
		refLnameField.setEditable(false);
		refMatchesField.setEditable(false);

	}

	/**
	 *
	 */
	public void setLocations() {
		northCheckbox.setSelected(controller.refTravel(referee, JavaBallController.Location.NORTH));
		centralCheckbox.setSelected(controller.refTravel(referee, JavaBallController.Location.CENTRAL));
		southCheckbox.setSelected(controller.refTravel(referee, JavaBallController.Location.SOUTH));
	}


	/**
	 * This method handles events for the the Referee Frame (i.e. adding,
	 * editing and removing referee information)
	 * 
	 * @param ae
	 */
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == cancelButton) {

			// Close window
			dispose();
		}
		if (ae.getSource() == removeButton) {
			controller.removeReferee(referee);
			dispose();
		}
		if (ae.getSource() == saveButton) {

			if (controller.indexCounter() == RefereeList.MAX_REFEREES) {
				JOptionPane.showMessageDialog(null, "Sorry.\n"
						+ "The Referee List is full!");

			} else {

				if (refFnameField.getText() == null || refFnameField.getText().equals(""))
				{
					fnameLabel.setForeground(Color.red);
				}  
				if (refLnameField.getText() == null || refLnameField.getText().equals(""))
				{
					lnameLabel.setForeground(Color.red);
				}
				if (refMatchesField.getText() == null || refMatchesField.getText().equals(""))
				{
					allocationLabel.setForeground(Color.red);
				}
				// Get travel locations for referee
				String n = northCheckbox.isSelected() ? "Y" : "N";
				String c = centralCheckbox.isSelected() ? "Y" : "N";
				String s = southCheckbox.isSelected() ? "Y" : "N";
				String travel = n + c + s;
				Referee.Qualifications qual = 
						ijbButton.isSelected() ? Referee.Qualifications.IJB : Referee.Qualifications.NJB ;

				if (this.referee == null) 
				{
					if (!(refFnameField.getText().equals("")) && !(refLnameField.getText().equals("")))
					{
						controller.addReferee(refFnameField.getText(),
								refLnameField.getText(),qual,
								Integer.parseInt(String.valueOf(qualLevel.getSelectedItem())), 
								Integer.parseInt(refMatchesField.getText()),
								(JavaBallController.Location) homeLoc.getSelectedItem(), travel);
						controller.updateTable();
						dispose();

					} else {
						JOptionPane.showMessageDialog(null, "Please enter valid data.\n"+"Invalid Entries are in red");
					}				

				}
				else {
					controller.editReferee(referee, qual,
							Integer.parseInt(String.valueOf(qualLevel.getSelectedItem())),
							(JavaBallController.Location) homeLoc.getSelectedItem(), travel);

					controller.updateTable();
					dispose();
				}
			}
		}
	}
}