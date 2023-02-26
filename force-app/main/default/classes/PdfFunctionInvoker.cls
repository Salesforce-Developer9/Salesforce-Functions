public class PdfFunctionInvoker {
public static void invoke() {
      System.debug('Invoking myfunction');
      ContentVersion cv = [SELECT Id, ContentDocumentId, VersionData FROM ContentVersion where ContentDocumentId = '0692w00000Ovj8HAAR'];
        Blob data=cv.VersionData;
        String st=EncodingUtil.Base64Encode(data);
        input inp = new input(st,'Test Account','03-02-2023');
        functions.Function myFunction = functions.Function.get('first.pdffunjava');
        functions.FunctionInvocation invocation = myFunction.invoke(Json.serialize(inp));
        String jsonResponse = invocation.getResponse();

        System.debug('Response from myfunction ' + jsonResponse);
                JSONParser parser = JSON.createParser(jsonResponse);
                Response response = (Response)parser.readValueAs(Response.class);
                System.debug('Response came');
                System.debug(response.content);
      ContentVersion conver = new ContentVersion();
        conver.Title = 'SignedFile';
        conver.PathOnClient = 'SignedFile' + '.pdf';
      conver.VersionData = EncodingUtil.base64Decode(response.content);//blob.valueOf(response.content);
      insert conver;
      
       Id contentDocumentId = [SELECT ContentDocumentId FROM ContentVersion WHERE Id = :conver.Id].ContentDocumentId;
        
        // Link the content document to sObject record in this case it is account 
        ContentDocumentLink condocLink = new ContentDocumentLink();
        condocLink.ContentDocumentId = contentDocumentId;
        condocLink.LinkedEntityId = '0012w00001L9qFKAAZ';
        condocLink.ShareType = 'V';
        insert condocLink;
      
    
  }
    
    Public class input{
        Public String pdfData;
        Public String sign;
        Public String signDate;
        
        public input(String pdfData,String sign,String signDate){
            this.pdfData=pdfData;
            this.sign=sign;
            this.signDate=signDate;
        }
    }
    public class Response {
        public String content;
    }
    
    
    
}