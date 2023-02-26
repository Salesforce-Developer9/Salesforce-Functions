package com.example;

public class FunctionInput {
  private String name;
  private String description;
  private String accId;
  private String accToDelete;
  

  public FunctionInput() {}

  public FunctionInput(String name, String description,String accId,String accToDelete) {
    this.name = name;
    this.description = description;
    this.accId=accId;
    this.accToDelete=accToDelete;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }
   public String getAccId() {
    return this.accId;
  }
  public String getAccToDelete() {
    return this.accToDelete;
  }
}