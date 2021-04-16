package Lab_2.Lab1.AutoShow;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class MyValidator {
	
	private static String XML;
	private static String XSD;
	
	public MyValidator(String XML, String XSD) {
		MyValidator.XML = XML;
		MyValidator.XSD = XSD;
	}

	public boolean isValid() {
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(XSD));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(XML)));
		} catch (SAXParseException e) {
			System.out.println("SAX Parse Exception: line " + e.getLineNumber() + ".\n" + e.getMessage());
			return false;
		} catch (SAXException e) {
			System.out.print("Global SAX Exception:\n" + e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.print("Input/Output Stream Exception:\n" + e.getMessage());
		}
		return true;
	}
	
}