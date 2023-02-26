public class FunctionInvocationPattern {
    
    
    
    
    
    
    Public Static void SynchronousCalling(String Name,String Description,String accId,String accToDelete){
        System.debug('Calling Function Sysnchronousaly');
        
        Input inp =New Input(Name,Description,accId,accToDelete);
        
        functions.Function accountFunction = functions.Function.get('first.unitofworkjava');
		functions.FunctionInvocation invocation = accountFunction.invoke(JSON.serialize(inp));
		String jsonResponse = invocation.getResponse();
		System.debug('Got response ' + jsonResponse);
        
    }
    
    Public Static void AsynchronousCalling(){
        
        functions.Function myFunction = functions.Function.get('first.firstfun');
        // Pass a callback to Function.invoke to perform an asynchronous invocation.
        functions.FunctionInvocation invocation = myFunction.invoke('{}', new functionCallbackhandler());
        
    }
    
    public class functionCallbackhandler implements functions.FunctionCallback {
        public void handleResponse(functions.FunctionInvocation result) {
            // Handle result of function invocation
            String jsonResponse = result.getResponse();
            System.debug('Got response ' + jsonResponse);

            // Log error, if applicable
            if (result.getStatus() == functions.FunctionInvocationStatus.ERROR) {
                functions.FunctionInvocationError resultError = result.getError();
                if (resultError != null) {
                    System.debug('Error type: ' + resultError.getType());
                    System.debug('Error message: ' + resultError.getMessage());
                } else {
                    System.debug('Error: UNKNOWN');
                }
                return;
            }
            // Successful response, deserialize the response to an Apex object.
            JSONParser parser = JSON.createParser(jsonResponse);
            Response response = (Response)parser.readValueAs(Response.class);
            System.debug('Response is '+response);
            
            
        }
    }
    
    public class Response {
        public List<Account> accounts;
    }
    
    public class Input {
        public String name;
        public String description;
         public String accId;
        public String accToDelete;
        public Input(String name,String description,String accId,String accToDelete){
            this.name=name;
            this.description=description;
            this.accId=accId;
            this.accToDelete=accToDelete;
            
        }
        
    }
    

}