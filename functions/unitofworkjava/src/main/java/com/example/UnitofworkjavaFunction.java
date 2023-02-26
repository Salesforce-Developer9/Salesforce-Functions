package com.example;

import com.salesforce.functions.jvm.sdk.Context;
import com.salesforce.functions.jvm.sdk.InvocationEvent;
import com.salesforce.functions.jvm.sdk.SalesforceFunction;
import com.salesforce.functions.jvm.sdk.data.DataApi;
import com.salesforce.functions.jvm.sdk.data.Record;
import com.salesforce.functions.jvm.sdk.data.RecordModificationResult;
import com.salesforce.functions.jvm.sdk.data.RecordWithSubQueryResults;
import com.salesforce.functions.jvm.sdk.data.ReferenceId;
import com.salesforce.functions.jvm.sdk.data.builder.UnitOfWorkBuilder;
import java.util.ArrayList;
import java.util.Map;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Describe UnitofworkjavaFunction here.
 */
public class UnitofworkjavaFunction implements SalesforceFunction<FunctionInput, FunctionOutput> {
  private static final Logger LOGGER = LoggerFactory.getLogger(UnitofworkjavaFunction.class);

  @Override
  public FunctionOutput apply(InvocationEvent<FunctionInput> event, Context context)
      throws Exception {

    LOGGER.info("Function Started");
List<Account> accounts = new ArrayList<>();
   try{
   String accountName = event.getData().getName();
   String description = event.getData().getDescription();
   String accId = event.getData().getAccId();
   String accToDelete = event.getData().getAccToDelete();

    DataApi dataApi = context.getOrg().get().getDataApi();

    UnitOfWorkBuilder unitOfWork = dataApi.newUnitOfWorkBuilder();
     Record account =
        dataApi
            .newRecordBuilder("Account")
            .withField("Name", accountName)
            .withField("Description", description)
            .build();

    ReferenceId accountRefId = unitOfWork.registerCreate(account);

    Record account1 =
        dataApi
            .newRecordBuilder("Account")
            .withField("Id", accId)
            .withField("Description", "Test Description")
            .build();

    unitOfWork.registerUpdate(account1); 

    unitOfWork.registerDelete("Account",accToDelete);        

     for (int i = 0; i < 5; i++) {

      Record contact =
          
          dataApi
            .newRecordBuilder("Contact")
            .withField("FirstName", "Test")
            .withField("LastName", "Contact"+i)
            .withField("AccountId", accountRefId)
            .build();
         unitOfWork.registerCreate(contact);
  
      }

       Map<ReferenceId, RecordModificationResult> result =
        dataApi.commitUnitOfWork(unitOfWork.build());

    LOGGER.info("Function successfully commited UoW with {} affected records!", result.size());
     
    String query =
        String.format("SELECT Id, Name FROM Account WHERE Id = '%s'", result.get(accountRefId).getId());
     List<RecordWithSubQueryResults> records = dataApi.query(query).getRecords(); 

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
