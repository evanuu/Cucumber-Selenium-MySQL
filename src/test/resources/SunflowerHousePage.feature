@ValidateInfoWithDB
Feature: Validate project page info with DB

  @SunflowerHouse
  Scenario: validate SunflowerHouse page info with db
    Given sf title exists in db
    Then check if sf architect exists in db
    Then check if sf year exists in db
    And dropdown sf is clicked
    Then check if sf city exists in db
    Then check if sf country exists in db