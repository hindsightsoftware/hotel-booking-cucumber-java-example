@loyalty
Feature: Reward frequent customers

  @HOTEL-13 @MANUAL
  Scenario: Gold member checks in and a superior room is available
    Given a gold loyalty guest with a standard room reservation
    And there are superior rooms available for the length of their stay
    When the guest checks in
    Then the guest should be upgraded to a superior room

  @HOTEL-13
  Scenario Outline: Silver member checks in 
    Given a silver loyalty guest with a standard room reservation
    And there are < room > rooms available for the length of their stay
    And the user clicks a button
    And another step
    And another step
    When a step
    Given A step
    
    Examples:
      | room                | col 2 |
      | this is really wide |       |
      |                     |       |
