package com.example;

public class FunctionInput {
    private String pdfData;
    private String sign;
    private String signDate;
    

    public FunctionInput() {}
    
    public FunctionInput (String pdfData,String sign,String signDate){
        this.pdfData=pdfData;
        this.sign=sign;
       this.signDate=signDate;

    }

    public String getPdfData() {
        return this.pdfData;
      }
      public String getSign() {
        return this.sign;
      }
      public String getSignDate() {
        return this.signDate;
      }
    
}
