/*******************************************************************************
 * Copyright (c) 2014 Proxima Centauri SRL <info@proxima-centauri.it>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Proxima Centauri SRL <info@proxima-centauri.it> - initial API and implementation
 ******************************************************************************/
package it.proximacentauri.ienergy.da.report;

import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

public class CustomJasperReportsMultiFormatView extends JasperReportsMultiFormatView {

	@Override
	protected void renderReport(JasperPrint populatedReport, Map<String, Object> model, HttpServletResponse response) throws Exception {

		Properties contentDispositions = this.getContentDispositionMappings();

		contentDispositions.clear();

		String fileNameString = "attachment; filename=" + model.get("filename");
		contentDispositions.setProperty("csv", fileNameString + ".csv");
		contentDispositions.setProperty("xls", fileNameString + ".xls");
		contentDispositions.setProperty("html", fileNameString + ".html");
		contentDispositions.setProperty("pdf", fileNameString + ".pdf");

		super.renderReport(populatedReport, model, response);
	}
}
