package com.example;

import com.salesforce.functions.jvm.sdk.Context;
import com.salesforce.functions.jvm.sdk.InvocationEvent;
import com.salesforce.functions.jvm.sdk.SalesforceFunction;
import com.salesforce.functions.jvm.sdk.data.Record;
import com.salesforce.functions.jvm.sdk.data.RecordWithSubQueryResults;
import com.salesforce.functions.jvm.sdk.data.RecordQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe RelatedrecordsFunction here.
 */
public class RelatedrecordsFunction implements SalesforceFunction<FunctionInput, FunctionOutput> {
  private static final Logger LOGGER = LoggerFactory.getLogger(RelatedrecordsFunction.class);

  @Override
  public FunctionOutput apply(InvocationEvent<FunctionInput> event, Context context)
      throws Exception {
 LOGGER.info("function started");
 List<Account> accounts = new ArrayList<>();
 try{

 
    List<RecordWithSubQueryResults> records =
        context.getOrg().get().getDataApi().query("SELECT Id, Name, (SELECT Id, Name from Contacts) FROM Account ")
        .getRecords();

    LOGGER.info("Function successfully queried {} account records!", records.size());

    
    for (RecordWithSubQueryResults record : records) {
      LOGGER.info("Processing Account");
      String id = record.getStringField("Id").get();
      String name = record.getStringField("Name").get();
     
      LOGGER.info("Account name"+name);
      accounts.add(new Account(id, name));
      LOGGER.info("getting contacts of the Account");
      List<RecordWithSubQueryResults> contacts =  record.getSubQueryResult("Contacts").isPresent() ? record.getSubQueryResult("Contacts").get().getRecords():null;
      
      LOGGER.info("the Account Have {} Contacts records!", contacts.size());
      for (Record rec : contacts) {
          String conid = rec.getStringField("Id").get();
          String conname = rec.getStringField("Name").get();
          LOGGER.info("Contact is "+conname); 
      }

    }
 }
 catch(Exception e){
          LOGGER.info("Exception Came"+e.getMessage());
      }

    return new FunctionOutput(accounts);
  }
}
