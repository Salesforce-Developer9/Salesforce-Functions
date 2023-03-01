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
 * Describe RelatedrecordsjavaFunction here.
 */
public class RelatedrecordsjavaFunction implements SalesforceFunction<FunctionInput, FunctionOutput> {
  private static final Logger LOGGER = LoggerFactory.getLogger(RelatedrecordsjavaFunction.class);

  @Override
  public FunctionOutput apply(InvocationEvent<FunctionInput> event, Context context)
      throws Exception {

    List<RecordWithSubQueryResults> records =
        context.getOrg().get().getDataApi().query("SELECT Id, Name,(SELECT Id, Name from Contacts) FROM Account where id = \"0012w00001L9qFKAAZ\"").getRecords();

    LOGGER.info("Function successfully queried {} account records!", records.size());

    List<Account> accounts = new ArrayList<>();
    for (Record record : records) {
        LOGGER.info("Processing Account");
      String id = record.getStringField("Id").get();
      String name = record.getStringField("Name").get();
      accounts.add(new Account(id, name));
      LOGGER.info("getting contacts of the Account");
      RecordQueryResult contacts =  record.getSubQueryResult("Contacts").get();
      LOGGER.info("the Account Have {} Contacts records!", contacts.getTotalSize());
      /*for (Record record : contacts) {

      }*/

    }

    return new FunctionOutput(accounts);
  }
}
