@ValidateInfoWithDB
Feature: Validate project page info with DB

  @QisheCourtyard
  Scenario: validate qishe project info with db
    Given project title "Qishe Courtyard / ARCHSTUDIO" exists
    Then check if title exists in db
    Then check if architect exists in db
    Then check if year exists in db
    And dropdown is clicked
    Then check if city exists in db
    Then check if country exists in db

#  @SunflowerHouse
#  Scenario: validate SunflowerHouse page info with db
#    Given sf title exists in db
#    Then check if sf architect exists in db
#    Then check if sf year exists in db
#    And dropdown sf is clicked
#    Then check if sf city exists in db
#    Then check if sf country exists in db

