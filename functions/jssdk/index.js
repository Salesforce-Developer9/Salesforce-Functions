/**
 * Describe Jssdk here.
 *
 * The exported method is the entry point for your code when the function is invoked.
 *
 * Following parameters are pre-configured and provided to your function on execution:
 * @param event: represents the data associated with the occurrence of an event, and
 *                 supporting metadata about the source of that occurrence.
 * @param context: represents the connection to Functions and your Salesforce org.
 * @param logger: logging handler used to capture application logs and trace specifically
 *                 to a given execution of a function.
 */
export default async function (event, context, logger) {
  logger.info("Invoking function ");

  var results = await context.org.dataApi.query('SELECT Id, Name, (SELECT Id,Name FROM Contacts) FROM Account ');
    
    logger.info(JSON.stringify(results));
    logger.info('Parsing Starts');
    let response =[];
    try {
    let recordsToProcess = results.records;
   
    while (recordsToProcess.length > 0) {
      logger.info('Processing Batch with Size '+recordsToProcess.length); 
      recordsToProcess.forEach(function(item){
        let resp={
          "AccName":item.fields.name,
          "AccId":item.fields.id
        };
        
        if(!isEmpty(item.subQueryResults) ){
          item.subQueryResults.contacts.records.forEach(function(cont){
            resp['contactName']=cont.fields.name;
          })
          
        }
        response.push(resp);

    });
   
    results = await context.org.dataApi.queryMore(results);
    recordsToProcess=results.records;

    }
  } catch (err) {
    const errorMessage = `Failed to . Root Cause : ${err.message}`;
    logger.error(errorMessage);
  }

    
   /*  */
    
    return response;
}

function isEmpty(obj) {
  return Object.keys(obj).length === 0;
}