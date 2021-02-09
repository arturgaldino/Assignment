package Controllers;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.Report;

public final class FileController {

	private static JFileChooser fileChooser = new JFileChooser();
	private static FileNameExtensionFilter logFilterExtension = new FileNameExtensionFilter("Log Files", "log");
	private static FileNameExtensionFilter xmlFilterExtension = new FileNameExtensionFilter("Xml Files", "xml");
	private final static String OPEN_TITLE = "Open log file";
	private final static String SAVE_TITLE = "Select the directory to save the output";
	
	public static Path selectFilePath() {
		fileChooser.setFileFilter(logFilterExtension);
		fileChooser.setDialogTitle(OPEN_TITLE);
        int returnVal = fileChooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	return Paths.get(fileChooser.getSelectedFile().getAbsolutePath());
        }
        return null;
	}
	
	public static List<String> getAllLinesFromLog(Path path) throws IOException{
		return Files.readAllLines(path);
	}
	
	public static Path selectPathToSaveFile(Path defaultPath) {
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogTitle(SAVE_TITLE);
		fileChooser.setFileFilter(xmlFilterExtension);
		File dir = new File (defaultPath.getParent().toString());
		fileChooser.setCurrentDirectory(new java.io.File(dir, "output.xml"));
		int returnVal = fileChooser.showSaveDialog(null);
		
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	return Paths.get(fileChooser.getSelectedFile().getAbsolutePath());
        }
        return null;
	}
	
	
	public static void saveReportToXML(Report report, Path defaultPath) throws JAXBException, RuntimeException{
		Path folderPath = FileController.selectPathToSaveFile(defaultPath);
		if(folderPath == null) {
			throw new java.lang.RuntimeException("Directory not selected");
		}
		File fileReport = new File (folderPath.toFile().toString());
		
        JAXBContext jaxbContext = JAXBContext.newInstance(Report.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(report, fileReport);
	}
}
