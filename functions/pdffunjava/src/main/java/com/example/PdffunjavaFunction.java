package com.example;

import com.salesforce.functions.jvm.sdk.Context;
import com.salesforce.functions.jvm.sdk.InvocationEvent;
import com.salesforce.functions.jvm.sdk.SalesforceFunction;
import com.salesforce.functions.jvm.sdk.data.Record;
import com.salesforce.functions.jvm.sdk.data.DataApi;
import com.salesforce.functions.jvm.sdk.data.RecordModificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * Describe PdffunjavaFunction here.
 */
public class PdffunjavaFunction implements SalesforceFunction<FunctionInput, FunctionOutput> {
  private static final Logger LOGGER = LoggerFactory.getLogger(PdffunjavaFunction.class);

  @Override
  public FunctionOutput apply(InvocationEvent<FunctionInput> event, Context context)
      throws Exception {

        LOGGER.info("Function Started");
        String  pdfData = event.getData().getPdfData();
        
        LOGGER.info("Function successfully Got Data");
        LOGGER.info("pdfData"+pdfData);
              String currentPath = new java.io.File(".").getCanonicalPath();
              String inputFilePath = currentPath+"/lib/PlaceHolderFile.pdf";
              LOGGER.info("inputFilePath"+inputFilePath);
              File file = new File(inputFilePath);
              try ( FileOutputStream fos = new FileOutputStream(file); ) {
                byte[] decoder = Base64.getDecoder().decode(pdfData);
                LOGGER.info("decoder"+decoder);
                fos.write(decoder);
                LOGGER.info("PDF File Saved");
              } catch (Exception e) {
                LOGGER.info("Exception Came"+e.getMessage());
              }

    
        //String currentPath = new java.io.File(".").getCanonicalPath();

        //String inputFilePath = currentPath+"/lib/formPdf.pdf"; // Existing file
        LOGGER.info("Sign Start");

        String outputFilePath = currentPath+"/lib/pdfDoc1.pdf"; // New file
        PdfReader reader = new PdfReader(inputFilePath);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFilePath));
       AcroFields form = stamper.getAcroFields();
       form.setField("Signature_1", event.getData().getSign());
       form.setField("Signature_date", event.getData().getSignDate());
       stamper.setFormFlattening(true);
       stamper.close();
       

       LOGGER.info("Sign Done");
       byte[] input_file = Files.readAllBytes(Paths.get(outputFilePath));
       byte[] encodedBytes = Base64.getEncoder().encode(input_file);
       String pdfInBase64 = new String(encodedBytes);
       LOGGER.info("Creating content Version");
       return new FunctionOutput(pdfInBase64);
       /* 
       DataApi dataApi = context.getOrg().get().getDataApi();
       Record contentVersion =
        dataApi
            .newRecordBuilder("ContentVersion")
            .withField("VersionData", pdfInBase64)
            .withField("Title", "SomeFile")
            .withField("origin", "H")
            .withField("PathOnClient", "HolaAmigo.pdf")
            .build();
            LOGGER.info("inserting content Version");
    RecordModificationResult createResult = dataApi.create(contentVersion);

    LOGGER.info("content insert Done");
    String queryString =
        String.format("SELECT Id, ContentDocumentId FROM ContentVersion WHERE Id = '%s'", createResult.getId());
    List<Record> records = dataApi.query(queryString).getRecords();
    LOGGER.info("Creating content Link");
    Record contentLink =
        dataApi
            .newRecordBuilder("ContentDocumentLink")
            .withField("ContentDocumentId", records.get(0).getStringField("Id").get())
            .withField("LinkedEntityId", event.getData().getConsentId())
            .withField("ShareType", "V")
            .withField("Visibility", "AllUsers")
            .build();

    RecordModificationResult createResult1 = dataApi.create(contentLink);
    LOGGER.info("Content Link Done");


       List<Account> accounts = new ArrayList<>();
    

       return new FunctionOutput(accounts);*/
  }
}
