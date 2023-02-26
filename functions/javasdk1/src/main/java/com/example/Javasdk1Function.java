package com.example;

import com.salesforce.functions.jvm.sdk.Context;
import com.salesforce.functions.jvm.sdk.InvocationEvent;
import com.salesforce.functions.jvm.sdk.SalesforceFunction;
import com.salesforce.functions.jvm.sdk.data.DataApi;
import com.salesforce.functions.jvm.sdk.data.Record;
import com.salesforce.functions.jvm.sdk.data.RecordModificationResult;
import com.salesforce.functions.jvm.sdk.data.RecordWithSubQueryResults;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Describe Javasdk1Function here.
 */
public class Javasdk1Function implements SalesforceFunction<FunctionInput, FunctionOutput> {
  private static final Logger LOGGER = LoggerFactory.getLogger(Javasdk1Function.class);

  @Override
  public FunctionOutput apply(InvocationEvent<FunctionInput> event, Context context)
      throws Exception {
 List<Account> accounts = new ArrayList<>();
  LOGGER.info("Function Started");

   try{
   String accountName = event.getData().getName();
   String description = event.getData().getDescription();
   String accId = event.getData().getAccId();
   String accToDelete = event.getData().getAccToDelete();

    DataApi dataApi = context.getOrg().get().getDataApi();

    Record account =
        dataApi
            .newRecordBuilder("Account")
            .withField("Name", accountName)
            .withField("Description", description)
            .build();

    RecordModificationResult createResult = dataApi.create(account);
    
    Record account1 =
        dataApi
            .newRecordBuilder("Account")
            .withField("Id", accId)
            .withField("Description", "Test Description")
            .build();

    RecordModificationResult UpdateResult = dataApi.update(account1);


    dataApi.delete("Account",accToDelete);

    String query =
        String.format("SELECT Id, Name FROM Account WHERE Id = '%s'", createResult.getId());
     List<RecordWithSubQueryResults> records = dataApi.query(query).getRecords();

    LOGGER.info("Function successfully queried {} account records!", records.size());



   
    for (Record record : records) {
      String id = record.getStringField("Id").get();
      String name = record.getStringField("Name").get();
      accounts.add(new Account(id, name));
    }
   }
   catch(Exception e){
          LOGGER.info("Exception Came"+e.getMessage());
      }

    return new FunctionOutput(accounts);
  }
}
