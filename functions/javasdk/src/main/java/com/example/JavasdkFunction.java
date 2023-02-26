package com.example;

import com.salesforce.functions.jvm.sdk.Context;
import com.salesforce.functions.jvm.sdk.InvocationEvent;
import com.salesforce.functions.jvm.sdk.SalesforceFunction;
import com.salesforce.functions.jvm.sdk.data.Record;
import com.salesforce.functions.jvm.sdk.data.DataApi;
import com.salesforce.functions.jvm.sdk.data.RecordQueryResult;
import com.salesforce.functions.jvm.sdk.data.RecordWithSubQueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe JavasdkFunction here.
 */
public class JavasdkFunction implements SalesforceFunction<FunctionInput, FunctionOutput> {
  private static final Logger LOGGER = LoggerFactory.getLogger(JavasdkFunction.class);

  @Override
  public FunctionOutput apply(InvocationEvent<FunctionInput> event, Context context)
      throws Exception {
    
    DataApi dataApi = context.getOrg().get().getDataApi();
    List<Account> accounts = new ArrayList<>();

    String query ="SELECT Id, Name , Description FROM Account";
    RecordQueryResult rqr = dataApi.query(query);    

    LOGGER.info("Function successfully queried {} account records!", rqr.getTotalSize());
    if (rqr != null && rqr.getTotalSize() > 0){

      
        try{
       List<RecordWithSubQueryResults> lst = rqr.getRecords();
       while (lst.size() > 0) {
        LOGGER.info("Data Exist---"+lst.size());
        for (Record record : lst) {

            String id = record.getStringField("Id").get();
            String name = record.getStringField("Name").isPresent() ? record.getStringField("Name").get() : "";
            String description = record.getStringField("Description").get();
            
            accounts.add(new Account(id, name, description));
        }
        rqr = dataApi.queryMore(rqr);
        lst = rqr.getRecords();
       }
      }
      catch(Exception e){
          LOGGER.info("Exception Came"+e.getMessage());
      }


    }
    
     return new FunctionOutput(accounts);
  }
}
