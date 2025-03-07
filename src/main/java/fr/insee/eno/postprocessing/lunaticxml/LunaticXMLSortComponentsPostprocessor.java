package fr.insee.eno.postprocessing.lunaticxml;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import fr.insee.eno.exception.Utils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insee.eno.Constants;
import fr.insee.eno.exception.EnoGenerationException;
import fr.insee.eno.parameters.PostProcessing;
import fr.insee.eno.postprocessing.Postprocessor;
import fr.insee.eno.transform.xsl.XslTransformation;

/**
 * Customization of JS postprocessor.
 */
public class LunaticXMLSortComponentsPostprocessor implements Postprocessor {

	private static final Logger logger = LoggerFactory.getLogger(LunaticXMLSortComponentsPostprocessor.class);

	private XslTransformation saxonService = new XslTransformation();

	private static final String styleSheetPath = Constants.TRANSFORMATIONS_SORT_COMPONENTS_LUNATIC_XML;

	@Override
	public File process(File input, byte[] parameters, String surveyName) throws Exception {

		File outputForJSFile = new File(input.getParent(),
				Constants.BASE_NAME_FORM_FILE +
				Constants.SORT_COMPONENTS_LUNATIC_XML_EXTENSION);
		logger.debug("Output folder for basic-form : " + outputForJSFile.getAbsolutePath());

		InputStream JS_XSL = Constants.getInputStreamFromPath(styleSheetPath);
		InputStream inputStream = FileUtils.openInputStream(input);
		OutputStream outputStream = FileUtils.openOutputStream(outputForJSFile);
		try {
			saxonService.transformLunaticXMLToLunaticXMLSimplePost(inputStream,outputStream, JS_XSL, parameters);
		}catch(Exception e) {
			String errorMessage = String.format("An error was occured during the %s transformation. %s : %s",
					toString(),
					e.getMessage(),
					Utils.getErrorLocation(styleSheetPath,e));
			logger.error(errorMessage);
			throw new EnoGenerationException(errorMessage);
		}

		inputStream.close();
		outputStream.close();
		JS_XSL.close();
		logger.info("End JS sort component post-processing");

		return outputForJSFile;
	}

	public String toString() {
		return PostProcessing.LUNATIC_XML_SORT_COMPONENTS.name();
	}

}
