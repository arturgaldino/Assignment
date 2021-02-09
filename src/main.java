import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.JAXBException;

import Controllers.FileController;
import Controllers.ReportController;
import model.Report;

public class main extends JFrame {

	private JPanel contentPane;
	private JTextField inputField;
	private Path pathSelected;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main frame = new main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton inputFileButton = new JButton("Input File");
		inputFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pathSelected = FileController.selectFilePath();
				if(pathSelected != null) {
					inputField.setText(pathSelected.toString());
				}
			}
		});
		panel.add(inputFileButton);
		
		inputField = new JTextField();
		panel.add(inputField);
		inputField.setColumns(10);
		inputField.setEditable(false);
		
		JButton runButton = new JButton("Generate output");
		panel.add(runButton);
		
		JLabel resultLabel = new JLabel();
		panel.add(resultLabel);
		
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(pathSelected == null) {
					resultLabel.setText("File not Selected");
				}else {
					resultLabel.setText("");
					try {
						List<String> listLines = FileController.getAllLinesFromLog(pathSelected);
						ReportController renderingController = new ReportController();
						Report report = renderingController.createReportFile(listLines);
						FileController.saveReportToXML(report,pathSelected);
						resultLabel.setText("Output saved successfully");
					} catch (IOException e1) {
						e1.printStackTrace();
						resultLabel.setText("An error occured while reading the file");
					} 
					catch (JAXBException e1) {
						e1.printStackTrace();
						resultLabel.setText("An error occured while saving the xml file");
					}
					catch(RuntimeException e1) {
						resultLabel.setText("Save operation canceled");
					}
				}
			}
		});
	}

}
